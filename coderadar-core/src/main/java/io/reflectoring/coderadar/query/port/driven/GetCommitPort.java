package io.reflectoring.coderadar.query.port.driven;

import io.reflectoring.coderadar.domain.Commit;

public interface GetCommitPort {

    /**
     * Return a commit by a given id.
     *
     * @param commitId The id of the commit.
     * @return The commit with the given id.
     */
    Commit getCommitById(long commitId);

    /**
     * Returns whether a commit exists for a given id.
     *
     * @param commitId The id of the commit.
     * @return Whether the commit exists or not.
     */
    boolean existsById(long commitId);

    /**
     * Return a commit by a given hash.
     *
     * @param hash The hash of the commit.
     * @return The commit with the given hash.
     */
    Commit getCommitByHash(String hash);

    /**
     * Returns whether a commit exists for a given hash.
     *
     * @param hash The hash of the commit.
     * @return Whether the commit exists or not.
     */
    boolean existsByHash(String hash);
}
