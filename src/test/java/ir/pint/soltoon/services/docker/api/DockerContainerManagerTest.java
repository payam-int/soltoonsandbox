package ir.pint.soltoon.services.docker.api;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.exceptions.ContainerNotFoundException;
import com.spotify.docker.client.messages.ContainerInfo;
import ir.pint.soltoon.services.docker.DockerConfig;
import ir.pint.soltoon.services.docker.container.DockerContainer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Hashtable;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DockerContainerManagerTest {

    @Autowired
    private ApplicationContext context;

    private DockerContainer dockerContainer;
    private DockerContainerManager containerManager;
    private DockerClient dockerClient;

    @Autowired
    private DockerConfig dockerConfig;


    @Before
    public void setUp() throws Exception {
        dockerContainer = context.getBean(DockerContainer.class);
        containerManager = context.getBean(DockerContainerManager.class);
        dockerContainer.setContainerApi(containerManager);
        containerManager.setContainer(dockerContainer);
        dockerClient = DefaultDockerClient.fromEnv().build();
        containerManager.setDockerClient(dockerClient);

        Hashtable<String, String> labels = new Hashtable<>();
        labels.put(dockerConfig.getDefaultLabel(), "true");
        dockerContainer.setLabels(labels);

        dockerContainer.setName("example");
        dockerContainer.setDockerContainerConfig(dockerConfig.getContainerConfig("test-longruntime"));
    }

    @After
    public void tearDown() throws Exception {
        containerManager.removeContainer();
    }


    @Test
    public void createContainer() throws Exception {
        boolean b = containerManager.createContainer();
        assertTrue(b);

        assertTrue(dockerContainer.getId() != null);

        ContainerInfo containerInfo = dockerClient.inspectContainer(dockerContainer.getId());
        assertTrue(containerInfo != null);
    }

    @Test
    public void startContainer() throws Exception {
        containerManager.createContainer();

        assertTrue(containerManager.startContainer());
        ContainerInfo containerInfo = dockerClient.inspectContainer(dockerContainer.getId());

        assertEquals("running", containerInfo.state().status());
    }

    @Test
    public void terminateContainer() throws Exception {
        containerManager.createContainer();
        containerManager.startContainer();
        assertTrue(containerManager.terminateContainer());
        ContainerInfo containerInfo = dockerClient.inspectContainer(dockerContainer.getId());
        assertEquals("exited", containerInfo.state().status());
    }

    @Test(expected = ContainerNotFoundException.class)
    public void removeContainer() throws Exception {
        boolean b = containerManager.createContainer();
        containerManager.removeContainer();

        ContainerInfo containerInfo = dockerClient.inspectContainer(dockerContainer.getId());
    }


}