package com.example.postservice.fixture;

import com.example.postservice.model.entity.Post;
import com.example.postservice.model.entity.User;

public class PostFixture {
    public static Post get(Long postId, User user) {
        Post result = new Post();
        result.setId(postId);
        result.setUser(user);
        return result;
    }
}
