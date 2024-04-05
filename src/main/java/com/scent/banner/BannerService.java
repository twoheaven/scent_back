package com.scent.banner;

import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import net.coobird.thumbnailator.Thumbnails;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;

@Service
public class BannerService {
    private final BannerRepository bannerRepository;
    private final AmazonS3 amazonS3;

    @Autowired
    public BannerService(BannerRepository bannerRepository, AmazonS3 amazonS3) {
        this.bannerRepository = bannerRepository;
        this.amazonS3 = amazonS3;
    }

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    // 필드 저장 메서드
    public Banner createBanner(MultipartFile file,String backcolor) throws IOException {
    	Banner banner = new Banner();
        String storedFileName = generateStoredFileName(file); // 저장된 파일 이름 생성
        BufferedImage scaledImage = scaleImage(file.getInputStream()); // 이미지 스케일링
        uploadImageToS3(storedFileName, scaledImage); // S3에 이미지 업로드
        String s3Url = generateS3Url(storedFileName); // S3 URL 생성
        setBannerProperties(banner, file, s3Url, backcolor); // 필드 속성 설정
        return bannerRepository.save(banner); // 필드 저장 후 반환
    }

    public List<Banner> getAllBanners() {
        return bannerRepository.findAll();
    }

    public Banner getBannerById(Long id) {
        return bannerRepository.findById(id)
                .orElseThrow(() -> new BannerNotFoundException(id));
    }
    
    public void deleteBanner(Long id) {
        Banner banner = getBannerById(id);

        // S3에서 이미지 파일 삭제
        String s3ObjectKey = extractS3ObjectKey(banner.getStoredFilePath());
        deleteImageFile(s3ObjectKey);

        // 배너 객체 삭제
        bannerRepository.deleteById(id);
    }
    
    private String extractS3ObjectKey(String s3Url) {
        // S3 URL에서 객체 키 추출 메서드 구현
        String[] parts = s3Url.split("/");
        return parts[parts.length - 1];
    }
    
    private void deleteImageFile(String s3ObjectKey) {
        try {
            amazonS3.deleteObject(new DeleteObjectRequest(bucketName, s3ObjectKey));
        } catch (Exception e) {
            // 이미지 파일 삭제 중 오류 발생 시 예외 처리
            e.printStackTrace();
            throw new RuntimeException("이미지 파일 삭제 중 오류가 발생했습니다.");
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
        int targetWidth = 1920; // 타겟 가로 크기
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
    private void setBannerProperties(Banner banner, MultipartFile file, String s3Url, String backcolor) {
    	banner.setOriginalFileName(StringUtils.cleanPath(file.getOriginalFilename()));
    	banner.setStoredFilePath(s3Url);
    	banner.setFileSize(file.getSize());
    	banner.setBackcolor(backcolor);
    }
}