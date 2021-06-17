package io.reflectoring.coderadar.rest.useradministration.contributions;

import static io.reflectoring.coderadar.rest.GetContributorResponseMapper.mapContributors;

import io.reflectoring.coderadar.rest.domain.GetContributorResponse;
import io.reflectoring.coderadar.useradministration.port.driver.contributions.GetContributorsByUserUseCase;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GetContributorsByUserController {
  private final GetContributorsByUserUseCase getContributorsByUserUseCase;

  @GetMapping(path = "/users/{userId}/contributors")
  public ResponseEntity<List<GetContributorResponse>> getContributorsByUser(
      @PathVariable long userId) {
    return ResponseEntity.ok(
        mapContributors(getContributorsByUserUseCase.getContributorsByUser(userId)));
  }
}
