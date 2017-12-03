package ir.pint.soltoon.services.docker.api;

import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.*;
import ir.pint.soltoon.services.docker.container.DockerContainer;
import ir.pint.soltoon.services.docker.container.DockerContainerConfig;
import ir.pint.soltoon.services.logger.ExternalExceptionLogger;
import ir.pint.soltoon.services.scheduler.ShortTimeScheduler;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Logger;


/**
 * Manages actions performed on container. Uses spotify/docker-api library.
 */

@Component
@Scope("prototype")
public class DockerContainerManager implements DockerContainerManagerInterface {

    private final DockerContainerManagerRunningController dockerContainerManagerRunningController = new DockerContainerManagerRunningController(this);
    private DockerContainer container;

    /**
     * Api object for docker
     */
    private DockerClient dockerClient;

    private final ExternalExceptionLogger exceptionLogger;

    private final ShortTimeScheduler shortTimeScheduler;

    @Autowired
    public DockerContainerManager(ExternalExceptionLogger exceptionLogger, ShortTimeScheduler shortTimeScheduler) {
        this.exceptionLogger = exceptionLogger;
        this.shortTimeScheduler = shortTimeScheduler;
    }


    @Override
    public DockerContainer getContainer() {
        return container;
    }

    @Override
    public void setContainer(DockerContainer container) {
        this.container = container;
    }

    public DockerClient getDockerClient() {
        return dockerClient;
    }

    public void setDockerClient(DockerClient dockerClient) {
        this.dockerClient = dockerClient;
    }

    @Override
    public boolean startContainer() {
        if (!getContainer().getContainerInfo().isCreated())
            createContainer();

        try {
            dockerClient.startContainer(getContainer().getId());
            container.getContainerInfo().setStarted(true);
            container.getContainerInfo().setStartTime(Instant.now());
            shortTimeScheduler.addJob(dockerContainerManagerRunningController);
            getContainer().getEvents().started();
            return true;
        } catch (DockerException e) {
            e.printStackTrace();
            exceptionLogger.log(e);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean terminateContainer() {
        container.unuseNetwork();
        try {
            dockerClient.killContainer(getContainer().getId());
            getContainer().getEvents().terminated();
            return true;
        } catch (DockerException e) {
            e.printStackTrace();
            exceptionLogger.log(e);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void removeContainer() {
        container.unuseNetwork();
        try {
            dockerClient.removeContainer(getContainer().getId(), DockerClient.RemoveContainerParam.forceKill());
            getContainer().getEvents().removed();
        } catch (DockerException e) {
            e.printStackTrace();
            exceptionLogger.log(e);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean createContainer() {
        DockerContainerConfig dockerContainerConfig = getContainer().getDockerContainerConfig();

        ContainerConfig.Builder builder = ContainerConfig.builder();

        ArrayList<String> env = new ArrayList<>();
        env.addAll(dockerContainerConfig.getEnvironmentVariables());
        env.addAll(getContainer().getEnvironmentVariables());

        builder = builder.env(env);

        if (getContainer().getName() != null)
            builder.hostname(getContainer().getName());


        builder = builder.hostConfig(getHostConfig()).image(getContainer().getDockerContainerConfig().getImage());

        builder = builder.labels(getContainer().getLabels());

        ContainerConfig containerConfig = builder.build();

        try {
            ContainerCreation container = dockerClient.createContainer(containerConfig, getContainer().getName());
            getContainer().setId(container.id());
            getContainer().getContainerInfo().setCreated(true);
            getContainer().getEvents().created();
            return true;
        } catch (DockerException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return false;
    }

    private HostConfig getHostConfig() {
        HostConfig.Builder builder = HostConfig.builder();

        getContainer().getDockerContainerNetwork().use();

        builder = builder.networkMode(getContainer().getDockerContainerNetwork().getName());

        DockerContainerConfig config = getContainer().getDockerContainerConfig();
        if (config.getResourceLimits().getCpu() != -1)
            builder = builder.cpuPeriod(10000L).cpuQuota(config.getResourceLimits().getCpu());

        if (config.getResourceLimits().getMemory() != -1)
            builder = builder.memory(config.getResourceLimits().getMemory());

        if (config.getResourceLimits().getSwapMemory() != -1)
            builder = builder.memorySwap(config.getResourceLimits().getSwapMemory());

        return builder.build();
    }

    @Override
    public void refreshInformation() {
        try {
            ContainerInfo containerInfo = dockerClient.inspectContainer(getContainer().getId());


            if (Objects.equals(containerInfo.state().status(), "exited")) {
                container.getContainerInfo().setExited(true);
                container.getContainerInfo().setExitCode(containerInfo.state().exitCode());
                container.getContainerInfo().setExitTime(Instant.now());
                getContainer().getEvents().exited();
            }

            getContainer().getEvents().refreshed();
        } catch (DockerException e) {
            e.printStackTrace();
            LoggerFactory.getLogger(this.getClass().getName()).error("EEE", e);
            exceptionLogger.log(e);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @PostConstruct
    private void init() {

    }

}
