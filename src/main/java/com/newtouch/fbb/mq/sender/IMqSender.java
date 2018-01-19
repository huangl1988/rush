package com.newtouch.fbb.mq.sender;

import com.newtouch.fbb.mq.IMessage;

/**
 * Created by steven on 2018/1/18.
 */
public interface IMqSender {

    public void doSender(IMessage message);

}
