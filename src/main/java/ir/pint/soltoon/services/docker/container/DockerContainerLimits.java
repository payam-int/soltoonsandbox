package ir.pint.soltoon.services.docker.container;

public class DockerContainerLimits {
    private long cpu = -1;
    private long memory = -1;
    private long swapMemory = -1;
    private long timeout = -1;

    public DockerContainerLimits() {
    }

    public DockerContainerLimits(long cpu, long memory, long swapMemory, long timeout) {
        this.cpu = cpu;
        this.memory = memory;
        this.swapMemory = swapMemory;
        this.timeout = timeout;
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

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    @Override
    public String toString() {
        return String.format("[cpu=%d, memory=%d, swapMemory=%d, timeout=%d]", cpu, memory, swapMemory, timeout);
    }
}
