package com.anxin.kitchen.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.opengl.Visibility;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
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
import com.anxin.kitchen.view.MyListView;
import com.anxin.kitchen.view.RefreshLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
        RefreshLayout.OnLoadListener,RequestNetListener,View.OnClickListener{
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
    private boolean isOpenShopCart = false;
    private LinearLayout mShopCartLl;
    private ImageView mHoverScreen;
    private MyListView mShoppingCartLv;
    private PopupWindow mPopupWindow;  //弹出购物车
    private View mPopupView;  //购物车视图
    private static View mBottom;
    private static PopupWindow mPop;
    public List<RecorverMenuBean.Data> carhMenuList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recovery_meal);
        setTitleBar();
        initView();
        mCache = new Cache(this);
        mGson = new Gson();
        prefrenceUtil = new PrefrenceUtil(RecoveryMealActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isLogin()){
            Map<String, Object> dataMap = new HashMap<>();
            mToken = mCache.getAMToken();
            kichtchenId = prefrenceUtil.getKitchenId();
            dataMap.put(Constant.KITCHEN_ID, kichtchenId);
            dataMap.put("page",page);
            dataMap.put("token",mToken);
            dataMap.put("dietId",-1);
            Map<String,Object> menuData = new HashMap<>();
            menuData.put("token",mToken);
            String recoverSt = new PrefrenceUtil(this).getRecoverList();
            String cachMenuSt = new PrefrenceUtil(this).getRecoverMenu();
            myLog("------------>re" + recoverSt);
            if (null!=recoverSt&&(!recoverSt.equals("null"))&&recoverSt.length()>10) {
                mChosedMeals = mGson.fromJson(recoverSt, new TypeToken<LinkedHashMap<String ,RecoverBean.Data>>() {}.getType());
            }

            if (null!=cachMenuSt && (!cachMenuSt.equals("null"))&& cachMenuSt.length()>10){
                carhMenuList=mGson.fromJson(cachMenuSt,new TypeToken<List<RecorverMenuBean.Data>>(){}.getType());
            }
            requestNet(SystemUtility.getRecoverMenuUrl(),menuData,GET_MENU_);
            requestNet(SystemUtility.getRecoverList(), dataMap, GET_MENU_MEAL);

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mChosedMeals.size()>0){
            String choseMealsSt = mGson.toJson(mChosedMeals);
            new PrefrenceUtil(this).setRecoverList(choseMealsSt);
            myLog("------------------>" + choseMealsSt);
            carhMenuList = mCatalogAdapter.menuList;
            String cachMenuList = mGson.toJson(carhMenuList);
            new PrefrenceUtil(this).setRecoverMenuList(cachMenuList);
        }
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


    @SuppressLint("WrongViewCast")
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
        mShopCartLl = findViewById(R.id.shopcart_ll);
        mHoverScreen = findViewById(R.id.hover_screen);

        //获取得到PopupWindow的布局文件
        mPopupView = View.inflate(this, R.layout.shoping_cart_view, null);
        mBottom = findViewById(R.id.bottom_rl);
        mHoverScreen.setOnClickListener(this);
        mShoppingCartImg.setOnClickListener(this);
        mCloseAccountTv.setOnClickListener(this);
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
                setBottom();
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.shopping_cart_img:
                if (isOpenShopCart){
                    closeShoppingCart();
                    hidenHoverScreen();
                }else {
                   // openShoppingCart();
                    int num = mCatalogAdapter.menuList.get(0).getCounts();
                    if (num>0) {
                        propetyAnim(mHoverScreen);
                        showPopWindow(view, mPopupView, this, mHoverScreen);
                    }
                }
                break;
            case R.id.hover_screen:
                closeShoppingCart();
                hidenHoverScreen();
                break;
            case R.id.close_account_tv:
                Intent intent = new Intent(this,EnsureOrderActivity.class);
                String chosedMeal = mGson.toJson(mChosedMeals);
                intent.putExtra("chosedMeal",chosedMeal);
                startActivity(intent);
                break;
        }
    }



    /**
     * 显示PopupWindow
     * @param parent
     * @param view
     * @return
     */
    public  PopupWindow showPopWindow(View parent, View view, Context context, final ImageView iv_all) {
        mPop = new PopupWindow(view,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int popWidth = view.getMeasuredWidth();
        int popHeight = view.getMeasuredHeight();
        mShoppingCartLv  = view.findViewById(R.id.shopping_cart_lv);
        mShoppingCartLv.setAdapter(new ShoppingCartAdatpter());
        int loaction[] = new int[2];
        parent.getLocationOnScreen(loaction);
        mPop.setOutsideTouchable(true);
        mPop.setFocusable(true);
        //mPop.setBackgroundDrawable(new ColorDrawable(0));
        //mPop.setAnimationStyle(R.style.AnimBottom);
        //mPop.showAtLocation(parent, Gravity.TOP,(loaction[0]+parent.getWidth()/2)-popWidth/2,loaction[1]-popHeight);
        mPop.showAtLocation(parent, Gravity.BOTTOM,(loaction[0]+parent.getWidth()/2)-popWidth/2,222);

        LinearLayout clearLl = view.findViewById(R.id.clear_ll);
        clearLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (RecorverMenuBean.Data date:
                mCatalogAdapter.menuList) {
                    date.setCounts(0);
                }
                mChosedMeals.clear();
                mCatalogAdapter.notifyDataSetChanged();
                mContentAdapter.notifyDataSetChanged();
                setBottom();
                mPop.dismiss();
            }
        });
        //mPop.showAsDropDown(v,0,0);
        view.setFocusable(true); // 这个很重要
        view.setFocusableInTouchMode(true);

        mPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                propetyAnim2(iv_all);
                mPop = null;
            }
        });

        // 重写onKeyListener
        view.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    mPop.dismiss();
                    return true;
                }
                return false;
            }
        });

        // 点击其他地方消失
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (mPop != null && mPop.isShowing()) {
                    mPop.dismiss();
                }
                return false;
            }
        });

        return mPop;
    }


    /**
     * 半透明背景消失的动画
     * @param iv
     */
    public static void propetyAnim2(final ImageView iv){
        ObjectAnimator animator = ObjectAnimator.ofFloat(iv,"alpha",1,0.9f,0.7f,0.5f,0.2f,0);
        animator.setDuration(500);
        animator.setRepeatCount(0);
        animator.start();
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                iv.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    /**
     半透明背景出现的动画
     */
    private void propetyAnim(ImageView iv) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(iv, "alpha", 0, 0.2f, 0.5f, 0.7f, 0.9f, 1);
        animator.setDuration(500);
        animator.setRepeatCount(0);
        animator.start();
        iv.setVisibility(View.VISIBLE);
    }

    private void hidenHoverScreen() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mHoverScreen, "alpha", 1, 0.9f, 0.7f, 0.7f, 0.5f, 0);
        animator.setDuration(600);
        animator.setRepeatCount(0);
        animator.start();
        mHoverScreen.setVisibility(View.GONE);
    }


    private void showHoverScreen() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mHoverScreen, "alpha", 0, 0.2f, 0.5f, 0.7f, 0.9f, 1);
        animator.setDuration(600);
        animator.setRepeatCount(0);
        animator.start();
        mHoverScreen.setVisibility(View.VISIBLE);
    }

    private void openShoppingCart() {
        isOpenShopCart = true;
        //设置购物车
        setShopCartLv();
        mShopCartLl.setVisibility(View.VISIBLE);
        Animation openAnimation = AnimationUtils.loadAnimation(this, R.anim.shopcart_open);
        openAnimation.setFillAfter(true);
        mShopCartLl.startAnimation(openAnimation);
    }

    private void setShopCartLv() {
        mShoppingCartLv.setAdapter(new ShoppingCartAdatpter());
    }

    private void closeShoppingCart() {
        isOpenShopCart = false;
        Animation closeAnimation = AnimationUtils.loadAnimation(this, R.anim.shopcart_close);
        closeAnimation.setFillAfter(true);
        mShopCartLl.startAnimation(closeAnimation);
        closeAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mShopCartLl.setVisibility(View.GONE);
                mShopCartLl.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }


    //购物车适配器
    private class ShoppingCartAdatpter extends BaseAdapter{
        private LinkedList<RecoverBean.Data> mChosedList = new LinkedList<>();

        public ShoppingCartAdatpter() {
            for (String mealName:mChosedMeals.keySet()
                    ) {
                RecoverBean.Data meal = mChosedMeals.get(mealName);
                mChosedList.add(meal);
            }
            myLog("--------------n--->"+mChosedList.size());
        }

        @Override
        public int getCount() {
            return mChosedList.size();
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
        public View getView(final int i, View view, ViewGroup viewGroup) {
            final RecoverBean.Data meal = mChosedList.get(i);
            ViewHolder holder = null;
            if (null == view){
                holder = new ViewHolder();
                view = LayoutInflater.from(RecoveryMealActivity.this).inflate(R.layout.shopping_cart_item,viewGroup,false);
                holder.titleTv = view.findViewById(R.id.food_name);
                holder.contentTv = view.findViewById(R.id.food_content);
                holder.priceTv = view.findViewById(R.id.meal_price_tv);
                holder.addTv = view.findViewById(R.id.add_img);
                holder.reduceTv = view.findViewById(R.id.reduce_img);
                holder.numsTv = view.findViewById(R.id.nums_tv);
                view.setTag(holder);
            }else {
                holder = (ViewHolder) view.getTag();
            }
            holder.titleTv.setText(meal.getPackageName());
            String contents = "";

            //设置食疗内容
            for (int i1 = 0; i1<meal.getFoodList().size();i1++) {
               // myLog("------------>"+meal.getFoodList().get(i1).getDishName());
                if (i1<meal.getFoodList().size()-1){
                contents = contents + meal.getFoodList().get(i1).getDishName()+"\r\n";
                }else {
                    contents = contents + meal.getFoodList().get(i1).getDishName();
                }
            }
            holder.contentTv.setText(contents);
            //设置价格
            holder.priceTv.setText("￥"+(meal.getPrice()*meal.getNums())+".00");
            //设置数量
            holder.numsTv.setText(meal.getNums()+"");

            final ViewHolder finalHolder = holder;
            holder.addTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int nums = Integer.valueOf(finalHolder.numsTv.getText().toString());
                    nums = nums+1;
                    finalHolder.numsTv.setText(nums+"");
                    int type = meal.getDietId();
                    //先将总数加1
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
                    finalHolder.priceTv.setText("￥"+meal.getNums()*meal.getPrice()+".00");
                    if (mChosedMeals.containsKey(String.valueOf(meal.getPackageId()))) {
                        if (null != mChosedMeals.get(String.valueOf(meal.getPackageId())) && 0 != mChosedMeals.get(String.valueOf(meal.getPackageId())).getNums()) {
                            mChosedMeals.get(String.valueOf(meal.getPackageId())).setNums(meal.getNums());
                            myLog("----------num---->" + meal.getNums() + "   " + String.valueOf(meal.getPackageId()) + "  " + mChosedMeals.get(String.valueOf(meal.getPackageId())).getNums());
                          /*  mContentAdapter.notifyDataSetChanged();*/
                        }
                    }
                    mContentAdapter.notifyDataSetChanged();
                    mCatalogAdapter.notifyDataSetChanged();
                    setBottom();
                }
            });

            final ViewHolder finalHolder1 = holder;
            holder.reduceTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int nums = Integer.valueOf(finalHolder.numsTv.getText().toString());
                    if (nums>1){
                        deleteMeal1(nums, finalHolder, meal);
                        meal.setNums(nums-1);
                        finalHolder1.numsTv.setText(meal.getNums()+"");
                        finalHolder.priceTv.setText("￥"+meal.getNums()*meal.getPrice()+".00");
                    }else if (nums>0){
                        //如果只有一件货 则删除该商品
                        deleteMeal(nums, finalHolder, meal);
                        meal.setNums(nums-1);
                        mChosedList.remove(i);
                        mChosedMeals.remove(String.valueOf(meal.getPackageId()));
                        if (mChosedList.size()==0){
                            mPop.dismiss();
                        }else {
                            ShoppingCartAdatpter.this.notifyDataSetChanged();
                        }
                    }

                   mContentAdapter.notifyDataSetChanged();
                    mCatalogAdapter.notifyDataSetChanged();
                   /* if (mChosedMeals.containsKey(String.valueOf(meal.getPackageId()))) {
                        if (null != mChosedMeals.get(String.valueOf(meal.getPackageId())) && 0 != mChosedMeals.get(String.valueOf(meal.getPackageId())).getNums()) {
                            mChosedMeals.get(String.valueOf(meal.getPackageId())).setNums(meal.getNums());
                            mContentAdapter.notifyDataSetChanged();
                        }
                    }*/
                }
            });
            return view;
        }

        private void deleteMeal1(int nums, ViewHolder finalHolder, RecoverBean.Data meal) {
            nums = nums-1;
            finalHolder.numsTv.setText(nums+"");
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
            setBottom();
        }

        private void deleteMeal(int nums, ViewHolder finalHolder, RecoverBean.Data meal) {
            nums = nums-1;
            finalHolder.numsTv.setText(nums+"");
            mCatalogAdapter.menuList.get(0).setCounts(mCatalogAdapter.menuList.get(0).getCounts()-1);
            int type = meal.getDietId();
            myLog("---------type"+type);
            for (int i = 0;i<mCatalogAdapter.menuList.size();i++){
                myLog("-----------菜  系" + mCatalogAdapter.menuList.get(i).getId());
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
            mContentAdapter.notifyDataSetChanged();
            setBottom();
        }

        class ViewHolder{
            private ImageView addTv;
            private ImageView reduceTv;
            private TextView titleTv;
            private TextView contentTv;
            private TextView priceTv;
            private TextView numsTv;
        }
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

            //恢复菜单缓存
            if (carhMenuList != null){
                for (int i = 0; i<menuList.size();i++){
                    for (int j = 0; j<carhMenuList.size();j++){
                        if(menuList.get(i).getDietName().equals(carhMenuList.get(j).getDietName())){
                            menuList.get(i).setCounts(carhMenuList.get(j).getCounts());
                            break;
                        }
                    }
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
            myLog("-----重新刷新了-------------");
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
                    myLog("name--------------包含");
                    holder.numTv.setText(mChosedMeals.get(String.valueOf(meal.getPackageId())).getNums()+"");
                }
            }else {
                holder.numTv.setText("0");
            }

            StringBuffer buffer = new StringBuffer();
            for (int i = 0;i<meal.getFoodList().size();i++
                 ) {
                RecoverBean.FoodList food = meal.getFoodList().get(i);
                if (i != meal.getFoodList().size()-1) {
                    buffer.append(food.getDishName() + "\r\n");
                }else {
                    buffer.append(food.getDishName());
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
                        if (nums == 0){
                            mChosedMeals.remove(String.valueOf(meal.getPackageId()));
                        }
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
                            myLog("------------减少了-----" + mChosedMeals.get(name).getNums());
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
            mCloseAccountTv.setClickable(true);
            mCloseAccountTv.setEnabled(true);
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
            mCloseAccountTv.setClickable(false);
            mCloseAccountTv.setEnabled(false);
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
