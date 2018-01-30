package com.newtouch.fbb.service;

import com.newtouch.fbb.common.AbstractRedisUtil;
import com.newtouch.fbb.common.CommonContants;
import com.newtouch.fbb.mode.CommodyInfo;
import com.newtouch.fbb.mq.IMessage;
import com.newtouch.fbb.mq.sender.IMqSender;

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

        String redisInfo=abstractRedisUtil.getInfo(commodyCode.concat(CommonContants.COMMODY_NUMBER));
        String number =(redisInfo==null?"0":redisInfo);
        return  Long.parseLong(number);
    }

    public void rush(List<CommodyInfo> commodyInfoList){

        checkAll(commodyInfoList);
        String orderNo=getOrderNo();

        sender.doSender(buildMessage(commodyInfoList,orderNo));
        try{
            commodyInfoList.stream().forEach(commodyInfo -> {
                singleDone(commodyInfo,orderNo);
            });
        }catch (Throwable e){
            //rollBack(commodyInfoList,orderNo);
            e.printStackTrace();
        }

    }

    protected void singleDone(CommodyInfo commodyInfo,String orderNo){
        if(abstractRedisUtil.atomicSub(commodyInfo.getComodyCode().concat(CommonContants.COMMODY_NUMBER),commodyInfo.getNumber())){
            abstractRedisUtil.listInfo(orderNo.concat(CommonContants.COMMODY_CODES), commodyInfo.getComodyCode());
            abstractRedisUtil.setInfo(orderNo.concat(CommonContants.COMMODY_CODES).concat(commodyInfo.getComodyCode()).concat(CommonContants.COMMODY_NUMBER),CommonContants.COMMODY_WAITING_TIME,String.valueOf(commodyInfo.getNumber()));
            return;
        }
        throw new RuntimeException("over");
    }

    @Deprecated
    public void init(CommodyInfo commodyInfo){
        abstractRedisUtil.setInfoWithNo(commodyInfo.getComodyCode().concat(CommonContants.COMMODY_NUMBER),"10");
    }

    private void rollBack(List<CommodyInfo> commodyInfos,String orderNo){
        Optional.ofNullable(abstractRedisUtil.getListInfo(orderNo.concat(CommonContants.COMMODY_CODES))).ifPresent(list->{
            list.forEach(code->{
                try{
                    CommodyInfo currentCommodyInfo=commodyInfos.stream().filter(new Predicate<CommodyInfo>(){
                        @Override
                        public boolean test(CommodyInfo commodyInfo) {
                            return code.equals(commodyInfo.getComodyCode());
                        }
                    }).collect(Collectors.toList()).get(0);
                    abstractRedisUtil.atomicAdd(currentCommodyInfo.getComodyCode().concat(CommonContants.COMMODY_NUMBER),currentCommodyInfo.getNumber());
                    abstractRedisUtil.delKey(orderNo.concat(CommonContants.COMMODY_CODES));
                }catch(Exception e){

                }
            });
        });

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

    public abstract IMessage buildMessage(List<CommodyInfo> commodyInfos,String orderNo);


    /**
     * 校验其他
     * @param commodyInfoList
     * @return if ok return  else throw <T extends RuntimeException>  message with error code
     */
    protected abstract void checkOther(List<CommodyInfo> commodyInfoList);
}
