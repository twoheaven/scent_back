package com.scent.recommend;

@SuppressWarnings("serial")
public class RecommendNotFoundException extends RuntimeException {

    public RecommendNotFoundException(String message) {
        super(message);
    }
}