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
 * 用户钱包界面
 */
public class UserWalletSetFragment extends HomeBaseFragment implements View.OnClickListener {
    private Log LOG = Log.getLog();
    private View view;
    private ImageView backBtn;//返回
    private TextView detailedBtn;//查看明细
    private RelativeLayout walletWithdrawalsBtn;//提现
    private RelativeLayout walletMortgageBtn;//押金

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
        view = inflater.inflate(R.layout.user_wallet_fragment, null);
        initView();//初始化界面控制
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
                WalletExtractFragment walletExtractFragment = new WalletExtractFragment();
                ft.replace(R.id.content_frame, walletExtractFragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(null);
                ft.commit();
                break;
            case R.id.wallet_mortgage_rlt:
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

}
