package com.anxin.kitchen.activity.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anxin.kitchen.activity.BaseFragmentActivity;
import com.anxin.kitchen.activity.MainActivity;
import com.anxin.kitchen.adapter.OrderTabAdapter;
import com.anxin.kitchen.fragment.orderfragment.OrderFragment;
import com.anxin.kitchen.user.R;
import com.anxin.kitchen.view.PagerSlidingTab;

/**
 * 订单页面
 * Created by xujianjun on 2018/4/5.
 */

public class OrderActivity extends BaseFragmentActivity implements ViewPager.OnPageChangeListener {

    private ImageView ivBack;
    private TextView tvTitle;

    private PagerSlidingTab porderSlidingTab;
    private ViewPager vpOrder;

    private LinearLayout llAll;
    private TextView tvAll;
    private View vAll;
    private LinearLayout llNoPay;
    private TextView tvNoPay;
    private View vNoPay;
    private LinearLayout llPayed;
    private TextView tvPayed;
    private View vPayed;
    private LinearLayout llCompleted;
    private TextView tvCompleted;
    private View vCompleted;
    private LinearLayout llCanceled;
    private TextView tvCanceled;
    private View vCanceled;

    private OrderTabAdapter mTabAdapter;
    private Fragment[] mFragments;
    private String[] mTab;
    private int chooseType = 0;
    private int closeType = -1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        initView();
        initData();
        setTabChooseData(chooseType);

    }


    private void initView() {
        ivBack = findViewById(R.id.back_img);
        tvTitle = findViewById(R.id.title_tv);
        porderSlidingTab = (PagerSlidingTab) findViewById(R.id.porderSlidingTab);
        vpOrder = (ViewPager) findViewById(R.id.vp_order);

        llAll = (LinearLayout) findViewById(R.id.ll_all);
        tvAll = (TextView) findViewById(R.id.tv_all);
        vAll = (View) findViewById(R.id.v_all);
        llNoPay = (LinearLayout) findViewById(R.id.ll_no_pay);
        tvNoPay = (TextView) findViewById(R.id.tv_no_pay);
        vNoPay = (View) findViewById(R.id.v_no_pay);
        llPayed = (LinearLayout) findViewById(R.id.ll_payed);
        tvPayed = (TextView) findViewById(R.id.tv_payed);
        vPayed = (View) findViewById(R.id.v_payed);
        llCompleted = (LinearLayout) findViewById(R.id.ll_completed);
        tvCompleted = (TextView) findViewById(R.id.tv_completed);
        vCompleted = (View) findViewById(R.id.v_completed);
        llCanceled = (LinearLayout) findViewById(R.id.ll_canceled);
        tvCanceled = (TextView) findViewById(R.id.tv_canceled);
        vCanceled = (View) findViewById(R.id.v_canceled);

        ivBack.setOnClickListener(this);
        llAll.setOnClickListener(this);
        llNoPay.setOnClickListener(this);
        llPayed.setOnClickListener(this);
        llCompleted.setOnClickListener(this);
        llCanceled.setOnClickListener(this);
        vpOrder.setOnPageChangeListener(this);


        tvTitle.setText("我的订单");
        chooseType = getIntent().getIntExtra("type", 0);
        closeType = getIntent().getIntExtra("closeType", 0);

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
        vpOrder.setOffscreenPageLimit(5);
//        porderSlidingTab.setViewPager(vpOrder);//绑定pagerSlidingTab和ViewPager

        vpOrder.setCurrentItem(chooseType);
    }

    private void setTabChooseData(int position) {
        tvAll.setTextColor(getResources().getColor(R.color.color_desc));
        tvNoPay.setTextColor(getResources().getColor(R.color.color_desc));
        tvPayed.setTextColor(getResources().getColor(R.color.color_desc));
        tvCompleted.setTextColor(getResources().getColor(R.color.color_desc));
        tvCanceled.setTextColor(getResources().getColor(R.color.color_desc));

        vAll.setVisibility(View.INVISIBLE);
        vNoPay.setVisibility(View.INVISIBLE);
        vPayed.setVisibility(View.INVISIBLE);
        vCompleted.setVisibility(View.INVISIBLE);
        vCanceled.setVisibility(View.INVISIBLE);
        switch (position) {
            case 0:
                tvAll.setTextColor(getResources().getColor(R.color.tv_green));
                vAll.setVisibility(View.VISIBLE);
                break;
            case 1:
                tvNoPay.setTextColor(getResources().getColor(R.color.tv_green));
                vNoPay.setVisibility(View.VISIBLE);
                break;
            case 2:
                tvPayed.setTextColor(getResources().getColor(R.color.tv_green));
                vPayed.setVisibility(View.VISIBLE);
                break;
            case 3:
                tvCompleted.setTextColor(getResources().getColor(R.color.tv_green));
                vCompleted.setVisibility(View.VISIBLE);
                break;
            case 4:
                tvCanceled.setTextColor(getResources().getColor(R.color.tv_green));
                vCanceled.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (closeType != -1 && closeType == 1) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else
            finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_img:
                if (closeType != -1 && closeType == 1) {
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else
                    finish();
                break;
            case R.id.ll_all:
                vpOrder.setCurrentItem(0);
                break;
            case R.id.ll_no_pay:
                vpOrder.setCurrentItem(1);
                break;
            case R.id.ll_payed:
                vpOrder.setCurrentItem(2);
                break;
            case R.id.ll_completed:
                vpOrder.setCurrentItem(3);
                break;
            case R.id.ll_canceled:
                vpOrder.setCurrentItem(4);
                break;

            default:
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        setTabChooseData(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
