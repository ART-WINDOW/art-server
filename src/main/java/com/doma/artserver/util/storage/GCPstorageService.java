package com.doma.artserver.util.storage;

import com.google.cloud.storage.*;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.io.InputStream;

@Component
public class GCPstorageService implements StorageService<byte[]> {

    // 버킷 이름 지정
    private final String bucketName = "art";
    // GCP 프로젝트 ID
    @Value("${spring.gcp.project-id}")
    private String projectId;
    // Google Cloud Storage Client 초기화
    private final Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();

    @Override
    public String uploadFile(String fileName, byte[] data) {
        BlobId blobId = BlobId.of(bucketName, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();

        Storage.BlobTargetOption precondition;
        if (storage.get(blobId) != null) {
            precondition = Storage.BlobTargetOption.doesNotExist();
        } else {
            precondition = Storage.BlobTargetOption.generationMatch(
                    storage.get(bucketName, fileName).getGeneration());
        }

        storage.create(blobInfo, data, precondition);
        storage.createAcl(blobId, Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER));

        return storage.get(blobId).getMediaLink();
    }

    @Override
    public void deleteFile(String fileName) {

    }

    @Override
    public byte[] downloadFile(String fileName) {
        return null;
    }
}
