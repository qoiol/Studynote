package com.example.postservice.repository;

import com.example.postservice.model.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Log4j2
@Repository
@RequiredArgsConstructor
public class UserCacheRepository {
    private final RedisTemplate<String, UserDTO> userRedisTemplate;

    private final static Duration USER_CACHE_TTL = Duration.ofDays(3);

    public void setUser(UserDTO user) {
        String key = getKey(user.getUsername());
        log.info("Set User to Redis {}, {}", key, user);
        userRedisTemplate.opsForValue().set(key, user, USER_CACHE_TTL);
    }

    public Optional<UserDTO> getUser(String username) {
        String key = getKey(username);
        UserDTO user = userRedisTemplate.opsForValue().get(getKey(username));
        log.info("Get User to Redis {}, {}", key, user);
        return Optional.ofNullable(user);
    }

    private String getKey(String username) {
        return "USER:" + username;
    }
}
