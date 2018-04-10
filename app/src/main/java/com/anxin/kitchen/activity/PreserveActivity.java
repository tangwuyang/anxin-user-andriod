package com.anxin.kitchen.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.anxin.kitchen.bean.FoodsBean;
import com.anxin.kitchen.bean.MealBean;
import com.anxin.kitchen.bean.MealBean.FoodList;
import com.anxin.kitchen.user.R;
import com.anxin.kitchen.utils.Constant;
import com.anxin.kitchen.view.ChoseGroupDialog;
import com.anxin.kitchen.view.OrderingRuleDialog;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PreserveActivity extends BaseActivity implements View.OnClickListener{
    private static final int CHOSE_MEAL = 100;
    private static final int SET_NUMS = 102;   //设置套餐数量
    private ListView mAddPreserveLv;
    private List mDayList = new ArrayList();
    private TextView viewKitchen;
    private MealBean mealBean;
    private List<MealBean.Data> mealList;
    private PreserverAdapter preserverAdapter;
    String mealListSt;
    List<Long> days ; //未来一周的长整型集合
    ChoseGroupDialog dialog = null;
    private TextView mCloseAcountTv;  //去结算
    private boolean hasMeal = false; //是否有选择餐
    private boolean isChosedGroup = false;   //是否全部选择的是团选择团
    private boolean isChosedBySetNums = false; //是否选择输入数量
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preserve);
        tellRule();
        initView();
        getNextDayOfWeek();
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
            mealListSt = intent.getStringExtra("mealListSt");
            mealBean = mGson.fromJson(mealListSt,MealBean.class);
            myLog("------->" + mealListSt);
        }
        //String data = StringUtils.parserMessage(mealListSt,"data");

        if (!((null==mealListSt)||mealListSt.equals(Constant.NULL))){
            mealList = mealBean.getData();
        }

        List<List<MealBean.Data>> dataList = new ArrayList<>();
        LinkedHashMap<Long,String> weakDays = new LinkedHashMap<>();
        LinkedHashMap<Long,Map<String,MealBean.Data>> preMealMaps = new LinkedHashMap<>();
        //一周的时间跟周之间的对应
        for (int i = 0; i<days.size();i++) {
            Long day = days.get(i);
            String weekDay = transToWeekDay(day);
            if (!weakDays.containsKey(day)){
                days.add(day);
                weakDays.put(day,weekDay);
            }

            if (!preMealMaps.containsKey(day)){
                preMealMaps.put(day,new HashMap<String, MealBean.Data>());
            }
        }

        long lastDay = 0;
        if (!((null==mealListSt)||mealListSt.equals(Constant.NULL))){
            for (MealBean.Data mel :
                    mealList) {
                long thisDay = mel.getMenuDay();
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
        }else {

        }


    }


    //获取明天是星期几
    private String getNextDayOfWeek(){
        days = new ArrayList<>();
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        int month =  (now.get(Calendar.MONTH) + 1);
        int day = now.get(Calendar.DAY_OF_MONTH);
        now.set(year, month, 0);
        int dayOfMonth = now.get(Calendar.DAY_OF_MONTH);  //这个月的总天数
        int sheYuday = dayOfMonth - day;
        int diffDay = 7-sheYuday;

        //这个月还差几天  去下个月中借齐
        //只判断到了月 后期要添加年的逻辑  否则有问题
        if (diffDay<=0){
            for(int i = 1;i<=7;i++){
                String dateSt = String.valueOf(year);
                if (month<10){
                    dateSt = dateSt+"0"+month;
                }else {
                    dateSt = dateSt+month;
                }

                if ((day+1)<10){
                    dateSt = dateSt + "0"+ (day+i);
                }else {
                    dateSt = dateSt + (day+i);
                }
                days.add(Long.valueOf(dateSt));
            }
        }else {
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
                    dateSt = dateSt + "0" + (day + 1);
                } else {
                    dateSt = dateSt +  (day + 1);
                }
                days.add(Long.valueOf(dateSt));
            }
            //补足7天
            for(int i = 1; i<=7-sheYuday ; i++){
                String dateSt = String.valueOf(year);
                if ((month+1 )< 10) {
                    dateSt = dateSt + "0" + (month+1);
                } else {
                    dateSt = dateSt + month;
                }

                if ((i) < 10) {
                    dateSt = dateSt + "0" + (day);
                } else {
                    dateSt = dateSt + (day);
                }
                days.add(Long.valueOf(dateSt));
            }

        }
        for (Long dayl :
                days) {
            myLog("-------------->"+year+"  " + month + "  " + day + "   " + dayOfMonth + "  " + dayl);
        }
        myLog("-------------->"+year+"  " + month + "  " + day + "   " + dayOfMonth);
        return null;
    }

    private void updateUI(List<Long> days, LinkedHashMap<Long, String> weakDays, LinkedHashMap<Long, Map<String, MealBean.Data>> preMealMaps) {
        setAdapter(days,weakDays,preMealMaps);
    }

    private String transToWeekDay(long thisDay) {
        myLog("--------->"+ thisDay);
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
            case 1:
                return "星期一";
            case 2:
                return "星期二";
            case 3:
                return "星期三";
            case 4:
                return "星期四";
            case 5:
                return "星期五";
            case 6:
                return "星期六";
            case 0:
                return "星期天";

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
            case R.id.close_account_tv:
                String status = annylyDateValue();
                if (null==status) {
                    startNewActivity(ChoseTablewareActivity.class);
                }else {
                    Toast.makeText(this, status, Toast.LENGTH_SHORT).show();
                }
            default:
                break;
        }
    }



    public String annylyDateValue(){
        boolean mark = true;
        for (Long mealDay:preserverAdapter.preMealMaps.keySet()){
            Map<String, MealBean.Data> dayData = preserverAdapter.preMealMaps.get(mealDay);
                if (dayData.containsKey("午餐")){
                    hasMeal = true;
                    if ((dayData.get("午餐").getNums()==0)&&(!dayData.get("午餐").isGrouporderTag())){
                        return "还有套餐没有选择数量";
                    }else {
                        if (mark){
                            mark = false;
                            isChosedBySetNums = dayData.get("午餐").isSetNumsTag();  //是否按数量设置
                            isChosedGroup = dayData.get("午餐").isGrouporderTag();  //是否按团选择
                        }else {
                            if (!(isChosedGroup == dayData.get("午餐").isGrouporderTag())){
                                myLog("------------->午餐"+isChosedGroup+  "   " + dayData.get("午餐").isGrouporderTag());
                                return "请按照同一种方式选择数量";
                            } //比较前后是不是都是按照团选择
                        }
                    }
                }

            if (dayData.containsKey("晚餐")){
                hasMeal = true;
                if (dayData.get("晚餐").getNums()==0&&(!dayData.get("晚餐").isGrouporderTag())){
                    return "还有套餐没有选择数量";
                }else {
                    if (mark){
                        mark = false;
                        isChosedBySetNums = dayData.get("晚餐").isSetNumsTag();  //是否按数量设置
                        isChosedGroup = dayData.get("晚餐").isGrouporderTag();  //是否按团选择  以第一个为基准
                    }else {
                        if (!(isChosedGroup == dayData.get("晚餐").isGrouporderTag())){
                            myLog("------------->晚餐"+isChosedGroup+  "   " + dayData.get("晚餐").isGrouporderTag());
                            return "请按照同一种方式选择数量";
                        } //比较前后是不是都是按照团选择
                    }
                }
            }

        }

        if (!hasMeal){
            return "还没有选择套餐";
        }
        return null;
    }


    private class PreserverAdapter extends BaseAdapter {
        private List<Long> days;
        private LinkedHashMap<Long, String> weakDays;
        public LinkedHashMap<Long, Map<String, MealBean.Data>> preMealMaps;
        public PreserverAdapter(List<Long> days, LinkedHashMap<Long, String> weakDays, LinkedHashMap<Long, Map<String, MealBean.Data>> preMealMaps) {
            this.days = days;
            this.weakDays = weakDays;
            this.preMealMaps = preMealMaps;
        }

        public void updateData(long day, String type, MealBean.Data meal, FoodsBean.Data food){
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
                lunchItem.setTag(day+"-午餐");
                MealBean.Data lunch = preMealMaps.get(day).get("午餐");
                TextView mealTitle = lunchItem.findViewById(R.id.meal_title_tv);
                TextView mealContext = lunchItem.findViewById(R.id.meal_content_tv);
                ImageView foodImg = lunchItem.findViewById(R.id.food_img);
                TextView priceTv = lunchItem.findViewById(R.id.price_tv);
                priceTv.setText("￥"+lunch.getPrice());
                mealTitle.setText(lunch.getPackageName());
                if (lunch.isSelectByMaster()){
                    mealTitle.setTextColor(getResources().getColor(R.color.main_text_color));
                    mealContext.setTextColor(getResources().getColor(R.color.main_text_color));
                }
                List<FoodList> foodLists = lunch.getFoodList();
                StringBuffer foodBf = new StringBuffer();
                if (null!=foodLists&&foodLists.size() >0){
                    for (FoodList foodList:
                            foodLists) {
                        foodBf.append(foodList.getDishName()+"\r\n");
                    }
                    mealContext.setText(foodBf.toString());}

                DisplayImageOptions options = new DisplayImageOptions.Builder()
                        .showImageForEmptyUri(R.drawable.food1)
                        .cacheInMemory(true)
                        .cacheOnDisk(true)
                        .bitmapConfig(Bitmap.Config.RGB_565)
                        .build();
                String imgSrc = lunch.getImg();
                imageLoader.displayImage(imgSrc,foodImg,options);
                ImageView deleteImg = lunchItem.findViewById(R.id.delete_img);
                deleteImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        preMealMaps.get(day).remove("午餐");
                        PreserverAdapter.this.notifyDataSetChanged();
                        lunchItem.setTag(day+"-午餐");
                    }
                });

                //显示数量
                myLog("-------------->"+lunch.getNums()+"份");
                TextView numTv = lunchItem.findViewById(R.id.nums_tv);
                if (lunch.isGrouporderTag()){
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
                                lunchItem.setTag(day+"-午餐");
                                Intent intent = new Intent(PreserveActivity.this, SetCountActivity.class);
                                intent.putExtra("data",(String)lunchItem.getTag());
                                startActivityForResult(intent,SET_NUMS);
                                dialog.dismiss();
                            }
                        },(String)lunchItem.getTag());
                        dialog.show();
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

                    dinnerItem.setTag(day+"-晚餐");
                    MealBean.Data lunch = preMealMaps.get(day).get("晚餐");
                    TextView mealTitle = dinnerItem.findViewById(R.id.meal_title_tv);
                    TextView mealContext = dinnerItem.findViewById(R.id.meal_content_tv);
                    ImageView foodImg = dinnerItem.findViewById(R.id.food_img);
                    if (lunch.isSelectByMaster()){
                        mealTitle.setTextColor(getResources().getColor(R.color.main_text_color));
                        mealContext.setTextColor(getResources().getColor(R.color.main_text_color));
                    }
                    TextView priceTv = dinnerItem.findViewById(R.id.price_tv);
                    priceTv.setText("￥"+lunch.getPrice());

                    TextView numTv = dinnerItem.findViewById(R.id.nums_tv);
                    if (lunch.getNums()>0){
                        numTv.setText(lunch.getNums()+"份");
                    }

                    mealTitle.setText(lunch.getPackageName());
                    List<FoodList> foodLists = lunch.getFoodList();
                    StringBuffer foodBf = new StringBuffer();
                    if (null!=foodLists&&foodLists.size() >0) {
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
                    imageLoader.displayImage(imgSrc,foodImg,options);

                    ImageView deleteImg = dinnerItem.findViewById(R.id.delete_img);
                    deleteImg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            preMealMaps.get(day).remove("晚餐");
                            dinnerItem.setTag(day+"-晚餐");
                            PreserverAdapter.this.notifyDataSetChanged();
                        }
                    });

                if (lunch.isGrouporderTag()){
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
                                dinnerItem.setTag(day+"-晚餐");
                                Intent intent = new Intent(PreserveActivity.this, SetCountActivity.class);
                                intent.putExtra("data",(String)dinnerItem.getTag());
                                startActivityForResult(intent,SET_NUMS);
                                dialog.dismiss();
                            }
                        }, (String) dinnerItem.getTag());
                        dialog.show();
                    }
                });

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
            return view;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOSE_MEAL && resultCode == PreserveListActivity.AFTER_CHOESE){
            MealBean.Data mel = new MealBean.Data();
            mel.setSelectByMaster(true);  //是否是后选择的 改变字体颜色
            long day = data.getLongExtra("day",20180101);
            String type = data.getStringExtra("type");
            String foodSt = data.getStringExtra("food");
            FoodsBean.Data food = mGson.fromJson(foodSt,FoodsBean.Data.class);
            mel.setMenuDay(day);
            mel.setPackageName(food.getPackageName());
            String foodListSt = mGson.toJson(food.getFoodList());
            List<MealBean.FoodList> foodList = mGson.fromJson(foodListSt, new TypeToken<List<FoodList>>() {}.getType());
            mel.setFoodList(foodList);
            mel.setPrice(food.getPrice());
            mel.setImg(food.getImg());
            if (type.equals("午餐")){
                mel.setEatType(1);
            }else if (type.equals("晚餐")){
                mel.setEatType(2);
            }
            preserverAdapter.updateData(day,type,mel,food);
        }

        if (requestCode == SET_NUMS && resultCode == SetCountActivity.SET_COUNT){
            long day = data.getLongExtra("day",20180101);
            String type = data.getStringExtra("type");
            int nums = data.getIntExtra("nums",0);
            myLog("------------>"+day+ "  " + type+ "  " + nums);
            boolean isContain = preserverAdapter.preMealMaps.get(day).containsKey(String.valueOf(type));
            myLog("-------------" + isContain);
            if (isContain){
                preserverAdapter.preMealMaps.get(day).get(String.valueOf(type)).setSetNumsTag(true);
                preserverAdapter.preMealMaps.get(day).get(String.valueOf(type)).setNums(nums);
                preserverAdapter.preMealMaps.get(day).get(String.valueOf(type)).setGrouporderTag(false);
                preserverAdapter.preMealMaps.get(day).get(String.valueOf(type)).setRelativeGroupId(0);
                preserverAdapter.preMealMaps.get(day).get(String.valueOf(type)).setRelatedGroupName(null);
                myLog("--------------->fen:" +  preserverAdapter.preMealMaps.get(day).get(String.valueOf(type)).getNums());
            }
            preserverAdapter.notifyDataSetChanged();
        }
    }

    public void choseGroup(long day,String type,int groupId,String groupName){
        boolean isContain = preserverAdapter.preMealMaps.get(day).containsKey(String.valueOf(type));
        if (isContain){
            preserverAdapter.preMealMaps.get(day).get(String.valueOf(type)).setSetNumsTag(false);
            preserverAdapter.preMealMaps.get(day).get(String.valueOf(type)).setNums(0);
            preserverAdapter.preMealMaps.get(day).get(String.valueOf(type)).setGrouporderTag(true);
            preserverAdapter.preMealMaps.get(day).get(String.valueOf(type)).setRelativeGroupId(groupId);
            preserverAdapter.preMealMaps.get(day).get(String.valueOf(type)).setRelatedGroupName(groupName);
            myLog("--------------->fen:" +  preserverAdapter.preMealMaps.get(day).get(String.valueOf(type)).getNums());
        }
        preserverAdapter.notifyDataSetChanged();
    };

}
