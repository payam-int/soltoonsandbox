package ir.pint.soltoon.services.docker.network;

public class DockerNetworkConfig {
    private boolean isolated = true;

    public DockerNetworkConfig() {
    }

    public DockerNetworkConfig(boolean isolated) {
        this.isolated = isolated;
    }

    public boolean isIsolated() {
        return isolated;
    }

    public void setIsolated(boolean isolated) {
        this.isolated = isolated;
    }
}
