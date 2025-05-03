package com.example.postservice.controller.response;

import com.example.postservice.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserJoinResponse {
    private Integer id;
    private String username;
    private UserRole role;
}
