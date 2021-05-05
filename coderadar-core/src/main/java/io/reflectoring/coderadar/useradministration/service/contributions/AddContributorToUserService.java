package io.reflectoring.coderadar.useradministration.service.contributions;

import io.reflectoring.coderadar.contributor.ContributorNotFoundException;
import io.reflectoring.coderadar.contributor.port.driven.GetContributorPort;
import io.reflectoring.coderadar.useradministration.UserNotFoundException;
import io.reflectoring.coderadar.useradministration.port.driven.AddContributorToUserPort;
import io.reflectoring.coderadar.useradministration.port.driven.GetUserPort;
import io.reflectoring.coderadar.useradministration.port.driver.contributions.AddContributorToUserUseCase;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddContributorToUserService implements AddContributorToUserUseCase {

    private final GetUserPort getUserPort;
    private final GetContributorPort getContributorPort;
    private final AddContributorToUserPort addContributorToUserPort;

    private static final Logger logger = LoggerFactory.getLogger(AddContributorToUserService.class);

    @Override
    public void addContributor(Long contributorId, Long userId) {
        if (!getContributorPort.existsById(contributorId)) {
            throw new ContributorNotFoundException(contributorId);
        }
        if (!getUserPort.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
        addContributorToUserPort.addContributor(contributorId, userId);
        logger.info(
                "Added contributor with id: {} to user with id: {}",
                userId,
                contributorId);
    }
}
