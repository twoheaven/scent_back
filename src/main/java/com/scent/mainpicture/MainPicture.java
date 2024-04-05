package com.scent.mainpicture;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.scent.text.Text;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MainPicture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String originalFileName;

    @NotEmpty
    private String storedFilePath;
    

    private Long fileSize;
    
    @JsonIgnore
    @OneToOne
    private Text text;

}