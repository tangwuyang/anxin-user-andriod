package com.anxin.kitchen.fragment.myfragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.anxin.kitchen.fragment.HomeBaseFragment;
import com.anxin.kitchen.user.R;
import com.anxin.kitchen.utils.Log;

/**
 * 用户主界面
 */
public class MyMainFragment extends HomeBaseFragment implements View.OnClickListener {
    private Log LOG = Log.getLog();
    private View view;
    private Button mTestMapBt;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.my_main_fragment, null);
        mTestMapBt = view.findViewById(R.id.test_map_bt);
        mTestMapBt.setOnClickListener(this);
        initView();//初始化界面控制
        return view;
    }

    private void initView() {
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.test_map_bt:
                Intent intent = new Intent(getContext(),TestMapActivity.class);
                startActivity(intent);
            default:
                break;
        }
    }


}
