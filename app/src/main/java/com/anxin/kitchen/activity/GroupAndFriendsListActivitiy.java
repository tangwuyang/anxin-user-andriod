package com.anxin.kitchen.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anxin.kitchen.bean.ContactEntity;
import com.anxin.kitchen.bean.FriendsBean;
import com.anxin.kitchen.bean.MenuEntity;
import com.anxin.kitchen.bean.SearchGroupBean;
import com.anxin.kitchen.decoration.IndexStickyViewDecoration;
import com.anxin.kitchen.fragment.groupfragment.GroupMainFragment;
import com.anxin.kitchen.interface_.RequestNetListener;
import com.anxin.kitchen.user.R;
import com.anxin.kitchen.utils.Cache;
import com.anxin.kitchen.utils.Constant;
import com.anxin.kitchen.utils.Log;
import com.anxin.kitchen.utils.PrefrenceUtil;
import com.anxin.kitchen.utils.StringUtils;
import com.anxin.kitchen.utils.SystemUtility;
import com.anxin.kitchen.view.MyListView;
import com.bluetooth.tangwuyang.fantuanlibrary.IndexStickyView;
import com.bluetooth.tangwuyang.fantuanlibrary.adapter.IndexHeaderFooterAdapter;
import com.bluetooth.tangwuyang.fantuanlibrary.adapter.IndexStickyViewAdapter;
import com.bluetooth.tangwuyang.fantuanlibrary.entity.BaseEntity;
import com.bluetooth.tangwuyang.fantuanlibrary.listener.OnItemClickListener;
import com.bluetooth.tangwuyang.fantuanlibrary.listener.OnItemLongClickListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupAndFriendsListActivitiy extends BaseActivity implements View.OnClickListener,OnItemClickListener,OnItemLongClickListener,RequestNetListener{
    private static final String ADD_FRIENDS = "ADD_FRIENDS";
    private TextView mCancelTv;
    private ImageView mBackImg;
    private TextView mTitleTv;
    private TextView mCompleteTv;
    private TextView mChosedTv;

    private PrefrenceUtil mPrefrenceUtil;
    private Gson mGson;
    IndexStickyView mIndexStickyView;
    MyIndexStickyViewAdapter mAdapter;
    IndexHeaderFooterAdapter mBannerAdapter;
    private GroupAdapter groupAdapter = null;
    private String mToken;
    private Cache mCache;
    private ArrayList<ContactEntity> Friendslist;
    private List<MenuEntity> grouplist;
    private List<ContactEntity> chosedFriendList;
    private int groupId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_and_friends_list_activitiy);
        setTitleBar();
        mCache = new Cache(this);
        mToken = mCache.getAMToken();
        mGson = new Gson();
        Intent intent = getIntent();
        groupId = intent.getIntExtra("groupId",-1);
        initView();
        setGroupAndFriends();
    }

    //首先请求到所有团所对应的好友
    private void getData() {
        //弹出等待
        popLoad();
        //根据团查询每个团的好友
        for (MenuEntity group:
             grouplist) {
            int groupId = group.getGroupId();
            Map<String ,Object> dataMap = new HashMap<>();
            dataMap.put("groupId",groupId);
            dataMap.put("token",mToken);
            requestNet(SystemUtility.getFriendsUrl(),dataMap,group.getMenuTitle());
        }
    }


    @Override
    public void requestSuccess(String responseString, String requestCode) {
        super.requestSuccess(responseString, requestCode);
        String status = StringUtils.parserMessage(responseString,Constant.REQUEST_STATUS);
        if (null!=status && status.equals(Constant.REQUEST_SUCCESS)){
            myLog("--------->"+responseString);
            Intent intent1 = new Intent();
            setResult(Constant.ADD_FRIEND_CODE,intent1);
            String friends = mGson.toJson(chosedFriendList);
            finish();
        }
    }

    @Override
    public void requestFailure(String responseFailure, String requestCode) {
        super.requestFailure(responseFailure, requestCode);
    }

    private void popLoad() {

    }

    private void initView() {
        mPrefrenceUtil = new PrefrenceUtil(this);
        mIndexStickyView = findViewById(R.id.indexStickyView);
        mCompleteTv = findViewById(R.id.complete_bottom_tv);
        mChosedTv = findViewById(R.id.chosed_num_tv);
        mCompleteTv.setOnClickListener(this);
        chosedFriendList = new ArrayList<>();
    }

    private void setGroupAndFriends() {
        mAdapter = new MyIndexStickyViewAdapter(getFrindList());
        mIndexStickyView.setAdapter(mAdapter);
        mIndexStickyView.addItemDecoration(new IndexStickyViewDecoration(this));
        //添加饭团组
        setSearch();
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnItemLongClickListener(this);
    }

    private void setSearch() {
        //头部搜索
        mBannerAdapter = new IndexHeaderFooterAdapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {

                View view = LayoutInflater.from(GroupAndFriendsListActivitiy.this).inflate(R.layout.indexsticky_header_contact_banner, parent, false);
                ImageViewVH vh = new ImageViewVH(view);

                return vh;
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, BaseEntity itemData) {
                ImageViewVH imageViewVH = (ImageViewVH) holder;
                ((ImageViewVH) holder).search_rl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(GroupAndFriendsListActivitiy.this, "search", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            class ImageViewVH extends RecyclerView.ViewHolder {
                RelativeLayout search_rl;

                public ImageViewVH(View itemView) {
                    super(itemView);
                    search_rl = (RelativeLayout) itemView.findViewById(R.id.search_rl);
                    search_rl.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(GroupAndFriendsListActivitiy.this, "search", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        };
        mIndexStickyView.addIndexHeaderAdapter(mBannerAdapter);
    }

    private void setFristGroup() {
        //头部搜索
        mBannerAdapter = new IndexHeaderFooterAdapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {

                View view = LayoutInflater.from(GroupAndFriendsListActivitiy.this).inflate(R.layout.group_layout, parent, false);
                ImageViewVH vh = new ImageViewVH(view);
                vh.groupLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    }
                });
                return vh;
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, BaseEntity itemData) {
                ((ImageViewVH)holder).groupLv.setAdapter(groupAdapter);
                ((ImageViewVH)holder).groupTV.setText("饭团");
            }

            class ImageViewVH extends RecyclerView.ViewHolder {
                MyListView groupLv;
                TextView groupTV;
                public ImageViewVH(View itemView) {
                    super(itemView);
                    groupLv = (MyListView) itemView.findViewById(R.id.group_lv);
                    groupTV = itemView.findViewById(R.id.tv_index);
                }
            }
        };
        mIndexStickyView.addIndexHeaderAdapter(mBannerAdapter);
    }

    /**
     * 获取缓存中的好友列表
     * */
    private List<ContactEntity> getFrindList() {
        String friendsSt = mPrefrenceUtil.getFriends();
        FriendsBean bean = mGson.fromJson(friendsSt,FriendsBean.class);
        List<FriendsBean.Data> friendList =  bean.getData();
        myLog("---------------"+friendList.size());
        if (null!=Friendslist) {
            Friendslist.clear();
        }else {
            Friendslist = new ArrayList<>();
        }
        if(null != Friendslist){
            for (int i = 0;i<friendList.size();i++){
                ContactEntity contactEntity = new ContactEntity(friendList.get(i).getTrueName(),friendList.get(i).getPhone());
                Friendslist.add(contactEntity);
            }
        }
        return Friendslist;
    }

    private void setTitleBar() {
        mBackImg = findViewById(R.id.back_img);
        mCancelTv = findViewById(R.id.cancel_tv);
        mTitleTv = findViewById(R.id.title_tv);
        mTitleTv.setText("选择团友");
        mBackImg.setVisibility(View.GONE);
        mCancelTv.setVisibility(View.VISIBLE);
        mCancelTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cancel_tv:
                Intent intent = new Intent();
                setResult(Constant.ADD_FRIEND_CODE,intent);
                finish();
               break;
            case R.id.complete_bottom_tv:
                if (chosedFriendList.size()==0){
                    Toast.makeText(this, "请选择好友", Toast.LENGTH_SHORT).show();
                    return;
                }
                Map<String,Object> dataMap = new HashMap<>();
                StringBuffer phonesBf = new StringBuffer();
                StringBuffer namesBf = new StringBuffer();
                for (ContactEntity friend :
                        chosedFriendList) {
                    phonesBf.append(friend.getMobile()+",");
                    namesBf.append(friend.getName()+", ");
                }
                String phones = phonesBf.substring(0,phonesBf.length());
                String names = namesBf.substring(0,namesBf.length());
                myLog("-----"+phones + "   " +names);
                dataMap.put("groupId",groupId);
                dataMap.put("token",mToken);
                dataMap.put("phones",phones);
                dataMap.put("names",names);
                requestNet(SystemUtility.addFriendsToGroupUrl(),dataMap,ADD_FRIENDS);

                break;
        }
    }



    public List<MenuEntity> getGroupList() {
        String groupSt = mPrefrenceUtil.getGroups();
        myLog("-------groupst--->"+groupSt);
        SearchGroupBean bean = mGson.fromJson(groupSt,SearchGroupBean.class);
        List<SearchGroupBean.Data> groupList = bean.getData();
        if (null == this.grouplist){
            this.grouplist = new ArrayList<>();
        }
        this.grouplist.clear();
        myLog("----------------"+groupList.size());
        for (int i=0;i<groupList.size();i++){
            myLog("-----------gro-----"+groupList.get(i).getGroupDesc());
            this.grouplist.add(new MenuEntity(groupList.get(i).getGroupDesc(),R.drawable.vector_contact_focus));
        }
        myLog("------------------->"+this.grouplist.size());
        return this.grouplist;
    }

    @Override
    public void onItemClick(View childView, int position, Object item) {

    }

    @Override
    public void onItemLongClick(View childView, int position, Object item) {

    }

    class MyIndexStickyViewAdapter extends IndexStickyViewAdapter<ContactEntity> {

        public MyIndexStickyViewAdapter(List<ContactEntity> list) {
            super(list);
        }

        @Override
        public RecyclerView.ViewHolder onCreateIndexViewHolder(ViewGroup parent) {
            View view = LayoutInflater.from(GroupAndFriendsListActivitiy.this).inflate(R.layout.indexsticky_item_index, parent, false);
            return new IndexViewHolder(view);
        }

        @Override
        public RecyclerView.ViewHolder onCreateContentViewHolder(ViewGroup parent) {

            View view = LayoutInflater.from(GroupAndFriendsListActivitiy.this).inflate(R.layout.indexsticky_item_contact, parent, false);
            return new ContentViewHolder(view);
        }

        @Override
        public void onBindIndexViewHolder(RecyclerView.ViewHolder holder, int position, String indexName) {
            IndexViewHolder indexViewHolder = (IndexViewHolder) holder;
            indexViewHolder.mTextView.setText(indexName);
        }

        @Override
        public void onBindContentViewHolder(RecyclerView.ViewHolder holder, int position, final ContactEntity itemData) {

            final ContentViewHolder contentViewHolder = (ContentViewHolder) holder;
            contentViewHolder.mMobile.setText(itemData.getMobile());
            contentViewHolder.mName.setText(itemData.getName());
            contentViewHolder.mSelectImg.setVisibility(View.VISIBLE);
            contentViewHolder.mSelectImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (contentViewHolder.isChose){
                        contentViewHolder.isChose = false;
                        chosedFriendList.remove(itemData);
                        contentViewHolder.mSelectImg.setImageDrawable(GroupAndFriendsListActivitiy.this.getResources().getDrawable(R.drawable.unselected_drawable));
                    }else {
                        contentViewHolder.isChose = true;
                        chosedFriendList.add(itemData);
                        contentViewHolder.mSelectImg.setImageDrawable(GroupAndFriendsListActivitiy.this.getResources().getDrawable(R.drawable.selected_drawable));
                    }

                    mChosedTv.setText(chosedFriendList.size()+"人");
                }
            });
        }
    }


    class ContentViewHolder extends RecyclerView.ViewHolder {
        TextView mName;
        TextView mMobile;
        ImageView mAvatar;
        ImageView mSelectImg;
        boolean isChose;
        public ContentViewHolder(View itemView) {
            super(itemView);
            isChose = false;
            mSelectImg = itemView.findViewById(R.id.select_img);
            mName = (TextView) itemView.findViewById(R.id.tv_name);
            mMobile = (TextView) itemView.findViewById(R.id.tv_mobile);
            mAvatar = (ImageView) itemView.findViewById(R.id.img_avatar);
        }
    }
    class IndexViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;

        public IndexViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.tv_index);
        }
    }

    class GroupAdapter extends BaseAdapter {
        List<MenuEntity> dataList;

        public GroupAdapter(List<MenuEntity> dataList) {
            this.dataList = dataList;
        }

        @Override
        public int getCount() {
            return dataList.size();
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
            view = LayoutInflater.from(GroupAndFriendsListActivitiy.this).inflate(R.layout.indexsticky_item_contact,viewGroup,false);
            ImageView selectImg = view.findViewById(R.id.select_img);
            selectImg.setVisibility(View.VISIBLE);
            TextView nameTv = view.findViewById(R.id.tv_name);
            String groupName = dataList.get(i).getMenuTitle();
            myLog("------------name:"+groupName);
            nameTv.setText(dataList.get(i).getMenuTitle());

            return view;
        }
    }

}
