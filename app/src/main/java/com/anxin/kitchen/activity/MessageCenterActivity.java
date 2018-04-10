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

import com.anxin.kitchen.MyApplication;
import com.anxin.kitchen.bean.Message;
import com.anxin.kitchen.bean.MessageBean;
import com.anxin.kitchen.user.R;
import com.anxin.kitchen.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

public class MessageCenterActivity extends BaseActivity {
    private ListView mMessageLv;
    private MessageAdapter mMessageAdapter;
    private List<MessageBean> messageBeanList = new ArrayList<>();
    private MessageBean messageBean = new MessageBean();

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

    private void getData() {
        messageBean = MyApplication.getInstance().getMessageList();
        if (null == messageBean)
            messageBean = new MessageBean();
        messageBeanList.add(new MessageBean());
        messageBeanList.add(new MessageBean());
        messageBeanList.add(new MessageBean());
    }

    private void initView() {
        mMessageLv = findViewById(R.id.message_lv);
        mMessageAdapter = new MessageAdapter();
        ImageView back_img = (ImageView) findViewById(R.id.back_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private class MessageAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (messageBeanList != null) {
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
            if (null == null) {
                view = LayoutInflater.from(MessageCenterActivity.this).inflate(R.layout.message_item, viewGroup, false);
                holder = new ViewHolder();
                holder.messageTypeTv = view.findViewById(R.id.message_type_tv);
                holder.messageImg = view.findViewById(R.id.message_img);
                holder.messageTimeTv = view.findViewById(R.id.message_time_tv);
                holder.messageTitleTv = view.findViewById(R.id.message_title_tv);
                holder.messageContenTv = view.findViewById(R.id.message_content_tv);
                holder.enterMessageImg = view.findViewById(R.id.enter_message_img);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            Message messageItem = null;
            switch (position) {
                case 0:
                    holder.messageTypeTv.setText("订单通知");
                    holder.messageImg.setImageDrawable(getResources().getDrawable(R.drawable.message_order_icon));
                    List<Message> orderMessageList = messageBean.getOrderMessageList();
                    if (orderMessageList.size() != 0)
                        messageItem = orderMessageList.get(0);
                    break;
                case 1:
                    holder.messageTypeTv.setText("更新通知");
                    holder.messageImg.setImageDrawable(getResources().getDrawable(R.drawable.update_message_icon));
                    List<Message> updateMessageList = messageBean.getUpdateMessageList();
                    if (updateMessageList.size() != 0)
                        messageItem = updateMessageList.get(0);
                    break;
                case 2:
                    holder.messageTypeTv.setText("活动通知");
                    holder.messageImg.setImageDrawable(getResources().getDrawable(R.drawable.message_activity_icon));
                    List<Message> activityMessageList = messageBean.getActivityMessageList();
                    if (activityMessageList.size() != 0)
                        messageItem = activityMessageList.get(0);
                    break;
                default:
                    break;
            }
            if (messageItem == null)
                return view;
            String title = messageItem.getMsTitle();
            if (title != null && title.length() != 0)
                holder.messageTitleTv.setText(title);
            String content = messageItem.getMsContent();
            if (content != null && content.length() != 0)
                holder.messageContenTv.setText(content);
            String time = messageItem.getMsCreateTime();
            if (time != null && time.length() != 0) {
                String date = DateUtils.stampToDate(time, "MM-dd HH:mm");
                holder.messageTimeTv.setText(date);
            }
            return view;
        }
    }

    private class ViewHolder {
        private ImageView messageImg;
        private TextView messageTypeTv;
        private TextView messageTimeTv;
        private TextView messageTitleTv;
        private TextView messageContenTv;
        private ImageView enterMessageImg;
    }
}
