package com.anxin.kitchen.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.anxin.kitchen.fragment.groupfragment.GroupMainFragment;
import com.anxin.kitchen.fragment.mealfragment.MealMainFragment;
import com.anxin.kitchen.fragment.myfragment.MyMainFragment;
import com.anxin.kitchen.fragment.orderfragment.OrderMainFragment;
import com.anxin.kitchen.interface_.OnGivedPermissionListener;
import com.anxin.kitchen.user.R;
import com.anxin.kitchen.view.RequestLocationPermissionDialog;

/**
 * 主界面
 */
public class MainActivity extends FragmentActivity implements View.OnClickListener {
    private static final int BAIDU_READ_PHONE_STATE = 100;
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
    // 定义FragmentManager对象管理器
    private FragmentManager fragmentManager;
    //欢迎页面
    private RelativeLayout welcome_rlt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        context = this;
        requestLocationPermission();
        initView();//初始化界面控件
        setChioceItem(0);//初始化页面加载是显示点餐界面
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                welcome_rlt.setVisibility(View.GONE);
            }
        }, 2000);
    }

    private void requestLocationPermission() {
        boolean hasLocationPermission = getLocationPermission();
        if (!hasLocationPermission){
            popRequestWindow();
        }
    }

    //弹出请求权限框
    private void popRequestWindow() {
        RequestLocationPermissionDialog dialog = new RequestLocationPermissionDialog(this, new OnGivedPermissionListener() {
            @Override
            public void onGivedPermssion() {
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, BAIDU_READ_PHONE_STATE);
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
        welcome_rlt = (RelativeLayout) findViewById(R.id.welcome_bg);
        //初始化底部导航栏的控件
        myButtom_Lyt = (LinearLayout) findViewById(R.id.main_bottom_group);
        myButtomGroup = (RadioGroup) findViewById(R.id.bottom_radiogroup);
        mealButtomBtn = (RadioButton) findViewById(R.id.meal_radioBtn);
        groupButtomBtn = (RadioButton) findViewById(R.id.group_radioBtn);
        orderButtomBtn = (RadioButton) findViewById(R.id.order_radioBtn);
        myButtomBtn = (RadioButton) findViewById(R.id.my_radioBtn);
        myButtomGroup.setOnCheckedChangeListener(myButtomGroupCheck);
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
    }

    //显示底部栏
    public void showMainBotton() {
        myButtom_Lyt.setVisibility(View.VISIBLE);
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
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED
                ){
            return false;
        }else return true;
    }
}
