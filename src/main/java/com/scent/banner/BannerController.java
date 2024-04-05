package com.scent.banner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/banners")
public class BannerController {
    private final BannerService bannerService;

    @Autowired
    public BannerController(BannerService bannerService) {
        this.bannerService = bannerService;
    }

    // 배너 생성 엔드포인트
    @PostMapping("/post")
    public ResponseEntity<Banner> createBanner(@RequestParam("imageFile") MultipartFile file,
    										   @RequestParam("backColor") String backcolor) {
        try {
            Banner createdBanner = bannerService.createBanner(file, backcolor);
            return new ResponseEntity<>(createdBanner, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 모든 배너 조회 엔드포인트
    @GetMapping("/get")
    public ResponseEntity<List<Banner>> getAllBanners() {
        List<Banner> banners = bannerService.getAllBanners();
        return new ResponseEntity<>(banners, HttpStatus.OK);
    }

    // 배너 조회 엔드포인트
    @GetMapping("/get/{id}")
    public ResponseEntity<Banner> getBannerById(@PathVariable Long id) {
        Banner banner = bannerService.getBannerById(id);
        return new ResponseEntity<>(banner, HttpStatus.OK);
    }

    // 배너 삭제 엔드포인트
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteBanner(@PathVariable Long id) {
        bannerService.deleteBanner(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}