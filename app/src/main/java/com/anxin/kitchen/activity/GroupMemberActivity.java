package com.anxin.kitchen.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anxin.kitchen.bean.ContactEntity;
import com.anxin.kitchen.bean.FriendsBean;
import com.anxin.kitchen.decoration.IndexStickyViewDecoration;
import com.anxin.kitchen.fragment.groupfragment.GroupMainFragment;
import com.anxin.kitchen.interface_.RequestNetListener;
import com.anxin.kitchen.user.R;
import com.anxin.kitchen.utils.Cache;
import com.anxin.kitchen.utils.Constant;
import com.anxin.kitchen.utils.StringUtils;
import com.anxin.kitchen.utils.SystemUtility;
import com.bluetooth.tangwuyang.fantuanlibrary.IndexStickyView;
import com.bluetooth.tangwuyang.fantuanlibrary.adapter.IndexStickyViewAdapter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.anxin.kitchen.utils.Constant.GROUP_MAIN_REQEST_CODE;

public class GroupMemberActivity extends BaseActivity implements View.OnClickListener,RequestNetListener{
    private static final String GET_FRIEND_OF_GROUP = "GET_FRIEND_OF_GROUP";
    private IndexStickyView mMembersLv;
    private ImageView mBackImg;
    private ImageView mFunctionImg;
    private TextView mTitleTv;
    private String mToken;
    private String mGroupName;
    private int mGroupId;
    private Gson mGson;
    private List<String> mMenuNames = new ArrayList<>();
    private List<Integer> mMenuImgs = new ArrayList<>();

    List<ContactEntity> friendList = new ArrayList<>();
    private MyIndexStickyViewAdapter mAdapter;
    private boolean isAdd = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_member);
        Intent intent = getIntent();
        if (null != intent){
            mGroupId = intent.getIntExtra("groupId",0);
            mGroupName = intent.getStringExtra("groupName");
        }
        mGson = new Gson();
        mToken = new Cache(this).getAMToken();
        mMembersLv = findViewById(R.id.members_lv);
        mBackImg = findViewById(R.id.back_img);
        mTitleTv = findViewById(R.id.title_tv);
        mFunctionImg = findViewById(R.id.fantuan_menu_img);
        mFunctionImg.setVisibility(View.VISIBLE);
        mTitleTv.setText(mGroupName);
        mBackImg.setOnClickListener(this);
        mFunctionImg.setOnClickListener(this);

        mMenuNames.add("新增团友");
        mMenuNames.add("通讯录导入");
        mMenuNames.add("邀请团友");
        mMenuImgs.add(R.drawable.new_friend_drawable);
        mMenuImgs.add(R.drawable.contactor_imp_drawable);
        mMenuImgs.add(R.drawable.invate_friend_drawable);

        friendList.add(new ContactEntity("sdsd","sdsdsdsd"));
        mAdapter = new MyIndexStickyViewAdapter();
        mMembersLv.setAdapter(mAdapter);
        mMembersLv.addItemDecoration(new IndexStickyViewDecoration(this));
        requestData();
    }

    //获取该团的数据
    private void requestData() {
        if (0!=mGroupId){
            Map<String ,Object> dataMap = new HashMap<>();
            dataMap.put("groupId",mGroupId);
            dataMap.put("token",mToken);
            requestNet(SystemUtility.getFriendsUrl(),dataMap,GET_FRIEND_OF_GROUP);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_img:
                Intent intent = new Intent();
                // 获取用户计算后的结果
                intent.putExtra("isAdd", isAdd); //将计算的值回传回去
                //通过intent对象返回结果，必须要调用一个setResult方法，
                //setResult(resultCode, data);第一个参数表示结果返回码，一般只要大于1就可以，但是
                setResult(Constant.ADD_FRIEND_CODE, intent);
                finish(); //结束当前的activity的生命周期
                break;
            case R.id.fantuan_menu_img:
                popWindow();
                break;
        }
    }

    private void popWindow() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.fantuan_pop_menu, null);
        ListView menuLl = contentView.findViewById(R.id.menu_ll);
        menuLl.setAdapter(new MenuAdapter());
        PopupWindow popWnd = new PopupWindow (this);
        popWnd.setContentView(contentView);
/**背景阴影*/
//        ColorDrawable dw = new ColorDrawable(0x80000000);
        popWnd.setBackgroundDrawable(new ColorDrawable());
        popWnd.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popWnd.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popWnd.setOutsideTouchable(true);
        //popWnd.setBackgroundDrawable(new BitmapDrawable());
        popWnd.showAsDropDown(mFunctionImg,550,10);
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
            view = LayoutInflater.from(GroupMemberActivity.this).inflate(R.layout.fan_menu_item,viewGroup,false);
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
            switch (i){
                case 0:
                    //新增团友
                    Intent intent1 = new Intent(GroupMemberActivity.this, AddNewFriendActivity.class);
                    intent1.putExtra("groupId",mGroupId);
                    intent1.putExtra("groupName",mGroupName);
                    startActivityForResult(intent1,GROUP_MAIN_REQEST_CODE);
                    break;
                case 1:
                    //通讯录导入
                    Intent contactIntent = new Intent(GroupMemberActivity.this, ContactsActivity.class);
                    contactIntent.putExtra("groupId",mGroupId);
                    contactIntent.putExtra("groupName",mGroupName);
                    startActivityForResult(contactIntent,GROUP_MAIN_REQEST_CODE);
                    break;
                case 2:
                    //通讯录导入
                    //新增团友
                    Intent intent2 = new Intent(GroupMemberActivity.this, AddNewFriendActivity.class);
                    startActivityForResult(intent2,GROUP_MAIN_REQEST_CODE);
                    break;
                case 3:

                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void requestSuccess(String responseString, String requestCode) {
        super.requestSuccess(responseString, requestCode);
        if (requestCode == GET_FRIEND_OF_GROUP && null != responseString){
            String status = StringUtils.parserMessage(responseString, Constant.REQUEST_STATUS);
            if (status.equals(Constant.REQUEST_SUCCESS)){
                myLog("-----------friend---"+responseString);
                //解析数据
                FriendsBean bean = mGson.fromJson(responseString,FriendsBean.class);
                //refreshUi  刷新界面
                refreshUI(bean);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==Constant.GROUP_MAIN_REQEST_CODE && resultCode == Constant.ADD_FRIEND_CODE){
            boolean isAdd = data.getBooleanExtra("isAdd",false);
            if (isAdd) {
                this.isAdd = isAdd;
                requestData();
            }
        }
    }

    private void refreshUI(FriendsBean bean) {
        List<FriendsBean.Data> dataList = bean.getData();
        friendList.clear();
        for (FriendsBean.Data friend:
                dataList) {
            ContactEntity entity = new ContactEntity(friend.getTrueName(),friend.getPhone());
            friendList.add(entity);
        }
        myLog("---------size---------"+friendList.size());
        //mAdapter.notifyDataSetChanged();
        mMembersLv.setAdapter(new MyIndexStickyViewAdapter());
    }

    @Override
    public void requestFailure(String responseFailure, String requestCode) {
        super.requestFailure(responseFailure, requestCode);
    }

    class MyIndexStickyViewAdapter extends IndexStickyViewAdapter<ContactEntity> {

        public MyIndexStickyViewAdapter() {
            super(friendList);
            myLog("-------cons------"+friendList.size());
        }

        @Override
        public RecyclerView.ViewHolder onCreateIndexViewHolder(ViewGroup parent) {
            View view = LayoutInflater.from(GroupMemberActivity.this).inflate(R.layout.indexsticky_item_index, parent, false);
            return new IndexViewHolder(view);
        }

        @Override
        public RecyclerView.ViewHolder onCreateContentViewHolder(ViewGroup parent) {

            View view = LayoutInflater.from(GroupMemberActivity.this).inflate(R.layout.indexsticky_item_contact, parent, false);
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
        }
    }

    class ContentViewHolder extends RecyclerView.ViewHolder {
        TextView mName;
        TextView mMobile;
        ImageView mAvatar;

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
}
