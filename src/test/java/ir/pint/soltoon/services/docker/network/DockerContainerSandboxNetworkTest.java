package ir.pint.soltoon.services.docker.network;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.exceptions.NetworkNotFoundException;
import com.spotify.docker.client.messages.Network;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DockerContainerSandboxNetworkTest {

    @Autowired
    private ApplicationContext context;

    private DockerContainerSandboxNetwork network;


    private DockerClient dockerClient;

    @Before
    public void setUp() throws Exception {
        dockerClient = DefaultDockerClient.fromEnv().build();
        network = context.getBean(DockerContainerSandboxNetwork.class);
        network.setDockerClient(dockerClient);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test(expected = NetworkNotFoundException.class)
    public void unuse() throws Exception {
        network.use();
        network.unuse();


        Network network = dockerClient.inspectNetwork(this.network.getName());
    }

    @Test
    public void use() throws Exception {
        network.use();
        Network network = dockerClient.inspectNetwork(this.network.getName());
    }

}