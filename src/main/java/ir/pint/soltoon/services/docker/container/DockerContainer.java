package ir.pint.soltoon.services.docker.container;

import ir.pint.soltoon.services.docker.DockerStorage;
import ir.pint.soltoon.services.docker.api.DockerContainerApi;
import ir.pint.soltoon.services.docker.api.DockerContainerManager;
import ir.pint.soltoon.services.docker.network.DockerContainerNetwork;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public class DockerContainer implements DockerContainerGroup {
    private DockerContainerApi containerApi;
    private DockerContainerInfo containerInfo;
    private DockerContainerConfig dockerContainerConfig;
    private DockerContainerNetwork dockerContainerNetwork;

    private DockerStorage[] storages;
    private String[] network;
    private ArrayList<String> environmentVariables;


    private Instant maxLife = Instant.now().plusSeconds(24 * 3600);
    private String id;
    private String name = null;

    public Instant getMaxLife() {
        return maxLife;
    }

    @Override
    public DockerContainer[] getContainers() {
        return new DockerContainer[]{this};
    }

    public void setMaxLife(Instant maxLife) {
        this.maxLife = maxLife;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DockerContainerApi getContainerApi() {
        return containerApi;
    }

    public void setContainerApi(DockerContainerManager containerApi) {
        this.containerApi = containerApi;
    }

    public DockerContainerInfo getContainerInfo() {
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

    public String[] getNetwork() {
        return network;
    }

    public void setNetwork(String[] network) {
        this.network = network;
    }

    public void setNetwork(String network) {
        this.network = new String[]{network};
    }

    public String getName() {
        return this.name == null ? getId() : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContainerApi(DockerContainerApi containerApi) {
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
        this.environmentVariables.add(String.format("%s:%s", name, variable));
    }

    @Override
    public void remove() {
        dockerContainerNetwork.unuse();
    }
}
