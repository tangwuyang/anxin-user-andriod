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

import com.anxin.kitchen.bean.ContactEntity;
import com.anxin.kitchen.bean.FriendsBean;
import com.anxin.kitchen.decoration.IndexStickyViewDecoration;
import com.anxin.kitchen.user.R;
import com.anxin.kitchen.utils.FrinedsSearch;
import com.anxin.kitchen.utils.LocalContactSearch;
import com.anxin.kitchen.utils.PrefrenceUtil;

import java.util.ArrayList;
import java.util.List;

public class SearchFrinedActivity extends BaseActivity implements View.OnClickListener {
    private ImageView mBackImg;
    private EditText mSearchEt;
    private TextView mSearchTv;
    private ListView mFrindsLv;
    private PrefrenceUtil prefrenceUtil;
    private ArrayList<FriendsBean.Data> friends;
    private FriendAdapter mAdapter;
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
        mSearchTv.setOnClickListener(this);
        mBackImg.setOnClickListener(this);
    }

    private void initAdapter() {
        String friendSt = prefrenceUtil.getFriends();
        FriendsBean bean = null;
        if (null!=friendSt && !"null".equals(friendSt)) {
            bean = mGson.fromJson(friendSt,FriendsBean.class);
        }
        if (null!=bean) {
            friends = (ArrayList<FriendsBean.Data>) bean.getData();
        }else {
            friends = new ArrayList<>();
        }
        mAdapter = new FriendAdapter(friends);
        mFrindsLv.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.search_tv:
                String searchSt = mSearchEt.getText().toString();
                myLog("---------------->searchSt "+searchSt);

                if (searchSt != null && searchSt.length() > 0) {
                    ArrayList<FriendsBean.Data> listG = FrinedsSearch.searchContact(searchSt, friends);
                    myLog("--------------"+listG.size());
                    mAdapter = new FriendAdapter(listG);
                    mFrindsLv.setAdapter(mAdapter);
                } else {
                    myLog("--------------------------");
                    if (searchSt==null || searchSt.length()==0){
                        if (null != friends){
                            mAdapter = new FriendAdapter(friends);
                            mFrindsLv.setAdapter(mAdapter);

                        }
                    }
                }
                break;

            case R.id.back_img:
                finish();
                break;
        }
    }

    class FriendAdapter extends BaseAdapter{

        ArrayList<FriendsBean.Data> listG;
        public FriendAdapter() {

        }

        public FriendAdapter(ArrayList<FriendsBean.Data> listG) {
         this.listG = listG;
        }

        @Override
        public int getCount() {
            return listG.size();
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
            FriendsBean.Data friend = listG.get(i);
            TextView nameTv = view.findViewById(R.id.tv_name);
            TextView mobielTv = view.findViewById(R.id.tv_mobile);
            nameTv.setText(friend.getTrueName());
            mobielTv.setText(friend.getPhone());
            return view;
        }
    }
}
