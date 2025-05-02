package com.example.postservice.controller.response;

import com.example.postservice.model.UserRole;
import com.example.postservice.model.dto.UserDTO;
import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoResponse {
    private String id;
    private UserRole role;

    public static UserInfoResponse fromUserDTO(UserDTO user) {
        return UserInfoResponse.builder()
                .id(user.getId())
                .role(user.getRole())
                .build();
    }
}
