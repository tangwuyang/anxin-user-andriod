package com.anxin.kitchen.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.anxin.kitchen.MyApplication;
import com.anxin.kitchen.bean.BannerListBean;
import com.anxin.kitchen.bean.FriendsBean;
import com.anxin.kitchen.bean.MealBean;
import com.anxin.kitchen.bean.NearKitchenBean;
import com.anxin.kitchen.bean.SearchGroupBean;
import com.anxin.kitchen.fragment.groupfragment.GroupMainFragment;
import com.anxin.kitchen.fragment.mealfragment.MealMainFragment;
import com.anxin.kitchen.fragment.myfragment.MyMainFragment;
import com.anxin.kitchen.fragment.orderfragment.OrderMainFragment;
import com.anxin.kitchen.interface_.OnGivedPermissionListener;
import com.anxin.kitchen.interface_.RequestNetListener;
import com.anxin.kitchen.user.R;
import com.anxin.kitchen.utils.Cache;
import com.anxin.kitchen.utils.Constant;
import com.anxin.kitchen.utils.MyService;
import com.anxin.kitchen.utils.PrefrenceUtil;
import com.anxin.kitchen.utils.SharedPreferencesUtil;
import com.anxin.kitchen.utils.StringUtils;
import com.anxin.kitchen.utils.SystemUtility;
import com.anxin.kitchen.view.RequestLocationPermissionDialog;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 主界面
 */
public class MainActivity extends BaseActivity implements View.OnClickListener, RequestNetListener {
    private static final int BAIDU_READ_PHONE_STATE = 100;
    private static final String TAG = "MainActivity";
    public static final String SEARCH_GROUP = "SEARCH_GROUP";
    public static final String GET_FRIEND = "GET_FRIEND";
    public static final String DELETE_GROUP = "DELETE_GROUP";
    private static final String RE_GET_GROUP = "再次请求团";
    public static final String DELETE_FRIEND = "DELETE_FRIEND";
    public static final String NET_GET_RECENCT_ORDERS = "getRecentOrders";
    public static final String NET_GET_ORDERS_NUM = "getOrdersNum";
    private PrefrenceUtil prefrenceUtil;
    public static Context context;
    // 定义4个Fragment对象
    private MealMainFragment mealMainFragment;

    private GroupMainFragment groupMainFragment;
    private OrderMainFragment orderMainFragment;
    private MyMainFragment myMainFragment;
    // 定义每个选项中的相关控件
    private LinearLayout myButtom_Lyt;
    private RadioGroup myButtomGroup;
    private RadioButton mealButtomBtn;
    private RadioButton groupButtomBtn;
    private RadioButton orderButtomBtn;
    private RadioButton myButtomBtn;
    private ImageView buttom_img;
    // 定义FragmentManager对象管理器
    private FragmentManager fragmentManager;
    //欢迎页面
    public static final String GET_KITCHEN_ID = "GET_KITCHEN_ID";

    public static final String GET_BANNER_LIST = "GET_BANNER_LIST";
    private static final String GET_MENU_MEAL = "GET_MENU_MEAL";
    public String requesNetTag = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        prefrenceUtil = new PrefrenceUtil(this);
        context = this;
        //启动服务
        Intent agentService = new Intent(MainActivity.this, MyService.class);
        // bindService(agentService, conn, Service.BIND_AUTO_CREATE);
        startService(agentService);
        requestLocationPermission();
        //获取定位所有城市ID列表(本地缓存)
        if (MyApplication.getInstance().getAddressNameMap() == null)
            SystemUtility.sendGetAddressList();
        initView();//初始化界面控件
        setChioceItem(0);//初始化页面加载是显示点餐界面
    }

    private void requestLocationPermission() {
        boolean hasLocationPermission = getLocationPermission();
        if (!hasLocationPermission) {
            popRequestWindow();
        }
    }

    //弹出请求权限框
    private void popRequestWindow() {
        RequestLocationPermissionDialog dialog = new RequestLocationPermissionDialog(this, new OnGivedPermissionListener() {
            @Override
            public void onGivedPermssion() {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, BAIDU_READ_PHONE_STATE);
            }
        });
        dialog.show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            // requestCode即所声明的权限获取码，在checkSelfPermission时传入
            case BAIDU_READ_PHONE_STATE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 获取到权限，作相应处理（调用定位SDK应当确保相关权限均被授权，否则可能引起定位失败）

                } else {
                    // 没有获取到权限，做特殊处理
                    Toast.makeText(getApplicationContext(), "获取位置权限失败，请手动开启", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private Handler handler = new Handler();

    private void initView() {
        //初始化底部导航栏的控件
        myButtom_Lyt = (LinearLayout) findViewById(R.id.main_bottom_group);
        myButtomGroup = (RadioGroup) findViewById(R.id.bottom_radiogroup);
        mealButtomBtn = (RadioButton) findViewById(R.id.meal_radioBtn);
        groupButtomBtn = (RadioButton) findViewById(R.id.group_radioBtn);
        orderButtomBtn = (RadioButton) findViewById(R.id.order_radioBtn);
        myButtomBtn = (RadioButton) findViewById(R.id.my_radioBtn);
        myButtomGroup.setOnCheckedChangeListener(myButtomGroupCheck);
        buttom_img = findViewById(R.id.buttom_img);
    }

    RadioGroup.OnCheckedChangeListener myButtomGroupCheck = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup Group, int checkedId) {
            // TODO Auto-generated method stub
            if (mealButtomBtn.getId() == checkedId) {
                setChioceItem(0);
            } else if (groupButtomBtn.getId() == checkedId) {
                setChioceItem(1);
            } else if (orderButtomBtn.getId() == checkedId) {
                setChioceItem(2);
            } else if (myButtomBtn.getId() == checkedId) {
                setChioceItem(3);
            }
        }
    };

    @Override
    public void onClick(View v) {

    }

    //隐藏底部栏
    public void hideMainBottom() {
        myButtom_Lyt.setVisibility(View.GONE);
        buttom_img.setVisibility(View.GONE);
    }

    //显示底部栏
    public void showMainBotton() {
        myButtom_Lyt.setVisibility(View.VISIBLE);
        buttom_img.setVisibility(View.VISIBLE);
    }

    /**
     * 设置点击选项卡的事件处理
     *
     * @param index 选项卡的标号：0, 1, 2, 3
     */
    private void setChioceItem(int index) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        hideFragments(fragmentTransaction);
        switch (index) {
            case 0:
// 如果fg1为空，则创建一个并添加到界面上
                if (mealMainFragment == null) {
                    mealMainFragment = new MealMainFragment();
                    fragmentTransaction.add(R.id.content_frame, mealMainFragment);
                } else {
// 如果不为空，则直接将它显示出来
                    fragmentTransaction.show(mealMainFragment);
                }
                break;
            case 1:
                if (groupMainFragment == null) {
                    groupMainFragment = new GroupMainFragment();
                    fragmentTransaction.add(R.id.content_frame, groupMainFragment);
                } else {
                    fragmentTransaction.show(groupMainFragment);
                }
                break;
            case 2:
                if (orderMainFragment == null) {
                    orderMainFragment = new OrderMainFragment();
                    fragmentTransaction.add(R.id.content_frame, orderMainFragment);
                } else {
                    fragmentTransaction.show(orderMainFragment);
                }
                break;
            case 3:
                if (myMainFragment == null) {
                    myMainFragment = new MyMainFragment();
                    fragmentTransaction.add(R.id.content_frame, myMainFragment);
                } else {
                    fragmentTransaction.show(myMainFragment);
                }
                break;
        }
        fragmentTransaction.commit(); // 提交
    }

    /**
     * 隐藏Fragment
     *
     * @param fragmentTransaction
     */
    private void hideFragments(FragmentTransaction fragmentTransaction) {
        if (mealMainFragment != null) {
            fragmentTransaction.hide(mealMainFragment);
        }
        if (groupMainFragment != null) {
            fragmentTransaction.hide(groupMainFragment);
        }
        if (orderMainFragment != null) {
            fragmentTransaction.hide(orderMainFragment);
        }
        if (myMainFragment != null) {
            fragmentTransaction.hide(myMainFragment);
        }
    }


    //是否有位置权限
    public boolean getLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                ) {
            return false;
        } else return true;
    }


    @Override
    public void requestSuccess(String responseBody, String requestCode) {
        String status = StringUtils.parserMessage(responseBody, "message");
        if (requestCode != null && requestCode.equals(GET_KITCHEN_ID)) {
            myLog("------GET_KITCHEN_ID----->" + responseBody + status);
            if (null != status && status.equals(Constant.REQUEST_SUCCESS)) {
                Gson gson = new Gson();
                NearKitchenBean bean = gson.fromJson(responseBody, NearKitchenBean.class);
                int kichtchenId = bean.getData().getKitchenid();
                myLog("----getKitchenname----" + bean.getData().getKitchenname());
                PrefrenceUtil prefrenceUtil = new PrefrenceUtil(MainActivity.this);
                prefrenceUtil.putKitchenId(kichtchenId);
                Map<String, Object> dataMap = new HashMap<>();
                dataMap.put(Constant.KITCHEN_ID, kichtchenId);
                requestNet(SystemUtility.getBannerListUrl(), dataMap, GET_BANNER_LIST);
                requestNet(SystemUtility.getMenuMealUrl(), dataMap, GET_MENU_MEAL);
                return;
            }
        }

        //请求轮播广告返回
        if (requestCode != null && requestCode.equals(GET_BANNER_LIST)) {
            if (null != status && status.equals(Constant.REQUEST_SUCCESS)) {
                myLog("----GET_BANNER_LIST----" + responseBody);
                //再去获取广告列表
                BannerListBean bannerListBean = mGson.fromJson(responseBody, BannerListBean.class);
                List<BannerListBean.Data> dataList = bannerListBean.getData();
                if (null != mealMainFragment) {
                    mealMainFragment.setBanner(dataList);
                }
            }
            return;
        }
        //请求附近的菜单
        if (requestCode != null && requestCode.equals(GET_MENU_MEAL)) {
            myLog("--------GET_MENU_MEAL---->status" + status);
            if (null != status && status.equals(Constant.REQUEST_SUCCESS)) {
                myLog("--------" + responseBody);
                MealBean mealBean = mGson.fromJson(responseBody, MealBean.class);
                mealMainFragment.closeWaitingDialog();
                mealMainFragment.setMeal(mealBean);
            } else if (null != status && status.equals(Constant.REQUEST_EXCEPTION)) {
                Toast.makeText(context, "请求异常", Toast.LENGTH_SHORT).show();
                mealMainFragment.closeWaitingDialog();
            }
            return;
        }


        //查询所有创建的团
        if (requestCode != null && requestCode.equals(SEARCH_GROUP)) {
            if (null != status && status.equals(Constant.REQUEST_SUCCESS)) {
                String gsonSt = StringUtils.parserMessage(responseBody, "data");
                myLog("------------>" + gsonSt);
                Gson gson = new Gson();
                SearchGroupBean bean = gson.fromJson(gsonSt, SearchGroupBean.class);
                if (!gsonSt.equals(Constant.NULL)) {
                    myLog(bean.getData().size() + "--------" + gsonSt);
                    prefrenceUtil.putGroups(gsonSt);
                    groupMainFragment.setGroup(bean);
                }
            } else if (null != status && status.equals(Constant.LOGIN_FIRST)) {
                SystemUtility.startLoginUser(MainActivity.this);
            }
            return;
        }
        //查询团友
        if (requestCode != null && requestCode.equals(GET_FRIEND)) {
            if (null != status && status.equals(Constant.REQUEST_SUCCESS)) {
                Gson gson = new Gson();
                myLog("--------" + responseBody);
                FriendsBean bean = gson.fromJson(responseBody, FriendsBean.class);
                if (null != bean.getData() && bean.getData().size() > 0) {
                    prefrenceUtil.putFrinends(responseBody);
                    groupMainFragment.setFriend(bean);
                }
            } else if (null != status && status.equals(Constant.LOGIN_FIRST)) {
                SystemUtility.startLoginUser(MainActivity.this);
            }
            return;
        }

        if (requestCode != null && requestCode.equals(DELETE_GROUP)) {
            if (null != status && status.equals(Constant.REQUEST_SUCCESS)) {
                myLog("-------------delte" + responseBody);
                //重新请求团信息
                String token = MyApplication.getInstance().getCache().getAMToken();
                Map<String, Object> dataMap = new HashMap<>();
                dataMap.put(Constant.TOKEN, token);
                requestNet(SystemUtility.searchGroupUrl(), dataMap, RE_GET_GROUP);

            } else if (null != status && status.equals(Constant.HAS_NO_PREMISSION)) {
                Toast.makeText(context, "您暂时无权限删除此团", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        if (requestCode != null && requestCode.equals(RE_GET_GROUP)) {
            if (null != status && status.equals(Constant.REQUEST_SUCCESS)) {
                myLog("-------------re" + responseBody);
                String gsonSt = StringUtils.parserMessage(responseBody, "data");
                Gson gson = new Gson();
                SearchGroupBean bean = gson.fromJson(gsonSt, SearchGroupBean.class);
                myLog(bean.getData().size() + "--------" + gsonSt);
                prefrenceUtil.putGroups(gsonSt);
                groupMainFragment.updataGroup(bean);
            } else if (null != status && status.equals(Constant.LOGIN_FIRST)) {
                SystemUtility.startLoginUser(MainActivity.this);
            }
            return;
        }

        if (requestCode != null && requestCode.equals(DELETE_FRIEND)) {
            if (null != status && status.equals(Constant.REQUEST_SUCCESS)) {
                groupMainFragment.setDataChanged();
            }
        }

        if (requestCode != null && requestCode.equals(NET_GET_RECENCT_ORDERS)) {
            if (null != status && status.equals(Constant.REQUEST_SUCCESS)) {
                orderMainFragment.setGroup(responseBody);
                return;
            }
        }

        if (requestCode != null && requestCode.equals(NET_GET_ORDERS_NUM)) {
            if (null != status && status.equals(Constant.REQUEST_SUCCESS)) {
                SharedPreferencesUtil.getInstance(this).putSP("getOrderNumTime", System.currentTimeMillis() + "");
                orderMainFragment.setNum(responseBody);
                return;
            }
        }


    }

    @Override
    public void requestFailure(String responseFailureBody, String requestCode) {

    }

}
