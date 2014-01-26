/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.chenjw.parse.node.vtd;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang.StringUtils;

import com.chenjw.logger.Logger;
import com.ximpleware.AutoPilot;
import com.ximpleware.NavException;
import com.ximpleware.VTDNav;
import com.ximpleware.XPathEvalException;

/**
 * 池化后的vtd导航器
 * @author junwen.chenjw
 * @version $Id: PooledAutoPilot.java, v 0.1 2013年11月6日 下午1:37:08 junwen.chenjw Exp $
 */
public class PooledAutoPilot {
    /** 日志 */
    public static final Logger LOGGER = Logger.getLogger(PooledAutoPilot.class);
    
    /** xpath路径 */
    private String        path;
    /** xpath的导航器 */
    private AutoPilot     target;
    /** 所归属的池 */
    private AutoPilotPool pool;

    /**
     * 构造函数
     * 
     * @param target target
     * @param pool pool
     * @param path path
     */
    public PooledAutoPilot(AutoPilot target, AutoPilotPool pool, String path) {
        this.target = target;
        this.pool = pool;
        this.path = path;
    }

    /**
     * 取得路径
     * @return path
     */
    public String getPath() {
        return path;
    }

    /**
     * 绑定某个nav
     * @param vnv 绑定到的nav
     */
    public void bind(VTDNav vnv) {
        target.bind(vnv);
    }

    /**
     * 执行xpath
     * @return 找到的位置，如果找不到返回-1
     */
    public int evalXPath() {
        try {
            return target.evalXPath();
        } catch (XPathEvalException e) {
            LOGGER.error( "eval vtd xpath fail : ", e);
            return -1;
        } catch (NavException e) {
            LOGGER.error( "eval vtd xpath fail : ", e);
            return -1;
        }
    }

    /**
     * 执行xpath
     * @return 结果
     */
    public String evalXPathToString() {
        return target.evalXPathToString();
    }

    /**
     * 释放资源用于重用
     */
    public void release() {
        target.resetXPath();
        pool.release(this);
    }
    
    public static void main(String [] args) throws IOException{
        
        String tttt="2013091300001633,中国联通;2013090700001580,壹基金;2013090700001581,杭州市电力局（市区）;2013091100001616,麦咖啡;2013101400001759,中国电信;2013102300001801,重庆移动;2013112700002210,广东移动;2013102400001832,江苏银行储蓄卡;2013102900001871,民生银行储蓄卡;2013103000001889,广发银行储蓄卡;2013102500001833,江苏银行信用卡;2013102800001854,光大银行信用卡;2013102900001872,民生银行信用卡;2013102800001851,中信银行信用卡;2013102500001837,中国国际航空;2013092700001708,银乐迪;2013102900001866,口水娃;2013102800001852,三只松鼠;2013102500001839,商业价值;2013102500001838,银泰;2013102400001827,佐丹奴;2013102300001799,美特斯邦威;2013100800001721,挂号网;2013102400001829,浙江农信丰收借记卡;2013102900001873,汉口银行储蓄卡;2013102900001874,汉口银行信用卡;2013092500001697,平安银行;2013111300001965,中石化浙江掌厅;2013112900002231,12306;2013110600001938,机票-淘宝旅行;2013110600001939,景点门票-淘宝旅行;2013111800001987,万达电影;2013110800001948,中国南方航空;2013110200001918,交通违章代办;2013110100001902,天猫超市;2013121300002344,金逸电影;2013112800002220,布丁酒店;default,其他;";
        Map<String,String> eee=new HashMap<String,String>();
        for(String sss:tttt.split(";")){
            eee.put(StringUtils.substringBefore(sss, ","), StringUtils.substringAfter(sss, ","));
        }
        
        File f1=new File("/home/chenjw/桌面/1.txt");
        Map<String,Integer> nums=new HashMap<String,Integer>();        
        File f2=new File("/home/chenjw/桌面/2.txt");
        LineIterator li=FileUtils.lineIterator(f1, "UTF-8");
        while(li.hasNext()){
            String str=li.nextLine();
            String code=StringUtils.substringBefore(str, " ");
            Integer num=Integer.parseInt(StringUtils.substringAfter(str, " "));
            nums.put(code, num);
        }
        Map<String,String> descs=new HashMap<String,String>(); 
        LineIterator li2= FileUtils.lineIterator(f2, "UTF-8");
        while(li2.hasNext()){
            String str=li2.nextLine();
            String code=StringUtils.substringBefore(str, " ");
            String desc=StringUtils.substringAfter(str, " ");
            descs.put(code, desc);
        }
        List<String[]> list=new ArrayList<String[]>();
        for(Entry<String,String> entry:descs.entrySet()){
            if(eee.containsKey(entry.getKey())){
                continue;
            }
            if(!nums.containsKey(entry.getKey())){
                continue;
            }
            String[] ooo=new String[3];
            ooo[0]=entry.getKey();
            ooo[1]=entry.getValue();
            ooo[2]=String.valueOf(nums.get(entry.getKey()));
            list.add(ooo);
        }

        Collections.sort(list, new Comparator<String[]>(){

            @Override
            public int compare(String[] o1, String[] o2) {
               return Integer.parseInt(o1[2])-Integer.parseInt(o2[2]);
            }});
        for(String[] aaa:list){
            System.out.println(aaa[0] +" "+aaa[1]+" "+aaa[2]);
        }
    }
}
