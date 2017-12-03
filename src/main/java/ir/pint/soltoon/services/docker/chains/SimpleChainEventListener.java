package ir.pint.soltoon.services.docker.chains;

import ir.pint.soltoon.services.docker.container.DockerContainer;
import ir.pint.soltoon.services.docker.container.DockerContainerGroup;
import ir.pint.soltoon.services.docker.events.DockerEventListener;

public class SimpleChainEventListener implements DockerEventListener {
    private DockerContainer nextContainer;

    @Override
    public void onExit() {
        nextContainer.getContainerApi().startContainer();
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onRemove() {

    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onTerminate() {

    }
}
