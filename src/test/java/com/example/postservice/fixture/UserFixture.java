package com.example.postservice.fixture;

import com.example.postservice.model.entity.User;

public class UserFixture {
    public static User get(String userId, String password) {
        User result = new User();
        result.setUsername(userId);
        result.setPassword(password);
        return result;
    }
}
