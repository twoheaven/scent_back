package com.scent.mainpicture;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Optional;
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
public class MainPictureService {
    private final MainPictureRepository mainPictureRepository;
    private final AmazonS3 amazonS3;

    @Autowired
    public MainPictureService(MainPictureRepository mainPictureRepository, AmazonS3 amazonS3) {
        this.mainPictureRepository = mainPictureRepository;
        this.amazonS3 = amazonS3;
    }

    @Value("${aws.s3.bucketName}")
    private String bucketName;
    
    // 이미지 저장 메서드
    public MainPicture createMainPicture(MultipartFile file) throws IOException {
    	MainPicture mainPicture = new MainPicture();
        String storedFileName = generateStoredFileName(file); // 저장된 파일 이름 생성
        BufferedImage scaledImage = scaleImage(file.getInputStream()); // 이미지 스케일링
        uploadImageToS3(storedFileName, scaledImage); // S3에 이미지 업로드
        String s3Url = generateS3Url(storedFileName); // S3 URL 생성
        setMainPictureProperties(mainPicture, file, s3Url); // 필드 속성 설정
        return mainPictureRepository.save(mainPicture); // 필드 저장 후 반환
    }
    
    // 이미지 업데이트 메서드
    public MainPicture updateMainPicture(Long id, MultipartFile newFile) throws IOException {
    	MainPicture existingMainPicture = getMainPictureById(id); // 기존 필드 가져오기
        deletePreviousFile(existingMainPicture); // 이전 파일 삭제
        String storedFileName = generateStoredFileName(newFile); // 새 파일 이름 생성
        BufferedImage scaledImage = scaleImage(newFile.getInputStream()); // 이미지 스케일링
        uploadImageToS3(storedFileName, scaledImage); // S3에 이미지 업로드
        String s3Url = generateS3Url(storedFileName); // S3 URL 생성
        existingMainPicture.setStoredFilePath(s3Url); // 필드 속성 업데이트
        setMainPictureProperties(existingMainPicture, newFile, s3Url); // 필드 속성 설정
        return mainPictureRepository.save(existingMainPicture); // 필드 저장 후 반환
    }

    public MainPicture getMainPictureById(Long id) {
        // ID를 사용하여 필드를 데이터베이스에서 가져옵니다.
        Optional<MainPicture> optionalMainPicture = mainPictureRepository.findById(id);
        
        // 만약 필드가 존재한다면
        if (optionalMainPicture.isPresent()) {
        	MainPicture mainPicture = optionalMainPicture.get();
                        
            // 필드 객체를 반환합니다.
            return mainPicture;
        } else {
            // 필드가 존재하지 않을 경우 NoSuchElementException을 던집니다.
            throw new NoSuchElementException("Field with id " + id + " not found");
        }
    }
    
    // 저장된 파일 이름 생성 메서드
    private String generateStoredFileName(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        return UUID.randomUUID().toString() + "_" + StringUtils.getFilenameExtension(fileName) + ".png";
    }

    // 이미지 스케일링 메서드
    private BufferedImage scaleImage(InputStream inputStream) throws IOException {
        BufferedImage image = Thumbnails.of(inputStream).scale(1).asBufferedImage();
        int targetWidth = 500; // 타겟 가로 크기
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
    private void setMainPictureProperties(MainPicture mainPicture, MultipartFile file, String s3Url) {
    	mainPicture.setOriginalFileName(StringUtils.cleanPath(file.getOriginalFilename()));
    	mainPicture.setStoredFilePath(s3Url);
    	mainPicture.setFileSize(file.getSize());
    }

    // 이전 파일 삭제 메서드
    private void deletePreviousFile(MainPicture existingMainPicture) {
        amazonS3.deleteObject(bucketName, existingMainPicture.getStoredFilePath());
    }
}
