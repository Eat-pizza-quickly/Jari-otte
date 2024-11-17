package com.eatpizzaquickly.userservice.repository;

import com.eatpizzaquickly.userservice.dto.UserResponseDto;
import com.eatpizzaquickly.userservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    Optional<UserResponseDto> findUserById(Long id);
    boolean existsByEmail(String email);

    User findByKakaoId(Long kakaoId);
}
