package ir.pint.soltoon.services.docker.container;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
public class DockerContainerFactory {
    public DockerContainer createContainer() {
        return new DockerContainer();
    }

    public DockerContainer createContainer(String tag) {
        return new DockerContainer();
    }
}
