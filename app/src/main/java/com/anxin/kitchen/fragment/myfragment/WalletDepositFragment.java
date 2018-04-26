package com.anxin.kitchen.fragment.myfragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.anxin.kitchen.MyApplication;
import com.anxin.kitchen.bean.Account;
import com.anxin.kitchen.event.AsyncHttpRequestMessage;
import com.anxin.kitchen.event.OnUserWalletEvent;
import com.anxin.kitchen.fragment.HomeBaseFragment;
import com.anxin.kitchen.user.R;
import com.anxin.kitchen.utils.EventBusFactory;
import com.anxin.kitchen.utils.Log;
import com.anxin.kitchen.utils.StringUtils;
import com.anxin.kitchen.utils.SystemUtility;
import com.anxin.kitchen.utils.ToastUtil;
import com.anxin.kitchen.utils.UmengHelper;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 押金提现界面
 */
public class WalletDepositFragment extends HomeBaseFragment implements View.OnClickListener {
    private Log LOG = Log.getLog();
    private View view;
    private ImageView backBtn;//返回
    private Double userDeposit;
    private TextView userDeposit_tv;
    private EditText payUserID_edit, payUserName_rdit;
    private static final String sendReturnDeposit_http = "sendReturnDeposit";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBusFactory.getInstance().register(this);
        hideMainBottom();
        SystemUtility.sendGetUserInfo(SystemUtility.AMToken, SystemUtility.sendUserWallet);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.wallet_deposit_fragment, null);
        initView();//初始化界面控制
        return view;
    }

    private void initView() {
        backBtn = (ImageView) view.findViewById(R.id.back_btn);
        backBtn.setOnClickListener(this);

        userDeposit_tv = view.findViewById(R.id.Deposit_tv);//押金
        payUserID_edit = view.findViewById(R.id.payUserID);//支付宝账户
        payUserName_rdit = view.findViewById(R.id.payUserName);//支付宝姓名
        View determineBtn = view.findViewById(R.id.determine_btn);//确认提现
        determineBtn.setOnClickListener(this);
        updateWallet();
    }

    public void onEventMainThread(OnUserWalletEvent event) {//用户信息修改监听
        updateWallet();
    }

    private void updateWallet() {
        Account account = mApp.getAccount();
        //余额
        String userMoney = account.getUserDeposit();
        if (userMoney != null && userMoney.length() != 0) {
            this.userDeposit = Double.parseDouble(userMoney);
            userDeposit_tv.setText(userDeposit + "");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("WalletDepositFragment");
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("WalletDepositFragment");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                getFragmentManager().popBackStack();
                break;
            case R.id.determine_btn:
                if (userDeposit == 0){
                    ToastUtil.showToast("您没有押金");
                    return;
                }
                String payID = payUserID_edit.getText().toString();
                String payName = payUserName_rdit.getText().toString();
                if (payID == null || payID.length() == 0) {
                    ToastUtil.showToast("请输入支付宝账号");
                    return;
                }
                if (payName == null || payName.length() == 0) {
                    ToastUtil.showToast("请输入您的姓名");
                    return;
                }
                sendReturnMoney(userDeposit, payID, payName);
                break;
            default:
                break;
        }

    }

    private void sendReturnMoney(Double money, String alipay, String trueName) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("money", money);
            jsonObject.put("alipay", alipay);
            jsonObject.put("trueName", trueName);
            jsonObject.put("type", 2);
        } catch (JSONException e) {
            MobclickAgent.reportError(MyApplication.getInstance(), e);
        }
        String urlPath = SystemUtility.sendUserWithdraw();
        Map<String, Object> dataMap = new HashMap();
        dataMap.put("token", SystemUtility.AMToken);
        dataMap.put("formData", jsonObject.toString());
        SystemUtility.requestNetPost(urlPath, dataMap, sendReturnDeposit_http);
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
//        LOG.e("----------requestCode------" + requestCode);
//        LOG.e("----------responseMsg------" + responseMsg);
//        LOG.e("----------requestStatus------" + requestStatus);
        switch (requestCode) {
            //验证码登陆
            case sendReturnDeposit_http:
                if (requestStatus != null && requestStatus.equals(SystemUtility.RequestSuccess)) {
                    String code = StringUtils.parserMessage(responseMsg, "code");
                    String data = StringUtils.parserMessage(responseMsg, "data");
                    if (code != null && code.equals("1")) {
                        ToastUtil.showToast("提现成功");
                        getFragmentManager().popBackStack();
                    }
                }
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusFactory.getInstance().unregister(this);
    }
}
