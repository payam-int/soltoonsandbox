package ir.pint.soltoon.services.docker;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.Container;
import ir.pint.soltoon.services.docker.api.DockerContainerApi;
import ir.pint.soltoon.services.docker.api.DockerContainerManager;
import ir.pint.soltoon.services.docker.container.DockerContainer;
import ir.pint.soltoon.services.docker.container.DockerContainerConfig;
import ir.pint.soltoon.services.docker.container.DockerContainerGroup;
import ir.pint.soltoon.services.docker.network.DockerNetwork;
import ir.pint.soltoon.services.logger.ExternalExceptionLogger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class DockerService implements Docker {


    private Logger logger = LoggerFactory.getLogger(getClass().getName());

    private DockerClient dockerClient;

    private final ApplicationContext context;

    private final ExternalExceptionLogger externalExceptionLogger;


    private final DockerConfig dockerConfig;

    private final DockerNetwork dockerNetwork;

    @Autowired
    public DockerService(ApplicationContext context, ExternalExceptionLogger externalExceptionLogger, DockerConfig dockerConfig, DockerNetwork dockerNetwork) {
        this.context = context;
        this.externalExceptionLogger = externalExceptionLogger;
        this.dockerConfig = dockerConfig;
        this.dockerNetwork = dockerNetwork;
    }

    @PostConstruct
    public void init() {
        dockerClient = DefaultDockerClient.builder().uri(dockerConfig.getUri()).build();

        dockerNetwork.setClient(dockerClient);
        dockerNetwork.init();

        removeSoltoonContainers();
    }

    private void removeSoltoonContainers() {
        try {
            List<Container> containers = dockerClient.listContainers(DockerClient.ListContainersParam.allContainers(), DockerClient.ListContainersParam.withLabel(dockerConfig.getDefaultLabel()));

            if (containers.size() > 0)
                logger.info(String.format("There is %d containers tagged with name %s.", containers.size(), dockerConfig.getDefaultLabel()));
            else
                logger.info(String.format("There is no container tagged with name %s.", dockerConfig.getDefaultLabel()));

            for (Container container : containers)
                try {
                    dockerClient.removeContainer(container.id(), DockerClient.RemoveContainerParam.forceKill());
                } catch (DockerException e) {
                    e.printStackTrace();
                    externalExceptionLogger.log(e);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        } catch (DockerException e) {
            e.printStackTrace();
            externalExceptionLogger.log(e);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean assignContainerGroup(DockerContainerGroup containerGroup) {
        DockerContainer[] containers = containerGroup.getContainers();

        for (DockerContainer c : containers) {
            DockerContainerManager manager = context.getBean(DockerContainerManager.class);
            manager.setContainer(c);
            manager.setDockerClient(dockerClient);

            c.setContainerApi(manager);
        }
        return true;
    }

    @Override
    public boolean runContainerGroup(DockerContainerGroup containerGroup) {

        return false;
    }

    @Override
    public String getName() {
        return null;
    }
}
