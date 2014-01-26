/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.chenjw.parse.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import com.chenjw.parse.enums.NodeTypeEnum;
import com.chenjw.parse.spi.ArgFilter;
import com.chenjw.parse.spi.Filter;
import com.chenjw.parse.spi.Selectors;
import com.chenjw.parse.spi.Tool;
import com.chenjw.parse.spi.impl.SimpleSelectors;

/**
 * 用于映射解析模板的配置信息到配置模型
 * 
 * @author junwen.chenjw
 * @version $Id: ConfigMapper.java, v 0.1 2013年8月1日 下午9:40:51 junwen.chenjw Exp $
 */
public class ConfigMapper {

    /**
     * 根据filter定义获得filter名字
     * @param desc 描述
     * @return 方法名
     */
    private static String parseName(String desc) {
        return StringUtils.substringBefore(desc, "(");
    }

    /**
     * 解析filter定义中的入参
     * @param desc 描述
     * @return 入参
     */
    private static String[] parseArgs(String desc) {
        String argStr = StringUtils.substringAfter(desc, "(");
        if (StringUtils.isBlank(argStr)) {
            return null;
        }
        argStr = argStr.substring(1, argStr.length() - 2);
        return StringUtils.splitByWholeSeparator(argStr, "','");
    }

    /**
     * 根据定义生成filter
     * @param desc 描述
     * @param tools 工具
     * @return 过滤器
     */
    private static Filter findFilterByDesc(String desc, Map<String, Tool> tools) {
        if (desc == null || desc.length() == 0) {
            return null;
        }
        String name = parseName(desc);
        Filter t = (Filter) tools.get(name);
        if (t != null) {
            if (t instanceof ArgFilter) {
                String[] args = parseArgs(desc);
                ArgFilter af = (ArgFilter) t;
                af = (ArgFilter) af.clone();
                af.setArgs(args);
                t = af;
            }
            return t;
        } else {
            return null;
        }
    }

    /**
     * 解析filters
     * @param key key
     * @param config 配置
     * @param tools 工具
     * @return 过滤器
     */
    @SuppressWarnings("rawtypes")
    public static Filter[] findFilters(String key, Map<String, Object> config,
                                       Map<String, Tool> tools) {

        Object obj = config.get(key);
        if (obj == null) {
            return null;
        }
        if (obj instanceof Filter) {
            return new Filter[] { (Filter) obj };
        } else if (obj instanceof List) {
            List<Filter> trans = new ArrayList<Filter>();
            for (Object o : (List) obj) {
                if (o instanceof Filter) {
                    trans.add((Filter) o);
                } else if (o instanceof String) {
                    Filter t = findFilterByDesc((String) o, tools);
                    if (t != null) {
                        trans.add(t);
                    }
                }

            }
            return trans.toArray(new Filter[trans.size()]);
        } else if (obj instanceof String) {
            Filter t = findFilterByDesc((String) obj, tools);
            if (t != null) {
                return new Filter[] { t };
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * 解析selectors
     * @param key key
     * @param config 配置
     * @return 选择器
     */
    @SuppressWarnings({ "unchecked" })
    public static Selectors findSelector(String key, Map<String, Object> config) {
        Object obj = config.get(key);
        if (obj == null) {
            return null;
        }
        if (obj instanceof List) {
            return new SimpleSelectors((List<Object>) obj);

        } else if (obj instanceof String) {
            return new SimpleSelectors((String) obj);
        } else if (obj instanceof Selectors) {
            return (Selectors) obj;
        } else {
            return null;
        }
    }



    /**
     * 解析模板中单独配置的IgnoreCheckers
     * @param key key
     * @param config 配置
     * @return 忽略器ID
     */
    @SuppressWarnings({ "unchecked" })
    public static String[] findIgnoreCheckersFromConfig(String key, Map<String, Object> config) {
        Object obj = config.get(key);
        if (obj == null) {
            return null;
        }
        if (obj instanceof List) {
            List<String> trans = (List<String>) obj;
            return trans.toArray(new String[trans.size()]);
        } else if (obj instanceof String) {
            return new String[] { (String) obj };
        } else {
            return null;
        }
    }

    /**
     * 映射模板中的字段配置到ItemConfig
     * @param templateConfig 模板配置
     * @param rule 属性配置
     * @param tools 工具
     * @return 属性配置
     */
    public static Map<String, ItemConfig> mapItemConfig(Map<String, Object> rule,
                                                        Map<String, Tool> tools) {
        if (tools == null) {
            tools = new HashMap<String, Tool>();
        }
        Map<String, ItemConfig> result = new HashMap<String, ItemConfig>();
        mapConfigNode(result, rule, tools);
        return result;
    }

    /**
     * 映射模板中某个字段定义的属性
     * @param templateConfig 模板配置
     * @param config 配置
     * @param key key
     * @param value value
     * @param tools 工具
     */
    @SuppressWarnings("unchecked")
    private static void mapKeyAttr(ItemConfig config, String key, Map<String, Object> value,
                                   Map<String, Tool> tools) {
        config.setKey(key);
        if (value.get("required") != null) {
            config.setRequired((Boolean) value.get("required"));
        }
        if (value.get("type") == null) {
            config.setType(NodeTypeEnum.DATA);
        } else {
            config.setType(NodeTypeEnum.getEnum((String) value.get("type")));
        }
        config.setParent((String) (value.get("parent")));
        // 使用优化器优化重复路径，这里也可以选择不优化，直接用findSelector的结果
        config.setSelectors(findSelector("selector", value));
        if (value.get("multiple") != null) {
            config.setMultiple((Boolean) value.get("multiple"));
        }
        if (value.get("join") != null) {
            config.setJoin((Boolean) value.get("join"));
        }

        config.setFilters(findFilters("filter", value, tools));
        // if this node type is multiple

        // items
        Map<String, Object> itemsMap = (Map<String, Object>) (value.get("items"));
        if (itemsMap != null) {
            Map<String, ItemConfig> items = new HashMap<String, ItemConfig>();
            mapConfigNode(items, itemsMap, tools);
            config.setItems(items);
        }
        // shared items
        Map<String, Object> sharedItemsMap = (Map<String, Object>) (value.get("shared-items"));
        if (sharedItemsMap != null) {
            Map<String, ItemConfig> sharedItems = new HashMap<String, ItemConfig>();
            mapConfigNode(sharedItems, sharedItemsMap, tools);
            config.setSharedItems(sharedItems);
        }

    }

    /**
     * 映射字段节点
     * @param templateConfig 模板配置
     * @param res  属性配置
     * @param rule 属性配置
     * @param tools 工具
     */
    @SuppressWarnings("unchecked")
    private static void mapConfigNode(Map<String, ItemConfig> res, Map<String, Object> rule,
                                      Map<String, Tool> tools) {
        if (rule == null) {
            return;
        }
        // 没有parent的部分
        Map<String, ItemConfig> noParentConfigs = new HashMap<String, ItemConfig>();
        // 有parent的部分
        Map<String, Map<String, ItemConfig>> configsByParent = new HashMap<String, Map<String, ItemConfig>>();

        // /////
        for (Object s : rule.entrySet()) {
            Entry<String, Map<String, Object>> e = (Entry<String, Map<String, Object>>) s;
            String key = e.getKey();
            ItemConfig config = new ItemConfig();
            // 匹配属性
            mapKeyAttr(config, key, e.getValue(), tools);
            // 按照parent分类
            String parent = config.getParent();
            // 没parent的
            if (parent == null) {
                noParentConfigs.put(key, config);
            }
            // 有parent的
            else {
                Map<String, ItemConfig> parentConfigs = configsByParent.get(parent);
                if (parentConfigs == null) {
                    parentConfigs = new HashMap<String, ItemConfig>();
                    configsByParent.put(parent, parentConfigs);
                }
                parentConfigs.put(key, config);
            }
        }
        for (Entry<String, ItemConfig> entry : noParentConfigs.entrySet()) {
            String key = entry.getKey();
            ItemConfig config = entry.getValue();
            fillByParent(config, configsByParent);
            res.put(key, config);
        }

    }

    /**
     * 递归的方式填充
     * 
     * @param config 配置
     * @param configsByParent 父节点配置
     */
    private static void fillByParent(ItemConfig config,
                                     Map<String, Map<String, ItemConfig>> configsByParent) {
        Map<String, ItemConfig> groupKeys = config.getGroupKeys();

        Map<String, ItemConfig> configs = configsByParent.get(config.getKey());
        if (groupKeys == null) {
            groupKeys = configs;
            config.setGroupKeys(groupKeys);
        } else {
            if (configs != null) {
                groupKeys.putAll(configs);
            }
        }
        if (groupKeys != null) {
            for (Entry<String, ItemConfig> entry : groupKeys.entrySet()) {
                fillByParent(entry.getValue(), configsByParent);
            }
        }
    }
}
