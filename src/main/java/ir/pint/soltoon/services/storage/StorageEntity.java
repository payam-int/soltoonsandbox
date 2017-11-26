package ir.pint.soltoon.services.storage;

import java.io.InputStream;

public interface StorageEntity {
    String getEntityId();
    String getId();
    InputStream getInputStream();
}
