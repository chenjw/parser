/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.chenjw.parse;

import com.chenjw.parse.clean.JsoupVtdCleaner;

/**
 * һЩ����������ȫ������
 * 
 * 
 * @author junwen.chenjw
 * @version $Id: ParseConstants.java, v 0.1 2013��8��16�� ����10:33:01 junwen.chenjw Exp $
 */
public class ParseConfig {
    /** �Ƿ����xpath·���Ż� */
    public static boolean  isOptimizeXpath      = true;
    /**xpath���� */
    public static Class<?> cleanerClass = JsoupVtdCleaner.class;
}
