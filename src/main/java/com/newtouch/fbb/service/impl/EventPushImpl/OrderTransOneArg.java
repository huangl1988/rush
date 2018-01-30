package com.newtouch.fbb.service.impl.EventPushImpl;

import com.lmax.disruptor.EventTranslator;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;
import com.newtouch.fbb.mode.CommodyInfo;
import com.newtouch.fbb.mode.OrderEvent;

import java.util.List;

/**
 * Created by steven on 2018/1/26.
 */
public class OrderTransOneArg implements EventTranslatorOneArg<OrderEvent,List<CommodyInfo>> {

    private static OrderTransOneArg orderTransOneArg = new OrderTransOneArg();

    private OrderTransOneArg(){}

    public static OrderTransOneArg getInstance(){
        return orderTransOneArg;
    }



    @Override
    public void translateTo(OrderEvent current, long l,List<CommodyInfo> old) {
        System.out.println(l);
        current.setCommodyInfoList(old);
        //current.setUserId(old.getUserId());
    }
}
