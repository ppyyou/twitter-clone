package com.example.backend.controller;

import com.example.backend.dto.PostRequest;
import com.example.backend.dto.PostResponse;
import com.example.backend.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostResponse> create(
        @Valid @RequestBody PostRequest request,
        @AuthenticationPrincipal UserDetails user
    ) {
        return ResponseEntity.ok(postService.createPost(request, user.getUsername()));
    }

    @GetMapping
    public ResponseEntity<Page<PostResponse>> list(Pageable pageable) {
        return ResponseEntity.ok(postService.getPosts(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(postService.getPost(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostResponse> update(
        @PathVariable Long id,
        @Valid @RequestBody PostRequest request,
        @AuthenticationPrincipal UserDetails user
    ) {
        return ResponseEntity.ok(postService.updatePost(id, request, user.getUsername()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
        @PathVariable Long id,
        @AuthenticationPrincipal UserDetails user
    ) {
        postService.deletePost(id, user.getUsername());
        return ResponseEntity.noContent().build();
    }
}


