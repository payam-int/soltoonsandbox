package ir.pint.soltoon.services.docker;

import com.github.dockerjava.api.DockerClient;

public interface DockerService {
    DockerClient getDockerClient();
    void addTask(DockerTask task);
}
