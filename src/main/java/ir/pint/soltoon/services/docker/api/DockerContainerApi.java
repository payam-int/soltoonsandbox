package ir.pint.soltoon.services.docker.api;

import ir.pint.soltoon.services.docker.container.DockerContainer;

public interface DockerContainerApi {
    DockerContainer getContainer();

    void setContainer(DockerContainer container);

    boolean startContainer();

    boolean terminateContainer();

    void removeContainer();

    boolean createContainer();

    void refreshInformation();
}
