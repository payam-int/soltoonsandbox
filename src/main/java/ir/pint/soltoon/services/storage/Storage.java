package ir.pint.soltoon.services.storage;

import java.io.InputStream;

public interface Storage {
    StorageEntity addEntity(InputStream inputStream, FileType fileType, long life, String name);

    StorageEntity addEntity(InputStream inputStream, FileType fileType, long life);

    StorageEntity addEntity(InputStream inputStream, FileType fileType, String name);

    StorageEntity addEntity(InputStream inputStream, FileType fileType);

    StorageEntity addEntity(StorageEntity storageEntity);

    StorageEntity getEntity(String id);

    boolean entityExists(String id);

    boolean entityExists(StorageEntity storageEntity);
}
