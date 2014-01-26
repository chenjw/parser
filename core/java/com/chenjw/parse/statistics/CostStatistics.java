/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.chenjw.parse.statistics;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 用来统计时间消耗和次数
 * 
 * 
 * @author junwen.chenjw
 * @version $Id: CostStatistics.java, v 0.1 2013年10月14日 下午2:06:05 junwen.chenjw Exp $
 */
public class CostStatistics {
    /**  总时间消耗*/
    private AtomicLong cost  = new AtomicLong(0);
    /** 总次数 */
    private AtomicLong times = new AtomicLong(0);

    /**
     * 
     * 平均消耗时间
     * 
     * @return 平均消耗时间
     */
    public double getAvgCost() {
        long times = this.times.get();
        if (times == 0) {
            return 0D;
        }
        return cost.get() * 1D / times;
    }

    /**
     * Getter method for property <tt>cost</tt>.
     * 
     * @return property value of cost
     */
    public AtomicLong getCost() {
        return cost;
    }

    /**
     * Setter method for property <tt>cost</tt>.
     * 
     * @param cost value to be assigned to property cost
     */
    public void setCost(AtomicLong cost) {
        this.cost = cost;
    }

    /**
     * Getter method for property <tt>times</tt>.
     * 
     * @return property value of times
     */
    public AtomicLong getTimes() {
        return times;
    }

    /**
     * Setter method for property <tt>times</tt>.
     * 
     * @param times value to be assigned to property times
     */
    public void setTimes(AtomicLong times) {
        this.times = times;
    }

}
