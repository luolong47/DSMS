package io.github.luolong47.service;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.jwt.JWTUtil;
import io.github.luolong47.dto.LoginRequest;
import io.github.luolong47.dto.LoginResponse;
import io.github.luolong47.entity.User;
import io.github.luolong47.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    
    @Value("${jwt.secret:your-256-bit-secret}")
    private String jwtSecret;
    
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("用户不存在"));
        
        String encodedPassword = SecureUtil.md5(request.getPassword());
        if (!StrUtil.equals(encodedPassword, user.getPassword())) {
            throw new BadCredentialsException("密码错误");
        }
        
        Map<String, Object> payload = new HashMap<>();
        payload.put("uid", user.getId());
        payload.put("username", user.getUsername());
        payload.put("jti", UUID.fastUUID().toString());
        
        String token = JWTUtil.createToken(payload, jwtSecret.getBytes());
        
        return LoginResponse.builder()
                .token(token)
                .userInfo(LoginResponse.UserInfo.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .build())
                .build();
    }
    
    public boolean validateToken(String token) {
        try {
            return JWTUtil.verify(token, jwtSecret.getBytes());
        } catch (Exception e) {
            return false;
        }
    }
}