package io.reflectoring.coderadar.projectadministration.project;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import io.reflectoring.coderadar.domain.Project;
import io.reflectoring.coderadar.projectadministration.port.driven.project.GetProjectPort;
import io.reflectoring.coderadar.projectadministration.service.project.GetProjectService;
import io.reflectoring.coderadar.useradministration.port.driven.GetUserPort;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetProjectServiceTest {

  @Mock private GetProjectPort getProjectPortMock;

  @Mock private GetUserPort getUserPort;

  private GetProjectService testSubject;

  @BeforeEach
  void setUp() {
    this.testSubject = new GetProjectService(getProjectPortMock, getUserPort);
  }

  @Test
  void returnsGetProjectResponseWithIdOne() {
    // given
    long projectId = 1L;
    String projectName = "project name";
    String vcsUrl = "http://valid.url";
    String vcsUsername = "username";
    String vcsPassword = "password";
    Date startDate = new Date();
    Date endDate = new Date();

    Project expectedResponse =
        new Project()
            .setId(projectId)
            .setName(projectName)
            .setVcsUrl(vcsUrl)
            .setVcsUsername(vcsUsername)
            .setVcsStart(startDate);

    when(getProjectPortMock.get(projectId)).thenReturn(expectedResponse);

    // when
    Project actualResponse = testSubject.get(1L);

    // then
    assertThat(actualResponse).isEqualTo(expectedResponse);
  }
}
