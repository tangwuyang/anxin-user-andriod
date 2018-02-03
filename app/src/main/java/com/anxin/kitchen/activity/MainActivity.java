package com.anxin.kitchen.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.anxin.kitchen.fragment.groupfragment.GroupMainFragment;
import com.anxin.kitchen.fragment.mealfragment.MealMainFragment;
import com.anxin.kitchen.fragment.myfragment.MyMainFragment;
import com.anxin.kitchen.fragment.orderfragment.OrderMainFragment;
import com.anxin.kitchen.user.R;

/**
 * 主界面
 */
public class MainActivity extends FragmentActivity implements View.OnClickListener {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        context = this;
        initView();//初始化界面控件
        setChioceItem(0);//初始化页面加载是显示点餐界面
    }

    private void initView() {
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

}
