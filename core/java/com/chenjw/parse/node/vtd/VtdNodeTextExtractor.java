/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.chenjw.parse.node.vtd;

import java.util.HashSet;
import java.util.Set;

import com.chenjw.logger.Logger;
import com.ximpleware.NavException;
import com.ximpleware.PilotException;
import com.ximpleware.VTDNav;

/**
 * vtd的文本抽取器实现
 * @author junwen.chenjw
 * @version $Id: VtdNodeTextExtractor.java, v 0.1 2013年11月6日 上午11:56:04 junwen.chenjw Exp $
 */
public class VtdNodeTextExtractor {
    /** 日志 */
    public static final Logger LOGGER = Logger.getLogger(VtdNodeTextExtractor.class);
    /** 结束时需要添加空格的那些节点（块状节点）  */
    private static final Set<String> NEED_WHITESPACE_TAGS_SET = new HashSet<String>();
    static {
        // 对于一些块状元素，在结束时需要加上空格
        String[] needWhitespaceTags = new String[] { "center", "div", "dl", "fieldset", "form",
            "h1", "h2", "h3", "h4", "h5", "h6", "ol", "p", "pre", "table", "ul", "tr", "td", "li",
            "br" };
        for (String needWhitespaceTag : needWhitespaceTags) {
            NEED_WHITESPACE_TAGS_SET.add(needWhitespaceTag);
        }
    }

    /**
     * 判断是否需要添加空格
     * @param tagName 标签名称
     * @return 是否加空格
     */
    public static boolean isNeedWhitespaceTag(String tagName) {
        return NEED_WHITESPACE_TAGS_SET.contains(tagName);
    }

    /**
     * 从 NavRecorder 中抽取字符串
     * 
     * @param node
     * @return
     */
    public String extract(NavRecorder node) {
        VTDNav nav = node.recover();
        NodeVisitPilot ap = new NodeVisitPilot();
        ap.bind(nav);
        StringBuffer sb = new StringBuffer();
        try {
            // 遍历所有节点，然后找到文本节点输出
            while (ap.iterate2()) {
                int i = nav.getCurrentIndex();
                int type = nav.getTokenType(i);
                if (type == VTDNav.TOKEN_DOCUMENT || type == VTDNav.TOKEN_STARTING_TAG) {
                    String tagName = nav.toString(i);
                    if (isNeedWhitespaceTag(tagName) && sb.length() != 0) {
                        sb.append(" ");
                    }
                } else if (type == VTDNav.TOKEN_CHARACTER_DATA) {
                    String value = nav.toString(i);
                    sb.append(value);
                }
            }
        } catch (PilotException e) {
            LOGGER.error("extract vtd node fail : ", e);
        } catch (NavException e) {
            LOGGER.error("extract vtd node fail : ", e);
        }
        return sb.toString();

    }

}
