package com.newtouch.fbb.common.demo;

import com.newtouch.fbb.common.AbstractRedisUtil;
import redis.clients.jedis.JedisPool;

/**
 * Created by steven on 2018/1/19.
 */
public class DemoRedisUtil extends AbstractRedisUtil{
    private static boolean  initFlag = false;
    public  void init(){
        synchronized (DemoRedisUtil.class){
            if(initFlag)
                return;
            JedisPool jedisPool = new JedisPool("192.168.253.1",6379);
            DemoRedisUtil.setJedisPool(jedisPool);
            initFlag=true;

        }

    }

}
