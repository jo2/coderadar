package io.reflectoring.coderadar.game.port.driver.get;

import io.reflectoring.coderadar.domain.Quest;

public interface GetQuestUseCase {

    Quest getQuestById(Long id);
}
