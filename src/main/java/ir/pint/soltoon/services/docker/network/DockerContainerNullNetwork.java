package ir.pint.soltoon.services.docker.network;

import com.spotify.docker.client.DockerClient;
import org.springframework.stereotype.Component;

import javax.inject.Singleton;

public class DockerContainerNullNetwork extends DockerContainerNetwork {

    public DockerContainerNullNetwork() {

    }

    public DockerContainerNullNetwork(String name) {

    }

    @Override
    public DockerClient getDockerClient() {
        return null;
    }

    @Override
    public void setDockerClient(DockerClient dockerClient) {

    }

    @Override
    public String getName() {
        return "none";
    }

    @Override
    public void setName(String name) {

    }

    @Override
    public void unuse() {

    }

    @Override
    public void use() {

    }
}
