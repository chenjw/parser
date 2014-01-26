/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.chenjw.parse.spi.impl;

import java.util.regex.Pattern;

import com.chenjw.logger.Logger;
import com.chenjw.parse.core.Context;
import com.chenjw.parse.spi.ArgFilter;

/**
 * 
 * 基于正则的filter，注册为“regex”
 * 
 * @author junwen.chenjw
 * @version $Id: RegexFilter.java, v 0.1 2013年8月1日 下午9:46:14 junwen.chenjw Exp $
 */
public class RegexFilter extends ArgFilter {
	public static final Logger LOGGER = Logger.getLogger(RegexFilter.class);
    /** 正则表达式 */
    private Pattern pattern;

    /** 
     * @see com.chenjw.parse.spi.alipay.aggrbillinfo.core.design.parse.spi.tools.ArgFilter#setArgs(java.lang.String[])
     */
    @Override
    public void setArgs(String[] args) {
        pattern = Pattern.compile(args[0]);
    }

    /** 
     * @see com.chenjw.parse.spi.alipay.aggrbillinfo.core.design.parse.spi.tools.ArgFilter#doFilter(java.lang.String, com.alipay.aggrbillinfo.core.design.parse.core.Context, java.lang.String[])
     */
    @Override
    protected String doFilter(String input, Context context, String[] args) {
        if (input == null) {
            return null;
        }
        try {
            boolean success = pattern.matcher(input).matches();
            if (success) {
                return input;
            } else {
                return null;
            }
        } catch (Exception e) {
        	LOGGER.error("doFilter fail", e);
            return null;
        }

    }

}
