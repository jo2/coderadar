package io.reflectoring.coderadar.contributor;

import io.reflectoring.coderadar.EntityNotFoundException;

public class ContributorNotFoundException extends EntityNotFoundException {
  public ContributorNotFoundException(long id) {
    super("Contributor with id " + id + " not found.");
  }

  public ContributorNotFoundException(String authorName) {
    super("Contributor with author name " + authorName + " not found.");
  }
}
