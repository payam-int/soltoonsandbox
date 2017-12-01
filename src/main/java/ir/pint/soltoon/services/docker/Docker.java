package ir.pint.soltoon.services.docker;

import ir.pint.soltoon.services.docker.container.DockerContainerGroup;

/**
 * Runs and assigns container groups.
 */

public interface Docker {
    boolean assignContainerGroup(DockerContainerGroup containerGroup);

    boolean runContainerGroup(DockerContainerGroup containerGroup);

    String getName();
}
