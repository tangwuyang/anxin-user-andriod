package com.anxin.kitchen.activity;

import android.content.Intent;
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

import com.anxin.kitchen.bean.FoodMenuBean;
import com.anxin.kitchen.interface_.RequestNetListener;
import com.anxin.kitchen.user.R;
import com.anxin.kitchen.utils.Cache;
import com.anxin.kitchen.utils.Constant;
import com.anxin.kitchen.utils.PrefrenceUtil;
import com.anxin.kitchen.utils.StringUtils;
import com.anxin.kitchen.utils.SystemUtility;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PreserveListActivity extends BaseActivity implements RequestNetListener{
    private static final String REQUEST_MENU = "REQUEST_MENU";
    public static final int AFTER_CHOESE = 200;
    private ListView mMealCatalogLv;
    private List<String> mCatalogList = new ArrayList<>();
    private CatalogAdapter mCatalogAdapter;
    private ListView mContentLv;
    private ContentAdapter mContentAdapter;
    private PrefrenceUtil prefrenceUtil;
    private Cache mCache;
    private Gson mGson;
    private long day;
    private String type;
    private String recevieData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preserve_list);
        setTitleBar();
        initView();
        getData();
    }

    private void getData() {
        getMenu();
        getAllMeal();
    }


    //第一次进入不按品类加载
    private void getAllMeal() {
    }

    private void getMenu() {
        long kitchenId = prefrenceUtil.getKitchenId();
        String token = mCache.getAMToken();
        Map<String ,Object> dataMap = new HashMap<>();
        dataMap.put("token",token);
        requestNet(SystemUtility.getFoodMenuUrl(),dataMap,REQUEST_MENU);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //setData();
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
        prefrenceUtil = new PrefrenceUtil(this);
        mCache = new Cache(this);
        mGson = new Gson();
        Intent intent = getIntent();
        if (null!=intent){
            recevieData = intent.getStringExtra("data");
            day = Long.valueOf(recevieData.substring(0,recevieData.indexOf("-")));
            type = recevieData.substring(recevieData.indexOf("-")+1);
            myLog("------------->"+day + "  " + type);
        }
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

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.putExtra("day",day);
                    intent.putExtra("type",type);
                    setResult(AFTER_CHOESE,intent);
                    finish();
                }
            });
            return view;
        }
    }

    @Override
    public void requestFailure(String responseFailure, String requestCode) {
        super.requestFailure(responseFailure, requestCode);
    }

    @Override
    public void requestSuccess(String responseString, String requestCode) {
        super.requestSuccess(responseString, requestCode);
        String status = StringUtils.parserMessage(responseString, Constant.REQUEST_STATUS);
        if (requestCode==REQUEST_MENU && status.equals(Constant.REQUEST_SUCCESS)){
            myLog("---------->menu" + responseString);
            FoodMenuBean bean = mGson.fromJson(responseString,FoodMenuBean.class);
            List<FoodMenuBean.Data> data = bean.getData();
            List<String> muneList = new ArrayList<>();
            for (FoodMenuBean.Data menu :
                    data) {
                muneList.add(menu.getCuisineName());
            }
            this.mCatalogList = muneList;
            mCatalogAdapter.notifyDataSetChanged();
        }
    }
}
