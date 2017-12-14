package ir.pint.soltoon.services.storage;

import java.io.InputStream;

public interface Storage {
    StorageEntity addEntity(InputStream inputStream, FileType fileType, String name, String id, long life);

    StorageEntity addEntity(InputStream inputStream, FileType fileType, String name, long life);

    StorageEntity addEntity(InputStream inputStream, FileType fileType, String name, String id);

    StorageEntity addEntity(InputStream inputStream, FileType fileType, String name);

    StorageEntity addEntity(StorageEntity storageEntity);

    StorageEntity getEntity(String id);

    boolean entityExists(String id);

    boolean entityExists(StorageEntity storageEntity);
}
