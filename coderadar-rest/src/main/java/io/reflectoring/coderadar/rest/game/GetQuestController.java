package io.reflectoring.coderadar.rest.game;

import io.reflectoring.coderadar.domain.Quest;
import io.reflectoring.coderadar.game.port.driver.get.GetQuestUseCase;
import io.reflectoring.coderadar.rest.domain.GetQuestResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import static io.reflectoring.coderadar.rest.GetQuestResponseMapper.mapQuest;

@RestController
@RequiredArgsConstructor
public class GetQuestController {

    private final GetQuestUseCase getQuestUseCase;

    @GetMapping("/quests/{questId}")
    public ResponseEntity<GetQuestResponse> getQuest(@PathVariable long questId) {
        Quest quest = getQuestUseCase.getQuestById(questId);
        return new ResponseEntity<>(mapQuest(quest), HttpStatus.OK);
    }
}
