package io.reflectoring.coderadar.rest.useradministration.contributions;

import io.reflectoring.coderadar.rest.AbstractBaseController;
import io.reflectoring.coderadar.useradministration.port.driver.contributions.AddContributorToUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AddContributorToUserController implements AbstractBaseController {
    private final AddContributorToUserUseCase addContributorToUserUseCase;

    @PostMapping(path = "/users/{userId}/contributors/{contributorId}/add")
    public ResponseEntity<HttpStatus> addContributorToUser(
            @PathVariable long contributorId,
            @PathVariable long userId) {
        addContributorToUserUseCase.addContributor(contributorId, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
