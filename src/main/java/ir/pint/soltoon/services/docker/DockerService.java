package ir.pint.soltoon.services.docker;

import com.spotify.docker.client.DockerClient;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class DockerService implements Docker {

    private DockerClient dockerClient;

    @PostConstruct
    public void init() {
        removeSoltoonContainers();
    }

    private void removeSoltoonContainers() {
        // @todo
    }

    @Override
    public boolean assignContainerGroup(DockerContainerGroup containerGroup) {
        return false;
    }

    @Override
    public boolean runContainerGroup(DockerContainerGroup containerGroup) {
        return false;
    }

    @Override
    public String getHostName() {
        return null;
    }
}
