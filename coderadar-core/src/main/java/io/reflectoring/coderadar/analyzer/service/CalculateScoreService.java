package io.reflectoring.coderadar.analyzer.service;

import io.reflectoring.coderadar.analyzer.domain.AnalyzeCommitDto;
import io.reflectoring.coderadar.analyzer.domain.MetricValue;
import io.reflectoring.coderadar.contributor.ContributorNotFoundException;
import io.reflectoring.coderadar.contributor.port.driven.GetContributorPort;
import io.reflectoring.coderadar.contributor.port.driven.UpdateContributorPort;
import io.reflectoring.coderadar.contributor.port.driver.UpdateContributorCommand;
import io.reflectoring.coderadar.domain.Commit;
import io.reflectoring.coderadar.domain.Contributor;
import io.reflectoring.coderadar.projectadministration.CommitNotFoundException;
import io.reflectoring.coderadar.query.port.driven.GetCommitPort;
import io.reflectoring.coderadar.useradministration.port.driver.game.AddPointsToUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CalculateScoreService {

    private final AddPointsToUserUseCase addPointsToUserUseCase;
    private final GetCommitPort getCommitPort;
    private final GetContributorPort getContributorPort;
    private final UpdateContributorPort updateContributorPort;

    public void calculateScoreForCommit(AnalyzeCommitDto commitDto, List<MetricValue> metricValues) {
        if (!getCommitPort.existsById(commitDto.getId())) {
            throw new CommitNotFoundException(commitDto.getId());
        }
        Commit commit = getCommitPort.getCommitById(commitDto.getId());
        if (!getContributorPort.existsByAuthorName(commit.getAuthor())) {
            throw new ContributorNotFoundException(commit.getAuthor());
        }
        Contributor contributor = getContributorPort.getByAuthorName(commit.getAuthor());
        // TODO calculate score
        int score = 21;




        updateContributorPort.updateContributor(contributor.getId(),
                new UpdateContributorCommand(contributor.getDisplayName(), contributor.getPointsOverAll() + score));
        // if the contributor is assigned to a user, calculate the score
        if (contributor.getUserId() != null) {
            addPointsToUserUseCase.addPointsToUser(contributor.getUserId(), score);
        }
    }
}
