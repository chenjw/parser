/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.chenjw.parse.node;

import com.chenjw.logger.Logger;
import com.chenjw.parse.core.DomNodeIterator;
import com.chenjw.parse.node.vtd.AutoPilotPool;
import com.chenjw.parse.node.vtd.NavRecorder;
import com.chenjw.parse.node.vtd.PooledAutoPilot;
import com.chenjw.parse.node.vtd.VtdNodeTextExtractor;
import com.ximpleware.VTDNav;

/**
 * vtd实现的节点
 * 
 * @author junwen.chenjw
 * @version $Id: VtdNode.java, v 0.1 2013年11月19日 下午1:31:34 junwen.chenjw Exp $
 */
public class VtdNode extends BaseNode {
    /** 日志 */
    public static final Logger LOGGER = Logger.getLogger(VtdNode.class);
	
    /** 用于文本抽取 */
    private static final VtdNodeTextExtractor TEXT_EXTRACTOR = new VtdNodeTextExtractor();
    /** 由于nav的游标会不断变化，所以这里要记录当前时刻在该节点上的nav状态，以便随时可以恢复 */
    private final NavRecorder                       navRecorder    = new NavRecorder();

    /** 
     * @see com.alipay.aggrbillinfo.core.design.parse.node.BaseNode#parseAttr(java.lang.String)
     */
    @Override
    protected String parseAttr(String path) {
        PooledAutoPilot ap = AutoPilotPool.INSTANCE.find(path);
        VTDNav nav = navRecorder.recover();
        ap.bind(nav);
        return ap.evalXPathToString();
    }

    /**
     * 构造函数
     * 
     * @param nav vtd的nav
     */
    public VtdNode(VTDNav nav) {
        navRecorder.record(nav);
    }

    /** 
     * @see com.alipay.aggrbillinfo.core.design.parse.core.DomNode#parseNode(java.lang.String)
     */
    @Override
    public VtdNode parseNode(String path) {
        PooledAutoPilot ap = AutoPilotPool.INSTANCE.find(path);
        try {
            VTDNav nav = navRecorder.recover();
            ap.bind(nav);
            int i = ap.evalXPath();
            if (i != -1) {
                return new VtdNode(nav);
            } else {
                return null;
            }
        } catch (Exception e) {
            LOGGER.error("vtd parse node fail : ", e);
            return null;
        } finally {
            ap.release();
        }
    }

    /** 
     * @see com.alipay.aggrbillinfo.core.design.parse.core.DomNode#parseNodeList(java.lang.String)
     */
    @Override
    public DomNodeIterator parseNodeList(String path) {
        PooledAutoPilot ap = AutoPilotPool.INSTANCE.find(path);
        try {
            VTDNav nav = navRecorder.recover();
            return new VtdNodeIterator(ap, nav);
        } catch (Exception e) {
            LOGGER.error("vtd parse nodelist fail : ", e);
            return null;
        }
    }

    /** 
     * @see com.alipay.aggrbillinfo.core.design.parse.core.DomNode#toText()
     */
    @Override
    public String toText() {
        return TEXT_EXTRACTOR.extract(navRecorder);
    }

}
