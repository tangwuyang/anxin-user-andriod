package com.anxin.kitchen.fragment.mealfragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.model.BitmapDescriptor;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
import com.anxin.kitchen.activity.LocationActivity;
import com.anxin.kitchen.activity.MainActivity;
import com.anxin.kitchen.activity.MessageCenterActivity;
import com.anxin.kitchen.activity.PreserveActivity;
import com.anxin.kitchen.activity.RecoveryMealActivity;
import com.anxin.kitchen.activity.SendMealLocationActivity;
import com.anxin.kitchen.fragment.HomeBaseFragment;
import com.anxin.kitchen.user.R;
import com.anxin.kitchen.utils.EventBusFactory;
import com.anxin.kitchen.utils.Log;
import com.anxin.kitchen.utils.SystemUtility;
import com.anxin.kitchen.view.CustomGridView;
import com.bumptech.glide.Glide;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.youth.banner.Banner;

import org.apache.http.Header;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.ContentValues.TAG;
import static com.umeng.analytics.AnalyticsConfig.getLocation;

/**
 * 点餐主界面
 */
public class MealMainFragment extends HomeBaseFragment implements View.OnClickListener {
    private Log LOG = Log.getLog();
    private View view;
    private RecyclerView mPreserverRv;
    private LinearLayoutManager mLiearManager;
    private ImageView mMessageImg;  //消息中心
    private ImageView mRecoveryMealImg;   //康复食疗
    private ImageView mPreserverMealImg;  //预约点餐
    private TextView mLocationTv;
    private Banner mBanner;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;
    private double lat;
    private double lon;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    private MainActivity activity;

    String[] images= new String[] {
            "http://218.192.170.132/BS80.jpg",
            "http://img.zcool.cn/community/0166c756e1427432f875520f7cc838.jpg",
            "http://img.zcool.cn/community/018fdb56e1428632f875520f7b67cb.jpg",
            "http://img.zcool.cn/community/01c8dc56e1428e6ac72531cbaa5f2c.jpg",
            "http://img.zcool.cn/community/01fda356640b706ac725b2c8b99b08.jpg",
            "http://img.zcool.cn/community/01fd2756e142716ac72531cbf8bbbf.jpg",
            "http://img.zcool.cn/community/0114a856640b6d32f87545731c076a.jpg"};
    //设置图片标题:自动对应
    String[] titles=new String[]{"十大星级品牌联盟，全场2折起","全场2折起","十大星级品牌联盟","嗨购5折不要停","12趁现在","嗨购5折不要停，12.12趁现在","实打实大顶顶顶顶"};
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
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
                    mLocationTv.setText(amapLocation.getCity()+amapLocation.getDistrict()+amapLocation.getStreet()+amapLocation.getStreetNum());
                    //获取到位置信息 再去获取kitchenId
                    getKitchenId();
                } else {
                    //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                    android.util.Log.e("AmapError", "location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                }
            }
        }
    };

    public void setBanner(){
        //设置样式,默认为:Banner.NOT_INDICATOR(不显示指示器和标题)
        //可选样式如下:
        //1. Banner.CIRCLE_INDICATOR    显示圆形指示器
        //2. Banner.NUM_INDICATOR   显示数字指示器
        //3. Banner.NUM_INDICATOR_TITLE 显示数字指示器和标题
        //4. Banner.CIRCLE_INDICATOR_TITLE  显示圆形指示器和标题
        mBanner.setBannerStyle(Banner.CIRCLE_INDICATOR_TITLE);
        //设置轮播样式（没有标题默认为右边,有标题时默认左边）
        //可选样式:
        //Banner.LEFT   指示器居左
        //Banner.CENTER 指示器居中
        //Banner.RIGHT  指示器居右
        mBanner.setIndicatorGravity(Banner.CENTER);
        //设置轮播要显示的标题和图片对应（如果不传默认不显示标题）
        mBanner.setBannerTitle(titles);
        //设置是否自动轮播（不设置则默认自动）
        mBanner.isAutoPlay(true);
        //设置轮播图片间隔时间（不设置默认为2000）
        mBanner.setDelayTime(5000);

        //设置图片资源:可选图片网址/资源文件，默认用Glide加载,也可自定义图片的加载框架
        //所有设置参数方法都放在此方法之前执行
        //banner.setImages(images);

        //自定义图片加载框架
        mBanner.setImages(images, new Banner.OnLoadImageListener() {
            @Override
            public void OnLoadImage(ImageView view, Object url) {
                System.out.println("加载中");
                Glide.with(activity.getApplicationContext()).load(url).into(view);
                System.out.println("加载完");
            }
        });
        //设置点击事件，下标是从1开始
        mBanner.setOnBannerClickListener(new Banner.OnBannerClickListener() {//设置点击事件
            @Override
            public void OnBannerClick(View view, int position) {
                Toast.makeText(activity.getApplicationContext(), "你点击了：" + position, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getKitchenId() {
        if (null != activity){
            Map<String,Object> dataMap = new HashMap();
            dataMap.put("longitude",lon);
            dataMap.put("latitude",lat);
            activity.myLog("------------->开始请求" + lon + "  " +lat);
            activity.requestNet(SystemUtility.getNearKitchenId(),dataMap,activity.GET_KITCHEN_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.meal_main_fragment, container,false);
        mPreserverRv = view.findViewById(R.id.preserver_rv);
        mPreserverRv.setFocusable(false);
        mMessageImg = view.findViewById(R.id.message_img);
        mRecoveryMealImg = view.findViewById(R.id.recovery_meal_img);
        mPreserverMealImg = view.findViewById(R.id.preserver_meal_img);
        mLocationTv = view.findViewById(R.id.location_tv);
        mBanner = view.findViewById(R.id.broadcast_banner);
        setMyLocation();
        return view;
    }

    private void setMyLocation() {
        mLocationClient = new AMapLocationClient(getActivity().getApplicationContext());
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
        mLocationOption.setOnceLocation(true);
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
    private void initView() {
        mPreserverRv = getView().findViewById(R.id.preserver_rv);
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        setListener();
        setAdapter();
    }

    private void setListener() {
        mMessageImg.setOnClickListener(this);
        mPreserverMealImg.setOnClickListener(this);
        mRecoveryMealImg.setOnClickListener(this);
        mLocationTv.setOnClickListener(this);
    }

    //设置点餐适配器
    private void setAdapter() {
        mLiearManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        PreserverAdapter adapter = new PreserverAdapter();
        mPreserverRv.setLayoutManager(mLiearManager);
        mPreserverRv.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.message_img:
                startNewActivity(MessageCenterActivity.class);
                break;
            case R.id.preserver_meal_img:
                startNewActivity(PreserveActivity.class);
                break;
            case R.id.recovery_meal_img:
                startNewActivity(RecoveryMealActivity.class);
                break;
            case R.id.location_tv:
                startNewActivity(SendMealLocationActivity.class);  //送餐地址活动
            default:
                break;
        }
    }

    protected void startNewActivity(Class classType) {
        Intent intent = new Intent(getContext(), classType);
        startActivity(intent);
    }


    private class PreserverAdapter extends RecyclerView.Adapter<PreserverAdapter.ViewHolderw>{


        @Override
        public ViewHolderw onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.preserver_item,parent,false);
            ViewHolderw holder = new ViewHolderw(view);
            holder.mPreserverDayRv = view.findViewById(R.id.preserver_rv);
            return holder;

        }

        @Override
        public void onBindViewHolder(ViewHolderw holder, int position) {
            GridLayoutManager layoutManage = new GridLayoutManager(getContext(), 2);
            // holder.mPreserverDayRv.setLayoutManager(layoutManage);
            holder.mPreserverDayRv.setAdapter(new PreserverGridAdapter());
        }

        @Override
        public int getItemCount() {
            return 2;
        }

        public class ViewHolderw extends RecyclerView.ViewHolder {
            private TextView dateTv;
            private CustomGridView mPreserverDayRv;
            public ViewHolderw(View itemView) {
                super(itemView);
            }
        }
    }

    private class PreserverFoodAdapter extends RecyclerView.Adapter<PreserverFoodAdapter.ViewHolder>{

        @Override
        public PreserverFoodAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.preserver_food_item,parent,false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(PreserverFoodAdapter.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 4;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public ViewHolder(View itemView) {
                super(itemView);
            }
        }
    }

    private class PreserverGridAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return 4;
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
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.preserver_food_item,viewGroup,false);
            return view;
        }
    }}
