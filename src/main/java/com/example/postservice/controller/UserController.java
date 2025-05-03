package com.example.postservice.controller;


import com.example.postservice.controller.response.*;
import com.example.postservice.exception.ErrorCode;
import com.example.postservice.exception.PostApplicationException;
import com.example.postservice.controller.request.UserDeleteRequest;
import com.example.postservice.controller.request.UserJoinRequest;
import com.example.postservice.controller.request.LoginRequest;
import com.example.postservice.model.dto.UserDTO;
import com.example.postservice.service.UserService;
import com.example.postservice.util.ClassUtils;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @PostMapping("/join")
    public Response<UserJoinResponse> join(@RequestBody @Valid UserJoinRequest request) {
        return Response.success(userService.join(request.getName(), request.getPassword()));
    }

    @PostMapping("/login")
    public Response<LoginResponse> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        LoginResponse loginInfo = userService.login(request.getName(), request.getPassword());
//        JwtTokenUtils.addCookie("access-token", loginInfo.getToken(), response);
        log.info("login info : {}", loginInfo.toString());
        return Response.success(loginInfo);
    }

    @DeleteMapping("/{id}")
    public Response<UserDeleteResponse> delete(@PathVariable String id, @RequestBody UserDeleteRequest request) {
        return Response.success(userService.deleteUser(id, request.getPassword()));
    }

    @GetMapping("/alarm")
    public Response<Page<AlarmResponse>> alarm(Pageable pageable, Authentication authentication) {
        UserDTO user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), UserDTO.class)
                .orElseThrow(() -> new PostApplicationException(ErrorCode.INTERNAL_SERVER_ERROR, "Casting to User class failed"));
        return Response.success(userService.alarmList(pageable, user.getUsername()).map(AlarmResponse::fromAlarmDTO));
    }

}
