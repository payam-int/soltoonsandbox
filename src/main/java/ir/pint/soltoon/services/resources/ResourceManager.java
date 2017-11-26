package ir.pint.soltoon.services.resources;

public interface ResourceManager {
    boolean reserveStorage(String storage, long size);

    boolean reserveStorage(long size);

    long getStorageUsage();

    void freeStorage(String storage, long size);

    void freeStorage(long size);

    boolean reserveCpu(int size);

    void freeCpu(int size);

    int getCpuUsage();

    boolean reserveMemory(long size);

    void freeMemory(long size);

    long getMemoryUsage();
}
