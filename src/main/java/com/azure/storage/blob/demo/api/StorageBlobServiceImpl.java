package com.azure.storage.blob.demo.api;

import com.azure.core.http.policy.HttpLogDetailLevel;
import com.azure.core.http.policy.HttpLogOptions;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.BlockBlobItem;
import com.azure.storage.blob.specialized.BlockBlobClient;
import com.azure.storage.common.StorageSharedKeyCredential;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class StorageBlobServiceImpl implements StorageBlobService<String> {

    private BlockBlobClient blobClient;

    public StorageBlobServiceImpl(String endpoint,
                                  String accountName,
                                  String accountKey,
                                  String containerName,
                                  String fileName) {
        StorageSharedKeyCredential credential = new StorageSharedKeyCredential(accountName, accountKey);

        HttpLogOptions logOptions = new HttpLogOptions();
        logOptions.setLogLevel(HttpLogDetailLevel.BODY_AND_HEADERS);
        logOptions.setPrettyPrintBody(true);
        BlobServiceClient storageClient = new BlobServiceClientBuilder()
            .httpLogOptions(logOptions)
            .endpoint(endpoint)
            .credential(credential)
            .buildClient();
        BlobContainerClient blobContainerClient = storageClient.getBlobContainerClient(containerName);
//        if (!blobContainerClient.exists()) {
//            blobContainerClient.create();
//        }
        blobClient = blobContainerClient.getBlobClient(fileName).getBlockBlobClient();
    }

    @Override
    public boolean save(String context) throws IOException {
        InputStream dataStream = new ByteArrayInputStream(context.getBytes(StandardCharsets.UTF_8));
        BlockBlobItem blockBlobItem = blobClient.upload(dataStream, context.length());
        dataStream.close();
        return (blockBlobItem != null && blockBlobItem.getLastModified() != null);
    }

    @Override
    public String get() throws IOException {
        int dataSize = (int) blobClient.getProperties().getBlobSize();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(dataSize);
        blobClient.downloadStream(outputStream);
        outputStream.close();
        return new String(outputStream.toByteArray(), StandardCharsets.UTF_8);
    }
}
