package com.example.postservice.filter;

import com.example.postservice.model.dto.UserDTO;
import com.example.postservice.service.UserService;
import com.example.postservice.util.JwtTokenUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Set;

/**
 * The type Jwt authentication filter.
 */
@Log4j2
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final String key;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(header == null || !header.startsWith("Bearer")) {
            log.error("Error occurs while getting header. header is null or invalid. request uri : {}", request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String token = header.split(" ")[1].trim();

            // 만료 기간 검증
            if(JwtTokenUtils.isExpired(token, key)) {
                log.error("Key is expired");
                filterChain.doFilter(request, response);
                return;
            }

            // user 검증
            String userId = JwtTokenUtils.getUserId(token, key);
            UserDTO user = userService.loadUserById(userId);

            // 유효한 경우에
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    user, null, user.getAuthorities()
            );
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        } catch(RuntimeException e) {
            log.error("Error occurs while validating. {}", e.toString());
            filterChain.doFilter(request, response);
            return;
        }

        filterChain.doFilter(request, response);

    }
}
