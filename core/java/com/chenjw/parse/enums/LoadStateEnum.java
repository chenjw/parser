/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.chenjw.parse.enums;

/**
 * 装载状态的枚举
 * 
 * @author junwen.chenjw
 * @version $Id: LoadStateEnum.java, v 0.1 2013年8月1日 下午9:39:22 junwen.chenjw Exp $
 */
public enum LoadStateEnum {

    /** 未加载 */
    NOTLOADED,
    /**加载成功  */
    LOADING,
    /** 已加载，结果为成功 */
    SUCCESS,
    /** 已加载，结果为失败 */
    FAIL;
}
