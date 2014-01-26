/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.chenjw.parse.node.vtd;

import com.ximpleware.BookMark;
import com.ximpleware.VTDNav;

/**
 * 
 * 用于记录和还原nav的当前位置
 * 
 * @author junwen.chenjw
 * @version $Id: NavRecorder.java, v 0.1 2013年8月15日 下午11:35:17 junwen.chenjw Exp $
 */
public class NavRecorder {
    /** VTDNav */
    private VTDNav   nav;
    /** 位置标记 */
    private BookMark bookMark;
    /** 当前索引 */
    private int      currentIndex;

    /**
     * 记录当前游标位置
     * @param nav nav
     */
    public void record(VTDNav nav) {
        this.nav = nav;
        this.bookMark = new BookMark();
        bookMark.recordCursorPosition(nav);
        currentIndex = nav.getCurrentIndex();
    }

    /**
     * 从记录的游标位置恢复
     * @return nav
     */
    public VTDNav recover() {
        if (nav.getCurrentIndex() != currentIndex) {
            bookMark.setCursorPosition(nav);
        }
        return nav;
    }

}
