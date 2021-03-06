package com.anxin.kitchen.fragment.orderfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.anxin.kitchen.MyApplication;
import com.anxin.kitchen.adapter.OrderAdapter;
import com.anxin.kitchen.fragment.BaseFragment;
import com.anxin.kitchen.response.OrderListResponse;
import com.anxin.kitchen.user.R;
import com.anxin.kitchen.utils.Cache;
import com.anxin.kitchen.utils.Constant;
import com.anxin.kitchen.utils.JsonHandler;
import com.anxin.kitchen.utils.SystemUtility;
import com.anxin.kitchen.utils.ToastUtil;
import com.anxin.kitchen.view.RefreshLayout;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

/**
 * 我的订单标签页---根据Type区分
 * Type -1全部  0未付款 1:已付款 2:已发货 3:完成 4:退款
 * Created by xujianjun on 2018/4/5.
 */

public class OrderFragment extends BaseFragment implements RefreshLayout.OnRefreshListener, RefreshLayout.OnLoadListener {

    private final String NET_REQUEST_GET_ORDER_LIST = "requestGetOrderList";
    private RefreshLayout refreshOrder;
    private ListView lvOrder;
    private RelativeLayout rlNoOrders;
    private OrderAdapter mAdapter;

    /**
     * -1:全部,0:未付款1:已付款2:已发货3:完成4:退款
     */
    private int mType;
    private int page = 1;
    private String token;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mType = getArguments().getInt("type", -1);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_order, null);
        initView();
        return view;
    }

    private void initView() {
        refreshOrder = (RefreshLayout) view.findViewById(R.id.refresh_order);
        lvOrder = (ListView) view.findViewById(R.id.lv_order);
        rlNoOrders = view.findViewById(R.id.rl_no_orders);

//        lvOrder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Order order = (Order) mAdapter.getItem(i);
//                Intent intent = new Intent(mActivity, OrderDetailActivity.class);
//                intent.putExtra("orderId", order.getUser().getId());
//                mActivity.startActivity(intent);
//            }
//        });


        refreshOrder.setOnLoadListener(this);
        refreshOrder.setOnRefreshListener(this);
        lvOrder.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
//                if (i == 0)
//                    refreshOrder.setEnabled(true);
//                else
//                    refreshOrder.setEnabled(false);
                boolean enable = false;
                if(lvOrder != null && lvOrder.getChildCount() > 0){
                    boolean firstItemVisible = lvOrder.getFirstVisiblePosition() == 0;
                    boolean topOfFirstItemVisible = lvOrder.getChildAt(0).getTop() == 0;
                    enable = firstItemVisible && topOfFirstItemVisible;
                }
                refreshOrder.setEnabled(enable);
            }
        });
//        getOrderList(page);
    }


    @Override
    public void onRefresh() {
        page = 1;
        getOrderList(page);
    }

    @Override
    public void onLoad() {
        if (mAdapter != null && mAdapter.getCount() >= page * 10) {
            page++;
            getOrderList(page);
        } else {
            refreshOrder.setLoading(false);
        }
    }

    private void getOrderList(int page) {
        if (null == token) {
            token = new Cache(mActivity).getAMToken();
        }
//        token = "C59B7F78953E2B894FBCFE12ED66E5D9";
        if (token == null) {
            SystemUtility.startLoginUser(mActivity);
        } else {
            String url = SystemUtility.getOrderListUrl();
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("page", page);
            dataMap.put("status", mType);
            dataMap.put("pageSize", 10);
            dataMap.put(Constant.TOKEN, token);
            requestNet(url, dataMap, NET_REQUEST_GET_ORDER_LIST);
        }
    }

    @Override
    public void requestSuccess(String responseBody, String requestCode) {
        if (requestCode.equals(NET_REQUEST_GET_ORDER_LIST)) {
            try {
                refreshOrder.setRefreshing(false);
                refreshOrder.setLoading(false);
            } catch (Exception e) {
                MobclickAgent.reportError(MyApplication.getInstance(), e);
            }
            OrderListResponse response = JsonHandler.getHandler().getTarget(responseBody, OrderListResponse.class);
            if (response == null || response.getData() == null || response.getData().getData() == null) {
                ToastUtil.showToast("没有更多了");
                return;
            }
            if (page == 1) {
                mAdapter = new OrderAdapter(mActivity, response.getData().getData());
                lvOrder.setAdapter(mAdapter);
                if (response.getData().getData().size() > 0) {

                    rlNoOrders.setVisibility(View.GONE);
                } else {
                    rlNoOrders.setVisibility(View.VISIBLE);
                }

            } else {
                mAdapter.upDate(response.getData().getData());
            }

        }
    }

    @Override
    public void requestFailure(String responseFailureBody, String requestCode) {
        if (requestCode.equals(NET_REQUEST_GET_ORDER_LIST)) {
            try {
                refreshOrder.setRefreshing(false);
                refreshOrder.setLoading(false);
            } catch (Exception e) {
                MobclickAgent.reportError(MyApplication.getInstance(), e);
            }

            if (page == 1) {
                rlNoOrders.setVisibility(View.VISIBLE);
            }
            if (page > 1) {
                page--;
            } else {
                page = 0;
            }
            ToastUtil.showToast(responseFailureBody);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("OrderFragment");
        onRefresh();
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("OrderFragment");
    }
}
