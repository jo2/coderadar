package io.reflectoring.coderadar.graph.useradministration;

import io.reflectoring.coderadar.domain.Quest;
import io.reflectoring.coderadar.domain.User;
import io.reflectoring.coderadar.graph.Mapper;
import io.reflectoring.coderadar.graph.useradministration.domain.QuestEntity;
import io.reflectoring.coderadar.graph.useradministration.domain.UserEntity;

public class QuestMapper implements Mapper<Quest, QuestEntity> {

  @Override
  public Quest mapGraphObject(QuestEntity nodeEntity) {
    Quest quest = new Quest();
    quest.setId(nodeEntity.getId());
    quest.setTitle(nodeEntity.getTitle());
    quest.setDescription(nodeEntity.getDescription());
    quest.setPoints(nodeEntity.getPoints());
    quest.setCoins(nodeEntity.getCoins());
    return quest;
  }

  @Override
  public QuestEntity mapDomainObject(Quest domainObject) {
    QuestEntity questEntity = new QuestEntity();
    questEntity.setId(domainObject.getId());
    questEntity.setTitle(domainObject.getTitle());
    questEntity.setDescription(domainObject.getDescription());
    questEntity.setPoints(domainObject.getPoints());
    questEntity.setCoins(domainObject.getCoins());
    return questEntity;
  }
}
