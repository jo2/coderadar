package io.reflectoring.coderadar.contributor;

import io.reflectoring.coderadar.EntityNotFoundException;

public class ContributorByUserNotFoundException extends EntityNotFoundException {
  public ContributorByUserNotFoundException(long id, long userId) {
    super("Contributor with id " + id + " and user id " + userId + " not found.");
  }
}
