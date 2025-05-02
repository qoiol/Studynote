package com.example.postservice.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserJoinRequest {
    @NotBlank(message = "enter 4 to 20 characters")
    @Size(max = 20, min = 4, message = "enter 4 to 20 characters")
    private String name;
    @NotBlank(message = "enter 4 to 20 characters")
    @Size(max = 20, min = 4, message = "enter 4 to 20 characters")
    private String password;
}
