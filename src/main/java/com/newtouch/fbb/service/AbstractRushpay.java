package com.newtouch.fbb.service;

import com.newtouch.fbb.common.AbstractRedisUtil;
import com.newtouch.fbb.common.CommonContants;
import com.newtouch.fbb.mode.CommodyInfo;
import com.newtouch.fbb.mq.IMessage;
import com.newtouch.fbb.mq.sender.IMqSender;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 * Created by steven on 2018/1/18.
 */
public abstract class AbstractRushpay {

    protected AbstractRedisUtil abstractRedisUtil;

    protected IMqSender sender;


    private boolean isEnough(String commodyCode,Long orderNum){
        return orderNum<=getCommodyNumber(commodyCode);
    }

    protected Long getCommodyNumber(String commodyCode){

        Long restNumber=abstractRedisUtil.getSize(CommonContants.CODES.concat(commodyCode));
        return restNumber==null?0:restNumber;

    }

    public void rush(List<CommodyInfo> commodyInfoList,String userId){

        checkAll(commodyInfoList);
        String orderNo=getOrderNo();
        try(Jedis jedis=abstractRedisUtil.getJedis(); Pipeline pipeline=jedis.pipelined();){
            AbstractRedisUtil.lock(pipeline,userId);
            AbstractRedisUtil.pipelinePop(commodyInfoList,pipeline);
        }catch (IOException e){
            return;
        }
        try {
           commodyInfoList.stream().forEach(commodyInfo -> {
               commodyInfo.setCommodys(commodyInfo.getCommodyNos().get());
               commodyInfo.setCommodyNos(null);
               if(commodyInfo.getCommodys().size()!=commodyInfo.getNumber()){
                   throw new RuntimeException("not enough");
               }
            });
            this.saveDB(commodyInfoList,orderNo,userId);
        }catch (Exception e){
            e.printStackTrace();
            sender.doSender(this.buildMessage(commodyInfoList,orderNo,"roll-back"));
            this.rollBack(commodyInfoList,orderNo,userId);
            return;
        }

    }




    private void rollBack(List<CommodyInfo> commodyInfos,String orderNo,String userId){

        try(Jedis jedis=abstractRedisUtil.getJedis(); Pipeline pipeline=jedis.pipelined();){
            abstractRedisUtil.del(pipeline,userId);
            abstractRedisUtil.sadd(pipeline,commodyInfos);
        }catch (Exception e){
            toMail();
        }
    }

    private void toMail() {
    }

    DateTimeFormatter dateTimeFormatter =DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
    private String getOrderNo(){
        Random random = new Random();
        IntStream intStream = random.ints(100000,999999);
        int randomInt = intStream.limit(1).findFirst().getAsInt();
        LocalDateTime date = LocalDateTime.now();
        String time=dateTimeFormatter.format(date);
        return time.concat(String.valueOf(randomInt));
    }

    protected void checkAll(List<CommodyInfo> commodyInfoList){
        checkOther(commodyInfoList);
        commodyInfoList.stream().forEach(commodyInfo -> {
            if(!isEnough(commodyInfo.getComodyCode(),commodyInfo.getNumber())){
                throw new RuntimeException("number is not enough");
            }
        });
        return ;
    }

    public abstract IMessage buildMessage(List<CommodyInfo> commodyInfos,String orderNo,String topic);

    public abstract void saveDB(List<CommodyInfo> commodyInfos,String orderNo,String userId);

    /**
     * 校验其他
     * @param commodyInfoList
     * @return if ok return  else throw <T extends RuntimeException>  message with error code
     */
    protected abstract void checkOther(List<CommodyInfo> commodyInfoList);
}
