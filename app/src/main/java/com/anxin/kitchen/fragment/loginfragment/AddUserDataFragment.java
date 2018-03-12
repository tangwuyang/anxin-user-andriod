package com.anxin.kitchen.fragment.loginfragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.anxin.kitchen.fragment.HomeBaseFragment;
import com.anxin.kitchen.fragment.myfragment.UserNameSetFragment;
import com.anxin.kitchen.user.R;
import com.anxin.kitchen.utils.Log;
import com.anxin.kitchen.view.RoundedImageView;

import java.io.File;

/**
 * 完善用户资料界面
 */
public class AddUserDataFragment extends HomeBaseFragment implements View.OnClickListener {
    private Log LOG = Log.getLog();
    private View view;
    private RelativeLayout userIconBtn;//用户头像按钮
    private RelativeLayout userNameBtn;//用户名称按钮
    private RoundedImageView userIcon;//用户头像
    private TextView userName;//用户名称

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
        view = inflater.inflate(R.layout.add_user_data_fragment, null);
        initView();//初始化界面控制
        return view;
    }

    private void initView() {
        userIconBtn = (RelativeLayout) view.findViewById(R.id.user_icon_rlt);
        userNameBtn = (RelativeLayout) view.findViewById(R.id.user_name_rlt);
        userNameBtn.setOnClickListener(this);
        userIconBtn.setOnClickListener(this);

        userIcon = (RoundedImageView) view.findViewById(R.id.user_icon);
        userName = (TextView) view.findViewById(R.id.user_name);
        //获取本地用户名称
        String name = mApp.getCache().getNickName();
        if (name != null && name.length() != 0) {
            userName.setText(name);
        }
        //获取本地缓存头像
        String mImageURI = mApp.getCache().getAccountImageURI(mApp.getCache().getUserPhone());
        if (mImageURI != null && !mImageURI.isEmpty()) {
            Uri mSaveUri = Uri.fromFile(new File(mImageURI));
            userIcon.setImageURI(mSaveUri);
        }
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
            default:
                break;
        }
    }

}
