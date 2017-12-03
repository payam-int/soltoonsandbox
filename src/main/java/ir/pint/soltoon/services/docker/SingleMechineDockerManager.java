package ir.pint.soltoon.services.docker;

import ir.pint.soltoon.services.docker.container.DockerContainer;
import ir.pint.soltoon.services.docker.container.DockerContainerConfig;
import ir.pint.soltoon.services.docker.container.DockerContainerGroup;
import ir.pint.soltoon.services.docker.container.DockerContainerList;
import ir.pint.soltoon.services.docker.network.DockerContainerNetwork;
import ir.pint.soltoon.services.docker.network.DockerNetwork;
import ir.pint.soltoon.services.docker.network.DockerNetworkManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

// @todo resource management

@Service
public class SingleMechineDockerManager implements DockerManager {

    private final Docker docker;

    private final DockerConfig dockerConfig;

    private final ApplicationContext context;

    private final DockerNetwork dockerNetwork;

    @Autowired
    public SingleMechineDockerManager(Docker docker, DockerConfig dockerConfig, ApplicationContext context, DockerNetwork dockerNetwork) {
        this.docker = docker;
        this.dockerConfig = dockerConfig;
        this.context = context;
        this.dockerNetwork = dockerNetwork;
    }

    @Override
    public DockerContainer getContainer(String tag) {
        DockerContainerConfig containerConfig = dockerConfig.getContainerConfig(tag);
        DockerContainer dockerContainer = context.getBean(DockerContainer.class);
        dockerContainer.setDockerContainerConfig(containerConfig);
        docker.assignContainerGroup(dockerContainer);
        return dockerContainer;
    }

    @Override
    public DockerContainerGroup groupContainers(DockerContainer... containers) {
        DockerContainerList containerList = context.getBean(DockerContainerList.class);
        containerList.addContainers(containers);
        return containerList;
    }

    @Override
    public void linkContainers(DockerContainerGroup containerGroup) {
        DockerContainer[] containers = containerGroup.getContainers();
        DockerContainerNetwork network = dockerNetwork.getNetwork();
        for (DockerContainer container : containers)
            container.setDockerContainerNetwork(network);
    }

    @Override
    public void crossLinkContainers(DockerContainer container, DockerContainerGroup containerGroup, String variable) {
        DockerContainer[] containers = containerGroup.getContainers();
        DockerContainerNetwork network = dockerNetwork.getNetwork();
        for (DockerContainer c : containers) {
            c.setDockerContainerNetwork(network);
            c.addEnvironmentVariable(variable, container.getName());
        }

        container.setDockerContainerNetwork(network);

    }

    @Override
    public boolean runContainerGroup(DockerContainerGroup containerGroup) {
        docker.assignContainerGroup(containerGroup);
        return docker.runContainerGroup(containerGroup);
    }

    @Override
    public boolean runContainer(DockerContainer container) {
        return docker.runContainerGroup(container);
    }
}
