package com.scent.subpicture;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Service
public class SubPictureService {
    private final SubPictureRepository subPictureRepository;
    private final AmazonS3 amazonS3;

    @Autowired
    public SubPictureService(SubPictureRepository subPictureRepository, AmazonS3 amazonS3) {
        this.subPictureRepository = subPictureRepository;
        this.amazonS3 = amazonS3;
    }

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    // 메서드: 새로운 SubPicture 객체를 생성하고 연결된 이미지를 Amazon S3에 업로드합니다.
    public SubPicture createSubPicture(MultipartFile file) throws IOException {
        SubPicture subPicture = new SubPicture();
        subPicture.setOriginalFileName(file.getOriginalFilename());
        subPicture.setFileSize(file.getSize());

        // 고유한 저장 파일 이름을 생성하고 객체 메타데이터를 설정합니다.
        String storedFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        amazonS3.putObject(new PutObjectRequest(bucketName, storedFileName, file.getInputStream(), metadata));

        String s3Url = amazonS3.getUrl(bucketName, storedFileName).toString();
        subPicture.setStoredFilePath(s3Url);

        return subPictureRepository.save(subPicture);
    }

    // 모든 SubPicture 객체를 가져오는 메서드
    public List<SubPicture> getAllSubPictures() {
        return subPictureRepository.findAll();
    }

    // 주어진 ID로 SubPicture 객체를 가져오는 메서드
    public SubPicture getSubPictureById(Long id) {
        return subPictureRepository.findById(id)
                .orElseThrow(() -> new SubPictureNotFoundException(id));
    }
    
    // SubPicture 객체를 삭제하는 메서드
    public void deleteSubPicture(Long id) {
        SubPicture subPicture = getSubPictureById(id);

        // S3에서 이미지 파일 삭제
        String s3ObjectKey = extractS3ObjectKey(subPicture.getStoredFilePath());
        deleteImageFile(s3ObjectKey);

        // 배너 객체 삭제
        subPictureRepository.deleteById(id);
    }
    
    // S3 URL에서 객체 키를 추출하는 메서드
    private String extractS3ObjectKey(String s3Url) {
        String[] parts = s3Url.split("/");
        return parts[parts.length - 1];
    }
    
    // 이미지 파일을 삭제하는 메서드
    private void deleteImageFile(String s3ObjectKey) {
        try {
            amazonS3.deleteObject(new DeleteObjectRequest(bucketName, s3ObjectKey));
        } catch (Exception e) {
            // 이미지 파일 삭제 중 오류 발생 시 예외 처리
            e.printStackTrace();
            throw new RuntimeException("이미지 파일 삭제 중 오류가 발생했습니다.");
        }
    }
}