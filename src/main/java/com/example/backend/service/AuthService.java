package com.example.backend.service;

import com.example.backend.dto.AuthRequest;
import com.example.backend.dto.AuthResponse;
import com.example.backend.dto.RefreshTokenRequest;
import com.example.backend.dto.RegisterRequest;
import com.example.backend.entity.User;
import com.example.backend.repository.UserRepository;
import com.example.backend.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

        String access = jwtService.generateToken(user);
        // 간단화를 위해 refresh는 access와 동일한 만료로 임시 발급
        String refresh = jwtService.generateToken(user);
        return AuthResponse.builder().accessToken(access).refreshToken(refresh).build();
    }

    public AuthResponse login(AuthRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        User user = userRepository.findByUsername(request.getUsername())
            .orElseThrow(() -> new RuntimeException("Invalid credentials"));
        String access = jwtService.generateToken(user);
        String refresh = jwtService.generateToken(user);
        return AuthResponse.builder().accessToken(access).refreshToken(refresh).build();
    }

    public AuthResponse refreshToken(RefreshTokenRequest request) {
        // 데모 구현: refresh 토큰이 유효하면 동일 사용자로 새 access 발급
        String identifier = jwtService.extractUsername(request.getRefreshToken());
        User user = userRepository.findByUsername(identifier)
            .orElseGet(() -> userRepository.findById(parseId(identifier)).orElseThrow());
        if (!jwtService.isTokenValid(request.getRefreshToken(), user)) {
            throw new RuntimeException("Invalid refresh token");
        }
        String access = jwtService.generateToken(user);
        String refresh = jwtService.generateToken(user);
        return AuthResponse.builder().accessToken(access).refreshToken(refresh).build();
    }

    private Long parseId(String s) {
        try { return Long.parseLong(s); } catch (Exception e) { return -1L; }
    }
}


