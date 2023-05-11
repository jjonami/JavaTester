package kr.co.sampler.directSend;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;

@Data
@AllArgsConstructor
@Builder
public class DirectSendDTO {
    private String alarmCode;
    private String alarmType;
    private String alarmUrl;
    private HashMap<String, Object> parameters;
}
