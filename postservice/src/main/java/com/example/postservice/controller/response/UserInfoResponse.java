package com.example.postservice.controller.response;

import com.example.postservice.model.UserRole;
import com.example.postservice.model.dto.UserDTO;
import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoResponse {
    private String username;
    private UserRole role;

    public static UserInfoResponse fromUserDTO(UserDTO user) {
        return UserInfoResponse.builder()
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }
}
