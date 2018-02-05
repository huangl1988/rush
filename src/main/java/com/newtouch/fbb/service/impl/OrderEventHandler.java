package com.newtouch.fbb.service.impl;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;
import com.newtouch.fbb.mode.OrderEvent;
import com.newtouch.fbb.service.AbstractRushpay;

/**
 * Created by steven on 2018/1/26.
 */
public class OrderEventHandler implements EventHandler<OrderEvent>,WorkHandler<OrderEvent> {

    private static OrderEventHandler orderHandler = new OrderEventHandler();

    private OrderEventHandler(){}

    public static OrderEventHandler getInstance(){
        return new OrderEventHandler();
    }

    public static AbstractRushpay abstractRushpay=new RushPayImpl();

    @Override
    public void onEvent(OrderEvent orderEvent, long sequence, boolean endOfBatch) throws Exception {
//        try{
//            System.out.println("handler:"+sequence);
//            abstractRushpay.rush(orderEvent.getCommodyInfoList());
//            orderEvent.setCode("succ");
//
//        }catch(Exception e){
//            e.printStackTrace();
//            orderEvent.setCode("error");
//        }
    }

    @Override
    public void onEvent(OrderEvent orderEvent) throws Exception {
//        try{
//            System.out.println("handler:"+orderEvent.getUserId());
//            abstractRushpay.rush(orderEvent.getCommodyInfoList());
//            orderEvent.setCode("succ");
//
//        }catch(Exception e){
//            e.printStackTrace();
//            orderEvent.setCode("error");
//        }
    }
}
