package com.anxin.kitchen.bean;


import com.bluetooth.tangwuyang.fantuanlibrary.entity.BaseEntity;

/**
 * Created by ylhu on 16-12-21.
 */
public class MenuEntity implements BaseEntity {
    private long menuId;
    private String menuTitle;
    private int menuIconRes;
    private int groupId;
    public MenuEntity(String title, int iconRes) {

        this.menuTitle = title;
        this.menuIconRes = iconRes;
    }

    public MenuEntity(String menuTitle, int menuIconRes, int groupId) {
        this.menuTitle = menuTitle;
        this.menuIconRes = menuIconRes;
        this.groupId = groupId;
    }

    public long getMenuId() {

        return menuId;
    }

    public void setMenuId(long menuId) {

        this.menuId = menuId;
    }

    public String getMenuTitle() {

        return menuTitle;
    }

    public void setMenuTitle(String menuTitle) {

        this.menuTitle = menuTitle;
    }

    public int getMenuIconRes() {

        return menuIconRes;
    }

    public void setMenuIconRes(int menuIconRes) {

        this.menuIconRes = menuIconRes;
    }

    @Override
    public String getIndexField() {

        return menuTitle;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }
}
