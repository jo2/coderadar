package io.reflectoring.coderadar.useradministration.port.driven;

import io.reflectoring.coderadar.domain.User;

public interface UpdateUserPort {
  /**
   * Update a given user.
   *
   * @param user The user to update.
   */
  void updateUser(User user);
}
