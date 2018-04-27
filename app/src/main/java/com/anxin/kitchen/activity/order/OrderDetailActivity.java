package com.anxin.kitchen.activity.order;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anxin.kitchen.activity.MainActivity;
import com.anxin.kitchen.adapter.OrderMemberAdapter;
import com.anxin.kitchen.bean.Order.OrderDetail;
import com.anxin.kitchen.bean.Order.OrderUser;
import com.anxin.kitchen.response.BaseResponse;
import com.anxin.kitchen.response.OrderDetailResponse;
import com.anxin.kitchen.user.R;
import com.anxin.kitchen.utils.Cache;
import com.anxin.kitchen.utils.Constant;
import com.anxin.kitchen.utils.JsonHandler;
import com.anxin.kitchen.utils.SystemUtility;
import com.anxin.kitchen.utils.TimeUtil;
import com.anxin.kitchen.utils.ToastUtil;
import com.anxin.kitchen.view.MyGridView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xujianjun on 2018/4/7.
 */

public class OrderDetailActivity extends BaseOrderActivity implements View.OnClickListener {
    private final String NET_GET_ORDER_DETAIL = "getOrderDetail";
    private final String NET_CANCEL_ORDER = "cancelOrder";
    private Activity mActivity;

    private ImageView ivBack;
    private TextView tvTitle;
    private TextView tvCancel;
    private TextView tvOrderDetailHint;
    private LinearLayout llOrderDetailInfo;
    private LinearLayout llOrderDetailPayInfo;
    private TextView tvOrderDetailPayMoney;
    private TextView tvOrderDetailMoneyOther;
    private LinearLayout llOrderDetailPay;

    private MyGridView gvMenber;
    private OrderMemberAdapter mMemberAdapter;

    private long mOrderId;
    private String token;
    private int closeType = -1;
    private OrderDetail mOrderDetail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        mActivity = this;
        mOrderId = getIntent().getLongExtra("orderId", 0);
        closeType = getIntent().getIntExtra("closeType", 0);
        initView();
        getOrderDetail();
    }

    private void initView() {
        ivBack = findViewById(R.id.back_img);
        tvTitle = findViewById(R.id.title_tv);
        tvCancel = findViewById(R.id.complete_tv);
        tvOrderDetailHint = (TextView) findViewById(R.id.tv_order_detail_hint);
        llOrderDetailInfo = (LinearLayout) findViewById(R.id.ll_order_detail_info);
        llOrderDetailPayInfo = (LinearLayout) findViewById(R.id.ll_order_detail_pay_info);
        tvOrderDetailPayMoney = (TextView) findViewById(R.id.tv_order_detail_pay_money);
        tvOrderDetailMoneyOther = (TextView) findViewById(R.id.tv_order_detail_money_other);
        llOrderDetailPay = (LinearLayout) findViewById(R.id.ll_order_detail_pay);

        tvTitle.setText("订单详情");
        tvCancel.setText("取消");

        ivBack.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
        llOrderDetailPay.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK) { //表示按返回键 时的操作
                // 监听到返回按钮点击事件
                if (closeType != -1 && closeType == 1) {
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else
                    finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_img:
                if (closeType != -1 && closeType == 1) {
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else
                    finish();
                break;
            case R.id.complete_tv:
                cancelOrder();
                break;
            case R.id.ll_order_detail_pay:
                Intent intent = new Intent(mActivity, PayActivity.class);
                intent.putExtra("orderIds", mOrderDetail.getOrderId());
                intent.putExtra("makeType", mOrderDetail.getUser().getMakeType());
                mActivity.startActivity(intent);
                break;
            case R.id.ll_order_detail_suborder_title:
                TextView tvExpand = (TextView) view.getTag();
                LinearLayout llMember = (LinearLayout) tvExpand.getTag();
                if (tvExpand.getText().equals("∧ 收起")) {
                    tvExpand.setText("∨ 展开");
                    List<OrderUser> list = new ArrayList<>();
                    if (mOrderDetail.getGroup().getOrderUsers() != null && mOrderDetail.getGroup().getOrderUsers().size() > 5) {
                        list.add(mOrderDetail.getGroup().getOrderUsers().get(0));
                        list.add(mOrderDetail.getGroup().getOrderUsers().get(1));
                        list.add(mOrderDetail.getGroup().getOrderUsers().get(2));
                        list.add(mOrderDetail.getGroup().getOrderUsers().get(3));
                        list.add(mOrderDetail.getGroup().getOrderUsers().get(4));
                    } else {
                        list.addAll(mOrderDetail.getGroup().getOrderUsers());
                    }
                    if (mMemberAdapter != null && gvMenber != null) {
                        mMemberAdapter.update(list);
                    }
//                    llMember.setVisibility(View.GONE);
                } else {
                    tvExpand.setText("∧ 收起");
                    if (mMemberAdapter != null && gvMenber != null) {
                        mMemberAdapter.update(mOrderDetail.getGroup().getOrderUsers());
                    }
//                    llMember.setVisibility(View.VISIBLE);
                }
                break;
            default:
                break;
        }
    }

    private void setData(OrderDetail info) {
        if (info == null) {
            return;
        }
        if (info.getGroup() == null) {
            tvTitle.setText("订单详情");
        } else {
            if (info.getGroup().getPayType() == 1) {
                tvTitle.setText("订单详情(统一支付)");
            } else {
                tvTitle.setText("订单详情(AA支付)");
            }

        }
        llOrderDetailInfo.removeAllViews();
        if (info.getUser().getStatus() == 0) {
            tvOrderDetailHint.setVisibility(View.VISIBLE);
            tvOrderDetailHint.setText("请在" + TimeUtil.getInstance().getNowTimeSS(info.getLastPayTime()) + "前付款");
            llOrderDetailPayInfo.setVisibility(View.VISIBLE);
            if (info.isAllPay()) {
                //统一待付款
                tvOrderDetailPayMoney.setText(info.getGroup().getMoney() + "");
            } else {
                tvOrderDetailPayMoney.setText(info.getUser().getMoney() + "");
            }
            if (info.getUser().getTablewareDeposit() == 0) {
                tvOrderDetailMoneyOther.setVisibility(View.GONE);
            } else {
                tvOrderDetailMoneyOther.setVisibility(View.VISIBLE);
                tvOrderDetailMoneyOther.setText("其中餐具押金：" + info.getUser().getTablewareDeposit() + "元");
            }
        } else {
            llOrderDetailPayInfo.setVisibility(View.GONE);
            tvOrderDetailHint.setVisibility(View.GONE);
        }

        //套餐信息
        View viewPackageInfo = LayoutInflater.from(mActivity).inflate(R.layout.item_order_detail_group_order_info, null);
        ImageView ivOrderDetailType = (ImageView) viewPackageInfo.findViewById(R.id.iv_order_detail_type);

        if (info.getUser().getMakeType() != 1) {
            ivOrderDetailType.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.icon_order_user));
        } else {
            ivOrderDetailType.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.icon_order_group));
        }
        TextView tvItemOrderTablewareName = (TextView) viewPackageInfo.findViewById(R.id.tv_item_order_tableware_name);
        TextView tvItemOrderTablewareDeposit = (TextView) viewPackageInfo.findViewById(R.id.tv_item_order_tableware_deposit);
        TextView tvItemOrderTablewareNum = (TextView) viewPackageInfo.findViewById(R.id.tv_item_order_tableware_num);
        TextView tvItemOrderTablewarePrice = (TextView) viewPackageInfo.findViewById(R.id.tv_item_order_tableware_price);
        TextView tvItemOrderDeliveryNum = (TextView) viewPackageInfo.findViewById(R.id.tv_item_order_delivery_num);
        TextView tvItemOrderDeliveryPrice = (TextView) viewPackageInfo.findViewById(R.id.tv_item_order_delivery_price);
        TextView tvItemOrderAllPrice = (TextView) viewPackageInfo.findViewById(R.id.tv_item_order_all_price);
        tvItemOrderTablewareName.setText(info.getUser().getTablewareName() + "(押金)");
        tvItemOrderTablewareDeposit.setText("¥" + info.getUser().getTablewareDeposit());
        tvItemOrderTablewareNum.setText("x" + info.getUser().getNum());
        tvItemOrderTablewarePrice.setText("¥" + info.getUser().getTablewareFee());
//        tvItemOrderDeliveryNum.setText();
        tvItemOrderDeliveryPrice.setText("¥" + info.getUser().getDeliveryFee());
        if (info.isAllPay()) {
            tvItemOrderAllPrice.setText("¥" + info.getGroup().getMoney());
        } else {
            tvItemOrderAllPrice.setText("¥" + info.getUser().getMoney());
        }

        //套餐详细列表
        LinearLayout llPackageInfo = viewPackageInfo.findViewById(R.id.ll_order_package_info);
        llPackageInfo.removeAllViews();
        if (info.getUser().getPackageList() != null) {
            for (int i = 0; i < info.getUser().getPackageList().size(); i++) {
                View viewPackage = LayoutInflater.from(mActivity).inflate(R.layout.item_order_package, null);
                TextView tvPackageName = (TextView) viewPackage.findViewById(R.id.tv_item_order_package_name);
                TextView tvPackageOther = (TextView) viewPackage.findViewById(R.id.tv_item_order_package_other);
                TextView tvPackageNum = (TextView) viewPackage.findViewById(R.id.tv_item_order_package_num);
                TextView tvPackagePrice = (TextView) viewPackage.findViewById(R.id.tv_item_order_package_price);
                tvPackageName.setText(info.getUser().getPackageList().get(i).getPackageName());
                tvPackageOther.setText(info.getUser().getPackageList().get(i).getFoods().replaceAll(",", "\n"));
                tvPackageNum.setText("x" + info.getUser().getPackageList().get(i).getNum());
                tvPackagePrice.setText("¥" + info.getUser().getPackageList().get(i).getSubtotal());

                llPackageInfo.addView(viewPackage);
            }
            llOrderDetailInfo.addView(viewPackageInfo);
        }
        //配送信息
        View viewDelivery = LayoutInflater.from(mActivity).inflate(R.layout.item_order_detail_delivery_info, null);
        TextView tvOrderDetailDeliveryAddress = (TextView) viewDelivery.findViewById(R.id.tv_order_detail_delivery_address);
        TextView tvOrderDetailDeliveryUserName = (TextView) viewDelivery.findViewById(R.id.tv_order_detail_delivery_user_name);
        TextView tvOrderDetailDeliveryUserPhone = (TextView) viewDelivery.findViewById(R.id.tv_order_detail_delivery_user_phone);
        TextView tvOrderDetailDeliveryTime = (TextView) viewDelivery.findViewById(R.id.tv_order_detail_delivery_time);
        tvOrderDetailDeliveryAddress.setText(info.getContact().getAddress());
        tvOrderDetailDeliveryUserName.setText(info.getContact().getContactName());
        tvOrderDetailDeliveryUserPhone.setText(info.getContact().getContactPhone());
//        tvOrderDetailDeliveryTime.setText(TimeUtil.getInstance().getNowTime(info.getContact().getDeliveryTime()));
        if (info.getGroup() == null) {
//            tvOrderDetailDeliveryAddress.setText(info.getUser().getAddress());
//            tvOrderDetailDeliveryUserName.setText(info.getUser().getContactName());
//            tvOrderDetailDeliveryUserPhone.setText(info.getUser().getContactPhone());
            if (info.getUser().getDeliveryTime() > 0) {
                tvOrderDetailDeliveryTime.setText(TimeUtil.getInstance().getNowTimeSS(info.getUser().getDeliveryTime()));
            } else {
                tvOrderDetailDeliveryTime.setText("暂未配送");
            }

        } else {
//            tvOrderDetailDeliveryAddress.setText(info.getGroup().getAddress());
//            tvOrderDetailDeliveryUserName.setText(info.getGroup().getContactName());
//            tvOrderDetailDeliveryUserPhone.setText(info.getGroup().getContactPhone());
            if (info.getGroup().getDeliveryTime() > 0) {
                tvOrderDetailDeliveryTime.setText(TimeUtil.getInstance().getNowTimeSS(info.getGroup().getDeliveryTime()));
            } else {
                tvOrderDetailDeliveryTime.setText("暂未配送");
            }

        }

        llOrderDetailInfo.addView(viewDelivery);
        //订单信息
        View viewOrder = LayoutInflater.from(mActivity).inflate(R.layout.item_order_detail_order_info, null);
        TextView tvOrderDetailOrderStatus = (TextView) viewOrder.findViewById(R.id.tv_order_detail_order_status);
        TextView tvOrderDetailOrderNo = (TextView) viewOrder.findViewById(R.id.tv_order_detail_order_no);
        TextView tvOrderDetailOrderPayType = (TextView) viewOrder.findViewById(R.id.tv_order_detail_order_pay_type);
        TextView tvOrderDetailOrderCreateTime = (TextView) viewOrder.findViewById(R.id.tv_order_detail_order_create_time);
        if (info.getGroup() == null) {
            switch (info.getUser().getStatus()) {
                case 0:
                    tvOrderDetailOrderStatus.setText("待付款");
                    tvOrderDetailOrderStatus.setTextColor(mActivity.getResources().getColor(R.color.red));
                    tvCancel.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    tvOrderDetailOrderStatus.setText("已付款");
                    tvOrderDetailOrderStatus.setTextColor(mActivity.getResources().getColor(R.color.tv_gray));
                    break;
                case 2:
                    tvOrderDetailOrderStatus.setText("已发货");
                    tvOrderDetailOrderStatus.setTextColor(mActivity.getResources().getColor(R.color.tv_gray));
                    break;
                case 3:
                    tvOrderDetailOrderStatus.setText("完成");
                    tvOrderDetailOrderStatus.setTextColor(mActivity.getResources().getColor(R.color.tv_gray));
                    break;
                case 4:
                    tvOrderDetailOrderStatus.setText("退款");
                    tvOrderDetailOrderStatus.setTextColor(mActivity.getResources().getColor(R.color.tv_gray));
                    break;
                case 5:
                    tvOrderDetailOrderStatus.setText("取消");
                    tvOrderDetailOrderStatus.setTextColor(mActivity.getResources().getColor(R.color.tv_gray));
                    break;
                default:
                    tvOrderDetailOrderStatus.setText("");
                    break;
            }
            tvOrderDetailOrderNo.setText(info.getUser().getId() + "");
            if (info.getUser().getStatus() == 0) {
                tvOrderDetailOrderPayType.setText("");
            } else {
                switch (info.getUser().getPayType()) {
                    case 1:
                        tvOrderDetailOrderPayType.setText("微信支付");
                        break;
                    case 2:
                        tvOrderDetailOrderPayType.setText("支付宝支付");
                        break;
                    default:
                        tvOrderDetailOrderPayType.setText("");
                        break;
                }
            }

            tvOrderDetailOrderCreateTime.setText(TimeUtil.getInstance().getNowTimeSS(info.getUser().getCreateTime()));
        } else {
            switch (info.getGroup().getStatus()) {
                case 0:
                    if (info.getGroup().getPayType() == 1) {
                        tvOrderDetailOrderStatus.setText("统一待付款");
                    } else {
                        tvOrderDetailOrderStatus.setText("AA待付款");
                    }
                    tvOrderDetailOrderStatus.setTextColor(mActivity.getResources().getColor(R.color.red));
                    tvCancel.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    tvOrderDetailOrderStatus.setText("已付款");
                    tvOrderDetailOrderStatus.setTextColor(mActivity.getResources().getColor(R.color.tv_gray));
                    break;
                case 2:
                    tvOrderDetailOrderStatus.setText("已发货");
                    tvOrderDetailOrderStatus.setTextColor(mActivity.getResources().getColor(R.color.tv_gray));
                    break;
                case 3:
                    tvOrderDetailOrderStatus.setText("已完成");
                    tvOrderDetailOrderStatus.setTextColor(mActivity.getResources().getColor(R.color.tv_gray));
                    break;
                case 4:
                    tvOrderDetailOrderStatus.setText("已取消");
                    tvOrderDetailOrderStatus.setTextColor(mActivity.getResources().getColor(R.color.tv_gray));
                    break;

                default:
                    tvOrderDetailOrderStatus.setText("");
                    break;
            }
            tvOrderDetailOrderNo.setText(info.getGroup().getId() + "");
            if (info.getUser().getStatus() == 0) {
                tvOrderDetailOrderPayType.setText("");
            } else {
                switch (info.getUser().getPayType()) {
                    case 1:
                        tvOrderDetailOrderPayType.setText("微信支付");
                        break;
                    case 2:
                        tvOrderDetailOrderPayType.setText("支付宝支付");
                        break;
                    default:
                        tvOrderDetailOrderPayType.setText("");
                        break;
                }
            }

            tvOrderDetailOrderCreateTime.setText(TimeUtil.getInstance().getNowTimeSS(info.getGroup().getCreateTime()));
            if (info.getGroup().getUserId() == info.getUser().getUserId()) {

            } else {

            }
        }
        llOrderDetailInfo.addView(viewOrder);
        //组团信息
        if (info.getGroup() != null && info.getGroup().getPayType() == 2 && info.getGroup().getOrderUsers() != null && info.getGroup().getOrderUsers().size() > 0) {
            View viewMember = LayoutInflater.from(mActivity).inflate(R.layout.item_order_detail_suborder_info, null);
            LinearLayout llOrderDetailSuborderTitle = (LinearLayout) viewMember.findViewById(R.id.ll_order_detail_suborder_title);
            TextView tvOrderDetailSuborderExpand = (TextView) viewMember.findViewById(R.id.tv_order_detail_suborder_expand);
            LinearLayout llOrderDetailSuborderContent = (LinearLayout) viewMember.findViewById(R.id.ll_order_detail_suborder_content);
            gvMenber = (MyGridView) viewMember.findViewById(R.id.gv_menber);
            llOrderDetailSuborderTitle.setOnClickListener(this);
            llOrderDetailSuborderTitle.setTag(tvOrderDetailSuborderExpand);
            tvOrderDetailSuborderExpand.setTag(llOrderDetailSuborderContent);
            List<OrderUser> list = new ArrayList<>();
            if (info.getGroup().getOrderUsers() != null && info.getGroup().getOrderUsers().size() > 5) {
                list.add(info.getGroup().getOrderUsers().get(0));
                list.add(info.getGroup().getOrderUsers().get(1));
                list.add(info.getGroup().getOrderUsers().get(2));
                list.add(info.getGroup().getOrderUsers().get(3));
                list.add(info.getGroup().getOrderUsers().get(4));
            } else {
                list.addAll(info.getGroup().getOrderUsers());
            }
            mMemberAdapter = new OrderMemberAdapter(mActivity, list);
            gvMenber.setAdapter(mMemberAdapter);

            llOrderDetailInfo.addView(viewMember);
        }


    }

    private void getOrderDetail() {
        if (null == token) {
            token = new Cache(mActivity).getAMToken();
        }
        if (token == null) {
            SystemUtility.startLoginUser(mActivity);
        } else {
            String url = SystemUtility.getOrderDetailUrl();
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("orderId", mOrderId);
            dataMap.put(Constant.TOKEN, token);
            requestNet(url, dataMap, NET_GET_ORDER_DETAIL);
        }
    }

    private void cancelOrder() {
        if (null == token) {
            token = new Cache(mActivity).getAMToken();
        }
        if (token == null) {
            SystemUtility.startLoginUser(mActivity);
        } else {
            String url = SystemUtility.cancelOrder();
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("orderId", mOrderId);
            dataMap.put(Constant.TOKEN, token);
            requestNet(url, dataMap, NET_CANCEL_ORDER);
        }
    }


    @Override
    public void requestSuccess(String responseBody, String requestCode) {
        if (requestCode.equals(NET_GET_ORDER_DETAIL)) {
            OrderDetailResponse response = JsonHandler.getHandler().getTarget(responseBody, OrderDetailResponse.class);
            mOrderDetail = response.getData();
            setData(mOrderDetail);
        } else if (requestCode.equals(NET_CANCEL_ORDER)) {
            BaseResponse response = JsonHandler.getHandler().getTarget(responseBody, BaseResponse.class);
            if (response.getCode() == 1) {
                ToastUtil.showToast(response.getMessage());
                finish();
            } else {
                ToastUtil.showToast(response.getMessage());
            }
        }
    }

    @Override
    public void requestFailure(String responseFailureBody, String requestCode) {
        if (requestCode.equals(NET_GET_ORDER_DETAIL)) {
            ToastUtil.showToast(responseFailureBody);
        } else if (requestCode.equals(NET_CANCEL_ORDER)) {
            ToastUtil.showToast(responseFailureBody);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mOrderId = getIntent().getLongExtra("orderId", 0);
        getOrderDetail();
    }

    //    @Override
//    protected void onResume() {
//        super.onResume();
//        getOrderDetail();
//    }


}
