package com.anxin.kitchen.bean;

import com.lcodecore.ILabel;

/**
 * Created by 唐午阳 on 2018/4/16.
 */

public class LabelBean implements ILabel {
    private String id;
    private String name;

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }
}
