package ir.pint.soltoon.services.docker.api;

import ir.pint.soltoon.services.scheduler.ScheduledJob;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class DockerContainerManagerRunningController implements ScheduledJob {
    private final DockerContainerManager dockerContainerManager;
    private long lastUpdate = 0;


    public DockerContainerManagerRunningController(DockerContainerManager dockerContainerManager) {
        this.dockerContainerManager = dockerContainerManager;
    }

    @Override
    public boolean isReady() {
        if (!dockerContainerManager.getContainer().getContainerInfo().isStarted())
            return false;

        if (System.currentTimeMillis() - lastUpdate >= dockerContainerManager.getContainer().getDockerContainerConfig().getUpdate()) {
            dockerContainerManager.refreshInformation();
            lastUpdate = System.currentTimeMillis();
        }
        return dockerContainerManager.getContainer().getContainerInfo().isExited() || isTimedout();
    }

    @Override
    public boolean runJob() {
        if (!dockerContainerManager.getContainer().getContainerInfo().isExited())
            terminateIfTimedout();

        return true;
    }

    /**
     * Check if container is timed out.
     *
     * @return true if it is timed out.
     */
    boolean isTimedout() {
        return dockerContainerManager.getContainer().getContainerInfo().isStarted() && dockerContainerManager.getContainer().getContainerInfo().getStartTime()
                .plus(dockerContainerManager.getContainer().getDockerContainerConfig().getResourceLimits().getTimeout(), ChronoUnit.MILLIS)
                .isBefore(Instant.now());
    }

    /**
     * Terminates container if it is not exited normally.
     */
    void terminateIfTimedout() {
        dockerContainerManager.refreshInformation();
        if (!dockerContainerManager.getContainer().getContainerInfo().isExited()) {
            dockerContainerManager.terminateContainer();
            dockerContainerManager.refreshInformation();
        }
    }
}