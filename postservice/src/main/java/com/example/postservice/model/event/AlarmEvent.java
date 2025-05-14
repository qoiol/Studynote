package com.example.postservice.model.event;

import com.example.postservice.model.AlarmArgs;
import com.example.postservice.model.AlarmType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlarmEvent {
    private Integer receiveUserId;
    private AlarmType alarmType;
    private AlarmArgs alarmArgs;
}
