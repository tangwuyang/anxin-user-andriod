package com.anxin.kitchen.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.anxin.kitchen.user.R;
import com.anxin.kitchen.view.MyListView;

public class EnsureOrderActivity extends BaseActivity implements View.OnClickListener {
    private ImageView mBackImg;
    private TextView mTitleTv;
    private ImageView mTransmitImg;
    private MyListView mMealLv;
    private MyListView mTablemareLv;
    private MyListView mPayWayLv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ensure_order);
        initView();
    }

    private void initView() {
        mBackImg = findViewById(R.id.back_img);
        mTitleTv = findViewById(R.id.title_tv);
        mMealLv = findViewById(R.id.meal_lv);
        mPayWayLv = findViewById(R.id.payment_lv);
        mTablemareLv = findViewById(R.id.tableware_lv);
        mTransmitImg = findViewById(R.id.transmit_img);
        mMealLv.setAdapter(new MealAdapter());
        mTablemareLv.setAdapter(new TablewareAdapter());
        mPayWayLv.setAdapter(new PaymentAdapter());
        mTitleTv.setText("确认订单");
        mBackImg.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_img:
                this.finish();
                break;
            case R.id.transmit_img:
                break;
        }
    }


    class MealAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = LayoutInflater.from(EnsureOrderActivity.this).inflate(R.layout.order_meal_item,viewGroup,false);
            return view;
        }
    }
    class TablewareAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = LayoutInflater.from(EnsureOrderActivity.this).inflate(R.layout.order_tablemare_item,viewGroup,false);
            return view;
        }
    }
    class PaymentAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = LayoutInflater.from(EnsureOrderActivity.this).inflate(R.layout.order_paymentway_item,viewGroup,false);
            return view;
        }
    }
}
