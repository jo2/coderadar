package io.reflectoring.coderadar.graph.useradministration.adapter.game;

import io.reflectoring.coderadar.domain.Quest;
import io.reflectoring.coderadar.game.port.driven.GetQuestPort;
import io.reflectoring.coderadar.graph.useradministration.QuestMapper;
import io.reflectoring.coderadar.graph.useradministration.repository.QuestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetQuestAdapter implements GetQuestPort {

    private final QuestRepository questRepository;
    private final QuestMapper questMapper = new QuestMapper();

    @Override
    public Quest getQuestById(Long id) {
        return questMapper.mapGraphObject(questRepository.findById(id).orElseThrow());
    }

    @Override
    public boolean existsById(Long id) {
        return questRepository.existsById(id);
    }
}
