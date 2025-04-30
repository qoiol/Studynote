package com.example.postservice.dto;

import com.example.postservice.domain.User;
import com.example.postservice.domain.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostUserDTO {

    private String id;
    private UserRole role;

    public static PostUserDTO fromUser(User user) {
        PostUserDTO userDTO = new PostUserDTO();
        userDTO.setId(user.getId());
        userDTO.setRole(user.getRole());
        return userDTO;
    }
}
