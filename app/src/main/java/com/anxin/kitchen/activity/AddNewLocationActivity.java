package com.anxin.kitchen.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.anxin.kitchen.bean.AddressBean;
import com.anxin.kitchen.user.R;
import com.anxin.kitchen.utils.Log;
import com.anxin.kitchen.utils.MyService;
import com.anxin.kitchen.utils.ToastUtil;

public class AddNewLocationActivity extends BaseActivity implements View.OnClickListener {
    private ImageView mBackImg;//返回按钮
    private LinearLayout selectAddressRlt;//选择地址
    private TextView street_tv;//定位地址
    private Button addAddressBtn;//完成
    private AddressBean addressBean = null;
    private EditText contactNameEdit;//联系人姓名
    private EditText contactPhontEdit;//联系人电话
    private EditText contactAddressEdit;//详细地址

    private static final int LOCATION_NAME = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_location);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setListeners();
    }

    private void setListeners() {
    }

    private void initView() {
        setTitle("新增地址");
        mBackImg = findViewById(R.id.back_img);//返回按钮
        mBackImg.setOnClickListener(this);
        addAddressBtn = findViewById(R.id.add_address_btn);
        addAddressBtn.setOnClickListener(this);
        selectAddressRlt = findViewById(R.id.select_address_rlt);//选择地址
        selectAddressRlt.setOnClickListener(this);
        street_tv = findViewById(R.id.street_tv);//地址
        contactNameEdit = findViewById(R.id.contact_name_edit);
        contactPhontEdit = findViewById(R.id.contact_phone_edit);
        contactAddressEdit = findViewById(R.id.contact_address_edit);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        Log.e("", "------------requestCode--------" + requestCode);
        switch (requestCode) {
            case LOCATION_NAME:
                if (resultCode == RESULT_OK) {
                    addressBean = (AddressBean) data.getSerializableExtra("AddressBean");
//                    Log.i("", "--------addressBean-------ww--" + addressBean.toString());
                    street_tv.setText(addressBean.getStreetName());
                }
                break;
            default:
                break;

        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_img:
                onBackPressed();
                break;
            case R.id.add_address_btn:
                sendAddAddress();
                break;
            case R.id.select_address_rlt:
                Intent intent = new Intent();
                intent.setClass(AddNewLocationActivity.this, LocationActivity.class);
                startActivityForResult(intent, LOCATION_NAME);
                break;
            default:
                break;
        }
    }

    private void sendAddAddress() {
        String name = contactNameEdit.getText().toString();
        if (name == null || name.length() <= 0) {
            ToastUtil.showToast("请输入联系人姓名");
            return;
        }
        String phone = contactPhontEdit.getText().toString();
        if (phone == null || phone.length() <= 0){
            ToastUtil.showToast("请输入联系人电话号码");
return;
        }
        String address = contactAddressEdit.getText().toString();
        if (address == null || address.length() <= 0) {
            ToastUtil.showToast("请输入详细地址");
            return;
        }
        if (addressBean == null || addressBean.getStreetName().length() <= 0) {
            ToastUtil.showToast("请选择送餐地址");
            return;
        }
    }
}
