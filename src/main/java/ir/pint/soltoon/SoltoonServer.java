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
import com.spotify.docker.client.messages.HostConfig;
import ir.pint.soltoon.services.docker.DockerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ResourceBanner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.FileSystemResource;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;

@SpringBootApplication
public class SoltoonServer {
    @Autowired
    private DockerConfig dockerConfig;

    @PostConstruct
    public void init() {
//        System.out.println(dockerConfig.getContainers());
    }

    public static void main(String[] args) throws JsonProcessingException, DockerException, InterruptedException, DockerCertificateException {
        SpringApplication app = new SpringApplication(SoltoonServer.class);
        app.setBanner(new ResourceBanner(new FileSystemResource("src/main/resources/ir/pint/soltoon/banner.txt")));
        app.run(args);
    }
}
