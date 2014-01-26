/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.chenjw.parse.clean;

import com.chenjw.parse.ParseConfig;
import com.chenjw.parse.core.Cleaner;

/**
 * 清洗工厂类
 * 
 * @author junwen.chenjw
 * @version $Id: XpathEngineFactory.java, v 0.1 2013年11月6日 上午11:09:02 junwen.chenjw Exp $
 */
public class CleanerFactory  {

    /**
     * 取得XpathEngine
     * 
     * @return XpathEngine
     */
    public static Cleaner getXpathEngine() {
        return LazyInstance.INSTANCE;
    }
    
    // 使用静态内部类实现延迟加载   
    private static class LazyInstance {   
       private static final Cleaner INSTANCE ;   
       static{
           
           // 初始化XpathEngine
           Class<?> engineClass = ParseConfig.cleanerClass;
           try {
               INSTANCE= (Cleaner) engineClass.newInstance();
           } catch (Exception e) {
               throw new IllegalStateException("XpathEngine init fail",e);
           } 
       } 
    }
}
