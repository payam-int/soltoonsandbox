package ir.pint.soltoon.services.docker.container;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

@Component
@Scope("prototype")

public class DockerContainerList implements DockerContainerGroup {
    private LinkedList<DockerContainer> containers = new LinkedList<>();

    @Override
    public DockerContainer[] getContainers() {
        return (DockerContainer[]) containers.toArray(new DockerContainer[containers.size()]);
    }

    public void addContainer(DockerContainer container) {
        containers.add(container);
    }

    public void addContainers(DockerContainer[] containers) {
        this.containers.addAll(Arrays.asList(containers));
    }

    @Override
    public boolean remove() {
        boolean r = true;
        for (DockerContainer c : containers)
            r &= c.remove();

        return r;
    }

}
