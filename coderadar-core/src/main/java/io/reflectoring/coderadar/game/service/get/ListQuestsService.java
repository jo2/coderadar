package io.reflectoring.coderadar.game.service.get;

import io.reflectoring.coderadar.domain.Quest;
import io.reflectoring.coderadar.game.port.driven.ListQuestsPort;
import io.reflectoring.coderadar.game.port.driver.get.ListQuestsUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListQuestsService implements ListQuestsUseCase {

    private final ListQuestsPort listQuestsPort;

    @Override
    public List<Quest> listQuests() {
        return listQuestsPort.listQuests();
    }

    @Override
    public List<Quest> listQuestsByIds(List<Long> questIds) {
        return listQuestsPort.listQuestsByIds(questIds);
    }
}
