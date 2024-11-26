package com.doma.artserver.util.storage;

import com.google.cloud.storage.*;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.net.URI;

@Component
public class GCPstorageService implements StorageService<byte[]> {

    // 버킷 이름 지정
    private final String bucketName = "art-window-image";
    // GCP 프로젝트 ID
    @Value("${spring.gcp.project-id}")
    private String projectId;
    // Google Cloud Storage Client 초기화
    private final Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();

    @Override
    public String uploadFile(String fileName, byte[] data) {
        BlobId blobId = BlobId.of(bucketName, fileName.trim());
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();

        Storage.BlobTargetOption precondition;
        Blob existingBlob = storage.get(blobId); // 기존 Blob 객체 가져오기

        if (existingBlob == null) {
            // 객체가 존재하지 않는 경우 (새로 업로드할 때)
//            precondition = Storage.BlobTargetOption.doesNotExist();
            // 파일 업로드
            storage.create(blobInfo, data);
            // 모든 사용자에게 읽기 권한 부여
//        storage.createAcl(blobId, Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER));
        } else {
            // 객체가 존재하는 경우, generationMatch를 사용하여 업로드 조건 설정
//            precondition = Storage.BlobTargetOption.generationMatch(existingBlob.getGeneration());
            return "https://storage.googleapis.com/" + bucketName + "/" + existingBlob.getBlobId().getName();
        }

        // ACL 변경 사항이 적용된 Blob 객체를 다시 가져옴
//        Blob updatedBlob = storage.get(blobId);

        return "https://storage.googleapis.com/" + bucketName + "/" + fileName.trim();
    }

    @Override
    public void deleteFile(String fileName) {
        // 삭제 로직 구현
    }

    @Override
    public byte[] downloadFile(String fileName) {
        // 다운로드 로직 구현
        return null;
    }
}
