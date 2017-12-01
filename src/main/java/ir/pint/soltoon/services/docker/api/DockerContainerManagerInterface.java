package ir.pint.soltoon.services.docker.api;

import ir.pint.soltoon.services.docker.container.DockerContainer;

public interface DockerContainerManagerInterface {
    DockerContainer getContainer();

    void setContainer(DockerContainer container);


    /**
     * @return true if successful.
     */
    boolean startContainer();


    /**
     * @return true if successful.
     */
    boolean terminateContainer();

    void removeContainer();

    /**
     * @return true if successful.
     */
    boolean createContainer();

    /**
     * Refresh container information.
     */
    void refreshInformation();
}
