package com.anxin.kitchen.fragment.myfragment;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.anxin.kitchen.fragment.HomeBaseFragment;
import com.anxin.kitchen.user.R;
import com.anxin.kitchen.utils.Log;
import com.anxin.kitchen.utils.SystemUtility;
import com.anxin.kitchen.utils.UmengHelper;
import com.umeng.analytics.MobclickAgent;

/**
 * 设置界面
 */
public class SettingFragment extends HomeBaseFragment implements View.OnClickListener {
    private Log LOG = Log.getLog();
    private View view;
    private ImageView backBtn;//返回
    private RelativeLayout myRlt;//关于我们
    private RelativeLayout agreementRlt;//服务协议
    private Button logoutLoginBtn;

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
        view = inflater.inflate(R.layout.setting_fragment, null);
        initView();//初始化界面控制
        return view;
    }

    private void initView() {
        myRlt = (RelativeLayout) view.findViewById(R.id.my_rlt);
        agreementRlt = (RelativeLayout) view.findViewById(R.id.agreement_rlt);
        backBtn = (ImageView) view.findViewById(R.id.back_btn);
        logoutLoginBtn = view.findViewById(R.id.logout_user);
        logoutLoginBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        myRlt.setOnClickListener(this);
        agreementRlt.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("SettingFragment");
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("SettingFragment");
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                getFragmentManager().popBackStack();
                break;
            case R.id.my_rlt://关于我们
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                MyHomeFragment myHomeFragment = new MyHomeFragment();
                ft.replace(R.id.content_frame, myHomeFragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(null);
                ft.commit();
                break;
            case R.id.agreement_rlt://服务协议
                FragmentTransaction ft2 = getFragmentManager().beginTransaction();
                AgreementFragment agreementFragment = new AgreementFragment();
                ft2.replace(R.id.content_frame, agreementFragment);
                ft2.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft2.addToBackStack(null);
                ft2.commit();
                break;
            case R.id.logout_user:
                if (mApp.getCache().getAMToken() == null)
                    return;
                String userId = mApp.getAccount().getUserID();
                if (userId != null)
                    UmengHelper.getInstance().deleteUserAlias(userId);
                mApp.setAccount(null);
                mApp.getCache().setAcount(getActivity(), null);
                mApp.getCache().setAMToken(null);
                SystemUtility.AMToken = "";
                getFragmentManager().popBackStack();
                break;
            default:
                break;
        }
    }

}
