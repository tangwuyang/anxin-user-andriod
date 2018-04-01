package com.anxin.kitchen.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
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

import com.anxin.kitchen.bean.MealBean;
import com.anxin.kitchen.bean.RecorverMenuBean;
import com.anxin.kitchen.bean.RecoverBean;
import com.anxin.kitchen.interface_.RequestNetListener;
import com.anxin.kitchen.tangwuyangs_test.Test;
import com.anxin.kitchen.user.R;
import com.anxin.kitchen.utils.Cache;
import com.anxin.kitchen.utils.Constant;
import com.anxin.kitchen.utils.Log;
import com.anxin.kitchen.utils.PrefrenceUtil;
import com.anxin.kitchen.utils.StringUtils;
import com.anxin.kitchen.utils.SystemUtility;
import com.anxin.kitchen.view.RefreshLayout;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 *    康复食疗
 * */
public class RecoveryMealActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener,
        RefreshLayout.OnLoadListener,RequestNetListener{
    private static final String GET_MENU_MEAL = "GET_MENU_MEAL"; //获取全部
    private static final String GET_MENU_ = "GET_MENU_";  //请求菜单
    private static final String GET_CA_MENU_MEAL = "GET_CA_MENU_MEAL";  //去获取具体某一个菜系的具体
    private ListView mMealCatalogLv;
    private ImageView mShoppingCartImg;
    private TextView mBottomNum;
    private TextView mMoneyTv;
    private TextView mCloseAccountTv;
    private List<String> mCatalogList = new ArrayList<>();
    private CatalogAdapter mCatalogAdapter;
    private ListView mContentLv;
    private ContentAdapter mContentAdapter;
    private RefreshLayout mMealRefreshLy;
    private MealBean mealBean;
    private PrefrenceUtil prefrenceUtil;
    private int kichtchenId;
    private List<RecoverBean.Data> mealList;
    private String mToken;
    private Cache mCache;
    private Gson mGson;
    private int page = 0;   //选择查看页码
    private long dietId = -1; 	//食疗ID(-1标识全部)
    private boolean isChoseMeal;  //是否有选择食材
    private LinkedHashMap<String ,RecoverBean.Data> mChosedMeals = new LinkedHashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recovery_meal);
        setTitleBar();
        initView();
        mCache = new Cache(this);
        mGson = new Gson();
        prefrenceUtil = new PrefrenceUtil(RecoveryMealActivity.this);
        Map<String, Object> dataMap = new HashMap<>();
        mToken = mCache.getAMToken();
        kichtchenId = prefrenceUtil.getKitchenId();
        dataMap.put(Constant.KITCHEN_ID, kichtchenId);
        dataMap.put("page",page);
        dataMap.put("token",mToken);
        dataMap.put("dietId",-1);
        Map<String,Object> menuData = new HashMap<>();
        menuData.put("token",mToken);
        requestNet(SystemUtility.getRecoverMenuUrl(),menuData,GET_MENU_);
        requestNet(SystemUtility.getRecoverList(), dataMap, GET_MENU_MEAL);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setContentAdapter() {
        if (null == mContentAdapter){
            mContentAdapter = new ContentAdapter();
        }
        mContentLv.setAdapter(mContentAdapter);
    }

    private void setCatalogAdapter(RecorverMenuBean bean) {

        if (null == mCatalogAdapter){
            mCatalogAdapter = new CatalogAdapter(bean.getData());
        }
        mMealCatalogLv.setAdapter(mCatalogAdapter);
    }


    private void initView() {
        mMealRefreshLy = findViewById(R.id.refresh_ll);
        mMealCatalogLv = findViewById(R.id.meal_catalog_lv);
        mContentLv = findViewById(R.id.content_lv);
        ImageView back_img = (ImageView) findViewById(R.id.back_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mMealRefreshLy.setOnLoadListener(this);
        mMealRefreshLy.setOnRefreshListener(this);
        mShoppingCartImg = findViewById(R.id.shopping_cart_img);
        mBottomNum = findViewById(R.id.nums_tv);
        mMoneyTv = findViewById(R.id.money_tv);
        mCloseAccountTv = findViewById(R.id.close_account_tv);
    }


    @Override
    public void requestFailure(String responseFailure, String requestCode) {
        super.requestFailure(responseFailure, requestCode);
    }

    @Override
    public void requestSuccess(String responseBody, String requestCode) {
        super.requestSuccess(responseBody, requestCode);
        String status = StringUtils.parserMessage(responseBody, "message");

        //请求附近的菜单
        if (requestCode != null && requestCode.equals(GET_MENU_MEAL)) {
            if (null != status && status.equals(Constant.REQUEST_SUCCESS)) {
                myLog("--------" + responseBody);
                RecoverBean bean = mGson.fromJson(responseBody,RecoverBean.class);
                mealList = bean.getData();
                setContentAdapter();
            }
            return;
        }

        //请求附近的菜单
        if (requestCode != null && requestCode.equals(GET_CA_MENU_MEAL)) {
            if (null != status && status.equals(Constant.REQUEST_SUCCESS)) {
                myLog("--------" + responseBody);
                RecoverBean bean = mGson.fromJson(responseBody,RecoverBean.class);
                mealList = bean.getData();
                setContentAdapter();
            }
            return;
        }


        if (requestCode != null && requestCode.equals(GET_MENU_)){
            if (null != status && status.equals(Constant.REQUEST_SUCCESS)){
                RecorverMenuBean bean = mGson.fromJson(responseBody,RecorverMenuBean.class);
                setCatalogAdapter(bean);
            }
            return;
        }
    }

    private void setTitleBar() {
        setTitle("康复食疗");
        findViewById(R.id.search_img).setVisibility(View.VISIBLE);
        RelativeLayout bottom_rl = findViewById(R.id.bottom_rl);
    }

    @Override
    public void onRefresh() {
        mMealRefreshLy.postDelayed(new Runnable() {

            @Override
            public void run() {
                // 更新数据  更新完后调用该方法结束刷新
                //mealBean.getData().clear();
                for (int i = 0; i < 8; i++) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("itemImage", i+"刷新");
                    map.put("itemText", i+"刷新");
                }
                mContentAdapter.notifyDataSetChanged();
                mMealRefreshLy.setRefreshing(false);
            }
        }, 2000);
    }

    @Override
    public void onLoad() {
        mMealRefreshLy.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 更新数据  更新完后调用该方法结束刷新
                mMealRefreshLy.setLoading(false);
                for (int i = 1; i < 10; i++) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("itemImage", i+"更多");
                    map.put("itemText", i+"更多");

                }
                mContentAdapter.notifyDataSetChanged();
            }
        }, 2000);
    }


    //康复食疗分类适配器
    private class CatalogAdapter extends BaseAdapter{
        public List<RecorverMenuBean.Data> menuList;
        private List<Boolean> markList;
        private LinkedHashMap<Integer,Integer> countMap = new LinkedHashMap<>();
        private List<RecorverMenuBean.Data> counts;

        public CatalogAdapter(List<RecorverMenuBean.Data> data) {
            menuList = data;
            markList = new ArrayList<>();
            RecorverMenuBean recorverMenuBean = new RecorverMenuBean();
            RecorverMenuBean.Data allMenu;
            allMenu = new RecorverMenuBean.Data();
            allMenu.setDietName("全  部");
            allMenu.setId(-1);
            menuList.add(0, allMenu);
            //初始化的时候去内存中加载是否有上次点击的菜
            setCounts(menuList);
            for (int i = 0; i < data.size(); i++) {
                if (i == 0) {
                    markList.add(true);
                } else {
                    markList.add(false);
                }
            }
        }

        @Override
        public int getCount() {
            return menuList.size();
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
            view = LayoutInflater.from(RecoveryMealActivity.this).inflate(R.layout.recovery_catalog_item,viewGroup,false);
            TextView catalogTv = view.findViewById(R.id.catalog_tv);
            catalogTv.setText(menuList.get(position).getDietName());
            int nums = menuList.get(position).getCounts();
            TextView numsTv= view.findViewById(R.id.nums_tv);
            if (nums>0){
                numsTv.setVisibility(View.VISIBLE);
                numsTv.setText(menuList.get(position).getCounts()+"");
            }else {
                numsTv.setVisibility(View.GONE);
            }


            //设置背景
            if (markList.get(position)){
                view.setBackgroundColor(getResources().getColor(R.color.white));
            }else {
                view.setBackgroundColor(getResources().getColor(R.color.main_bg_color));
            }

            //选择菜系
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
                    CatalogAdapter.this.notifyDataSetChanged();

                    Map<String, Object> dataMap = new HashMap<>();

                    dataMap.put(Constant.KITCHEN_ID, kichtchenId);
                    dataMap.put("page",page);
                    dataMap.put("token",mToken);
                    dataMap.put("dietId",menuList.get(position).getId());
                    dataMap.put(Constant.KITCHEN_ID, kichtchenId);
                    requestNet(SystemUtility.getRecoverList(), dataMap, GET_CA_MENU_MEAL);
                }
            });
            return view;
        }

        public void setCounts(List<RecorverMenuBean.Data> counts) {
            this.counts = counts;
        }
    }

    private class ContentAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return mealList.size();
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
            final RecoverBean.Data meal = mealList.get(position);
            ViewHolder holder = null;
            if (null == view){
                view = LayoutInflater.from(RecoveryMealActivity.this).inflate(R.layout.expression_meal_item,viewGroup,false);
                holder = new ViewHolder();
                holder.titleTv = view.findViewById(R.id.food_name);
                holder.contentTv = view.findViewById(R.id.food_content);
                holder.iconImg = view.findViewById(R.id.food_img);
                holder.addTv = view.findViewById(R.id.add_img);
                holder.reduceTv = view.findViewById(R.id.reduce_img);
                holder.numTv = view.findViewById(R.id.nums_tv);
                holder.priceTv = view.findViewById(R.id.meal_price_tv);
                view.setTag(holder);
            }else {
                holder = (ViewHolder) view.getTag();
            }

            holder.titleTv.setText(meal.getPackageName());

            for (String name:mChosedMeals.keySet()){
                myLog("name-----------"+name + "  " + mChosedMeals.get(name).getNums());
            }

            if (mChosedMeals.containsKey(String.valueOf(meal.getPackageId()))) {

                if (null != mChosedMeals.get(String.valueOf(meal.getPackageId())) && 0 != mChosedMeals.get(String.valueOf(meal.getPackageId())).getNums()) {
                    holder.numTv.setText(mChosedMeals.get(String.valueOf(meal.getPackageId())).getNums()+"");
                }
            }
            StringBuffer buffer = new StringBuffer();
            for (int i = 0;i<meal.getFoodList().size();i++
                 ) {
                RecoverBean.FoodList food = meal.getFoodList().get(i);
                if (i != meal.getFoodList().size()-1) {
                    buffer.append(food.getDishName() + "\r\n");
                }
            }
            holder.contentTv.setText(buffer.toString());
            myLog("-------1------->"+ meal.getPrice());
            holder.priceTv.setText("￥"+meal.getPrice());
            final ViewHolder finalHolder = holder;
            holder.priceTv.setText("￥"+meal.getPrice());

            //点击事件
            holder.reduceTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int nums = Integer.valueOf(finalHolder.numTv.getText().toString());
                    if (nums>0){
                        nums = nums-1;
                        finalHolder.numTv.setText(nums+"");
                        mCatalogAdapter.menuList.get(0).setCounts(mCatalogAdapter.menuList.get(0).getCounts()-1);
                        int type = meal.getDietId();
                        myLog("---------type"+type);
                        for (int i = 0;i<mCatalogAdapter.menuList.size();i++){
                            myLog("-----------菜系" + mCatalogAdapter.menuList.get(i).getId());
                            if (type == mCatalogAdapter.menuList.get(i).getId()){
                                mCatalogAdapter.menuList.get(i).setCounts(mCatalogAdapter.menuList.get(i).getCounts()-1);
                                break;
                            }
                        }

                        String name = String.valueOf(meal.getPackageId());
                        if (mChosedMeals.containsKey(name)){
                            mChosedMeals.get(name).setNums(mChosedMeals.get(name).getNums()-1);
                        }
                        mCatalogAdapter.notifyDataSetChanged();
                        setBottom();
                    }
                }
            });

            holder.addTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int nums = Integer.valueOf(finalHolder.numTv.getText().toString());
                        nums = nums+1;
                        finalHolder.numTv.setText(nums+"");
                    int type = meal.getDietId();
                    mCatalogAdapter.menuList.get(0).setCounts(mCatalogAdapter.menuList.get(0).getCounts()+1);
                    for (int i = 0;i<mCatalogAdapter.menuList.size();i++){
                        if (type == mCatalogAdapter.menuList.get(i).getId()){
                            mCatalogAdapter.menuList.get(i).setCounts(mCatalogAdapter.menuList.get(i).getCounts()+1);
                            break;
                        }
                    }
                    String name = String.valueOf(meal.getPackageId());
                    RecoverBean.Data transMeal = meal;
                    if (mChosedMeals.containsKey(name)){
                        mChosedMeals.get(name).setNums(mChosedMeals.get(name).getNums()+1);
                        myLog("-------加一个-" +mChosedMeals.get(name).getNums());
                    }else {
                        myLog("-------放进了一个-" );
                        transMeal.setNums(1);
                        mChosedMeals.put(String.valueOf(meal.getPackageId()),transMeal);
                    }
                    mCatalogAdapter.notifyDataSetChanged();
                    setBottom();
                }
            });
            return view;
        }
    }

    private void setBottom() {
        int num = mCatalogAdapter.menuList.get(0).getCounts();
        if (num>0){
            mShoppingCartImg.setImageDrawable(getResources().getDrawable(R.drawable.shoppint_cart_1));
            mCloseAccountTv.setText("去结算");
            mCloseAccountTv.setBackgroundColor(getResources().getColor(R.color.red));
            mBottomNum.setVisibility(View.VISIBLE);
            mBottomNum.setText(num+"");
            double allMoney = 0;
            for (String key: mChosedMeals.keySet() ) {
                RecoverBean.Data meal = mChosedMeals.get(key);
                allMoney = allMoney + meal.getNums() * meal.getPrice();
            }
            mMoneyTv.setText("￥"+allMoney);
        }else {
            mShoppingCartImg.setImageDrawable(getResources().getDrawable(R.drawable.shopping_cart_0));
            mCloseAccountTv.setText("20元起送");
            mCloseAccountTv.setBackgroundColor(getResources().getColor(R.color.color_desc));
            mBottomNum.setVisibility(View.GONE);
            mBottomNum.setText("");
            mMoneyTv.setText("未选购商品");
        }
    }

    class ViewHolder{
        TextView titleTv;
        TextView contentTv;
        ImageView iconImg;
        ImageView addTv;
        ImageView reduceTv;
        TextView numTv;
        TextView priceTv;
    }
}
