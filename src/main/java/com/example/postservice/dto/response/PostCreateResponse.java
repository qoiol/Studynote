package com.example.postservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostCreateResponse {
    private String title;
    private String body;
    private String name;
}
