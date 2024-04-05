package com.scent.banner;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Banner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String originalFileName;

    @NotEmpty
    private String storedFilePath;

    private Long fileSize;
    
    private String backcolor;

	/**
	 * @return the originalFileName
	 */
	public String getOriginalFileName() {
		return originalFileName;
	}

	/**
	 * @param originalFileName the originalFileName to set
	 */
	public void setOriginalFileName(String originalFileName) {
		this.originalFileName = originalFileName;
	}

	/**
	 * @return the fileSize
	 */
	public Long getFileSize() {
		return fileSize;
	}

	/**
	 * @param fileSize the fileSize to set
	 */
	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	/**
	 * @return the storedFilePath
	 */
	public String getStoredFilePath() {
		return storedFilePath;
	}

	/**
	 * @param storedFilePath the storedFilePath to set
	 */
	public void setStoredFilePath(String storedFilePath) {
		this.storedFilePath = storedFilePath;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the backcolor
	 */
	public String getBackcolor() {
		return backcolor;
	}

	/**
	 * @param backcolor the backcolor to set
	 */
	public void setBackcolor(String backcolor) {
		this.backcolor = backcolor;
	}

    // Getter, Setter, 기타 메서드 생략
}