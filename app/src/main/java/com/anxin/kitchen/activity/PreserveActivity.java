package com.anxin.kitchen.activity;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.anxin.kitchen.user.R;

import java.util.ArrayList;
import java.util.List;

public class PreserveActivity extends Activity {
    private ListView mAddPreserveLv;
    private List mDayList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preserve);
        initView();
        initData();
    }

    private void initData() {
        mDayList.add("周一");
        mDayList.add("周二");
        mDayList.add("周三");
        mDayList.add("周四");
        mDayList.add("周五");
        mDayList.add("周六");
        mDayList.add("周天");
    }

    private void initView() {
        setTitle("预约点餐");
        findViewById(R.id.view_kitchen).setVisibility(View.VISIBLE);
        mAddPreserveLv = findViewById(R.id.add_preserve_lv);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setAdapter();
    }
    private void setAdapter() {
        mAddPreserveLv.setAdapter(new PreserverAdapter());
    }

    private class PreserverAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mDayList.size();
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
        public View getView(int position, View view, ViewGroup viewGroup) {
            view = LayoutInflater.from(PreserveActivity.this).inflate(R.layout.preserver_lv_item,viewGroup,false);
            TextView dayTv = view.findViewById(R.id.day_tv);
            dayTv.setText((String) mDayList.get(position));
            if (position==6){
                view.findViewById(R.id.dinner_item).setVisibility(View.GONE);
                view.findViewById(R.id.add_img).setVisibility(View.VISIBLE);
            }
            return view;
        }
    }
}
