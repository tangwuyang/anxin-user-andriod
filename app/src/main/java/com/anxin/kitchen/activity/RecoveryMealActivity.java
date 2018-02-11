package com.anxin.kitchen.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.anxin.kitchen.user.R;
import com.anxin.kitchen.utils.Log;

import java.util.ArrayList;
import java.util.List;


/**
 *    康复食疗
 * */
public class RecoveryMealActivity extends BaseActivity {
private ListView mMealCatalogLv;
private List<String> mCatalogList = new ArrayList<>();
private CatalogAdapter mCatalogAdapter;
private ListView mContentLv;
private ContentAdapter mContentAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recovery_meal);
        setTitleBar();
        initView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        setData();
        setCatalogAdapter();
        setContentAdapter();
    }

    private void setContentAdapter() {
        if (null == mContentAdapter){
            mContentAdapter = new ContentAdapter();
        }
        mContentLv.setAdapter(mContentAdapter);
    }

    private void setCatalogAdapter() {

        if (null == mCatalogAdapter){
            mCatalogAdapter = new CatalogAdapter();
        }
        mMealCatalogLv.setAdapter(mCatalogAdapter);
    }

    private void setData() {
        mCatalogList.add("全部");
        mCatalogList.add("月子餐");
        mCatalogList.add("糖尿病餐");
        mCatalogList.add("高血压餐");
        mCatalogList.add("心脏病餐");
        mCatalogList.add("肾移植餐");
        mCatalogList.add("其他");
    }

    private void initView() {
        mMealCatalogLv = findViewById(R.id.meal_catalog_lv);
        mContentLv = findViewById(R.id.content_lv);
    }


    private void setTitleBar() {
        setTitle("康复食疗");
        findViewById(R.id.search_img).setVisibility(View.VISIBLE);
        RelativeLayout bottom_rl = findViewById(R.id.bottom_rl);
    }


    //康复食疗分类适配器
    private class CatalogAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return mCatalogList.size();
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
            view = LayoutInflater.from(RecoveryMealActivity.this).inflate(R.layout.recovery_catalog_item,viewGroup,false);
            TextView catalogTv = view.findViewById(R.id.catalog_tv);
            catalogTv.setText(mCatalogList.get(position));
            return view;
        }
    }

    private class ContentAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return mCatalogList.size();
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
            view = LayoutInflater.from(RecoveryMealActivity.this).inflate(R.layout.expression_meal_item,viewGroup,false);
            return view;
        }
    }
}
