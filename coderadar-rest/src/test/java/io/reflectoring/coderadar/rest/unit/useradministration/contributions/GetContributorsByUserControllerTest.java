package io.reflectoring.coderadar.rest.unit.useradministration.contributions;

import static org.mockito.Mockito.mock;

import io.reflectoring.coderadar.domain.Contributor;
import io.reflectoring.coderadar.rest.domain.GetContributorResponse;
import io.reflectoring.coderadar.rest.unit.UnitTestTemplate;
import io.reflectoring.coderadar.rest.useradministration.contributions.GetContributorsByUserController;
import io.reflectoring.coderadar.useradministration.port.driver.contributions.GetContributorsByUserUseCase;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class GetContributorsByUserControllerTest extends UnitTestTemplate {
  private final GetContributorsByUserUseCase getContributorsByUserUseCase =
      mock(GetContributorsByUserUseCase.class);
  private final GetContributorsByUserController testController =
      new GetContributorsByUserController(getContributorsByUserUseCase);

  @Test
  void testGetContributorsByUser() {
    Mockito.when(getContributorsByUserUseCase.getContributorsByUser(1L))
        .thenReturn(
            Arrays.asList(
                new Contributor().setDisplayName("test").setUserId(1L),
                new Contributor().setDisplayName("test 2").setUserId(1L)));

    ResponseEntity<List<GetContributorResponse>> response =
        testController.getContributorsByUser(1L);
    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertNotNull(response.getBody());
    Assertions.assertEquals(2, response.getBody().size());
    Assertions.assertEquals("test", response.getBody().get(0).getDisplayName());
    Assertions.assertEquals(1L, response.getBody().get(0).getId());
    Assertions.assertEquals(1L, response.getBody().get(1).getId());
  }
}
