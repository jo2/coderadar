package io.reflectoring.coderadar.useradministration.port.driven;

public interface RemoveContributorFromUserPort {
    /**
     * Remove a contributor from the logged in user.
     *
     * @param contributorId The id of the contributor.
     * @param userId The id of the user to add the contributor for.
     */
    void removeContributor(Long contributorId, Long userId);
}
