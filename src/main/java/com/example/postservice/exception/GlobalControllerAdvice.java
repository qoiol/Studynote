package com.example.postservice.exception;

import com.example.postservice.dto.response.Response;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Log4j2
@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(PostApplicationException.class)
    public ResponseEntity<?> applicationHandler(PostApplicationException e) {
        log.error("Error occurs {}", e.toString());
        return ResponseEntity.status(e.getErrorCode().getStatus().value())
                .body(Response.error(e.getErrorCode().name()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> validationHandler(MethodArgumentNotValidException e) {
        log.error("Error occurs {}", e.toString());
        return ResponseEntity.status(e.getStatusCode())
                .body(Response.error(e.getFieldError().getDefaultMessage()));
    }
}
