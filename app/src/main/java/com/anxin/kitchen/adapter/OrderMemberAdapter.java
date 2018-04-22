package com.anxin.kitchen.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.anxin.kitchen.bean.Order.OrderUser;
import com.anxin.kitchen.user.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by xujianjun on 2018/4/7.
 */

public class OrderMemberAdapter extends BaseAdapter {
    private Activity mActivity;
    private List<OrderUser> mList;
    private LayoutInflater mInflater;


    public OrderMemberAdapter(Activity mActivity, List<OrderUser> mList) {
        this.mActivity = mActivity;
        this.mList = mList;
        this.mInflater = LayoutInflater.from(mActivity);
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
        Holder holder = null;
        if (view == null) {
            view = mInflater.inflate(R.layout.adapter_order_detail_member_info, null);
            holder = new Holder(view);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }
        ImageLoader.getInstance().displayImage(mList.get(i).getUserLogo() == null ? "" : mList.get(i).getUserLogo(), holder.ivOrderDetailMemberCover);
        holder.tvOrderDetailMemberName.setText(mList.get(i).getTrueName());
        switch (mList.get(i).getStatus()) {
            case 0:
                holder.tvOrderDetailMemberPayStatus.setText("未付款");
                holder.tvOrderDetailMemberPayStatus.setTextColor(mActivity.getResources().getColor(R.color.red));
                break;
            case 1:
                holder.tvOrderDetailMemberPayStatus.setText("已付款");
                holder.tvOrderDetailMemberPayStatus.setTextColor(mActivity.getResources().getColor(R.color.tv_gray));
                break;
            case 2:
                holder.tvOrderDetailMemberPayStatus.setText("已发货");
                holder.tvOrderDetailMemberPayStatus.setTextColor(mActivity.getResources().getColor(R.color.tv_gray));
                break;
            case 3:
                holder.tvOrderDetailMemberPayStatus.setText("完成");
                holder.tvOrderDetailMemberPayStatus.setTextColor(mActivity.getResources().getColor(R.color.tv_gray));
                break;
            case 4:
                holder.tvOrderDetailMemberPayStatus.setText("退款");
                holder.tvOrderDetailMemberPayStatus.setTextColor(mActivity.getResources().getColor(R.color.tv_gray));
                break;
            case 5:
                holder.tvOrderDetailMemberPayStatus.setText("取消");
                holder.tvOrderDetailMemberPayStatus.setTextColor(mActivity.getResources().getColor(R.color.tv_gray));
                break;
            default:
                holder.tvOrderDetailMemberPayStatus.setText("");
                break;
        }


        return view;
    }


    private class Holder {
        private ImageView ivOrderDetailMemberCover;
        private TextView tvOrderDetailMemberName;
        private TextView tvOrderDetailMemberPayStatus;

        public Holder(View view) {
            ivOrderDetailMemberCover = (ImageView) view.findViewById(R.id.iv_order_detail_member_cover);
            tvOrderDetailMemberName = (TextView) view.findViewById(R.id.tv_order_detail_member_name);
            tvOrderDetailMemberPayStatus = (TextView) view.findViewById(R.id.tv_order_detail_member_pay_status);
        }
    }

    public void update(List<OrderUser> list){
        this.mList = list;
        notifyDataSetChanged();
    }
}
