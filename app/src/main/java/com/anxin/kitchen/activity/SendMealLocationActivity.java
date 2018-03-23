package com.anxin.kitchen.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.anxin.kitchen.user.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SendMealLocationActivity extends BaseActivity implements View.OnClickListener{
    private ListView mLocationsLv;
    private List<String> mLocationList = new ArrayList<String>();
    private List<String> mContactList = new ArrayList<>();
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_meal_location);
        initView();
        getData();
        setMyLocation();
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
        setTitle("送餐地址");
        mLocationsLv = findViewById(R.id.send_location_lv);
        mBackImg = findViewById(R.id.back_img);
        mRelocationLl = findViewById(R.id.re_location_ll);
        mAddBt = findViewById(R.id.add_bt);
        mLocationTv = findViewById(R.id.location_tv);
        mRelocationImg = findViewById(R.id.re_location_img);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setListeners();
        setAdapter();
    }

    private void setListeners() {
        mRelocationLl.setOnClickListener(this);
        mBackImg.setOnClickListener(this);
        mAddBt.setOnClickListener(this);
    }

    private void setAdapter() {
        SendMealLocationAdapter adapter = new SendMealLocationAdapter();
        mLocationsLv.setAdapter(adapter);
    }

    private void getData() {
        mLocationList.add("地址:深圳市南山区，西丽大学城");
        mLocationList.add("地址:深圳市南山区，科信科技园");
        mContactList.add("黄先生   138*****4567");
        mContactList.add("胡小姐   138*****4567");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.re_location_ll:
                Animation anim =new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                anim.setFillAfter(false); // 设置保持动画最后的状态
                anim.setDuration(3000); // 设置动画时间
                anim.setInterpolator(new AccelerateInterpolator()); // 设置插入器
                mRelocationImg.startAnimation(anim);


                break;
            case R.id.back_img:
                onBackPressed();
                break;
            case R.id.add_bt:
                startNewActivity(AddNewLocationActivity.class);
                break;
        }
    }


    private class SendMealLocationAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return mContactList.size();
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
        public View getView(int position, View view, ViewGroup viewGroup) {
            view = LayoutInflater.from(SendMealLocationActivity.this).inflate(R.layout.send_meal_location_item,viewGroup,false);
            TextView locationTv = view.findViewById(R.id.location_tv);
            TextView contactTv = view.findViewById(R.id.contact_tv);
            locationTv.setText(mLocationList.get(position));
            contactTv.setText(mContactList.get(position));
            return view;
        }
    }
}
