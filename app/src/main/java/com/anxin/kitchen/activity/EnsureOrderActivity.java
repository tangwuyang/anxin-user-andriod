package com.anxin.kitchen.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.anxin.kitchen.bean.RecoverBean;
import com.anxin.kitchen.bean.TablewareBean;
import com.anxin.kitchen.interface_.RequestNetListener;
import com.anxin.kitchen.user.R;
import com.anxin.kitchen.utils.Constant;
import com.anxin.kitchen.utils.StringUtils;
import com.anxin.kitchen.utils.SystemUtility;
import com.anxin.kitchen.view.MyListView;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class EnsureOrderActivity extends BaseActivity implements View.OnClickListener ,RequestNetListener{
    private ImageView mBackImg;
    private TextView mTitleTv;
    private ImageView mTransmitImg;
    private MyListView mMealLv;
    private MyListView mTablemareLv;
    private MyListView mPayWayLv;
    private TextView mAllMoneyTv;
    private TextView mTablewareMoneyTv;
    TablewareBean tablewareBean;
    private double mealMoney = 0;
    private double tablewareMoeny = 0;
    private static final String GET_TABLEWARE = "GET_TABLEWARE";
    private LinkedHashMap<String ,RecoverBean.Data> mChosedMeals = new LinkedHashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ensure_order);
        initView();
        getTableWare();
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
        String chosedMealSt = getIntent().getStringExtra("chosedMeal");
        mChosedMeals = mGson.fromJson(chosedMealSt, new TypeToken<LinkedHashMap<String ,RecoverBean.Data>>() {}.getType());
        mMealLv.setAdapter(new MealAdapter());
        mPayWayLv.setAdapter(new PaymentAdapter());
        mTitleTv.setText("确认订单");
        mTransmitImg.setVisibility(View.VISIBLE);
        mBackImg.setOnClickListener(this);
    }

    @Override
    public void requestSuccess(String responseString, String requestCode) {
        super.requestSuccess(responseString, requestCode);
        String status = StringUtils.parserMessage(responseString, Constant.REQUEST_STATUS);
        if (requestCode==GET_TABLEWARE && status.equals(Constant.REQUEST_SUCCESS)){
            String tempSt = responseString.substring(responseString.indexOf("\"data\":"),responseString.lastIndexOf("}"));
            String tablewareSt = tempSt.substring(tempSt.indexOf(":",0)+1);
            myLog("---------------->"+ tablewareSt);
            tablewareBean = mGson.fromJson(tablewareSt,TablewareBean.class);
            mTablemareLv.setAdapter(new TablewareAdapter());
        }
    }

    //获取所有餐具
    private void getTableWare() {
        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("page",1);
        requestNet(SystemUtility.getTablewareListUrl(),dataMap,GET_TABLEWARE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_img:
                this.finish();
                break;
            case R.id.transmit_img:
                break;
        }
    }


    class MealAdapter extends BaseAdapter{
        private LinkedList<RecoverBean.Data> mChosedList = new LinkedList<>();

        public MealAdapter() {
            for (String mealName:mChosedMeals.keySet()
                    ) {
                RecoverBean.Data meal = mChosedMeals.get(mealName);
                mealMoney = mealMoney + meal.getPrice()*meal.getNums();
                mChosedList.add(meal);
            }
        }

        @Override
        public int getCount() {
            return mChosedMeals.size();
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
            RecoverBean.Data meal = mChosedList.get(i);
            view = LayoutInflater.from(EnsureOrderActivity.this).inflate(R.layout.order_meal_item,viewGroup,false);
            TextView titleTv = view.findViewById(R.id.meal_title_tv);
            TextView contentTv = view.findViewById(R.id.meal_content_tv);
            TextView numsTv = view.findViewById(R.id.nums_tv);
            TextView moneny = view.findViewById(R.id.money_tv);
            titleTv.setText(meal.getPackageName());
            String contents = "";

            //设置食疗内容
            for (int i1 = 0; i1<meal.getFoodList().size();i1++) {
                // myLog("------------>"+meal.getFoodList().get(i1).getDishName());
                if (i1<meal.getFoodList().size()-1){
                    contents = contents + meal.getFoodList().get(i1).getDishName()+"\r\n";
                }else {
                    contents = contents + meal.getFoodList().get(i1).getDishName();
                }
            }
            contentTv.setText(contents);
            numsTv.setText("✘"+meal.getNums());
            moneny.setText("￥"+(meal.getPrice()*meal.getNums())+".00");
            return view;
        }
    }
    class TablewareAdapter extends BaseAdapter{
        List<Boolean> selectMark = new ArrayList<>();

        public TablewareAdapter() {
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
            TablewareBean.Data data = tablewareBean.getData().get(i);
            myLog("----------------->"+data.getName());
            view = LayoutInflater.from(EnsureOrderActivity.this).inflate(R.layout.order_tablemare_item,viewGroup,false);
            TextView tablewareTitle = view.findViewById(R.id.tableware_title_tv);
            TextView tablewareContent = view.findViewById(R.id.tableware_content_tv);
            TextView numsTv = view.findViewById(R.id.nums_tv);
            TextView moneyTv = view.findViewById(R.id.money_tv);
            ImageView selectImg = view.findViewById(R.id.select_img);
            tablewareTitle.setText(data.getName());
            tablewareContent.setText("(押金"+data.getDeposit()+"元/份)");
            int num = 0;
            for (String key :
                    mChosedMeals.keySet()) {
                num = num + mChosedMeals.get(key).getNums();
            }
            numsTv.setText("✘"+num);
            moneyTv.setText("￥"+num*data.getUsePrice()+".00");
            if (selectMark.get(i)){
                tablewareMoeny = num*data.getUsePrice();
                mAllMoneyTv.setText("￥"+(mealMoney+tablewareMoeny));
                mTablewareMoneyTv.setText("其中餐具押金"+tablewareMoeny+"元");
                selectImg.setImageDrawable(getResources().getDrawable(R.drawable.selected_drawable));
            }else {
                selectImg.setImageDrawable(getResources().getDrawable(R.drawable.unselected_drawable));
            }

            selectImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (int j = 0; j<selectMark.size();j++){
                        if (i!=j){
                            selectMark.set(j,false);
                        }else {
                            selectMark.set(j,true);
                        }
                    }
                    TablewareAdapter.this.notifyDataSetChanged();
                }
            });
            return view;
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
            view = LayoutInflater.from(EnsureOrderActivity.this).inflate(R.layout.order_paymentway_item,viewGroup,false);
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
