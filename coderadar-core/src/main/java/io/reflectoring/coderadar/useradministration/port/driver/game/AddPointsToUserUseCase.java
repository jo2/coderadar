package io.reflectoring.coderadar.useradministration.port.driver.game;

public interface AddPointsToUserUseCase {
    /**
     * Add an amount of points to a user.
     *
     * @param userId The id of the user to add the points to.
     * @param points The amount of points to add.
     */
    void addPointsToUser(Long userId, int points);
}
