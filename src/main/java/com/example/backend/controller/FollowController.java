package com.example.backend.controller;

import com.example.backend.dto.UserResponse;
import com.example.backend.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/follows")
@RequiredArgsConstructor
public class FollowController {
    private final FollowService followService;

    @PostMapping("/toggle/{targetUserId}")
    public ResponseEntity<Boolean> toggle(
        @PathVariable Long targetUserId,
        @AuthenticationPrincipal UserDetails user
    ) {
        return ResponseEntity.ok(followService.toggleFollow(targetUserId, user.getUsername()));
    }

    @GetMapping("/{userId}/followers")
    public ResponseEntity<List<UserResponse>> followers(@PathVariable Long userId) {
        return ResponseEntity.ok(followService.getFollowers(userId));
    }

    @GetMapping("/{userId}/following")
    public ResponseEntity<List<UserResponse>> following(@PathVariable Long userId) {
        return ResponseEntity.ok(followService.getFollowing(userId));
    }
}


