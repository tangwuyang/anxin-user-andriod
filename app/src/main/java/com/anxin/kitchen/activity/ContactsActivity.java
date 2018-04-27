package com.anxin.kitchen.activity;


import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anxin.kitchen.bean.ContactEntity;
import com.anxin.kitchen.decoration.IndexStickyViewDecoration;
import com.anxin.kitchen.interface_.OnGivedPermissionListener;
import com.anxin.kitchen.interface_.RequestNetListener;
import com.anxin.kitchen.user.R;
import com.anxin.kitchen.utils.Cache;
import com.anxin.kitchen.utils.Constant;
import com.anxin.kitchen.utils.LocalContactSearch;
import com.anxin.kitchen.utils.StringUtils;
import com.anxin.kitchen.utils.SystemUtility;
import com.anxin.kitchen.view.RequestLocationPermissionDialog;
import com.bluetooth.tangwuyang.fantuanlibrary.IndexStickyView;
import com.bluetooth.tangwuyang.fantuanlibrary.adapter.IndexHeaderFooterAdapter;
import com.bluetooth.tangwuyang.fantuanlibrary.adapter.IndexStickyViewAdapter;
import com.bluetooth.tangwuyang.fantuanlibrary.entity.BaseEntity;
import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactsActivity extends BaseActivity implements View.OnClickListener, RequestNetListener {
    private static final String ADD_FRIEND_FROM_CONTACT = "ADD_FRIEND_FROM_CONTACT";
    private static final int BAIDU_READ_PHONE_STATE = 100;
    private Gson mGson;
    private String mGroupName;
    private int mGroupId;
    private IndexStickyView mIndexStickyView;
    private MyIndexStickyViewAdapter mAdapter;
    private IndexHeaderFooterAdapter mBannerAdapter;
    private ImageView mBackImg;
    private String mToken;
    private boolean isAdd = false;
    ArrayList<ContactEntity> list;
    private Handler mhander = new Handler(){
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    boolean hasContactsPermission = msg.getData().getBoolean("hasPermission",false);
                    myLog("-------------->" + hasContactsPermission);
                    if (!hasContactsPermission) {
                        popRequestWindow();
                    }else {
                        setFriends();
                    }
                    break;
            }
        }
    };
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        setTitle("通讯录朋友");
        mBackImg = findViewById(R.id.back_img);
        mGson = new Gson();
        Intent intent = getIntent();
        if (null != intent) {
            mGroupId = intent.getIntExtra("groupId", 0);
            mGroupName = intent.getStringExtra("groupName");
        }

        requestPermission();
        mToken = new Cache(this).getAMToken();
        mBackImg.setOnClickListener(this);
        mIndexStickyView = findViewById(R.id.indexStickyView);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestPermission() {
        new Thread(){
            @Override
            public void run() {
                super.run();
                boolean hasContactsPermission = getContactsPermission();
                Message message = new Message();
                message.what = 1;
                Bundle bundle = new Bundle();
                bundle.putBoolean("hasPermission",hasContactsPermission);
                message.setData(bundle);
                mhander.sendMessage(message);
            }
        }.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void popRequestWindow() {

        requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, BAIDU_READ_PHONE_STATE);
       /* RequestLocationPermissionDialog dialog = new RequestLocationPermissionDialog(this, new OnGivedPermissionListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onGivedPermssion() {
                //ActivityCompat.requestPermissions(ContactsActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, BAIDU_READ_PHONE_STATE);
                ActivityCompat.requestPermissions(ContactsActivity.this,new String[]{Manifest.permission.WRITE_CONTACTS,Manifest.permission.READ_CONTACTS}, BAIDU_READ_PHONE_STATE);
            }
        });*/
        //dialog.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        setFriends();
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    private boolean getContactsPermission() {


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED
                ) {
            return false;
        } else return true;
    }

    private void setFriends() {
        mAdapter = new MyIndexStickyViewAdapter(getContactsList());
        mIndexStickyView.setAdapter(mAdapter);
        mIndexStickyView.addItemDecoration(new IndexStickyViewDecoration(this));
        setSearch();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
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
                    boolean addStatus = addFriendToThisGroup(itemData.getMobile(), itemData.getName(), contentViewHolder);
                    if (addStatus) {
                        //刷新ui改变边框背景，变成不能点击
                    } else {
                        //提示添加失败
                        // Toast.makeText(ContactsActivity.this,"请求异常，请稍后再试",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


    private ContentViewHolder mHolder;

    private boolean addFriendToThisGroup(String mobile, String name, ContentViewHolder holder) {
        mHolder = holder;
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("token", mToken);
        Map<String, Object> formData = new HashMap<>();
        formData.put("groupId", mGroupId);
        formData.put("phone", mobile);
        formData.put("trueName", name);
        String formDataSt = mGson.toJson(formData);
        myLog("--------form---" + formDataSt);
        dataMap.put("formData", formDataSt);
        requestNet(SystemUtility.addFriendToGroupUrl(), dataMap, ADD_FRIEND_FROM_CONTACT);
        return false;
    }

    ;

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
        list = new ArrayList<>();
        //获取联系人信息的Uri
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        //获取ContentResolver
        ContentResolver contentResolver = this.getContentResolver();
        //查询数据，返回Cursor
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();

        while (cursor != null && cursor.moveToNext()) {

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

                View view = LayoutInflater.from(ContactsActivity.this).inflate(R.layout.top_search_layout, parent, false);
                ImageViewVH vh = new ImageViewVH(view);

                return vh;
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, BaseEntity itemData) {
                ImageViewVH imageViewVH = (ImageViewVH) holder;
               /* ((ImageViewVH) holder).search_rl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(ContactsActivity.this, "search", Toast.LENGTH_SHORT).show();
                    }
                });*/
            }

            class ImageViewVH extends RecyclerView.ViewHolder {
                EditText searchEt;
                TextView searchTv;
                public ImageViewVH(View itemView) {
                    super(itemView);
                    searchEt = (EditText) itemView.findViewById(R.id.search_et);
                    /*searchEt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(ContactsActivity.this, "search", Toast.LENGTH_SHORT).show();
                        }
                    });*/
                    searchEt.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });
                    searchTv = itemView.findViewById(R.id.search_tv);
                    searchTv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String searchSt = searchEt.getText().toString();
                            myLog("---------------->searchSt "+searchSt);

                            if (searchSt != null && searchSt.length() > 0) {
                                ArrayList<ContactEntity> listG = LocalContactSearch.searchContact(searchSt, list);
                                myLog("--------------"+listG.size());
                                mAdapter = new MyIndexStickyViewAdapter(listG);
                                mIndexStickyView.setAdapter(mAdapter);
                                mIndexStickyView.addItemDecoration(new IndexStickyViewDecoration(ContactsActivity.this));
                                setSearch();
                            } else {
                                myLog("--------------------------");
                                if (searchSt==null || searchSt.length()==0){
                                    if (null != list){
                                        mAdapter = new MyIndexStickyViewAdapter(list);
                                        mIndexStickyView.setAdapter(mAdapter);
                                        mIndexStickyView.addItemDecoration(new IndexStickyViewDecoration(ContactsActivity.this));
                                        setSearch();
                                    }
                                }
                            }
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
        if (null != responseString && requestCode.equals(ADD_FRIEND_FROM_CONTACT)) {
            String status = StringUtils.parserMessage(responseString, Constant.REQUEST_STATUS);
            if (status.equals(Constant.REQUEST_SUCCESS)) {
                isAdd = true;
                mHolder.mAddTv.setBackgroundColor(getResources().getColor(R.color.white));
                mHolder.mAddTv.setText("已添加");
                mHolder.mAddTv.setTextColor(getResources().getColor(R.color.shallow_black));
                mHolder.mAddTv.setClickable(false);
            } else {
                Toast.makeText(this, "添加失败", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
