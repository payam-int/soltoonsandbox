package ir.pint.soltoon.services.docker.network;

import com.spotify.docker.client.DockerClient;

public class DockerContainerNetwork {
    private String name;
    private int usages = -1;
    private DockerClient dockerClient;

    public DockerContainerNetwork() {
    }

    public DockerContainerNetwork(String name) {
        this.name = name;
    }

    public DockerClient getDockerClient() {
        return dockerClient;
    }

    public void setDockerClient(DockerClient dockerClient) {
        this.dockerClient = dockerClient;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void unuse() {
        if (usages > 0)
            usages--;

        if (usages == 0)
            removeNetwork();
    }

    private void removeNetwork() {

        usages--;
    }

    public void use() {
        if (usages == -1) createNetwork();

        if (usages > -1) usages++;
    }

    private void createNetwork() {
        usages++;
    }
}
