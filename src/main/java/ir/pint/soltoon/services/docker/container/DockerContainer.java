package ir.pint.soltoon.services.docker.container;

import ir.pint.soltoon.services.docker.DockerNamingService;
import ir.pint.soltoon.services.docker.DockerStorage;
import ir.pint.soltoon.services.docker.api.DockerContainerManagerInterface;
import ir.pint.soltoon.services.docker.api.DockerContainerManager;
import ir.pint.soltoon.services.docker.events.DockerEventListener;
import ir.pint.soltoon.services.docker.network.DockerContainerNetwork;
import ir.pint.soltoon.services.docker.network.DockerContainerNoneNetwork;
import ir.pint.soltoon.services.scheduler.DefaultLongTimeScheduler;
import ir.pint.soltoon.services.scheduler.LongTimeScheduler;
import ir.pint.soltoon.services.scheduler.ScheduledObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

@Component
@Scope("prototype")
public class DockerContainer implements DockerContainerGroup, ScheduledObject {
    private DockerContainerManagerInterface containerApi;
    private DockerContainerInfo containerInfo;
    private DockerContainerConfig dockerContainerConfig;
    private DockerContainerNetwork dockerContainerNetwork = new DockerContainerNoneNetwork();
    private DockerEvents events = new DockerEvents();

    private DockerStorage[] storages;
    private ArrayList<String> environmentVariables = new ArrayList<>();


    private Instant objectDie = Instant.now().plus(DefaultLongTimeScheduler.defaultLifetime);
    private String id;
    private String name = null;

    private final DockerNamingService namingService;

    private final ApplicationContext context;

    @Autowired
    public DockerContainer(DockerNamingService namingService, ApplicationContext context) {
        this.namingService = namingService;
        this.context = context;
    }


    @PostConstruct
    private void init() {
        this.name = namingService.getContainerName();
        watch();
    }

    private Map<String, String> labels = new Hashtable<>();


    @Override
    public DockerContainer[] getContainers() {
        return new DockerContainer[]{this};
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DockerContainerManagerInterface getContainerApi() {
        return containerApi;
    }

    public void setContainerApi(DockerContainerManager containerApi) {
        this.containerApi = containerApi;
    }

    public DockerContainerInfo getContainerInfo() {
        if (containerInfo == null)
            containerInfo = new DockerContainerInfo();

        return containerInfo;
    }

    public void setContainerInfo(DockerContainerInfo containerInfo) {
        this.containerInfo = containerInfo;
    }

    public DockerStorage[] getStorages() {
        return storages;
    }

    public void setStorages(DockerStorage[] storages) {
        this.storages = storages;
    }

    public String getName() {
        return this.name == null ? getId() : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContainerApi(DockerContainerManagerInterface containerApi) {
        this.containerApi = containerApi;
    }

    public DockerContainerConfig getDockerContainerConfig() {
        return dockerContainerConfig;
    }

    public void setDockerContainerConfig(DockerContainerConfig dockerContainerConfig) {
        this.dockerContainerConfig = dockerContainerConfig;
    }

    public DockerContainerNetwork getDockerContainerNetwork() {
        return dockerContainerNetwork;
    }

    public void setDockerContainerNetwork(DockerContainerNetwork dockerContainerNetwork) {
        this.dockerContainerNetwork = dockerContainerNetwork;
    }

    public ArrayList<String> getEnvironmentVariables() {
        return environmentVariables;
    }

    public void setEnvironmentVariables(ArrayList<String> environmentVariables) {
        this.environmentVariables = environmentVariables;
    }

    public void addEnvironmentVariable(String env) {
        this.environmentVariables.add(env);
    }

    public void addEnvironmentVariable(String name, String variable) {
        this.environmentVariables.add(String.format("%s=%s", name, variable));
    }


    @Override
    public void watch() {
        context.getBean(LongTimeScheduler.class).addJob(this);
    }

    @Override
    public boolean isDead() {
        return objectDie.isBefore(Instant.now());
    }

    @Override
    public boolean remove() {
        containerApi.removeContainer();
        return true;
    }


    @Override
    public long getScheduledTime() {
        return objectDie.toEpochMilli();
    }

    public Map<String, String> getLabels() {
        return labels;
    }

    public void setLabels(Map<String, String> labels) {
        this.labels = labels;
    }

    public void unuseNetwork() {
        if (dockerContainerNetwork != null) {
            dockerContainerNetwork.unuse();
            dockerContainerNetwork = null;
        }
    }

    public void addEventListener(DockerEventListener l) {
        events.add(l);
    }

    public DockerEvents getEvents() {
        return events;
    }
}
