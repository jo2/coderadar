package org.wickedsource.coderadar.job.merge;

import java.util.Arrays;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.wickedsource.coderadar.CoderadarConfiguration;
import org.wickedsource.coderadar.commit.domain.CommitRepository;
import org.wickedsource.coderadar.job.JobLogger;
import org.wickedsource.coderadar.job.core.ProcessingStatus;
import org.wickedsource.coderadar.project.domain.Project;
import org.wickedsource.coderadar.project.domain.ProjectRepository;

@Service
@ConditionalOnProperty(CoderadarConfiguration.MASTER)
public class LogMergerTrigger {

    private JobLogger jobLogger;

    private MergeLogJobRepository jobRepository;

    private ProjectRepository projectRepository;

    private CommitRepository commitRepository;

    @Autowired
    public LogMergerTrigger(JobLogger jobLogger, MergeLogJobRepository jobRepository,
                            ProjectRepository projectRepository, CommitRepository commitRepository) {
        this.jobLogger = jobLogger;
        this.jobRepository = jobRepository;
        this.projectRepository = projectRepository;
        this.commitRepository = commitRepository;
    }

    @Scheduled(fixedDelay = CoderadarConfiguration.TIMER_INTERVAL)
    public void trigger() {
        for (Project project : projectRepository.findAll()) {
            if (shouldJobBeQueuedForProject(project)) {
                MergeLogJob newJob = new MergeLogJob();
                newJob.setProcessingStatus(ProcessingStatus.WAITING);
                newJob.setQueuedDate(new Date());
                newJob.setProject(project);
                jobRepository.save(newJob);
                jobLogger.queuedNewJob(newJob, project);
            }
        }
    }

    private boolean shouldJobBeQueuedForProject(Project project) {
        if (isJobCurrentlyQueuedForProject(project)) {
            jobLogger.alreadyQueuedForProject(MergeLogJob.class, project);
            return false;
        } else {
            int scannedAndUnmergedCommits =
                    commitRepository.countByProjectIdAndScannedTrueAndMergedFalse(project.getId());
            int totalCommits = commitRepository.countByProjectId(project.getId());
            return totalCommits == scannedAndUnmergedCommits && totalCommits > 0;
        }
    }

    private boolean isJobCurrentlyQueuedForProject(Project project) {
        int count = jobRepository.countByProcessingStatusInAndProjectId(
                Arrays.asList(ProcessingStatus.WAITING, ProcessingStatus.PROCESSING), project.getId());
        return count > 0;
    }
}
