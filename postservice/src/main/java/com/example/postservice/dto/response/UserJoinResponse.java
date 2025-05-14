package com.example.postservice.dto.response;

import com.example.postservice.domain.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserJoinResponse {
    private String id;
    private UserRole role;
}
