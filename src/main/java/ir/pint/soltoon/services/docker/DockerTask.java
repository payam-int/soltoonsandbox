package ir.pint.soltoon.services.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.exception.BadRequestException;
import com.github.dockerjava.api.exception.ConflictException;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.Link;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.print.Doc;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

// @todo change cpu period to CPUlimit
public class DockerTask {

    private static Logger logger = LoggerFactory.getLogger(DockerTask.class.getName());

    private static AtomicLong DOCKER_TASK_ID = new AtomicLong(1000000);

    private long startTime;
    private String imageName;
    private String containerName;
    private DockerTaskLimits taskLimits;
    private Map<String, String> environmentVariables = new Hashtable<>();
    private List<Link> linksList = new ArrayList<>();
    private List<Bind> bindList = new ArrayList<>();
    private String containerId;
    private DockerTask runWithTask = null;
    private ConcurrentLinkedQueue<DockerTaskEventListener> eventListeners = new ConcurrentLinkedQueue<>();
    private boolean timedOut = false;

    private DockerTask() {
        this.containerName = "SS_" + DOCKER_TASK_ID.getAndIncrement();
    }

    public DockerTask(String imageName, DockerTaskLimits taskLimits) {
        this();
        this.imageName = imageName;
        this.taskLimits = taskLimits;
    }

    public DockerTask(String imageName) {
        this();
        this.imageName = imageName;
        this.taskLimits = new DockerTaskLimits();
    }

    public static void chainTasks(DockerTask... tasks) {
        for (int i = 0; i < tasks.length - 1; i++) {
            tasks[i].setRunWithTask(tasks[i + 1]);
        }
    }

    public boolean isStarted() {
        return startTime == -1;
    }

    public boolean isExpired() {
        return getTaskLimits().getTimeout() > 0 && startTime != -1 && System.currentTimeMillis() - startTime < getTaskLimits().getTimeout() * 1000;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }


    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public DockerTaskLimits getTaskLimits() {
        return taskLimits;
    }

    public void setTaskLimits(DockerTaskLimits taskLimits) {
        this.taskLimits = taskLimits;
    }

    public double getRemainingTime() {
        double timeout = getTaskLimits().getTimeout();
        if (timeout <= 0)
            return Double.MAX_VALUE;
        else
            return timeout * 1000 - (System.currentTimeMillis() - startTime);
    }

    public Map<String, String> getEnvironmentVariables() {
        return environmentVariables;
    }

    public void setEnvironmentVariables(Map<String, String> environmentVariables) {
        this.environmentVariables = environmentVariables;
    }

    public List<Link> getLinksList() {
        return linksList;
    }

    public void setLinksList(List<Link> linksList) {
        this.linksList = linksList;
    }

    public void addLink(Link link) {
        getLinksList().add(link);
    }

    public List<Bind> getBindList() {
        return bindList;
    }

    public void setBindList(List<Bind> bindList) {
        this.bindList = bindList;
    }

    public void addBind(Bind bind) {
        getBindList().add(bind);
    }

    public String getContainerId() {
        return containerId;
    }

    public DockerTask getRunWithTask() {
        return runWithTask;
    }

    public void setRunWithTask(DockerTask runWithTask) {
        this.runWithTask = runWithTask;
    }

    public String getContainerName() {
        return containerName;
    }

    public ConcurrentLinkedQueue<DockerTaskEventListener> getEventListeners() {
        return eventListeners;
    }

    public void setEventListeners(ConcurrentLinkedQueue<DockerTaskEventListener> eventListeners) {
        this.eventListeners = eventListeners;
    }

    public void addEventListener(DockerTaskEventListener eventListener) {
        this.eventListeners.add(eventListener);
    }

    public void fireEvent(DockerTaskEventType eventType, Object... data) {
        for (DockerTaskEventListener e : eventListeners)
            e.handle(eventType, data);
    }

    public void start(DockerClient dockerClient) {
        startTime = System.currentTimeMillis();

        logger.debug(String.format("Creating start command for container %s.", containerName));

        removeContainer(dockerClient);

        CreateContainerCmd containerCmd = dockerClient.createContainerCmd(imageName)
                .withName(containerName);

        if (linksList.size() > 0)
            containerCmd = containerCmd.withLinks(linksList);


//        containerCmd = containerCmd.withCpu("0-1s");

        if (taskLimits.getMemoryLimit() > 0)
            containerCmd = containerCmd.withMemory(taskLimits.getMemoryLimit()).withMemorySwap(taskLimits.getMemoryLimit());

        if (bindList.size() > 0)
            containerCmd = containerCmd.withBinds(bindList);

        if (environmentVariables.size() > 0)
            containerCmd = containerCmd.withEnv(getEnvList());

        CreateContainerResponse exec;
        try {
            exec = containerCmd.exec();
        } catch (BadRequestException e) {
            removeContainer(dockerClient);
            exec = containerCmd.exec();
        }

        this.containerId = exec.getId();
        dockerClient.startContainerCmd(containerId).exec();
    }

    private void removeContainer(DockerClient dockerClient) {
        removeContainer(dockerClient, false);
    }

    private void removeContainer(DockerClient dockerClient, boolean tried) {
        try {
            dockerClient.removeContainerCmd(containerName).exec();
        } catch (NotFoundException e) {

        } catch (ConflictException e) {
            if (!tried) {
                destroy(dockerClient);
                removeContainer(dockerClient, true);
            }
        } catch (Exception e) {
            // ignore
        }
    }

    private List<String> getEnvList() {
        ArrayList<String> sl = new ArrayList<>(environmentVariables.size());
        for (String k : environmentVariables.keySet()) {
            sl.add(String.format("%s=%s", k, environmentVariables.get(k)));
        }
        return sl;
    }

    public void addEnvironmentVariable(String name, String value) {
        environmentVariables.put(name, value);
    }

    public void destroy(DockerClient dockerClient) {
        dockerClient.killContainerCmd(containerId != null ? containerId : containerName).exec();
    }


    public boolean isTimedOut() {
        return timedOut;
    }

    public void setTimedOut(boolean timedOut) {
        this.timedOut = timedOut;
    }

    public boolean isAlive(DockerClient dockerClient) {
        InspectContainerResponse inspectContainerResponse = dockerClient.inspectContainerCmd(containerId).exec();
        return inspectContainerResponse.getState().getRunning();
    }

    public InspectContainerResponse getInspect(DockerClient dockerClient) {
        if (containerId == null)
            return null;

        InspectContainerResponse exec = dockerClient.inspectContainerCmd(containerId).exec();
        return exec;
    }


}
