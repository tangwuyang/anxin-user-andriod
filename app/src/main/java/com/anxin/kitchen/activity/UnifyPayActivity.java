package com.anxin.kitchen.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anxin.kitchen.bean.MealBean;
import com.anxin.kitchen.bean.OrderInfoBean;
import com.anxin.kitchen.bean.PreMoneyBean;
import com.anxin.kitchen.bean.RecoverBean;
import com.anxin.kitchen.bean.TablewareBean;
import com.anxin.kitchen.interface_.RequestNetListener;
import com.anxin.kitchen.user.R;
import com.anxin.kitchen.utils.Cache;
import com.anxin.kitchen.utils.Constant;
import com.anxin.kitchen.utils.PrefrenceUtil;
import com.anxin.kitchen.utils.StringUtils;
import com.anxin.kitchen.utils.SystemUtility;
import com.anxin.kitchen.view.MyListView;
import com.anxin.kitchen.view.WaitingDialog;
import com.google.gson.reflect.TypeToken;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class UnifyPayActivity extends BaseActivity implements View.OnClickListener ,RequestNetListener{
    private static final String CREATE_DIET = "CREATE_DIET";
    private static final String GET_PRE_MONEY = "GET_PRE_MONEY";
    private static final String ENSURE_MONEY = "ENSURE_MONEY";
    private static final String PAY_MONEY = "PAY_MONEY";
    private static final int GET_LOCATION = 202;
    private String mlocation;
    private ImageView mBackImg;
    private TextView mTitleTv;
    private ImageView mTransmitImg;
    private MyListView mMealLv;
    private MyListView mTablemareLv;
    private MyListView mPayWayLv;
    private TextView mAllMoneyTv;
    private TextView mTablewareMoneyTv;
    private TextView mEnsurePayTv;
    TablewareBean tablewareBean;
    private double allMealMoney = 0;
    private double allTablewareMoeny = 0;
    private static final String GET_TABLEWARE = "GET_TABLEWARE";
    private Cache mCache;
    private PrefrenceUtil mPrefrenceUtil;
    private LinkedHashMap<String ,RecoverBean.Data> mChosedMeals = new LinkedHashMap<>();
    private LinkedList<MealBean.Data> mMeals = new LinkedList<>();
    private long tablewareId;
    private int payType;
    private int kitchenId;
    public LinkedHashMap<Long, Map<String, MealBean.Data>> preMealMaps;
    private boolean isChosedGroup; //选择的是团的方式
    private boolean isChosedBySetNums;//通过选择数量
    private int allNums = 0;  //总数量
    private double tablewareDepositeMoney; //使用押金
    private double tablewareUseMoney;  //使用费用
    private String tablewareName;    //餐具名
    private double sendCost;
    private double allSendCost;   //所有配送费
    private WaitingDialog mdialog;
    private RelativeLayout mLocationRl;
    private TextView mLocationTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unify_pay);
        setTitle("统一支付");
        mLocationTv = findViewById(R.id.location_tv);
        mLocationRl = findViewById(R.id.user_address_rlt);
        mdialog = new WaitingDialog(this,100);
        if (null == mCache) {
            mCache = new Cache(this);
            mToken = mCache.getAMToken();
        }
        kitchenId = new PrefrenceUtil(this).getKitchenId();
        initData();
        getPreMoney();
        initView();
        // getTableWare();
    }


    //后台获取总金额跟押金
    private void getPreMoney() {
        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("token",mToken);
        dataMap.put("kitchenId",kitchenId);
        dataMap.put("tablewareId",tablewareId);
        dataMap.put("timePackages",getPackages());
        dataMap.put("payType",1);
        myLog("--------------->" + mToken +"  " + tablewareId + "  " + getPackages());
        requestNet(SystemUtility.getDeMoneyUrl(),dataMap,GET_PRE_MONEY);
    }


    /**
     * intent.putExtra("meals",data);
     * intent.putExtra("groupTag",isChosedGroup);
     * intent.putExtra("inputNumsTag",isChosedBySetNums);
     * intent.putExtra("nums",allNums);
     * intent.putExtra("tablewareName",tablewareName);
     * intent.putExtra("tablewareUseMoney",tablewareUseMoney);
     * intent.putExtra("tablewareDepositeMoney",tablewareDepositeMoney);
     **/
    private void initData(){
        Intent intent = getIntent();
        allNums = intent.getIntExtra("nums",0);
        tablewareName = intent.getStringExtra("tablewareName");
        tablewareDepositeMoney = intent.getDoubleExtra("tablewareDepositeMoney",0);
        tablewareUseMoney = intent.getDoubleExtra("tablewareUseMoney",0);
        String mealSt = intent.getStringExtra("meals");
        isChosedGroup = intent.getBooleanExtra("groupTag",false);
        isChosedBySetNums = intent.getBooleanExtra("inputNumsTag",false);
        tablewareId = intent.getLongExtra("tableawareId",0);
        sendCost = intent.getDoubleExtra("sendCost",0);
        if (null!=mealSt){
            //还原数据
            preMealMaps = mGson.fromJson(mealSt, new TypeToken<LinkedHashMap<Long ,Map<String,MealBean.Data>>>() {}.getType());
            for (Long day :
                    preMealMaps.keySet()) {
                myLog("-----------day---->"+day);
                Map<String, MealBean.Data> dayData = preMealMaps.get(day);
                if (dayData.containsKey("午餐")){
                    mMeals.add(dayData.get("午餐"));
                }
                if (dayData.containsKey("晚餐")){
                    mMeals.add(dayData.get("晚餐"));
                }
            }
        }
    }

    private void initView() {
        mBackImg = findViewById(R.id.back_img);
        mTitleTv = findViewById(R.id.title_tv);
        mMealLv = findViewById(R.id.meal_lv);
        mPayWayLv = findViewById(R.id.payment_lv);
        mTablemareLv = findViewById(R.id.tableware_lv);
        mTransmitImg = findViewById(R.id.transmit_img);
        mAllMoneyTv = findViewById(R.id.all_money_tv);
        mTablewareMoneyTv  = findViewById(R.id.tableware_money_tv);
        mEnsurePayTv = findViewById(R.id.ensure_pay_tv);
        mMealLv.setAdapter(new MealAdapter());
        mPayWayLv.setAdapter(new PaymentAdapter());
        mTitleTv.setText("确认订单");
        mCache = new Cache(this);
        mPrefrenceUtil = new PrefrenceUtil(this);
        mTransmitImg.setVisibility(View.VISIBLE);
        mBackImg.setOnClickListener(this);
        mEnsurePayTv.setOnClickListener(this);
        mLocationRl.setOnClickListener(this);
    }

    String ids = "";
    @Override
    public void requestSuccess(String responseString, String requestCode) {
        super.requestSuccess(responseString, requestCode);
        String status = StringUtils.parserMessage(responseString, Constant.REQUEST_STATUS);
        if (requestCode==GET_TABLEWARE && status.equals(Constant.REQUEST_SUCCESS)){
            String tempSt = responseString.substring(responseString.indexOf("\"data\":"),responseString.lastIndexOf("}"));
            String tablewareSt = tempSt.substring(tempSt.indexOf(":",0)+1);
            myLog("---------------->"+ tablewareSt);
            tablewareBean = mGson.fromJson(tablewareSt,TablewareBean.class);
            //mTablemareLv.setAdapter(new TablewareAdapter());
        }

        if (requestCode ==  CREATE_DIET && status.equals(Constant.REQUEST_SUCCESS)){
            Toast.makeText(this, "创建订单成功", Toast.LENGTH_SHORT).show();
            PrefrenceUtil prefrenceUtil = new PrefrenceUtil(this);
            /*prefrenceUtil.setRecoverList("");
            prefrenceUtil.setRecoverMenuList("");
            startNewActivity(MainActivity.class);*/
            OrderInfoBean bean = mGson.fromJson(responseString,OrderInfoBean.class);

            //再次获取需要支付的费用
            Map<String ,Object> dataMap = new HashMap<>();

            for (OrderInfoBean.Data data:bean.getData()){
                ids = ids + data.getId()+",";
            }
            ids = ids.substring(0,ids.lastIndexOf(","));
            myLog("-------->"+ids);
            dataMap.put("orderIds",ids);
            dataMap.put("token",mToken);
            requestNet(SystemUtility.getSureDeMoneyUrl(),dataMap,ENSURE_MONEY);
            return;
        }else if (requestCode == CREATE_DIET&& (!status.equals(Constant.REQUEST_SUCCESS))){
            Toast.makeText(this, "创建订单失败", Toast.LENGTH_SHORT).show();
            mdialog.stopAnimation();
            mdialog.dismiss();
        }

        if (requestCode == ENSURE_MONEY&& (status.equals(Constant.REQUEST_SUCCESS))){
            PreMoneyBean bean = mGson.fromJson(responseString,PreMoneyBean.class);
            Map<String ,Object> dataMap = new HashMap<>();
            myLog("--------s>"+ids);
            dataMap.put("orderIds",ids);
            dataMap.put("token",mToken);
            requestNet(SystemUtility.payUrl(),dataMap,PAY_MONEY);

        }else if (requestCode == ENSURE_MONEY&& (!status.equals(Constant.REQUEST_SUCCESS))){
            Toast.makeText(this, "确认订单失败", Toast.LENGTH_SHORT).show();
            mdialog.stopAnimation();
            mdialog.dismiss();
        }

        if (requestCode == PAY_MONEY&& (status.equals(Constant.REQUEST_SUCCESS))){
            Toast.makeText(this, "付款成功", Toast.LENGTH_SHORT).show();
            mdialog.stopAnimation();
            mdialog.dismiss();
            //要修改  跳转到订单活动
            new PrefrenceUtil(this).setPreserveList("");
            startNewActivity(MainActivity.class);
        }else if (requestCode == PAY_MONEY&& (!status.equals(Constant.REQUEST_SUCCESS))){
            Toast.makeText(this, "付款失败", Toast.LENGTH_SHORT).show();
            mdialog.stopAnimation();
            mdialog.dismiss();
        }

        if (requestCode == GET_PRE_MONEY && status.equals(Constant.REQUEST_SUCCESS)){
            PreMoneyBean bean = mGson.fromJson(responseString,PreMoneyBean.class);
            mAllMoneyTv.setText("￥" + (bean.getData().getTotalPay()));
            mTablewareMoneyTv.setText("其中餐具押金" + bean.getData().getPayDeposit() + "元");
        }
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_LOCATION){
            String location = data.getStringExtra("location");
            mLocationTv.setText(location);
            mlocation = location;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_img:
                this.finish();
                break;
            case R.id.transmit_img:
                break;
            case R.id.ensure_pay_tv:
                createOrder();
                break;
            case R.id.user_address_rlt:
                Intent intent = new Intent(this,SendMealLocationActivity.class);
                startActivityForResult(intent,GET_LOCATION);
                break;
        }
    }

    private void createOrder() {
        //获取所有套餐信息
        mdialog.show();
        mdialog.startAnimation();
        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("token",mToken);
        dataMap.put("kitchenId",kitchenId);
        dataMap.put("tablewareId",tablewareId);
        dataMap.put("timePackages",getPackages());
        dataMap.put("payType",1);
        dataMap.put("address",mlocation);
        dataMap.put("contactPhone",mCache.getUserPhone());
        dataMap.put("contactName",mCache.getNickName());
        requestNet(SystemUtility.createOederUrl(),dataMap,CREATE_DIET);
    }

    @NonNull
    private String getPackages() {
        String tempPackages = "";
        for (int i = 0; i<mMeals.size();i++) {
            MealBean.Data meal = mMeals.get(i);
            double littlePrice = meal.getPrice()*meal.getNums();
            BigDecimal b = new BigDecimal(meal.getPrice()*meal.getNums());
            double price = b.setScale(2, BigDecimal.ROUND_UP).doubleValue();
            String dateTem = transToWeekDay(meal.getMenuDay());
            String dateSt = null;
            if (meal.getEatType()==1){
                dateSt = transData(dateTem + " 12:00");
            }else if (meal.getEatType()==2){
                dateSt = transData(dateTem + " 18:00");
            }
            tempPackages = tempPackages+"0*"+dateSt+"*"+meal.getEatType()+"*"+meal.getPackageId()+"*"
                    +meal.getNums()+"*"+price+"0,";
        }
        return tempPackages.substring(0,tempPackages.lastIndexOf(","));
    }


    private String transData(String dataSt){
        SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm" );
        try {
            myLog("----------da----->"+ dataSt);
            Date date = sdf.parse(dataSt);
            String dateSt = String.valueOf(date.getTime());
            myLog("----------da----->"+dataSt + "  " + dateSt);
            return dateSt.substring(0,10);
        } catch (ParseException e) {
            myLog("------------------>异常");
            e.printStackTrace();
        }
        return null;
    }


    private String transToWeekDay(long thisDay) {
        myLog("-------w------>" + thisDay);
        String dataSt = String.valueOf(thisDay);
        StringBuffer dateBf = new StringBuffer();
        dateBf.append(dataSt.substring(0, 4));
        dateBf.append("-");
        dateBf.append(dataSt.substring(4, 6));
        dateBf.append("-");
        dateBf.append(dataSt.substring(6));
        dataSt = dateBf.toString();
        return dataSt;
    }
    private void getAllMoney(){
        for (int i = 0 ; i<mMeals.size();i++){
            MealBean.Data meal = mMeals.get(i);
            double mealMoney = meal.getNums()*meal.getPrice();
            double tablewareDeMoney = tablewareDepositeMoney*meal.getNums();
            double tablewareUseMoney1 = tablewareUseMoney*meal.getNums();
            double sendCosts = sendCost*meal.getNums();
            double thisTablewareMoney = tablewareDeMoney + tablewareUseMoney1 + sendCosts;
            allMealMoney += (mealMoney+ tablewareUseMoney1 + sendCosts);
            allTablewareMoeny += tablewareDeMoney;
        }
    }
    int position = -1;
    class MealAdapter extends BaseAdapter{
        boolean mark = false;
        private LinkedList<RecoverBean.Data> mChosedList = new LinkedList<>();

        public MealAdapter() {
        }

        @Override
        public int getCount() {
            myLog("-----------view-------" +mMeals.size() );
            return mMeals.size();
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
            myLog("--------view----"+i);
            MealBean.Data meal = mMeals.get(i);
            ViewHolder holder = null;
            if (view == null){
                view = LayoutInflater.from(UnifyPayActivity.this).inflate(R.layout.pre_order_meal_item,viewGroup,false);
                holder = new ViewHolder(view);
                view.setTag(holder);
                view.setTag(R.id.position,position);
            }else {
                holder = (ViewHolder) view.getTag();
                view.setTag(R.id.position,position);
            }
            holder.sendTimeTv.setText(meal.getMenuDay()+"");
            holder.mealNums.setText("✘"+meal.getNums());
            holder.mealTitleTv.setText(meal.getPackageName());
            holder.mealContentTv.setText(meal.getFoodList().get(0).getDishName());
            holder.mealNums.setText("✘"+meal.getNums());
            holder.tablewareDeNums.setText("✘"+meal.getNums());
            holder.tablewareUseNUms.setText("✘"+meal.getNums());
            holder.sendNums.setText("✘"+meal.getNums());
            holder.tableDeNameTv.setText(tablewareName+"(押金)");
            holder.tableUseNameTv.setText(tablewareName+"(使用费)");
            holder.tablewareDeReTv.setText("(押金"+tablewareDepositeMoney+"元/份)");
            holder.tablewareUseReTv.setText("(使用费"+tablewareUseMoney+"元/份)");
            myLog("----------------all--->"+view.getTag(R.id.position));
            if (((int)view.getTag(R.id.position)) != i){
                double mealMoney = meal.getNums()*meal.getPrice();
                double tablewareDeMoney = tablewareDepositeMoney*meal.getNums();
                double tablewareUseMoney1 = tablewareUseMoney*meal.getNums();
                double sendCosts = sendCost*meal.getNums();
                double thisTablewareMoney = tablewareDeMoney + tablewareUseMoney1 + sendCosts;
                myLog("----------------all--->"+allTablewareMoeny );
                position = i;
                holder.mealMoneyTv.setText("￥"+mealMoney);
                holder.tablewareDeMoneyTv.setText("￥"+tablewareDeMoney);
                holder.tablewareUseMoneyTv.setText("￥"+tablewareUseMoney1);
                holder.sendMoneyTv.setText("￥"+sendCosts);
                holder.allMoneyTfv.setText("￥"+(sendCosts+tablewareUseMoney1+tablewareDeMoney+mealMoney));
            }

            if (!mark) {
                if (i == (mMeals.size() - 1)) {
                    getAllMoney();
                    mark = true;
                }
            }
            return view;
        }
    }

    class ViewHolder{
        View view;
        TextView sendTimeTv ;
        TextView mealTitleTv ;
        TextView mealContentTv ;
        TextView mealNums ;
        TextView tablewareDeNums;
        TextView tablewareUseNUms;
        TextView sendNums ;
        TextView tableDeNameTv ;
        TextView tableUseNameTv ;
        TextView tablewareDeReTv;
        TextView tablewareUseReTv ;
        TextView mealMoneyTv ;
        TextView tablewareDeMoneyTv;
        TextView tablewareUseMoneyTv;
        TextView sendMoneyTv ;
        TextView allMoneyTfv ;
        public ViewHolder(View view) {
            this.view = view;
            sendTimeTv = view.findViewById(R.id.time_tv);
            mealTitleTv = view.findViewById(R.id.meal_title_tv);
            mealContentTv = view.findViewById(R.id.meal_content_tv);
            mealNums = view.findViewById(R.id.nums_tv);
            tablewareDeNums = view.findViewById(R.id.tableware_nums_tv1);
            tablewareUseNUms = view.findViewById(R.id.tableware_nums_tv2);
            sendNums = view.findViewById(R.id.send_nums_tv);
            tableDeNameTv = view.findViewById(R.id.tableware_de_tv);
            tableUseNameTv = view.findViewById(R.id.tableware_use_tv);
            tablewareDeReTv = view.findViewById(R.id.tablew_g_m_tv);
            tablewareUseReTv = view.findViewById(R.id.tablew_u_m_tv);
            mealMoneyTv = view.findViewById(R.id.money_tv);
            tablewareDeMoneyTv = view.findViewById(R.id.tableware_money_tv1);
            tablewareUseMoneyTv = view.findViewById(R.id.tableware_money_tv2);
            sendMoneyTv = view.findViewById(R.id.send_money_tv);
            allMoneyTfv = view.findViewById(R.id.all_money_tv);
        }
    }

    class PaymentAdapter extends BaseAdapter{
        List<Boolean> selectMark = new ArrayList<>();

        public PaymentAdapter() {
            selectMark.add(true);
            selectMark.add(false);
        }
        @Override
        public int getCount() {
            return 2;
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
        public View getView(final int i, View view, ViewGroup viewGroup) {
            view = LayoutInflater.from(UnifyPayActivity.this).inflate(R.layout.order_paymentway_item,viewGroup,false);
            ImageView payImg = view.findViewById(R.id.pay_img);
            ImageView paySelectImg = view.findViewById(R.id.pay_select_img);
            TextView payTitleTv = view.findViewById(R.id.pay_title_tv);
            if (i == 0){
                payImg.setImageDrawable(getResources().getDrawable(R.drawable.weixin_pay));
                payTitleTv.setText("微信支付");
            }else {
                payImg.setImageDrawable(getResources().getDrawable(R.drawable.zhifubao_pay));
                payTitleTv.setText("支付宝支付");
            }
            if (selectMark.get(i)){
                payType = i+1;
                paySelectImg.setImageDrawable(getResources().getDrawable(R.drawable.selected_drawable));
            }else {
                paySelectImg.setImageDrawable(getResources().getDrawable(R.drawable.unselected_drawable));
            }

            paySelectImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (int j = 0; j<selectMark.size();j++){
                        if (i!=j){
                            selectMark.set(j,false);
                        }else {
                            selectMark.set(j,true);
                        }
                    }
                    PaymentAdapter.this.notifyDataSetChanged();
                }
            });
            return view;
        }
    }
}
