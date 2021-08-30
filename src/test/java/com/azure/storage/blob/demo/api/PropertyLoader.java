package com.azure.storage.blob.demo.api;

import org.junit.platform.commons.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyLoader {

    public static StorageBlobDemoProperties getStorageBlobDemoProperties(String propertiesFilePath) {
        StorageBlobDemoProperties blobDemoProperties = null;
        final Properties properties = new Properties();
        try (InputStream inputStream = PropertyLoader.class.getResourceAsStream(propertiesFilePath)) {
            if (inputStream != null) {
                properties.load(inputStream);
                blobDemoProperties = new StorageBlobDemoProperties();
                String endpoint = getConfiguredOrEnvironmentVariable(properties,"storage-endpoint");
                String accountName = getConfiguredOrEnvironmentVariable(properties,"storage-account-name");
                String accountKey = getConfiguredOrEnvironmentVariable(properties,"storage-account-key");
                String containerName = getConfiguredOrEnvironmentVariable(properties,"storage-container-name");
                String fileNamePrefix = getConfiguredOrEnvironmentVariable(properties,"storage-file-name-prefix");
                blobDemoProperties.setEndpoint(endpoint);
                blobDemoProperties.setAccountName(accountName);
                blobDemoProperties.setAccountKey(accountKey);
                blobDemoProperties.setContainerName(containerName);
                blobDemoProperties.setFileNamePrefix(fileNamePrefix);
            }
        } catch (IOException e) {
            // Omitted
        }
        return blobDemoProperties;
    }

    private static String getConfiguredOrEnvironmentVariable(Properties properties, String key) {
        String value = properties.getProperty(key);
        if (StringUtils.isBlank(value)) {
            value = System.getProperty(key);
        }
        return value;
    }
}
