package ir.pint.soltoon.services.docker;

import com.github.dockerjava.api.command.InspectContainerResponse;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestDockerService {


    public static Logger logger = LoggerFactory.getLogger(TestDockerService.class.getName());

    @BeforeClass
    public static void before() {
        BasicConfigurator.configure();
    }


    @Test
    public void runningExitCodeImage() {
        CountDownLatch lock = new CountDownLatch(1);
        Docker docker = new Docker();
        DockerTask dockerTask = new DockerTask("test-exitcode:latest");
        dockerTask.addEnvironmentVariable("EXITCODE", "10");

        dockerTask.addEventListener(new DockerTaskEventListener() {
            @Override
            public void handle(DockerTaskEventType eventType, Object... data) {
                logger.debug(String.format("Event %s fired!", eventType.toString()));
                System.out.println(eventType);
                System.out.println(((DockerTask) data[0]).getImageName());
                if (eventType != DockerTaskEventType.STARTED)
                    lock.countDown();
            }
        });

        docker.addTask(dockerTask);
        try {
            lock.await(50, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
            assertTrue(false);
        }
        InspectContainerResponse inspect = dockerTask.getInspect(docker.getDockerClient());
        System.out.println(inspect.getState().getStatus());
        assertEquals(10, (int) inspect.getState().getExitCode());
    }

    @Test
    public void memoryLimitTest() {
        CountDownLatch lock = new CountDownLatch(1);
        Docker docker = new Docker();
        DockerTask dockerTask = new DockerTask("test-memorylimit:latest", new DockerTaskLimits(1024 * 1024 * 50, 0, 5000));

        dockerTask.addEventListener(new DockerTaskEventListener() {
            @Override
            public void handle(DockerTaskEventType eventType, Object... data) {
                logger.debug(String.format("Event %s fired!", eventType.toString()));
                System.out.println(eventType);
                System.out.println(((DockerTask) data[0]).getImageName());
                if (eventType != DockerTaskEventType.STARTED)
                    lock.countDown();
            }
        });

        docker.addTask(dockerTask);
        try {
            lock.await(50, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
            assertTrue(false);
        }
        InspectContainerResponse inspect = dockerTask.getInspect(docker.getDockerClient());
        System.out.println(inspect.getState().getStatus());
        assertEquals(137, (int) inspect.getState().getExitCode());
    }


    @Test
    public void longRuntimeTest() {
        CountDownLatch lock = new CountDownLatch(1);
        Docker docker = new Docker();
        DockerTask dockerTask = new DockerTask("test-longruntime:latest", new DockerTaskLimits(0, 0, 5));

        dockerTask.addEventListener(new DockerTaskEventListener() {
            @Override
            public void handle(DockerTaskEventType eventType, Object... data) {
                logger.debug(String.format("Event %s fired!", eventType.toString()));
                System.out.println(eventType);
                System.out.println(((DockerTask) data[0]).getImageName());
                if (eventType == DockerTaskEventType.DESTROYED)
                    lock.countDown();
            }
        });

        docker.addTask(dockerTask);
        try {
            lock.await(50, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
            assertTrue(false);
        }
        InspectContainerResponse inspect = dockerTask.getInspect(docker.getDockerClient());
        System.out.println(inspect.getState().getStatus());
        assertEquals(137, (int) inspect.getState().getExitCode());
    }

    @Test
    public void chainedTasksTest() {
        CountDownLatch lock = new CountDownLatch(3);
        Docker docker = new Docker();
        DockerTaskEventListener dockerTaskEventListener = new DockerTaskEventListener() {
            @Override
            public void handle(DockerTaskEventType eventType, Object... data) {
                logger.debug(String.format("Event %s fired!", eventType.toString()));
                System.out.println(eventType);
                System.out.println(((DockerTask) data[0]).getContainerName());
                if (eventType == DockerTaskEventType.DESTROYED)
                    lock.countDown();
            }
        };
        DockerTask dockerTask = new DockerTask("test-longruntime:latest", new DockerTaskLimits(0, 0, 5));
        dockerTask.addEventListener(dockerTaskEventListener);
        DockerTask dockerTask2 = new DockerTask("test-longruntime:latest", new DockerTaskLimits(0, 0, 5));
        dockerTask2.addEventListener(dockerTaskEventListener);
        DockerTask dockerTask3 = new DockerTask("test-longruntime:latest", new DockerTaskLimits(0, 0, 5));
        dockerTask3.addEventListener(dockerTaskEventListener);

        DockerTask.chainTasks(dockerTask, dockerTask2, dockerTask3);

        docker.addTask(dockerTask);
        try {
            lock.await(500, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
            assertTrue(false);
        }
        assertEquals(137, (int) dockerTask.getInspect(docker.getDockerClient()).getState().getExitCode());
        assertEquals(137, (int) dockerTask2.getInspect(docker.getDockerClient()).getState().getExitCode());
        assertEquals(137, (int) dockerTask3.getInspect(docker.getDockerClient()).getState().getExitCode());
    }

    // not a real test
    public void floodTest() {
        CountDownLatch lock = new CountDownLatch(100);
        Docker docker = new Docker();
        DockerTaskEventListener dockerTaskEventListener = new DockerTaskEventListener() {
            @Override
            public void handle(DockerTaskEventType eventType, Object... data) {
                logger.debug(String.format("Event %s fired!", eventType.toString()));
                System.out.println(eventType);
                System.out.println(((DockerTask) data[0]).getContainerName());
                if (eventType == DockerTaskEventType.DESTROYED)
                    lock.countDown();
            }
        };

        for (int i = 0; i < 100; i++) {
            DockerTask dockerTask = new DockerTask("test-longruntime:latest", new DockerTaskLimits(0, 0, 5));
            dockerTask.addEventListener(dockerTaskEventListener);
            docker.addTask(dockerTask);
        }
        try {
            lock.await(500, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }
}
