package com.anxin.kitchen.fragment.orderfragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anxin.kitchen.activity.AllOrderActivity;
import com.anxin.kitchen.fragment.HomeBaseFragment;
import com.anxin.kitchen.user.R;
import com.anxin.kitchen.utils.Log;

/**
 * 订单主界面
 */

public class OrderMainFragment extends HomeBaseFragment implements View.OnClickListener {
    private Log LOG = Log.getLog();
    private View view;
    private TextView mAllOrderBtn, mAllOrderBtn2;//全部订单

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.order_main_fragment, null);
        initView();//初始化界面控制
        return view;
    }

    /**
     * 初始化控件，点击事件
     */
    private void initView() {
        mAllOrderBtn = (TextView) view.findViewById(R.id.allOrder_btn);
        mAllOrderBtn2 = (TextView) view.findViewById(R.id.allOrder_btn2);
        mAllOrderBtn.setOnClickListener(this);
        mAllOrderBtn2.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    /**
     * 点击事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.allOrder_btn://查看所有订单
            case R.id.allOrder_btn2:
                startActivity(new Intent(getActivity(), AllOrderActivity.class));
                break;
            default:
                break;
        }
    }

    /**
     * 初始化列表数据，正常由服务器返回的Json数据
     */
    private void initJsonData() {


    }
}
