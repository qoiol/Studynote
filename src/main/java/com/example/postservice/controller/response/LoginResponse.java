package com.example.postservice.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {
//    private String id;
//    private UserRole role;
    private String token;

//    @Override
//    public String toString() {
//        return "login-info{" +
//                "id='" + id + '\'' +
//                ", role=" + role +
//                ", token='" + token + '\'' +
//                '}';
//    }
}
