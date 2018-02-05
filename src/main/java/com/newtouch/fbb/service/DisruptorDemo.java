package com.newtouch.fbb.service;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.newtouch.fbb.mode.CommodyInfo;
import com.newtouch.fbb.mode.OrderEvent;
import com.newtouch.fbb.mq.sender.impl.SenderImpl;
import com.newtouch.fbb.service.impl.EventPushImpl.OrderTransOneArg;
import com.newtouch.fbb.service.impl.OrderEventHandler;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by steven on 2018/1/26.
 */
public class DisruptorDemo {

    private static Disruptor<OrderEvent> disruptor;

    private static int RINGBUFFER_SIZE=2048;

    private static RingBuffer<OrderEvent> ringBuffer;

    RestTemplate restTemplate = new RestTemplate();

    static{
        Executor executor=Executors.newFixedThreadPool(10);
        disruptor=new Disruptor<OrderEvent>(OrderEvent::new,RINGBUFFER_SIZE, executor, ProducerType.MULTI,new YieldingWaitStrategy());

        disruptor.handleEventsWith(OrderEventHandler.getInstance()).then((event,v,l)->{
            event=null;
        });
        disruptor.start();
        ringBuffer=RingBuffer.create(ProducerType.MULTI,OrderEvent::new,RINGBUFFER_SIZE,new YieldingWaitStrategy());
//        OrderEventHandler[] orderEventHandler = new OrderEventHandler[10];
//        for(int i=0;i<orderEventHandler.length;i++)
//            orderEventHandler[i]=OrderEventHandler.getInstance();
//        SequenceBarrier sb=ringBuffer.newBarrier();
//        WorkerPool workerPool=new WorkerPool(ringBuffer, sb, new ExceptionHandler() {
//            @Override
//            public void handleEventException(Throwable throwable, long l, Object o) {
//
//            }
//
//            @Override
//            public void handleOnStartException(Throwable throwable) {
//
//            }
//
//            @Override
//            public void handleOnShutdownException(Throwable throwable) {
//
//            }
//        },orderEventHandler);
//        ringBuffer.addGatingSequences(workerPool.getWorkerSequences());
//        workerPool.start(executor);

    }


    static List<CommodyInfo>  infoList = new ArrayList<>();

    public static void initData(){

        CommodyInfo commodyInfo = CommodyInfo.builder().comodyCode("001").number(2l).build();
        infoList.add(commodyInfo);
        CommodyInfo commodyInfo2 = CommodyInfo.builder().comodyCode("002").number(2l).build();
        infoList.add(commodyInfo2);

    }

    public static void main(String arg[]){
        initData();
        Long start = System.currentTimeMillis();
        List<Future> list = new ArrayList<Future>();
        ExecutorService executorService = Executors.newFixedThreadPool(200);
        for (int i=0;i<200;i++){
            pushData(infoList,i);
        }

        Long end = System.currentTimeMillis();
        System.out.println(SenderImpl.queue.size());
        System.out.println(end-start);

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        OrderEvent event = ringBuffer.get(1);

        System.out.println(event.getCode());

    }

    public static long pushData(List<CommodyInfo> list,int i){
        long seq=ringBuffer.next();
        OrderEvent event=ringBuffer.get(seq);
        event.setCommodyInfoList(list);
        event.setUserId(i+"");
        ringBuffer.publish(seq);
        return 1l;
    }

    public static OrderEvent getData(Long seq){
        return ringBuffer.get(seq);
    }







}
