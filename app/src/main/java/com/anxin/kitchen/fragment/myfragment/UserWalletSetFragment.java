package com.anxin.kitchen.fragment.myfragment;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.anxin.kitchen.MyApplication;
import com.anxin.kitchen.bean.Account;
import com.anxin.kitchen.event.AsyncHttpRequestMessage;
import com.anxin.kitchen.event.OnUserAcountEvent;
import com.anxin.kitchen.event.OnUserWalletEvent;
import com.anxin.kitchen.fragment.HomeBaseFragment;
import com.anxin.kitchen.fragment.loginfragment.AddUserDataFragment;
import com.anxin.kitchen.user.R;
import com.anxin.kitchen.utils.EventBusFactory;
import com.anxin.kitchen.utils.Log;
import com.anxin.kitchen.utils.StringUtils;
import com.anxin.kitchen.utils.SystemUtility;
import com.anxin.kitchen.utils.ToastUtil;
import com.anxin.kitchen.utils.UmengHelper;

/**
 * 用户钱包界面
 */
public class UserWalletSetFragment extends HomeBaseFragment implements View.OnClickListener {
    private Log LOG = Log.getLog();
    private View view;
    private ImageView backBtn;//返回
    private TextView detailedBtn;//查看明细
    private RelativeLayout walletWithdrawalsBtn;//提现
    private RelativeLayout walletMortgageBtn;//押金
    private TextView TotalAmount_tv;//总金额
    private TextView walletMortgage_tv;//押金
    private TextView UserDeposit_refund;
    private int userMoney = 0;
    private int userDeposit = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBusFactory.getInstance().register(this);
        hideMainBottom();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.user_wallet_fragment, null);
        initView();//初始化界面控制
        SystemUtility.sendGetUserInfo(SystemUtility.AMToken, SystemUtility.sendUserWallet);
        return view;
    }

    private void initView() {
        backBtn = (ImageView) view.findViewById(R.id.back_btn);
        detailedBtn = (TextView) view.findViewById(R.id.detailed_btn);
        walletWithdrawalsBtn = (RelativeLayout) view.findViewById(R.id.wallet_withdrawals_rlt);
        walletMortgageBtn = (RelativeLayout) view.findViewById(R.id.wallet_mortgage_rlt);
        walletMortgageBtn.setOnClickListener(this);
        walletWithdrawalsBtn.setOnClickListener(this);
        detailedBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);

        TotalAmount_tv = view.findViewById(R.id.Totalamount_tv);
        walletMortgage_tv = view.findViewById(R.id.wallet_mortgage_tv);
        UserDeposit_refund = view.findViewById(R.id.UserDeposit_refund);
        updateWallet();
    }

    private void updateWallet() {
        Account account = mApp.getAccount();
        //余额
        String userMoney = account.getUserMoney();
        if (userMoney != null && userMoney.length() != 0) {
            this.userMoney = Integer.valueOf(userMoney);
            TotalAmount_tv.setText(userMoney);
        }
        //押金
        String UserDeposit = account.getUserDeposit();
        if (UserDeposit != null && UserDeposit.length() != 0) {
            this.userDeposit = Integer.valueOf(UserDeposit);
            walletMortgage_tv.setText(UserDeposit);
        }
        if (userDeposit > 0)
            UserDeposit_refund.setVisibility(View.VISIBLE);
        else
            UserDeposit_refund.setVisibility(View.GONE);
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
        String codeToKen = StringUtils.parserMessage(responseMsg, "code");
        if (codeToKen != null && (codeToKen.equals("4") || codeToKen.equals("7"))) {
            SystemUtility.startLoginUser(getActivity());
            return;
        }
    }

    public void onEventMainThread(OnUserAcountEvent event) {//用户信息修改监听
        updateWallet();
    }

    public void onEventMainThread(OnUserWalletEvent event) {//用户信息修改监听
        updateWallet();
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        switch (v.getId()) {
            case R.id.back_btn:
                getFragmentManager().popBackStack();
                break;
            case R.id.detailed_btn:
                WalletDetailedFragment walletDetailedFragment = new WalletDetailedFragment();
                ft.replace(R.id.content_frame, walletDetailedFragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(null);
                ft.commit();
                break;
            case R.id.wallet_withdrawals_rlt:
                if (userMoney == 0)
                    return;
                WalletExtractFragment walletExtractFragment = new WalletExtractFragment();
                ft.replace(R.id.content_frame, walletExtractFragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(null);
                ft.commit();
                break;
            case R.id.wallet_mortgage_rlt:
                if (userDeposit == 0)
                    return;
                WalletDepositFragment walletDepositFragment = new WalletDepositFragment();
                ft.replace(R.id.content_frame, walletDepositFragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(null);
                ft.commit();
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusFactory.getInstance().unregister(this);
    }
}
