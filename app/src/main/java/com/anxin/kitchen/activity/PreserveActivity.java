package com.anxin.kitchen.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
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
    private static final int CHOSE_MEAL = 100;
    private ListView mAddPreserveLv;
    private List mDayList = new ArrayList();
    private TextView viewKitchen;
    private MealBean mealBean;
    private List<MealBean.Data> mealList;
    private PreserverAdapter preserverAdapter;
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
        preserverAdapter = new PreserverAdapter(days,weakDays,preMealMaps);
        mAddPreserveLv.setAdapter(preserverAdapter);
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

        public void updateData(long day,String type,MealBean.Data meal){
            boolean isContain = preMealMaps.get(day).containsKey(type);
            if (isContain){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    preMealMaps.get(day).replace(type,meal);
                }
            }else {
                preMealMaps.get(day).put(type,meal);
            }
            PreserverAdapter.this.notifyDataSetChanged();
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
            final View lunchItem = view.findViewById(R.id.lunch_item);
            final View dinnerItem = view.findViewById(R.id.dinner_item);
            final long day = days.get(position);
            String weekDay = weakDays.get(day);
            dayTv.setText(weekDay);


            //操作午餐
            if (preMealMaps.get(day).containsKey("午餐")){
                MealBean.Data lunch = preMealMaps.get(day).get("午餐");
                TextView mealTitle = lunchItem.findViewById(R.id.meal_title_tv);
                TextView mealContext = lunchItem.findViewById(R.id.meal_content_tv);
                mealTitle.setText(lunch.getPackageName());
                if (lunch.isSelectByMaster()){
                    mealTitle.setTextColor(getResources().getColor(R.color.main_text_color));
                    mealContext.setTextColor(getResources().getColor(R.color.main_text_color));
                }
                List<MealBean.FoodList> foodLists = lunch.getFoodList();
                StringBuffer foodBf = new StringBuffer();
                if (null!=foodLists&&foodLists.size() >0){
                    for (MealBean.FoodList foodList:
                            foodLists) {
                        foodBf.append(foodList.getDishName()+"\r\n");
                    }
                    mealContext.setText(foodBf.toString());}
                ImageView deleteImg = lunchItem.findViewById(R.id.delete_img);
                deleteImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        preMealMaps.get(day).remove("午餐");
                        PreserverAdapter.this.notifyDataSetChanged();
                        lunchItem.setTag(day+"-午餐");
                    }
                });
            }else {
                lunchItem.setTag(day+"-午餐");
                lunchItem.setVisibility(View.GONE);
                add_img0.setVisibility(View.VISIBLE);
                add_img0.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(PreserveActivity.this,PreserveListActivity.class);
                        intent.putExtra("data",(String) lunchItem.getTag());
                        startActivityForResult(intent,CHOSE_MEAL);
                    }
                });
            }


            //操作晚餐
            if (preMealMaps.get(day).containsKey("晚餐")){
                {
                    MealBean.Data lunch = preMealMaps.get(day).get("晚餐");
                    TextView mealTitle = dinnerItem.findViewById(R.id.meal_title_tv);
                    TextView mealContext = dinnerItem.findViewById(R.id.meal_content_tv);
                    if (lunch.isSelectByMaster()){
                        mealTitle.setTextColor(getResources().getColor(R.color.main_text_color));
                        mealContext.setTextColor(getResources().getColor(R.color.main_text_color));
                    }

                    mealTitle.setText(lunch.getPackageName());
                    List<MealBean.FoodList> foodLists = lunch.getFoodList();
                    StringBuffer foodBf = new StringBuffer();
                    if (null!=foodLists&&foodLists.size() >0) {
                        for (MealBean.FoodList foodList :
                                foodLists) {
                            foodBf.append(foodList.getDishName() + "\r\n");
                        }
                        mealContext.setText(foodBf.toString());
                    }
                    ImageView deleteImg = dinnerItem.findViewById(R.id.delete_img);
                    deleteImg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            preMealMaps.get(day).remove("晚餐");
                            dinnerItem.setTag(day+"-晚餐");
                            PreserverAdapter.this.notifyDataSetChanged();
                        }
                    });
                }
            }else {
                dinnerItem.setTag(day+"-晚餐");
                dinnerItem.setVisibility(View.GONE);
                addImg1.setVisibility(View.VISIBLE);
                addImg1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(PreserveActivity.this,PreserveListActivity.class);
                        intent.putExtra("data",(String)dinnerItem.getTag());
                        startActivityForResult(intent,CHOSE_MEAL);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOSE_MEAL && resultCode == PreserveListActivity.AFTER_CHOESE){
            MealBean.Data mel = new MealBean.Data();
            mel.setSelectByMaster(true);
            long day = data.getLongExtra("day",20180101);
            String type = data.getStringExtra("type");
            mel.setMenuDay(day);
            mel.setPackageName("重新选择的套餐");
            if (type.equals("午餐")){
                mel.setEatType(1);
            }else if (type.equals("晚餐")){
                mel.setEatType(2);
            }
            preserverAdapter.updateData(day,type,mel);
        }
    }
}
