package ir.pint.soltoon.services.storage;

import java.io.InputStream;

public interface Storage {
    StorageEntity addEntity(StorageEntity storageEntity);

    StorageEntity getEntity(String id);

    boolean entityExists(String id);

    boolean entityExists(StorageEntity storageEntity);
}
