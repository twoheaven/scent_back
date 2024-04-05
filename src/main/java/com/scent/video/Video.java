package com.scent.video;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.scent.text.Text;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String youtubeLink;

    @JsonIgnore
    @ManyToOne
    private Text text;

    public String getYoutubeLink() {
        return youtubeLink;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

	/**
	 * @param youtubeLink the youtubeLink to set
	 */
	public void setYoutubeLink(String youtubeLink) {
		this.youtubeLink = youtubeLink;
	}
	/**
	 * @return the text
	 */
	public Text getText() {
		return text;
	}
	/**
	 * @param text the text to set
	 */
	public void setText(Text text) {
		this.text = text;
	}

    // 생성자, 게터, 세터 등 필요한 메서드 추가
}
