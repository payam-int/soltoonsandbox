package ir.pint.soltoon.services.docker.api;

import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.ContainerConfig;
import com.spotify.docker.client.messages.ContainerCreation;
import com.spotify.docker.client.messages.ContainerInfo;
import com.spotify.docker.client.messages.HostConfig;
import ir.pint.soltoon.services.docker.DockerConfig;
import ir.pint.soltoon.services.docker.container.DockerContainer;
import ir.pint.soltoon.services.docker.container.DockerContainerConfig;
import ir.pint.soltoon.services.logger.ExternalExceptionLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.Instant;


/**
 * Manages actions performed on container. Uses spotify/docker-api library.
 */

@Component
@Scope("prototype")
public class DockerContainerManager implements DockerContainerApi {
    private DockerContainer container;
    private DockerClient dockerClient;

    @Autowired
    private ExternalExceptionLogger exceptionLogger;

    public DockerContainerManager() {

    }

    public DockerContainerManager(DockerContainer container, DockerClient dockerClient) {
        this.container = container;
        this.dockerClient = dockerClient;
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
        try {
            dockerClient.startContainer(getContainer().getId());
            container.getContainerInfo().setStarted(true);
            container.getContainerInfo().setStartTime(Instant.now());

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
        try {
            dockerClient.killContainer(getContainer().getId());
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
        try {
            dockerClient.removeContainer(getContainer().getId(), DockerClient.RemoveContainerParam.forceKill());
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

        builder = builder.env(dockerContainerConfig.getEnvironmentVariables());

        if (getContainer().getName() != null)
            builder.hostname(getContainer().getName());

        builder = builder.hostConfig(getHostConfig()).image(getContainer().getDockerContainerConfig().getImage());

        builder.labels(getContainer().getLabels());

        ContainerConfig containerConfig = builder.build();

        try {
            ContainerCreation container = dockerClient.createContainer(containerConfig);
            getContainer().setId(container.id());
            getContainer().getContainerInfo().setCreated(true);

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

            if (containerInfo.state().status().equals("exited")) {
                container.getContainerInfo().setExited(true);
                container.getContainerInfo().setExitCode(containerInfo.state().exitCode());
                container.getContainerInfo().setExitTime(Instant.now());
            }
        } catch (DockerException e) {
            e.printStackTrace();
            exceptionLogger.log(e);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
