package io.reflectoring.coderadar.graph.query.adapter;

import io.reflectoring.coderadar.domain.Commit;
import io.reflectoring.coderadar.graph.analyzer.repository.CommitRepository;
import io.reflectoring.coderadar.graph.projectadministration.project.adapter.CommitBaseDataMapper;
import io.reflectoring.coderadar.query.port.driven.GetCommitPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetCommitAdapter implements GetCommitPort {

    private final CommitRepository commitRepository;
    private final CommitBaseDataMapper commitBaseDataMapper;

    @Override
    public Commit getCommitById(long commitId) {
        return commitBaseDataMapper.mapGraphObject(commitRepository.findById(commitId).orElseThrow());
    }

    @Override
    public boolean existsById(long commitId) {
        return commitRepository.existsById(commitId);
    }

    @Override
    public Commit getCommitByHash(String hash) {
        return commitBaseDataMapper.mapGraphObject(commitRepository.findByHash(hash));
    }

    @Override
    public boolean existsByHash(String hash) {
        return commitRepository.existsByHash(hash);
    }
}
