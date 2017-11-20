package ir.pint.soltoon.services.docker;

public interface DockerTaskEventListener {
    void handle(DockerTaskEventType eventType, Object... data);
}
