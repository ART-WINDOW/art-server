package com.doma.artserver.util.storage;


public interface StorageService<T> {
    void uploadFile(String bucketName, String fileName, T data);
    void deleteFile(String bucketName, String fileName);
    T downloadFile(String bucketName, String fileName);
}
