package com.example.demo.dto;

import com.example.demo.domain.Post;
import lombok.Builder;

public class PostSaveDto {

    private String title;
    private String content;
    private String author;

    @Builder
    public PostSaveDto(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }
}
