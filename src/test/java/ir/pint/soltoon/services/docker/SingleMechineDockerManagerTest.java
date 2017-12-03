package ir.pint.soltoon.services.docker;

import ir.pint.soltoon.services.docker.container.DockerContainer;
import ir.pint.soltoon.services.docker.container.DockerContainerList;
import ir.pint.soltoon.services.docker.events.DockerEventListener;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.Semaphore;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SingleMechineDockerManagerTest {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private SingleMechineDockerManager manager;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void crossLinkContainers() throws Exception {
    }

    @Test
    public void runContainerGroup() throws Exception {
        DockerContainer server = manager.getContainer("test-connection");
        server.addEnvironmentVariable("PORT", "1234");
        server.addEnvironmentVariable("ROLE", "server");
        server.addEnvironmentVariable("HOSTS", "2");
        final Semaphore semaphore = new Semaphore(-2);

        DockerEventListener eh = new DockerEventListener() {
            @Override
            public void onExit() {
                semaphore.release();
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onRefresh() {

            }

            @Override
            public void onRemove() {

            }

            @Override
            public void onCreate() {

            }

            @Override
            public void onTerminate() {
            }
        };



        DockerContainer client1 = manager.getContainer("test-connection");
        client1.addEnvironmentVariable("PORT", "1234");
        client1.addEnvironmentVariable("ROLE", "client");

        DockerContainer client2 = manager.getContainer("test-connection");
        client2.addEnvironmentVariable("PORT", "1234");
        client2.addEnvironmentVariable("ROLE", "client");

        server.addEventListener(eh);
        client1.addEventListener(eh);
        client2.addEventListener(eh);

        DockerContainerList l = context.getBean(DockerContainerList.class);

        l.addContainers(new DockerContainer[]{client1, client2});

        manager.crossLinkContainers(server, l, "HOST");

        DockerContainerList l2 = context.getBean(DockerContainerList.class);
        l2.addContainers(new DockerContainer[]{server, client1, client2});



        manager.runContainerGroup(l2);

        semaphore.acquire();

        assertEquals(0, server.getContainerInfo().getExitCode());
        assertEquals(0, client1.getContainerInfo().getExitCode());
        assertEquals(0, client2.getContainerInfo().getExitCode());


    }

}