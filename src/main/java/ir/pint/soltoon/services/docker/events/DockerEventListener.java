package ir.pint.soltoon.services.docker.events;

public interface DockerEventListener {
    void onExit();

    void onStart();

    void onRefresh();

    void onRemove();

    void onCreate();

    void onTerminate();
}
