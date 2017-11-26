package ir.pint.soltoon.services.docker.dockerTask;

public class DockerTaskLimit {
    private long memoryLimit;
    private double cpuLimit;
    private double timeout;

    public DockerTaskLimit() {
    }

    public DockerTaskLimit(long memoryLimit, double cpuLimit, double timeout) {
        this.memoryLimit = memoryLimit;
        this.cpuLimit = cpuLimit;
        this.timeout = timeout;
    }

    public long getMemoryLimit() {
        return memoryLimit;
    }

    public void setMemoryLimit(long memoryLimit) {
        this.memoryLimit = memoryLimit;
    }

    public double getCpuLimit() {
        return cpuLimit;
    }

    public void setCpuLimit(double cpuLimit) {
        this.cpuLimit = cpuLimit;
    }

    public double getTimeout() {
        return timeout;
    }

    public void setTimeout(double timeout) {
        this.timeout = timeout;
    }
}
