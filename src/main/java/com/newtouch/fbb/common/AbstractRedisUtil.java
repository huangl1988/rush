package com.newtouch.fbb.common;

import com.newtouch.fbb.mode.CommodyInfo;
import redis.clients.jedis.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by steven on 2018/1/17.
 */
public abstract class AbstractRedisUtil {

    private static JedisPool jedisPool=null;

    public abstract void init();

    public static void setJedisPool(JedisPool sJedisPool){
        jedisPool=sJedisPool;
    }

    public static void pipelinePop(List<CommodyInfo> commodyInfos,Pipeline pipeline){

        commodyInfos.stream().forEach(commodyInfo -> {
            commodyInfo.setCommodyNos(pipeline.spop(CommonContants.CODES.concat(commodyInfo.getComodyCode()),commodyInfo.getNumber()));
        });

    }

    public static List<Response<Long>> lock(Pipeline pipeline,String ...keys){
        List<Response<Long>> list = new ArrayList<>();
        for(String key:keys){
            list.add(pipeline.setnx(key,key));
        }
        return list;
    }

    public static Long getSize(String key){
        try(Jedis jedis=jedisPool.getResource();){
            return jedis.scard(key);
        }
    }

    public static Jedis getJedis(){
        return jedisPool.getResource();
    }

    public void del(Pipeline pipeline, String userId) {
        pipeline.del(userId);
    }

    public void sadd(Pipeline pipeline, List<CommodyInfo> commodyInfos) {
       commodyInfos.stream().forEach(commodyInfo -> {
           Iterator<String> it=commodyInfo.getCommodys().iterator();
           while (it.hasNext()){
               pipeline.sadd(CommonContants.CODES.concat(commodyInfo.getComodyCode()),it.next());
           }
       });
    }
}
