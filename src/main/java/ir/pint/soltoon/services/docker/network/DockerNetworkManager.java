package ir.pint.soltoon.services.docker.network;

import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.Network;
import ir.pint.soltoon.services.docker.DockerConfig;
import ir.pint.soltoon.services.logger.ExternalExceptionLogger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
@Scope("prototype")
public class DockerNetworkManager implements DockerNetwork {
    private Logger logger = LoggerFactory.getLogger(getClass().getName());

    private final ExternalExceptionLogger externalExceptionLogger;

    private DockerClient dockerClient;

    private final DockerConfig dockerConfig;

    @Autowired
    public DockerNetworkManager(ExternalExceptionLogger externalExceptionLogger, DockerConfig dockerConfig) {
        this.externalExceptionLogger = externalExceptionLogger;
        this.dockerConfig = dockerConfig;
    }

    @Override
    public void setClient(DockerClient dockerClient) {
        this.dockerClient = dockerClient;
    }

    @Override
    public void init() {
        try {
            List<Network> networks = dockerClient.listNetworks(DockerClient.ListNetworksParam.withLabel(dockerConfig.getDefaultLabel()));
            if (networks.size() > 0)
                logger.info(String.format("There is %d networks tagged with name %s.", networks.size(), dockerConfig.getDefaultLabel()));
            else
                logger.info(String.format("There is no network tagged with name %s.", dockerConfig.getDefaultLabel()));

            for (Network n : networks) {
                try {
                    dockerClient.removeNetwork(n.id());
                } catch (DockerException e) {
                    e.printStackTrace();
                    externalExceptionLogger.log(e);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        } catch (DockerException e) {
            e.printStackTrace();
            externalExceptionLogger.log(e);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
