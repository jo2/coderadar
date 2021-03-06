package io.reflectoring.coderadar.graph.projectadministration.project.adapter;

import io.reflectoring.coderadar.graph.contributor.repository.ContributorRepository;
import io.reflectoring.coderadar.graph.projectadministration.project.repository.ProjectRepository;
import io.reflectoring.coderadar.projectadministration.port.driven.project.DeleteProjectPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteProjectAdapter implements DeleteProjectPort {

  private final ProjectRepository projectRepository;
  private final ContributorRepository contributorRepository;

  @Override
  public void delete(long projectId) {
    projectRepository.setBeingDeleted(projectId, true);

    /*
     * The empty while loops are necessary because only 10000 entities can be deleted at a time.
     * @see ProjectRepository#deleteProjectFindings(Long)
     * @see ProjectRepository#deleteProjectMetrics(Long)
     */
    while (projectRepository.deleteProjectMetrics(projectId) > 0) ;
    while (projectRepository.deleteProjectFiles(projectId) > 0) ;

    projectRepository.deleteProjectModules(projectId);
    projectRepository.deleteProjectBranches(projectId);
    projectRepository.deleteProjectCommits(projectId);
    projectRepository.deleteProjectConfiguration(projectId);
    projectRepository.deleteContributorRelationships(projectId);
    contributorRepository.deleteContributorsWithoutProjects();
    projectRepository.deleteById(projectId);
  }

  @Override
  public void deleteBranchesFilesAndCommits(long projectId) {
    while (projectRepository.deleteProjectFiles(projectId) > 0) ;
    projectRepository.deleteProjectModules(projectId);
    projectRepository.deleteProjectBranches(projectId);
    projectRepository.deleteProjectCommits(projectId);
  }
}
