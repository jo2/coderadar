package io.reflectoring.coderadar.useradministration.service.game;

import io.reflectoring.coderadar.IllegalLevelConfigurationException;
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
    Level level = getLevel(user.getLevel());

    if (level.getPointsForLevelUp() >= 0 && user.getPointsOverAll() > level.getNeededPoints() + level.getPointsForLevelUp()) {
      throw new IllegalLevelConfigurationException("User has to many points for level " + user.getLevel());
    }
    if (level.getPointsForLevelUp() >= 0 && user.getPointsInLevel() > level.getPointsForLevelUp()) {
      throw new IllegalLevelConfigurationException("User has more points than reachable in level " + user.getLevel());
    }

    user.setPointsOverAll(user.getPointsOverAll() + points);
    while (points > 0) {
      if (level.getPointsForLevelUp() < 0 || level.getPointsForLevelUp() > user.getPointsInLevel() + points) {
        user.setPointsInLevel(user.getPointsInLevel() + points);
        points = 0;
      } else {
        points = points - (level.getPointsForLevelUp() - user.getPointsInLevel());
        user.setLevel(level.getLevel() + 1);
        user.setPointsInLevel(0);
        level = getLevel(user.getLevel());
      }
    }
    updateUserScorePort.updateUser(user);
  }

  private Level getLevel(int level) {
    return levelConfig.getLevels().stream()
                    .filter(l -> l.getLevel() == level)
                    .findFirst()
                    .orElseThrow();
  }
}
