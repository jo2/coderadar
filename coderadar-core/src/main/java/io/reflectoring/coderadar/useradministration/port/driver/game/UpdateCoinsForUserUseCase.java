package io.reflectoring.coderadar.useradministration.port.driver.game;

public interface UpdateCoinsForUserUseCase {
    /**
     * Add or subtract an amount of coins to or from a user.
     *
     * @param userId The id of the user to add or subtract the coins to or from.
     * @param coins The amount of coins to add or subtract.
     */
    void updateCoinsForUser(Long userId, int coins);
}
