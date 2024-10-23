package com.doma.artserver.util.storage;


public interface StorageService<T> {
    String uploadFile(String fileName, T data);
    void deleteFile(String fileName);
    T downloadFile(String fileName);
}
