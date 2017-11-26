package ir.pint.soltoon;

import com.bugsnag.Bugsnag;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.Container;
import org.springframework.boot.ResourceBanner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.FileSystemResource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan
public class SoltoonServer {
    public static void main(String[] args) throws JsonProcessingException, DockerException, InterruptedException, DockerCertificateException {
//        SpringApplication app = new SpringApplication(SoltoonServer.class);
//        app.setBanner(new ResourceBanner(new FileSystemResource("src/main/resources/ir/pint/soltoon/banner.txt")));
//        app.run(args);

        DefaultDockerClient build = DefaultDockerClient.builder().uri("unix:///var/run/docker.sock").build();
        try {
            List<Container> containers = build.listContainers(DockerClient.ListContainersParam.withLabel("soltoonsandbox"));
            for (Container container : containers)
                System.out.println(container.labels());
        } catch (DockerException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
