package com.example.postservice.fixture;

import com.example.postservice.model.entity.User;

public class UserFixture {
    public static User get(Integer id, String username, String password) {
        User result = new User();
        result.setId(id);
        result.setUsername(username);
        result.setPassword(password);
        return result;
    }
}
