package com.anxin.kitchen.activity.order;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anxin.kitchen.adapter.OrderMemberAdapter;
import com.anxin.kitchen.bean.Order.OrderDetail;
import com.anxin.kitchen.response.OrderDetailResponse;
import com.anxin.kitchen.user.R;
import com.anxin.kitchen.utils.Cache;
import com.anxin.kitchen.utils.Constant;
import com.anxin.kitchen.utils.JsonHandler;
import com.anxin.kitchen.utils.SystemUtility;
import com.anxin.kitchen.utils.TimeUtil;
import com.anxin.kitchen.utils.ToastUtil;
import com.anxin.kitchen.view.MyGridView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xujianjun on 2018/4/7.
 */

public class OrderDetailActivity extends BaseOrderActivity implements View.OnClickListener {
    private final String NET_GET_ORDER_DETAIL = "getOrderDetail";
    private Activity mActivity;

    private ImageView ivBack;
    private TextView tvTitle;
    private TextView tvOrderDetailHint;
    private LinearLayout llOrderDetailInfo;
    private LinearLayout llOrderDetailPayInfo;
    private TextView tvOrderDetailPayMoney;
    private TextView tvOrderDetailMoneyOther;
    private LinearLayout llOrderDetailPay;

    private long mOrderId;
    private String token;

    private OrderDetail mOrderDetail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        mActivity = this;
        mOrderId = getIntent().getLongExtra("orderId", 0);
        initView();
        getOrderDetail();
    }

    private void initView() {
        ivBack = findViewById(R.id.back_img);
        tvTitle = findViewById(R.id.title_tv);
        tvOrderDetailHint = (TextView) findViewById(R.id.tv_order_detail_hint);
        llOrderDetailInfo = (LinearLayout) findViewById(R.id.ll_order_detail_info);
        llOrderDetailPayInfo = (LinearLayout) findViewById(R.id.ll_order_detail_pay_info);
        tvOrderDetailPayMoney = (TextView) findViewById(R.id.tv_order_detail_pay_money);
        tvOrderDetailMoneyOther = (TextView) findViewById(R.id.tv_order_detail_money_other);
        llOrderDetailPay = (LinearLayout) findViewById(R.id.ll_order_detail_pay);

        tvTitle.setText("订单详情");
        ivBack.setOnClickListener(this);
        llOrderDetailPay.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_img:
                finish();
                break;
            case R.id.ll_order_detail_pay:
                break;
            case R.id.ll_order_detail_suborder_title:
                TextView tvExpand = (TextView) view.getTag();
                LinearLayout llMember = (LinearLayout) tvExpand.getTag();
                if (llMember.getVisibility() == View.VISIBLE) {
                    tvExpand.setText("∨ 展开");
                    llMember.setVisibility(View.GONE);
                } else {
                    tvExpand.setText("∧ 收起");
                    llMember.setVisibility(View.VISIBLE);
                }
                break;
            default:
                break;
        }
    }

    private void setData(OrderDetail info) {
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
            tvOrderDetailPayMoney.setText(info.getUser().getMoney() + "");
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
        if (info.getGroup() == null) {
            ivOrderDetailType.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.icon_order_user));
        } else {
            ivOrderDetailType.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.icon_order_group));
            if (info.getGroup().getUserId() == info.getUser().getUserId()) {

            } else {

            }
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
        tvItemOrderAllPrice.setText("¥" + info.getUser().getMoney());

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
            tvOrderDetailDeliveryTime.setText(TimeUtil.getInstance().getNowTime(info.getUser().getDeliveryTime()));
        } else {
//            tvOrderDetailDeliveryAddress.setText(info.getGroup().getAddress());
//            tvOrderDetailDeliveryUserName.setText(info.getGroup().getContactName());
//            tvOrderDetailDeliveryUserPhone.setText(info.getGroup().getContactPhone());
            tvOrderDetailDeliveryTime.setText(TimeUtil.getInstance().getNowTime(info.getGroup().getDeliveryTime()));
            if (info.getGroup().getUserId() == info.getUser().getUserId()) {

            } else {

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
            tvOrderDetailOrderCreateTime.setText(TimeUtil.getInstance().getNowTime(info.getUser().getCreateTime()));
        } else {
            switch (info.getGroup().getStatus()) {
                case 1:
                    if (info.getGroup().getPayType() == 1) {
                        tvOrderDetailOrderStatus.setText("统一待付款");
                    } else {
                        tvOrderDetailOrderStatus.setText("AA待付款");
                    }

                    tvOrderDetailOrderStatus.setTextColor(mActivity.getResources().getColor(R.color.red));
                    break;
                case 2:
                    tvOrderDetailOrderStatus.setText("已取消");
                    tvOrderDetailOrderStatus.setTextColor(mActivity.getResources().getColor(R.color.tv_gray));
                    break;
                case 3:
                    tvOrderDetailOrderStatus.setText("自动取消");
                    tvOrderDetailOrderStatus.setTextColor(mActivity.getResources().getColor(R.color.tv_gray));
                    break;
                case 4:
                    tvOrderDetailOrderStatus.setText("已付款");
                    tvOrderDetailOrderStatus.setTextColor(mActivity.getResources().getColor(R.color.tv_gray));
                    break;
                default:
                    tvOrderDetailOrderStatus.setText("");
                    break;
            }
            tvOrderDetailOrderNo.setText(info.getGroup().getId() + "");
            switch (info.getGroup().getPayType()) {
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
            tvOrderDetailOrderCreateTime.setText(TimeUtil.getInstance().getNowTime(info.getGroup().getCreateTime()));
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
            MyGridView gvMenber = (MyGridView) viewMember.findViewById(R.id.gv_menber);
            llOrderDetailSuborderTitle.setOnClickListener(this);
            llOrderDetailSuborderTitle.setTag(tvOrderDetailSuborderExpand);
            tvOrderDetailSuborderExpand.setTag(llOrderDetailSuborderContent);
            OrderMemberAdapter memberAdapter = new OrderMemberAdapter(mActivity, info.getGroup().getOrderUsers());
            gvMenber.setAdapter(memberAdapter);

            llOrderDetailInfo.addView(viewMember);
        }


    }

    private void getOrderDetail() {
        if (null == token) {
            token = new Cache(mActivity).getAMToken();
        }
//        token = "C59B7F78953E2B894FBCFE12ED66E5D9";
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

    @Override
    public void requestSuccess(String responseBody, String requestCode) {
        if (requestCode.equals(NET_GET_ORDER_DETAIL)) {
            OrderDetailResponse response = JsonHandler.getHandler().getTarget(responseBody, OrderDetailResponse.class);
            mOrderDetail = response.getData();
            setData(mOrderDetail);
        }
    }

    @Override
    public void requestFailure(String responseFailureBody, String requestCode) {
        if (requestCode.equals(NET_GET_ORDER_DETAIL)) {
            ToastUtil.showToast(responseFailureBody);
        }
    }

}
