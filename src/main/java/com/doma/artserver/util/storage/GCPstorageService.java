package com.doma.artserver.util.storage;

import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
public class GCPstorageService implements StorageService<InputStream> {
    @Override
    public void uploadFile(String bucketName, String fileName, InputStream data) {
    }

    @Override
    public void deleteFile(String bucketName, String fileName) {

    }

    @Override
    public InputStream downloadFile(String bucketName, String fileName) {
        return null;
    }
}
