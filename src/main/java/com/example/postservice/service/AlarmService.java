package com.example.postservice.service;

import com.example.postservice.exception.ErrorCode;
import com.example.postservice.exception.PostApplicationException;
import com.example.postservice.repository.EmitterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.web.embedded.TomcatVirtualThreadsWebServerFactoryCustomizer;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Log4j2
@Service
@RequiredArgsConstructor
public class AlarmService {

    private final static Long DEFAULT_TIMEOUT = 60L * 1000 * 60;
    private final static String ALARM_NAME = "alarm";

    private final EmitterRepository emitterRepository;

    public void send(Long alarmId, Integer userId) {
        emitterRepository.get(userId).ifPresentOrElse(
                sseEmitter -> {
                    try {
                        sseEmitter.send(SseEmitter.event().id(alarmId.toString()).name(ALARM_NAME).data("new alarm"));
                    } catch (IOException e) {
                        emitterRepository.delete(userId);
                        throw new PostApplicationException(ErrorCode.NOTIFICATION_CONNECT_ERROR);
                    }
                }
                , () -> log.info("No emitter founded")
        );
    }

    public SseEmitter connectAlarm(Integer userId) {
        SseEmitter sseEmitter = new SseEmitter(DEFAULT_TIMEOUT);

        emitterRepository.save(userId, sseEmitter);

        sseEmitter.onCompletion(() -> emitterRepository.delete(userId));
        sseEmitter.onTimeout(() -> emitterRepository.delete(userId));

        try {
            sseEmitter.send(SseEmitter.event().id("").name(ALARM_NAME).data("connect completed"));
        } catch (IOException e) {
            throw new PostApplicationException(ErrorCode.NOTIFICATION_CONNECT_ERROR);
        }

        return sseEmitter;
    }
}
