package com.anxin.kitchen.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.anxin.kitchen.adapter.MyBaseAdapter;
import com.anxin.kitchen.user.R;
import com.anxin.kitchen.view.HorizontalScrollMenu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ViewKitchenActivity extends BaseActivity implements View.OnClickListener{
    private static final String[] CONTENT = new String[] { "频道一", "频道二", "频道三", "频道四", "频道五", "频道六" };
    private List<Fragment> list=new ArrayList<Fragment>();

    private TabLayout tabLayout = null;
    private HorizontalScrollMenu hsm_container;
    private ImageView mBackImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_kitchen);
        ((TextView)findViewById(R.id.title_tv)).setText("厨师视频");
        initView();
    }

    public void initView()
    {
        hsm_container = (HorizontalScrollMenu) findViewById(R.id.hsm_container);
        hsm_container.setSwiped(false);
        mBackImg = findViewById(R.id.back_img);
        hsm_container.setAdapter(new MenuAdapter());
        mBackImg.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_img:
                this.finish();
                break;
        }
    }


    class MenuAdapter extends MyBaseAdapter
    {
        private  String[] names = new String[] { "频道一", "频道二", "频道三", "频道四", "频道五", "频道六" };


        @Override
        public List<String> getMenuItems() {
            // TODO Auto-generated method stub
            return Arrays.asList(names);
        }

        @Override
        public List<View> getContentViews()
        {
            // TODO Auto-generated method stub
            List<View> views = new ArrayList<View>();
            for (String str : names)
            {
                View v = LayoutInflater.from(ViewKitchenActivity.this).inflate(
                        R.layout.kitchen_content_view, null);
                TextView tv = (TextView) v.findViewById(R.id.tv_content);
                tv.setText(str);
                views.add(v);
            }
            return views;
        }

        @Override
        public void onPageChanged(int position, boolean visitStatus)
        {
            // TODO Auto-generated method stub
            Toast.makeText(ViewKitchenActivity.this,
                    "内容页：" + (position + 1) + " 访问状态：" + visitStatus,
                    Toast.LENGTH_SHORT).show();
        }

    }

}

