package com.anxin.kitchen.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.anxin.kitchen.user.R;

import java.util.ArrayList;
import java.util.List;

public class SendMealLocationActivity extends BaseActivity implements View.OnClickListener{
    private ListView mLocationsLv;
    private List<String> mLocationList = new ArrayList<String>();
    private List<String> mContactList = new ArrayList<>();
    private ImageView mBackImg;
    private LinearLayout mRelocationLl;
    private TextView mAddBt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_meal_location);
        initView();
        getData();
    }

    private void initView() {
        setTitle("送餐地址");
        mLocationsLv = findViewById(R.id.send_location_lv);
        mBackImg = findViewById(R.id.back_img);
        mRelocationLl = findViewById(R.id.re_location_ll);
        mAddBt = findViewById(R.id.add_bt);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setListeners();
        setAdapter();
    }

    private void setListeners() {
        mRelocationLl.setOnClickListener(this);
        mBackImg.setOnClickListener(this);
        mAddBt.setOnClickListener(this);
    }

    private void setAdapter() {
        SendMealLocationAdapter adapter = new SendMealLocationAdapter();
        mLocationsLv.setAdapter(adapter);
    }

    private void getData() {
        mLocationList.add("地址:深圳市南山区，西丽大学城");
        mLocationList.add("地址:深圳市南山区，科信科技园");
        mContactList.add("黄先生   138*****4567");
        mContactList.add("胡小姐   138*****4567");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.re_location_ll:
                startNewActivity(LocationActivity.class);
                break;
            case R.id.back_img:
                onBackPressed();
                break;
            case R.id.add_bt:
                startNewActivity(AddNewLocationActivity.class);
                break;
        }
    }


    private class SendMealLocationAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return mContactList.size();
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
            view = LayoutInflater.from(SendMealLocationActivity.this).inflate(R.layout.send_meal_location_item,viewGroup,false);
            TextView locationTv = view.findViewById(R.id.location_tv);
            TextView contactTv = view.findViewById(R.id.contact_tv);
            locationTv.setText(mLocationList.get(position));
            contactTv.setText(mContactList.get(position));
            return view;
        }
    }
}
