package ir.pint.soltoon.services.docker.container;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties
public class DockerContainerConfig {
    private String name;
    private String image;

    private List<String> environmentVariables = new ArrayList<>();

    private DockerContainerLimits resourceLimits = DockerContainerLimits.NO_LIMITS;

    public DockerContainerConfig() {
    }

    public DockerContainerConfig(String name, String image, ArrayList<String> environmentVariables, DockerContainerLimits resourceLimits) {
        this.name = name;
        this.image = image;
        this.environmentVariables = environmentVariables;
        this.resourceLimits = resourceLimits;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<String> getEnvironmentVariables() {
        return environmentVariables;
    }

    public void setEnvironmentVariables(List<String> environmentVariables) {
        this.environmentVariables = environmentVariables;
    }

    public DockerContainerLimits getResourceLimits() {
        return resourceLimits;
    }

    public void setResourceLimits(DockerContainerLimits resourceLimits) {
        this.resourceLimits = resourceLimits;
    }

    @Override
    public String toString() {
        String rl = "", n = "", i = "", envs = "";

        if (name != null)
            n = name;
        if (image != null)
            i = image;
        if (resourceLimits != null)
            rl = resourceLimits.toString();
        if (environmentVariables != null)
            envs = environmentVariables.toString();

        return String.format("%s {image: %s, limits: %s, env: %s}", n, i, rl, envs);
    }
}
