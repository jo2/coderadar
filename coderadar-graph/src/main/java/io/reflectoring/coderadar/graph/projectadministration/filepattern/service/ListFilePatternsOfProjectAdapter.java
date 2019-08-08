package io.reflectoring.coderadar.graph.projectadministration.filepattern.service;

import io.reflectoring.coderadar.graph.projectadministration.filepattern.FilePatternMapper;
import io.reflectoring.coderadar.graph.projectadministration.filepattern.repository.ListFilePatternsOfProjectRepository;
import io.reflectoring.coderadar.graph.projectadministration.project.repository.GetProjectRepository;
import io.reflectoring.coderadar.projectadministration.ProjectNotFoundException;
import io.reflectoring.coderadar.projectadministration.domain.FilePattern;
import io.reflectoring.coderadar.projectadministration.port.driven.filepattern.ListFilePatternsOfProjectPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ListFilePatternsOfProjectAdapter implements ListFilePatternsOfProjectPort {
  private ListFilePatternsOfProjectRepository listFilePatternsOfProjectRepository;
  private final FilePatternMapper filePatternMapper = new FilePatternMapper();
  private final GetProjectRepository getProjectRepository;

  @Autowired
  public ListFilePatternsOfProjectAdapter(
      ListFilePatternsOfProjectRepository listFilePatternsOfProjectRepository,
      GetProjectRepository getProjectRepository) {
    this.listFilePatternsOfProjectRepository = listFilePatternsOfProjectRepository;
    this.getProjectRepository = getProjectRepository;
  }

  @Override
  public List<FilePattern> listFilePatterns(Long projectId) throws ProjectNotFoundException {
    getProjectRepository
        .findById(projectId)
        .orElseThrow(() -> new ProjectNotFoundException(projectId));
    return new ArrayList<>(
        filePatternMapper.mapNodeEntities(
            listFilePatternsOfProjectRepository.findByProjectId(projectId)));
  }
}
