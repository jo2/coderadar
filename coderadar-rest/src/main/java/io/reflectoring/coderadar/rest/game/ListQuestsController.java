package io.reflectoring.coderadar.rest.game;

import io.reflectoring.coderadar.domain.Quest;
import io.reflectoring.coderadar.game.port.driver.get.ListQuestsUseCase;
import io.reflectoring.coderadar.rest.domain.GetQuestResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static io.reflectoring.coderadar.rest.GetQuestResponseMapper.mapQuests;

@RestController
@RequiredArgsConstructor
public class ListQuestsController {

    private final ListQuestsUseCase listQuestsUseCase;

    @GetMapping("/quests/")
    public ResponseEntity<List<GetQuestResponse>> getQuest() {
        List<Quest> quests = listQuestsUseCase.listQuests();
        return new ResponseEntity<>(mapQuests(quests), HttpStatus.OK);
    }

    @PostMapping("/quests/")
    public ResponseEntity<List<GetQuestResponse>> getQuestByIds(@RequestBody List<Long> questIds) {
        List<Quest> quests = listQuestsUseCase.listQuestsByIds(questIds);
        return new ResponseEntity<>(mapQuests(quests), HttpStatus.OK);
    }
}
