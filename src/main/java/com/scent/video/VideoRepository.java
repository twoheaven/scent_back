package com.scent.video;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.scent.text.Text;

public interface VideoRepository extends JpaRepository<Video, Long> {

	List<Video> findByText(Text text);
}
