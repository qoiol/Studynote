package com.example.postservice.service;

import com.example.postservice.model.entity.User;
import com.example.postservice.model.dto.AlarmDTO;
import com.example.postservice.model.dto.UserDTO;
import com.example.postservice.controller.response.UserDeleteResponse;
import com.example.postservice.controller.response.UserInfoResponse;
import com.example.postservice.controller.response.UserJoinResponse;
import com.example.postservice.controller.response.LoginResponse;
import com.example.postservice.exception.ErrorCode;
import com.example.postservice.exception.PostApplicationException;
import com.example.postservice.repository.AlarmRepository;
import com.example.postservice.repository.UserCacheRepository;
import com.example.postservice.repository.UserRepository;
import com.example.postservice.util.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AlarmRepository alarmRepository;
    private final UserCacheRepository userCacheRepository;
    private final BCryptPasswordEncoder encoder;

    @Value("${jwt.secret-key}")
    private String secretKey;
    @Value("${jwt.expired-time-ms}")
    private long expiredTimeMs;

    public UserDTO loadUserById(String username) {
        return userCacheRepository.getUser(username).orElseGet(() ->
                userRepository.findByUsername(username).map(UserDTO::fromUser).orElseThrow(() ->
                        new PostApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s is not found", username)))
        );
    }

    @Transactional
    public UserJoinResponse join(String username, String password) {

        userRepository.findByUsername(username).ifPresent(it -> {
            throw new PostApplicationException(ErrorCode.DUPLICATED_USER_ID, String.format("%s is duplicated", username));
        });

        User user = new User();
        user.setUsername(username);
        user.setPassword(encoder.encode(password));
        userRepository.save(user);

        return new UserJoinResponse(user.getId(), user.getUsername(), user.getRole());
    }

    @Transactional
    public LoginResponse login(String username, String password) {
        // user 정보 없을 때 exception
//        User user = userRepository.findByUsername(username).orElseThrow(() -> new PostApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s is not exists", username)));
        // -> 캐시 먼저 검색 -> db
        UserDTO user = loadUserById(username);

        // redis 캐싱
        userCacheRepository.setUser(user);

        // 비밀번호 틀릴 때 exception
        if(!encoder.matches(password, user.getPassword())) {
            throw new PostApplicationException(ErrorCode.INCORRECT_PASSWORD);
        }

        // 토큰 생성
        String token = JwtTokenUtils.generateToken(username, user.getRole().name(), secretKey, expiredTimeMs);

        return new LoginResponse(token);
    }

    @Transactional
    public UserInfoResponse getUserInfo(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new PostApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s is not exists", username)));

        return new UserInfoResponse(user.getUsername(), user.getRole());
    }


    @Transactional
    public UserDeleteResponse deleteUser(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new PostApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s is not exists", username)));

        if(!encoder.matches(password, user.getPassword()))
            throw new PostApplicationException(ErrorCode.INCORRECT_PASSWORD, "Please check your password and try again.");

        userRepository.deleteUserFromDB(username);
        return new UserDeleteResponse(username);
    }

    public Page<AlarmDTO> alarmList(Pageable pageable, Integer userId) {
//        User user = userRepository.findById(userId).orElseThrow(() -> new PostApplicationException(ErrorCode.USER_NOT_FOUND, String.format("user %s not found", userId)));
//        return alarmRepository.findByUser(pageable, user).map(AlarmDTO::fromAlarm);
        return alarmRepository.findByUserId(pageable, userId).map(AlarmDTO::fromAlarm);
    }
}
