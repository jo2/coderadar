package io.reflectoring.coderadar.useradministration.port.driver.contributions;

public interface RemoveContributorFromUserUseCase {
    /**
     * Remove a contributor from the logged in user.
     *
     * @param contributorId The id of the contributor.
     * @param userId The id of the user to add the contributor for.
     */
    void removeContributor(Long contributorId, Long userId);
}
