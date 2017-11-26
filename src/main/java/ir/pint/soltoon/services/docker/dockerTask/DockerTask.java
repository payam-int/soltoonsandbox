package ir.pint.soltoon.services.docker.dockerTask;

import com.spotify.docker.client.DockerClient;

import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

// @todo add link
public class DockerTask {

    private static Logger logger = LoggerFactory.getLogger(DockerTask.class.getName());
    private static AtomicLong DOCKER_TASK_ID = new AtomicLong(1000000);

    private long startTime;
    private String imageName;
    private String containerName;
    private DockerTaskLimit taskLimits;
    private Map<String, String> environmentVariables = new Hashtable<>();
    private List<DockerLink> linksList = new ArrayList<>();
    private List<DockerBind> bindList = new ArrayList<>();
    private String containerId;
    private String networkId;
    private DockerTask runWithTask = null;
    private ConcurrentLinkedQueue<DockerTaskEventListener> eventListeners = new ConcurrentLinkedQueue<>();
    private boolean timedOut = false;

    private DockerTask() {
        this.containerName = "SS_" + DOCKER_TASK_ID.getAndIncrement();
    }

    public DockerTask(String imageName, DockerTaskLimit taskLimits) {
        this();
        this.imageName = imageName;
        this.taskLimits = taskLimits;
    }

    public DockerTask(String imageName) {
        this();
        this.imageName = imageName;
        this.taskLimits = new DockerTaskLimit();
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

    public DockerTaskLimit getTaskLimits() {
        return taskLimits;
    }

    public void setTaskLimits(DockerTaskLimit taskLimits) {
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

    public List<DockerLink> getLinksList() {
        return linksList;
    }

    public void setLinksList(List<DockerLink> linksList) {
        this.linksList = linksList;
    }

    public void addLink(DockerLink link) {
        getLinksList().add(link);
    }

    public List<DockerBind> getBindList() {
        return bindList;
    }

    public void setBindList(List<DockerBind> bindList) {
        this.bindList = bindList;
    }

    public void addBind(DockerBind bind) {
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


        HostConfig.Builder hostConfigBuilder = HostConfig.builder();

        for (DockerBind bind : bindList)
            hostConfigBuilder = hostConfigBuilder.appendBinds(bind.toBindString());


        if (taskLimits.getMemoryLimit() > 0)
            hostConfigBuilder = hostConfigBuilder.memory(taskLimits.getMemoryLimit()).memorySwap(taskLimits.getMemoryLimit());

        if (taskLimits.getCpuLimit() > 0)
            hostConfigBuilder = hostConfigBuilder.cpuQuota((long) (100000 * taskLimits.getCpuLimit()));

        HostConfig hostConfig = hostConfigBuilder.networkMode(networkId).build();

//
//        ContainerConfig.NetworkingConfig.create(endpointConfigHashtable)



        final ContainerConfig containerConfig = ContainerConfig.builder()
                .env(getEnvList())
                .hostConfig(hostConfig)
                .image(imageName)
                .hostname(containerName)
                .build();

        ContainerCreation container;
        try {
            container = dockerClient.createContainer(containerConfig, containerName);
            this.containerId = container.id();
            dockerClient.startContainer(containerId);
        } catch (DockerException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


//        if (linksList.size() > 0)
//            containerCmd = containerCmd.withLinks(linksList);


//        containerCmd = containerCmd.withCpu("0-1s");

//        CreateContainerResponse exec;
//        try {
//            exec = containerCmd.exec();
//        } catch (BadRequestException e) {
//            removeContainer(dockerClient);
//            exec = containerCmd.exec();
//        }


//        dockerClient.startContainerCmd(containerId).exec();
    }

    private void removeContainer(DockerClient dockerClient) {
        removeContainer(dockerClient, false);
    }

    private void removeContainer(DockerClient dockerClient, boolean tried) {
        try {
            dockerClient.removeContainer(containerName);
        } catch (Exception e) {
            if (!tried) {
                destroy(dockerClient);
                removeContainer(dockerClient, true);
            }
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
        try {
            dockerClient.killContainer(containerId != null ? containerId : containerName);
        } catch (DockerException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public boolean isTimedOut() {
        return timedOut;
    }

    public void setTimedOut(boolean timedOut) {
        this.timedOut = timedOut;
    }

    public boolean isAlive(DockerClient dockerClient) {
        try {
            ContainerInfo containerInfo = dockerClient.inspectContainer(containerName);
            return containerInfo.state().running();
        } catch (DockerException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ContainerInfo getInspect(DockerClient dockerClient) {
        if (containerId == null)
            return null;
        try {
            return dockerClient.inspectContainer(containerName);
        } catch (DockerException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void connectTo(DockerTask task, String taskDns, String secondTaskDns) {

    }


}
