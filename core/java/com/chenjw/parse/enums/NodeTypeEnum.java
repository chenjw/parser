/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.chenjw.parse.enums;

/**
 * 节点类型的枚举
 * 
 * @author junwen.chenjw
 * @version $Id: TypeEnum.java, v 0.1 2013年8月1日 下午9:39:53 junwen.chenjw Exp $
 */
public enum NodeTypeEnum {

    /** 数据节点解析结果为字符串 */
    DATA("data"),
    /** 组节点 */
    GROUP("group"),
    /** 列表节点，解析结果为一个ArrayList */
    LIST("list"),
    /** html节点，解析结果为一个节点组 */
    HTML("html");

    /** 和解析脚本中的type对应 */
    private String type;

    /**
     * @param type
     */
    private NodeTypeEnum(String type) {
        this.type = type;
    }

    /**
     * 获得类型
     * @return 类型
     */
    public String getType() {
        return type;
    }

    /**
     * 从类型获得枚举
     * @param type 类型
     * @return 枚举
     */
    public static NodeTypeEnum getEnum(String type) {
        for (NodeTypeEnum item : values()) {
            if (item.getType().equals(type)) {
                return item;
            }
        }
        return null;
    }
}
