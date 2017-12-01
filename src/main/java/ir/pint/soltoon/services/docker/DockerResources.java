package ir.pint.soltoon.services.docker;

public class DockerResources {
    private long cpu;
    private long memory;
    private long swapMemory;
    private int containers;
    private int networks;
    private long containerLifetime;

    public DockerResources() {
    }

    public long getCpu() {
        return cpu;
    }

    public void setCpu(long cpu) {
        this.cpu = cpu;
    }

    public long getMemory() {
        return memory;
    }

    public void setMemory(long memory) {
        this.memory = memory;
    }

    public long getSwapMemory() {
        return swapMemory;
    }

    public void setSwapMemory(long swapMemory) {
        this.swapMemory = swapMemory;
    }

    public int getContainers() {
        return containers;
    }

    public void setContainers(int containers) {
        this.containers = containers;
    }

    public int getNetworks() {
        return networks;
    }

    public void setNetworks(int networks) {
        this.networks = networks;
    }

    public long getContainerLifetime() {
        return containerLifetime;
    }

    public void setContainerLifetime(long containerLifetime) {
        this.containerLifetime = containerLifetime;
    }
}
