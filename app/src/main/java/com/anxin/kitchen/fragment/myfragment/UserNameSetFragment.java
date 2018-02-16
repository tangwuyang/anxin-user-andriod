package com.anxin.kitchen.fragment.myfragment;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.anxin.kitchen.fragment.HomeBaseFragment;
import com.anxin.kitchen.user.R;
import com.anxin.kitchen.utils.Log;

/**
 * 设置用户名称界面
 */
public class UserNameSetFragment extends HomeBaseFragment implements View.OnClickListener {
    private Log LOG = Log.getLog();
    private View view;
    private ImageView backBtn;//返回
    private TextView storeBtn;//保存

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideMainBottom();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.user_name_set_fragment, null);
        initView();//初始化界面控制
        return view;
    }

    private void initView() {
        backBtn = (ImageView) view.findViewById(R.id.back_btn);
        storeBtn = (TextView) view.findViewById(R.id.store_btn);
        storeBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                getFragmentManager().popBackStack();
                break;
            case R.id.store_btn:
                getFragmentManager().popBackStack();
                break;
            default:
                break;
        }
    }

}
