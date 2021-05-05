package io.reflectoring.coderadar.useradministration.port.driven;

import io.reflectoring.coderadar.domain.Contributor;

import java.util.List;

public interface GetContributorsByUserPort {
    /**
     * Get all contributors for a given user.
     *
     * @param userId The id of the user.
     * @return The list of contributors.
     */
    List<Contributor> getContributorsByUser(Long userId);
}
