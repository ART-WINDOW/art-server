package com.doma.artserver.util.storage;

import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.io.InputStream;

@Component
public class GCPstorageService implements StorageService<InputStream> {


    // 버킷 이름 지정
    private final String bucketName = "art";
    // GCP 프로젝트 ID
    @Value("${spring.gcp.project-id}")
    private String projectId;
    // Google Cloud Storage Client 초기화
    private final Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();

    @Override
    public String uploadFile(String fileName, InputStream data) {

        return "";
    }

    @Override
    public void deleteFile(String fileName) {

    }

    @Override
    public InputStream downloadFile(String fileName) {
        return null;
    }
}
