package com.anxin.kitchen.fragment.groupfragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anxin.kitchen.activity.AddNewFriendActivity;
import com.anxin.kitchen.activity.CreateGroupActivity;
import com.anxin.kitchen.activity.GroupMemberActivity;
import com.anxin.kitchen.activity.InvateFriendActivity;
import com.anxin.kitchen.activity.LoginActivity;
import com.anxin.kitchen.activity.MainActivity;
import com.anxin.kitchen.bean.ContactEntity;
import com.anxin.kitchen.bean.FriendsBean;
import com.anxin.kitchen.bean.MenuEntity;
import com.anxin.kitchen.bean.SearchGroupBean;
import com.anxin.kitchen.bean.UserEntity;
import com.anxin.kitchen.decoration.IndexStickyViewDecoration;
import com.anxin.kitchen.event.OnUserAcountEvent;
import com.anxin.kitchen.fragment.HomeBaseFragment;
import com.anxin.kitchen.user.R;
import com.anxin.kitchen.utils.Cache;
import com.anxin.kitchen.utils.Constant;
import com.anxin.kitchen.utils.EventBusFactory;
import com.anxin.kitchen.utils.Log;
import com.anxin.kitchen.utils.SystemUtility;
import com.anxin.kitchen.view.MyListView;
import com.anxin.kitchen.view.WaitingDialog;
import com.bluetooth.tangwuyang.fantuanlibrary.IndexStickyView;
import com.bluetooth.tangwuyang.fantuanlibrary.adapter.IndexHeaderFooterAdapter;
import com.bluetooth.tangwuyang.fantuanlibrary.adapter.IndexStickyViewAdapter;
import com.bluetooth.tangwuyang.fantuanlibrary.entity.BaseEntity;
import com.bluetooth.tangwuyang.fantuanlibrary.listener.OnItemClickListener;
import com.bluetooth.tangwuyang.fantuanlibrary.listener.OnItemLongClickListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.anxin.kitchen.utils.Constant.CREATE_GROUP_RESULT_CODE;
import static com.anxin.kitchen.utils.Constant.GROUP_MAIN_REQEST_CODE;

/**
 * 饭团主界面
 */
public class GroupMainFragment extends HomeBaseFragment implements View.OnClickListener, OnItemClickListener, OnItemLongClickListener {
    private static final String TAG = "GroupMainFragment";
    private Log LOG = Log.getLog();
    private View view;
    SwipeRefreshLayout mRefreshLayout;
    IndexStickyView mIndexStickyView;
    MyIndexStickyViewAdapter mAdapter;
    private ImageView mMenuImg;
    private GroupAdapter groupAdapter = null;
    IndexHeaderFooterAdapter<UserEntity> mFavAdapter;
    IndexHeaderFooterAdapter<MenuEntity> mMenuAdapter;
    IndexHeaderFooterAdapter<UserEntity> mNormalAdapter;
    IndexHeaderFooterAdapter mBannerAdapter;
    IndexHeaderFooterAdapter<UserEntity> mFooterAdapter;
    IndexHeaderFooterAdapter mFooterBannerAdapter;

    private List<ContactEntity> Friendslist;
    private List<String> mMenuNames = new ArrayList<>();
    private List<Integer> mMenuImgs = new ArrayList<>();
    String token;
    MainActivity activity;
    private WaitingDialog mWaitingDiag;
    private PopupWindow popWnd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBusFactory.getInstance().register(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.group_main_fragment, null);
        //initView();//初始化界面控制
        //initEditData();
        grouplist = new ArrayList<>();
        activity = (MainActivity) getActivity();
        mRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        mIndexStickyView = view.findViewById(R.id.indexStickyView);
        mMenuNames.add("创建饭团");
        mMenuImgs.add(R.drawable.create_new_group_drawale);
        mWaitingDiag = new WaitingDialog(activity, 1000 * 20);
        mWaitingDiag.show();
        mWaitingDiag.startAnimation();
        initView();
        return view;
    }

    /**
     * 请求网咯获取好友列表
     * 以及定参团列表
     */
    private void requestInternetGetData() {
        if (null == token) {
            token = new Cache(activity).getAMToken();
        }
        if (token == null) {
            if (!SystemUtility.isForeground(getContext(), "com.anxin.kitchen.activity.LoginActivity")) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.putExtra("tag",true);
                startActivityForResult(intent, 200);
            }
        } else {
            getAllGroups();
            getAllFriends();
        }
    }

    public void onEventMainThread(OnUserAcountEvent event) {//用户信息修改监听
        activity.myLog("----------------->是否刷新");
       // requestInternetGetData();
    }



    private void getAllGroups() {
        String url = SystemUtility.searchGroupUrl();
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put(Constant.TOKEN, token);
        activity.requestNet(url, dataMap, activity.SEARCH_GROUP);

    }



    private void getAllFriends() {
        String url = SystemUtility.getFriendsUrl();
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put(Constant.TOKEN, token);
        activity.requestNet(url, dataMap, activity.GET_FRIEND);
    }

    @Override
    public void onStart() {
        super.onStart();
        setRefresh();
        requestInternetGetData();
    }

    //下拉刷新
    private void setRefresh() {
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRefreshLayout.setRefreshing(true);
                mRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshLayout.setRefreshing(false);
                    }
                }, 3000);
            }
        });
    }

    private void setSearch() {
        //头部搜索
        mBannerAdapter = new IndexHeaderFooterAdapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
                View view = LayoutInflater.from(mContext).inflate(R.layout.indexsticky_header_contact_banner, parent, false);
                ImageViewVH vh = new ImageViewVH(view);

                return vh;
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, BaseEntity itemData) {
                ImageViewVH imageViewVH = (ImageViewVH) holder;
                ((ImageViewVH) holder).search_rl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        activity.myLog("----------------开始搜索");
                        Toast.makeText(activity, "search", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(activity, "search", Toast.LENGTH_SHORT).show();
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

                View view = LayoutInflater.from(mContext).inflate(R.layout.group_layout, parent, false);
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
                ((ImageViewVH) holder).groupLv.setAdapter(groupAdapter);
                ((ImageViewVH) holder).groupTV.setText("饭团");
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

    private void addFanGroup() {
        mMenuAdapter = new IndexHeaderFooterAdapter<MenuEntity>("↑", "饭团", getGroup()) {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
                View view = LayoutInflater.from(mContext).inflate(R.layout.indexsticky_item_contact, parent, false);
                return new ContentViewHolder(view);
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, MenuEntity itemData) {

                ContentViewHolder contentViewHolder = (ContentViewHolder) holder;
                contentViewHolder.mMobile.setVisibility(View.GONE);
                contentViewHolder.mName.setText(itemData.getMenuTitle());
                contentViewHolder.mAvatar.setImageResource(itemData.getMenuIconRes());
            }
        };
        mMenuAdapter.setOnItemLongClickListener(new OnItemLongClickListener<MenuEntity>() {
            @Override
            public void onItemLongClick(View childView, int position, MenuEntity item) {
                Toast.makeText(mContext, "长按：" + item.getMenuTitle() + ",位置：" + position, Toast.LENGTH_SHORT).show();
            }
        });
        mIndexStickyView.addIndexHeaderAdapter(mMenuAdapter);
    }

    public void setFriend(FriendsBean bean) {
        List<FriendsBean.Data> friendList = bean.getData();
        activity.myLog("---------------" + friendList.size());
        if (null != Friendslist) {
            Friendslist.clear();
        } else {
            Friendslist = new ArrayList<>();
        }
        String phone = mApp.getCache().getUserPhone();
        if (null != Friendslist && friendList.size() > 0) {
            for (int i = 0; i < friendList.size(); i++) {
                ContactEntity contactEntity = new ContactEntity(friendList.get(i).getTrueName(),
                        friendList.get(i).getPhone(), friendList.get(i).getGroupId(),
                        friendList.get(i).getUserLogo(), friendList.get(i).getId());
                if (!contactEntity.getName().equals("暂无") && !friendList.get(i).getPhone().equals(phone)) {
                    Friendslist.add(contactEntity);
                }
            }
        }
//        if (){
//            Friendslist.add(new ContactEntity("暂无好友", ""));
//        }
        mAdapter = new MyIndexStickyViewAdapter(Friendslist);
        mIndexStickyView.setAdapter(mAdapter);
        // mAdapter.notifyDataSetChanged();
        mIndexStickyView.addItemDecoration(new IndexStickyViewDecoration(getContext()));
        //添加饭团组
        // addFanGroup();
        groupAdapter = new GroupAdapter(this.grouplist);
        setFristGroup();
        setSearch();
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnItemLongClickListener(this);
    }

    public void updataGroup(SearchGroupBean bean) {
        setGroup(bean);
        groupAdapter.notifyDataSetChanged();
    }

    public void setDataChanged() {
        mAdapter.notifyDataSetChanged();
    }

    class GroupAdapter extends BaseAdapter {
        List<MenuEntity> dataList;

        public GroupAdapter(List<MenuEntity> dataList) {
            this.dataList = dataList;
            activity.myLog("-------dalist_size--------" + dataList.size());
        }

        @Override
        public int getCount() {
            if (null != dataList) {
                return dataList.size();
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
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = LayoutInflater.from(activity).inflate(R.layout.indexsticky_item_contact, viewGroup, false);
            final MenuEntity entity = dataList.get(i);
            TextView nameTv = view.findViewById(R.id.tv_name);
            final RelativeLayout infoRl = view.findViewById(R.id.info_rl);
            String nameAndNums = entity.getMenuTitle() + "(" + entity.getGroupNum() + "人)";
            Spannable string = new SpannableString(nameAndNums);
            int blacktPosition = nameAndNums.indexOf("(");
            int rightPosition = nameAndNums.indexOf(")") + 1;
            string.setSpan(new ForegroundColorSpan(activity.getResources().getColor(R.color.shallow_black)), blacktPosition, rightPosition, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            nameTv.setText(string);
            TextView deleteTv = view.findViewById(R.id.delete_tv);
            deleteTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Map<String, Object> dataMap = new HashMap<>();
                    dataMap.put("groupId", entity.getGroupId());
                    dataMap.put("token", token);
                    activity.requestNet(SystemUtility.deleteGroupUrl(), dataMap, activity.DELETE_GROUP);
                }
            });

            infoRl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, GroupMemberActivity.class);
                    intent.putExtra("groupId", entity.getGroupId());
                    intent.putExtra("groupName", entity.getMenuTitle());
                    startActivityForResult(intent, Constant.GROUP_MAIN_REQEST_CODE);
                }
            });
            return view;
        }
    }

    /**
     * 有需要可以添加顶部视图
     */
    private void addTopView() {
        //自定义添加头部收藏信息  如有需要 可以添加头部尾部自定义
        mFavAdapter = new IndexHeaderFooterAdapter<UserEntity>("☆", "我的收藏", initFavDatas()) {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {

                View view = LayoutInflater.from(mContext).inflate(R.layout.indexsticky_item_contact, parent, false);
                return new ContentViewHolder(view);
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, UserEntity itemData) {

                ContentViewHolder contentViewHolder = (ContentViewHolder) holder;
                contentViewHolder.mMobile.setText(itemData.getMobile());
                contentViewHolder.mName.setText(itemData.getName());
            }
        };
        mFavAdapter.setOnItemClickListener(new OnItemClickListener<UserEntity>() {
            @Override
            public void onItemClick(View childView, int position, UserEntity item) {
                Toast.makeText(mContext, "点击：" + item.getName() + ",位置：" + position, Toast.LENGTH_SHORT).show();
            }
        });
        mIndexStickyView.addIndexHeaderAdapter(mFavAdapter);
    }

    /**
     * 有需要可以添加底部视图
     */
    private void addBottomView() {
        //添加一个底部自定义列表
        mFooterAdapter = new IndexHeaderFooterAdapter<UserEntity>("$", "Footer", initFavDatas()) {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {

                View view = LayoutInflater.from(mContext).inflate(R.layout.indexsticky_item_contact, parent, false);
                return new ContentViewHolder(view);
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, UserEntity itemData) {
                ContentViewHolder contentViewHolder = (ContentViewHolder) holder;
                contentViewHolder.mMobile.setText(itemData.getMobile());
                contentViewHolder.mName.setText(itemData.getName());
            }
        };
        mFooterAdapter.setOnItemClickListener(new OnItemClickListener<UserEntity>() {
            @Override
            public void onItemClick(View childView, int position, UserEntity item) {
                Toast.makeText(mContext, "点击Footer：" + item.getName() + ",位置：" + position, Toast.LENGTH_SHORT).show();
            }
        });
        mIndexStickyView.addIndexFooterAdapter(mFooterAdapter);

        //Footer Banner
        mFooterBannerAdapter = new IndexHeaderFooterAdapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {

                View view = LayoutInflater.from(mContext).inflate(R.layout.indexsticky_header_contact_banner, parent, false);
                ImageViewVH vh = new ImageViewVH(view);
                vh.img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(mContext, "Footer图片视图点击", Toast.LENGTH_SHORT).show();
                    }
                });
                return vh;
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, BaseEntity itemData) {

            }

            class ImageViewVH extends RecyclerView.ViewHolder {
                ImageView img;

                public ImageViewVH(View itemView) {
                    super(itemView);
                    img = (ImageView) itemView.findViewById(R.id.img);
                }
            }
        };
        mIndexStickyView.addIndexFooterAdapter(mFooterBannerAdapter);

    }

    ContactEntity mAdd = new ContactEntity("阿圆add", "15525672987");
    List<ContactEntity> mAddCollections = new ArrayList<>();

    void initEditData() {

        mAddCollections.add(new ContactEntity("阿圆add Collections1", "15525672987"));
        mAddCollections.add(new ContactEntity("阿圆add Collections2", "15525672987"));
    }

    private List<UserEntity> initFavDatas() {
        List<UserEntity> list = new ArrayList<>();
        list.add(new UserEntity("张三", "13298449923"));
        list.add(new UserEntity("李四", "13298449923"));
        return list;
    }

    List<MenuEntity> grouplist;

    private List<MenuEntity> getGroup() {
        grouplist = new ArrayList<>();
        grouplist.add(new MenuEntity("星期一午饭团（12人）", R.drawable.vector_contact_focus));
        grouplist.add(new MenuEntity("星期一晚饭团（12人）", R.drawable.vector_contact_focus));
        return grouplist;
    }

    private List<ContactEntity> initDatas() {
        Friendslist = new ArrayList<>();
        // 初始化数据
        List<String> contactStrings = Arrays.asList(getResources().getStringArray(R.array.contact_array));
        List<String> mobileStrings = Arrays.asList(getResources().getStringArray(R.array.mobile_array));
        for (int i = 0; i < contactStrings.size(); i++) {
            ContactEntity contactEntity = new ContactEntity(contactStrings.get(i), mobileStrings.get(i));
            Friendslist.add(contactEntity);
        }
        return Friendslist;
    }


    private void initView() {
        TextView title = view.findViewById(R.id.title_tv);
        title.setText("饭团");
        mMenuImg = view.findViewById(R.id.fantuan_menu_img);
        mMenuImg.setVisibility(View.VISIBLE);
        mMenuImg.setOnClickListener(this);
        ImageView back_img = view.findViewById(R.id.back_img);
        back_img.setVisibility(View.GONE);

    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fantuan_menu_img:
//                popMenu();
                Intent intent = new Intent(getContext(), CreateGroupActivity.class);
                startActivityForResult(intent, GROUP_MAIN_REQEST_CODE);
                break;
            default:
                break;
        }
    }

    private void popMenu() {
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.fantuan_pop_menu, null);
        ListView menuLl = contentView.findViewById(R.id.menu_ll);
        menuLl.setAdapter(new MenuAdapter());
        popWnd = new PopupWindow(getContext());
        popWnd.setContentView(contentView);

        popWnd.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.fan_menu_bg));
        popWnd.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popWnd.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popWnd.setOutsideTouchable(true);
        //popWnd.setBackgroundDrawable(new BitmapDrawable());
        popWnd.showAsDropDown(mMenuImg, 550, 10);
    }

    /*
     * 设置群
     * */
    public void setGroup(SearchGroupBean bean) {
        if (null != mWaitingDiag) {
            mWaitingDiag.stopAnimation();
            mWaitingDiag.dismiss();
        }
        List<SearchGroupBean.Data> groupList = bean.getData();
        if (null == this.grouplist) {
            this.grouplist = new ArrayList<>();
        }
        this.grouplist.clear();
        activity.myLog("----------------" + groupList.size());
        for (int i = 0; i < groupList.size(); i++) {
            activity.myLog("----------------" + groupList.get(i).getGroupDesc());
            this.grouplist.add(new MenuEntity(groupList.get(i).getGroupDesc(), R.drawable.vector_contact_focus, groupList.get(i).getId(), groupList.get(i).getGroupNum()));
        }
   /*     mAdapter = new MyIndexStickyViewAdapter(initDatas());
        mIndexStickyView.setAdapter(mAdapter);*/

    }


    class MenuAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mMenuNames.size();
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
        public View getView(final int i, View view, ViewGroup viewGroup) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.fan_menu_item, viewGroup, false);
            ImageView menuImg = view.findViewById(R.id.menu_img);
            RelativeLayout menuLl = view.findViewById(R.id.menu_ll);
            menuImg.setImageResource(mMenuImgs.get(i));
            TextView menuName = view.findViewById(R.id.menu_tv);
            menuName.setText(mMenuNames.get(i));
            menuLl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    menuChose(i);
                }
            });
            return view;
        }

        private void menuChose(int i) {
            switch (i) {
                case 0:
                    //创建饭团
                    Intent intent = new Intent(getContext(), CreateGroupActivity.class);
                    startActivityForResult(intent, GROUP_MAIN_REQEST_CODE);
                    break;
                case 1:
                    //新增团友
                    Intent intent1 = new Intent(getContext(), AddNewFriendActivity.class);
                    startActivityForResult(intent1, GROUP_MAIN_REQEST_CODE);
                    break;
                case 2:
                    //通讯录导入
                    break;
                case 3:
                    //邀请团友
                    Intent intent3 = new Intent(getContext(), InvateFriendActivity.class);
                    startActivityForResult(intent3, GROUP_MAIN_REQEST_CODE);
                    break;
                default:
                    break;
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GROUP_MAIN_REQEST_CODE && resultCode == Constant.ADD_FRIEND_CODE) {
            getAllGroups();
            getAllFriends();
            return;
        }

        if (requestCode==200 && resultCode == 201){
            activity.myLog("-------------->登录成功");
            getAllGroups();
            getAllFriends();
            return;
        }
    }

    @Override
    public void onItemClick(View childView, int position, Object item) {
        Toast.makeText(mContext, "点击" + mAdd.getName() + ",位置：" + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemLongClick(View childView, int position, Object item) {
        Toast.makeText(mContext, "长按：" + mAdd.getName() + ",位置：" + position, Toast.LENGTH_SHORT).show();
    }


    class MyIndexStickyViewAdapter extends IndexStickyViewAdapter<ContactEntity> {

        public MyIndexStickyViewAdapter(List<ContactEntity> list) {

            super(list);
        }

        @Override
        public RecyclerView.ViewHolder onCreateIndexViewHolder(ViewGroup parent) {

            View view = LayoutInflater.from(mContext).inflate(R.layout.indexsticky_item_index, parent, false);
            return new IndexViewHolder(view);
        }

        @Override
        public RecyclerView.ViewHolder onCreateContentViewHolder(ViewGroup parent) {

            View view = LayoutInflater.from(mContext).inflate(R.layout.contact_item, parent, false);
            return new ContentViewHolder(view);
        }

        @Override
        public void onBindIndexViewHolder(RecyclerView.ViewHolder holder, int position, String indexName) {

            IndexViewHolder indexViewHolder = (IndexViewHolder) holder;
            indexViewHolder.mTextView.setText(indexName);
        }

        @Override
        public void onBindContentViewHolder(RecyclerView.ViewHolder holder, int position, final ContactEntity itemData) {

            ContentViewHolder contentViewHolder = (ContentViewHolder) holder;
            contentViewHolder.mMobile.setText(itemData.getMobile());
            contentViewHolder.mName.setText(itemData.getName());
            activity.setImg(contentViewHolder.mAvatar, itemData.getUserLogo());

            // contentViewHolder.mAvatar.setBackgroundResource(getResources().getDrawable(R.drawable.));

        }
    }


    class ContentViewHolder extends RecyclerView.ViewHolder {
        TextView mName;
        TextView mMobile;
        ImageView mAvatar;
        TextView mDeleteTv;

        public ContentViewHolder(View itemView) {

            super(itemView);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusFactory.getInstance().unregister(this);
    }

}
