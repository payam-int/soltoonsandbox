package ir.pint.soltoon.services.docker.container;

import ir.pint.soltoon.services.docker.events.DockerEventListener;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;

public class DockerEvents extends LinkedBlockingQueue<DockerEventListener> {

    public DockerEvents() {
    }

    public DockerEvents(int capacity) {
        super(capacity);
    }

    public DockerEvents(Collection<? extends DockerEventListener> c) {
        super(c);
    }

    public void exited() {
        Iterator<DockerEventListener> iterator = iterator();
        while (iterator.hasNext())
            iterator.next().onExit();
    }

    public void started() {
        Iterator<DockerEventListener> iterator = iterator();
        while (iterator.hasNext())
            iterator.next().onStart();
    }

    public void refreshed() {
        Iterator<DockerEventListener> iterator = iterator();
        while (iterator.hasNext())
            iterator.next().onRefresh();
    }

    public void removed() {
        Iterator<DockerEventListener> iterator = iterator();
        while (iterator.hasNext())
            iterator.next().onRemove();
    }

    public void created() {
        Iterator<DockerEventListener> iterator = iterator();
        while (iterator.hasNext())
            iterator.next().onCreate();
    }

    public void terminated() {
        Iterator<DockerEventListener> iterator = iterator();
        while (iterator.hasNext())
            iterator.next().onTerminate();
    }
}
