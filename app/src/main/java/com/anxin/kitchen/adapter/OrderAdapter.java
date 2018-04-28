package com.anxin.kitchen.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anxin.kitchen.activity.order.OrderDetailActivity;
import com.anxin.kitchen.activity.order.PayActivity;
import com.anxin.kitchen.bean.Order.Order;
import com.anxin.kitchen.bean.Order.PackageOrder;
import com.anxin.kitchen.user.R;
import com.anxin.kitchen.utils.TimeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的订单适配器
 * Created by xujianjun on 2018/4/5.
 */

public class OrderAdapter extends BaseAdapter {
    private Activity mActivity;
    private List<Order> mList;
    private LayoutInflater mInflater;

    private final int TYPE_USER = 0;
    private final int TYPE_GROUP_LEADER = 1;
    private final int TYPE_GROUP_MEMBER = 2;


    public OrderAdapter(Activity mActivity, List<Order> mList) {
        this.mActivity = mActivity;
        this.mList = mList;
        this.mInflater = LayoutInflater.from(mActivity);
    }

    @Override
    public int getItemViewType(int type) {
        if (type == TYPE_USER) {
            return TYPE_USER;
        } else if (type == TYPE_GROUP_LEADER) {
            return TYPE_GROUP_LEADER;
        } else {
            return TYPE_GROUP_MEMBER;
        }
    }


    @Override
    public int getViewTypeCount() {
        return 5;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        HolderUser holderUser = null;
        HolderGroupLeader holderGroupLeader = null;
        HolderGroupRember holderGroupRember = null;
        int type = getItemViewType(getDataType(mList.get(i)));
        if (view == null) {
            switch (type) {
                case TYPE_USER:
                    view = mInflater.inflate(R.layout.adapter_order_user, null);
                    holderUser = new HolderUser(view);
                    view.setTag(holderUser);
                    break;
                case TYPE_GROUP_LEADER:
                    view = mInflater.inflate(R.layout.adapter_order_group_leader, null);
                    holderGroupLeader = new HolderGroupLeader(view);
                    view.setTag(holderGroupLeader);
                    break;
                case TYPE_GROUP_MEMBER:
                    view = mInflater.inflate(R.layout.adapter_order_group_member, null);
                    holderGroupRember = new HolderGroupRember(view);
                    view.setTag(holderGroupRember);
                    break;
            }
        } else {
            switch (type) {
                case TYPE_USER:
                    if(view.getTag() instanceof  HolderUser){
                        holderUser = (HolderUser) view.getTag();
                    }else{
                        view = mInflater.inflate(R.layout.adapter_order_user, null);
                        holderUser = new HolderUser(view);
                        view.setTag(holderUser);
                    }

                    break;
                case TYPE_GROUP_LEADER:
                    if(view.getTag() instanceof  HolderGroupLeader){
                        holderGroupLeader = (HolderGroupLeader) view.getTag();
                    }else{
                        view = mInflater.inflate(R.layout.adapter_order_group_leader, null);
                        holderGroupLeader = new HolderGroupLeader(view);
                        view.setTag(holderGroupLeader);
                    }
                    break;
                case TYPE_GROUP_MEMBER:
                    if(view.getTag() instanceof  HolderGroupRember){
                        holderGroupRember = (HolderGroupRember) view.getTag();
                    }else{
                        view = mInflater.inflate(R.layout.adapter_order_group_member, null);
                        holderGroupRember = new HolderGroupRember(view);
                        view.setTag(holderGroupRember);
                    }

                    break;
            }

        }


        switch (type) {
            case TYPE_USER:
                holderUser.tvOrderUserOrderNum.setText(mList.get(i).getUser().getId() + "");
                if (mList.get(i).getUser().getDeliveryTime() == 0) {
                    holderUser.tvOrderUserOrderTime.setText("暂未配送");
                } else {
                    holderUser.tvOrderUserOrderTime.setText("送餐时间：" + TimeUtil.getInstance().getNowTime(mList.get(i).getUser().getDeliveryTime()));
                }
                switch (mList.get(i).getUser().getStatus()) {
                    case 0:
                        holderUser.tvOrderUserOrderStatus.setText("待付款");
                        holderUser.tvOrderUserOrderStatus.setTextColor(mActivity.getResources().getColor(R.color.red));
                        holderUser.tvOrderUserOrderPay.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        holderUser.tvOrderUserOrderStatus.setText("已付款");
                        holderUser.tvOrderUserOrderStatus.setTextColor(mActivity.getResources().getColor(R.color.tv_gray));
                        holderUser.tvOrderUserOrderPay.setVisibility(View.GONE);
                        break;
                    case 2:
                        holderUser.tvOrderUserOrderStatus.setText("已发货");
                        holderUser.tvOrderUserOrderStatus.setTextColor(mActivity.getResources().getColor(R.color.tv_gray));
                        holderUser.tvOrderUserOrderPay.setVisibility(View.GONE);
                        break;
                    case 3:
                        holderUser.tvOrderUserOrderStatus.setText("完成");
                        holderUser.tvOrderUserOrderStatus.setTextColor(mActivity.getResources().getColor(R.color.tv_gray));
                        holderUser.tvOrderUserOrderPay.setVisibility(View.GONE);
                        break;
                    case 4:
                        holderUser.tvOrderUserOrderStatus.setText("退款");
                        holderUser.tvOrderUserOrderStatus.setTextColor(mActivity.getResources().getColor(R.color.tv_gray));
                        holderUser.tvOrderUserOrderPay.setVisibility(View.GONE);
                        break;
                    case 5:
                        holderUser.tvOrderUserOrderStatus.setText("取消");
                        holderUser.tvOrderUserOrderStatus.setTextColor(mActivity.getResources().getColor(R.color.tv_gray));
                        holderUser.tvOrderUserOrderPay.setVisibility(View.GONE);
                        break;
                    default:
                        holderUser.tvOrderUserOrderStatus.setText("");
                        holderUser.tvOrderUserOrderPay.setVisibility(View.GONE);
                        break;
                }
                holderUser.tvItemOrderTablewareName.setText(mList.get(i).getUser().getTablewareName() + "(押金)");
                setPackageData(holderUser.llOrderPackageInfo, mList.get(i).getUser().getPackageList());
                holderUser.tvItemOrderTablewareDeposit.setText("¥" + mList.get(i).getUser().getTablewareDeposit());
                holderUser.tvItemOrderTablewareNum.setText("x" + mList.get(i).getUser().getNum());
                holderUser.tvItemOrderTablewarePrice.setText("¥" + mList.get(i).getUser().getTablewareFee());
                holderUser.tvItemOrderDeliveryPrice.setText("¥" + mList.get(i).getUser().getDeliveryFee());
                holderUser.tvItemOrderAllPrice.setText("¥" + mList.get(i).getUser().getMoney());

                holderUser.llOrderUser.setOnClickListener(new MyClickListener(i));
                holderUser.tvOrderUserOrderPay.setOnClickListener(new MyClickListener(i));

                break;
            case TYPE_GROUP_LEADER:
                holderGroupLeader.tvOrderNo.setText(mList.get(i).getGroup().getId() + "");
                holderGroupLeader.tvOrderTime.setText("下单时间：" + TimeUtil.getInstance().getNowTime(mList.get(i).getGroup().getCreateTime()));
                if (mList.get(i).getUser().getDeliveryTime() == 0) {
                    holderGroupLeader.tvOrderSendTime.setText("暂未配送");
                } else {
                    holderGroupLeader.tvOrderSendTime.setText("送餐时间：" + TimeUtil.getInstance().getNowTime(mList.get(i).getUser().getDeliveryTime()));
                }
                switch (mList.get(i).getGroup().getStatus()) {
                    case 0:
                        if (mList.get(i).getGroup().getPayType() == 1) {
                            holderGroupLeader.tvOrderStatus.setText("统一待付款");
                        } else {
                            holderGroupLeader.tvOrderStatus.setText("AA待付款");
                        }
                        holderGroupLeader.tvOrderUserOrderStatus.setTextColor(mActivity.getResources().getColor(R.color.red));
                        break;
                    case 1:
                        holderGroupLeader.tvOrderStatus.setText("已付款");
                        holderGroupLeader.tvOrderUserOrderStatus.setTextColor(mActivity.getResources().getColor(R.color.tv_gray));
                        break;
                    case 2:
                        holderGroupLeader.tvOrderStatus.setText("已发货");
                        holderGroupLeader.tvOrderUserOrderStatus.setTextColor(mActivity.getResources().getColor(R.color.tv_gray));
                        break;
                    case 3:
                        holderGroupLeader.tvOrderStatus.setText("已完成");
                        holderGroupLeader.tvOrderUserOrderStatus.setTextColor(mActivity.getResources().getColor(R.color.tv_gray));
                        break;
                    case 4:
                        holderGroupLeader.tvOrderStatus.setText("已取消");
                        holderGroupLeader.tvOrderUserOrderStatus.setTextColor(mActivity.getResources().getColor(R.color.tv_gray));
                        break;

                    default:
                        holderGroupLeader.tvOrderStatus.setText("");
                        break;
                }

                holderGroupLeader.tvOrderUserOrderNum.setText(mList.get(i).getUser().getId() + "");
                if (mList.get(i).getUser().getDeliveryTime() == 0) {
                    holderGroupLeader.tvOrderUserOrderTime.setText("暂未配送");
                } else {
                    holderGroupLeader.tvOrderUserOrderTime.setText("下单时间：" + TimeUtil.getInstance().getNowTime(mList.get(i).getUser().getCreateTime()));
                }
                switch (mList.get(i).getUser().getStatus()) {
                    case 0:
                        holderGroupLeader.tvOrderUserOrderStatus.setText("待付款");
                        holderGroupLeader.tvOrderUserOrderStatus.setTextColor(mActivity.getResources().getColor(R.color.red));
                        holderGroupLeader.tvOrderUserOrderPay.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        holderGroupLeader.tvOrderUserOrderStatus.setText("已付款");
                        holderGroupLeader.tvOrderUserOrderStatus.setTextColor(mActivity.getResources().getColor(R.color.tv_gray));
                        holderGroupLeader.tvOrderUserOrderPay.setVisibility(View.GONE);
                        break;
                    case 2:
                        holderGroupLeader.tvOrderUserOrderStatus.setText("已发货");
                        holderGroupLeader.tvOrderUserOrderStatus.setTextColor(mActivity.getResources().getColor(R.color.tv_gray));
                        holderGroupLeader.tvOrderUserOrderPay.setVisibility(View.GONE);
                        break;
                    case 3:
                        holderGroupLeader.tvOrderUserOrderStatus.setText("完成");
                        holderGroupLeader.tvOrderUserOrderStatus.setTextColor(mActivity.getResources().getColor(R.color.tv_gray));
                        holderGroupLeader.tvOrderUserOrderPay.setVisibility(View.GONE);
                        break;
                    case 4:
                        holderGroupLeader.tvOrderUserOrderStatus.setText("退款");
                        holderGroupLeader.tvOrderUserOrderStatus.setTextColor(mActivity.getResources().getColor(R.color.tv_gray));
                        holderGroupLeader.tvOrderUserOrderPay.setVisibility(View.GONE);
                        break;
                    case 5:
                        holderGroupLeader.tvOrderUserOrderStatus.setText("取消");
                        holderGroupLeader.tvOrderUserOrderStatus.setTextColor(mActivity.getResources().getColor(R.color.tv_gray));
                        holderGroupLeader.tvOrderUserOrderPay.setVisibility(View.GONE);
                        break;
                    default:
                        holderGroupLeader.tvOrderUserOrderStatus.setText("");
                        holderGroupLeader.tvOrderUserOrderPay.setVisibility(View.GONE);
                        break;
                }
                setPackageData(holderGroupLeader.llOrderPackageInfo, mList.get(i).getUser().getPackageList());
                holderGroupLeader.tvItemOrderTablewareName.setText(mList.get(i).getUser().getTablewareName() + "(押金)");
                holderGroupLeader.tvItemOrderTablewareDeposit.setText("¥" + mList.get(i).getUser().getTablewareDeposit());
                holderGroupLeader.tvItemOrderTablewareNum.setText("x" + mList.get(i).getUser().getNum());
                holderGroupLeader.tvItemOrderTablewarePrice.setText("¥" + mList.get(i).getUser().getTablewareFee());
                holderGroupLeader.tvItemOrderDeliveryPrice.setText("¥" + mList.get(i).getUser().getDeliveryFee());

                if (mList.get(i).getGroup().getPayType() == 1) {
                    //统一支付
                    holderGroupLeader.tvItemOrderAllPrice.setText("¥" + mList.get(i).getGroup().getMoney());
                } else {
                    //AA支付
                    holderGroupLeader.tvItemOrderAllPrice.setText("¥" + mList.get(i).getUser().getMoney());
                }

                holderGroupLeader.llOrderGroup.setOnClickListener(new MyClickListener(i));
                holderGroupLeader.tvOrderUserOrderPay.setOnClickListener(new MyClickListener(i));


                break;
            case TYPE_GROUP_MEMBER:
                holderGroupRember.tvOrderNo.setText(mList.get(i).getGroup().getId() + "");
                holderGroupRember.tvOrderTime.setText("下单时间：" + TimeUtil.getInstance().getNowTime(mList.get(i).getGroup().getCreateTime()));
                if (mList.get(i).getUser().getDeliveryTime() == 0) {
                    holderGroupRember.tvOrderSendTime.setText("暂未配送");
                } else {
                    holderGroupRember.tvOrderSendTime.setText("送餐时间：" + TimeUtil.getInstance().getNowTime(mList.get(i).getUser().getDeliveryTime()));
                }
                switch (mList.get(i).getGroup().getStatus()) {
                    case 0:
                        if (mList.get(i).getGroup().getPayType() == 1) {
                            holderGroupRember.tvOrderStatus.setText("统一待付款");
                        } else {
                            holderGroupRember.tvOrderStatus.setText("AA待付款");
                        }
                        holderGroupRember.tvOrderUserOrderStatus.setTextColor(mActivity.getResources().getColor(R.color.red));
                        break;
                    case 1:
                        holderGroupRember.tvOrderStatus.setText("已付款");
                        holderGroupRember.tvOrderUserOrderStatus.setTextColor(mActivity.getResources().getColor(R.color.tv_gray));
                        break;
                    case 2:
                        holderGroupRember.tvOrderStatus.setText("已发货");
                        holderGroupRember.tvOrderUserOrderStatus.setTextColor(mActivity.getResources().getColor(R.color.tv_gray));
                        break;
                    case 3:
                        holderGroupRember.tvOrderStatus.setText("已完成");
                        holderGroupRember.tvOrderUserOrderStatus.setTextColor(mActivity.getResources().getColor(R.color.tv_gray));
                        break;
                    case 4:
                        holderGroupRember.tvOrderStatus.setText("已取消");
                        holderGroupRember.tvOrderUserOrderStatus.setTextColor(mActivity.getResources().getColor(R.color.tv_gray));
                        break;

                    default:
                        holderGroupRember.tvOrderStatus.setText("");
                        break;
                }

                holderGroupRember.tvOrderUserOrderNum.setText(mList.get(i).getUser().getId() + "");
                if (mList.get(i).getUser().getDeliveryTime() == 0) {
                    holderGroupRember.tvOrderUserOrderTime.setText("暂未配送");
                } else {
                    holderGroupRember.tvOrderUserOrderTime.setText("下单时间：" + TimeUtil.getInstance().getNowTime(mList.get(i).getUser().getCreateTime()));
                }
                switch (mList.get(i).getUser().getStatus()) {
                    case 0:
                        holderGroupRember.tvOrderUserOrderStatus.setText("未付款");
                        holderGroupRember.tvOrderUserOrderStatus.setTextColor(mActivity.getResources().getColor(R.color.red));
                        holderGroupRember.tvOrderUserOrderPay.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        holderGroupRember.tvOrderUserOrderStatus.setText("已付款");
                        holderGroupRember.tvOrderUserOrderStatus.setTextColor(mActivity.getResources().getColor(R.color.tv_gray));
                        holderGroupRember.tvOrderUserOrderPay.setVisibility(View.GONE);
                        break;
                    case 2:
                        holderGroupRember.tvOrderUserOrderStatus.setText("已发货");
                        holderGroupRember.tvOrderUserOrderStatus.setTextColor(mActivity.getResources().getColor(R.color.tv_gray));
                        holderGroupRember.tvOrderUserOrderPay.setVisibility(View.GONE);
                        break;
                    case 3:
                        holderGroupRember.tvOrderUserOrderStatus.setText("完成");
                        holderGroupRember.tvOrderUserOrderStatus.setTextColor(mActivity.getResources().getColor(R.color.tv_gray));
                        holderGroupRember.tvOrderUserOrderPay.setVisibility(View.GONE);
                        break;
                    case 4:
                        holderGroupRember.tvOrderUserOrderStatus.setText("退款");
                        holderGroupRember.tvOrderUserOrderStatus.setTextColor(mActivity.getResources().getColor(R.color.tv_gray));
                        holderGroupRember.tvOrderUserOrderPay.setVisibility(View.GONE);
                        break;
                    case 5:
                        holderGroupRember.tvOrderUserOrderStatus.setText("取消");
                        holderGroupRember.tvOrderUserOrderStatus.setTextColor(mActivity.getResources().getColor(R.color.tv_gray));
                        holderGroupRember.tvOrderUserOrderPay.setVisibility(View.GONE);
                        break;
                    default:
                        holderGroupRember.tvOrderUserOrderStatus.setText("");
                        holderGroupRember.tvOrderUserOrderPay.setVisibility(View.GONE);
                        break;
                }
                setPackageData(holderGroupRember.llOrderPackageInfo, mList.get(i).getUser().getPackageList());
                holderGroupRember.tvItemOrderTablewareName.setText(mList.get(i).getUser().getTablewareName() + "(押金)");
                holderGroupRember.tvItemOrderTablewareDeposit.setText("¥" + mList.get(i).getUser().getTablewareDeposit());
                holderGroupRember.tvItemOrderTablewareNum.setText("x" + mList.get(i).getUser().getNum());
                holderGroupRember.tvItemOrderTablewarePrice.setText("¥" + mList.get(i).getUser().getTablewareFee());
                holderGroupRember.tvItemOrderDeliveryPrice.setText("¥" + mList.get(i).getUser().getDeliveryFee());
                holderGroupRember.tvItemOrderAllPrice.setText("¥" + mList.get(i).getUser().getMoney());

                holderGroupRember.llOrderMember.setOnClickListener(new MyClickListener(i));
                holderGroupRember.tvOrderUserOrderPay.setOnClickListener(new MyClickListener(i));

                break;
        }
        return view;
    }

    private class MyClickListener implements View.OnClickListener {
        private int mPosition;

        public MyClickListener(int mPosition) {
            this.mPosition = mPosition;
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tv_order_user_order_pay:
                    Intent intent = new Intent(mActivity, PayActivity.class);
                    intent.putExtra("orderIds", mList.get(mPosition).getOrderId());
                    intent.putExtra("makeType", mList.get(mPosition).getUser().getMakeType());
                    mActivity.startActivity(intent);
                    break;
                case R.id.ll_order_user:
                case R.id.ll_order_group:
                case R.id.ll_order_member:
                    Intent intentDetail = new Intent(mActivity, OrderDetailActivity.class);
                    intentDetail.putExtra("orderId", mList.get(mPosition).getUser().getId());
                    mActivity.startActivity(intentDetail);
                    break;
                default:
                    break;
            }

        }
    }

    public void upDate(List<Order> list) {
        if (list != null) {
            if (mList == null) {
                mList = new ArrayList<>();
            }
            mList.addAll(list);
            notifyDataSetChanged();
        }
    }


    //个人订餐
    private class HolderUser {
        private LinearLayout llOrderUser;
        private ImageView ivOrderType;
        private LinearLayout llOrderUserOrderInfo;
        private TextView tvOrderUserOrderNum;
        private TextView tvOrderUserOrderTime;
        private TextView tvOrderUserOrderStatus;
        private TextView tvOrderUserOrderPay;

        private LinearLayout llOrderPackageInfo;
        private TextView tvItemOrderTablewareName;
        private TextView tvItemOrderTablewareDeposit;
        private TextView tvItemOrderTablewareNum;
        private TextView tvItemOrderTablewarePrice;
        private TextView tvItemOrderDeliveryNum;
        private TextView tvItemOrderDeliveryPrice;
        private TextView tvItemOrderAllPrice;

        public HolderUser(View v) {
            llOrderUser = v.findViewById(R.id.ll_order_user);
            ivOrderType = (ImageView) v.findViewById(R.id.iv_order_type);
            llOrderUserOrderInfo = (LinearLayout) v.findViewById(R.id.ll_order_user_order_info);
            tvOrderUserOrderNum = (TextView) v.findViewById(R.id.tv_order_user_order_num);
            tvOrderUserOrderTime = (TextView) v.findViewById(R.id.tv_order_user_order_time);
            tvOrderUserOrderStatus = (TextView) v.findViewById(R.id.tv_order_user_order_status);
            tvOrderUserOrderPay = (TextView) v.findViewById(R.id.tv_order_user_order_pay);
            llOrderPackageInfo = (LinearLayout) v.findViewById(R.id.ll_order_package_info);
            tvItemOrderTablewareName = (TextView) v.findViewById(R.id.tv_item_order_tableware_name);
            tvItemOrderTablewareDeposit = (TextView) v.findViewById(R.id.tv_item_order_tableware_deposit);
            tvItemOrderTablewareNum = (TextView) v.findViewById(R.id.tv_item_order_tableware_num);
            tvItemOrderTablewarePrice = (TextView) v.findViewById(R.id.tv_item_order_tableware_price);
            tvItemOrderDeliveryNum = (TextView) v.findViewById(R.id.tv_item_order_delivery_num);
            tvItemOrderDeliveryPrice = (TextView) v.findViewById(R.id.tv_item_order_delivery_price);
            tvItemOrderAllPrice = (TextView) v.findViewById(R.id.tv_item_order_all_price);
        }


    }

    //饭团 发起者
    private class HolderGroupLeader {
        private LinearLayout llOrderGroup;
        private LinearLayout llOrderInfo;
        private TextView tvOrderNo;
        private TextView tvOrderTime;
        private TextView tvOrderSendTime;
        private TextView tvOrderStatus;

        private ImageView ivOrderType;
        private LinearLayout llOrderUserOrderInfo;
        private TextView tvOrderUserOrderNum;
        private TextView tvOrderUserOrderTime;
        private TextView tvOrderUserOrderStatus;
        private TextView tvOrderUserOrderPay;

        private LinearLayout llOrderPackageInfo;
        private TextView tvItemOrderTablewareName;
        private TextView tvItemOrderTablewareDeposit;
        private TextView tvItemOrderTablewareNum;
        private TextView tvItemOrderTablewarePrice;
        private TextView tvItemOrderDeliveryNum;
        private TextView tvItemOrderDeliveryPrice;
        private TextView tvItemOrderAllPrice;

        public HolderGroupLeader(View v) {
            llOrderGroup = v.findViewById(R.id.ll_order_group);
            llOrderInfo = (LinearLayout) v.findViewById(R.id.ll_order_info);
            tvOrderNo = (TextView) v.findViewById(R.id.tv_order_no);
            tvOrderTime = (TextView) v.findViewById(R.id.tv_order_time);
            tvOrderSendTime = (TextView) v.findViewById(R.id.tv_order_send_time);
            tvOrderStatus = (TextView) v.findViewById(R.id.tv_order_status);

            ivOrderType = (ImageView) v.findViewById(R.id.iv_order_type);
            llOrderUserOrderInfo = (LinearLayout) v.findViewById(R.id.ll_order_user_order_info);
            tvOrderUserOrderNum = (TextView) v.findViewById(R.id.tv_order_user_order_num);
            tvOrderUserOrderTime = (TextView) v.findViewById(R.id.tv_order_user_order_time);
            tvOrderUserOrderStatus = (TextView) v.findViewById(R.id.tv_order_user_order_status);
            tvOrderUserOrderPay = (TextView) v.findViewById(R.id.tv_order_user_order_pay);
            llOrderPackageInfo = (LinearLayout) v.findViewById(R.id.ll_order_package_info);
            tvItemOrderTablewareName = (TextView) v.findViewById(R.id.tv_item_order_tableware_name);
            tvItemOrderTablewareDeposit = (TextView) v.findViewById(R.id.tv_item_order_tableware_deposit);
            tvItemOrderTablewareNum = (TextView) v.findViewById(R.id.tv_item_order_tableware_num);
            tvItemOrderTablewarePrice = (TextView) v.findViewById(R.id.tv_item_order_tableware_price);
            tvItemOrderDeliveryNum = (TextView) v.findViewById(R.id.tv_item_order_delivery_num);
            tvItemOrderDeliveryPrice = (TextView) v.findViewById(R.id.tv_item_order_delivery_price);
            tvItemOrderAllPrice = (TextView) v.findViewById(R.id.tv_item_order_all_price);
        }


    }

    //饭团成员
    private class HolderGroupRember {
        private LinearLayout llOrderMember;
        private LinearLayout llOrderInfo;
        private TextView tvOrderNo;
        private TextView tvOrderTime;
        private TextView tvOrderSendTime;
        private TextView tvOrderStatus;

        private LinearLayout llOrderPackageInfo;
        private TextView tvItemOrderTablewareName;
        private TextView tvItemOrderTablewareDeposit;
        private TextView tvItemOrderTablewareNum;
        private TextView tvItemOrderTablewarePrice;
        private TextView tvItemOrderDeliveryNum;
        private TextView tvItemOrderDeliveryPrice;
        private TextView tvItemOrderAllPrice;

        private ImageView ivOrderType;
        private LinearLayout llOrderUserOrderInfo;
        private TextView tvOrderUserOrderNum;
        private TextView tvOrderUserOrderTime;
        private TextView tvOrderUserOrderStatus;
        private TextView tvOrderUserOrderPay;

        public HolderGroupRember(View v) {
            llOrderMember = v.findViewById(R.id.ll_order_member);
            ivOrderType = (ImageView) v.findViewById(R.id.iv_order_type);
            llOrderUserOrderInfo = (LinearLayout) v.findViewById(R.id.ll_order_user_order_info);
            tvOrderUserOrderNum = (TextView) v.findViewById(R.id.tv_order_user_order_num);
            tvOrderUserOrderTime = (TextView) v.findViewById(R.id.tv_order_user_order_time);
            tvOrderUserOrderStatus = (TextView) v.findViewById(R.id.tv_order_user_order_status);
            tvOrderUserOrderPay = (TextView) v.findViewById(R.id.tv_order_user_order_pay);

            llOrderPackageInfo = (LinearLayout) v.findViewById(R.id.ll_order_package_info);
            tvItemOrderTablewareName = (TextView) v.findViewById(R.id.tv_item_order_tableware_name);
            tvItemOrderTablewareDeposit = (TextView) v.findViewById(R.id.tv_item_order_tableware_deposit);
            tvItemOrderTablewareNum = (TextView) v.findViewById(R.id.tv_item_order_tableware_num);
            tvItemOrderTablewarePrice = (TextView) v.findViewById(R.id.tv_item_order_tableware_price);
            tvItemOrderDeliveryNum = (TextView) v.findViewById(R.id.tv_item_order_delivery_num);
            tvItemOrderDeliveryPrice = (TextView) v.findViewById(R.id.tv_item_order_delivery_price);
            tvItemOrderAllPrice = (TextView) v.findViewById(R.id.tv_item_order_all_price);

            llOrderInfo = (LinearLayout) v.findViewById(R.id.ll_order_info);
            tvOrderNo = (TextView) v.findViewById(R.id.tv_order_no);
            tvOrderTime = (TextView) v.findViewById(R.id.tv_order_time);
            tvOrderSendTime = (TextView) v.findViewById(R.id.tv_order_send_time);
            tvOrderStatus = (TextView) v.findViewById(R.id.tv_order_status);
        }

    }

    private int getDataType(Order order) {
        if(order.getUser().getMakeType()==1){
            if (order.getGroup() == null) {
                return TYPE_GROUP_LEADER;
            } else {
                if (order.getGroup().getUserId() == order.getUser().getUserId()) {
                    return TYPE_GROUP_LEADER;
                } else {
                    return TYPE_GROUP_MEMBER;
                }
            }
        }else{
            return TYPE_USER;
        }

    }

    private void setPackageData(LinearLayout llContent, List<PackageOrder> list) {
        llContent.removeAllViews();
        if (list == null) return;
        for (int i = 0; i < list.size(); i++) {
            View view = LayoutInflater.from(mActivity).inflate(R.layout.item_order_package, null);
            TextView tvPackageName = (TextView) view.findViewById(R.id.tv_item_order_package_name);
            TextView tvPackageOther = (TextView) view.findViewById(R.id.tv_item_order_package_other);
            TextView tvPackageNum = (TextView) view.findViewById(R.id.tv_item_order_package_num);
            TextView tvPackagePrice = (TextView) view.findViewById(R.id.tv_item_order_package_price);
            tvPackageName.setText(list.get(i).getPackageName());
            tvPackageOther.setText(list.get(i).getFoods().replaceAll(",", "\n"));
            tvPackageNum.setText("x" + list.get(i).getNum());
            tvPackagePrice.setText("¥" + list.get(i).getSubtotal());
            llContent.addView(view);
        }
    }

}
