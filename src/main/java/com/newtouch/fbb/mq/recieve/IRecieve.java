package com.newtouch.fbb.mq.recieve;

import com.newtouch.fbb.mq.IMessage;

/**
 * Created by steven on 2018/1/18.
 */
public interface IRecieve {
    /**
     * 插入db
     * @param message
     */
    public void insertToDB(IMessage message);

    /**
     * 提交预zhifu
     * @param message
     */
    public void addInfoToPay(IMessage message);

    /**
     * 更新缓存信息（订单状态或删除缓存订单）
     * @param message
     */
    public void reflushCache(IMessage message);

    /**
     * 插入预超时处理队列
     * @param message
     */
    public void addInfoToDelayQueue(IMessage message);

}
