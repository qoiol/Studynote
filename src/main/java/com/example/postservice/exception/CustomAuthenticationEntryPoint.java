package com.example.postservice.exception;

import com.example.postservice.dto.response.Response;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

@Log4j2
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.error("entrypoint");
        response.setContentType("application/json");
        response.setStatus(ErrorCode.INVALID_TOKEN.getStatus().value());
        response.getWriter().write(Response.error(ErrorCode.INVALID_TOKEN.name()).toStream());
    }
}
