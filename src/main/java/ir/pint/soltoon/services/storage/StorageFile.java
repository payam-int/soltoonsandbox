package ir.pint.soltoon.services.storage;

import java.io.InputStream;

public interface StorageFile extends StorageEntity {
    InputStream getInputStream();
}
