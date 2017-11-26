package ir.pint.soltoon.services.docker;


import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.exceptions.DockerRequestException;
import com.spotify.docker.client.messages.NetworkConfig;
import ir.pint.soltoon.services.docker.dockerTask.DockerTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * @todo Load from config files
 */

@Service
public class Docker implements DockerService {
    private static Logger logger = LoggerFactory.getLogger(Docker.class.getName());


    private DockerConfig dockerConfig;

    private DockerClient dockerClient;
    private DockerContainerPool dockerContainerPool;

    @PostConstruct
    private void configureNetwork() {
        NetworkConfig internalNetwork = NetworkConfig.builder().name(dockerConfig.getNetwork()).driver("bridge").internal(true).checkDuplicate(true).build();
        try {
            dockerClient.createNetwork(internalNetwork);
        } catch (DockerException e) {
            if (!(e instanceof DockerRequestException && ((DockerRequestException) e).status() == 403))
                e.printStackTrace();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Autowired
    public Docker(DockerConfig dockerConfig) {
        this.dockerConfig = dockerConfig;
        logger.debug(String.format("Initiating docker service on socket '%s'.", dockerConfig.getUri()));
        dockerClient = new DefaultDockerClient(dockerConfig.getUri());
        dockerContainerPool = new DockerContainerPool(this);
        logger.debug("Starting docker scheduling thread.");
        dockerContainerPool.start();
    }

    public DockerClient getDockerClient() {
        return dockerClient;
    }

    @Override
    public void addTask(DockerTask task) {
        logger.debug(String.format("Adding task (%s) to docker.", task.getContainerName()));
        dockerContainerPool.addTask(task);
    }
}
