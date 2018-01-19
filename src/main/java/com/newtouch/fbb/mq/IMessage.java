package com.newtouch.fbb.mq;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by steven on 2018/1/18.
 */
public interface IMessage extends Serializable{

    public String getMessageId();

    public <T> T getMessageBody();

    public LocalDateTime getEventTime();

    public String getTopic();

}
