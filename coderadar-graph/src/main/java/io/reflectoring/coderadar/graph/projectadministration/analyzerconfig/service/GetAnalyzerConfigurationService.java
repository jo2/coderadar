package io.reflectoring.coderadar.graph.projectadministration.analyzerconfig.service;

import io.reflectoring.coderadar.core.projectadministration.domain.AnalyzerConfiguration;
import io.reflectoring.coderadar.core.projectadministration.port.driven.analyzerconfig.GetAnalyzerConfigurationPort;
import io.reflectoring.coderadar.graph.projectadministration.analyzerconfig.repository.GetAnalyzerConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GetAnalyzerConfigurationService implements GetAnalyzerConfigurationPort {
    private final GetAnalyzerConfigurationRepository getAnalyzerConfigurationRepository;

    @Autowired
    public GetAnalyzerConfigurationService(GetAnalyzerConfigurationRepository getAnalyzerConfigurationRepository) {
        this.getAnalyzerConfigurationRepository = getAnalyzerConfigurationRepository;
    }

    @Override
    public Optional<AnalyzerConfiguration> getAnalyzerConfiguration(Long id) {
        return getAnalyzerConfigurationRepository.findById(id);
    }
}
