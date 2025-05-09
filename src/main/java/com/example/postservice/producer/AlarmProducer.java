package com.example.postservice.producer;

import com.example.postservice.model.event.AlarmEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RequiredArgsConstructor
public class AlarmProducer {

    private final KafkaTemplate<Integer, AlarmEvent> kafkaTemplate;

    @Value("${spring.kafka.topic.alarm}")
    private String topic;

    public void send(AlarmEvent alarmEvent) {
        kafkaTemplate.send(topic, alarmEvent.getReceiveUserId(), alarmEvent);
        log.info("Send to kafka finished");
    }
}
