package ir.pint.soltoon.services.docker;

import ir.pint.soltoon.services.docker.dockerTask.DockerTask;
import ir.pint.soltoon.services.docker.dockerTask.DockerTaskEventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListSet;

public class DockerContainerPool extends Thread {
    private static Logger logger = LoggerFactory.getLogger(DockerContainerPool.class.getName());

    private DockerService dockerService;
    private ConcurrentLinkedQueue<DockerTask> waitingTasks = new ConcurrentLinkedQueue<>();

    private int taskLimit = 3;
    private int skipper = 0;

    private ConcurrentSkipListSet<DockerTask> runningTasks = new ConcurrentSkipListSet<DockerTask>(new Comparator<DockerTask>() {
        public int compare(DockerTask o1, DockerTask o2) {
            return (int) (o1.getRemainingTime() - o2.getRemainingTime());
        }
    });

    public DockerContainerPool(DockerService dockerService) {
        setName("Docker Scheduling");
        this.dockerService = dockerService;
    }

    public DockerContainerPool(DockerService dockerService, int taskLimit) {
        this(dockerService);
        this.taskLimit = taskLimit;
    }

    public void addTask(DockerTask task) {
        waitingTasks.add(task);
    }

    @Override
    public void run() {
        logger.debug("DockerContainerPool is running.");

        while (true) {
            try {

                checkRunningTasks();

                if (canRunNewTask()) {
                    runNewTaskGroup();
                }

                sleep(100);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void runNewTaskGroup() {
        DockerTask newTask = waitingTasks.poll();
        if (newTask == null)
            return;

        logger.debug("DockerContainerPool can run new task group.");
        for (DockerTask task = newTask; task != null; task = task.getRunWithTask()) {
            task.start(dockerService.getDockerClient());
            task.fireEvent(DockerTaskEventType.STARTED, task);
            runningTasks.add(task);
        }
    }

    private boolean canRunNewTask() {
        return runningTasks.size() < taskLimit;
    }

    private void checkRunningTasks() {
        Iterator<DockerTask> runningTasksIterator = runningTasks.iterator();
        while (runningTasksIterator.hasNext()) {
            DockerTask task = runningTasksIterator.next();

            if (skipper == 0 && !task.isAlive(dockerService.getDockerClient())) {
                runningTasksIterator.remove();
                task.fireEvent(DockerTaskEventType.TERMINATED, task);
            }
            skipper = (skipper + 1) % 10;
            if (task.getRemainingTime() <= 0) {
                if (!task.isAlive(dockerService.getDockerClient())) {
                    runningTasksIterator.remove();
                    task.fireEvent(DockerTaskEventType.TERMINATED, task);
                } else {
                    task.destroy(dockerService.getDockerClient());
                    runningTasksIterator.remove();
                    task.setTimedOut(true);
                    task.fireEvent(DockerTaskEventType.DESTROYED, task);
                }
            }
        }
    }
}
