package com.anxin.kitchen.activity;

import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptor;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.anxin.kitchen.user.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LocationActivity extends BaseActivity implements AMap.OnCameraChangeListener,AMap.OnMapClickListener,GeocodeSearch.OnGeocodeSearchListener ,TextWatcher,Inputtips.InputtipsListener{
    private String city = "北京";
    private static final String TAG = "LocationActivity";
    private AMap aMap;
    private MapView mapView;
    private LinearLayout mChoseLocationLl;
    private UiSettings mUiSettings;
    private ListView mRelativePositionLv;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    private boolean firstLocation = true;
    private GeocodeSearch geocoderSearch ;
    private AutoCompleteTextView mInputLocationTv;

    private Map<String,String> priviceMap = new HashMap<>();

    {
        priviceMap.put("北京市",11+"");
        priviceMap.put("天津市",12+"");
        priviceMap.put("河北省",13+"");
        priviceMap.put("山西省",14+"");
        priviceMap.put("内蒙古自治区",15+"");
        priviceMap.put("辽宁省",21+"");
        priviceMap.put("吉林省",22+"");
        priviceMap.put("黑龙江省",23+"");
        priviceMap.put("上海市",31+"");
        priviceMap.put("江苏省",32+"");
        priviceMap.put("浙江省",33+"");
        priviceMap.put("安徽省",34+"");
        priviceMap.put("福建省",35+"");
        priviceMap.put("江西省",36+"");
        priviceMap.put("山东省",37+"");
        priviceMap.put("河南省",41+"");
        priviceMap.put("湖北省",42+"");
        priviceMap.put("湖南省",43+"");
        priviceMap.put("广东省",44+"");
        priviceMap.put("广西壮族自治区",45+"");
        priviceMap.put("海南省",46+"");
        priviceMap.put("重庆市",50+"");
        priviceMap.put("四川省",51+"");
        priviceMap.put("贵州省",52+"");
        priviceMap.put("云南省",53+"");
        priviceMap.put("西藏自治区",54+"");
        priviceMap.put("陕西省",61+"");
        priviceMap.put("甘肃省",62+"");
        priviceMap.put("青海省",63+"");
        priviceMap.put("宁夏回族自治区",64+"");
        priviceMap.put("新疆维吾尔自治区",65+"");
        priviceMap.put("台湾省",71+"");
        priviceMap.put("香港特别行政区",81+"");
        priviceMap.put("澳门特别行政区",82+"");

    }
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
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
                    city = amapLocation.getCity();//城市信息
                    amapLocation.getDistrict();//城区信息
                    amapLocation.getStreet();//街道信息
                    amapLocation.getStreetNum();//街道门牌号信息
                    amapLocation.getCityCode();//城市编码
                    amapLocation.getAdCode();//地区编码
                    amapLocation.getAoiName();//获取当前定位点的AOI信息
                    lat = amapLocation.getLatitude();
                    lon = amapLocation.getLongitude();
                    Log.v("pcw", amapLocation.getProvince()+"city"+amapLocation.getCityCode()+ "  dis"+amapLocation.getAdCode());
                    Log.v("pcw", "lat : " + lat + " lon : " + lon);
                    Log.v("pcw", "Country : " + amapLocation.getCountry() + " province : " + amapLocation.getProvince() + " City : " + amapLocation.getCity() + " District : " + amapLocation.getDistrict());

                    // 设置当前地图显示为当前位置
                    moveToLocation();
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(new LatLng(lat, lon));
                    markerOptions.title("当前位置");
                    markerOptions.visible(true);
                    BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.position_1));
                    markerOptions.icon(bitmapDescriptor);
                    aMap.addMarker(markerOptions);


                } else {
                    //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                }
            }
        }
    };



    private void moveToLocation() {
        if (firstLocation) {
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 19));
            firstLocation = false;
        }
    }

    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;
    private double lat;
    private double lon;


    /**
     * * 初始化AMap对象
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            mUiSettings = aMap.getUiSettings();
            mUiSettings.setZoomGesturesEnabled(true);
            mUiSettings.setScrollGesturesEnabled(true);
        }
        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);

        setUpMap();
        ImageView back_img = (ImageView) findViewById(R.id.back_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setUpMap() {
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(true);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(true);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
        aMap.setOnCameraChangeListener(this);// 对amap添加移动地图事件监听器
        aMap.setOnMapClickListener(this);// 对amap添加单击地图事件监听器


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        setTitle("选择您的地址");
        mapView = (MapView) findViewById(R.id.map_view);
        mCameraTextView = findViewById(R.id.test_location_tv);
        mRelativePositionLv = findViewById(R.id.relative_position_lv);
        mInputLocationTv = findViewById(R.id.input_edittext);
        mChoseLocationLl = findViewById(R.id.chose_location_ll);
        //必须要写
        mapView.onCreate(savedInstanceState);
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        mInputLocationTv.addTextChangedListener(this);
        init();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }


    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    private TextView mCameraTextView;
    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        Log.i("change","----------------->"+cameraPosition.toString());
        // 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        String latloginfo = cameraPosition.target.toString();
        String latlog = latloginfo.substring(latloginfo.indexOf("(")+1,latloginfo.indexOf(")"));
        String[] lat_log_s = latlog.split(",");
        double[] lat_log = new double[2];
        lat_log[0] = Double.valueOf(lat_log_s[0]);
        lat_log[1] = Double.valueOf(lat_log_s[1]);
       LatLonPoint point = new LatLonPoint(lat_log[0],lat_log[1]);
        mCameraTextView.setText("onCameraChangeFinish:"
                + cameraPosition.toString() + "   " + lat_log[0] + "   " + lat_log[1]);
        RegeocodeQuery query = new RegeocodeQuery(point, 200,GeocodeSearch.AMAP);
        //GeocodeQuery geocodeQuery = new GeocodeQuery()
        geocoderSearch.getFromLocationAsyn(query);

    }

    @Override
    public void onMapClick(LatLng latLng) {
        Toast.makeText(this, latLng+"", Toast.LENGTH_SHORT).show();
        mCameraTextView.setText("long pressed, point=" + latLng);
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
        Log.i(TAG, "onRegeocodeSearched: ---------------"+ regeocodeResult.toString());
        mRelativePositionLv.setVisibility(View.GONE);
        mCameraTextView.setVisibility(View.VISIBLE);
        mChoseLocationLl.setVisibility(View.VISIBLE);
        String addressName = regeocodeResult.getRegeocodeAddress().getFormatAddress()
                + "附近";
         mCameraTextView.setText("   " + addressName);
        String cityCode = regeocodeResult.getRegeocodeAddress().getCityCode();
        String provice = regeocodeResult.getRegeocodeAddress().getProvince();
        String districtCode = regeocodeResult.getRegeocodeAddress().getAdCode();
        Set<String> proviceSet = priviceMap.keySet();
        String priviceCode = null;
        for (String proviceName :
                proviceSet) {
            if (proviceName.equals(provice)){
                priviceCode = priviceMap.get(proviceName);
            }
        }
        myLog("-------------provice"+priviceCode + "   cityCode"+cityCode + "  dist"+ districtCode);

    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
        Log.i(TAG, "onGeocodeSearched: ---------------"+ geocodeResult);
        mRelativePositionLv.setVisibility(View.GONE);
        mCameraTextView.setText(View.VISIBLE);
        mCameraTextView.setText("long pressed, point=" + geocodeResult.getGeocodeAddressList());
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        mRelativePositionLv.setVisibility(View.VISIBLE);
        mCameraTextView.setVisibility(View.GONE);
        mChoseLocationLl.setVisibility(View.GONE);
        String newText = charSequence.toString().trim();
        InputtipsQuery inputquery = new InputtipsQuery(newText, city);
        inputquery.setCityLimit(true);
        Inputtips inputTips = new Inputtips(LocationActivity.this, inputquery);
        inputTips.setInputtipsListener(this);
        inputTips.requestInputtipsAsyn();
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onGetInputtips(List<Tip> tipList, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            List<HashMap<String, String>> listString = new ArrayList<HashMap<String, String>>();
            for (int i = 0; i < tipList.size(); i++) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("name", tipList.get(i).getName());
                map.put("address", tipList.get(i).getDistrict());
                listString.add(map);
            }
            SimpleAdapter aAdapter = new SimpleAdapter(getApplicationContext(), listString, R.layout.location_item_layout,
                    new String[] {"name","address"}, new int[] {R.id.poi_field_id, R.id.poi_value_id});

            mRelativePositionLv.setAdapter(aAdapter);
            aAdapter.notifyDataSetChanged();

        } else {

        }
    }
}
