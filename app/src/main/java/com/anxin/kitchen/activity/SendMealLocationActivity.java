package com.anxin.kitchen.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.anxin.kitchen.MyApplication;
import com.anxin.kitchen.bean.AddressBean;
import com.anxin.kitchen.event.AddressListEvent;
import com.anxin.kitchen.fragment.myfragment.UserAddressFragment;
import com.anxin.kitchen.user.R;
import com.anxin.kitchen.utils.EventBusFactory;
import com.anxin.kitchen.utils.Log;
import com.anxin.kitchen.utils.SystemUtility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SendMealLocationActivity extends Activity implements View.OnClickListener {
    private Log LOG = Log.getLog();
    private ListView mLocationsLv;
    private List<AddressBean> addressBeanList = new ArrayList<>();
    private ImageView mBackImg;
    private LinearLayout mRelocationLl;
    private TextView mAddBt;
    private TextView mLocationTv;
    private ImageView mRelocationImg;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;
    private double lat;
    private double lon;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    private boolean isFirstLoc = true;
    private SendMealLocationAdapter myAdaped;
    private AddressBean defaultAddress = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBusFactory.getInstance().register(this);
        setContentView(R.layout.activity_send_meal_location);
        initView();
        setMyLocation();
        setListeners();
        setAdapter();
    }

    private void setMyLocation() {
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        setUpMap();
    }

    private void setUpMap() {
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(false);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mLocationClient.stopLocation();
        mLocationClient.onDestroy();
    }

    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (isFirstLoc) {
                if (amapLocation != null) {
                    if (amapLocation.getErrorCode() == 0) {
                        //定位成功回调信息，设置相关消息
                        amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                        amapLocation.getLatitude();//获取纬度
                        amapLocation.getLongitude();//获取经度
                        amapLocation.getAccuracy();//获取精度信息
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = new Date(amapLocation.getTime());
                        df.format(date);//定位时间
                        amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                        amapLocation.getCountry();//国家信息
                        amapLocation.getProvince();//省信息
                        amapLocation.getCity();//城市信息
                        amapLocation.getDistrict();//城区信息
                        amapLocation.getStreet();//街道信息
                        amapLocation.getStreetNum();//街道门牌号信息
                        amapLocation.getCityCode();//城市编码
                        amapLocation.getAdCode();//地区编码
                        amapLocation.getAoiName();//获取当前定位点的AOI信息
                        lat = amapLocation.getLatitude();
                        lon = amapLocation.getLongitude();
                        android.util.Log.v("pcw", "lat : " + lat + " lon : " + lon);
                        android.util.Log.v("pcw", "Country : " + amapLocation.getCountry() + " province : " + amapLocation.getProvince() + " City : " + amapLocation.getCity() + " District : " + amapLocation.getDistrict());
                        mLocationTv.setText(amapLocation.getCity() + amapLocation.getDistrict() + amapLocation.getStreet() + amapLocation.getStreetNum());
                        //获取到位置信息 再去获取kitchenId
                        mRelocationImg.clearAnimation();
                    } else {
                        //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                        android.util.Log.e("AmapError", "location Error, ErrCode:"
                                + amapLocation.getErrorCode() + ", errInfo:"
                                + amapLocation.getErrorInfo());
                    }
                }

                isFirstLoc = false;
            }
        }
    };

    private void initView() {
        TextView titleTv = findViewById(R.id.title_tv);
        titleTv.setText("送餐地址");
        mLocationsLv = findViewById(R.id.send_location_lv);
        mBackImg = findViewById(R.id.back_img);
        mRelocationLl = findViewById(R.id.re_location_ll);
        mAddBt = findViewById(R.id.add_bt);
        mLocationTv = findViewById(R.id.location_tv);
        mRelocationImg = findViewById(R.id.re_location_img);
        mLocationsLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                LOG.e("-------------onItemClick---------");
                AddressBean addressBean = addressBeanList.get(i);
                Intent intent = getIntent();
//                LOG.e("-------------addressBean---------" + addressBean.toString());
                intent.putExtra("addressBean", addressBean); //将计算的值回传回去
                //通过intent对象返回结果，必须要调用一个setResult方法，
                //setResult(resultCode, data);第一个参数表示结果返回码，一般只要大于1就可以，但是
                setResult(RESULT_OK, intent);
                finish(); //结束当前的activity的生命周期
            }
        });
    }

    /**
     * 监听网络请求返回
     *
     * @param addressListEvent
     */
    public void onEventMainThread(AddressListEvent addressListEvent) {
//        LOG.e("----------------------------" + MyApplication.getInstance().getCache().getAddressList(getActivity()).toString());
        addressBeanList = MyApplication.getInstance().getAddressBeanList();
        myAdaped.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        defaultAddress = MyApplication.getInstance().getCache().getDefaultAddress(this);
    }

    private void setListeners() {
        mRelocationLl.setOnClickListener(this);
        mBackImg.setOnClickListener(this);
        mAddBt.setOnClickListener(this);
    }

    private void setAdapter() {
        addressBeanList = MyApplication.getInstance().getAddressBeanList();
        myAdaped = new SendMealLocationAdapter();
        mLocationsLv.setAdapter(myAdaped);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.re_location_ll:
                Animation anim = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                anim.setFillAfter(false); // 设置保持动画最后的状态
                anim.setDuration(3000); // 设置动画时间
                anim.setInterpolator(new AccelerateInterpolator()); // 设置插入器
                mRelocationImg.startAnimation(anim);
                break;
            case R.id.back_img:
                onBackPressed();
                break;
            case R.id.add_bt:
                Intent intent = new Intent(this, AddNewLocationActivity.class);
                startActivityForResult(intent, 200);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200) {
//            String location = data.getStringExtra("location");
//            Intent intent = new Intent();
//
//            intent.putExtra("location", location); //将计算的值回传回去
//            //通过intent对象返回结果，必须要调用一个setResult方法，
//            //setResult(resultCode, data);第一个参数表示结果返回码，一般只要大于1就可以，但是
//            setResult(2, intent);
//            finish(); //结束当前的activity的生命周期
            SystemUtility.sendGetAddressHttp();
        }
    }

    private class SendMealLocationAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return addressBeanList.size();
        }

        @Override
        public AddressBean getItem(int position) {
            // TODO Auto-generated method stub
            return addressBeanList.get(position);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            View view = convertView;
            final ViewHolder holder;
            if (view == null) {
                holder = new ViewHolder();
                view = LayoutInflater.from(SendMealLocationActivity.this).inflate(R.layout.send_meal_location_item, null);
                holder.addressTitle = view.findViewById(R.id.location_tv);
                holder.addressPhone = view.findViewById(R.id.contact_tv);
                holder.select_status_img = view.findViewById(R.id.select_status_img);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            final AddressBean addressBean = addressBeanList.get(position);
            if (addressBean == null)
                return view;
            String phoneNumber = addressBean.getPhoneNumber();
            String contactName = addressBean.getContactName();
            String streetName = addressBean.getStreetName();
            holder.addressPhone.setText(contactName + "  " + phoneNumber);
            final String addressTitle = streetName;
            holder.addressTitle.setText(addressTitle);
            if (defaultAddress != null){
                if (defaultAddress.getAddressID().equals(addressBean.getAddressID())){
                    holder.select_status_img.setImageDrawable(getResources().getDrawable(R.drawable.login_selected));
                }else {
                    holder.select_status_img.setImageDrawable(getResources().getDrawable(R.drawable.login_no_selected));
                }
            }
//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    android.util.Log.e("","---------------------");
//                    Log.e("", "---------addressBean------------" + addressBean.toString());
//                    Intent intent = getIntent();
//                    intent.putExtra("addressBean", addressBean); //将计算的值回传回去
//                    //通过intent对象返回结果，必须要调用一个setResult方法，
//                    //setResult(resultCode, data);第一个参数表示结果返回码，一般只要大于1就可以，但是
//                    setResult(2, intent);
//                    finish(); //结束当前的activity的生命周期
//                }
//            });
            return view;
        }

        class ViewHolder {
            private TextView addressTitle, addressPhone;
            private ImageView select_status_img;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusFactory.getInstance().unregister(this);
    }
}
