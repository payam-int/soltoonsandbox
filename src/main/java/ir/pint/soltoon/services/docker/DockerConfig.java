package ir.pint.soltoon.services.docker;

import ir.pint.soltoon.services.docker.container.DockerContainerConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "docker")
public class DockerConfig {
    private String Uri;
    private String defaultLabel;
    private String prefix;
    private List<DockerContainerConfig> containers = new ArrayList<>();
    private DockerResources resources;
    private Boolean buildImages;
    private File imagesDirectory;


    private Map<String, DockerContainerConfig> containerConfigMap = new Hashtable<>();

    @PostConstruct
    public void init() {
        for (DockerContainerConfig containerConfig : containers)
            containerConfigMap.put(containerConfig.getName(), containerConfig);
    }

    public String getUri() {
        return Uri;
    }

    public void setUri(String uri) {
        Uri = uri;
    }

    public String getDefaultLabel() {
        return defaultLabel;
    }

    public void setDefaultLabel(String defaultLabel) {
        this.defaultLabel = defaultLabel;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public List<DockerContainerConfig> getContainers() {
        return containers;
    }

    public void setContainers(List<DockerContainerConfig> containers) {
        this.containers = containers;
    }

    public DockerContainerConfig getContainerConfig(String name) {
        return containerConfigMap.getOrDefault(name, new DockerContainerConfig());
    }

    public DockerResources getResources() {
        return resources;
    }

    public void setResources(DockerResources resources) {
        this.resources = resources;
    }

    public Map<String, DockerContainerConfig> getContainerConfigMap() {
        return containerConfigMap;
    }

    public void setContainerConfigMap(Map<String, DockerContainerConfig> containerConfigMap) {
        this.containerConfigMap = containerConfigMap;
    }

    public Boolean getBuildImages() {
        return buildImages;
    }

    public void setBuildImages(Boolean buildImages) {
        this.buildImages = buildImages;
    }

    public File getImagesDirectory() {
        return imagesDirectory;
    }

    public void setImagesDirectory(File imagesDirectory) {
        this.imagesDirectory = imagesDirectory;
    }
}
