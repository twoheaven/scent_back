package com.scent.field;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import net.coobird.thumbnailator.Thumbnails;

@Service
public class FieldService {
    private final FieldRepository fieldRepository;
    private final AmazonS3 amazonS3;

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    @Autowired
    public FieldService(FieldRepository fieldRepository, AmazonS3 amazonS3) {
        this.fieldRepository = fieldRepository;
        this.amazonS3 = amazonS3;
    }

    // 필드 저장 메서드
    public Field saveField(MultipartFile file) throws IOException {
        Field field = new Field();
        String storedFileName = generateStoredFileName(file); // 저장된 파일 이름 생성
        BufferedImage scaledImage = scaleImage(file.getInputStream()); // 이미지 스케일링
        uploadImageToS3(storedFileName, scaledImage); // S3에 이미지 업로드
        String s3Url = generateS3Url(storedFileName); // S3 URL 생성
        setFieldProperties(field, file, s3Url); // 필드 속성 설정
        return fieldRepository.save(field); // 필드 저장 후 반환
    }
    
    // 필드 이미지 업데이트 메서드
    public Field updateFieldImage(Long id, MultipartFile newFile) throws IOException {
        Field existingField = getFieldById(id); // 기존 필드 가져오기
        deletePreviousFile(existingField); // 이전 파일 삭제
        String storedFileName = generateStoredFileName(newFile); // 새 파일 이름 생성
        BufferedImage scaledImage = scaleImage(newFile.getInputStream()); // 이미지 스케일링
        uploadImageToS3(storedFileName, scaledImage); // S3에 이미지 업로드
        String s3Url = generateS3Url(storedFileName); // S3 URL 생성
        existingField.setStoredFilePath(s3Url); // 필드 속성 업데이트
        setFieldProperties(existingField, newFile, s3Url); // 필드 속성 설정
        return fieldRepository.save(existingField); // 필드 저장 후 반환
    }
    
    // 필드 세부 정보 업데이트 메서드
    public Field updateFieldDetail(Long id, Field fieldDetails) {
        Field existingField = getFieldById(id); // 기존 필드 가져오기
        existingField.setCasting(fieldDetails.getCasting()); // 캐스팅 정보 업데이트
        existingField.setDate(fieldDetails.getDate()); // 날짜 정보 업데이트
        existingField.setLocation(fieldDetails.getLocation()); // 위치 정보 업데이트
        existingField.setTitle(fieldDetails.getTitle()); // 제목 정보 업데이트
        return fieldRepository.save(existingField); // 필드 저장 후 반환
    }

    // 저장된 파일 이름 생성 메서드
    private String generateStoredFileName(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        return UUID.randomUUID().toString() + "_" + StringUtils.getFilenameExtension(fileName) + ".png";
    }

    // 이미지 스케일링 메서드
    private BufferedImage scaleImage(InputStream inputStream) throws IOException {
        BufferedImage image = Thumbnails.of(inputStream).scale(1).asBufferedImage();
        int targetWidth = 700; // 타겟 가로 크기
        return Scalr.resize(image, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_TO_WIDTH, targetWidth);
    }

    // 이미지를 S3에 업로드하는 메서드
    private void uploadImageToS3(String storedFileName, BufferedImage scaledImage) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(scaledImage, "png", os);
        byte[] pngData = os.toByteArray();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(pngData.length);
        amazonS3.putObject(new PutObjectRequest(bucketName, storedFileName, new ByteArrayInputStream(pngData), metadata));
    }

    // S3 URL 생성 메서드
    private String generateS3Url(String storedFileName) {
        return amazonS3.getUrl(bucketName, storedFileName).toString();
    }

    // 필드 속성 설정 메서드
    private void setFieldProperties(Field field, MultipartFile file, String s3Url) {
        field.setOriginalFileName(StringUtils.cleanPath(file.getOriginalFilename()));
        field.setStoredFilePath(s3Url);
        field.setFileSize(file.getSize());
    }

    // 이전 파일 삭제 메서드
    private void deletePreviousFile(Field existingField) {
        amazonS3.deleteObject(bucketName, existingField.getStoredFilePath());
    }

    // ID로 필드 가져오기 메서드
    public Field getFieldById(Long id) {
        return fieldRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 ID의 필드를 찾을 수 없습니다: " + id));
    }

    // 모든 필드 가져오기 메서드
    public List<Field> getAllFields() {
        List<Field> allFields = fieldRepository.findAll();
        if (allFields.isEmpty()) {
            throw new NoSuchElementException("필드가 없습니다.");
        }
        return allFields;
    }
}
