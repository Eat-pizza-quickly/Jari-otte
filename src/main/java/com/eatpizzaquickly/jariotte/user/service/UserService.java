package com.eatpizzaquickly.jariotte.user.service;

import com.eatpizzaquickly.jariotte.domain.common.config.JwtUtils;
import com.eatpizzaquickly.jariotte.domain.common.config.PasswordEncoder;
import com.eatpizzaquickly.jariotte.user.dto.UserRequestDto;
import com.eatpizzaquickly.jariotte.user.dto.UserResponseDto;
import com.eatpizzaquickly.jariotte.user.entity.User;
import com.eatpizzaquickly.jariotte.user.entity.UserRole;
import com.eatpizzaquickly.jariotte.user.exception.DuplicateUserException;
import com.eatpizzaquickly.jariotte.user.exception.NotMatchException;
import com.eatpizzaquickly.jariotte.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final RedisTemplate<String, String> redisTemplate;

    @Transactional
    public UserResponseDto signUp(UserRequestDto userRequestDto) {
        if(userRepository.existsByEmail(userRequestDto.getEmail())) {
            throw new DuplicateUserException("이미 가입된 유저입니다.");
        }
        String password = passwordEncoder.encode(userRequestDto.getPassword());
        UserRole role = Optional.ofNullable(userRequestDto.getUserRole()).orElse(UserRole.USER);

        User user = User.builder()
                .email(userRequestDto.getEmail())
                .password(password)
                .nickname(userRequestDto.getNickname())
                .userRole(role)
                .build();
        user = userRepository.save(user);
        return UserResponseDto.from(user);
    }

    @Transactional
    public String login(UserRequestDto userRequestDto) {
        User user = userRepository.findByEmail(userRequestDto.getEmail())
                .orElseThrow();

        if (!passwordEncoder.matches(userRequestDto.getPassword(), user.getPassword())){
            throw new NotMatchException("잘못된 비밀번호 입니다.");
        }

        String accessToken = jwtUtils.createToken(user.getEmail(),user.getUserRole());
        String refreshToken = jwtUtils.createRefreshToken(user.getEmail(),user.getUserRole());
        // Redis에 RefreshToken 저장
        redisTemplate.opsForValue().set(
                "RT:" + user.getEmail(),
                refreshToken,
                jwtUtils.getRefreshTokenExpirationTime(),
                TimeUnit.MILLISECONDS
        );
        return accessToken;
    }

    public UserResponseDto MyPage(String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new NotMatchException("유저를 찾을 수 없습니다."));
        return UserResponseDto.from(user);
    }

    @Transactional
    public UserResponseDto updateUser(String email,UserRequestDto userRequestDto) {
        String newPassword = null;
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new NotMatchException("유저를 찾을수 없습니다."));
        // 비밀번호가 동일하지 않으면 새 비밀번호로 인코딩, 동일하면 기존 비밀번호 유지
        if (!passwordEncoder.matches(userRequestDto.getPassword(), user.getPassword())) {
            newPassword = passwordEncoder.encode(userRequestDto.getPassword());
        } else {
            newPassword = user.getPassword();  // 기존 비밀번호 유지
        }
        user.updateUser(newPassword,userRequestDto.getNickname());
        user = userRepository.save(user);
        return UserResponseDto.from(user);
    }

    @Transactional
    public void deleteUser(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new NotMatchException("유저를 찾을수 없습니다"));
        if(!passwordEncoder.matches(password,user.getPassword())){
            throw new NotMatchException("비밀번호가 일치 하지않습니다.");
        }
        user.deleteAccount();
    }



}
