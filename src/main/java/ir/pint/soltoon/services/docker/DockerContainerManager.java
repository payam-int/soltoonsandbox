package ir.pint.soltoon.services.docker;

import com.bugsnag.Report;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.ContainerInfo;
import ir.pint.soltoon.services.logger.ExceptionLogger;
import org.springframework.beans.factory.annotation.Autowired;

public class DockerContainerManager {
    private DockerContainer container;
    private DockerClient dockerClient;

    @Autowired
    private ExceptionLogger exceptionLogger;

    public DockerContainerManager() {
    }

    public DockerContainerManager(DockerContainer container, DockerClient dockerClient) {
        this.container = container;
        this.dockerClient = dockerClient;
    }

    public DockerContainer getContainer() {
        return container;
    }

    public void setContainer(DockerContainer container) {
        this.container = container;
    }

    public DockerClient getDockerClient() {
        return dockerClient;
    }

    public void setDockerClient(DockerClient dockerClient) {
        this.dockerClient = dockerClient;
    }

    public boolean startContainer() {
        try {
            dockerClient.startContainer(getContainer().getId());
            container.getContainerInfo().setStarted(true);
            container.getContainerInfo().setStartTime(System.currentTimeMillis());
            return true;
        } catch (DockerException e) {
            e.printStackTrace();
            exceptionLogger.log(e);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

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

    public void removeContainer() {
        try {
            dockerClient.removeContainer(getContainer().getId());
        } catch (DockerException e) {
            e.printStackTrace();
            exceptionLogger.log(e);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean createContainer() {

        return false;
    }

    public void refreshInformation() {
        try {
            ContainerInfo containerInfo = dockerClient.inspectContainer(getContainer().getId());

            if (containerInfo.state().status().equals("exited")) {
                container.getContainerInfo().setExited(true);
                container.getContainerInfo().setExitCode(containerInfo.state().exitCode());
            }
        } catch (DockerException e) {
            e.printStackTrace();
            exceptionLogger.log(e);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
