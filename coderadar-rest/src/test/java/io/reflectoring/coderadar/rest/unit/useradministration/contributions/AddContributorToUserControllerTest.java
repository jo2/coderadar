package io.reflectoring.coderadar.rest.unit.useradministration.contributions;

import static org.mockito.Mockito.mock;

import io.reflectoring.coderadar.contributor.ContributorNotFoundException;
import io.reflectoring.coderadar.rest.unit.UnitTestTemplate;
import io.reflectoring.coderadar.rest.useradministration.contributions.AddContributorToUserController;
import io.reflectoring.coderadar.useradministration.UserNotFoundException;
import io.reflectoring.coderadar.useradministration.port.driver.contributions.AddContributorToUserUseCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class AddContributorToUserControllerTest extends UnitTestTemplate {
  private final AddContributorToUserUseCase addContributorToUserUseCase =
      mock(AddContributorToUserUseCase.class);
  private final AddContributorToUserController testController =
      new AddContributorToUserController(addContributorToUserUseCase);

  @Test
  void testAddContributorToUser() {
    ResponseEntity<HttpStatus> response = testController.addContributorToUser(1L, 2L);
    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  void testAddContributorToUserThrowsExceptionWhenUserNotFound() {
    Mockito.doThrow(UserNotFoundException.class)
        .when(addContributorToUserUseCase)
        .addContributor(1L, 2L);
    Assertions.assertThrows(
        UserNotFoundException.class, () -> testController.addContributorToUser(1L, 2L));
  }

  @Test
  void testAddContributorToUserThrowsExceptionWhenContributorNotFound() {
    Mockito.doThrow(ContributorNotFoundException.class)
        .when(addContributorToUserUseCase)
        .addContributor(1L, 2L);
    Assertions.assertThrows(
        ContributorNotFoundException.class, () -> testController.addContributorToUser(1L, 2L));
  }
}
