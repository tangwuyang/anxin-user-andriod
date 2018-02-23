package com.anxin.kitchen.adapter;

import android.view.View;

import com.anxin.kitchen.view.HorizontalScrollMenu;

import java.util.List;

/**
 * Created by 唐午阳 on 2018/2/21.
 */

public abstract class MyBaseAdapter {
    private HorizontalScrollMenu mHorizontalScrollMenu;

    public abstract List<String> getMenuItems();

    public abstract List<View> getContentViews();

    public abstract void onPageChanged(int position,boolean visitStatus);

    public void setHorizontalScrollMenu(HorizontalScrollMenu horizontalScrollMenu)
    {
        mHorizontalScrollMenu=horizontalScrollMenu;
    }

    public void notifyDataSetChanged()
    {
        mHorizontalScrollMenu.notifyDataSetChanged(this);
    }
}
