package io.reflectoring.coderadar.game.port.driven;

import io.reflectoring.coderadar.domain.Quest;

public interface GetQuestPort {

    Quest getQuestById(Long id);

    boolean existsById(Long id);
}
