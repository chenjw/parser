/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.chenjw.parse.statistics;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 用来统计每句xpath语句的执行时间成本
 * 
 * @author junwen.chenjw
 * @version $Id: TimeStatistics.java, v 0.1 2013年10月14日 下午2:07:56 junwen.chenjw Exp $
 */
public class TimeStatistics {

    /** 实现消耗，key为xpath语句，value为消耗统计  */
    public Map<String, CostStatistics> costs = new ConcurrentHashMap<String, CostStatistics>();

    /**
     * 添加消耗统计
     * @param key key
     * @param time 消耗时间
     */
    public void addCost(String key, long time) {
        CostStatistics cost = costs.get(key);
        if (cost == null) {
            cost = new CostStatistics();
            cost.setCost(new AtomicLong(0));
            cost.setTimes(new AtomicLong(0));
            costs.put(key, cost);
        }
        cost.getCost().addAndGet(time);
        cost.getTimes().incrementAndGet();
    }

    /**
     * 根据消耗排序
     * @return 排序结果
     */
    @SuppressWarnings("unchecked")
    public String[] sortByCost() {
        Object[] objs = costs.entrySet().toArray();
        Arrays.sort(objs, new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                Entry<String, CostStatistics> entry1 = (Entry<String, CostStatistics>) o1;
                Entry<String, CostStatistics> entry2 = (Entry<String, CostStatistics>) o2;
                return (int) (entry2.getValue().getAvgCost() - entry1.getValue().getAvgCost());
            }
        });
        String[] result = new String[objs.length];
        for (int i = 0; i < objs.length; i++) {
            Entry<String, CostStatistics> entry = (Entry<String, CostStatistics>) objs[i];
            result[i] = entry.getValue().getAvgCost() + " " + entry.getKey();
        }
        return result;
    }

}
