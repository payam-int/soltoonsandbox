package ir.pint.soltoon.services.docker.network;

import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.exceptions.NetworkNotFoundException;
import com.spotify.docker.client.messages.NetworkConfig;
import com.spotify.docker.client.messages.NetworkCreation;
import ir.pint.soltoon.services.docker.DockerConfig;
import ir.pint.soltoon.services.docker.DockerNamingService;
import ir.pint.soltoon.services.logger.ExternalExceptionLogger;
import ir.pint.soltoon.services.scheduler.DefaultLongTimeScheduler;
import ir.pint.soltoon.services.scheduler.LongTimeScheduler;
import ir.pint.soltoon.services.scheduler.ScheduledObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.util.Map;

@Component
@Scope("prototype")
@Primary
public class DockerContainerSandboxNetwork implements ScheduledObject, DockerContainerNetwork {
    private String name;
    private int usages = -1;
    private DockerClient dockerClient;
    private String id;
    private Instant objectDie = Instant.now().plus(DefaultLongTimeScheduler.defaultLifetime);
    private Instant createTime;
    private Instant removeTime;

    @Autowired
    private DockerConfig dockerConfig;

    @Autowired
    private ExternalExceptionLogger externalExceptionLogger;

    @Autowired
    private ApplicationContext context;

    public DockerContainerSandboxNetwork() {
    }

    @PostConstruct
    private void init() {
        this.name = context.getBean(DockerNamingService.class).getNetworkName();

        watch();
    }

    public DockerContainerSandboxNetwork(String name) {
        this.name = name;
    }

    @Override
    public DockerClient getDockerClient() {
        return dockerClient;
    }

    @Override
    public void setDockerClient(DockerClient dockerClient) {
        this.dockerClient = dockerClient;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public synchronized void unuse() {
        synchronized (this) {
            if (usages > 0)
                usages--;
        }

        if (usages == 0)
            removeNetwork();
    }

    @Override
    public void watch() {
        context.getBean(LongTimeScheduler.class).addJob(this);
    }

    private boolean removeNetwork() {
        usages--;
        try {
            dockerClient.removeNetwork(this.id);
            removeTime = Instant.now();
            return true;
        } catch (DockerException e) {
            e.printStackTrace();
            externalExceptionLogger.log(e);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public synchronized void use() {

        if (usages == -1) createNetwork();
        if (usages > -1) usages++;
    }

    private boolean createNetwork() {
        NetworkConfig netConfig = NetworkConfig.builder()
                .name(getName())
                .checkDuplicate(true)
                .internal(true)
                .driver("bridge")
                .labels(Map.of(dockerConfig.getDefaultLabel(), "true"))
                .build();

        createTime = Instant.now();

        usages++;
        try {
            NetworkCreation network = dockerClient.createNetwork(netConfig);

            this.id = network.id();
            return true;
        } catch (DockerException e) {
            e.printStackTrace();
            externalExceptionLogger.log(e);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean isDead() {
        return objectDie.isBefore(Instant.now());
    }

    @Override
    public boolean remove() {
        if (removeTime != null)
            return true;

        if (!removeNetwork()) {
            try {
                dockerClient.inspectNetwork(id);
            } catch (NetworkNotFoundException e) {
                removeTime = Instant.now();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                externalExceptionLogger.log(e);
            }
        }

        return false;
    }
}
