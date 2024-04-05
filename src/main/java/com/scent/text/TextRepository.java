package com.scent.text;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TextRepository extends JpaRepository<Text, Long>, JpaSpecificationExecutor<Text> {
	
    // Pageable 파라미터를 통해 페이징 정보를 받아올 수 있음
    Page<Text> findAll(Pageable pageable);
}
