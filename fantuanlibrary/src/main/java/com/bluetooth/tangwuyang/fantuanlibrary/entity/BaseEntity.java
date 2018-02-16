package com.bluetooth.tangwuyang.fantuanlibrary.entity;

/**
 * 要展示的数据实体基类
 * @author: tangwuyang on 2018/2/12
 * @site: http://ittiger.cn
 */
public interface BaseEntity {
    /**
     * 要索引的字段数据信息，例如联系人中对姓名进行索引，则此处返回姓名字段值
     * @return
     */
    String getIndexField();
}
