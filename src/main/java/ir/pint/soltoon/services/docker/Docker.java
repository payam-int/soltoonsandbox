package ir.pint.soltoon.services.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @todo Load from config files
 */
public class Docker implements DockerService {
    private static Logger logger = LoggerFactory.getLogger(Docker.class.getName());


    private DockerClient dockerClient;
    private DockerScheduling dockerScheduling;

    public Docker() {
        DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost("unix:///var/run/docker.sock")
                .withDockerCertPath("/home/payam/.docker")
                .build();

        DockerClientBuilder dockerClientBuilder = DockerClientBuilder.getInstance(config);


        dockerClient = dockerClientBuilder.build();
        dockerScheduling = new DockerScheduling(this);
        dockerScheduling.start();

    }

    public DockerClient getDockerClient() {
        return dockerClient;
    }

    @Override
    public void addTask(DockerTask task) {
        logger.debug(String.format("Adding task (%s) to docker.", task.getContainerName()));

        dockerScheduling.addTask(task);
    }
}
