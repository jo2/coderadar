package io.reflectoring.coderadar.game.port.driven;

import io.reflectoring.coderadar.domain.Quest;

import java.util.List;

public interface ListQuestsPort {

    List<Quest> listQuests();

    List<Quest> listQuestsByIds(List<Long> questIds);
}
