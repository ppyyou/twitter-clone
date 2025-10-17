package com.example.backend.service;

import com.example.backend.dto.UserResponse;
import com.example.backend.entity.Follow;
import com.example.backend.entity.User;
import com.example.backend.exception.BadRequestException;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.repository.FollowRepository;
import com.example.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class FollowService {
    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    /**
     * 팔로우 토글
     */
    public boolean toggleFollow(Long targetUserId, String currentUsername) {
        User currentUser = userRepository.findByUsername(currentUsername)
            .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다"));

        User targetUser = userRepository.findById(targetUserId)
            .orElseThrow(() -> new ResourceNotFoundException("대상 사용자를 찾을 수 없습니다"));

        // 자기 자신 팔로우 방지
        if (currentUser.getId().equals(targetUser.getId())) {
            throw new BadRequestException("자기 자신을 팔로우할 수 없습니다");
        }

        // 이미 팔로우했는지 확인
        Optional<Follow> existingFollow = followRepository.findByFollowerAndFollowing(
            currentUser, targetUser
        );

        if (existingFollow.isPresent()) {
            // 언팔로우
            followRepository.delete(existingFollow.get());
            return false;
        } else {
            // 팔로우
            Follow follow = new Follow();
            follow.setFollower(currentUser);
            follow.setFollowing(targetUser);
            followRepository.save(follow);
            return true;
        }
    }

    /**
     * 팔로워 목록 조회
     */
    @Transactional(readOnly = true)
    public List<UserResponse> getFollowers(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다"));

        List<Follow> follows = followRepository.findByFollowing(user);
        return follows.stream()
            .map(follow -> UserResponse.from(follow.getFollower()))
            .toList();
    }

    /**
     * 팔로잉 목록 조회
     */
    @Transactional(readOnly = true)
    public List<UserResponse> getFollowing(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다"));

        List<Follow> follows = followRepository.findByFollower(user);
        return follows.stream()
            .map(follow -> UserResponse.from(follow.getFollowing()))
            .toList();
    }
}


