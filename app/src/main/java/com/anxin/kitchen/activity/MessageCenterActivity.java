package com.anxin.kitchen.activity;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.anxin.kitchen.bean.MessageBean;
import com.anxin.kitchen.user.R;

import java.util.ArrayList;
import java.util.List;

public class MessageCenterActivity extends AppCompatActivity {
    private ListView mMessageLv;
    private MessageAdapter mMessageAdapter;
    private List<MessageBean> messageBeanList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_center);
        initView();
        initAdapter();
    }

    private void initAdapter() {
        getData();
        if (null != mMessageAdapter) {
            mMessageLv.setAdapter(mMessageAdapter);
        }
    }

    private void getData(){
        MessageBean bean1 = new MessageBean("",0,"订单已发货","您的订单预计30分钟后送达");
        MessageBean bean2 = new MessageBean("",1,"新版本v11.2.2已更新",
                "修复了部分bug，用户体验优化，点击了解详情...");
        MessageBean bean3 = new MessageBean("",2,"每月1号优惠日活动",
                "每月1号大家瓜分1000万，人人可得！");
        messageBeanList.add(bean1);
        messageBeanList.add(bean2);
        messageBeanList.add(bean3);
    }

    private void initView() {
        mMessageLv = findViewById(R.id.message_lv);
        mMessageAdapter = new MessageAdapter();
    }

    private class MessageAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            if (messageBeanList!= null){
                return messageBeanList.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            ViewHolder holder = null;
            MessageBean bean = messageBeanList.get(position);
            if (null==null) {
                view = LayoutInflater.from(MessageCenterActivity.this).inflate(R.layout.message_item, viewGroup, false);
                holder = new ViewHolder();
                holder.messageTypeTv = view.findViewById(R.id.message_type_tv);
                holder.messageImg = view.findViewById(R.id.message_img);
                holder.messageTimeTv = view.findViewById(R.id.message_title_tv);
                holder.messageTitleTv = view.findViewById(R.id.message_title_tv);
                holder.messageContenTv = view.findViewById(R.id.message_content_tv);
                holder.enterMessageImg = view.findViewById(R.id.enter_message_img);
                view.setTag(holder);
            }else {
                holder = (ViewHolder) view.getTag();
            }
            switch (bean.getMessageType()){
                case 0:
                    holder.messageTypeTv.setText("订单通知");
                    break;
                case 1:
                    holder.messageTypeTv.setText("更新通知");
                    break;
                case 2:
                    holder.messageTypeTv.setText("活动通知");
                    break;
                default:
                    break;
            }
            holder.messageTitleTv.setText(bean.getMessageTitle());
            holder.messageContenTv.setText(bean.getMessageContent());
            return view;
        }
    }

    private class ViewHolder{
        private ImageView messageImg;
        private TextView messageTypeTv;
        private TextView messageTimeTv;
        private TextView messageTitleTv;
        private TextView messageContenTv;
        private ImageView enterMessageImg;
    }
}
