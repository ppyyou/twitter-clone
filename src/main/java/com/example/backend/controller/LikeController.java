package com.example.backend.controller;

import com.example.backend.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;

    @PostMapping("/toggle/{postId}")
    public ResponseEntity<Boolean> toggle(
        @PathVariable Long postId,
        @AuthenticationPrincipal UserDetails user
    ) {
        return ResponseEntity.ok(likeService.toggleLike(postId, user.getUsername()));
    }

    @GetMapping("/status/{postId}")
    public ResponseEntity<Boolean> status(
        @PathVariable Long postId,
        @AuthenticationPrincipal UserDetails user
    ) {
        return ResponseEntity.ok(likeService.isLiked(postId, user.getUsername()));
    }
}


