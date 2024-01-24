package com.belly.model;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.json.JSONUtil;

/**
 * @author Belly
 * @version 1.1.0
 *
 * 输出Meta对象类
 */
public class MetaManager {
    /**
     * volatile:
     * 1.线程的可见性：当一个线程去修改一个共享变量时，另一个可用读取这个修改的值
     * 2.顺序的一致性：禁止指令重排
     */
    private static volatile Meta meta = null;

    private MetaManager() {
    }

    public static Meta getMetaObject(){
        //双重校验
        if (null == meta){
            synchronized (MetaManager.class){
                if (null == meta){
                    meta = initMeta();
                }
            }
        }
        return meta;
    }

    private static Meta initMeta(){
        String metaJson = ResourceUtil.readUtf8Str("meta.json");
        Meta metaBean = JSONUtil.toBean(metaJson, Meta.class);

        //校验Meta对象 todo

        return metaBean;
    }
}
