package com.newtouch.fbb.mq.sender.impl;

import com.newtouch.fbb.mq.IMessage;
import com.newtouch.fbb.mq.sender.IMqSender;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by steven on 2018/1/19.
 */
public class SenderImpl implements IMqSender {

    public static ConcurrentLinkedQueue<IMessage> queue= new ConcurrentLinkedQueue<>();

    @Override
    public void doSender(IMessage message) {
        queue.add(message);
    }
}
