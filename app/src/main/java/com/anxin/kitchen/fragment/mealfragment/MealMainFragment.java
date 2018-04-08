package com.anxin.kitchen.fragment.mealfragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.anxin.kitchen.activity.MainActivity;
import com.anxin.kitchen.activity.MessageCenterActivity;
import com.anxin.kitchen.activity.PreserveActivity;
import com.anxin.kitchen.activity.RecoveryMealActivity;
import com.anxin.kitchen.activity.SendMealLocationActivity;
import com.anxin.kitchen.bean.BannerListBean;
import com.anxin.kitchen.bean.MealBean;
import com.anxin.kitchen.fragment.HomeBaseFragment;
import com.anxin.kitchen.user.R;
import com.anxin.kitchen.utils.Log;
import com.anxin.kitchen.utils.SystemUtility;
import com.anxin.kitchen.view.CustomGridView;
import com.anxin.kitchen.view.WaitingDialog;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.youth.banner.Banner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    List<MealBean.Data> mealList;
    private MealBean mealBean;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private WaitingDialog mWaitingDialog;
    String[] mealTypeImgs= new String[] {
            "drawable://" + R.drawable.lunch_icon,
            "drawable://" + R.drawable.diner_icon,
    };
    //设置图片标题:自动对应
    String[] titles=new String[]{"十大星级品牌联盟，全场2折起","全场2折起","十大星级品牌联盟","嗨购5折不要停","12趁现在","嗨购5折不要停，12.12趁现在","实打实大顶顶顶顶"};
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) MainActivity.context;
        mWaitingDialog = new WaitingDialog(getActivity());
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

    public void setBanner(List<BannerListBean.Data> dataList){
        //设置样式,默认为:Banner.NOT_INDICATOR(不显示指示器和标题)
        //可选样式如下:
        //1. Banner.CIRCLE_INDICATOR    显示圆形指示器
        //2. Banner.NUM_INDICATOR   显示数字指示器
        //3. Banner.NUM_INDICATOR_TITLE 显示数字指示器和标题
        //4. Banner.CIRCLE_INDICATOR_TITLE  显示圆形指示器和标题
        String[] bannerTitles = new String[dataList.size()];
        String[] bannerUrls = new String[dataList.size()];
        for (int i = 0; i<dataList.size();i++){
            bannerTitles[i] = dataList.get(i).getBannerName();
            bannerUrls[i] = dataList.get(i).getImg();
        }
        mBanner.setBannerStyle(Banner.CIRCLE_INDICATOR_TITLE);
        //设置轮播样式（没有标题默认为右边,有标题时默认左边）
        //可选样式:
        //Banner.LEFT   指示器居左
        //Banner.CENTER 指示器居中
        //Banner.RIGHT  指示器居右
        mBanner.setIndicatorGravity(Banner.CENTER);
        //设置轮播要显示的标题和图片对应（如果不传默认不显示标题）
        mBanner.setBannerTitle(bannerTitles);
        //设置是否自动轮播（不设置则默认自动）
        mBanner.isAutoPlay(true);
        //设置轮播图片间隔时间（不设置默认为2000）
        mBanner.setDelayTime(5000);

        //设置图片资源:可选图片网址/资源文件，默认用Glide加载,也可自定义图片的加载框架
        //所有设置参数方法都放在此方法之前执行
        //banner.setImages(images);

        //自定义图片加载框架
        mBanner.setImages(bannerUrls, new Banner.OnLoadImageListener() {
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
            mWaitingDialog.show();
            mWaitingDialog.startAnimation();
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
        mPreserverRv.setNestedScrollingEnabled(false);
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
    }

    private void setListener() {
        mMessageImg.setOnClickListener(this);
        mPreserverMealImg.setOnClickListener(this);
        mRecoveryMealImg.setOnClickListener(this);
        mLocationTv.setOnClickListener(this);
    }

    public void closeWaitingDialog(){
        mWaitingDialog.stopAnimation();
        mWaitingDialog.dismiss();
    }

    //设置点餐适配器
    private void setAdapter(List<List<MealBean.Data>> dataList) {
        mLiearManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        PreserverAdapter adapter = new PreserverAdapter(dataList);
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
                Intent intent1 = new Intent(activity,PreserveActivity.class);
                Gson gson = new Gson();
                String mealBeanSt = null;
                if (null != mealBean){
                    mealBeanSt = gson.toJson(mealBean);
                }
                intent1.putExtra("mealListSt",mealBeanSt);
                startActivity(intent1);
                break;
            case R.id.recovery_meal_img:
                Intent intent2 = new Intent(activity,RecoveryMealActivity.class);
                Gson gson2 = new Gson();
                String mealBeanSt2 = null;
                if (null != mealBean){
                    mealBeanSt2 = gson2.toJson(mealBean);
                }
                intent2.putExtra("mealListSt",mealBeanSt2);
                startActivity(intent2);
                break;
            case R.id.location_tv:
                //startNewActivity(SendMealLocationActivity.class);
                Intent intent = new Intent(activity,SendMealLocationActivity.class);
                startActivity(intent);
                activity.overridePendingTransition(R.anim.activity_open,R.anim.activity_close);
                //送餐地址活动
            default:
                break;
        }
    }

    protected void startNewActivity(Class classType) {
        Intent intent = new Intent(getContext(), classType);
        startActivity(intent);
    }

    public void setMeal(MealBean mealBean) {
        //更新首页菜品
        this.mealBean = mealBean;
        mealList = mealBean.getData();
        List<List<MealBean.Data>> dataList = new ArrayList<>();
        List<MealBean.Data> thisDayData = new ArrayList<>();
        long lastDay = 0;
        for (MealBean.Data mel :
                mealList) {
            long thisDay = mel.getMenuDay();
            if (thisDay!=lastDay){
                lastDay = thisDay;
                thisDayData = new ArrayList<>();
                thisDayData.add(mel);
                dataList.add(thisDayData);
            }else {
                thisDayData.add(mel);
            }
        }
        setAdapter(dataList);
    }


    private class PreserverAdapter extends RecyclerView.Adapter<PreserverAdapter.ViewHolderw>{

        List<List<MealBean.Data>> dataList;
        public PreserverAdapter(List<List<MealBean.Data>> dataList) {
            this.dataList = dataList;
        }

        @Override
        public ViewHolderw onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.preserver_item,parent,false);
            ViewHolderw holder = new ViewHolderw(view);
            holder.mPreserverDayRv = view.findViewById(R.id.preserver_rv);
            holder.dateTv = view.findViewById(R.id.date_tv);
            return holder;

        }

        @Override
        public void onBindViewHolder(ViewHolderw holder, int position) {
            GridLayoutManager layoutManage = new GridLayoutManager(getContext(), 2);
            // holder.mPreserverDayRv.setLayoutManager(layoutManage);
            String dataSt = String.valueOf(dataList.get(position).get(0).getMenuDay());
            StringBuffer dateBf = new StringBuffer();
            dateBf.append(dataSt.substring(0,4));
            dateBf.append(".");
            dateBf.append(dataSt.substring(4,6));
            dateBf.append(".");
            dateBf.append(dataSt.substring(6));
            holder.dateTv.setText(dateBf);
            holder.mPreserverDayRv.setAdapter(new PreserverGridAdapter(this.dataList.get(position)));
        }

        @Override
        public int getItemCount() {
            return this.dataList.size();
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
        List<MealBean.Data> dataList;
        public PreserverGridAdapter(List<MealBean.Data> data) {
            this.dataList = data;
        }

        @Override
        public int getCount() {
            return dataList.size();
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
            MealViewHolder holder = null;
            if (null == view) {
                view = LayoutInflater.from(getContext()).inflate(R.layout.preserver_food_item, viewGroup, false);
                holder = new MealViewHolder();
                holder.BackgroundImg = view.findViewById(R.id.main_img);
                holder.iconImg = view.findViewById(R.id.type_img);
                holder.titleTv = view.findViewById(R.id.meal_title_tv);
                holder.contextTv = view.findViewById(R.id.meal_content_tv);
                view.setTag(holder);
            }else {
                holder = (MealViewHolder) view.getTag();
            }
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .showImageForEmptyUri(R.drawable.food1)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
            // holder.BackgroundImg.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            String imgSrc = dataList.get(i).getImg();
            activity.myLog("----------->"+imgSrc);
            imageLoader.displayImage(dataList.get(i).getImg(),holder.BackgroundImg,options);
            int type = dataList.get(i).getEatType();
            if (type ==1){
                imageLoader.displayImage(mealTypeImgs[0],holder.iconImg,options);
            }else if (type == 2){
                imageLoader.displayImage(mealTypeImgs[1],holder.iconImg,options);
            }else if (type == 3){
            }
            holder.titleTv.setText(dataList.get(i).getPackageName());
            List<MealBean.FoodList> foodLists = dataList.get(i).getFoodList();
            StringBuffer foodBf = new StringBuffer();
            for (MealBean.FoodList foodList:
                    foodLists) {
                foodBf.append(foodList.getDishName()+"\r\n");
            }
            holder.contextTv.setText(foodBf.toString());
            return view;
        }
    }

    private class MealViewHolder{
        private ImageView BackgroundImg;
        private ImageView iconImg;
        private TextView titleTv;
        private TextView contextTv;
    }

}
