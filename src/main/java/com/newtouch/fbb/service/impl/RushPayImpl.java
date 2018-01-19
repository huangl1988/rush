package com.newtouch.fbb.service.impl;

import com.newtouch.fbb.common.CommonContants;
import com.newtouch.fbb.common.demo.DemoRedisUtil;
import com.newtouch.fbb.mode.CommodyInfo;
import com.newtouch.fbb.mq.IMessage;
import com.newtouch.fbb.mq.sender.impl.SenderImpl;
import com.newtouch.fbb.mq.vo.MessageVo;
import com.newtouch.fbb.service.AbstractRushpay;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by steven on 2018/1/19.
 */
public class RushPayImpl extends AbstractRushpay{

    public RushPayImpl(){
        this.abstractRedisUtil=new DemoRedisUtil();
        abstractRedisUtil.init();
        this.sender=new SenderImpl();
    }

    @Override
    public IMessage buildMessage(List<CommodyInfo> commodyInfos, String orderNo) {

        return MessageVo.builder().eventTime(LocalDateTime.now()).messageBody(commodyInfos).messageId(orderNo).topic(CommonContants.TOPIC_NAME).build();
    }

    @Override
    protected void checkOther(List<CommodyInfo> commodyInfoList) {
        return;
    }
}
