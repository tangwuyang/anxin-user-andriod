package com.anxin.kitchen.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.geocoder.RegeocodeRoad;
import com.amap.api.services.geocoder.StreetNumber;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.anxin.kitchen.MyApplication;
import com.anxin.kitchen.bean.AddressBean;
import com.anxin.kitchen.bean.PoiBean;
import com.anxin.kitchen.user.R;
import com.anxin.kitchen.utils.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LocationActivity extends Activity implements AMap.OnCameraChangeListener, AMap.OnMapClickListener, GeocodeSearch.OnGeocodeSearchListener, TextWatcher, Inputtips.InputtipsListener, PoiSearch.OnPoiSearchListener {
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
    private GeocodeSearch geocoderSearch;
    private AutoCompleteTextView mInputLocationTv;
    private TextView locationTv;//定位城市
    private List<AddressBean> addressBeansList = new ArrayList();//地址列表
    private AddressBean myAddressBean = null;
    private MyAdaped myAdaped;//列表适配器

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
                    locationTv.setText(city);
                    Log.v("pcw", amapLocation.getProvince() + "city" + amapLocation.getCityCode() + "  dis" + amapLocation.getAdCode());
                    Log.v("pcw", "lat : " + lat + " lon : " + lon);
                    Log.v("pcw", "Country : " + amapLocation.getCountry() + " province : " + amapLocation.getProvince() + " City : " + amapLocation.getCity() + " District : " + amapLocation.getDistrict() + "street" + amapLocation.getStreet());
                    Log.v("pcw", "street" + amapLocation.getStreet() + "streetNum" + amapLocation.getStreetNum());
                    Log.v("pcw", "getAddress" + amapLocation.getAddress());
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
        myAdaped = new MyAdaped();
        mRelativePositionLv.setAdapter(myAdaped);
        mRelativePositionLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AddressBean addressBean = addressBeansList.get(i);
                myAddressBean.setCityName(addressBean.getCityName());
                myAddressBean.setStreetName(addressBean.getStreetName());
                myAddressBean.setDistrictName(addressBean.getDistrictName());
                myAddressBean.setProvinceName(addressBean.getProvinceName());
                myAddressBean.setLatitude(addressBean.getLatitude());
                myAddressBean.setLongitude(addressBean.getLongitude());
//                Log.e("", "------AddressBean----------");
                if (addressBean != null) {
//                    ToastUtil.showToast(addressBean.getStreetName());
                    Intent intent = getIntent();
                    intent.putExtra("AddressBean", myAddressBean);
                    setResult(RESULT_OK, intent);
//                    Log.e("", "------AddressBean------setResult----");
                }
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
        mapView = (MapView) findViewById(R.id.map_view);
        mCameraTextView = findViewById(R.id.test_location_tv);
        mRelativePositionLv = findViewById(R.id.relative_position_lv);
        mInputLocationTv = findViewById(R.id.input_edittext);
        mChoseLocationLl = findViewById(R.id.chose_location_ll);
        TextView titleTv = findViewById(R.id.title_tv);
        titleTv.setText("请选择您的地址");
        locationTv = findViewById(R.id.location_tv);
        //必须要写
        mapView.onCreate(savedInstanceState);
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        mInputLocationTv.addTextChangedListener(this);
        myAddressBean = (AddressBean) getIntent().getSerializableExtra("addressBean");
        if (myAddressBean == null)
            myAddressBean = new AddressBean();
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
        Log.i("change", "----------------->" + cameraPosition.toString());
        // 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        String latloginfo = cameraPosition.target.toString();
        String latlog = latloginfo.substring(latloginfo.indexOf("(") + 1, latloginfo.indexOf(")"));
        LatLng latLng = cameraPosition.target;
        String[] lat_log_s = latlog.split(",");
        double[] lat_log = new double[2];
        lat_log[0] = Double.valueOf(lat_log_s[0]);
        lat_log[1] = Double.valueOf(lat_log_s[1]);
        LatLonPoint point = new LatLonPoint(lat_log[0], lat_log[1]);
//        mCameraTextView.setText("onCameraChangeFinish:"
//                + cameraPosition.toString() + "   " + lat_log[0] + "   " + lat_log[1]);
        RegeocodeQuery query = new RegeocodeQuery(point, 200, GeocodeSearch.AMAP);
        RegeocodeQuery geocodeQuery = new RegeocodeQuery(new LatLonPoint(latLng.latitude, latLng.longitude), 200, GeocodeSearch.AMAP);
        geocoderSearch.getFromLocationAsyn(query);
        doSearchQuery(city, latLng.latitude, latLng.longitude);

    }

    /**
     * 开始进行poi搜索
     */
    protected void doSearchQuery(String city, double latitude, double longitude) {
        String mType = "|住宿服务|风景名胜|商务住宅|地名地址信息";
        query = new PoiSearch.Query("", mType, city);// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setPageSize(20);// 设置每页最多返回多少条poiitem
        query.setPageNum(1);// 设置查第一页
        poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(this);
        //以当前定位的经纬度为准搜索周围5000米范围
        // 设置搜索区域为以lp点为圆心，其周围5000米范围
        poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(latitude, longitude), 1000, true));//
        poiSearch.searchPOIAsyn();// 异步搜索
    }

    int currentPage;
    private PoiSearch.Query query;// Poi查询条件类
    private PoiSearch poiSearch;

    @Override
    public void onMapClick(LatLng latLng) {
      /*  Toast.makeText(this, latLng + "", Toast.LENGTH_SHORT).show();
        mCameraTextView.setText("long pressed, point=" + latLng);*/
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
        Log.i(TAG, "onRegeocodeSearched: ---------------" + regeocodeResult.toString());
      /*  mRelativePositionLv.setVisibility(View.GONE);
        mCameraTextView.setVisibility(View.VISIBLE);
        mChoseLocationLl.setVisibility(View.VISIBLE);*/
        String addressName = regeocodeResult.getRegeocodeAddress().getFormatAddress()
                + "附近";

        mCameraTextView.setText("   " + addressName);
        locationTv.setText(regeocodeResult.getRegeocodeAddress().getCity());
        //从服务器获取地址ID转换
//        String city = regeocodeResult.getRegeocodeAddress().getCity();
//        String provice = regeocodeResult.getRegeocodeAddress().getProvince();
//        String district = regeocodeResult.getRegeocodeAddress().getDistrict();
//        String cityID = MyApplication.getInstance().getAddressNameToID(city);
//        String proviceID = MyApplication.getInstance().getAddressNameToID(provice);
//        String districtID = MyApplication.getInstance().getAddressNameToID(district);
//        String builder = regeocodeResult.getRegeocodeAddress().getBuilding();
//        List<RegeocodeRoad> street = regeocodeResult.getRegeocodeAddress().getRoads();
//        StreetNumber streetNumber = regeocodeResult.getRegeocodeAddress().getStreetNumber();
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
        Log.i(TAG, "onGeocodeSearched: ---------------" + geocodeResult);
       /* mRelativePositionLv.setVisibility(View.GONE);
        mCameraTextView.setText(View.VISIBLE);
        mCameraTextView.setText("long pressed, point=" + geocodeResult.getGeocodeAddressList());*/
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
            addressBeansList.clear();
            for (int i = 0; i < tipList.size(); i++) {
                Tip tip = tipList.get(i);
                AddressBean addressBean = new AddressBean();
                String district = tip.getDistrict();
                String province = district.substring(0, district.indexOf("省") + 1);
                String city = district.substring(district.indexOf("省") + 1, district.indexOf("市") + 1);
                String distri = district.substring(district.indexOf("市") + 1, district.length());
                addressBean.setProvinceName(province);
                addressBean.setCityName(city);
                addressBean.setDistrictName(distri);
                addressBean.setStreetName(tip.getName());
                addressBean.setLongitude(tip.getPoint().getLongitude() + "");
                addressBean.setLatitude(tip.getPoint().getLatitude() + "");
//                myLog("------AddressBean---------" + addressBean.toString());
                addressBeansList.add(addressBean);
            }
            myAdaped.notifyDataSetChanged();
        } else {

        }
    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int arg1) {
        if (arg1 == 1000) {
            if (poiResult != null && poiResult.getQuery() != null) {// 搜索poi的结果
                if (poiResult.getQuery().equals(query)) {// 是否是同一条
                    List<PoiItem> poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
                    addressBeansList.clear();
                    if (poiItems != null && poiItems.size() > 0) {
                        for (int i = 0; i < poiItems.size(); i++) {
                            PoiItem poiItem = poiItems.get(i);   //写一个bean，作为数据存储
                            AddressBean bean = new AddressBean();
                            bean.setProvinceName(poiItem.getProvinceName());
                            bean.setCityName(poiItem.getCityName());
                            bean.setDistrictName(poiItem.getAdName());
                            bean.setStreetName(poiItem.getTitle());
                            bean.setLatitude(poiItem.getLatLonPoint().getLatitude() + "");
                            bean.setLongitude(poiItem.getLatLonPoint().getLongitude() + "");
//                            myLog("------AddressBean---------" + bean.toString());
                            addressBeansList.add(bean);
                        }

                        mRelativePositionLv.setVisibility(View.VISIBLE);
                        mCameraTextView.setVisibility(View.GONE);
                        mChoseLocationLl.setVisibility(View.GONE);
                        myAdaped.notifyDataSetChanged();
                      /*  poiData.addAll(tem);
                        mAdapter.notifyDataSetChanged();  //解析成功更新list布局*/
                    }
                }
            }
        }
    }

    class MyAdaped extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return addressBeansList.size();
        }

        @Override
        public AddressBean getItem(int position) {
            // TODO Auto-generated method stub
            return addressBeansList.get(position);
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
                view = LayoutInflater.from(LocationActivity.this).inflate(R.layout.location_item_layout, null);
                holder.group_name = (TextView) view.findViewById(R.id.poi_field_id);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            String addressName = addressBeansList.get(position).getStreetName();
            if (addressName != null && addressName.length() != 0)
                holder.group_name.setText(addressName);
            return view;
        }

        class ViewHolder {
            private TextView group_name;
        }

    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }
}
