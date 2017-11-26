package ir.pint.soltoon.services.docker;

import com.spotify.docker.client.messages.ContainerInfo;
import ir.pint.soltoon.SoltoonServer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
//@RunWith(SoltoonServer.class)
//@SpringBootTest
//@Import(MyTestsConfiguration.class)
public class TestDockerService {


    public static Logger logger = LoggerFactory.getLogger(TestDockerService.class.getName());
//
//
//
//    @Test
//    public void runningExitCodeImage() {
//        CountDownLatch lock = new CountDownLatch(1);
//        Docker docker = new Docker();
//        DockerTask dockerTask = new DockerTask("test-exitcode:latest");
//        dockerTask.addEnvironmentVariable("EXITCODE", "10");
//
//        dockerTask.addEventListener(new DockerTaskEventListener() {
//            @Override
//            public void handle(DockerTaskEventType eventType, Object... data) {
//                logger.debug(String.format("Event %s fired!", eventType.toString()));
//                System.out.println(eventType);
//                System.out.println(((DockerTask) data[0]).getImageName());
//                if (eventType != DockerTaskEventType.STARTED)
//                    lock.countDown();
//            }
//        });
//
//        docker.addTask(dockerTask);
//        try {
//            lock.await(50, TimeUnit.SECONDS);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//            assertTrue(false);
//        }
//        ContainerInfo inspect = dockerTask.getInspect(docker.getDockerClient());
//        System.out.println(inspect.state().running());
//        assertEquals(10, (int) inspect.state().exitCode());
//    }
//
//    @Test
//    public void memoryLimitTest() {
//        CountDownLatch lock = new CountDownLatch(1);
//        Docker docker = new Docker();
//        DockerTask dockerTask = new DockerTask("test-memorylimit:latest", new DockerTaskLimit(1024 * 1024 * 50, 0, 5000));
//
//        dockerTask.addEventListener(new DockerTaskEventListener() {
//            @Override
//            public void handle(DockerTaskEventType eventType, Object... data) {
//                logger.debug(String.format("Event %s fired!", eventType.toString()));
//                System.out.println(eventType);
//                System.out.println(((DockerTask) data[0]).getImageName());
//                if (eventType != DockerTaskEventType.STARTED)
//                    lock.countDown();
//            }
//        });
//
//        docker.addTask(dockerTask);
//        try {
//            lock.await(50, TimeUnit.SECONDS);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//            assertTrue(false);
//        }
//        ContainerInfo inspect = dockerTask.getInspect(docker.getDockerClient());
//        System.out.println(inspect.state().status());
//        assertEquals(137, (int) inspect.state().exitCode());
//    }
//
//
//    @Test
//    public void longRuntimeTest() {
//        CountDownLatch lock = new CountDownLatch(1);
//        Docker docker = new Docker();
//        DockerTask dockerTask = new DockerTask("test-longruntime:latest", new DockerTaskLimit(0, 0, 5));
//
//        dockerTask.addEventListener(new DockerTaskEventListener() {
//            @Override
//            public void handle(DockerTaskEventType eventType, Object... data) {
//                logger.debug(String.format("Event %s fired!", eventType.toString()));
//                System.out.println(eventType);
//                System.out.println(((DockerTask) data[0]).getImageName());
//                if (eventType == DockerTaskEventType.DESTROYED)
//                    lock.countDown();
//            }
//        });
//
//        docker.addTask(dockerTask);
//        try {
//            lock.await(50, TimeUnit.SECONDS);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//            assertTrue(false);
//        }
//        ContainerInfo inspect = dockerTask.getInspect(docker.getDockerClient());
//        System.out.println(inspect.state().status());
//        assertEquals(137, (int) inspect.state().exitCode());
//    }
//
//    @Test
//    public void chainedTasksTest() {
//        CountDownLatch lock = new CountDownLatch(3);
//        Docker docker = new Docker();
//        DockerTaskEventListener dockerTaskEventListener = new DockerTaskEventListener() {
//            @Override
//            public void handle(DockerTaskEventType eventType, Object... data) {
//                logger.debug(String.format("Event %s fired!", eventType.toString()));
//                System.out.println(eventType);
//                System.out.println(((DockerTask) data[0]).getContainerName());
//                if (eventType == DockerTaskEventType.DESTROYED)
//                    lock.countDown();
//            }
//        };
//        DockerTask dockerTask = new DockerTask("test-longruntime:latest", new DockerTaskLimit(0, 0, 5));
//        dockerTask.addEventListener(dockerTaskEventListener);
//        DockerTask dockerTask2 = new DockerTask("test-longruntime:latest", new DockerTaskLimit(0, 0, 5));
//        dockerTask2.addEventListener(dockerTaskEventListener);
//        DockerTask dockerTask3 = new DockerTask("test-longruntime:latest", new DockerTaskLimit(0, 0, 5));
//        dockerTask3.addEventListener(dockerTaskEventListener);
//
//        DockerTask.chainTasks(dockerTask, dockerTask2, dockerTask3);
//
//        docker.addTask(dockerTask);
//        try {
//            lock.await(500, TimeUnit.SECONDS);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//            assertTrue(false);
//        }
//        assertEquals(137, (int) dockerTask.getInspect(docker.getDockerClient()).state().exitCode());
//        assertEquals(137, (int) dockerTask2.getInspect(docker.getDockerClient()).state().exitCode());
//        assertEquals(137, (int) dockerTask3.getInspect(docker.getDockerClient()).state().exitCode());
//    }
//
//    @Test
//    public void cpuUsageTest(){
//        CountDownLatch lock = new CountDownLatch(2);
//        Docker docker = new Docker();
//        DockerTask dockerTask = new DockerTask("test-cpuusage:latest", new DockerTaskLimit(0, 0.25, 10));
//
//        dockerTask.addEventListener(new DockerTaskEventListener() {
//            @Override
//            public void handle(DockerTaskEventType eventType, Object... data) {
//                logger.debug(String.format("Event %s fired!", eventType.toString()));
//                System.out.println(eventType);
//                System.out.println(((DockerTask) data[0]).getImageName());
//                if (eventType != DockerTaskEventType.STARTED)
//                    lock.countDown();
//            }
//        });
//
//        docker.addTask(dockerTask);
//
//        DockerTask dockerTask2 = new DockerTask("test-cpuusage:latest", new DockerTaskLimit(0, 0.25, 10));
//
//        dockerTask2.addEventListener(new DockerTaskEventListener() {
//            @Override
//            public void handle(DockerTaskEventType eventType, Object... data) {
//                logger.debug(String.format("Event %s fired!", eventType.toString()));
//                System.out.println(eventType);
//                System.out.println(((DockerTask) data[0]).getImageName());
//                if (eventType != DockerTaskEventType.STARTED)
//                    lock.countDown();
//            }
//        });
//
//        docker.addTask(dockerTask2);
//        try {
//            lock.await(50, TimeUnit.SECONDS);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//            assertTrue(false);
//        }
//        ContainerInfo inspect = dockerTask.getInspect(docker.getDockerClient());
//        System.out.println(inspect.state().running());
//        assertEquals(137, (int) inspect.state().exitCode());
//    }
//
//    // not a real test
//    public void floodTest() {
//        CountDownLatch lock = new CountDownLatch(100);
//        Docker docker = new Docker();
//        DockerTaskEventListener dockerTaskEventListener = new DockerTaskEventListener() {
//            @Override
//            public void handle(DockerTaskEventType eventType, Object... data) {
//                logger.debug(String.format("Event %s fired!", eventType.toString()));
//                System.out.println(eventType);
//                System.out.println(((DockerTask) data[0]).getContainerName());
//                if (eventType == DockerTaskEventType.DESTROYED)
//                    lock.countDown();
//            }
//        };
//
//        for (int i = 0; i < 100; i++) {
//            DockerTask dockerTask = new DockerTask("test-longruntime:latest", new DockerTaskLimit(0, 0, 5));
//            dockerTask.addEventListener(dockerTaskEventListener);
//            docker.addTask(dockerTask);
//        }
//        try {
//            lock.await(500, TimeUnit.SECONDS);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//            assertTrue(false);
//        }
//    }
}
