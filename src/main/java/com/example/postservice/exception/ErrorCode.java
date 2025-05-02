package com.example.postservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    DUPLICATED_USER_ID(HttpStatus.CONFLICT, "User id is duplicated"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found"),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "Access denied"),
    INCORRECT_PASSWORD(HttpStatus.UNAUTHORIZED, "Incorrect password"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Token is invalid"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "Post not found"),
    INVALID_PERMISSION(HttpStatus.UNAUTHORIZED, "Invalid permission"),
    ALREADY_LIKED(HttpStatus.CONFLICT, "Already liked post");

    private HttpStatus status;
    private String message;
}
