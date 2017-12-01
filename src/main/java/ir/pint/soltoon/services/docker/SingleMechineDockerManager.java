package ir.pint.soltoon.services.docker;

import ir.pint.soltoon.services.docker.container.DockerContainer;
import ir.pint.soltoon.services.docker.container.DockerContainerGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class SingleMechineDockerManager implements DockerManager {

    private final Docker docker;

    @Autowired
    public SingleMechineDockerManager(Docker docker) {
        this.docker = docker;
    }

    @Override
    public DockerContainer getContainer(String tag) {
        return null;
    }

    @Override
    public DockerContainerGroup groupContainers(DockerContainer... containers) {
        return null;
    }
    
    @Override
    public void linkContainers(DockerContainerGroup containerGroup) {

    }

    @Override
    public boolean runContainerGroup(DockerContainerGroup containerGroup) {
        return false;
    }

    @Override
    public boolean runContainer(DockerContainer container) {
        return false;
    }
}
