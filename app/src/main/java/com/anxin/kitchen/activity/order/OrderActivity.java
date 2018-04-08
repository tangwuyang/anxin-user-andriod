package com.anxin.kitchen.activity.order;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.anxin.kitchen.activity.BaseFragmentActivity;
import com.anxin.kitchen.adapter.OrderTabAdapter;
import com.anxin.kitchen.fragment.orderfragment.OrderFragment;
import com.anxin.kitchen.user.R;
import com.anxin.kitchen.view.PagerSlidingTab;

/**
 * 订单页面
 * Created by xujianjun on 2018/4/5.
 */

public class OrderActivity extends BaseFragmentActivity {

    private ImageView ivBack;
    private TextView tvTitle;


    private PagerSlidingTab porderSlidingTab;
    private ViewPager vpOrder;
    private OrderTabAdapter mTabAdapter;
    private Fragment[] mFragments;
    private String[] mTab;
    private int chooseType =0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        initView();
        initData();
    }


    private void initView() {
        ivBack = findViewById(R.id.back_img);
        tvTitle = findViewById(R.id.title_tv);
        porderSlidingTab = (PagerSlidingTab) findViewById(R.id.porderSlidingTab);
        vpOrder = (ViewPager) findViewById(R.id.vp_order);

        ivBack.setOnClickListener(this);
        tvTitle.setText("我的订单");
        chooseType =getIntent().getIntExtra("type",0);
    }

    private void initData() {
        mTab = new String[]{"全部", "待付款", "已付款", "已完成", "已取消",};
        Bundle bundle0 = new Bundle();
        bundle0.putInt("type", -1);
        Bundle bundle1 = new Bundle();
        bundle1.putInt("type", 0);
        Bundle bundle2 = new Bundle();
        bundle2.putInt("type", 1);
        Bundle bundle3 = new Bundle();
        bundle3.putInt("type", 3);
        Bundle bundle4 = new Bundle();
        bundle4.putInt("type", 4);

        OrderFragment fragment0 = new OrderFragment();
        fragment0.setArguments(bundle0);
        OrderFragment fragment1 = new OrderFragment();
        fragment1.setArguments(bundle1);
        OrderFragment fragment2 = new OrderFragment();
        fragment2.setArguments(bundle2);
        OrderFragment fragment3 = new OrderFragment();
        fragment3.setArguments(bundle3);
        OrderFragment fragment4 = new OrderFragment();
        fragment4.setArguments(bundle4);

        mFragments = new Fragment[]{fragment0, fragment1, fragment2, fragment3, fragment4};

        mTabAdapter = new OrderTabAdapter(getSupportFragmentManager(), mTab, mFragments);
        vpOrder.setAdapter(mTabAdapter);
        porderSlidingTab.setViewPager(vpOrder);//绑定pagerSlidingTab和ViewPager

        vpOrder.setCurrentItem(chooseType);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_img:
                finish();
                break;
            default:
                break;
        }
    }
}
