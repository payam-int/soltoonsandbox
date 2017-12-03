package ir.pint.soltoon.services.docker.network;

import com.spotify.docker.client.DockerClient;
import org.springframework.stereotype.Component;

import javax.inject.Singleton;

@Component
@Singleton
public class DockerContainerNoneNetwork implements DockerContainerNetwork {

    public DockerContainerNoneNetwork() {

    }

    @Override
    public DockerClient getDockerClient() {
        // ignore
        return null;
    }

    @Override
    public void setDockerClient(DockerClient dockerClient) {
        // ignore
    }

    @Override
    public String getName() {
        return "none";
    }

    @Override
    public void setName(String name) {
        // ignore
    }

    @Override
    public void unuse() {
        // ignore
    }

    @Override
    public void use() {
        // ignore
    }
}
