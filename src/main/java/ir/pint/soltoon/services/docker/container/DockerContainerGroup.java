package ir.pint.soltoon.services.docker.container;

public interface DockerContainerGroup {
    DockerContainer[] getContainers();
    void remove();
}
