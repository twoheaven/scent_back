package com.scent.text;

import org.springframework.data.jpa.domain.Specification;

public class TextSpecification {
    
    public static Specification<Text> textContains(String keyword) {
        return (root, query, criteriaBuilder) -> 
            criteriaBuilder.or(
                criteriaBuilder.like(root.get("equipment"), "%" + keyword + "%"),
                criteriaBuilder.like(root.get("longIntro"), "%" + keyword + "%"),
                criteriaBuilder.like(root.get("portfolio"), "%" + keyword + "%"),
                criteriaBuilder.like(root.get("repertoire"), "%" + keyword + "%"),
                criteriaBuilder.like(root.get("shortIntro"), "%" + keyword + "%"),
                criteriaBuilder.like(root.get("teamName"), "%" + keyword + "%")
            );
    }
}
