package com.anxin.kitchen.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.anxin.kitchen.bean.MealBean;
import com.anxin.kitchen.user.R;
import com.anxin.kitchen.view.ChoseGroupDialog;
import com.anxin.kitchen.view.OrderingRuleDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PreserveActivity extends BaseActivity implements View.OnClickListener{
    private ListView mAddPreserveLv;
    private List mDayList = new ArrayList();
    private TextView viewKitchen;
    private MealBean mealBean;
    List<MealBean.Data> mealList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preserve);
        tellRule();
        initView();
        initData();
    }

    //告知订餐规则
    private void tellRule() {
        OrderingRuleDialog dialog = new OrderingRuleDialog(this);
        dialog.show();
    }

    private void initData() {
        Intent intent = getIntent();
        if (null != intent){
            String mealListSt = intent.getStringExtra("mealListSt");
            mealBean = mGson.fromJson(mealListSt,MealBean.class);
        }
        mealList = mealBean.getData();

        List<List<MealBean.Data>> dataList = new ArrayList<>();
        List<Long> days = new ArrayList<>();
        LinkedHashMap<Long,String> weakDays = new LinkedHashMap<>();
        LinkedHashMap<Long,Map<String,MealBean.Data>> preMealMaps = new LinkedHashMap<>();
        long lastDay = 0;
        for (MealBean.Data mel :
                mealList) {
            long thisDay = mel.getMenuDay();
            String weekDay = transToWeekDay(thisDay);
            if (!weakDays.containsKey(thisDay)){
                days.add(thisDay);
                weakDays.put(thisDay,weekDay);
            }
            if (!preMealMaps.containsKey(thisDay)){
                preMealMaps.put(thisDay,new HashMap<String, MealBean.Data>());
            }
            int type = mel.getEatType();
            if (type == 1){
                myLog("-----------------午餐");
                if (!preMealMaps.get(thisDay).containsKey("午餐")){
                    preMealMaps.get(thisDay).put("午餐",mel);
                }
            }else if (type == 2){
                myLog("-----------------晚餐");
                if (!preMealMaps.get(thisDay).containsKey("晚餐")){
                    preMealMaps.get(thisDay).put("晚餐",mel);
                }
            }

        }

        updateUI(days,weakDays,preMealMaps);
    }

    private void updateUI(List<Long> days, LinkedHashMap<Long, String> weakDays, LinkedHashMap<Long, Map<String, MealBean.Data>> preMealMaps) {
        setAdapter(days,weakDays,preMealMaps);
    }

    private String transToWeekDay(long thisDay) {
        String dataSt = String.valueOf(thisDay);
        StringBuffer dateBf = new StringBuffer();
        dateBf.append(dataSt.substring(0,4));
        dateBf.append("-");
        dateBf.append(dataSt.substring(4,6));
        dateBf.append("-");
        dateBf.append(dataSt.substring(6));
        dataSt = dateBf.toString();
        Date d = null;
        try {
            d = new SimpleDateFormat("yyyy-MM-dd").parse(dataSt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int day = d.getDay();
        myLog("---------------d" + day);
        switch (day){
            case 0:
                return "星期一";
            case 1:
                return "星期二";
            case 2:
                return "星期三";
            case 3:
                return "星期四";
            case 4:
                return "星期五";
            case 5:
                return "星期六";
            case 6:
                return "星期天";

        }
        return null;
    }

    private void initView() {
        setTitle("预约点餐");
        findViewById(R.id.view_kitchen).setVisibility(View.VISIBLE);
        mAddPreserveLv = findViewById(R.id.add_preserve_lv);
        ImageView back_img = (ImageView) findViewById(R.id.back_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        viewKitchen = findViewById(R.id.view_kitchen);
        viewKitchen.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    private void setAdapter(List<Long> days, LinkedHashMap<Long, String> weakDays, LinkedHashMap<Long, Map<String, MealBean.Data>> preMealMaps) {
        mAddPreserveLv.setAdapter(new PreserverAdapter(days,weakDays,preMealMaps));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.view_kitchen:
                startNewActivity(ViewKitchenActivity.class);
                break;
            default:
                break;
        }
    }

    private class PreserverAdapter extends BaseAdapter {
        private List<Long> days;
        private LinkedHashMap<Long, String> weakDays;
        private LinkedHashMap<Long, Map<String, MealBean.Data>> preMealMaps;
        public PreserverAdapter(List<Long> days, LinkedHashMap<Long, String> weakDays, LinkedHashMap<Long, Map<String, MealBean.Data>> preMealMaps) {
            this.days = days;
            this.weakDays = weakDays;
            this.preMealMaps = preMealMaps;
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
            view = LayoutInflater.from(PreserveActivity.this).inflate(R.layout.preserver_lv_item,viewGroup,false);
            TextView dayTv = view.findViewById(R.id.day_tv);
            ImageView addImg1 = view.findViewById(R.id.add_img1);
            ImageView add_img0 = view.findViewById(R.id.add_img0);
            View lunchItem = view.findViewById(R.id.lunch_item);
            View dinnerItem = view.findViewById(R.id.dinner_item);
            long day = days.get(position);
            String weekDay = weakDays.get(day);
            dayTv.setText(weekDay);

            if (preMealMaps.get(day).containsKey("午餐")){

            }else {
                lunchItem.setVisibility(View.GONE);
                add_img0.setVisibility(View.VISIBLE);
                add_img0.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startNewActivity(PreserveListActivity.class);
                    }
                });
            }

            if (preMealMaps.get(day).containsKey("晚餐")){

            }else {
                dinnerItem.setVisibility(View.GONE);
                addImg1.setVisibility(View.VISIBLE);
                addImg1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startNewActivity(PreserveListActivity.class);
                    }
                });
            }

            LinearLayout setCountLl = view.findViewById(R.id.set_count_ll);
            setCountLl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ChoseGroupDialog dialog = new ChoseGroupDialog(PreserveActivity.this);
                    dialog.show();
                }
            });
            return view;
        }
    }
}
