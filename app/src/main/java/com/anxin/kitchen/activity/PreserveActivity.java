package com.anxin.kitchen.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anxin.kitchen.MyApplication;
import com.anxin.kitchen.bean.FoodsBean;
import com.anxin.kitchen.bean.MealBean;
import com.anxin.kitchen.bean.MealBean.FoodList;
import com.anxin.kitchen.bean.SearchGroupBean;
import com.anxin.kitchen.bean.SendBean;
import com.anxin.kitchen.bean.TablewareBean;
import com.anxin.kitchen.interface_.RequestNetListener;
import com.anxin.kitchen.user.R;
import com.anxin.kitchen.utils.Constant;
import com.anxin.kitchen.utils.DateUtils;
import com.anxin.kitchen.utils.PrefrenceUtil;
import com.anxin.kitchen.utils.StringUtils;
import com.anxin.kitchen.utils.SystemUtility;
import com.anxin.kitchen.view.ChoseGroupDialog;
import com.anxin.kitchen.view.DeleteMealDialog;
import com.anxin.kitchen.view.OrderingRuleDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.umeng.analytics.MobclickAgent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PreserveActivity extends BaseActivity implements View.OnClickListener, RequestNetListener {
    private static final String GET_TABLEWARE = "GET_TABLEWARE";
    private static final String GET_SEND_COST = "GET_SEND_COST";
    public static final String SEARCH_GROUP = "SEARCH_GROUP";
    private static final int CHOSE_MEAL = 100;
    private static final int SET_NUMS = 102;   //设置套餐数量
    private static final int CREAT_GROUP = 200;
    private ListView mAddPreserveLv;
    private List mDayList = new ArrayList();
    private TextView viewKitchen;
    private MealBean mealBean;
    private List<MealBean.Data> mealList;
    private PreserverAdapter preserverAdapter;
    String mealListSt;
    List<Long> days; //未来一周的长整型集合
    ChoseGroupDialog dialog = null;
    private TextView mCloseAcountTv;  //去结算
    private boolean hasMeal = false; //是否有选择餐
    private boolean isChosedGroup = false;   //是否全部选择的是团选择团
    private boolean isChosedBySetNums = false; //是否选择输入数量
    private PopupWindow popupWindow;
    private TablewareBean tablewareBean;
    private int allNums = 0;  //总数量
    private long tablewareId; //选择餐具id
    private double tablewareDepositeMoney; //使用押金
    private double tablewareUseMoney;  //使用费用
    private String tablewareName;    //餐具名
    private double sendCost;   // 配送费
    private String tablewareType = "";
    private String mGroupList;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preserve);

        getTableWare();
        initView();
        getNextDayOfWeek();
        initData();
        getSendCost();
        setGroup();
    }

    private void setGroup() {
        /*mGroupList = new PrefrenceUtil(this).getGroups();
        if (null != mGroupList && !mGroupList.equals("null")){

        }else {
            //从新请求团*/
        String url = SystemUtility.searchGroupUrl();
        Map<String, Object> dataMap = new HashMap<>();
        isLogin();
        dataMap.put(Constant.TOKEN, mToken);
        requestNet(url, dataMap, SEARCH_GROUP);
        //}
    }


    //告知订餐规则
    private void tellRule() {
        OrderingRuleDialog dialog = new OrderingRuleDialog(this);
        dialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initData() {
        Intent intent = getIntent();
        if (null != intent) {
            mealListSt = intent.getStringExtra("mealListSt");
            mealBean = mGson.fromJson(mealListSt, MealBean.class);
            myLog("------->" + mealListSt);
        }
        //String data = StringUtils.parserMessage(mealListSt,"data");

        if (!((null == mealListSt) || mealListSt.equals(Constant.NULL))) {
            mealList = mealBean.getData();
        }

        List<List<MealBean.Data>> dataList = new ArrayList<>();
        LinkedHashMap<Long, WeekDayBean> weakDays = new LinkedHashMap<>();


        LinkedHashMap<Long, Map<String, MealBean.Data>> preMealMaps = new LinkedHashMap<>();

        String premealCache = new PrefrenceUtil(this).getPreserveList();
        myLog("-------------缓存内容>" + premealCache);
        if (null != premealCache && (!"null".equals(premealCache)) && premealCache.length()>0) {
            myLog("----------------走了缓存");
            preMealMaps = mGson.fromJson(premealCache, new TypeToken<LinkedHashMap<Long, Map<String, MealBean.Data>>>() {
            }.getType());
        } else {
            tellRule();
        }



        //一周的时间跟周之间的对应
        for (int i = 0; i < days.size(); i++) {
            Long day = days.get(i);
            String weekDay = transToWeekDay(day);
            WeekDayBean weekDayBean = new WeekDayBean(weekDay, DateUtils.getDate(String.valueOf(day)));
            if (!weakDays.containsKey(day)) {
                days.add(day);
                weakDays.put(day, weekDayBean);
            }

            if (preMealMaps != null && !preMealMaps.containsKey(day)) {
                preMealMaps.put(day, new HashMap<String, MealBean.Data>());
            }
        }


        for (Long key:preMealMaps.keySet()){
            myLog("-------->"+key);
        }

        String clickmeal = getIntent().getStringExtra("mealSt");
        if (null != clickmeal && clickmeal.length()>10){
            myLog("---------->meal" + clickmeal);
            MealBean.Data meal = mGson.fromJson(clickmeal,MealBean.Data.class);
            meal.setSelectByMaster(true);
            if (meal.getEatType()==1){
                if(preMealMaps.get(meal.getMenuDay()).containsKey("午餐")){
                    preMealMaps.get(meal.getMenuDay()).replace("午餐",meal);
                }else {
                    preMealMaps.get(meal.getMenuDay()).put("午餐",meal);
                }
            }else {
                if(preMealMaps.get(meal.getMenuDay()).containsKey("晚餐")){
                    preMealMaps.get(meal.getMenuDay()).replace("晚餐",meal);
                }else {
                    preMealMaps.get(meal.getMenuDay()).put("晚餐",meal);
                }
            }
        }
        long lastDay = 0;
        /*if (!((null == mealListSt) || mealListSt.equals(Constant.NULL))) {
            for (MealBean.Data mel :
                    mealList) {
                long thisDay = mel.getMenuDay();
                int type = mel.getEatType();
                if (type == 1) {
                    myLog("-----------------午餐");
                    if (null != preMealMaps.get(thisDay)) {
                        if (!preMealMaps.get(thisDay).containsKey("午餐")) {
                            preMealMaps.get(thisDay).put("午餐", mel);
                        }
                    }
                } else if (type == 2) {
                    myLog("-----------------晚餐");
                    if (null != preMealMaps.get(thisDay)) {
                        if (!preMealMaps.get(thisDay).containsKey("晚餐")) {
                            preMealMaps.get(thisDay).put("晚餐", mel);
                        }
                    }
                }

            }
        } else {

        }*/
        myLog("------------------>pre_be" + preMealMaps.size());
        updateUI(days, weakDays, preMealMaps);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        String mPreseverSt = mGson.toJson(preserverAdapter.preMealMaps);
        myLog("---------------->pre" + mPreseverSt);
        new PrefrenceUtil(this).setPreserveList(mPreseverSt);
    }

    //获取配送费
    private void getSendCost() {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("kitchenId", new PrefrenceUtil(this).getKitchenId());
        requestNet(SystemUtility.get_kitchen_settingUrl(), dataMap, GET_SEND_COST);
    }

    @Override
    public void requestSuccess(String responseString, String requestCode) {
        super.requestSuccess(responseString, requestCode);
        String status = StringUtils.parserMessage(responseString, Constant.REQUEST_STATUS);
        if (requestCode == GET_TABLEWARE && status.equals(Constant.REQUEST_SUCCESS)) {
            String tempSt = responseString.substring(responseString.indexOf("\"data\":"), responseString.lastIndexOf("}"));
            String tablewareSt = tempSt.substring(tempSt.indexOf(":", 0) + 1);
            myLog("---------->" + tablewareSt);
            tablewareBean = mGson.fromJson(tablewareSt, TablewareBean.class);
            return;
        }

        if (requestCode == GET_SEND_COST && status.equals(Constant.REQUEST_SUCCESS)) {
            SendBean bean = mGson.fromJson(responseString, SendBean.class);
            sendCost = bean.getData().getDeliveryGroupPrice();
            return;
        }


        //查询所有创建的团
        if (requestCode != null && requestCode.equals(SEARCH_GROUP)) {
            if (null != status && status.equals(Constant.REQUEST_SUCCESS)) {
                String gsonSt = StringUtils.parserMessage(responseString, "data");
                myLog("------------>" + gsonSt);
                Gson gson = new Gson();
                SearchGroupBean bean = gson.fromJson(gsonSt, SearchGroupBean.class);
                if (!gsonSt.equals(Constant.NULL)) {
                    myLog(bean.getData().size() + "--------" + gsonSt);
                    new PrefrenceUtil(this).putGroups(gsonSt);

                }
            } else if (null != status && status.equals(Constant.LOGIN_FIRST)) {
                SystemUtility.startLoginUser(this);
            }
            return;
        }
    }

    //获取所有餐具
    private void getTableWare() {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("page", -1);
        requestNet(SystemUtility.getTablewareListUrl(), dataMap, GET_TABLEWARE);
    }

    //获取明天是星期几
    private String getNextDayOfWeek() {
        days = new ArrayList<>();
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        int month = (now.get(Calendar.MONTH) + 1);
        int day = now.get(Calendar.DAY_OF_MONTH);
        now.set(year, month, 0);
        int dayOfMonth = now.get(Calendar.DAY_OF_MONTH);  //这个月的总天数
        int hour = now.get(Calendar.HOUR_OF_DAY);
        if (hour > 17) {
            day = day + 1;
        }
        myLog(dayOfMonth + "--------------->day" + day + "   " + hour);
        int sheYuday = 0;
        int diffDay = 0;
        if ((day) == dayOfMonth || (day - 1) == dayOfMonth) {
            diffDay = 7;
        } else {
            sheYuday = dayOfMonth - day;
            diffDay = 7 - sheYuday;
        }

        myLog("------------------>剩余天数" + diffDay);
        //这个月还差几天  去下个月中借齐
        //只判断到了月 后期要添加年的逻辑  否则有问题
        if (diffDay <= 0) {
            for (int i = 1; i <= 7; i++) {
                String dateSt = String.valueOf(year);
                if (month < 10) {
                    dateSt = dateSt + "0" + month;
                } else {
                    dateSt = dateSt + month;
                }

                if ((day + 1) < 10) {
                    dateSt = dateSt + "0" + (day + i);
                } else {
                    dateSt = dateSt + (day + i);
                }
                days.add(Long.valueOf(dateSt));
            }
        } else {
            //需要借的逻辑
            //首先当前月还剩下几天
            for (int i = 1; i <= sheYuday; i++) {
                String dateSt = String.valueOf(year);
                if (month < 10) {
                    dateSt = dateSt + "0" + month;
                } else {
                    dateSt = dateSt + month;
                }

                if ((day + 1) < 10) {
                    dateSt = dateSt + "0" + (day + i);
                } else {
                    dateSt = dateSt + (day + i);
                }
                days.add(Long.valueOf(dateSt));
            }
            //补足7天
            for (int i = 1; i <= diffDay; i++) {
                String dateSt = String.valueOf(year);
                if ((month) < 10) {
                    dateSt = dateSt + "0" + (month + 1);
                } else {
                    dateSt = dateSt + month;
                }

                if ((i) < 10) {
                    dateSt = dateSt + "0" + (i);
                } else {
                    dateSt = dateSt + (i);
                }
                days.add(Long.valueOf(dateSt));
            }
        }
        for (Long dayl :
                days) {
            myLog("-------------->" + year + "  " + month + "  " + day + "   " + dayOfMonth + "  " + dayl);
        }
        myLog("-------------->" + year + "  " + month + "  " + day + "   " + dayOfMonth);
        return null;
    }

    private void updateUI(List<Long> days, LinkedHashMap<Long, WeekDayBean> weakDays, LinkedHashMap<Long, Map<String, MealBean.Data>> preMealMaps) {
        setAdapter(days, weakDays, preMealMaps);
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

    private String transToWeekDay(long thisDay) {
        myLog("--------->" + thisDay);
        String dataSt = String.valueOf(thisDay);
        StringBuffer dateBf = new StringBuffer();
        dateBf.append(dataSt.substring(0, 4));
        dateBf.append("-");
        dateBf.append(dataSt.substring(4, 6));
        dateBf.append("-");
        dateBf.append(dataSt.substring(6));
        dataSt = dateBf.toString();
        Date d = null;
        try {
            d = new SimpleDateFormat("yyyy-MM-dd").parse(dataSt);
        } catch (ParseException e) {
            MobclickAgent.reportError(MyApplication.getInstance(), e);
        }
        int day = d.getDay();
        myLog("---------------d" + day);
        switch (day) {
            case 1:
                return "周一";
            case 2:
                return "周二";
            case 3:
                return "周三";
            case 4:
                return "周四";
            case 5:
                return "周五";
            case 6:
                return "周六";
            case 0:
                return "周日";

        }
        return null;
    }

    private void initView() {
        setTitle("预约点餐");
        findViewById(R.id.view_kitchen).setVisibility(View.VISIBLE);
        mAddPreserveLv = findViewById(R.id.add_preserve_lv);
        ImageView back_img = (ImageView) findViewById(R.id.back_img);
        mCloseAcountTv = findViewById(R.id.close_account_tv);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        viewKitchen = findViewById(R.id.view_kitchen);
        viewKitchen.setOnClickListener(this);
        mCloseAcountTv.setOnClickListener(this);
    }


    private void setAdapter(List<Long> days, LinkedHashMap<Long, WeekDayBean> weakDays, LinkedHashMap<Long, Map<String, MealBean.Data>> preMealMaps) {
        preserverAdapter = new PreserverAdapter(days, weakDays, preMealMaps);
        mAddPreserveLv.setAdapter(preserverAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.view_kitchen:
                startNewActivity(ViewKitchenActivity.class);
                break;
            case R.id.close_account_tv:
                String status = annylyDateValue();
                if (null == status) {
                    //closeOrder();
                    tablewareWindow(view);
                } else {
                    Toast.makeText(this, status, Toast.LENGTH_SHORT).show();
                }
            default:
                break;
        }
    }

    private void tablewareWindow(View view) {
        if (popupWindow != null && popupWindow.isShowing()) {
            return;
        }
        RelativeLayout layout = (RelativeLayout) getLayoutInflater().inflate(R.layout.tableware_popup, null);
        ListView mTablewareLv = layout.findViewById(R.id.chose_tableware_lv);
        getAllnums();
        TextView aaPay = layout.findViewById(R.id.aa_pay_tv);
        if (isChosedGroup) {
            aaPay.setVisibility(View.VISIBLE);
        } else aaPay.setVisibility(View.GONE);
        popupWindow = new PopupWindow(layout,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
//        int popWidth = view.getMeasuredWidth();
//        int popHeight = view.getMeasuredHeight();
//        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
//        Display display = wm.getDefaultDisplay();
//        popupWindow.setHeight(display.getHeight() * 7 / 10);
        //点击空白处时，隐藏掉pop窗口
        popupWindow.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(getResources().getColor(R.color.dialog_background));
        popupWindow.setBackgroundDrawable(dw);
        //添加弹出、弹入的动画
        popupWindow.setAnimationStyle(R.style.Popupwindow);
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        //添加按键事件监听
        setButtonListeners(layout);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                ColorDrawable dw = new ColorDrawable(getResources().getColor(R.color.white));
            }
        });
        //添加pop窗口关闭事件，主要是实现关闭时改变背景的透明度
//        popupWindow.setOnDismissListener(new poponDismissListener());
        mTablewareLv.setAdapter(new TablewareAdapter(tablewareBean));
//        backgroundAlpha(0.5f);
        RelativeLayout back_layout = layout.findViewById(R.id.back_layout);
        back_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        TextView unifyPayTv = layout.findViewById(R.id.unify_pay_tv);
        unifyPayTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeOrder();
                popupWindow.dismiss();
            }
        });

        aaPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeOrder();
                popupWindow.dismiss();
            }
        });
    }


    private void getAllnums() {
        allNums = 0;
        for (Long day :
                preserverAdapter.preMealMaps.keySet()) {
            myLog("-----------day---->" + day);

            Map<String, MealBean.Data> dayData = preserverAdapter.preMealMaps.get(day);
            if (dayData.containsKey("午餐")) {
                allNums = allNums + dayData.get("午餐").getNums();
                myLog("-----------day---->" + allNums + dayData.containsKey("晚餐"));

            }
            if (dayData.containsKey("晚餐")) {
                allNums = allNums + dayData.get("晚餐").getNums();
                myLog("-----------day---->" + allNums);
            }
        }
    }

    public void toCreatGroupAct() {
        Intent intent = new Intent(this, CreateGroupActivity.class);
        intent.putExtra("preserve", true);
        startActivityForResult(intent, CREAT_GROUP);
    }


    class TablewareAdapter extends BaseAdapter {
        private TablewareBean tablewareBean = null;
        List<Boolean> selectMark = new ArrayList<>();

        public TablewareAdapter(TablewareBean tablewareBean) {
            this.tablewareBean = tablewareBean;
            for (int i = 0; i < tablewareBean.getData().size(); i++) {
                if (i == 0) {
                    selectMark.add(i, true);
                } else {
                    selectMark.add(i, false);
                }
            }
        }

        @Override
        public int getCount() {
            return tablewareBean.getData().size();
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
            TablewareBean.Data tableware = tablewareBean.getData().get(i);
            view = LayoutInflater.from(PreserveActivity.this).inflate(R.layout.chose_tablemare_item, viewGroup, false);
            TextView tablewareNameTv = view.findViewById(R.id.tableware_title_tv);
            TextView tablewareGmTv = view.findViewById(R.id.tablew_g_m_tv);
            TextView tablewareNumsTv = view.findViewById(R.id.tableware_nums_tv);
            TextView tableMoney = view.findViewById(R.id.tableware_money_tv);
            tablewareNameTv.setText(tableware.getName());
            tablewareGmTv.setText("(押金" + tableware.getDeposit() + "元/份)");
            tableMoney.setText("￥" + tableware.getUsePrice());
            ImageView selectImg = view.findViewById(R.id.tableware_select_img);
            tablewareNumsTv.setText("✘" + allNums);
            if (selectMark.get(i)) {
                tablewareName = tableware.getName();
                tablewareId = tableware.getId();
                tablewareUseMoney = tableware.getUsePrice();
                tablewareDepositeMoney = tableware.getDeposit();
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
                    TablewareAdapter.this.notifyDataSetChanged();
                }
            });
            return view;
        }
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    private void setButtonListeners(RelativeLayout layout) {

    }

    /**
     * 跳转去结算界面
     */
    private void closeOrder() {
        Intent intent = new Intent(PreserveActivity.this, UnifyPayActivity.class);
        String data = mGson.toJson(preserverAdapter.preMealMaps);
        intent.putExtra("meals", data);
        intent.putExtra("groupTag", isChosedGroup);
        intent.putExtra("inputNumsTag", isChosedBySetNums);
        intent.putExtra("nums", allNums);
        intent.putExtra("tablewareName", tablewareName);
        intent.putExtra("tableawareId", tablewareId);
        intent.putExtra("tablewareUseMoney", tablewareUseMoney);
        intent.putExtra("tablewareDepositeMoney", tablewareDepositeMoney);
        intent.putExtra("sendCost", sendCost);
        startActivity(intent);
    }


    public String annylyDateValue() {
        boolean mark = true;
        for (Long mealDay : preserverAdapter.preMealMaps.keySet()) {
            Map<String, MealBean.Data> dayData = preserverAdapter.preMealMaps.get(mealDay);
            if (dayData.containsKey("午餐")) {
                hasMeal = true;
                if ((dayData.get("午餐").getNums() == 0) && (!dayData.get("午餐").isGrouporderTag())) {
                    return "还有套餐没有选择数量";
                } else {
                    if (mark) {
                        mark = false;
                        isChosedBySetNums = dayData.get("午餐").isSetNumsTag();  //是否按数量设置
                        isChosedGroup = dayData.get("午餐").isGrouporderTag();  //是否按团选择
                        int nums = dayData.get("午餐").getNums();
                        if (nums < 10) {
                            return "套餐数量未达到配送数量";
                        }
                    } else {
                        if (!(isChosedGroup == dayData.get("午餐").isGrouporderTag())) {
                            myLog("------------->午餐" + isChosedGroup + "   " + dayData.get("午餐").isGrouporderTag());
                            return "请按照同一种方式选择数量";
                        } //比较前后是不是都是按照团选择

                        int nums = dayData.get("午餐").getNums();
                        if (nums < 10) {
                            return "套餐数量未达到配送数量";
                        }
                    }
                }
            }

            if (dayData.containsKey("晚餐")) {
                hasMeal = true;
                if (dayData.get("晚餐").getNums() == 0 && (!dayData.get("晚餐").isGrouporderTag())) {
                    return "还有套餐没有选择数量";
                } else {
                    if (mark) {
                        mark = false;
                        isChosedBySetNums = dayData.get("晚餐").isSetNumsTag();  //是否按数量设置
                        isChosedGroup = dayData.get("晚餐").isGrouporderTag();  //是否按团选择  以第一个为基准
                        int nums = dayData.get("晚餐").getNums();
                        if (nums < 10) {
                            return "套餐数量未达到配送数量";
                        }
                    } else {
                        if (!(isChosedGroup == dayData.get("晚餐").isGrouporderTag())) {
                            myLog("------------->晚餐" + isChosedGroup + "   " + dayData.get("晚餐").isGrouporderTag());
                            return "请按照同一种方式选择数量";

                        } //比较前后是不是都是按照团选择
                        int nums = dayData.get("午餐").getNums();
                        if (nums < 10) {
                            return "套餐数量未达到配送数量";
                        }
                    }
                }
            }

        }

        if (!hasMeal) {
            return "还没有选择套餐";
        }
        return null;
    }


    private class PreserverAdapter extends BaseAdapter {
        private List<Long> days;
        private LinkedHashMap<Long, WeekDayBean> weakDays;
        public LinkedHashMap<Long, Map<String, MealBean.Data>> preMealMaps;

        public PreserverAdapter(List<Long> days, LinkedHashMap<Long, WeekDayBean> weakDays, LinkedHashMap<Long, Map<String, MealBean.Data>> preMealMaps) {
            this.days = days;
            this.weakDays = weakDays;
            this.preMealMaps = preMealMaps;
            myLog("------------->map大小"+preMealMaps.size());
        }

        public void updateData(Long day, String type, MealBean.Data meal, FoodsBean.Data food) {
            if (null !=preMealMaps) {
                boolean hasTheDay = preMealMaps.containsKey(day);
                myLog("---------------->是够包含" + hasTheDay);
                boolean isContain = preMealMaps.get(day).containsKey(type);
                if (isContain) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        preMealMaps.get(day).replace(type, meal);
                    }
                } else {
                    preMealMaps.get(day).put(type, meal);
                }
                PreserverAdapter.this.notifyDataSetChanged();
            }else {
                myLog("-------------------map为空");
            }
        }

        @Override
        public int getCount() {
            return weakDays.size();
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
            view = LayoutInflater.from(PreserveActivity.this).inflate(R.layout.preserver_lv_item, viewGroup, false);
            TextView dayTv = view.findViewById(R.id.day_tv);
            TextView weekTv = view.findViewById(R.id.week_tv);
            ImageView addImg1 = view.findViewById(R.id.add_img1);
            ImageView add_img0 = view.findViewById(R.id.add_img0);
            final View lunchItem = view.findViewById(R.id.lunch_item);
            final View dinnerItem = view.findViewById(R.id.dinner_item);
            final long day = days.get(position);
            WeekDayBean weekDayBean = weakDays.get(day);
            String week = weekDayBean.getMyWeek();
            String myDay = weekDayBean.getMyDay();
            dayTv.setText(myDay);
            weekTv.setText(week);


            //操作午餐
            if (preMealMaps != null && preMealMaps.get(day).containsKey("午餐")) {
                lunchItem.setTag(day + "-午餐");
                MealBean.Data lunch = preMealMaps.get(day).get("午餐");
                TextView mealTitle = lunchItem.findViewById(R.id.meal_title_tv);
                TextView mealContext = lunchItem.findViewById(R.id.meal_content_tv);
                ImageView foodImg1 = lunchItem.findViewById(R.id.food_img);
                TextView priceTv = lunchItem.findViewById(R.id.price_tv);
                priceTv.setText("￥" + lunch.getPrice());
                mealTitle.setText(lunch.getPackageName());
                if (lunch.isSelectByMaster()) {
                    mealTitle.setTextColor(getResources().getColor(R.color.main_text_color));
                    mealContext.setTextColor(getResources().getColor(R.color.main_text_color));
                }
                List<FoodList> foodLists = lunch.getFoodList();
                StringBuffer foodBf = new StringBuffer();
                if (null != foodLists && foodLists.size() > 0) {
                    for (FoodList foodList :
                            foodLists) {
                        foodBf.append(foodList.getDishName() + "\r\n");
                    }
                    mealContext.setText(foodBf.toString());
                }

                DisplayImageOptions options = new DisplayImageOptions.Builder()
                        .showImageForEmptyUri(R.drawable.food1)
                        .cacheInMemory(true)
                        .cacheOnDisk(true)
                        .bitmapConfig(Bitmap.Config.RGB_565)
                        .build();
                String imgSrc = lunch.getImg();
                imageLoader.displayImage(imgSrc, foodImg1, options);
                ImageView deleteImg = lunchItem.findViewById(R.id.delete_img);
                deleteImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deleteMeal(day, dinnerItem, "午餐");
                    }
                });
                foodImg1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(PreserveActivity.this, PreserveListActivity.class);
                        intent.putExtra("data", (String) lunchItem.getTag());
                        startActivityForResult(intent, CHOSE_MEAL);
                    }
                });
                //显示数量
                myLog("-------------->" + lunch.getNums() + "份");
                TextView numTv = lunchItem.findViewById(R.id.nums_tv);
                if (lunch.isGrouporderTag()) {
                    numTv.setText(lunch.getRelatedGroupName());
                }

                if (lunch.isSetNumsTag()) {
                    if (lunch.getNums() > 0) {
                        numTv.setText(lunch.getNums() + "份");
                    }
                }
                //午餐设置数量
                LinearLayout setCountLl = lunchItem.findViewById(R.id.set_count_ll);
                setCountLl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog = new ChoseGroupDialog(PreserveActivity.this, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                lunchItem.setTag(day + "-午餐");
                                Intent intent = new Intent(PreserveActivity.this, SetCountActivity.class);
                                intent.putExtra("data", (String) lunchItem.getTag());
                                startActivityForResult(intent, SET_NUMS);
                                dialog.dismiss();
                            }
                        }, (String) lunchItem.getTag());
                        dialog.show();
                    }
                });

            } else {
                lunchItem.setTag(day + "-午餐");
                lunchItem.setVisibility(View.GONE);
                add_img0.setVisibility(View.VISIBLE);
                add_img0.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(PreserveActivity.this, PreserveListActivity.class);
                        intent.putExtra("data", (String) lunchItem.getTag());
                        startActivityForResult(intent, CHOSE_MEAL);
                    }
                });
            }


            //操作晚餐
            if (preMealMaps != null && preMealMaps.get(day).containsKey("晚餐")) {

                dinnerItem.setTag(day + "-晚餐");
                MealBean.Data lunch = preMealMaps.get(day).get("晚餐");
                TextView mealTitle = dinnerItem.findViewById(R.id.meal_title_tv);
                TextView mealContext = dinnerItem.findViewById(R.id.meal_content_tv);
                ImageView foodImg = dinnerItem.findViewById(R.id.food_img);
                if (lunch.isSelectByMaster()) {
                    mealTitle.setTextColor(getResources().getColor(R.color.main_text_color));
                    mealContext.setTextColor(getResources().getColor(R.color.main_text_color));
                }
                TextView priceTv = dinnerItem.findViewById(R.id.price_tv);
                priceTv.setText("￥" + lunch.getPrice());

                TextView numTv = dinnerItem.findViewById(R.id.nums_tv);
                if (lunch.getNums() > 0) {
                    numTv.setText(lunch.getNums() + "份");
                }
                foodImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(PreserveActivity.this, PreserveListActivity.class);
                        intent.putExtra("data", (String) dinnerItem.getTag());
                        startActivityForResult(intent, CHOSE_MEAL);
                    }
                });
                mealTitle.setText(lunch.getPackageName());
                List<FoodList> foodLists = lunch.getFoodList();
                StringBuffer foodBf = new StringBuffer();
                if (null != foodLists && foodLists.size() > 0) {
                    for (FoodList foodList :
                            foodLists) {
                        foodBf.append(foodList.getDishName() + "\r\n");
                    }
                    mealContext.setText(foodBf.toString());
                }

                DisplayImageOptions options = new DisplayImageOptions.Builder()
                        .showImageForEmptyUri(R.drawable.food1)
                        .cacheInMemory(true)
                        .cacheOnDisk(true)
                        .bitmapConfig(Bitmap.Config.RGB_565)
                        .build();
                String imgSrc = lunch.getImg();
                imageLoader.displayImage(imgSrc, foodImg, options);

                ImageView deleteImg = dinnerItem.findViewById(R.id.delete_img);
                deleteImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deleteMeal(day, dinnerItem, "晚餐");
                    }
                });

                //显示数量
                myLog("---------wan----->" + lunch.getNums() + "份");
                if (lunch.isGrouporderTag()) {
                    numTv.setText(lunch.getRelatedGroupName());
                }

                if (lunch.isSetNumsTag()) {
                    if (lunch.getNums() > 0) {
                        numTv.setText(lunch.getNums() + "份");
                    }
                }

                //设置数量
                LinearLayout setCountLl = dinnerItem.findViewById(R.id.set_count_ll);
                setCountLl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog = new ChoseGroupDialog(PreserveActivity.this, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dinnerItem.setTag(day + "-晚餐");
                                Intent intent = new Intent(PreserveActivity.this, SetCountActivity.class);
                                intent.putExtra("data", (String) dinnerItem.getTag());
                                startActivityForResult(intent, SET_NUMS);
                                dialog.dismiss();
                            }
                        }, (String) dinnerItem.getTag());
                        dialog.show();
                    }
                });

            } else {
                dinnerItem.setTag(day + "-晚餐");
                dinnerItem.setVisibility(View.GONE);
                addImg1.setVisibility(View.VISIBLE);
                addImg1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(PreserveActivity.this, PreserveListActivity.class);
                        intent.putExtra("data", (String) dinnerItem.getTag());
                        startActivityForResult(intent, CHOSE_MEAL);
                    }
                });
            }
            return view;
        }

        DeleteMealDialog dialog1;

        private void deleteMeal(final long day, final View dinnerItem, final String mealType) {
            dialog1 = new DeleteMealDialog(PreserveActivity.this
                    , new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    preMealMaps.get(day).remove(mealType);
                    dinnerItem.setTag(day + "-" + mealType);
                    PreserverAdapter.this.notifyDataSetChanged();
                    dialog1.dismiss();
                }
            });
            dialog1.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOSE_MEAL && resultCode == PreserveListActivity.AFTER_CHOESE) {
            MealBean.Data mel = new MealBean.Data();
            mel.setSelectByMaster(true);  //是否是后选择的 改变字体颜色
            long day = data.getLongExtra("day", 20180101);
            String type = data.getStringExtra("type");
            String foodSt = data.getStringExtra("food");
            FoodsBean.Data food = mGson.fromJson(foodSt, FoodsBean.Data.class);
            mel.setMenuDay(day);
            mel.setPackageId(food.getPackageId());
            mel.setPackageName(food.getPackageName());
            String foodListSt = mGson.toJson(food.getFoodList());
            List<FoodList> foodList = mGson.fromJson(foodListSt, new TypeToken<List<FoodList>>() {
            }.getType());
            mel.setFoodList(foodList);
            mel.setPrice(food.getPrice());
            mel.setImg(food.getImg());
            if (type.equals("午餐")) {
                mel.setEatType(1);
            } else if (type.equals("晚餐")) {
                mel.setEatType(2);
            }
            preserverAdapter.updateData(day, type, mel, food);
        }

        if (requestCode == SET_NUMS && resultCode == SetCountActivity.SET_COUNT) {
            long day = data.getLongExtra("day", 20180101);
            String type = data.getStringExtra("type");
            int nums = data.getIntExtra("nums", 0);
            myLog("------------>" + day + "  " + type + "  " + nums);
            boolean isContain = preserverAdapter.preMealMaps.get(day).containsKey(String.valueOf(type));
            myLog("-------------" + isContain);
            for (Long key : preserverAdapter.preMealMaps.keySet()
                    ) {
                for (String meal :
                        preserverAdapter.preMealMaps.get(key).keySet()) {
                    preserverAdapter.preMealMaps.get(key).get(meal).setSetNumsTag(true);
                    preserverAdapter.preMealMaps.get(key).get(meal).setNums(nums);
                    preserverAdapter.preMealMaps.get(key).get(meal).setGrouporderTag(false);
                    preserverAdapter.preMealMaps.get(key).get(meal).setRelativeGroupId(0);
                    preserverAdapter.preMealMaps.get(key).get(meal).setRelatedGroupName(null);
                }
            }
           /* if (isContain) {
                preserverAdapter.preMealMaps.get(day).get(String.valueOf(type)).setSetNumsTag(true);
                preserverAdapter.preMealMaps.get(day).get(String.valueOf(type)).setNums(nums);
                preserverAdapter.preMealMaps.get(day).get(String.valueOf(type)).setGrouporderTag(false);
                preserverAdapter.preMealMaps.get(day).get(String.valueOf(type)).setRelativeGroupId(0);
                preserverAdapter.preMealMaps.get(day).get(String.valueOf(type)).setRelatedGroupName(null);
                myLog("--------------->fen:" + preserverAdapter.preMealMaps.get(day).get(String.valueOf(type)).getNums());
            }*/
            preserverAdapter.notifyDataSetChanged();
        }

        if (requestCode == CREAT_GROUP && resultCode == Constant.ADD_FRIEND_CODE) {
            //long day, String type, int groupId, String groupName, int nums, boolean isContain
            int groupId = data.getIntExtra("groupId", 1);
            int nums = data.getIntExtra("nums", 1);
            String groupName = data.getStringExtra("groupName");
            myLog("-------------->" + groupId + " " + nums + "  " + groupName);
            updataGroup(1, "1", groupId, groupName, nums, true);
        }

    }

    public void choseGroup(long day, String type, int groupId, String groupName, int nums) {
        boolean isContain = preserverAdapter.preMealMaps.get(day).containsKey(String.valueOf(type));

        updataGroup(day, type, groupId, groupName, nums, isContain);
    }

    private void updataGroup(long day, String type, int groupId, String groupName, int nums, boolean isContain) {
        for (Long key : preserverAdapter.preMealMaps.keySet()
                ) {
            for (String meal :
                    preserverAdapter.preMealMaps.get(key).keySet()) {
                preserverAdapter.preMealMaps.get(key).get(meal).setSetNumsTag(false);
                preserverAdapter.preMealMaps.get(key).get(meal).setNums(0);
                preserverAdapter.preMealMaps.get(key).get(meal).setGrouporderTag(true);
                preserverAdapter.preMealMaps.get(key).get(meal).setRelativeGroupId(groupId);
                preserverAdapter.preMealMaps.get(key).get(meal).setRelatedGroupName(groupName);
                preserverAdapter.preMealMaps.get(key).get(meal).setNums(nums);

            }
        }

        if (isContain) {

         /*   preserverAdapter.preMealMaps.get(day).get(String.valueOf(type)).setSetNumsTag(false);
            preserverAdapter.preMealMaps.get(day).get(String.valueOf(type)).setNums(0);
            preserverAdapter.preMealMaps.get(day).get(String.valueOf(type)).setGrouporderTag(true);
            preserverAdapter.preMealMaps.get(day).get(String.valueOf(type)).setRelativeGroupId(groupId);
            preserverAdapter.preMealMaps.get(day).get(String.valueOf(type)).setRelatedGroupName(groupName);
            preserverAdapter.preMealMaps.get(day).get(String.valueOf(type)).setNums(nums);*/
            // myLog("--------------->fen:" + preserverAdapter.preMealMaps.get(day).get(String.valueOf(type)).getNums());
        }
        preserverAdapter.notifyDataSetChanged();
    }

    ;

    private class poponDismissListener implements PopupWindow.OnDismissListener {
        @Override
        public void onDismiss() {
            allNums = 0;
        }
    }

    class WeekDayBean {
        private String myWeek;
        private String myDay;

        public WeekDayBean() {
        }

        public WeekDayBean(String myWeek, String myDay) {
            this.myWeek = myWeek;
            this.myDay = myDay;
        }

        public String getMyWeek() {
            return myWeek;
        }

        public void setMyWeek(String myWeek) {
            this.myWeek = myWeek;
        }

        public String getMyDay() {
            return myDay;
        }

        public void setMyDay(String myDay) {
            this.myDay = myDay;
        }
    }
}
