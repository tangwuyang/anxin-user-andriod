package com.anxin.kitchen.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.anxin.kitchen.bean.FriendsBean;
import com.anxin.kitchen.user.R;
import com.anxin.kitchen.utils.PrefrenceUtil;

import java.util.ArrayList;
import java.util.List;

public class SearchFrinedActivity extends BaseActivity implements View.OnClickListener {
    private ImageView mBackImg;
    private EditText mSearchEt;
    private TextView mSearchTv;
    private ListView mFrindsLv;
    private PrefrenceUtil prefrenceUtil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_frined);
        setTitle("搜索");
        prefrenceUtil = new PrefrenceUtil(this);
        mBackImg = findViewById(R.id.back_img);
        mSearchEt = findViewById(R.id.search_et);
        mSearchTv = findViewById(R.id.search_tv);
        mFrindsLv = findViewById(R.id.friends_lv);
        initAdapter();
        mBackImg.setOnClickListener(this);
    }

    private void initAdapter() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.search_tv:
                break;
        }
    }

    class FriendAdapter extends BaseAdapter{
        private List<FriendsBean.Data> friends;

        public FriendAdapter() {
            String friendSt = prefrenceUtil.getFriends();
            FriendsBean bean = null;
            if (null!=friendSt && !"null".equals(friendSt)) {
                bean = mGson.fromJson(friendSt,FriendsBean.class);
            }
            if (null!=bean) {
                this.friends = bean.getData();
            }
        }

        @Override
        public int getCount() {
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
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = LayoutInflater.from(SearchFrinedActivity.this).inflate(R.layout.contact_item, viewGroup, false);
            return null;
        }
    }
}
