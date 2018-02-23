package com.anxin.kitchen.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.anxin.kitchen.user.R;

import java.util.ArrayList;
import java.util.List;

public class PreserveListActivity extends BaseActivity {
    private ListView mMealCatalogLv;
    private List<String> mCatalogList = new ArrayList<>();
    private CatalogAdapter mCatalogAdapter;
    private ListView mContentLv;
    private ContentAdapter mContentAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preserve_list);
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
        mCatalogList.add("推荐");
        mCatalogList.add("湘菜");
        mCatalogList.add("粤菜");
        mCatalogList.add("川菜");
        mCatalogList.add("东北菜");
        mCatalogList.add("江浙菜");
        mCatalogList.add("上海菜");
        mCatalogList.add("西北菜");
        mCatalogList.add("徽菜");
        mCatalogList.add("其他");
    }
    private void setTitleBar() {
        setTitle("选择菜单");
        findViewById(R.id.search_img).setVisibility(View.VISIBLE);
    }
    private void initView() {
        mMealCatalogLv = findViewById(R.id.meal_catalog_lv);
        mContentLv = findViewById(R.id.content_lv);
        ImageView back_img = (ImageView) findViewById(R.id.back_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private class CatalogAdapter extends BaseAdapter {
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
            view = LayoutInflater.from(PreserveListActivity.this).inflate(R.layout.recovery_catalog_item,viewGroup,false);
            TextView catalogTv = view.findViewById(R.id.catalog_tv);
            catalogTv.setText(mCatalogList.get(position));
            view.findViewById(R.id.nums_tv).setVisibility(View.GONE);
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
            view = LayoutInflater.from(PreserveListActivity.this).inflate(R.layout.expression_meal_item,viewGroup,false);
            view.findViewById(R.id.add_lv).setVisibility(View.GONE);
            ImageView foodImg = view.findViewById(R.id.food_img);
            foodImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startNewActivity(MealIntroduceActivity.class);
                }
            });
            return view;
        }
    }
}
