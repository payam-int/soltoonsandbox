package ir.pint.soltoon.services.docker;

public class DockerContainer {
    private DockerContainerManager containerManager;
    private DockerContainerInfo containerInfo;
    private DockerResourceLimits resourceLimits;
    private DockerContainer[] links;
    private DockerStorage[] storages;


    private long life;
    private String id;
    private String name;

    public long getLife() {
        return life;
    }

    public void setLife(long life) {
        this.life = life;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DockerContainerManager getContainerManager() {
        return containerManager;
    }

    public void setContainerManager(DockerContainerManager containerManager) {
        this.containerManager = containerManager;
    }

    public DockerContainerInfo getContainerInfo() {
        return containerInfo;
    }

    public void setContainerInfo(DockerContainerInfo containerInfo) {
        this.containerInfo = containerInfo;
    }

    public DockerResourceLimits getResourceLimits() {
        return resourceLimits;
    }

    public void setResourceLimits(DockerResourceLimits resourceLimits) {
        this.resourceLimits = resourceLimits;
    }
}
