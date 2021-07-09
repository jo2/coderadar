package io.reflectoring.coderadar.graph.useradministration.adapter.game;

import io.reflectoring.coderadar.domain.Quest;
import io.reflectoring.coderadar.game.port.driven.ListQuestsPort;
import io.reflectoring.coderadar.graph.useradministration.QuestMapper;
import io.reflectoring.coderadar.graph.useradministration.repository.QuestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class ListQuestsAdapter implements ListQuestsPort {

    private final QuestRepository questRepository;
    private final QuestMapper questMapper = new QuestMapper();

    @Override
    public List<Quest> listQuests() {
        return questMapper.mapNodeEntities(StreamSupport
                .stream(questRepository.findAll().spliterator(), false)
                .collect(Collectors.toList()));
    }

    @Override
    public List<Quest> listQuestsByIds(List<Long> questIds) {
        return questMapper.mapNodeEntities(StreamSupport
                .stream(questRepository.findAllById(questIds).spliterator(), false)
                .collect(Collectors.toList()));
    }
}
