package io.reflectoring.coderadar.graph.useradministration.adapter;

import io.reflectoring.coderadar.domain.User;
import io.reflectoring.coderadar.graph.useradministration.UserMapper;
import io.reflectoring.coderadar.graph.useradministration.repository.UserRepository;
import io.reflectoring.coderadar.useradministration.port.driven.UpdateUserPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateUserAdapter implements UpdateUserPort {

  private final UserRepository userRepository;
  private final UserMapper userMapper = new UserMapper();

  @Override
  public void updateUser(User user) {
    userRepository.save(userMapper.mapDomainObject(user));
  }
}
