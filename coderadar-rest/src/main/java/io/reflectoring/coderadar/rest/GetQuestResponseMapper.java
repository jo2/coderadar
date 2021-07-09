package io.reflectoring.coderadar.rest;

import io.reflectoring.coderadar.domain.Quest;
import io.reflectoring.coderadar.domain.User;
import io.reflectoring.coderadar.rest.domain.GetQuestResponse;
import io.reflectoring.coderadar.rest.domain.GetUserResponse;

import java.util.ArrayList;
import java.util.List;

public class GetQuestResponseMapper {
  private GetQuestResponseMapper() {}

  public static GetQuestResponse mapQuest(Quest quest) {
    return new GetQuestResponse(
            quest.getId(),
            quest.getTitle(),
            quest.getDescription(),
            quest.getPoints(),
            quest.getCoins()
    );
  }

  public static List<GetQuestResponse> mapQuests(List<Quest> quests) {
    List<GetQuestResponse> result = new ArrayList<>(quests.size());
    for (Quest quest : quests) {
      result.add(mapQuest(quest));
    }
    return result;
  }
}
