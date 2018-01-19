package com.newtouch.fbb.common;

import redis.clients.jedis.*;

import java.io.IOException;

/**
 * Created by steven on 2018/1/17.
 */
public abstract class AbstractRedisUtil {

    private static JedisPool jedisPool=null;

    public abstract void init();

    public static void setJedisPool(JedisPool sJedisPool){
        jedisPool=sJedisPool;
    }

    public  boolean atomicSub(String key,Long subStep){
        boolean flag = true;
        Jedis jedis = jedisPool.getResource();
        Pipeline pipeline = jedis.pipelined();
       try{
           Response<Long> response=pipeline.decrBy(key,subStep);
           try {
               pipeline.close();
           } catch (IOException e) {
               e.printStackTrace();
           }
           if(response.get()<0){
               pipeline = jedis.pipelined();
               pipeline.incrBy(key,subStep);
               flag=false;
           }

       }finally {

           jedis.close();
       }
       return flag;
    }

    public String getInfo(String key){
        Jedis jedis = jedisPool.getResource();
        Pipeline pipeline = jedis.pipelined();
        try {
            Response<String> response=pipeline.get(key);
            try {
                pipeline.close();
            } catch (IOException e) {

            }
            return response.get();
        }finally {
            jedis.close();
        }

    }





    public  void setInfo(String key,int second,String value){
        Jedis jedis = jedisPool.getResource();
        Pipeline pipeline = jedis.pipelined();
        try {
            pipeline.setex(key,second,value);
        }finally {
            try {
                pipeline.close();
            } catch (IOException e) {

            }
            jedis.close();
        }
    }

    public  void setInfoWithNo(String key,String value){
        Jedis jedis = jedisPool.getResource();
        Pipeline pipeline = jedis.pipelined();
        try {
            pipeline.setnx(key,value);
        }finally {
            try {
                pipeline.close();
            } catch (IOException e) {

            }
            jedis.close();
        }
    }


    public  void removeInfo(String key){
        Jedis jedis = jedisPool.getResource();
        Pipeline pipeline = jedis.pipelined();
        try {
            pipeline.del(key);
        }finally {
            try {
                pipeline.close();
            } catch (IOException e) {

            }
            jedis.close();
        }
    }

    public  void listInfo(String key,String ...comodyInfos){
        Jedis jedis = jedisPool.getResource();
        Pipeline pipeline = jedis.pipelined();
        try {
            pipeline.lpush(key,comodyInfos);
        }finally {
            try {
                pipeline.close();
            } catch (IOException e) {

            }
            jedis.close();
        }
    }



}
