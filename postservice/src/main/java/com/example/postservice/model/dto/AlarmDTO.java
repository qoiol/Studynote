package com.example.postservice.model.dto;

import com.example.postservice.model.entity.Alarm;
import com.example.postservice.model.AlarmArgs;
import com.example.postservice.model.AlarmType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlarmDTO {
    private Long id;
    private AlarmType alarmType;
    private AlarmArgs alarmArgs;
    private Timestamp registeredAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public static AlarmDTO fromAlarm(Alarm alarm) {
        return AlarmDTO.builder()
                .id(alarm.getId())
                .alarmType(alarm.getAlarmType())
                .alarmArgs(alarm.getArgs())
                .registeredAt(alarm.getRegisteredAt())
                .updatedAt(alarm.getUpdatedAt())
                .deletedAt(alarm.getDeletedAt())
                .build();
    }

}
