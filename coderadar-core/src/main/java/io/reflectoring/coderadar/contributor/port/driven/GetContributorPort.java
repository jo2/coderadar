package io.reflectoring.coderadar.contributor.port.driven;

import io.reflectoring.coderadar.domain.Contributor;

public interface GetContributorPort {
  /**
   * @param id of the contributor.
   * @return Contributor with the supplied id.
   */
  Contributor get(long id);

  /**
   * @param id of the contributor.
   * @return Whether a contributor with the supplied id exists.
   */
  Boolean existsById(long id);

  /**
   * @param id of the contributor
   * @param userId of the user
   * @return Whether a contributor for a given userId exists.
   */
  Boolean existsByIdAndUserId(long id, long userId);

  /**
   * Whether a contributor exists by an authorName.
   *
   * @param authorName The authorName of the contributor.
   * @return Whether the contributor exists or not.
   */
  Boolean existsByAuthorName(String authorName);

  /**
   * The contributor by an authorName.
   *
   * @param authorName The authorName of the contributor.
   * @return The contributor with the given authorName.
   */
  Contributor getByAuthorName(String authorName);
}
