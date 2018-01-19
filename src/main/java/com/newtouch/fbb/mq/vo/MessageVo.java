package com.newtouch.fbb.mq.vo;

import com.newtouch.fbb.mode.CommodyInfo;
import com.newtouch.fbb.mq.IMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by steven on 2018/1/19.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageVo implements IMessage {

    private List<CommodyInfo> messageBody;

    private String messageId;

    private LocalDateTime eventTime;

    private String topic;

    @Override
    public String getMessageId() {
        return messageId;
    }

    @Override
    public List<CommodyInfo> getMessageBody() {
        return messageBody;
    }

    @Override
    public LocalDateTime getEventTime() {
        return eventTime;
    }

    @Override
    public String getTopic() {
        return topic;
    }
}
