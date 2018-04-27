package com.anxin.kitchen.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuAdapter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anxin.kitchen.MyApplication;
import com.anxin.kitchen.activity.order.OrderDetailActivity;
import com.anxin.kitchen.bean.AddressBean;
import com.anxin.kitchen.bean.OrderInfoBean;
import com.anxin.kitchen.bean.PreMoneyBean;
import com.anxin.kitchen.bean.RecorveOrderBean;
import com.anxin.kitchen.bean.RecoverBean;
import com.anxin.kitchen.bean.TablewareBean;
import com.anxin.kitchen.interface_.RequestNetListener;
import com.anxin.kitchen.user.R;
import com.anxin.kitchen.utils.BaseDialog;
import com.anxin.kitchen.utils.Cache;
import com.anxin.kitchen.utils.Constant;
import com.anxin.kitchen.utils.PrefrenceUtil;
import com.anxin.kitchen.utils.StringUtils;
import com.anxin.kitchen.utils.SystemUtility;
import com.anxin.kitchen.view.MyListView;
import com.anxin.kitchen.view.WaitingDialog;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class EnsureOrderActivity extends BaseActivity implements View.OnClickListener, RequestNetListener {
    private static final String CREATE_DIET = "CREATE_DIET";
    private static final String GET_PRO_MOENY = "GET_PRO_MOENY";
    private static final String PAY_MONEY = "PAY_MONEY";
    private static final String ENSURE_MONEY = "ENSURE_MONEY";
    private static final int GET_LOCATION = 202;
    private ImageView mBackImg;
    private TextView mTitleTv;
    private ImageView mTransmitImg;
    private MyListView mMealLv;
    private MyListView mTablemareLv;
    //    private MyListView mPayWayLv;
    private TextView mAllMoneyTv;
    private TextView mTablewareMoneyTv;
    private TextView mEnsurePayTv;
    TablewareBean tablewareBean;
    private double mealMoney = 0;
    private double tablewareMoeny = 0;
    private static final String GET_TABLEWARE = "GET_TABLEWARE";
    private Cache mCache;
    private PrefrenceUtil mPrefrenceUtil;
    private LinkedHashMap<String, RecoverBean.Data> mChosedMeals = new LinkedHashMap<>();
    private long tablewareId;
    private int payType;
    private TablewareAdapter tablewareAdapter;
    private int kitchenId;
    private WaitingDialog mdialog;
    private RelativeLayout mLocationRl;
    private TextView mLocationTv;
    private AddressBean addressBean = null;
    private MyListView mPayWayLv;
    private TextView sendTimeTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ensure_order);
        initView();
        getTableWare();
        initDate();
    }

    private void initDate() {
        addressBean = MyApplication.getInstance().getCache().getDefaultAddress(this);
        if (null == addressBean) {
            List<AddressBean> addressBeanList = MyApplication.getInstance().getAddressBeanList();
            for (int i = 0; i < addressBeanList.size(); i++) {
                AddressBean addressBean = addressBeanList.get(i);
                String isDefault = addressBean.getIsDefault();
                if (isDefault != null && isDefault.equals("1")) {
                    this.addressBean = addressBean;
                    mLocationTv.setText(addressBean.getStreetName());
                }
            }
        } else {
            mLocationTv.setText(addressBean.getStreetName());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    /**
     * 获取后台计算的关于这个单的钱
     */
    private void getPreMoney() {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("kitchenId", mPrefrenceUtil.getKitchenId());
        dataMap.put("tablewareId", tablewareId);

        //获取所有套餐信息
        String tempPackages = "";
        for (String key : mChosedMeals.keySet()) {
            double littlePrice = mChosedMeals.get(key).getPrice() * mChosedMeals.get(key).getNums();
            BigDecimal b = new BigDecimal(littlePrice);
            double price = b.setScale(2, BigDecimal.ROUND_UP).doubleValue();
            tempPackages = tempPackages + mChosedMeals.get(key).getPackageId() + "*"
                    + mChosedMeals.get(key).getNums() + "*" + price + "0,";
        }
        String packages = tempPackages.substring(0, tempPackages.lastIndexOf(","));
        dataMap.put("packages", packages);
        dataMap.put("payType", payType);
        dataMap.put("token", mCache.getAMToken());
        requestNet(SystemUtility.getDiet_DeMoneyUrl(), dataMap, GET_PRO_MOENY);
    }

    private void initView() {
        if (null == mCache) {
            mCache = new Cache(this);
        }
        mLocationTv = findViewById(R.id.location_tv);
        mLocationRl = findViewById(R.id.user_address_rlt);
        mToken = mCache.getAMToken();
        mPayWayLv = findViewById(R.id.payment_lv);
        mdialog = new WaitingDialog(this, 100);
        mBackImg = findViewById(R.id.back_img);
        mTitleTv = findViewById(R.id.title_tv);
        mMealLv = findViewById(R.id.meal_lv);
//        mPayWayLv = findViewById(R.id.payment_lv);
        mTablemareLv = findViewById(R.id.tableware_lv);
        mTransmitImg = findViewById(R.id.transmit_img);
        mAllMoneyTv = findViewById(R.id.all_money_tv);
        mTablewareMoneyTv = findViewById(R.id.tableware_money_tv);
        mEnsurePayTv = findViewById(R.id.ensure_pay_tv);
        String chosedMealSt = getIntent().getStringExtra("chosedMeal");
        mChosedMeals = mGson.fromJson(chosedMealSt, new TypeToken<LinkedHashMap<String, RecoverBean.Data>>() {
        }.getType());
        mMealLv.setAdapter(new MealAdapter());
         mPayWayLv.setAdapter(new PaymentAdapter());
        mTitleTv.setText("确认订单");
        kitchenId = new PrefrenceUtil(this).getKitchenId();
        mCache = new Cache(this);
        mPrefrenceUtil = new PrefrenceUtil(this);
        mTransmitImg.setVisibility(View.VISIBLE);
        mBackImg.setOnClickListener(this);
        mEnsurePayTv.setOnClickListener(this);
        tablewareAdapter = new TablewareAdapter();
        mLocationRl.setOnClickListener(this);


        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        int month = (now.get(Calendar.MONTH) + 1);
        int day = now.get(Calendar.DAY_OF_MONTH);
        now.set(year, month, 0);
        int dayOfMonth = now.get(Calendar.DAY_OF_MONTH);  //这个月的总天数
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int fen = now.get(Calendar.MINUTE);
        String sendtime = "送餐时间： "+(hour +1) + ":"+fen;
        sendTimeTv = findViewById(R.id.send_time_tv);
        sendTimeTv.setText(sendtime);
    }


    String ids = "";

    @Override
    public void requestSuccess(String responseString, String requestCode) {
        super.requestSuccess(responseString, requestCode);
        String status = StringUtils.parserMessage(responseString, Constant.REQUEST_STATUS);
        if (requestCode == GET_TABLEWARE && status.equals(Constant.REQUEST_SUCCESS)) {
            String tempSt = responseString.substring(responseString.indexOf("\"data\":"), responseString.lastIndexOf("}"));
            String tablewareSt = tempSt.substring(tempSt.indexOf(":", 0) + 1);
            myLog("---------------->" + tablewareSt);
            tablewareBean = mGson.fromJson(tablewareSt, TablewareBean.class);
            mTablemareLv.setAdapter(tablewareAdapter);
        }

        if (requestCode == CREATE_DIET && status.equals(Constant.REQUEST_SUCCESS)) {
            Toast.makeText(this, "创建订单成功", Toast.LENGTH_SHORT).show();
            RecorveOrderBean bean = mGson.fromJson(responseString, RecorveOrderBean.class);
            Map<String, Object> dataMap = new HashMap<>();

            ids = String.valueOf(bean.getData().getId());

            myLog("-------->" + ids);
            dataMap.put("orderIds", ids);
            dataMap.put("token", mToken);
            requestNet(SystemUtility.getSureDeMoneyUrl(), dataMap, ENSURE_MONEY);

        } else if (requestCode == CREATE_DIET && (!status.equals(Constant.REQUEST_SUCCESS))) {
            Toast.makeText(this, "创建订单失败：" + status, Toast.LENGTH_SHORT).show();
            mdialog.stopAnimation();
            mdialog.dismiss();
        }


        if (requestCode == ENSURE_MONEY && (status.equals(Constant.REQUEST_SUCCESS))) {
            PreMoneyBean bean = mGson.fromJson(responseString, PreMoneyBean.class);
            Map<String, Object> dataMap = new HashMap<>();
            myLog("--------s>" + ids);
            dataMap.put("orderIds", ids);
            dataMap.put("token", mToken);
            requestNet(SystemUtility.payUrl(), dataMap, PAY_MONEY);

        } else if (requestCode == ENSURE_MONEY && (!status.equals(Constant.REQUEST_SUCCESS))) {
            Toast.makeText(this, "确认订单失败", Toast.LENGTH_SHORT).show();
            mdialog.stopAnimation();
            mdialog.dismiss();
        }

        if (requestCode == GET_PRO_MOENY && status.equals(Constant.REQUEST_SUCCESS)) {
            PreMoneyBean bean = mGson.fromJson(responseString, PreMoneyBean.class);
            mAllMoneyTv.setText("￥" + bean.getData().getTotalPay());
            mTablewareMoneyTv.setText("其中餐具押金" + bean.getData().getPayDeposit() + "元");
        }

        if (requestCode == PAY_MONEY && (status.equals(Constant.REQUEST_SUCCESS))) {
//            Toast.makeText(this, "付款成功", Toast.LENGTH_SHORT).show();
            BaseDialog dialog = BaseDialog.showDialog(this, R.layout.orderplay_dialog, Gravity.CENTER, 0);
            mdialog.stopAnimation();
            mdialog.dismiss();
            clearCache();
            Intent intentPayOrder = new Intent(this, OrderDetailActivity.class);
            intentPayOrder.putExtra("orderId", Long.valueOf(ids));
            intentPayOrder.putExtra("closeType", 1);
            startActivity(intentPayOrder);
            //要修改  跳转到订单活动
//            startNewActivity(MainActivity.class);
        } else if (requestCode == PAY_MONEY && (!status.equals(Constant.REQUEST_SUCCESS))) {
            Toast.makeText(this, "付款失败" + status, Toast.LENGTH_SHORT).show();
            mdialog.stopAnimation();
            mdialog.dismiss();
        }


    }

    private void payMoney(RecorveOrderBean bean) {
        Map<String, Object> dataMap = new HashMap<>();

        ids = String.valueOf(bean.getData().getId());

        myLog("-------->" + ids);
        dataMap.put("orderIds", ids);
        dataMap.put("token", mToken);
        requestNet(SystemUtility.payUrl(), dataMap, PAY_MONEY);
    }

    private void clearCache() {
        PrefrenceUtil prefrenceUtil = new PrefrenceUtil(this);
        prefrenceUtil.setRecoverList("");
        prefrenceUtil.setRecoverMenuList("");
//        startNewActivity(MainActivity.class);
    }

    //获取所有餐具
    private void getTableWare() {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("page", 1);
        requestNet(SystemUtility.getTablewareListUrl(), dataMap, GET_TABLEWARE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_img:
                this.finish();
                break;
            case R.id.transmit_img:
                break;
            case R.id.ensure_pay_tv:
                createOrder();
                break;
            case R.id.user_address_rlt:
                Intent intent = new Intent(this, SendMealLocationActivity.class);
                startActivityForResult(intent, GET_LOCATION);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
//            LOG.e("onActivityResult: resultCode!=RESULT_OK");
            return;
        }
        if (requestCode == GET_LOCATION) {
            addressBean = (AddressBean) data.getSerializableExtra("addressBean");
            mLocationTv.setText(addressBean.getStreetName());
        }
    }

    private void createOrder() {
        //先去再次请求订餐金钱
        if (addressBean == null){
            Toast.makeText(this, "请选择送餐地址", Toast.LENGTH_SHORT).show();
            return;
        }
        mdialog.show();
        mdialog.startAnimation();
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("kitchenId", mPrefrenceUtil.getKitchenId());
        dataMap.put("tablewareId", tablewareId);

        //获取所有套餐信息
        String tempPackages = "";
        for (String key : mChosedMeals.keySet()) {
            double littlePrice = mChosedMeals.get(key).getPrice() * mChosedMeals.get(key).getNums();
            BigDecimal b = new BigDecimal(littlePrice);
            double price = b.setScale(2, BigDecimal.ROUND_UP).doubleValue();
            tempPackages = tempPackages + mChosedMeals.get(key).getPackageId() + "*"
                    + mChosedMeals.get(key).getNums() + "*" + price + "0,";
        }
        String packages = tempPackages.substring(0, tempPackages.lastIndexOf(","));
        dataMap.put("packages", packages);
        dataMap.put("payType", payType);
        dataMap.put("contactPhone", addressBean.getPhoneNumber());
        dataMap.put("contactName", addressBean.getContactName());
        dataMap.put("address", addressBean.getStreetName() + addressBean.getAddress());
        dataMap.put("token", mCache.getAMToken());
        requestNet(SystemUtility.createRecoverDiet(), dataMap, CREATE_DIET);
    }


    class MealAdapter extends BaseAdapter {
        private LinkedList<RecoverBean.Data> mChosedList = new LinkedList<>();

        public MealAdapter() {
            for (String mealName : mChosedMeals.keySet()
                    ) {
                RecoverBean.Data meal = mChosedMeals.get(mealName);
                mealMoney = mealMoney + meal.getPrice() * meal.getNums();
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
            view = LayoutInflater.from(EnsureOrderActivity.this).inflate(R.layout.order_meal_item, viewGroup, false);
            TextView titleTv = view.findViewById(R.id.meal_title_tv);
            TextView contentTv = view.findViewById(R.id.meal_content_tv);
            TextView numsTv = view.findViewById(R.id.nums_tv);
            TextView moneny = view.findViewById(R.id.money_tv);
            titleTv.setText(meal.getPackageName());
            String contents = "";

            //设置食疗内容
            for (int i1 = 0; i1 < meal.getFoodList().size(); i1++) {
                // myLog("------------>"+meal.getFoodList().get(i1).getDishName());
                if (i1 < meal.getFoodList().size() - 1) {
                    contents = contents + meal.getFoodList().get(i1).getDishName() + "\r\n";
                } else {
                    contents = contents + meal.getFoodList().get(i1).getDishName();
                }
            }
            contentTv.setText(contents);
            numsTv.setText("✘" + meal.getNums());
            moneny.setText("￥" + (meal.getPrice() * meal.getNums()));
            return view;
        }
    }

    class TablewareAdapter extends BaseAdapter {
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
            myLog("----------------->" + data.getName());
            view = LayoutInflater.from(EnsureOrderActivity.this).inflate(R.layout.order_tablemare_item, viewGroup, false);
            TextView tablewareTitle = view.findViewById(R.id.tableware_title_tv);
            TextView tablewareContent = view.findViewById(R.id.tableware_content_tv);
            TextView numsTv = view.findViewById(R.id.nums_tv);
            TextView moneyTv = view.findViewById(R.id.money_tv);
            ImageView selectImg = view.findViewById(R.id.select_img);
            tablewareTitle.setText(data.getName());
            tablewareContent.setText("(押金" + data.getDeposit() + "元/份)");
            int num = 0;
            for (String key :
                    mChosedMeals.keySet()) {
                num = num + mChosedMeals.get(key).getNums();
            }
            numsTv.setText("✘" + num);
            moneyTv.setText("￥" + num * data.getUsePrice() + ".00");
            if (selectMark.get(i)) {
                tablewareId = data.getId();
                getPreMoney();
                myLog("------------tableid" + tablewareId);
                tablewareMoeny = num * data.getUsePrice();
                selectImg.setImageDrawable(getResources().getDrawable(R.drawable.selected_drawable));
            } else {
                selectImg.setImageDrawable(getResources().getDrawable(R.drawable.unselected_drawable));
            }

            selectImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (int j = 0; j < selectMark.size(); j++) {
                        if (i != j) {
                            selectMark.set(j, false);
                        } else {
                            selectMark.set(j, true);
                        }
                    }
                    notifyDataSetChanged();
                }
            });
            return view;
        }
    }

    class PaymentAdapter extends BaseAdapter {
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
            view = LayoutInflater.from(EnsureOrderActivity.this).inflate(R.layout.order_paymentway_item, viewGroup, false);
            ImageView payImg = view.findViewById(R.id.pay_img);
            ImageView paySelectImg = view.findViewById(R.id.pay_select_img);
            TextView payTitleTv = view.findViewById(R.id.pay_title_tv);
            if (i == 0) {
                payImg.setImageDrawable(getResources().getDrawable(R.drawable.weixin_pay));
                payTitleTv.setText("微信支付");
            } else {
                payImg.setImageDrawable(getResources().getDrawable(R.drawable.zhifubao_pay));
                payTitleTv.setText("支付宝支付");
            }
            if (selectMark.get(i)) {
                payType = i + 1;
                paySelectImg.setImageDrawable(getResources().getDrawable(R.drawable.selected_drawable));
            } else {
                paySelectImg.setImageDrawable(getResources().getDrawable(R.drawable.unselected_drawable));
            }

            paySelectImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (int j = 0; j < selectMark.size(); j++) {
                        if (i != j) {
                            selectMark.set(j, false);
                        } else {
                            selectMark.set(j, true);
                        }
                    }
                    PaymentAdapter.this.notifyDataSetChanged();
                }
            });
            return view;
        }
    }
}
