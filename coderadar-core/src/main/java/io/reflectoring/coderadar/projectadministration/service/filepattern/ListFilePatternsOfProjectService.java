package io.reflectoring.coderadar.projectadministration.service.filepattern;

import io.reflectoring.coderadar.domain.FilePattern;
import io.reflectoring.coderadar.projectadministration.ProjectNotFoundException;
import io.reflectoring.coderadar.projectadministration.port.driven.filepattern.ListFilePatternsOfProjectPort;
import io.reflectoring.coderadar.projectadministration.port.driven.project.GetProjectPort;
import io.reflectoring.coderadar.projectadministration.port.driver.filepattern.get.ListFilePatternsOfProjectUseCase;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ListFilePatternsOfProjectService implements ListFilePatternsOfProjectUseCase {

  private final ListFilePatternsOfProjectPort port;
  private final GetProjectPort getProjectPort;

  @Override
  public List<FilePattern> listFilePatterns(long projectId) {
    if (getProjectPort.existsById(projectId)) {
      return port.listFilePatterns(projectId);
    } else {
      throw new ProjectNotFoundException(projectId);
    }
  }
}
