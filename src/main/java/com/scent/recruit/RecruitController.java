package com.scent.recruit;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.scent.text.TextNotFoundException;

@CrossOrigin
@RestController
@RequestMapping("/api/recruits")
public class RecruitController {
    private final RecruitRepository recruitRepository;

    @Autowired
    public RecruitController(RecruitRepository recruitRepository) {
        this.recruitRepository = recruitRepository;
    }

    // 모든 배너 조회 get /api/recruits
    @GetMapping("/get")
    public List<Recruit> getAllRecruits() {
        // textRepository에서 모든 Banner를 조회합니다.
        return recruitRepository.findAll();
    }

    // 특정 ID의 배너 조회 get /api/recruits/{id}
    @GetMapping("/get/{id}")
    public ResponseEntity<Recruit> getRecruitById(@PathVariable Long id) {
        // 주어진 id로 데이터베이스에서 해당 id를 가진 Text 엔티티를 조회합니다.
    	Recruit recruit = recruitRepository.findById(id)
                                  .orElseThrow(() -> new TextNotFoundException(id));
        
        // 조회된 Text 엔티티를 ResponseEntity.ok()를 사용하여 응답합니다.
        // ResponseEntity는 HTTP 응답을 생성하는데 사용되며, ok()는 성공적인 응답을 나타냅니다.
        return ResponseEntity.ok(recruit);
    }

    // 배너 생성 post api/recruits
    @PostMapping("/post")
    public Recruit createRecruit(@RequestBody Recruit recruit) {
    	Recruit savedRecruit = recruitRepository.save(recruit);
        // 생성된 배너 정보와 CREATED 응답 반환
        return savedRecruit;
    }

    // 배너 업데이트 put api/recruits/{id}
    @PutMapping("/put/{id}")
    public Recruit updateRecruit(@PathVariable Long id, @RequestBody Recruit newRecruit) {
    	Recruit existingRecruit = recruitRepository.findById(id)
                 .orElseThrow(() -> new RuntimeException("Text not found with id: " + id));

         existingRecruit.setYoutubeLink(newRecruit.getYoutubeLink());
         
         // 업데이트된 Text 엔티티를 데이터베이스에 저장하고 저장된 엔티티를 반환합니다.
         return recruitRepository.save(existingRecruit);
    }

    // 최근섭외 삭제 delete api/recruits/delete/{id}
    @DeleteMapping("/delete/{id}")
    public void deleteRecruit(@PathVariable Long id) {
        // 주어진 id에 해당하는 Text 엔티티를 데이터베이스에서 삭제합니다.
    	recruitRepository.deleteById(id);
    }
}