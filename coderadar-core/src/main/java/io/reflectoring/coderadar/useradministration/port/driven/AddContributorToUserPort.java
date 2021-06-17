package io.reflectoring.coderadar.useradministration.port.driven;

public interface AddContributorToUserPort {
  /**
   * Add a contributor to the logged in user.
   *
   * @param contributorId The id of the contributor.
   * @param userId The id of the user to add the contributor for.
   */
  void addContributor(Long contributorId, Long userId);
}
