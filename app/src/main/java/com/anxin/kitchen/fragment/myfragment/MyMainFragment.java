package com.anxin.kitchen.fragment.myfragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.anxin.kitchen.activity.LoginActivity;
import com.anxin.kitchen.activity.SettingActivity;
import com.anxin.kitchen.bean.Account;
import com.anxin.kitchen.event.AsyncHttpRequestMessage;
import com.anxin.kitchen.event.OnUserAcountEvent;
import com.anxin.kitchen.event.ViewUpdateHeadIconEvent;
import com.anxin.kitchen.fragment.HomeBaseFragment;
import com.anxin.kitchen.fragment.loginfragment.AddUserDataFragment;
import com.anxin.kitchen.user.R;
import com.anxin.kitchen.utils.BaseDialog;
import com.anxin.kitchen.utils.EventBusFactory;
import com.anxin.kitchen.utils.Log;
import com.anxin.kitchen.utils.StringUtils;
import com.anxin.kitchen.utils.SystemUtility;
import com.anxin.kitchen.utils.ToastUtil;
import com.anxin.kitchen.view.RoundedImageView;

import org.json.JSONException;
import org.json.JSONObject;

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
    private static final String snedGetkitchenPhone_http = "snedGetkitchenPhone";
    private String contactPhone = "";//联系电话
    private TextView workTime_tv;

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
        snedGetkitchenPhone();
        return view;
    }

    /**
     * 获取联系电话
     */
    private void snedGetkitchenPhone() {
        String urlPhat = SystemUtility.getonfigUrl();
        SystemUtility.requestNetGet(urlPhat, snedGetkitchenPhone_http);
    }

    private void initView() {
        settingBtn = (ImageView) view.findViewById(R.id.my_setting_btn);
        userSetBtn = (RelativeLayout) view.findViewById(R.id.user_set);
        userWalletBtn = (RelativeLayout) view.findViewById(R.id.wallet_rlt);
        userAddressBtn = (RelativeLayout) view.findViewById(R.id.user_address_rlt);
        userInvitationBtn = (RelativeLayout) view.findViewById(R.id.user_invitation_rlt);
        userContactBtn = (RelativeLayout) view.findViewById(R.id.user_contact_rlt);
        workTime_tv = view.findViewById(R.id.workTime);
        userContactBtn.setOnClickListener(this);
        userInvitationBtn.setOnClickListener(this);
        userAddressBtn.setOnClickListener(this);
        userWalletBtn.setOnClickListener(this);
        settingBtn.setOnClickListener(this);
        userSetBtn.setOnClickListener(this);
        userSetBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
//                AddUserDataFragment addUserDataFragment = new AddUserDataFragment();
//                FragmentTransaction ft = getFragmentManager().beginTransaction();
//                ft.replace(R.id.content_frame, addUserDataFragment);
//                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//                ft.addToBackStack(null);
//                ft.commit();
//                BaseDialog dialog = BaseDialog.showDialog(getActivity(), R.layout.orderplay_dialog);
//                Window window = dialog.getWindow();
//                window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                return false;
            }
        });

        userIcon = (RoundedImageView) view.findViewById(R.id.user_icon);
        userName = (TextView) view.findViewById(R.id.user_name);
        userPhone = (TextView) view.findViewById(R.id.user_phone);
        Totalamount_tv = view.findViewById(R.id.Totalamount_tv);
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
        super.onResume();
        updateUserAcount();
        showMainBottom();
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
//                SettingFragment settingFragment = new SettingFragment();
//                ft.replace(R.id.content_frame, settingFragment);
//                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//                ft.addToBackStack(null);
//                ft.commit();
                startActivity(new Intent(getActivity(), SettingActivity.class));
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
                if (contactPhone == null || contactPhone.length() == 0) {
                    return;
                }
                final BaseDialog dialog = BaseDialog.showDialog(getActivity(), R.layout.phone_dialog_layout);
                dialog.setText(R.id.phone_tv, contactPhone);
                TextView cancel_tv = dialog.getView(R.id.cancel_tv);
                cancel_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                TextView callPhone = dialog.getView(R.id.open_right_tv);
                callPhone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        call(contactPhone);
                    }
                });
                break;
            default:
                break;
        }
    }

    /**
     * 调用拨号界面
     *
     * @param phone 电话号码
     */
    private void call(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     * 监听网络请求返回
     *
     * @param asyncHttpRequestMessage
     */
    public void onEventMainThread(AsyncHttpRequestMessage asyncHttpRequestMessage) {
        String requestCode = asyncHttpRequestMessage.getRequestCode();
        String responseMsg = asyncHttpRequestMessage.getResponseMsg();
        String requestStatus = asyncHttpRequestMessage.getRequestStatus();
        switch (requestCode) {
            //验证码发送
            case snedGetkitchenPhone_http:
                //网络请求返回成功
                if (requestStatus != null && requestStatus.equals(SystemUtility.RequestSuccess)) {
                    String data = StringUtils.parserMessage(responseMsg, "data");
//                    LOG.e("-------data---------" + data);
                    String workTime = null;
                    contactPhone = data.substring(data.indexOf("contactPhone:") + 13, data.indexOf("}"));
//                    LOG.e("-------contactPhone---------" + contactPhone);
                    workTime = data.substring(data.indexOf("workTime:") + 9, data.indexOf(","));
//                    LOG.e("-------workTime---------" + workTime);
                    if (workTime != null && workTime.length() != 0)
                        workTime_tv.setText(workTime);
                }
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
