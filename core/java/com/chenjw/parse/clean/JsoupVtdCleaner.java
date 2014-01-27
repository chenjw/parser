/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.chenjw.parse.clean;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Entities.EscapeMode;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import com.chenjw.logger.Logger;
import com.chenjw.parse.core.DomNode;
import com.chenjw.parse.core.Cleaner;
import com.chenjw.parse.node.StatisticsNode;
import com.chenjw.parse.node.VtdNode;
import com.ximpleware.VTDGen;
import com.ximpleware.VTDNav;
import com.ximpleware.parser.XMLChar;

/**
 * 清洗采用jsoup来实现，xpath使用vtd来实现
 * 
 * @author junwen.chenjw
 * @version $Id: JsoupVtdCleaner.java, v 0.1 2013年11月19日 下午1:17:38 junwen.chenjw Exp $
 */
public class JsoupVtdCleaner implements Cleaner {
	public static final Logger LOGGER = Logger.getLogger(JsoupVtdCleaner.class);
    /** 
     * @see com.Cleaner.aggrbillinfo.core.design.parse.core.XpathEngine#clean(java.lang.String)
     */
    @Override
    public DomNode clean(String html) {
        String encoding = "UTF-8";
        Document jsoupDoc = Jsoup.parseBodyFragment(html);
        removeComments(jsoupDoc);
        jsoupDoc.outputSettings().escapeMode(EscapeMode.xhtml).outline(true).charset(encoding)
            .prettyPrint(false);

        html = jsoupDoc.html();

        VTDGen vg = new VTDGen();
        try {
            vg.setDoc(html.getBytes(encoding));
            vg.parse(false);
        } catch (Exception e) {
        	LOGGER.error( "vtd parse fail : ", e);
        }
        VTDNav nav = vg.getNav();
        return new StatisticsNode(new VtdNode(nav));
    }

    /**
     * 去掉无效字符
     * 
     * @param str 字符串
     * @return 结果
     */
    private static String removeUnvalidChar(String str) {
        if (StringUtils.isEmpty(str)) {
            return str;
        }
        char[] chars = str.toCharArray();
        int pos = 0;
        for (int i = 0; i < chars.length; i++) {
            if (XMLChar.isValidChar((int) chars[i])) {
                chars[pos++] = chars[i];
            }
        }
        return new String(chars, 0, pos);
    }

    /**
     * 删除节点中的注释
     * @param node 节点
     */
    private static void removeComments(Node node) {
        for (int i = 0; i < node.childNodes().size();) {
            Node child = node.childNode(i);
            if (child instanceof Comment || child instanceof DataNode) {
                child.remove();
            } else if (child instanceof TextNode) {
                TextNode textNode = (TextNode) child;
                String text = textNode.text();
                text = removeUnvalidChar(text);
                textNode.text(text);
                i++;
            } else if (child instanceof Element) {
                Element element = (Element) child;
                // vtd对tagname有要求，不能包含特殊字符
                element.tagName(element.tagName().replaceAll("[^a-z]", ""));
                // 出于性能考虑，只保留id和class的属性
                Attributes attrs = child.attributes();

                if (attrs != null && attrs.size() > 0) {
                    for (Attribute attr : attrs.asList()) {
                        String key = attr.getKey();
                        if (StringUtils.isEmpty(key)) {
                            attr.setKey("notsupportedkey");
                            continue;
                        }
                        if (isSupportAttr(key)) {
                            attr.setValue(removeUnvalidChar(attr.getValue()));
                            continue;
                        }
                        attrs.remove(key);
                    }
                }
                removeComments(child);
                i++;
            }
        }
    }

    /**
     * 判断是否支持该属性名
     * @param key 属性名
     * @return 是否支持
     */
    private static boolean isSupportAttr(String key) {
        return true;
    }
}
