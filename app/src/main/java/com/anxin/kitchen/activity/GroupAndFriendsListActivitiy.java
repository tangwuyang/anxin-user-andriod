package com.anxin.kitchen.activity;

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
import com.anxin.kitchen.user.R;
import com.anxin.kitchen.utils.Log;
import com.anxin.kitchen.utils.PrefrenceUtil;
import com.anxin.kitchen.view.MyListView;
import com.bluetooth.tangwuyang.fantuanlibrary.IndexStickyView;
import com.bluetooth.tangwuyang.fantuanlibrary.adapter.IndexHeaderFooterAdapter;
import com.bluetooth.tangwuyang.fantuanlibrary.adapter.IndexStickyViewAdapter;
import com.bluetooth.tangwuyang.fantuanlibrary.entity.BaseEntity;
import com.bluetooth.tangwuyang.fantuanlibrary.listener.OnItemClickListener;
import com.bluetooth.tangwuyang.fantuanlibrary.listener.OnItemLongClickListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class GroupAndFriendsListActivitiy extends BaseActivity implements View.OnClickListener,OnItemClickListener,OnItemLongClickListener{
    private TextView mCancelTv;
    private ImageView mBackImg;
    private TextView mTitleTv;
    private PrefrenceUtil mPrefrenceUtil;
    private Gson mGson;
    IndexStickyView mIndexStickyView;
    MyIndexStickyViewAdapter mAdapter;
    IndexHeaderFooterAdapter mBannerAdapter;
    private GroupAdapter groupAdapter = null;

    private ArrayList<ContactEntity> Friendslist;
    private List<MenuEntity> grouplist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_and_friends_list_activitiy);
        setTitleBar();
        mGson = new Gson();
        initView();
        setGroupAndFriends();
    }

    private void initView() {
        mPrefrenceUtil = new PrefrenceUtil(this);
        mIndexStickyView = findViewById(R.id.indexStickyView);
    }

    private void setGroupAndFriends() {
        mAdapter = new MyIndexStickyViewAdapter(getFrindList());
        mIndexStickyView.setAdapter(mAdapter);
        mIndexStickyView.addItemDecoration(new IndexStickyViewDecoration(this));
        //添加饭团组
        groupAdapter = new GroupAdapter(this.getGroupList());
        setFristGroup();
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
                        myLog("----------------开始搜索");
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
                this.finish();
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
        for (int i=0;i<7;i++){
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
        public void onBindContentViewHolder(RecyclerView.ViewHolder holder, int position, ContactEntity itemData) {

            ContentViewHolder contentViewHolder = (ContentViewHolder) holder;
            contentViewHolder.mMobile.setText(itemData.getMobile());
            contentViewHolder.mName.setText(itemData.getName());
            contentViewHolder.mSelectImg.setVisibility(View.VISIBLE);
        }
    }


    class ContentViewHolder extends RecyclerView.ViewHolder {
        TextView mName;
        TextView mMobile;
        ImageView mAvatar;
        ImageView mSelectImg;
        public ContentViewHolder(View itemView) {

            super(itemView);
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
