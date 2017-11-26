package ir.pint.soltoon.services.docker;

public interface Docker {
    boolean assignContainerGroup(DockerContainerGroup containerGroup);

    boolean runContainerGroup(DockerContainerGroup containerGroup);

    String getHostName();
}
