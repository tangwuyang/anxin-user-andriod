package com.anxin.kitchen.fragment.myfragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.anxin.kitchen.MyApplication;
import com.anxin.kitchen.activity.AddNewLocationActivity;
import com.anxin.kitchen.activity.EditAddressActivity;
import com.anxin.kitchen.activity.LocationActivity;
import com.anxin.kitchen.bean.AddressBean;
import com.anxin.kitchen.event.AddressListEvent;
import com.anxin.kitchen.event.AsyncHttpRequestMessage;
import com.anxin.kitchen.fragment.HomeBaseFragment;
import com.anxin.kitchen.user.R;
import com.anxin.kitchen.utils.EventBusFactory;
import com.anxin.kitchen.utils.Log;
import com.anxin.kitchen.utils.StringUtils;
import com.anxin.kitchen.utils.SystemUtility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * 送餐地址界面
 */
public class UserAddressFragment extends HomeBaseFragment implements View.OnClickListener {
    private Log LOG = Log.getLog();
    private View view;
    private ImageView backBtn;//返回
    private Button addUserAddressBtn;//添加送餐地址
    private static final String sendUpdateAddress_http = "sendUpdateAddress";
    private List<AddressBean> addressBeanList = new ArrayList<>();
    private ListView addressListView;
    private MyAdaped myAdaped;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBusFactory.getInstance().register(this);
        hideMainBottom();
        SystemUtility.sendGetAddressHttp();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.user_address_fragment, null);
        initView();//初始化界面控制
        return view;
    }

    private void initView() {
        backBtn = (ImageView) view.findViewById(R.id.back_btn);//返回按钮
        backBtn.setOnClickListener(this);
        addUserAddressBtn = (Button) view.findViewById(R.id.add_address_btn);
        addUserAddressBtn.setOnClickListener(this);
        addressListView = view.findViewById(R.id.address_list);
        addressBeanList = MyApplication.getInstance().getAddressBeanList();
        myAdaped = new MyAdaped();
        addressListView.setAdapter(myAdaped);
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                getFragmentManager().popBackStack();
                break;
            case R.id.add_address_btn:
                Intent addAddress = new Intent(getActivity(), AddNewLocationActivity.class);
                startActivity(addAddress);
                break;
            default:
                break;
        }
    }

    /**
     * 监听网络请求返回
     *
     * @param asyncHttpRequestMessage
     */
    public void onEventMainThread(AddressListEvent asyncHttpRequestMessage) {
//        LOG.e("----------------------------" + MyApplication.getInstance().getCache().getAddressList(getActivity()).toString());
        addressBeanList = MyApplication.getInstance().getAddressBeanList();
        myAdaped.notifyDataSetChanged();
    }

    public void onEventMainThread(AsyncHttpRequestMessage asyncHttpRequestMessage) {
        String requestCode = asyncHttpRequestMessage.getRequestCode();
        String responseMsg = asyncHttpRequestMessage.getResponseMsg();
        String requestStatus = asyncHttpRequestMessage.getRequestStatus();
        switch (requestCode) {
            //验证码发送
            case sendUpdateAddress_http:
                //网络请求返回成功
                if (requestStatus != null && requestStatus.equals(SystemUtility.RequestSuccess)) {
                    //解析验证码返回
                    String code = StringUtils.parserMessage(responseMsg, "code");
                    String data = StringUtils.parserMessage(responseMsg, "data");
                    if (code != null && code.equals("1")) {
                        SystemUtility.sendGetAddressHttp();
                    }
                }
                break;
        }
    }

    class MyAdaped extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return addressBeanList.size();
        }

        @Override
        public AddressBean getItem(int position) {
            // TODO Auto-generated method stub
            return addressBeanList.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View view = convertView;
            final ViewHolder holder;
            if (view == null) {
                holder = new ViewHolder();
                view = LayoutInflater.from(getActivity()).inflate(R.layout.address_item_layout, null);
                holder.addressTitle = view.findViewById(R.id.address_title);
                holder.addressText = view.findViewById(R.id.address_tv);
                holder.addressPhone = view.findViewById(R.id.address_phone);
                holder.addressDefultBtn = view.findViewById(R.id.address_defult);
                holder.addressEditBtn = view.findViewById(R.id.address_edit);
                holder.defult_iv = view.findViewById(R.id.defult_iv);
                holder.content_iv = view.findViewById(R.id.content_iv);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            final AddressBean addressBean = addressBeanList.get(position);
            if (addressBean == null)
                return view;
            String phoneNumber = addressBean.getPhoneNumber();
            String isDefault = addressBean.getIsDefault();
            String contactName = addressBean.getContactName();
            String cityName = addressBean.getCityName();
            String districtName = addressBean.getDistrictName();
            String streetName = addressBean.getStreetName();
            String address = addressBean.getAddress();
            holder.addressPhone.setText(contactName + "  " + phoneNumber);
            String addressTitle = cityName + districtName + streetName;
            holder.addressTitle.setText(addressTitle);
            holder.addressText.setText(address);
            if (isDefault != null && isDefault.equals("1")) {
                holder.defult_iv.setVisibility(View.VISIBLE);
                holder.addressDefultBtn.setVisibility(View.GONE);
                holder.content_iv.setVisibility(View.GONE);
            } else {
                holder.defult_iv.setVisibility(View.GONE);
                holder.addressDefultBtn.setVisibility(View.VISIBLE);
                holder.content_iv.setVisibility(View.VISIBLE);
            }
            //设为默认地址
            holder.addressDefultBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateAddressHttp(addressBean);
                }
            });
            //编辑地址
            holder.addressEditBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent addAddress = new Intent(getActivity(), EditAddressActivity.class);
                    addAddress.putExtra("addressBean", addressBean);
                    startActivity(addAddress);
                }
            });
            return view;
        }

        class ViewHolder {
            private TextView addressTitle, addressText, addressPhone, addressEditBtn, addressDefultBtn;
            private ImageView defult_iv;
            private View content_iv;
        }

    }

    /**
     * 修改地址信息
     */
    private void updateAddressHttp(AddressBean addressBean) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", addressBean.getAddressID());
            jsonObject.put("isDefault", 1);
            jsonObject.put("contactName", addressBean.getContactName());
            jsonObject.put("phone", addressBean.getPhoneNumber());
            jsonObject.put("province", Integer.valueOf(addressBean.getProvinceID()));
            jsonObject.put("city", Integer.valueOf(addressBean.getCityID()));
            jsonObject.put("district", Integer.valueOf(addressBean.getDistrictID()));
            jsonObject.put("street", addressBean.getStreetName());
            jsonObject.put("address", addressBean.getAddress());
            jsonObject.put("longitude", addressBean.getLongitude());
            jsonObject.put("latitude", addressBean.getLatitude());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String urlPath = SystemUtility.sendUpdateAddress();
        Map<String, Object> dataMap = new HashMap();
        dataMap.put("token", SystemUtility.AMToken);
        dataMap.put("formData", jsonObject.toString());
//        Log.e("onEventMainThread", "----------dataMap--------------" + dataMap.toString());
        SystemUtility.requestNetPost(urlPath, dataMap, sendUpdateAddress_http);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusFactory.getInstance().unregister(this);
    }
}
