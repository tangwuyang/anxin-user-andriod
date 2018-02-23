package com.anxin.kitchen.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.anxin.kitchen.user.R;
import com.anxin.kitchen.view.MyGridView;
import com.anxin.kitchen.view.MyListView;

import java.util.ArrayList;
import java.util.List;
import java.util.SimpleTimeZone;

/**
 * 订餐详细介绍
 * created by tangwuyang
 * */
public class MealIntroduceActivity extends AppCompatActivity {
    private List<String> nameLists = new ArrayList<>();
    private List<String> nunmsLists = new ArrayList<>();
    private List<String> stepLists = new ArrayList<>();

    private MyGridView meterialGv;
    private MyListView stepsLv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_introduce);
        initData();
        initView();
    }

    private void initView() {
        meterialGv = findViewById(R.id.material_gv);
        stepsLv = findViewById(R.id.step_lv);
        meterialGv.setAdapter(new MeterialGv());
        stepsLv.setAdapter(new StepAdapter());
    }

    class StepAdapter extends  BaseAdapter{

        @Override
        public int getCount() {
            return stepLists.size();
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
            view = LayoutInflater.from(MealIntroduceActivity.this).inflate(R.layout.cook_step_item,viewGroup,false);
            TextView step = view.findViewById(R.id.step_tv);
            step.setText(stepLists.get(i));

            return view;
        }
    }
    class MeterialGv extends BaseAdapter{

        @Override
        public int getCount() {
            return nameLists.size();
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
            view = LayoutInflater.from(MealIntroduceActivity.this).inflate(R.layout.material_item,viewGroup,false);
            TextView name = view.findViewById(R.id.material_name_tv);
            name.setText(nameLists.get(i));
            TextView nums = view.findViewById(R.id.nums_tv);
            nums.setText(nunmsLists.get(i));
            return view;
        }
    }
    private void initData() {
        nameLists.add("大闸蟹");
        nameLists.add("香辣酱");
        nameLists.add("生姜");
        nameLists.add("豆瓣酱");
        nunmsLists.add("1只");
        nunmsLists.add("1只");
        nunmsLists.add("1只");
        nunmsLists.add("1只");
        stepLists.add("1、大闸蟹用铁签或剪刀破蟹甲穿心、去软壳，用软毛刷\n" +
                "洗刷干净");
        stepLists.add("2、将螃蟹剪去小腿，解小块，拆蟹盖，去腮备用。大葱\n" +
                "切段，小葱切花，生姜切片，干辣椒切段备用");
    }
}
