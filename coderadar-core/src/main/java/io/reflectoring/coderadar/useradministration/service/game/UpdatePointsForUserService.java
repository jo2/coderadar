package io.reflectoring.coderadar.useradministration.service.game;

import io.reflectoring.coderadar.domain.User;
import io.reflectoring.coderadar.useradministration.port.driven.GetUserPort;
import io.reflectoring.coderadar.useradministration.port.driven.UpdateUserPort;
import io.reflectoring.coderadar.useradministration.port.driver.game.UpdateCoinsForUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdatePointsForUserService implements UpdateCoinsForUserUseCase {

  private final GetUserPort getUserPort;
  private final UpdateUserPort updateUserPort;

  @Override
  public void updateCoinsForUser(Long userId, int coins) {
    User user = getUserPort.getUser(userId);
    user.setCoins(user.getCoins() + coins);
    updateUserPort.updateUser(user);
  }
}
