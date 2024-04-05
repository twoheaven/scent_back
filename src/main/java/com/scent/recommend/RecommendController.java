package com.scent.recommend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/recommends")
public class RecommendController {
    private final RecommendRepository recommendRepository;

    @Autowired
    public RecommendController(RecommendRepository recommendRepository) {
        this.recommendRepository = recommendRepository;
    }

    
/*    @GetMapping("/get")
    public Recommend getRecommendation() {
        // Recommend 엔티티를 조회하여 반환
        return recommendRepository.findById((long) 1) // 또는 다른 ID로 조회 가능
                .orElseThrow(() -> new RecommendNotFoundException("Recommendation not found"));
    }*/

    @GetMapping("/get")
    public ResponseEntity<Recommend> getRecommendation() {
        Recommend recommendation = recommendRepository.findById((long) 1).orElse(null);

        if (recommendation == null) {
            // 데이터를 찾지 못한 경우 400 Bad Request 응답을 반환
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        return ResponseEntity.ok(recommendation);
    }
    
    @PostMapping("/post")
    public Recommend createRecommendation(@RequestBody Recommend recommend) {
        // 이미 존재하는 추천이 있으면 그냥 반환
        Recommend existingRecommendation = recommendRepository.findById((long) 1).orElse(null);
        if (existingRecommendation != null) {
            return existingRecommendation;
        } else {
            // 새로운 객체 생성 및 저장
            return recommendRepository.save(recommend);
        }
    }
    
    @PutMapping("/put")
    public Recommend updateRecommendation(@RequestBody Recommend updatedRecommendation) {
        Recommend existingRecommendation = recommendRepository.findById(1L) // 또는 다른 ID로 조회 가능
                .orElseThrow(() -> new RecommendNotFoundException("Recommendation not found"));

        // 업데이트할 필드들을 업데이트합니다.
        existingRecommendation.setId1(updatedRecommendation.getId1());
        existingRecommendation.setId2(updatedRecommendation.getId2());
        existingRecommendation.setId3(updatedRecommendation.getId3());
        existingRecommendation.setId4(updatedRecommendation.getId4());
        existingRecommendation.setId5(updatedRecommendation.getId5());
        existingRecommendation.setId6(updatedRecommendation.getId6());
        existingRecommendation.setId7(updatedRecommendation.getId7());
        existingRecommendation.setId8(updatedRecommendation.getId8());

        // 엔티티를 저장하여 업데이트를 반영합니다.
        return recommendRepository.save(existingRecommendation);
    }
}