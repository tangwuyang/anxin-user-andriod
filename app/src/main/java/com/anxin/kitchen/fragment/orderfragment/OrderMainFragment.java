package com.anxin.kitchen.fragment.orderfragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.anxin.kitchen.MyApplication;
import com.anxin.kitchen.activity.LoginActivity;
import com.anxin.kitchen.activity.MainActivity;
import com.anxin.kitchen.activity.ViewKitchenActivity;
import com.anxin.kitchen.activity.order.OrderActivity;
import com.anxin.kitchen.activity.order.OrderDetailActivity;
import com.anxin.kitchen.adapter.OrderAdapter;
import com.anxin.kitchen.bean.Order.Order;
import com.anxin.kitchen.bean.OrderInfoBean;
import com.anxin.kitchen.fragment.HomeBaseFragment;
import com.anxin.kitchen.response.OrderNumResponse;
import com.anxin.kitchen.response.OrderResponse;
import com.anxin.kitchen.user.R;
import com.anxin.kitchen.utils.Cache;
import com.anxin.kitchen.utils.Constant;
import com.anxin.kitchen.utils.JsonHandler;
import com.anxin.kitchen.utils.Log;
import com.anxin.kitchen.utils.SharedPreferencesUtil;
import com.anxin.kitchen.utils.StringUtils;
import com.anxin.kitchen.utils.SystemUtility;
import com.anxin.kitchen.view.WaitingDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private TextView kitchen_View;//可视厨房


    private OrderAdapter mAdapter;
    String token;
    MainActivity activity;
    private WaitingDialog mWaitingDiag;
    private TextView pendingPaymentNumber;
    private TextView alreadyPaidNumber;
    private TextView completeNumber;
    private TextView refundNumber;
    private boolean isLoginResult = false;

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
        pendingPaymentNumber = (TextView) view.findViewById(R.id.pending_payment_number);
        alreadyPaidNumber = (TextView) view.findViewById(R.id.already_paid_number);
        completeNumber = (TextView) view.findViewById(R.id.complete_number);
        refundNumber = (TextView) view.findViewById(R.id.refund_number);
        kitchen_View = view.findViewById(R.id.kitchen_View);

        mAllOrderBtn.setOnClickListener(this);
        mAllOrderBtn2.setOnClickListener(this);
        kitchen_View.setOnClickListener(this);
        view.findViewById(R.id.pending_payment_rlt).setOnClickListener(this);
        view.findViewById(R.id.already_paid_rlt).setOnClickListener(this);
        view.findViewById(R.id.complete_rlt).setOnClickListener(this);
        view.findViewById(R.id.refund_rlt).setOnClickListener(this);

        //初始化ExpandlistView的id
        myOrderListView = (ListView) view.findViewById(R.id.myorder_listview);
        //初始化列表数据，正常由服务器返回的Json数据
        initJsonData();
//        myOrderAdaped = new MyOrderAdaped();
//        myOrderListView.setAdapter(myOrderAdaped);
        myOrderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Order order = (Order) mAdapter.getItem(i);
                Intent intent = new Intent(activity, OrderDetailActivity.class);
                intent.putExtra("orderId", order.getUser().getId());
                activity.startActivity(intent);
            }
        });

        activity = (MainActivity) getActivity();
//        mWaitingDiag = new WaitingDialog(activity);
//        mWaitingDiag.show();
//        mWaitingDiag.startAnimation();


    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (isLoginResult)
            return;
        getOrderData();
    }

    public void getOrderData() {
            token = new Cache(getActivity()).getAMToken();
//        token = "C59B7F78953E2B894FBCFE12ED66E5D9";
        if (token == null) {
            if (!SystemUtility.isForeground(getContext(), "com.anxin.kitchen.activity.LoginActivity")) {
                isLoginResult = false;
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.putExtra("tag", true);
                startActivityForResult(intent, 200);
            }
        } else {
            getRecenctOrders();
            getOrdersNum();
        }
    }

    /**
     * 点击事件
     */
    @Override
    public void onClick(View v) {
        if (null == token) {
            token = new Cache(activity).getAMToken();
        }
        if (token == null) {
            if (!SystemUtility.isForeground(getContext(), "com.anxin.kitchen.activity.LoginActivity")) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.putExtra("tag", true);
                startActivityForResult(intent, 200);
            }
            return;
        }
        switch (v.getId()) {
            case R.id.allOrder_btn://查看所有订单
            case R.id.allOrder_btn2:
                getActivity().startActivity(new Intent(getActivity(), OrderActivity.class));
//                Intent intent = new Intent(getActivity(),PayActivity.class);
//                intent.putExtra("payType",2);
//                intent.putExtra("payMoney",0.01);
//                getActivity().startActivity(intent);
                break;
            case R.id.pending_payment_rlt:
                Intent intent1 = new Intent(getActivity(), OrderActivity.class);
                intent1.putExtra("type", 1);
                getActivity().startActivity(intent1);
                break;
            case R.id.already_paid_rlt:
                Intent intent2 = new Intent(getActivity(), OrderActivity.class);
                intent2.putExtra("type", 2);
                getActivity().startActivity(intent2);
                break;
            case R.id.complete_rlt:
                Intent intent3 = new Intent(getActivity(), OrderActivity.class);
                intent3.putExtra("type", 3);
                getActivity().startActivity(intent3);
                break;
            case R.id.refund_rlt:
                Intent intent4 = new Intent(getActivity(), OrderActivity.class);
                intent4.putExtra("type", 4);
                getActivity().startActivity(intent4);
                break;
            case R.id.kitchen_View:
                Intent intent5 = new Intent(getActivity(), ViewKitchenActivity.class);
                getActivity().startActivity(intent5);
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

    //获取最近订单
    private void getRecenctOrders() {
        if (null == token) {
            token = new Cache(activity).getAMToken();
        }
        String url = SystemUtility.getRecenctOrdersUrl();
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put(Constant.TOKEN, token);
        activity.requestNet(url, dataMap, activity.NET_GET_RECENCT_ORDERS);
    }

    //获取订单数量
    private void getOrdersNum() {
        if (null == token) {
            token = new Cache(activity).getAMToken();
        }
        String url = SystemUtility.getOrdersNumUrl();
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("localTime", StringUtils.isEmpty(SharedPreferencesUtil.getInstance(activity).getSP("getOrderNumTime")) ? 0 : Long.parseLong(SharedPreferencesUtil.getInstance(activity).getSP("getOrderNumTime")));
        dataMap.put(Constant.TOKEN, token);
        activity.requestNet(url, dataMap, activity.NET_GET_ORDERS_NUM);
    }

    public void setGroup(String json) {
        if (null != mWaitingDiag && mWaitingDiag.isShowing()) {
            mWaitingDiag.stopAnimation();
            mWaitingDiag.dismiss();
        }
        OrderResponse response = JsonHandler.getHandler().getTarget(json, OrderResponse.class);
        if (response != null && response.getData() != null) {
            List<Order> list = new ArrayList<>();
            list.add(response.getData());
            mAdapter = new OrderAdapter(getActivity(), list);
            myOrderListView.setAdapter(mAdapter);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        LOG.e("--------------onActivityResult-------requestCode---------" + requestCode);
//        LOG.e("--------------onActivityResult-------resultCode---------" + resultCode);
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 188) {
            isLoginResult = true;
            return;
        }
        if (token == null) {
            token = MyApplication.getInstance().getCache().getAMToken();
        }
    }

    public void setNum(String json) {
        if (null != mWaitingDiag && mWaitingDiag.isShowing()) {
            mWaitingDiag.stopAnimation();
            mWaitingDiag.dismiss();
        }
        OrderNumResponse response = JsonHandler.getHandler().getTarget(json, OrderNumResponse.class);
        if (response.getData() != null) {
            int num1 = response.getData().get("0") == null ? 0 : response.getData().get("0");
            int num2 = response.getData().get("1") == null ? 0 : response.getData().get("1");
            int num3 = response.getData().get("3") == null ? 0 : response.getData().get("3");
            int num4 = response.getData().get("4") == null ? 0 : response.getData().get("4");
            if (num1 > 0) {
                pendingPaymentNumber.setVisibility(View.VISIBLE);
                if (num1 > 99) {
                    pendingPaymentNumber.setText("99+");
                } else {
                    pendingPaymentNumber.setText(num1 + "");
                }
            } else {
                pendingPaymentNumber.setVisibility(View.GONE);
            }

            if (num2 > 0) {
                alreadyPaidNumber.setVisibility(View.VISIBLE);
                if (num2 > 99) {
                    alreadyPaidNumber.setText("99+");
                } else {
                    alreadyPaidNumber.setText(num2 + "");
                }
            } else {
                alreadyPaidNumber.setVisibility(View.GONE);
            }

            if (num3 > 0) {
                completeNumber.setVisibility(View.VISIBLE);
                if (num3 > 99) {
                    completeNumber.setText("99+");
                } else {
                    completeNumber.setText(num3 + "");
                }
            } else {
                completeNumber.setVisibility(View.GONE);
            }

            if (num4 > 0) {
                refundNumber.setVisibility(View.VISIBLE);
                if (num4 > 99) {
                    refundNumber.setText("99+");
                } else {
                    refundNumber.setText(num4 + "");
                }
            } else {
                refundNumber.setVisibility(View.GONE);
            }
        }
    }
}
