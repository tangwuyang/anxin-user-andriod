package com.anxin.kitchen.fragment.myfragment;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.anxin.kitchen.fragment.HomeBaseFragment;
import com.anxin.kitchen.user.R;
import com.anxin.kitchen.utils.Log;

/**
 * 设置界面
 */
public class UserSettingsFragment extends HomeBaseFragment implements View.OnClickListener {
    private Log LOG = Log.getLog();
    private View view;
    private ImageView backBtn;//返回
    private RelativeLayout userIconBtn;//用户头像
    private RelativeLayout userNameBtn;//用户姓名
    private RelativeLayout userGenderBtn;//用户性别
    private RelativeLayout userBirthdayBtn;//用户生日

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
        view = inflater.inflate(R.layout.user_setting_fragment, null);
        initView();//初始化界面控制
        return view;
    }

    private void initView() {
        backBtn = (ImageView) view.findViewById(R.id.back_btn);
        backBtn.setOnClickListener(this);

        userIconBtn = (RelativeLayout) view.findViewById(R.id.user_icon_rlt);
        userNameBtn = (RelativeLayout) view.findViewById(R.id.user_name_rlt);
        userGenderBtn = (RelativeLayout) view.findViewById(R.id.user_gender_rlt);
        userBirthdayBtn = (RelativeLayout) view.findViewById(R.id.user_birthday_rlt);
        userBirthdayBtn.setOnClickListener(this);
        userGenderBtn.setOnClickListener(this);
        userNameBtn.setOnClickListener(this);
        userIconBtn.setOnClickListener(this);
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
            case R.id.user_icon_rlt://修改用户头像
                break;
            case R.id.user_name_rlt://修改用户名
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                UserNameSetFragment userNameSetFragment = new UserNameSetFragment();
                ft.replace(R.id.content_frame, userNameSetFragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(null);
                ft.commit();
                break;
            case R.id.user_gender_rlt://修改用户性别
                break;
            case R.id.user_birthday_rlt://修改用户生日
                break;
            default:
                break;
        }
    }

}
