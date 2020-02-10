package io.reflectoring.coderadar.graph.query.adapter;

import io.reflectoring.coderadar.graph.analyzer.repository.CommitRepository;
import io.reflectoring.coderadar.graph.projectadministration.branch.repository.BranchRepository;
import io.reflectoring.coderadar.graph.projectadministration.domain.CommitEntity;
import io.reflectoring.coderadar.graph.projectadministration.domain.FileEntity;
import io.reflectoring.coderadar.graph.projectadministration.domain.FileToCommitRelationshipEntity;
import io.reflectoring.coderadar.graph.projectadministration.project.adapter.CommitBaseDataMapper;
import io.reflectoring.coderadar.projectadministration.domain.*;
import io.reflectoring.coderadar.query.port.driven.GetCommitsInProjectPort;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class GetCommitsInProjectAdapter implements GetCommitsInProjectPort {
  private final CommitRepository commitRepository;
  private final BranchRepository branchRepository;
  private final CommitBaseDataMapper commitBaseDataMapper = new CommitBaseDataMapper();

  public GetCommitsInProjectAdapter(
      CommitRepository commitRepository, BranchRepository branchRepository) {
    this.commitRepository = commitRepository;
    this.branchRepository = branchRepository;
  }

  private List<Commit> mapCommitEntities(List<CommitEntity> commitEntities) {
    IdentityHashMap<CommitEntity, Commit> walkedCommits =
        new IdentityHashMap<>(commitEntities.size());
    IdentityHashMap<FileEntity, File> walkedFiles = new IdentityHashMap<>(commitEntities.size());
    List<Commit> result = new ArrayList<>(commitEntities.size());
    for (CommitEntity commitEntity : commitEntities) {
      Commit commit = walkedCommits.get(commitEntity);
      if (commit == null) {
        commit = commitBaseDataMapper.mapNodeEntity(commitEntity);
      }
      List<Commit> parents = new ArrayList<>(commitEntity.getParents().size());
      for (CommitEntity parent : commitEntity.getParents()) {
        Commit parentCommit = walkedCommits.get(parent);
        if (parentCommit == null) {
          parentCommit = commitBaseDataMapper.mapNodeEntity(parent);
          parentCommit.setTouchedFiles(getFiles(parent.getTouchedFiles(), walkedFiles, true));
          result.add(parentCommit);
        }
        parents.add(parentCommit);
      }
      commit.setParents(parents);
      commit.setTouchedFiles(getFiles(commitEntity.getTouchedFiles(), walkedFiles, true));
      walkedCommits.put(commitEntity, commit);
      result.add(commit);
    }
    return result;
  }

  @Override
  public List<Commit> getCommitsSortedByTimestampDescWithNoRelationships(
      Long projectId, String branch) {
    return getCommitsInBranch(projectId, branch);
  }

  public List<Commit> getSortedByTimestampAsc(Long projectId) {
    List<CommitEntity> commitEntities =
        commitRepository.findByProjectIdWithAllRelationshipsSortedByTimestampAsc(projectId);
    return mapCommitEntities(commitEntities);
  }

  /**
   * Returns all not yet analyzed commits in this project, that match the supplied file patterns
   *
   * @param projectId The id of the project.
   * @param filePatterns The patterns to use.
   * @return A list of commits with initialized FileToCommitRelationShips and no parents.
   */
  @Override
  public List<Commit> getNonAnalyzedSortedByTimestampAscWithNoParents(
      Long projectId, List<FilePattern> filePatterns, String branch) {
    // Map Ant-Patterns to RegEx
    List<String> includes = new ArrayList<>();
    List<String> excludes = new ArrayList<>();
    for (FilePattern filePattern : filePatterns) {
      if (filePattern.getInclusionType().equals(InclusionType.INCLUDE)) {
        includes.add(PatternUtil.toPattern(filePattern.getPattern()).toString());
      } else {
        excludes.add(PatternUtil.toPattern(filePattern.getPattern()).toString());
      }
    }
    List<CommitEntity> commitEntities =
        commitRepository.findByProjectIdWithFileAndParentRelationshipsSortedByTimestampAsc(
            projectId, includes, excludes);

    CommitEntity branchCommit = branchRepository.getCommitForBranch(projectId, branch);
    CommitEntity startCommit =
        commitEntities.stream()
            .filter(commitEntity -> commitEntity.getName().equals(branchCommit.getName()))
            .collect(Collectors.toList())
            .get(0);
    List<CommitEntity> result = new ArrayList<>();
    setCommitParents(startCommit, result);
    result.sort(Comparator.comparingLong(CommitEntity::getTimestamp));
    return mapCommitEntitiesNoParents(result);
  }

  private List<Commit> mapCommitEntitiesNoParents(List<CommitEntity> commitEntities) {
    List<Commit> commits = new ArrayList<>(commitEntities.size());
    IdentityHashMap<FileEntity, File> walkedFiles = new IdentityHashMap<>(commitEntities.size());
    for (CommitEntity commitEntity : commitEntities) {
      Commit commit = commitBaseDataMapper.mapNodeEntity(commitEntity);
      commit.setTouchedFiles(getFiles(commitEntity.getTouchedFiles(), walkedFiles, false));
      commits.add(commit);
    }
    return commits;
  }

  private List<FileToCommitRelationship> getFiles(
      List<FileToCommitRelationshipEntity> relationships,
      IdentityHashMap<FileEntity, File> walkedFiles,
      boolean createRels) {
    List<FileToCommitRelationship> fileToCommitRelationships =
        new ArrayList<>(relationships.size());

    for (FileToCommitRelationshipEntity fileToCommitRelationshipEntity : relationships) {
      FileToCommitRelationship fileToCommitRelationship = new FileToCommitRelationship();

      if (createRels) {
        fileToCommitRelationship.setOldPath(fileToCommitRelationshipEntity.getOldPath());
        fileToCommitRelationship.setChangeType(fileToCommitRelationshipEntity.getChangeType());
      }

      File file = walkedFiles.get(fileToCommitRelationshipEntity.getFile());
      if (file == null) {
        file = new File();
        file.setId(fileToCommitRelationshipEntity.getFile().getId());
        file.setPath(fileToCommitRelationshipEntity.getFile().getPath());
        walkedFiles.put(fileToCommitRelationshipEntity.getFile(), file);
      }
      fileToCommitRelationship.setFile(file);

      fileToCommitRelationships.add(fileToCommitRelationship);
    }
    return fileToCommitRelationships;
  }

  private List<Commit> getCommitsInBranch(Long projectId, String branch) {
    List<CommitEntity> commitsWithParents =
        commitRepository.findByProjectIdWithParentRelationships(projectId);

    CommitEntity branchCommit = branchRepository.getCommitForBranch(projectId, branch);

    List<CommitEntity> startCommitList =
        commitsWithParents.stream()
            .filter(commitEntity -> commitEntity.getName().equals(branchCommit.getName()))
            .collect(Collectors.toList());

    CommitEntity startCommit;
    if (startCommitList.isEmpty()) {
      return new ArrayList<>();
    } else {
      startCommit = startCommitList.get(0);
    }
    List<CommitEntity> result = new ArrayList<>();
    setCommitParents(startCommit, result);

    result.sort((t1, t2) -> -Long.compare(t1.getTimestamp(), t2.getTimestamp()));
    List<Commit> domainObjects = new ArrayList<>();
    for (CommitEntity commitEntity : result) {
      domainObjects.add(commitBaseDataMapper.mapNodeEntity(commitEntity));
    }
    return domainObjects;
  }

  private void setCommitParents(CommitEntity startCommit, List<CommitEntity> result) {
    result.add(startCommit);
    if (!startCommit.getParents().isEmpty()) {
      for (CommitEntity parent : startCommit.getParents()) {
        if (!result.contains(parent)) {
          setCommitParents(parent, result);
        }
      }
    }
  }
}
