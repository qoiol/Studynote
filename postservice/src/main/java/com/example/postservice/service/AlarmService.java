package com.example.postservice.service;

import com.example.postservice.exception.ErrorCode;
import com.example.postservice.exception.PostApplicationException;
import com.example.postservice.model.AlarmArgs;
import com.example.postservice.model.AlarmType;
import com.example.postservice.model.entity.Alarm;
import com.example.postservice.model.entity.User;
import com.example.postservice.repository.AlarmRepository;
import com.example.postservice.repository.EmitterRepository;
import com.example.postservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
    private final AlarmRepository alarmRepository;
    private final UserRepository userRepository;

    public void send(AlarmType type, AlarmArgs args, Integer receiveUserId) {

        User user = userRepository.findById(receiveUserId).orElseThrow(() -> new PostApplicationException(ErrorCode.USER_NOT_FOUND));

        // alarm save
        Alarm alarm = alarmRepository.save(Alarm.builder()
                .alarmType(type)
                .args(args)
                .user(user)
                .build()
        );

        emitterRepository.get(user.getId()).ifPresentOrElse(
                sseEmitter -> {
                    try {
                        sseEmitter.send(SseEmitter.event().id(alarm.getId().toString()).name(ALARM_NAME).data("new alarm"));
                    } catch (IOException e) {
                        emitterRepository.delete(user.getId());
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
