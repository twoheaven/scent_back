package com.scent.subpicture;

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
public class SubPicture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String originalFileName;

    private String storedFilePath;

    private Long fileSize;
    
    @JsonIgnore
    @ManyToOne
    private Text text;

    // Getter 메서드
    public Long getId() {
        return id;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public String getStoredFilePath() {
        return storedFilePath;
    }

    public Long getFileSize() {
        return fileSize;
    }

    // Setter 메서드
    public void setId(Long id) {
        this.id = id;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public void setStoredFilePath(String storedFilePath) {
        this.storedFilePath = storedFilePath;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

	/**
	 * @param text the text to set
	 */
	public void setText(Text text) {
		this.text = text;
	}	
	public Text getText() {
		return text;
	}
}