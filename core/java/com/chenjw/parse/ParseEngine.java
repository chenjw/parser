/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.chenjw.parse;

import java.util.HashMap;
import java.util.Map;

import com.chenjw.logger.Logger;
import com.chenjw.parse.clean.CleanerFactory;
import com.chenjw.parse.config.ConfigMapper;
import com.chenjw.parse.config.ItemConfig;
import com.chenjw.parse.core.DomNode;
import com.chenjw.parse.spi.Tool;
import com.chenjw.parse.utils.GlobalTools;
import com.chenjw.parse.value.FrameValue;
import com.chenjw.parse.value.ValueFactory;

/**
 * 
 * 解析引擎，线程安全，初始化方法比较耗时，建议一个解析模板一个ParseEngine实例
 * 
 * @author chenjw
 * @version $Id: ParseEngine.java, v 0.1 2014年1月25日 下午3:33:43 chenjw Exp $
 */
public class ParseEngine {
    /** 日志 */
    public static final Logger LOGGER = Logger.getLogger(ParseEngine.class);


         /** 模板中的字段配置 */
    private Map<String, ItemConfig> rule;
    
    public ParseEngine(Map<String, Object> ruleConfig,Map<String, Tool> customTools){
        Map<String, Tool> tools = new HashMap<String, Tool>();
        Map<String, Tool> globalTools=GlobalTools.getGlobalTools();
        // 设置全局工具
        if(globalTools!=null){
            tools.putAll(globalTools);
        }
        // 设置脚本自定义的工具
        if (customTools != null) {
            tools.putAll(customTools);
        }
        // 初始化脚本规则
        rule = ConfigMapper.mapItemConfig(ruleConfig, tools);
    }
    
    /**
     * 
     * 解析dom树
     * 
     * @param root
     * @return
     */
    public Map<String, Object> parse(DomNode root) {
        try {
            // 创建顶层frame
            FrameValue frameValue = ValueFactory.createFrameValue(rule);
            boolean loadSuccess = frameValue.load(root);
           
            if (loadSuccess) {
                Map<String, Object> results = new HashMap<String, Object>();
                frameValue.writeResult(results);
                return results;
            } else {
               return null;
            }
        } catch (Exception e) {
            // 不抛出异常，理论上不会有解析失败，只有解析不出来
            LOGGER.error( "parse fail", e);
            return null;
        } 
    }
    
    
    /**
     * 清洗html并生成dom数
     * 
     * @param html
     * @return
     */
    public Map<String, Object> parse(String html) {
        try {
            // node是不可变的，所以这里可以重用session中的node
            DomNode root  = CleanerFactory.getXpathEngine().clean(html);
            return parse(root);
        } catch (Exception e) {
            // 不抛出异常，理论上不会有解析失败，只有解析不出来
            LOGGER.error( "clean fail", e);
            return null;
        }
    }
}
