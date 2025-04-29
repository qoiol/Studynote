package com.example.postservice.controller;


import com.example.postservice.dto.request.PostCreateRequest;
import com.example.postservice.dto.response.PostCreateResponse;
import com.example.postservice.dto.response.Response;
import com.example.postservice.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping
    public Response<Void> create(@RequestBody PostCreateRequest request, Authentication authentication) {
        postService.create(request.getTitle(), request.getContent(), authentication.getName());

//        log.error("postcreate authentication {}, {}", authentication.getName(), authentication.getAuthorities());
        return Response.success();
    }
}
