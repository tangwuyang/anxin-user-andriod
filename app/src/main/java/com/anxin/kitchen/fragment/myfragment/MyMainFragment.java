package com.anxin.kitchen.fragment.myfragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.anxin.kitchen.activity.LoginActivity;
import com.anxin.kitchen.bean.Account;
import com.anxin.kitchen.event.OnUserAcountEvent;
import com.anxin.kitchen.event.ViewUpdateHeadIconEvent;
import com.anxin.kitchen.fragment.HomeBaseFragment;
import com.anxin.kitchen.fragment.loginfragment.AddUserDataFragment;
import com.anxin.kitchen.user.R;
import com.anxin.kitchen.utils.EventBusFactory;
import com.anxin.kitchen.utils.Log;
import com.anxin.kitchen.utils.SystemUtility;
import com.anxin.kitchen.view.RoundedImageView;

import java.io.File;

/**
 * 用户主界面
 */
public class MyMainFragment extends HomeBaseFragment implements View.OnClickListener {
    private Log LOG = Log.getLog();
    private View view;
    private ImageView settingBtn;//设置界面
    private RelativeLayout userSetBtn;//用户设置
    private RelativeLayout userWalletBtn;//钱包
    private RelativeLayout userAddressBtn;//我的地址
    private RelativeLayout userInvitationBtn;//邀请好友
    private RelativeLayout userContactBtn;//联系我们

    private RoundedImageView userIcon;//用户头像
    private TextView userName;//用户名称
    private TextView userPhone;//用户手机
    private TextView Totalamount_tv;//用户钱包金额

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBusFactory.getInstance().register(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.my_main_fragment, null);
        initView();//初始化界面控制
        return view;
    }

    private void initView() {
        settingBtn = (ImageView) view.findViewById(R.id.my_setting_btn);
        userSetBtn = (RelativeLayout) view.findViewById(R.id.user_set);
        userWalletBtn = (RelativeLayout) view.findViewById(R.id.wallet_rlt);
        userAddressBtn = (RelativeLayout) view.findViewById(R.id.user_address_rlt);
        userInvitationBtn = (RelativeLayout) view.findViewById(R.id.user_invitation_rlt);
        userContactBtn = (RelativeLayout) view.findViewById(R.id.user_contact_rlt);
        userContactBtn.setOnClickListener(this);
        userInvitationBtn.setOnClickListener(this);
        userAddressBtn.setOnClickListener(this);
        userWalletBtn.setOnClickListener(this);
        settingBtn.setOnClickListener(this);
        userSetBtn.setOnClickListener(this);
        userSetBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AddUserDataFragment addUserDataFragment = new AddUserDataFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, addUserDataFragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(null);
                ft.commit();
                return false;
            }
        });

        userIcon = (RoundedImageView) view.findViewById(R.id.user_icon);
        userName = (TextView) view.findViewById(R.id.user_name);
        userPhone = (TextView) view.findViewById(R.id.user_phone);
        Totalamount_tv = view.findViewById(R.id.Totalamount_tv);
        updateUserAcount();
    }

    private void updateUserAcount() {
        if (mApp.getAccount() == null) {
            userName.setText("立即登录");
            userPhone.setText("登录后可享受更多特权");

            userIcon.setImageResource(R.drawable.icon);
            return;
        }
        Account account = mApp.getAccount();
        //获取本地用户名称
        String name = account.getUserTrueName();
        if (name != null && name.length() != 0) {
            userName.setText(name);
        }
        //获取本地缓存头像
        String mImageURI = mApp.getCache().getAccountImageURI(mApp.getCache().getUserPhone());
        if (mImageURI != null && !mImageURI.isEmpty()) {
            Uri mSaveUri = Uri.fromFile(new File(mImageURI));
            userIcon.setImageURI(mSaveUri);
        }
        //获取本地用户号码
        String phone = mApp.getCache().getUserPhone();
        if (phone != null && phone.length() != 0) {
            userPhone.setText(phone);
        }
        String userMoney = account.getUserMoney();
//        LOG.e("--------------userMoney---------------" + userMoney);
        if (userMoney != null && userMoney.length() != 0) {
            Totalamount_tv.setText(userMoney);
        }
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        showMainBottom();
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        switch (v.getId()) {
            case R.id.test_map_bt:
//                Intent intent = new Intent(getContext(),TestMapActivity.class);
//                startActivity(intent);
                break;
            case R.id.my_setting_btn://设置界面
                SettingFragment settingFragment = new SettingFragment();
                ft.replace(R.id.content_frame, settingFragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(null);
                ft.commit();
                break;
            case R.id.user_set://用户个性化设置
                if (mApp.getAccount() == null) {
                    SystemUtility.startLoginUser(getActivity());
                } else {
                    UserSettingsFragment userSettingsFragment = new UserSettingsFragment();
                    ft.replace(R.id.content_frame, userSettingsFragment);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.addToBackStack(null);
                    ft.commit();
                }
                break;
            case R.id.wallet_rlt://用户钱包
                if (mApp.getAccount() == null) {
                    SystemUtility.startLoginUser(getActivity());
                } else {
                    UserWalletSetFragment userWalletSetFragment = new UserWalletSetFragment();
                    ft.replace(R.id.content_frame, userWalletSetFragment);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.addToBackStack(null);
                    ft.commit();
                }
                break;
            case R.id.user_address_rlt://用户地址
                if (mApp.getAccount() == null) {
                    SystemUtility.startLoginUser(getActivity());
                } else {
                    UserAddressFragment userAddressFragment = new UserAddressFragment();
                    ft.replace(R.id.content_frame, userAddressFragment);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.addToBackStack(null);
                    ft.commit();
                }
                break;
            case R.id.user_invitation_rlt://邀请用户
                break;
            case R.id.user_contact_rlt://联系我们
                break;
            default:
                break;
        }
    }

    public void onEventMainThread(ViewUpdateHeadIconEvent event) {//头像修改监听
        updateUserAcount();
    }

    public void onEventMainThread(OnUserAcountEvent event) {//用户信息修改监听
        updateUserAcount();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusFactory.getInstance().unregister(this);
    }
}
