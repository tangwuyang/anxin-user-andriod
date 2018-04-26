package com.anxin.kitchen.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.anxin.kitchen.bean.MealBean;
import com.anxin.kitchen.bean.RecoverBean;
import com.anxin.kitchen.bean.TablewareBean;
import com.anxin.kitchen.interface_.RequestNetListener;
import com.anxin.kitchen.user.R;
import com.anxin.kitchen.utils.Constant;
import com.anxin.kitchen.utils.PrefrenceUtil;
import com.anxin.kitchen.utils.StringUtils;
import com.anxin.kitchen.utils.SystemUtility;
import com.anxin.kitchen.view.MyListView;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ChoseTablewareActivity extends BaseActivity implements View.OnClickListener, RequestNetListener {
    private static final String GET_TABLEWARE = "GET_TABLEWARE";
    private static final String GET_SEND_COST = "GET_SEND_COST";
    private TextView viewKitchen;
    private TextView mUnifyPayTv;
    private TextView mEachPayTv;
    private ListView mTablewareLv;
    public LinkedHashMap<Long, Map<String, MealBean.Data>> preMealMaps;
    private boolean isChosedGroup; //选择的是团的方式
    private boolean isChosedBySetNums;//通过选择数量
    private int allNums = 0;  //总数量
    private long tablewareId; //选择餐具id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chose_tableware);
        initData();
        initView();
        getSendCost();
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
     * intent.putExtra("meals",data);
     * intent.putExtra("groupTag",isChosedGroup);
     * intent.putExtra("inputNumsTag",isChosedBySetNums);
     * intent.putExtra("tablewareName",tablewareName);
     * intent.putExtra("tablewareUseMoney",tablewareUseMoney);
     * intent.putExtra("tablewareDepositeMoney",tablewareDepositeMoney);
     **/
    private void initData() {
        Intent intent = getIntent();
        String mealSt = intent.getStringExtra("meals");
        if (null != mealSt) {
            //还原数据
            preMealMaps = mGson.fromJson(mealSt, new TypeToken<LinkedHashMap<Long, Map<String, MealBean.Data>>>() {
            }.getType());
            for (Long day :
                    preMealMaps.keySet()) {
                myLog("-----------day---->" + day);
                Map<String, MealBean.Data> dayData = preMealMaps.get(day);
                if (dayData.containsKey("午餐")) {
                    allNums = allNums + dayData.get("午餐").getNums();
                } else if (dayData.containsKey("晚餐")) {
                    allNums = allNums + dayData.get("晚餐").getNums();
                }
            }

            getTableWare();
        }
        isChosedGroup = intent.getBooleanExtra("groupTag", false);
        isChosedBySetNums = intent.getBooleanExtra("inputNumsTag", false);
    }

    //获取配送费
    private void getSendCost() {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("kitchenId", new PrefrenceUtil(this).getKitchenId());
        requestNet(SystemUtility.get_kitchen_settingUrl(), dataMap, GET_SEND_COST);
    }

    //获取所有餐具
    private void getTableWare() {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("page", -1);
        requestNet(SystemUtility.getTablewareListUrl(), dataMap, GET_TABLEWARE);
    }

    private void initView() {
        setTitle("预约点餐");
        findViewById(R.id.view_kitchen).setVisibility(View.VISIBLE);
        ImageView back_img = (ImageView) findViewById(R.id.back_img);
        mUnifyPayTv = findViewById(R.id.unify_pay_tv);
        mEachPayTv = findViewById(R.id.each_pay_tv);
        mTablewareLv = findViewById(R.id.chose_tableware_lv);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        viewKitchen = findViewById(R.id.view_kitchen);
        viewKitchen.setOnClickListener(this);
        mUnifyPayTv.setOnClickListener(this);
        mEachPayTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.view_kitchen:
                startNewActivity(ViewKitchenActivity.class);
                break;
            case R.id.unify_pay_tv:
                break;
            case R.id.each_pay_tv:
                break;
            default:
                break;
        }
    }

    @Override
    public void requestSuccess(String responseString, String requestCode) {
        super.requestSuccess(responseString, requestCode);
        String status = StringUtils.parserMessage(responseString, Constant.REQUEST_STATUS);
        if (requestCode == GET_TABLEWARE && status.equals(Constant.REQUEST_SUCCESS)) {
            String tempSt = responseString.substring(responseString.indexOf("\"data\":"), responseString.lastIndexOf("}"));
            String tablewareSt = tempSt.substring(tempSt.indexOf(":", 0) + 1);
            TablewareBean bean = mGson.fromJson(tablewareSt, TablewareBean.class);
            mTablewareLv.setAdapter(new TablewareAdapter(bean));
        }
    }

    @Override
    public void requestFailure(String responseFailure, String requestCode) {
        super.requestFailure(responseFailure, requestCode);

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
            view = LayoutInflater.from(ChoseTablewareActivity.this).inflate(R.layout.chose_tablemare_item, viewGroup, false);
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
                tablewareId = tableware.getId();
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
}
