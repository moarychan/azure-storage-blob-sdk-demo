package com.azure.storage.blob.demo.api;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.platform.commons.util.StringUtils;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StorageBlobServiceImplTest {

    private static final String STORAGE_BLOB_ENV_PROFILE_NAME = "blob_profile";
    private static final String DEFAULT_STORAGE_BLOB_ENV_PROFILE_NAME = "moto";

    private StorageBlobService<String> blobService;
    private final String context = "HelloWorld!";

    @BeforeAll
    public void setup() {
        String profile = System.getProperty(STORAGE_BLOB_ENV_PROFILE_NAME);
        if (StringUtils.isBlank(profile)) {
            profile = DEFAULT_STORAGE_BLOB_ENV_PROFILE_NAME;
        }
        StorageBlobDemoProperties properties =
            PropertyLoader.getStorageBlobDemoProperties("/" + profile + ".properties");
        String fileName = properties.getFileNamePrefix() + "-" + System.currentTimeMillis() + ".txt";
        this.blobService = new StorageBlobServiceImpl(properties.getEndpoint(),
            properties.getAccountName(), properties.getAccountKey(), properties.getContainerName(), fileName);
    }

    @Order(1)
    @Test
    public void upload() {
        boolean saved = false;
        try {
            saved = this.blobService.save(this.context);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Assertions.assertTrue(saved);
    }

    @Order(2)
    @Test
    public void getData() {
        String download = null;
        try {
            download = blobService.get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(this.context, download);
    }
}
