package ir.pint.soltoon.services.storage;

public interface StorageManager {
    Storage getStorage(String name);

    void addStorage(Storage storage);

    void addStorage(String name, Storage storage);
}
