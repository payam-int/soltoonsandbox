package ir.pint.soltoon.services.docker;

public interface DockerManager {
    DockerContainer getContainer(String tag);

    DockerContainerGroup groupContainers(DockerContainer... containers);

    void linkContainers(DockerContainer container1, DockerContainer container2);

    void crosslinkContainers(DockerContainer[] containers1, DockerContainer[] containers2);

    void crosslinkContainers(DockerContainer container1, DockerContainer... containers2);

    boolean runContainerGroup(DockerContainerGroup containerGroup);

    boolean runContainer(DockerContainer container);

    boolean buildContainerGroup(DockerContainerGroup containerGroup);

    boolean buildContainer(DockerContainer container);
}
