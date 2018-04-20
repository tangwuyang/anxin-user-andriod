package com.anxin.kitchen.activity;


import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anxin.kitchen.bean.ContactEntity;
import com.anxin.kitchen.decoration.IndexStickyViewDecoration;
import com.anxin.kitchen.interface_.RequestNetListener;
import com.anxin.kitchen.user.R;
import com.anxin.kitchen.utils.Cache;
import com.anxin.kitchen.utils.Constant;
import com.anxin.kitchen.utils.StringUtils;
import com.anxin.kitchen.utils.SystemUtility;
import com.bluetooth.tangwuyang.fantuanlibrary.IndexStickyView;
import com.bluetooth.tangwuyang.fantuanlibrary.adapter.IndexHeaderFooterAdapter;
import com.bluetooth.tangwuyang.fantuanlibrary.adapter.IndexStickyViewAdapter;
import com.bluetooth.tangwuyang.fantuanlibrary.entity.BaseEntity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactsActivity extends BaseActivity implements View.OnClickListener,RequestNetListener{
    private static final String ADD_FRIEND_FROM_CONTACT = "ADD_FRIEND_FROM_CONTACT";
    private Gson mGson;
    private String mGroupName;
    private int mGroupId;
    private IndexStickyView mIndexStickyView;
    private MyIndexStickyViewAdapter mAdapter;
    private IndexHeaderFooterAdapter mBannerAdapter;
    private ImageView mBackImg;
    private String mToken;
    private boolean isAdd = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        setTitle("通讯录朋友");
        mBackImg = findViewById(R.id.back_img);
        mGson = new Gson();
        Intent intent = getIntent();
        if (null != intent){
            mGroupId = intent.getIntExtra("groupId",0);
            mGroupName = intent.getStringExtra("groupName");
        }
        mToken = new Cache(this).getAMToken();
        mBackImg.setOnClickListener(this);
        mIndexStickyView = findViewById(R.id.indexStickyView);
        setFriends();
    }

    private void setFriends() {
        mAdapter = new MyIndexStickyViewAdapter(getContactsList());
        mIndexStickyView.setAdapter(mAdapter);
        mIndexStickyView.addItemDecoration(new IndexStickyViewDecoration(this));
        setSearch();
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
        }
    }

    class MyIndexStickyViewAdapter extends IndexStickyViewAdapter<ContactEntity> {

        public MyIndexStickyViewAdapter(List<ContactEntity> list) {
            super(list);
        }

        @Override
        public RecyclerView.ViewHolder onCreateIndexViewHolder(ViewGroup parent) {
            View view = LayoutInflater.from(ContactsActivity.this).inflate(R.layout.indexsticky_item_index, parent, false);
            return new IndexViewHolder(view);
        }

        @Override
        public RecyclerView.ViewHolder onCreateContentViewHolder(ViewGroup parent) {

            View view = LayoutInflater.from(ContactsActivity.this).inflate(R.layout.add_contact_item, parent, false);
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
            contentViewHolder.mAddTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean addStatus = addFriendToThisGroup(itemData.getMobile(),itemData.getName(),contentViewHolder);
                    if (addStatus){
                        //刷新ui改变边框背景，变成不能点击
                    }else {
                        //提示添加失败
                        // Toast.makeText(ContactsActivity.this,"请求异常，请稍后再试",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


    private ContentViewHolder mHolder;
    private boolean addFriendToThisGroup(String mobile, String name,ContentViewHolder holder) {
        mHolder = holder;
        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("token",mToken);
        Map<String ,Object> formData = new HashMap<>();
        formData.put("groupId",mGroupId);
        formData.put("phone",mobile);
        formData.put("trueName",name);
        String formDataSt = mGson.toJson(formData);
        myLog("--------form---"+formDataSt);
        dataMap.put("formData",formDataSt);
        requestNet(SystemUtility.addFriendToGroupUrl(),dataMap,ADD_FRIEND_FROM_CONTACT);
        return false;
    };

    class IndexViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;

        public IndexViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.tv_index);
        }
    }

    class ContentViewHolder extends RecyclerView.ViewHolder {
        TextView mName;
        TextView mMobile;
        ImageView mAvatar;
        TextView mAddTv;
        public ContentViewHolder(View itemView) {
            super(itemView);
            mAddTv = itemView.findViewById(R.id.add_tv);
            mName = (TextView) itemView.findViewById(R.id.tv_name);
            mMobile = (TextView) itemView.findViewById(R.id.tv_mobile);
            mAvatar = (ImageView) itemView.findViewById(R.id.img_avatar);
        }
    }

    private List<ContactEntity> getContactsList() {
        List<ContactEntity> list = new ArrayList<>();
        //获取联系人信息的Uri
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        //获取ContentResolver
        ContentResolver contentResolver = this.getContentResolver();
        //查询数据，返回Cursor
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        List<Map<String,Object>> list1 = new ArrayList<Map<String,Object>>();

            while (cursor!=null&&cursor.moveToNext()) {

                //获取联系人的ID
                String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                //获取联系人的姓名
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                //查询电话类型的数据操作
                Cursor phones = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId,
                        null, null);
                while (phones.moveToNext()) {
                    String phoneNumber = phones.getString(phones.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.NUMBER));
                    ContactEntity contactEntity = new ContactEntity(name, phoneNumber);
                    list.add(contactEntity);
                }
                phones.close();
            }

        return list;
    }

    private void setSearch() {
        //头部搜索
        mBannerAdapter = new IndexHeaderFooterAdapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {

                View view = LayoutInflater.from(ContactsActivity.this).inflate(R.layout.indexsticky_header_contact_banner, parent, false);
                ImageViewVH vh = new ImageViewVH(view);

                return vh;
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, BaseEntity itemData) {
                ImageViewVH imageViewVH = (ImageViewVH) holder;
                ((ImageViewVH) holder).search_rl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(ContactsActivity.this, "search", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(ContactsActivity.this, "search", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        };
        mIndexStickyView.addIndexHeaderAdapter(mBannerAdapter);
    }

    @Override
    public void requestFailure(String responseFailure, String requestCode) {
        super.requestFailure(responseFailure, requestCode);
    }

    @Override
    public void requestSuccess(String responseString, String requestCode) {
        super.requestSuccess(responseString, requestCode);
        if (null!=responseString && requestCode.equals(ADD_FRIEND_FROM_CONTACT)){
            String status = StringUtils.parserMessage(responseString, Constant.REQUEST_STATUS);
            if (status.equals(Constant.REQUEST_SUCCESS)){
                isAdd = true;
                mHolder.mAddTv.setBackgroundColor(getResources().getColor(R.color.white));
                mHolder.mAddTv.setText("已添加");
                mHolder.mAddTv.setTextColor(getResources().getColor(R.color.shallow_black));
                mHolder.mAddTv.setClickable(false);
            }else {
                Toast.makeText(this, "添加失败", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
