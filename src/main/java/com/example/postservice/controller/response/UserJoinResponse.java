package com.example.postservice.controller.response;

import com.example.postservice.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserJoinResponse {
    private String id;
    private UserRole role;
}
