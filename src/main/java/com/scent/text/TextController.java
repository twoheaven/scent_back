package com.scent.text;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import com.scent.mainpicture.MainPicture;
import com.scent.mainpicture.MainPictureRepository;
import com.scent.mainpicture.MainPictureService;
import com.scent.subpicture.SubPicture;
import com.scent.subpicture.SubPictureNotFoundException;
import com.scent.subpicture.SubPictureRepository;
import com.scent.subpicture.SubPictureService;
import com.scent.video.Video;
import com.scent.video.VideoRepository;

import jakarta.persistence.criteria.Predicate;

@CrossOrigin
@RestController
@RequestMapping("/api/texts")
public class TextController {

    @Autowired
    private TextRepository textRepository;
    
    private final VideoRepository videoRepository;
    private final SubPictureRepository subpictureRepository;
    private final MainPictureRepository mainPictureRepository;
    @Autowired
    private MainPictureService mainPictureService;
    @Autowired
    private SubPictureService subPictureService;
    
    @Autowired
    public TextController(TextRepository textRepository, VideoRepository videoRepository,SubPictureRepository subpictureRepository, MainPictureRepository mainPictureRepository) {
        this.videoRepository = videoRepository;
        this.subpictureRepository = subpictureRepository;
        this.mainPictureRepository= mainPictureRepository;
		this.textRepository = textRepository;
    }

    //mainpicture crud
    
    //메인 사진 생성 post /api/texts/post/{textId}/main-picture
    @PostMapping("/post/{textId}/main-picture")
    public ResponseEntity<Text> addMainPictureToText(
            @PathVariable Long textId,
            @RequestParam("imageFile") MultipartFile file
    ) throws IOException {
        // 주어진 textId로 데이터베이스에서 해당 id를 가진 Text 엔티티를 조회합니다.
        Text text = textRepository.findById(textId)
                                  .orElseThrow(() -> new TextNotFoundException(textId));

        // 새로운 메인 사진을 생성하고 저장합니다.
        MainPicture mainPicture = mainPictureService.createMainPicture(file);

        // Text와 메인 사진을 연결합니다.
        text.setMainPicture(mainPicture);

        // 연결된 Text 엔티티를 저장하고 반환합니다.
        text = textRepository.save(text);

        // ResponseEntity.ok()를 사용하여 성공 응답을 반환합니다.
        return ResponseEntity.ok(text);
    }
    
    //메인 사진 수정 put /api/texts/put/{textId}/main-picture 
    @PutMapping("/put/{textId}/main-picture")
    public ResponseEntity<MainPicture> updateMainPictureOfText(
            @PathVariable Long textId,
            @RequestParam("imageFile") MultipartFile file
    ) throws IOException {
        // 주어진 textId로 데이터베이스에서 해당 id를 가진 Text 엔티티를 조회합니다.
        Text text = textRepository.findById(textId)
                                  .orElseThrow(() -> new TextNotFoundException(textId));

        // 현재 Text의 메인 사진을 가져옵니다.
        MainPicture existingMainPicture = text.getMainPicture();

        if (existingMainPicture != null) {
            // 기존의 MainPicture 엔티티를 업데이트합니다.
            MainPicture updatedMainPicture = mainPictureService.updateMainPicture(existingMainPicture.getId(), file);
            return ResponseEntity.ok(updatedMainPicture);
        } else {
            // 기존의 MainPicture가 없을 경우, 새로운 MainPicture를 생성합니다.
            MainPicture newMainPicture = mainPictureService.createMainPicture(file);
            
            // Text와 메인 사진을 연결합니다.
            text.setMainPicture(newMainPicture);
            textRepository.save(text);

            // 생성된 MainPicture를 반환합니다.
            return ResponseEntity.ok(newMainPicture);
        }
    }

    //subpicture crud
    
    //subpicture 생성 post /api/texts/post/{textId}/sub-pictures
    @PostMapping("/post/{textId}/sub-pictures")
    public ResponseEntity<Text> addSubPictureToText(
            @PathVariable Long textId,
            @RequestParam("imageFile") MultipartFile file
    ) throws IOException {
        // 주어진 textId로 데이터베이스에서 해당 id를 가진 Text 엔티티를 조회합니다.
        Text text = textRepository.findById(textId)
                                  .orElseThrow(() -> new TextNotFoundException(textId));

        // 새로운 SubPicture를 생성하고 저장합니다.
        SubPicture subPicture = subPictureService.createSubPicture(file);

        // Text와 SubPicture를 연결합니다.
        text.addSubPicture(subPicture);

        // 연결된 Text 엔티티를 저장하고 반환합니다.
        text = textRepository.save(text);

        // ResponseEntity.ok()를 사용하여 성공 응답을 반환합니다.
        return ResponseEntity.ok(text);
    }

  //subpicture 삭제 delete /api/texts/delete/{textId}/sub-pictures/{subPictureId}
    @DeleteMapping("/delete/{textId}/sub-pictures/{subPictureId}")
    public ResponseEntity<Text> removeSubPictureFromText(
            @PathVariable Long textId,
            @PathVariable Long subPictureId
    ) {
        // 주어진 textId로 데이터베이스에서 해당 id를 가진 Text 엔티티를 조회합니다.
        Text text = textRepository.findById(textId)
                                  .orElseThrow(() -> new TextNotFoundException(textId));

        // 주어진 subPictureId로 데이터베이스에서 해당 id를 가진 SubPicture 엔티티를 조회합니다.
        SubPicture subPicture = subpictureRepository.findById(subPictureId)
                                                    .orElseThrow(() -> new SubPictureNotFoundException(subPictureId));

        // Text에서 SubPicture를 제거하고 저장합니다.
        text.removeSubPicture(subPicture);
        textRepository.save(text);

        // SubPicture 엔티티를 삭제합니다.
        subpictureRepository.delete(subPicture);

        // ResponseEntity.ok()를 사용하여 성공 응답을 반환합니다.
        return ResponseEntity.ok(text);
    }
    
    //video crud
    
    // Text에 Video 추가 API post /api/texts/{textId}/videos
    @PostMapping("/post/{textId}/videos")
    public Text addVideoToText(@PathVariable Long textId, @RequestBody Video video) {
        Text text = textRepository.findById(textId).orElse(null);
        if (text != null) {
            text.addVideo(video);
            return textRepository.save(text);
        }
        return null;
    }

    //video 업데이트 put /api/texts/videos/{videoId}
    @PutMapping("/put/videos/{videoId}")
    public Video updateVideo(@PathVariable Long videoId, @RequestBody Video updatedVideo) {
        Video video = videoRepository.findById(videoId).orElse(null);
        if (video != null) {
            video.setYoutubeLink(updatedVideo.getYoutubeLink());
            return videoRepository.save(video);
        }
        return null;
    }

    //video 삭제 delete /api/texts/delete/{textId}/videos/{videoId}
    @DeleteMapping("/delete/{textId}/videos/{videoId}")
    public Text removeVideoFromText(@PathVariable Long textId, @PathVariable Long videoId) {
        Text text = textRepository.findById(textId).orElse(null);
        Video video = videoRepository.findById(videoId).orElse(null);
        if (text != null && video != null && text.getVideos().contains(video)) {
            text.removeVideo(video);
            videoRepository.delete(video);
            return textRepository.save(text);
        }
        return null;
    }
    
    //text crud
    
  //text 상세 조회 get /api/texts/{id}
    @GetMapping("/get/{id}")
    public ResponseEntity<Text> getTextById(@PathVariable Long id) {
        Optional<Text> textOptional = textRepository.findById(id);
        
        if (!textOptional.isPresent()) {
            // 데이터를 찾지 못한 경우 400 Bad Request 응답을 반환
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        Text text = textOptional.get();

        // 조회된 Text 엔티티를 ResponseEntity.ok()를 사용하여 응답합니다.
        // ResponseEntity는 HTTP 응답을 생성하는데 사용되며, ok()는 성공적인 응답을 나타냅니다.
        return ResponseEntity.ok(text);
    }
    
    
    //text 생성 post /api/texts
    @PostMapping("/post")
    public Text createText(@RequestBody Text text) {
        // 클라이언트로부터 전달받은 JSON 데이터를 Text 엔티티 객체로 매핑하여 생성합니다.
        // 클라이언트가 JSON으로 전달한 데이터에는 teamMany가 이미 정수로 전달될 것입니다.

        // Text 엔티티를 데이터베이스에 저장하고 저장된 엔티티를 반환합니다.
        Text savedText = textRepository.save(text);

        // 저장된 엔티티의 ID를 반환합니다.
        return savedText;
    }

    //text 업데이트 put /api/texts/{id}
    @PutMapping("/put/{id}")
    public Text updateText(@PathVariable Long id, @RequestBody Text newText) {
        // 데이터베이스에서 해당 id로 기존의 Text 엔티티를 조회합니다.
        Text existingText = textRepository.findById(id)
                                          .orElseThrow(() -> new RuntimeException("Text not found with id: " + id));

        // newText 객체의 필드로 기존 엔티티 필드를 업데이트합니다.
        existingText.setDivision(newText.getDivision());
        existingText.setTeamName(newText.getTeamName());
        existingText.setShortIntro(newText.getShortIntro());
        existingText.setTeamMany(newText.getTeamMany());
        existingText.setLongIntro(newText.getLongIntro());
        existingText.setPortfolio(newText.getPortfolio());
        existingText.setRepertoire(newText.getRepertoire());
        existingText.setEquipment(newText.getEquipment());

        // 업데이트된 Text 엔티티를 데이터베이스에 저장하고 저장된 엔티티를 반환합니다.
        return textRepository.save(existingText);
    }

    //text 삭제 delete /api/texts/{id}
    @DeleteMapping("/delete/{id}")
    public void deleteText(@PathVariable Long id) {
        // 주어진 id에 해당하는 Text 엔티티를 데이터베이스에서 삭제합니다.
        textRepository.deleteById(id);
    }

    //모든 text 조회 get /api/texts/get
    @GetMapping("/get")
    public Page<Text> getAllTexts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        // PageRequest.of(page, size)를 사용하여 페이징 정보를 생성
        PageRequest pageRequest = PageRequest.of(page, size);
        
        // textRepository에서 findAll 메서드에 페이징 정보 적용
        return textRepository.findAll(pageRequest);
    }
    
    @GetMapping("/get/search")
    public ResponseEntity<List<Text>> search(@RequestParam("keyword") String keyword) {
        // 검색어를 소문자로 변환
    	String keyword1 = keyword.toLowerCase();
        
        // Specification을 사용하여 검색 조건을 만듭니다.
        Specification<Text> textSpec = (root, query, criteriaBuilder) -> {
            String likeKeyword = "%" + keyword1 + "%";
            
            // 검색 대상 필드 중에서 원하는 필드만 선택하여 검색
            Predicate[] predicates = new Predicate[]{
                criteriaBuilder.like(criteriaBuilder.lower(root.get("equipment")), likeKeyword),
                criteriaBuilder.like(criteriaBuilder.lower(root.get("longIntro")), likeKeyword),
                criteriaBuilder.like(criteriaBuilder.lower(root.get("portfolio")), likeKeyword),
                criteriaBuilder.like(criteriaBuilder.lower(root.get("repertoire")), likeKeyword),
                criteriaBuilder.like(criteriaBuilder.lower(root.get("shortIntro")), likeKeyword),
                criteriaBuilder.like(criteriaBuilder.lower(root.get("teamName")), likeKeyword)
            };

            return criteriaBuilder.or(predicates);
        };

        List<Text> searchResults = textRepository.findAll(textSpec);

        // Text 엔티티의 SubPictures, MainPicture, Videos를 함께 조회하고 설정
        searchResults.forEach(text -> {
            text.setSubPictures(subpictureRepository.findByText(text));

            // MainPicture가 null인 경우에 대한 처리 추가
            MainPicture mainPicture = mainPictureRepository.findByText(text);
            if (mainPicture != null) {
                text.setMainPicture(mainPicture);
            }

            text.setVideos(videoRepository.findByText(text));
        });

        return ResponseEntity.ok(searchResults);
    }
    
}
