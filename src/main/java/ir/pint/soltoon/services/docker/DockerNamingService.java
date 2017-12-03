package ir.pint.soltoon.services.docker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;

@Service
public class DockerNamingService {
    private AtomicLong networkId = new AtomicLong(1000000000L);
    private AtomicLong containerId = new AtomicLong(1000000000L);

    private final static String prefix = String.format("%05d", ((int) (Math.random() * 100000)));

    @Autowired
    private DockerConfig dockerConfig;

    public String getNetworkName() {
        return String.format("%s_%s_%d", dockerConfig.getPrefix(), prefix, networkId.getAndIncrement());
    }

    public String getContainerName() {
        return String.format("%s_%s_%d", dockerConfig.getPrefix(), prefix, containerId.getAndIncrement());
    }
}
