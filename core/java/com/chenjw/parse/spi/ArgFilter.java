/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.chenjw.parse.spi;

import com.chenjw.logger.Logger;
import com.chenjw.parse.core.Context;

/**
 * 带参数的filter
 * 
 * @author junwen.chenjw
 * @version $Id: ArgFilter.java, v 0.1 2013年8月1日 下午9:45:18 junwen.chenjw Exp $
 */
public abstract class ArgFilter implements Filter, Cloneable {
	public static final Logger LOGGER = Logger.getLogger(ArgFilter.class);
	/** 参数 */
	private String[] args;

	/**
	 * Setter method for property <tt>args</tt>.
	 * 
	 * @param args
	 *            value to be assigned to property args
	 */
	public void setArgs(String[] args) {
		this.args = args;
	}

	/**
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			LOGGER.error("clone fail", e);
			return null;
		}
	}

	/**
	 * @see com.chenjw.parse.spi.alipay.aggrbillinfo.core.design.parse.spi.tools.Filter#doFilter(java.lang.String,
	 *      com.alipay.aggrbillinfo.core.design.parse.core.Context)
	 */
	@Override
	public String doFilter(String input, Context context) {
		return doFilter(input, context, args);

	}

	/**
	 * 子类需要实现过滤器实现逻辑
	 * 
	 * @param input
	 *            输入
	 * @param context
	 *            上下文
	 * @param args
	 *            参数
	 * @return 结果
	 */
	protected abstract String doFilter(String input, Context context,
			String[] args);

}
