package ir.pint.soltoon.services.docker.network;

import com.spotify.docker.client.DockerClient;

public interface DockerNetwork {
    void setClient(DockerClient dockerClient);
    void init();
    void cleanup();
    DockerContainerNetwork getNetwork();
}
