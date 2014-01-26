/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.chenjw.parse.utils;

import org.apache.commons.lang.StringUtils;

import com.chenjw.logger.Logger;
import com.chenjw.parse.config.ItemConfig;
import com.chenjw.parse.spi.Filter;
import com.chenjw.parse.spi.impl.FailoverFilter;
import com.chenjw.parse.value.StringValue;

/**
 * 解析时依赖的一些工具类
 * 
 * @author junwen.chenjw
 * @version $Id: ParseUtils.java, v 0.1 2013年8月1日 下午9:48:20 junwen.chenjw Exp $
 */
public class ParseUtils {
	public static final Logger LOGGER = Logger.getLogger(ParseUtils.class);
    /** 用来标记返回值为break的字符标记，如果遇到了break标记，就不会执行下面的filter */
    public static final String  BREAK_MARK = "[BREAK_MARK]defined_by_chenjw";


    /**
     * 
     * 过滤字符串
     * 
     * @param text 文本
     * @param filters 过滤器
     * @param context 上下文
     * @return 结果
     */
    private static String doFilter(String text, Filter[] filters, StringValue context) {

        if (filters != null) {
            String bak = text;
            for (Filter trans : filters) {
                // 针对failover做特殊处理
                if (trans instanceof FailoverFilter) {
                    if (StringUtils.isBlank(text)) {
                        text = bak;
                        continue;
                    } else {
                        break;
                    }
                }
                text = trans.doFilter(text, context);
                if (BREAK_MARK.equals(text)) {
                    text = null;
                    break;
                }
            }
        }
        return text;
    }

    /**
     * 过滤字符串
     * @param text 文本
     * @param config 配置
     * @param context 上下文
     * @return 结果
     */
    public static String doFilter(String text, ItemConfig config, StringValue context) {
        try {
            Filter[] filters = config.getFilters();
            return doFilter(text, filters, context);
        } catch (Exception e) {
        	LOGGER.error("执行filter失败！", e);
            return null;
        }
    }

    /**
     * 添加深度
     * 
     * @param depth 深度
     * @param str 字符串
     * @return 结果
     */
    public static String addDepth(int depth, String str) {
        if (str == null) {
            return null;
        }
        String[] ss = str.split("\n");
        for (int i = 0; i < ss.length; i++) {
            StringBuffer sb = new StringBuffer();
            for (int ii = 0; ii < depth; ii++) {
                sb.append("--");
            }
            sb.append(ss[i]);
            ss[i] = sb.toString();
        }
        return StringUtils.join(ss, "\n");
    }

    

}
