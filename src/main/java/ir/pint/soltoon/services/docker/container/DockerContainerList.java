package ir.pint.soltoon.services.docker.container;

import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

public class DockerContainerList implements DockerContainerGroup {
    private LinkedList<DockerContainer> containers = new LinkedList<>();

    @Override
    public DockerContainer[] getContainers() {
        return (DockerContainer[]) containers.toArray(new DockerContainer[containers.size()]);
    }

    public void addContainer(DockerContainer container) {
        containers.add(container);
    }

    @Override
    public boolean remove() {
        boolean r = true;
        for (DockerContainer c : containers)
            r &= c.remove();

        return r;
    }
}
