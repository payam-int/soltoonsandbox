package ir.pint.soltoon.services.docker;

import ir.pint.soltoon.services.docker.container.DockerContainer;
import ir.pint.soltoon.services.docker.container.DockerContainerGroup;


/**
 * Manages docker instances on different mechines and general commands on containers.
 */
public interface DockerManager {
    DockerContainer getContainer(String tag);
    DockerContainerGroup groupContainers(DockerContainer... containers);

    void linkContainers(DockerContainerGroup containerGroup);

    boolean runContainerGroup(DockerContainerGroup containerGroup);

    boolean runContainer(DockerContainer container);
}
