package io.reflectoring.coderadar.graph.contributor.adapter;

import io.reflectoring.coderadar.contributor.ContributorNotFoundException;
import io.reflectoring.coderadar.contributor.port.driven.GetContributorPort;
import io.reflectoring.coderadar.domain.Contributor;
import io.reflectoring.coderadar.graph.contributor.ContributorMapper;
import io.reflectoring.coderadar.graph.contributor.domain.ContributorEntity;
import io.reflectoring.coderadar.graph.contributor.repository.ContributorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetContributorAdapter implements GetContributorPort {
  private final ContributorRepository contributorRepository;
  private final ContributorMapper mapper = new ContributorMapper();

  @Override
  public Contributor get(long id) {
    ContributorEntity entity =
        contributorRepository.findById(id).orElseThrow(() -> new ContributorNotFoundException(id));
    return mapper.mapGraphObject(entity);
  }

  @Override
  public Boolean existsById(long id) {
    return contributorRepository.existsById(id);
  }

  @Override
  public Boolean existsByIdAndUserId(long id, long userId) {
    return contributorRepository.existsByIdAndUserId(id, userId);
  }

  @Override
  public Boolean existsByAuthorName(String authorName) {
    return contributorRepository.existsByNamesContains(authorName);
  }

  @Override
  public Contributor getByAuthorName(String authorName) {
    return mapper.mapGraphObject(contributorRepository.findByNamesContains(authorName).get(0));
  }
}
