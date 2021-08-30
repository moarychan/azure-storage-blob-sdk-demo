package com.azure.storage.blob.demo.api;

import java.io.IOException;

public interface StorageBlobService<T> {
    boolean save(T context) throws IOException;
    T get() throws IOException;
}
