package io.reflectoring.coderadar.graph.useradministration.adapter.contributions;

import io.reflectoring.coderadar.contributor.ContributorNotFoundException;
import io.reflectoring.coderadar.graph.contributor.domain.ContributorEntity;
import io.reflectoring.coderadar.graph.contributor.repository.ContributorRepository;
import io.reflectoring.coderadar.graph.useradministration.domain.UserEntity;
import io.reflectoring.coderadar.graph.useradministration.repository.UserRepository;
import io.reflectoring.coderadar.useradministration.UserNotFoundException;
import io.reflectoring.coderadar.useradministration.port.driven.AddContributorToUserPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddContributorToUserAdapter implements AddContributorToUserPort {

  private final UserRepository userRepository;
  private final ContributorRepository contributorRepository;

  @Override
  public void addContributor(Long contributorId, Long userId) {
    UserEntity user =
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    ContributorEntity contributor =
        contributorRepository
            .findById(contributorId)
            .orElseThrow(() -> new ContributorNotFoundException(contributorId));
    contributor.setUserId(userId);
    contributor = contributorRepository.save(contributor);
    user.getContributors().add(contributor);
    userRepository.save(user);
  }
}
