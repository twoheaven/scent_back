package com.scent.text;

public enum Division {
    // 버스킹(노래)
    BUSKING_ACOUSTIC_BAND("버스킹 어쿠스틱밴드"),
    BUSKING_ACOUSTIC_GUITAR_VOCAL("버스킹 통기타보컬"),
    BUSKING_KAYO_POP("버스킹 가요&팝"),
    BUSKING_ACAPELLA("버스킹 아카펠라"),
    BUSKING_POP_OPERA("버스킹 팝페라"),
    BUSKING_VOCAL_ENSEMBLE("버스킹 성악&합창"),
    BUSKING_TROT("버스킹 트로트"),
    BUSKING_HIPHOP_BEATBOX("버스킹 힙합&비트박스"),
    
    // 버스킹(연주)
    BUSKING_JAZZ_BAND("버스킹 재즈밴드"),
    BUSKING_DJ("버스킹 DJ"),
    BUSKING_CLASSICAL_ENSEMBLE("버스킹 클래식앙상블"),
    BUSKING_ELECTRIC_STRING("버스킹 전자현악"),
    BUSKING_ROCK_BAND("버스킹 퓨전국악"),
    BUSKING_BRASS_BAND("버스킹 브라스밴드"),
    BUSKING_MATCHING_BAND("버스킹 마칭밴드"),
    
    // 버스킹(퍼포먼스)
    BUSKING_MEDIA_PERFORMANCE("버스킹 미디어퍼포먼스"),
    BUSKING_LASER_TRON("버스킹 레이저트론"),
    BUSKING_MAGIC_BUBBLE("버스킹 매직&버블"),
    BUSKING_JUGGLING_MIME("버스킹 저글링&마임"),
    BUSKING_TAAK_NANTA("버스킹 타악(난타)"),
    BUSKING_TAEKWONDO("버스킹 태권도"),
    BUSKING_BRUSH_CALLIGRAPHY("버스킹 드로잉퍼포먼스"),
    BUSKING_ETC("버스킹 그 외 퍼포먼스"),
    
    //버스킹(댄스&무용)
    BUSKING_B_BOYING("버스킹 비보이&스트릿 댄스"),
    BUSKING_K_POP_DANCE("버스킹 케이팝 댄스"),
    BUSKING_CHEER("버스킹 치어리더"),
    BUSKING_MODERN("버스킹 현대무용&발레"),
    BUSKING_TRADITIONAL("버스킹 전통무용"),
    BUSKING_POLE_VALLEY("버스킹 폴&밸리 댄스"),
    
    //버스킹(MC&아나운서)
    BUSKING_MC("버스킹 MC&아나운서"),
    
    //연예인
    CELEB_K_POP_IDOL("연예인 K.POP(아이돌)"),
    CELEB_GENERAL_KAYO("연예인 일반가요"),
    CELEB_TROT("연예인 트로트"),
    CELEB_HIPHOP_DJ("연예인 힙합DJ"),
    CELEB_BAND("연예인 밴드"),
    CELEB_ANNOUNCER("연예인 아나운서"),
    CELEB_COMEDIAN("연예인 개그맨"),
    CELEB_YOUTUBER("연예인 유튜버/인플루언서"),
    
    //시스템
    SYSTEM_SOUND("시스템 음향"),
    SYSTEM_LIGHTING("시스템 조명"),
    SYSTEM_VIDEO("시스템 영상(LED,빔)"),
    SYSTEM_RELAY("시스템 중계"),
    SYSTEM_LASER("시스템 특수조명"),
    SYSTEM_STRUCTURE("시스템 구조물(트러스,레이저)"),
    SYSTEM_SPECIAL_EFFECTS("시스템 특수효과(장치,불꽃놀이)"),
    SYSTEM_STAGE("시스템 무대"),
    SYSTEM_CHAIR("시스템 전식"),
    SYSTEM_POWER_CAR("시스템 발전차"),
    SYSTEM_STAGE_CAR("시스템 무대차");
	
    private final String value;
    
    Division(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
}