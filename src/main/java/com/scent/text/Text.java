package com.scent.text;

import java.util.ArrayList;
import java.util.List;

import com.scent.mainpicture.MainPicture;
import com.scent.subpicture.SubPicture;
import com.scent.video.Video;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Text {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Division division;
    
    @Column(columnDefinition = "TEXT")
    private String teamName;
    @Column(columnDefinition = "TEXT")
    private String shortIntro;
    @Column(columnDefinition = "TEXT")
    private Integer teamMany;
    @Column(columnDefinition = "TEXT")
    private String longIntro;
    @Column(columnDefinition = "TEXT")
    private String portfolio;
    @Column(columnDefinition = "TEXT")
    private String repertoire;
    @Column(columnDefinition = "TEXT")
    private String equipment;

    //메인사진과 Text를 일대일 관계로 구성함 fetch = FetchType.LAZY
    @OneToOne(mappedBy = "text", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private MainPicture mainPicture;
    
    public void setMainPicture(MainPicture mainPicture) {
        this.mainPicture = mainPicture;
        mainPicture.setText(this);
    }

    public MainPicture getMainPicture() {
        return mainPicture;
    }
    
    @OneToMany(mappedBy = "text", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubPicture> subPictures = new ArrayList<>();
    
    // SubPicture 추가 및 삭제 메서드
    public void addSubPicture(SubPicture subPicture) {
        subPictures.add(subPicture);
        subPicture.setText(this);
    }

    public void removeSubPicture(SubPicture subPicture) {
        subPictures.remove(subPicture);
        subPicture.setText(null);
    }

    public List<SubPicture> getSubPictures() {
        return subPictures;
    }
    
    @OneToMany(mappedBy = "text", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Video> videos = new ArrayList<>();

    // Video 추가 및 삭제 메서드
    public void addVideo(Video video) {
        videos.add(video);
        video.setText(this);
    }

    public void removeVideo(Video video) {
        videos.remove(video);
        video.setText(null);
    }
    
    public List<Video> getVideos() {
        return videos;
    }
    
    public Division getDivision() {
        return division;
    }
    
    public void setDivision(Division division) {
		this.division = division;
    }
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    
	/**
	 * @return the teamName
	 */
	public String getTeamName() {
		return teamName;
	}
	/**
	 * @param teamName the teamName to set
	 */
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	
	/**
	 * @return the shortIntro
	 */
	public String getShortIntro() {
		return shortIntro;
	}
	/**
	 * @param shortIntro the shortIntro to set
	 */
	public void setShortIntro(String shortIntro) {
		this.shortIntro = shortIntro;
	}
	
	/**
	 * @return the teamMany
	 */
	public Integer getTeamMany() {
		return teamMany;
	}
	/**
	 * @param teamMany the teamMany to set
	 */
	public void setTeamMany(Integer teamMany) {
		this.teamMany = teamMany;
	}
	
	/**
	 * @return the longIntro
	 */
	public String getLongIntro() {
		return longIntro;
	}
	/**
	 * @param longIntro the longIntro to set
	 */
	public void setLongIntro(String longIntro) {
		this.longIntro = longIntro;
	}
	
	/**
	 * @return the portfolio
	 */
	public String getPortfolio() {
		return portfolio;
	}
	/**
	 * @param portfolio the portfolio to set
	 */
	public void setPortfolio(String portfolio) {
		this.portfolio = portfolio;
	}
	
	
	/**
	 * @return the repertoire
	 */
	public String getRepertoire() {
		return repertoire;
	}
	/**
	 * @param repertoire the repertoire to set
	 */
	public void setRepertoire(String repertoire) {
		this.repertoire = repertoire;
	}
	
	
	/**
	 * @return the equipment
	 */
	public String getEquipment() {
		return equipment;
	}
	/**
	 * @param equipment the equipment to set
	 */
	public void setEquipment(String equipment) {
		this.equipment = equipment;
	}
	
	 // Setter for subPictures
    public void setSubPictures(List<SubPicture> subPictures) {
        this.subPictures = subPictures;
    }
    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }
   
}
