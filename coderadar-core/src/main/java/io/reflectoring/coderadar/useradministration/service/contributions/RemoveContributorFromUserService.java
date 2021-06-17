package io.reflectoring.coderadar.useradministration.service.contributions;

import io.reflectoring.coderadar.contributor.ContributorByUserNotFoundException;
import io.reflectoring.coderadar.contributor.ContributorNotFoundException;
import io.reflectoring.coderadar.contributor.port.driven.GetContributorPort;
import io.reflectoring.coderadar.useradministration.UserNotFoundException;
import io.reflectoring.coderadar.useradministration.port.driven.GetUserPort;
import io.reflectoring.coderadar.useradministration.port.driven.RemoveContributorFromUserPort;
import io.reflectoring.coderadar.useradministration.port.driver.contributions.RemoveContributorFromUserUseCase;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RemoveContributorFromUserService implements RemoveContributorFromUserUseCase {

  private final GetUserPort getUserPort;
  private final GetContributorPort getContributorPort;
  private final RemoveContributorFromUserPort removeContributorFromUserPort;

  private static final Logger logger =
      LoggerFactory.getLogger(RemoveContributorFromUserService.class);

  @Override
  public void removeContributor(Long contributorId, Long userId) {
    if (!getContributorPort.existsById(contributorId)) {
      throw new ContributorNotFoundException(contributorId);
    }
    if (!getUserPort.existsById(userId)) {
      throw new UserNotFoundException(userId);
    }
    if (!getContributorPort.existsByIdAndUserId(contributorId, userId)) {
      throw new ContributorByUserNotFoundException(contributorId, userId);
    }
    removeContributorFromUserPort.removeContributor(contributorId, userId);
    logger.info("Removed contributor with id: {} from user with id: {}", userId, contributorId);
  }
}
