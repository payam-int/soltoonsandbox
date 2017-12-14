package ir.pint.soltoon.services.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@ConfigurationProperties(prefix = "localstorage")
public class LocalStorageConfig {
    private String baseDirectory;

    private long fileLifetime = TimeUnit.DAYS.toMillis(1);

    public LocalStorageConfig() {
    }

    public String getBaseDirectory() {
        return baseDirectory;
    }

    public long getFileLifetime() {
        return fileLifetime;
    }
}
