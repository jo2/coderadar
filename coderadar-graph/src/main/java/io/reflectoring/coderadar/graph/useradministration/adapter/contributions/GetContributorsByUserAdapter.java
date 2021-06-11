package io.reflectoring.coderadar.graph.useradministration.adapter.contributions;

import io.reflectoring.coderadar.domain.Contributor;
import io.reflectoring.coderadar.graph.contributor.ContributorMapper;
import io.reflectoring.coderadar.graph.contributor.repository.ContributorRepository;
import io.reflectoring.coderadar.useradministration.port.driven.GetContributorsByUserPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetContributorsByUserAdapter implements GetContributorsByUserPort {
    private final ContributorRepository contributorRepository;
    private final ContributorMapper contributorMapper = new ContributorMapper();

    @Override
    public List<Contributor> getContributorsByUser(Long userId) {
        return contributorMapper.mapNodeEntities(contributorRepository.findAllByUserId(userId));
    }
}
