package io.reflectoring.coderadar.rest.unit.useradministration.contributions;

import io.reflectoring.coderadar.contributor.ContributorByUserNotFoundException;
import io.reflectoring.coderadar.contributor.ContributorNotFoundException;
import io.reflectoring.coderadar.rest.unit.UnitTestTemplate;
import io.reflectoring.coderadar.rest.useradministration.contributions.RemoveContributorFromUserController;
import io.reflectoring.coderadar.useradministration.UserNotFoundException;
import io.reflectoring.coderadar.useradministration.port.driver.contributions.RemoveContributorFromUserUseCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.mock;

class RemoveContributorFromUserControllerTest extends UnitTestTemplate {
    private final RemoveContributorFromUserUseCase removeContributorToUserUseCase =
            mock(RemoveContributorFromUserUseCase.class);
    private final RemoveContributorFromUserController testController =
            new RemoveContributorFromUserController(removeContributorToUserUseCase);

    @Test
    void testRemoveContributorFromUser() {
        ResponseEntity<HttpStatus> response =
                testController.removeContributorToUser(1L, 2L);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testRemoveContributorFromUserThrowsExceptionWhenUserNotFound() {
        Mockito.doThrow(UserNotFoundException.class)
                .when(removeContributorToUserUseCase)
                .removeContributor(1L, 2L);
        Assertions.assertThrows(
                UserNotFoundException.class,
                () -> testController.removeContributorToUser(1L, 2L));
    }

    @Test
    void testRemoveContributorFromUserThrowsExceptionWhenContributorNotFound() {
        Mockito.doThrow(ContributorNotFoundException.class)
                .when(removeContributorToUserUseCase)
                .removeContributor(1L, 2L);
        Assertions.assertThrows(
                ContributorNotFoundException.class,
                () -> testController.removeContributorToUser(1L, 2L));
    }

    @Test
    void testRemoveContributorFromUserThrowsExceptionWhenContributorByUserNotFound() {
        Mockito.doThrow(ContributorByUserNotFoundException.class)
                .when(removeContributorToUserUseCase)
                .removeContributor(1L, 2L);
        Assertions.assertThrows(
                ContributorByUserNotFoundException.class,
                () -> testController.removeContributorToUser(1L, 2L));
    }
}
