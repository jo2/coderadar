package io.reflectoring.coderadar.useradministration;

import io.reflectoring.coderadar.EntityNotFoundException;

public class QuestNotFoundException extends EntityNotFoundException {
  public QuestNotFoundException(Long id) {
    super("Quest with id " + id + " not found.");
  }
}
