package io.reflectoring.coderadar.useradministration.service.contributions;

import io.reflectoring.coderadar.domain.Contributor;
import io.reflectoring.coderadar.useradministration.port.driven.GetContributorsByUserPort;
import io.reflectoring.coderadar.useradministration.port.driver.contributions.GetContributorsByUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetContributorsByUserService implements GetContributorsByUserUseCase {
    private final GetContributorsByUserPort getContributorsByUserPort;

    @Override
    public List<Contributor> getContributorsByUser(Long userId) {
        return getContributorsByUserPort.getContributorsByUser(userId);
    }
}
