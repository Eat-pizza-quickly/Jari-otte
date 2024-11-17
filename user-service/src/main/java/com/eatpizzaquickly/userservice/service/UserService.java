package com.eatpizzaquickly.userservice.service;


import com.eatpizzaquickly.userservice.common.config.JwtUtils;
import com.eatpizzaquickly.userservice.common.config.PasswordEncoder;
import com.eatpizzaquickly.userservice.dto.HostPointRequestDto;
import com.eatpizzaquickly.userservice.dto.KakaoUserDto;
import com.eatpizzaquickly.userservice.dto.UserRequestDto;
import com.eatpizzaquickly.userservice.dto.UserResponseDto;
import com.eatpizzaquickly.userservice.entity.HostBalance;
import com.eatpizzaquickly.userservice.entity.User;
import com.eatpizzaquickly.userservice.entity.UserRole;
import com.eatpizzaquickly.userservice.exception.*;
import com.eatpizzaquickly.userservice.repository.HostBalanceRepository;
import com.eatpizzaquickly.userservice.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final HostBalanceRepository hostBalanceRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final RedisTemplate<String, String> redisTemplate;
    private final EmailService emailService;

    @Transactional
    public UserResponseDto signUp(UserRequestDto userRequestDto, String token) {
        if (userRepository.existsByEmail(userRequestDto.getEmail())) {
            throw new DuplicateUserException("이미 가입된 유저입니다.");
        }

        verifyToken(userRequestDto.getEmail(), token);

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
                .orElseThrow(() -> new UserNotFoundException("유저를 찾을 수 없습니다."));

        if (!passwordEncoder.matches(userRequestDto.getPassword(), user.getPassword())) {
            throw new PasswordNotMatchException("잘못된 비밀번호 입니다.");
        }

        String accessToken = jwtUtils.createToken(user.getId(), user.getUserRole());
        String refreshToken = jwtUtils.createRefreshToken(user.getId(), user.getUserRole());
        // Redis에 RefreshToken 저장
        redisTemplate.opsForValue().set(
                "RT:" + user.getEmail(),
                refreshToken,
                jwtUtils.getRefreshTokenExpirationTime(),
                TimeUnit.MILLISECONDS
        );
        return accessToken;
    }

    public String logout(String accessToken) {
        String email = jwtUtils.getUserIdFromToken(accessToken);
        // 리프레시 토큰 삭제
        redisTemplate.delete("RT:" + email);
        // 액세스 토큰 블랙리스트 등록
        Claims claims = jwtUtils.extractClaims(accessToken);
        long expiration = claims.getExpiration().getTime() - System.currentTimeMillis();
        if (expiration > 0) {
            redisTemplate.opsForValue().set(
                    "BL:" + accessToken,
                    "logout",
                    expiration,
                    TimeUnit.MILLISECONDS);
        }

        return null;
    }


    @Cacheable(value = "userById", key = "#id", cacheManager = "redisCacheManager")
    public UserResponseDto myPage(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("유저를 찾을 수 없습니다."));
        return UserResponseDto.from(user);
    }

    @Transactional
    public UserResponseDto updateUser(Long id, UserRequestDto userRequestDto) {
        String newPassword = null;
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("유저를 찾을수 없습니다."));
        // 비밀번호가 동일하지 않으면 새 비밀번호로 인코딩, 동일하면 기존 비밀번호 유지
        if (!passwordEncoder.matches(userRequestDto.getPassword(), user.getPassword())) {
            newPassword = passwordEncoder.encode(userRequestDto.getPassword());
        } else {
            newPassword = user.getPassword();  // 기존 비밀번호 유지
        }
        user.updateUser(newPassword, userRequestDto.getNickname());
        user = userRepository.save(user);
        return UserResponseDto.from(user);
    }

    @Transactional
    public void deleteUser(Long id, String password) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new PasswordNotMatchException("유저를 찾을수 없습니다"));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new PasswordNotMatchException("비밀번호가 일치 하지않습니다.");
        }
        user.deleteAccount();
    }

    @Cacheable(value = "userById", key = "#id", cacheManager = "redisCacheManager")
    public UserResponseDto findById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("유저를 찾을 수 없습니다"));
        return UserResponseDto.from(user);
    }

    public List<UserResponseDto> findAll() {
        List<User> users = userRepository.findAll();

        // User 엔티티 리스트를 UserResponseDto 리스트로 변환
        return users.stream()
                .map(UserResponseDto::from) // UserResponseDto.from(User user) 메서드 사용
                .toList();
    }

    public void verifyToken(String email, String token) {
        String verifyToken = redisTemplate.opsForValue().get(email);

        if (verifyToken == null) {
            emailService.sendMail(email);
            throw new VerifyExpiredException("만료된 인증 번호입니다. 재발송된 번호를 다시 입력해주세요.");
        }
        if (!token.equals(verifyToken)) {
            throw new TokenNotMatchException("인증 실패! 인증 번호를 다시 입력해주세요");
        }
        log.info(email + " 인증 확인");
    }

    public boolean isUserExists(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional
    public String kakaoLogin(KakaoUserDto kakaoUser) {
        User user = userRepository.findByKakaoId(kakaoUser.getId());
        if (user == null) {
            user = userRepository.findByEmail(kakaoUser.getKakaoAccount().getEmail())
                    .orElse(null);
            if (user != null) {
                user.setKakaoId(kakaoUser.getId());
            } else {
                user = User.builder()
                        .email(kakaoUser.getKakaoAccount().getEmail())
                        .password("kakao" + kakaoUser.getId())
                        .nickname(kakaoUser.getKakaoAccount().getProfile().getNickName())
                        .userRole(UserRole.USER)
                        .kakaoId(kakaoUser.getId())
                        .build();
            }
            User savedUser = userRepository.save(user);
        }
        String accessToken = jwtUtils.createToken(user.getId(), user.getUserRole());
        String refreshToken = jwtUtils.createRefreshToken(user.getId(), user.getUserRole());
        // Redis에 RefreshToken 저장
        redisTemplate.opsForValue().set(
                "RT:" + user.getEmail(),
                refreshToken,
                jwtUtils.getRefreshTokenExpirationTime(),
                TimeUnit.MILLISECONDS
        );
        return accessToken;
    }

    @Transactional
    public void addPointsToHosts(List<HostPointRequestDto> hostpoints) {
        for (HostPointRequestDto hostPoint : hostpoints) {
            Long hostId = hostPoint.getHostId();
            Long points = hostPoint.getPoints();

            Optional<HostBalance> optionalHostBalance = hostBalanceRepository.findByHostId(hostId);

            HostBalance hostBalance = optionalHostBalance
                    .map(balance -> {
                        balance.addBalance(points);
                        return balance;
                    }).orElse(HostBalance.builder()
                            .hostId(hostId)
                            .balance(points)
                            .build());

            hostBalanceRepository.save(hostBalance);
        }
    }
}
