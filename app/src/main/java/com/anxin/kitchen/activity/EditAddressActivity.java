package com.anxin.kitchen.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anxin.kitchen.bean.AddressBean;
import com.anxin.kitchen.event.AsyncHttpRequestMessage;
import com.anxin.kitchen.user.R;
import com.anxin.kitchen.utils.EventBusFactory;
import com.anxin.kitchen.utils.Log;
import com.anxin.kitchen.utils.StringUtils;
import com.anxin.kitchen.utils.SystemUtility;
import com.anxin.kitchen.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditAddressActivity extends BaseActivity implements View.OnClickListener {
    private ImageView mBackImg;//返回按钮
    private LinearLayout selectAddressRlt;//选择地址
    private TextView street_tv;//定位地址
    private Button addAddressBtn;//完成
    private AddressBean addressBean = null;
    private EditText contactNameEdit;//联系人姓名
    private EditText contactPhontEdit;//联系人电话
    private EditText contactAddressEdit;//详细地址
    private static final String sendAddAddress_http = "sendAddAddress";
    private static final String sendDeleteAddress_http = "sendDeleteAddress";
    private static final int LOCATION_NAME = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBusFactory.getInstance().register(this);
        setContentView(R.layout.activity_add_new_location);
        addressBean = (AddressBean) getIntent().getSerializableExtra("addressBean");
        if (addressBean == null)
            addressBean = new AddressBean();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initView() {
        setTitle("编辑地址");
        mBackImg = findViewById(R.id.back_img);//返回按钮
        mBackImg.setOnClickListener(this);
        addAddressBtn = findViewById(R.id.add_address_btn);
        addAddressBtn.setOnClickListener(this);
        selectAddressRlt = findViewById(R.id.select_address_rlt);//选择地址
        selectAddressRlt.setOnClickListener(this);
        TextView deleteAddress = findViewById(R.id.view_kitchen);//删除地址
        deleteAddress.setOnClickListener(this);
        deleteAddress.setText("删除");
        deleteAddress.setVisibility(View.VISIBLE);
        street_tv = findViewById(R.id.street_tv);//地址
        contactNameEdit = findViewById(R.id.contact_name_edit);
        contactPhontEdit = findViewById(R.id.contact_phone_edit);
        contactAddressEdit = findViewById(R.id.contact_address_edit);
        updateAddress();
    }

    private void updateAddress() {
        contactNameEdit.setText(addressBean.getContactName());
        contactPhontEdit.setText(addressBean.getPhoneNumber());
        contactAddressEdit.setText(addressBean.getAddress());
        street_tv.setText(addressBean.getStreetName());
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
            case R.id.view_kitchen:
                sendDeleteAddress();
                break;
            case R.id.select_address_rlt:
                Intent intent = new Intent();
                intent.setClass(EditAddressActivity.this, LocationActivity.class);
                intent.putExtra("addressBean", addressBean);
                startActivityForResult(intent, LOCATION_NAME);
                break;
            default:
                break;
        }
    }

    private void sendDeleteAddress() {
        String urlPath = SystemUtility.sendDeleteAddress();
        Map<String, Object> dataMap = new HashMap();
        dataMap.put("token", SystemUtility.AMToken);
        dataMap.put("id", addressBean.getAddressID());
        Log.e("onEventMainThread", "----------dataMap--------------" + dataMap.toString());
        SystemUtility.requestNetPost(urlPath, dataMap, sendDeleteAddress_http);
    }

    /**
     * 监听网络请求返回
     *
     * @param asyncHttpRequestMessage
     */
    public void onEventMainThread(AsyncHttpRequestMessage asyncHttpRequestMessage) {
        String requestCode = asyncHttpRequestMessage.getRequestCode();
        String responseMsg = asyncHttpRequestMessage.getResponseMsg();
        String requestStatus = asyncHttpRequestMessage.getRequestStatus();
        String codeToKen = StringUtils.parserMessage(responseMsg, "code");
        if (codeToKen != null && (codeToKen.equals("4") || codeToKen.equals("7"))) {
            SystemUtility.startLoginUser(EditAddressActivity.this);
            return;
        }
        switch (requestCode) {
            //验证码发送
            case sendAddAddress_http:
                //网络请求返回成功
                if (requestStatus != null && requestStatus.equals(SystemUtility.RequestSuccess)) {
                    //解析验证码返回
                    String code = StringUtils.parserMessage(responseMsg, "code");
                    String data = StringUtils.parserMessage(responseMsg, "data");
                    if (code != null && code.equals("1")) {
                        finish();
                        SystemUtility.sendGetAddressHttp();
                    }
                }
                break;
            case sendDeleteAddress_http:
                //网络请求返回成功
                if (requestStatus != null && requestStatus.equals(SystemUtility.RequestSuccess)) {
                    //解析验证码返回
                    String code = StringUtils.parserMessage(responseMsg, "code");
                    String data = StringUtils.parserMessage(responseMsg, "data");
                    if (code != null && code.equals("1")) {
                        finish();
                        SystemUtility.sendGetAddressHttp();
                    }
                }
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
        if (phone == null || phone.length() <= 0) {
            ToastUtil.showToast("请输入联系人电话号码");
            return;
        }
        if (!SystemUtility.isMobileNO(phone)) {
            ToastUtil.showToast("请输入正确的手机号码");
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
        sendAddAddressHttp(name, phone, address);
    }

    /**
     * 修改地址信息
     *
     * @param name
     * @param phone
     * @param address
     */
    private void sendAddAddressHttp(String name, String phone, String address) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", addressBean.getAddressID());
            jsonObject.put("isDefault", Integer.valueOf(addressBean.getIsDefault()));
            jsonObject.put("contactName", name);
            jsonObject.put("phone", phone);
            jsonObject.put("province", Integer.valueOf(addressBean.getProvinceID()));
            jsonObject.put("city", Integer.valueOf(addressBean.getCityID()));
            jsonObject.put("district", Integer.valueOf(addressBean.getDistrictID()));
            jsonObject.put("street", addressBean.getStreetName());
            jsonObject.put("address", address);
            jsonObject.put("longitude", addressBean.getLongitude());
            jsonObject.put("latitude", addressBean.getLatitude());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String urlPath = SystemUtility.sendUpdateAddress();
        Map<String, Object> dataMap = new HashMap();
        dataMap.put("token", SystemUtility.AMToken);
        dataMap.put("formData", jsonObject.toString());
        Log.e("onEventMainThread", "----------dataMap--------------" + dataMap.toString());
        SystemUtility.requestNetPost(urlPath, dataMap, sendAddAddress_http);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //EventBusFactory销毁
        EventBusFactory.getInstance().unregister(this);
    }
}
