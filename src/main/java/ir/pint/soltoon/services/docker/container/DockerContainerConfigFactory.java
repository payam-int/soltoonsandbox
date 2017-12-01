package ir.pint.soltoon.services.docker.container;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DockerContainerConfigFactory {
    @Bean
    public DockerContainerConfig getConfig() {
        return new DockerContainerConfig();
    }
}
