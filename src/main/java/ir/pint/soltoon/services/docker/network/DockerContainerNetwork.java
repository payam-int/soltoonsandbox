package ir.pint.soltoon.services.docker.network;

import com.spotify.docker.client.DockerClient;

public interface DockerContainerNetwork {
    DockerClient getDockerClient();

    void setDockerClient(DockerClient dockerClient);

    String getName();

    void setName(String name);

    void unuse();

    void use();
}
