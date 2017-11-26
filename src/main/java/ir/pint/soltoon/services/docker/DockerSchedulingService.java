package ir.pint.soltoon.services.docker;

import ir.pint.soltoon.services.docker.dockerTask.DockerTask;
import org.springframework.stereotype.Service;

@Service
public interface DockerSchedulingService {
    void addTask(DockerTask task);
}
