package com.example.postservice.controller;


import com.example.postservice.dto.PostDTO;
import com.example.postservice.dto.request.PostCreateRequest;
import com.example.postservice.dto.response.Response;
import com.example.postservice.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/{id}")
    public Response<Void> update(@RequestBody PostCreateRequest request, @PathVariable Long id, Authentication authentication) {
        postService.update(id, request.getTitle(), request.getContent(), authentication.getName());
        return Response.success();
    }

    @DeleteMapping("/{id}")
    public Response<Void> delete(@PathVariable Long id, Authentication authentication) {
        postService.delete(id, authentication.getName());
        return Response.success();
    }

    @GetMapping
    public Response<Page<PostDTO>> list(Pageable pageable, Authentication authentication) {
        return Response.success(postService.list(pageable));
    }

    @GetMapping("/my")
    public Response<Page<PostDTO>> my(Pageable pageable, Authentication authentication) {
        return Response.success(postService.my(pageable, authentication.getName()));
    }
}
