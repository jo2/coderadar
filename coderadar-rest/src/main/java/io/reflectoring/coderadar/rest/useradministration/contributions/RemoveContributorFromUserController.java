package io.reflectoring.coderadar.rest.useradministration.contributions;

import io.reflectoring.coderadar.rest.AbstractBaseController;
import io.reflectoring.coderadar.useradministration.port.driver.contributions.RemoveContributorFromUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RemoveContributorFromUserController implements AbstractBaseController {
    private final RemoveContributorFromUserUseCase removeContributorFromUserUseCase;

    @PostMapping(path = "/users/{userId}/contributors/{contributorId}/remove")
    public ResponseEntity<HttpStatus> removeContributorToUser(
            @PathVariable long contributorId,
            @PathVariable long userId) {
        removeContributorFromUserUseCase.removeContributor(contributorId, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
