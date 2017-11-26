package ir.pint.soltoon;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.exceptions.DockerCertificateException;
import ir.pint.soltoon.services.docker.DockerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ResourceBanner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.core.io.FileSystemResource;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan
public class SoltoonServer {

    @Autowired
    DockerService dockerService;

    public static void main(String[] args) throws DockerCertificateException {
        SpringApplication app = new SpringApplication(SoltoonServer.class);
        app.setBanner(new ResourceBanner(new FileSystemResource("src/main/resources/ir/pint/soltoon/banner.txt")));
        app.run(args);
    }
}
