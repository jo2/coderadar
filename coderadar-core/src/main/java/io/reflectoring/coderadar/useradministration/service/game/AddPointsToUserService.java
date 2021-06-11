package io.reflectoring.coderadar.useradministration.service.game;

import io.reflectoring.coderadar.domain.Level;
import io.reflectoring.coderadar.domain.User;
import io.reflectoring.coderadar.useradministration.port.driven.GetUserPort;
import io.reflectoring.coderadar.useradministration.port.driven.UpdateUserPort;
import io.reflectoring.coderadar.useradministration.port.driver.game.AddPointsToUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddPointsToUserService implements AddPointsToUserUseCase {

    private final UpdateUserPort updateUserScorePort;
    private final GetUserPort getUserPort;
    private final LevelConfig levelConfig;

    @Override
    public void addPointsToUser(Long userId, int points) {
        User user = getUserPort.getUser(userId);
        user.setPointsOverAll(user.getPointsOverAll() + points);
        Level level = levelConfig.getLevels().stream().filter(l -> l.getLevel() == user.getLevel()).findFirst().orElseThrow();
        if (user.getPointsOverAll() >= (level.getNeededPoints() + level.getPointsForLevelUp())) {
            // level up
            user.setLevel(level.getLevel() + 1);
            user.setPointsInLevel(points - level.getNeededPoints());
        } else {
            user.setPointsInLevel(user.getPointsInLevel() + points);
        }
        updateUserScorePort.updateUser(user);
    }
}
