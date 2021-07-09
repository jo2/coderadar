package io.reflectoring.coderadar.game.service.get;

import io.reflectoring.coderadar.domain.Quest;
import io.reflectoring.coderadar.game.port.driven.GetQuestPort;
import io.reflectoring.coderadar.game.port.driver.get.GetQuestUseCase;
import io.reflectoring.coderadar.useradministration.QuestNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetQuestService implements GetQuestUseCase {

    private final GetQuestPort getQuestPort;

    @Override
    public Quest getQuestById(Long id) {
        if (!getQuestPort.existsById(id)) {
            throw new QuestNotFoundException(id);
        }
        return getQuestPort.getQuestById(id);
    }
}
