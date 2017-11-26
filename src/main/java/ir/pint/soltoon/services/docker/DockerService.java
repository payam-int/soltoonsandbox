package ir.pint.soltoon.services.docker;


import com.spotify.docker.client.DockerClient;
import ir.pint.soltoon.services.docker.dockerTask.DockerTask;


public interface DockerService {
    DockerClient getDockerClient();
    void addTask(DockerTask task);
}
