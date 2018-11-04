package com.kevin.mario.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * describe  : 配置类
 *
 * @author : kevin
 * @date : 2018/11/4 13:20
 **/
public class ConfigLoader {

    private Map<String, Object> configMap;

    public ConfigLoader() {
        this.configMap = new HashMap<>();
    }

    /**
     * 加载配置
     *
     * @param conf 配置文件路径
     */
    public void load(String conf) {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(new File(conf)));
            toMap(properties);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void toMap(Properties properties) {
        if (null != properties) {
            Set<Object> keys = properties.keySet();
            for (Object key : keys) {
                Object value = properties.get(key);
                configMap.put(key.toString(), value);
            }
        }
    }

    public void setConf(String name, String value) {
        configMap.put(name, value);
    }

    public String getConf(String name) {
        Object value = configMap.get(name);
        if (null != value) {
            return value.toString();
        }
        return null;
    }

    public Object getConig(String name) {
        return configMap.get(name);
    }
}
