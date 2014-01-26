/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.chenjw.parse.utils;

import java.util.HashMap;
import java.util.Map;

import com.chenjw.parse.spi.Tool;

/**
 * 运行时的工具类库保存在这个类的静态变量中
 * 
 * @author junwen.chenjw
 * @version $Id: GlobalTools.java, v 0.1 2013年11月19日 下午1:44:35 junwen.chenjw Exp $
 */
public class GlobalTools {


    /** 全局工具 */
    private static Map<String, Tool>         globalTools          = new HashMap<String, Tool>();

    /**
     * 设置全局工具
     * 
     * @param globalTools
     */
    public static void setGlobalTools(Map<String, Tool> globalTools){
        GlobalTools.globalTools=globalTools;
    }
    
    /**
     * 获得全局工具
     * 
     * @return 全局工具
     */
    public static Map<String, Tool>  getGlobalTools(){
       return globalTools;
    }

}
