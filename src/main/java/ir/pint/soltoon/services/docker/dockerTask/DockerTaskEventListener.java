package ir.pint.soltoon.services.docker.dockerTask;

public interface DockerTaskEventListener {
    void handle(DockerTaskEventType eventType, Object... data);
}
