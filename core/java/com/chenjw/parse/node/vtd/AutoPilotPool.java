/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.chenjw.parse.node.vtd;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.chenjw.logger.Logger;
import com.ximpleware.AutoPilot;
import com.ximpleware.XPathParseException;

/**
 * AutoPilot是非线程安全的，初始化非常耗时间，并且可以重用，所以用一个pool来管理
 * 
 * 
 * @author junwen.chenjw
 * @version $Id: AutoPilotPool.java, v 0.1 2013年8月12日 下午11:24:18 junwen.chenjw Exp $
 */
public final class AutoPilotPool {
    /** 日志 */
    public static final Logger LOGGER = Logger.getLogger(AutoPilotPool.class);
    /** 每个表达式最多放的缓存数量 */
    private static final int                    MAX_NUM  = 20;

    /** 
     * 每个表达式可以映射一个PooledAutoPilot的队列，便于重用
     *  */
    private Map<String, Queue<PooledAutoPilot>> poolMap  = new ConcurrentHashMap<String, Queue<PooledAutoPilot>>();

    /** 唯一实例 */
    public static final AutoPilotPool           INSTANCE = new AutoPilotPool();

    /**
     * 读取和释放都加个锁
     * 
     * @param autoPilot autoPilot
     */
    public void release(PooledAutoPilot autoPilot) {
        Queue<PooledAutoPilot> list = poolMap.get(autoPilot.getPath());
        if (list == null) {
            list = new ConcurrentLinkedQueue<PooledAutoPilot>();
            poolMap.put(autoPilot.getPath(), list);
        }
        if (list.size() < MAX_NUM) {
            list.add(autoPilot);
        }
    }

    /**
     * 根据表达式获得PooledAutoPilot，如果池耗尽了，就新创建一个
     * @param path xpath
     * @return PooledAutoPilot实例
     */
    public PooledAutoPilot find(String path) {
        Queue<PooledAutoPilot> list = poolMap.get(path);
        if (list == null) {
            list = new ConcurrentLinkedQueue<PooledAutoPilot>();
            poolMap.put(path, list);
        }
        PooledAutoPilot r = list.poll();
        if (r == null) {
            r = new PooledAutoPilot(createAutoPilot(path), this, path);
        }
        return r;
    }

    /**
     * 创建 PooledAutoPilot
     * @param path xpath
     * @return AutoPilot实例
     */
    private AutoPilot createAutoPilot(String path) {
        AutoPilot autoPilot = new AutoPilot();
        try {
            autoPilot.selectXPath(path);
        } catch (XPathParseException e) {
            LOGGER.error( "create vtd xpath fail : ", e);
        }
        return autoPilot;
    }
}
