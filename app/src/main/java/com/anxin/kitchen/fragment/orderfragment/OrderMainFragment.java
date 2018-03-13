package com.anxin.kitchen.fragment.orderfragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.anxin.kitchen.activity.AllOrderActivity;
import com.anxin.kitchen.bean.OrderInfoBean;
import com.anxin.kitchen.fragment.HomeBaseFragment;
import com.anxin.kitchen.user.R;
import com.anxin.kitchen.utils.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单主界面
 */

public class OrderMainFragment extends HomeBaseFragment implements View.OnClickListener {
    private Log LOG = Log.getLog();
    private View view;
    private TextView mAllOrderBtn, mAllOrderBtn2;//全部订单
    private List<OrderInfoBean> myOrderList = new ArrayList<>();//订单集合
    private MyOrderAdaped myOrderAdaped;//订单适配器
    private ListView myOrderListView;//订单列表

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
        mAllOrderBtn = (TextView) view.findViewById(R.id.allOrder_btn);//我的全部订单
        mAllOrderBtn2 = (TextView) view.findViewById(R.id.allOrder_btn2);//我的全部订单
        mAllOrderBtn.setOnClickListener(this);
        mAllOrderBtn2.setOnClickListener(this);

        //初始化ExpandlistView的id
        myOrderListView = (ListView) view.findViewById(R.id.myorder_listview);
        //初始化列表数据，正常由服务器返回的Json数据
        initJsonData();
        myOrderAdaped = new MyOrderAdaped();
        myOrderListView.setAdapter(myOrderAdaped);
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

    /**
     * 最近订单适配器
     */
    class MyOrderAdaped extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return myOrderList.size();
        }

        @Override
        public OrderInfoBean getItem(int position) {
            // TODO Auto-generated method stub
            return myOrderList.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View view = convertView;

            final ViewHolder holder;
            if (view == null) {
                holder = new ViewHolder();
                view = LayoutInflater.from(getActivity()).inflate(R.layout.order_item_center, null);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            return view;
        }

        class ViewHolder {
        }

    }
}
