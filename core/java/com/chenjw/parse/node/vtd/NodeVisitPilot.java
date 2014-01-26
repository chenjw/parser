/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.chenjw.parse.node.vtd;

import com.ximpleware.AutoPilot;
import com.ximpleware.VTDNav;

/**
 * 扩展vtd的AutoPilot
 * 
 * @author junwen.chenjw
 * @version $Id: NodeVisitPilot.java, v 0.1 2013年11月6日 下午1:36:30 junwen.chenjw Exp $
 */
public class NodeVisitPilot extends AutoPilot {

    /** 
     * @see com.ximpleware.AutoPilot#bind(com.ximpleware.VTDNav)
     */
    @Override
    public void bind(VTDNav vnv) {
        super.bind(vnv);
        this.selectNode();
    }

}
