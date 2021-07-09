package io.reflectoring.coderadar.game.port.driver.get;

import io.reflectoring.coderadar.domain.Quest;

import java.util.List;

public interface ListQuestsUseCase {

    List<Quest> listQuests();

    List<Quest> listQuestsByIds(List<Long> questIds);
}
