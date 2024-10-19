package com.eatpizzaquickly.jariotte.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private Boolean isDelete = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole userRole;


    public User(String email,UserRole userRole) {
        this.email = email;
        this.userRole = userRole;
    }

    public User(String email, String password, String nickname, UserRole userRole) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.userRole = userRole;
    }
    public void deleteAccount(){
        this.isDelete = true;
    }

}
