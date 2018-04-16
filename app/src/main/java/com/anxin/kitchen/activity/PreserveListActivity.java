package com.anxin.kitchen.activity;

import android.content.Intent;
import android.graphics.Bitmap;
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
import com.anxin.kitchen.bean.FoodsBean;
import com.anxin.kitchen.interface_.RequestNetListener;
import com.anxin.kitchen.user.R;
import com.anxin.kitchen.utils.Cache;
import com.anxin.kitchen.utils.Constant;
import com.anxin.kitchen.utils.PrefrenceUtil;
import com.anxin.kitchen.utils.StringUtils;
import com.anxin.kitchen.utils.SystemUtility;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PreserveListActivity extends BaseActivity implements RequestNetListener,View.OnClickListener{
    private static final String REQUEST_MENU = "REQUEST_MENU";
    public static final int AFTER_CHOESE = 200;
    private static final String GET_FOOD_REQ = "GET_FOOD_REQ";  //请求套餐
    private ImageLoader imageLoader = ImageLoader.getInstance();
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
    private FoodMenuBean mMenubean;
    private ImageView mSearchImg;
    private boolean login;
    private String mToken;
    private int kitchenId;
    private int page = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preserve_list);
        setTitleBar();
        initView();
        isLogin();
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
        //setContentAdapter();
    }
    private void setContentAdapter(FoodsBean foodsBean) {
        mContentAdapter = new ContentAdapter(foodsBean);
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
        mSearchImg = findViewById(R.id.search_img);
        mSearchImg.setVisibility(View.VISIBLE);
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
        mSearchImg.setOnClickListener(this);
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

    public boolean isLogin() {
        if (null == mToken){
            mCache = new Cache(PreserveListActivity.this);
            mToken =mCache.getAMToken();
            kitchenId = new PrefrenceUtil(PreserveListActivity.this).getKitchenId();
        }
        if (mToken==null){
            login = false;
            SystemUtility.startLoginUser(PreserveListActivity.this);
            return login;
        }else {
            login = true;
        }
        return login;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.search_img:
                startNewActivity(SearchMealActivityActivity.class);
                break;
        }
    }

    private class CatalogAdapter extends BaseAdapter {
        private List<Boolean> markList;

        public CatalogAdapter() {
            markList = new ArrayList<>();
            for (int i = 0; i < mCatalogList.size(); i++) {
                if (i == 0) {
                    markList.add(true);
                } else {
                    markList.add(false);
                }
            }
        }

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
        public View getView(final int position, View view, ViewGroup viewGroup) {
            view = LayoutInflater.from(PreserveListActivity.this).inflate(R.layout.recovery_catalog_item,viewGroup,false);
            TextView catalogTv = view.findViewById(R.id.catalog_tv);
            catalogTv.setText(mCatalogList.get(position));
            view.findViewById(R.id.nums_tv).setVisibility(View.GONE);
            //设置背景
            if (markList.get(position)){
                view.setBackgroundColor(getResources().getColor(R.color.white));
            }else {
                view.setBackgroundColor(getResources().getColor(R.color.main_bg_color));
            }

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (int i = 0;i<markList.size();i++){
                        if (i != position){
                            markList.set(i,false);
                        }else {
                            markList.set(position,true);
                        }
                    }
                    int id = mMenubean.getData().get(position).getId();
                    requestFoods(id);
                    CatalogAdapter.this.notifyDataSetChanged();
                }
            });
            return view;
        }
    }

    private class ContentAdapter extends BaseAdapter{
        private FoodsBean foodsBean;
        public ContentAdapter(FoodsBean foodsBean) {
            this.foodsBean = foodsBean;
        }

        @Override
        public int getCount() {
            return foodsBean.getData().size();
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
            ViewHolder holder = null;
            final FoodsBean.Data food = foodsBean.getData().get(position);
            if (null == view) {
                view = LayoutInflater.from(PreserveListActivity.this).inflate(R.layout.expression_meal_item, viewGroup, false);
                view.findViewById(R.id.add_lv).setVisibility(View.GONE);
                holder = new ViewHolder();
                holder.foodTitle = view.findViewById(R.id.food_name);
                holder.foodContent = view.findViewById(R.id.food_content);
                holder.foodPrice = view.findViewById(R.id.meal_price_tv);
                holder.foodImg = view.findViewById(R.id.food_img);
                view.setTag(holder);
            }else {
                holder = (ViewHolder) view.getTag();
            }
            holder.foodImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startNewActivity(MealIntroduceActivity.class);
                }
            });
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .showImageForEmptyUri(R.drawable.food1)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
            String imgSrc = food.getImg();
            imageLoader.displayImage(imgSrc,holder.foodImg,options);
            holder.foodPrice.setText("￥"+food.getPrice());
            holder.foodTitle.setText(food.getPackageName());
            String foodConten = "";
            for (int i = 0;i<food.getFoodList().size();i++){
                if (i != food.getFoodList().size()-1){
                    foodConten = foodConten + food.getFoodList().get(i).getDishName()+"\r\n";
                }else {
                    foodConten = foodConten + food.getFoodList().get(i).getDishName();
                }
            }
            holder.foodContent.setText(foodConten);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.putExtra("day",day);
                    intent.putExtra("type",type);
                    //myLog("--------------->"+mGson.toJson(food));
                    intent.putExtra("food",mGson.toJson(food));
                    setResult(AFTER_CHOESE,intent);
                    finish();
                }
            });
            return view;
        }

        class ViewHolder{
            private ImageView foodImg;
            private TextView foodTitle;
            private TextView foodContent;
            private TextView foodPrice;
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
            mMenubean = mGson.fromJson(responseString,FoodMenuBean.class);
            List<FoodMenuBean.Data> data = mMenubean.getData();
            List<String> muneList = new ArrayList<>();
            for (FoodMenuBean.Data menu :
                    data) {
                muneList.add(menu.getCuisineName());
            }
            this.mCatalogList = muneList;
            setCatalogAdapter();
            int id = mMenubean.getData().get(0).getId();
            requestFoods(id);
        }

        if (requestCode==GET_FOOD_REQ && status.equals(Constant.REQUEST_SUCCESS)){
            FoodsBean foodsBean = mGson.fromJson(responseString,FoodsBean.class);
            setContentAdapter(foodsBean);
        }
    }


    //去请求对应的套餐
    private void requestFoods(int id) {
        Map<String ,Object> dataMap = new HashMap<>();
        dataMap.put("page",page);
        dataMap.put("kitchenId",kitchenId);
        dataMap.put("cuisineId",id);
        dataMap.put("token",mToken);
        requestNet(SystemUtility.getFoodURL(),dataMap,GET_FOOD_REQ);
    }


}
