/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.chenjw.parse.node;

import com.chenjw.parse.core.DomNode;
import com.chenjw.parse.core.DomNodeIterator;
import com.chenjw.parse.node.vtd.NavRecorder;
import com.chenjw.parse.node.vtd.PooledAutoPilot;
import com.ximpleware.VTDNav;

/**
 * 基于vtd实现的html标签节点迭代器
 * 
 * @author junwen.chenjw
 * @version $Id: VtdNodeIterator.java, v 0.1 2013年11月19日 下午1:32:05 junwen.chenjw Exp $
 */
public class VtdNodeIterator implements DomNodeIterator {

    /** 池化的节点遍历器  */
    private final PooledAutoPilot ap;
    /** 记录了节点遍历过程中的游标状态，遍历到哪个节点了，记录的游标就指向哪个节点 */
    private final NavRecorder     currentNavRecorder = new NavRecorder();
    /** 判断下一个节点是否已经初始化  */
    private boolean               nextInited         = false;
    /** 是否还存在下一个节点 */
    private boolean               hasNext            = false;

    /**
     * 构造函数
     * 
     * @param ap ap
     * @param nav nav
     */
    public VtdNodeIterator(PooledAutoPilot ap, VTDNav nav) {
        this.ap = ap;
        currentNavRecorder.record(nav);
        ap.bind(nav);
    }

    /** 
     * @see java.util.Iterator#hasNext()
     */
    @Override
    public boolean hasNext() {
        if (!nextInited) {
            // 在执行xpath前状态可能已在外部被改变，要恢复成记录时的当前状态
            VTDNav nav = currentNavRecorder.recover();
            // 执行xpath
            hasNext = (ap.evalXPath() != -1);
            // nav在执行过xpath后状态会变化，所以要重新记录下当前状态
            currentNavRecorder.record(nav);
            nextInited = true;
        }
        return hasNext;
    }

    /** 
     * @see java.util.Iterator#next()
     */
    @Override
    public DomNode next() {
        if (hasNext()) {
            nextInited = false;
            return new VtdNode(currentNavRecorder.recover());
        } else {
            return null;
        }
    }


    /** 
     * @see com.alipay.aggrbillinfo.core.design.parse.core.DomNodeIterator#close()
     */
    @Override
    public void close() {
        ap.release();
    }

}
