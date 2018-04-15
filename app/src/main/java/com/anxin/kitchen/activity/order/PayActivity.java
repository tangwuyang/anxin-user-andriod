package com.anxin.kitchen.activity.order;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.anxin.kitchen.activity.BaseActivity;
import com.anxin.kitchen.bean.Order.Order;
import com.anxin.kitchen.interface_.ListenerBack;
import com.anxin.kitchen.response.BaseResponse;
import com.anxin.kitchen.response.OrderPayResponse;
import com.anxin.kitchen.response.PayWeixinInfoResponse;
import com.anxin.kitchen.response.PayZhifubaoResponse;
import com.anxin.kitchen.user.R;
import com.anxin.kitchen.user.wxapi.WXEntryActivity;
import com.anxin.kitchen.utils.Cache;
import com.anxin.kitchen.utils.Constant;
import com.anxin.kitchen.utils.GlobalVariable;
import com.anxin.kitchen.utils.JsonHandler;
import com.anxin.kitchen.utils.PayUtils;
import com.anxin.kitchen.utils.PopupUtil;
import com.anxin.kitchen.utils.SystemUtility;
import com.anxin.kitchen.utils.ToastUtil;
import com.anxin.kitchen.utils.UtilHandler;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 支付类
 * Created by xujianjun on 2018/4/10.
 */

public class PayActivity extends BaseActivity implements ListenerBack {
    private final int HANDLER_SHOW_POP = 1000;
    private final int HANDLER_ZHIFUBAO_PAY = 1001;
    private Activity mActivity;
    //获取用户信息---获取余额
    private final String NET_GET_USER_INFO = "getUserInfo";
    //支付食疗订单
    private final String NET_PAY_DIET = "payDiet";
    //支付团订单(代付)
    private final String NET_PAY_GROUP = "payGroup";
    //支付团订单(AA支付)
    private final String NET_PAY_AA = "payAA";
    //支付多份订单
    private final String NET_PAY_MULTI = "payMulti";
    //创建预支付订单
    private final String NET_CREATE_ORDER = "createOrder";
    //获取订单需要支付的费用相关,wantPay > 0 标识需要唤起支付(并支付该金额)
    private final String NET_ORDER_PAY_FRR = "orderPayFee";
    //支付订单，从账户内扣款
    private final String NET_PAY_ORDERS = "payOrders";

    private String token;
    //支付的订单id
    private long orderIds;
    //    //订单类型
    private Order mOrder;
    //    //支付的金额
//    private double payMoney;
    //充值金额
    private double rechargeMoney;
    //支付的类型 1:支付宝，2：微信
    private int payType;

    private IWXAPI mApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        mActivity = this;

        orderIds = getIntent().getLongExtra("orderIds", 0);
        mOrder = (Order) getIntent().getSerializableExtra("data");

//        payMoney = getIntent().getDoubleExtra("payMoney", 0);
//        payType = getIntent().getIntExtra("payType", 0);
//        payType = 2;

//        setFinishOnTouchOutside(false);
//        getUserInfo(mActivity);
        orderPayFee();
        mApi = WXAPIFactory.createWXAPI(mActivity, WXEntryActivity.WEIXIN_APP_ID, true);
        mApi.registerApp(WXEntryActivity.WEIXIN_APP_ID);
//        createOrder(mActivity, 1, payType);

//        mHandler.sendEmptyMessageDelayed(HANDLER_SHOW_POP, 500);
        //微信支付成功
        IntentFilter filterSucceed = new IntentFilter();
        filterSucceed.addAction(Constant.BROADCAST_PAY_SUCCEED);
        registerReceiver(mReciverSucceed, filterSucceed);
        //微信支付失败
        IntentFilter filterFailedFilter = new IntentFilter();
        filterSucceed.addAction(Constant.BROADCAST_PAY_FAILED);
        registerReceiver(mReciverFailed, filterFailedFilter);


    }


    @Override
    public void requestSuccess(String responseString, String requestCode) {
        BaseResponse response = JsonHandler.getHandler().getTarget(responseString, BaseResponse.class);
        if (response == null) return;
        if (response.getCode() == 1) {
//            if (requestCode.equals(NET_GET_USER_INFO)) {
//                UserInfoResponse userResponse = JsonHandler.getHandler().getTarget(responseString, UserInfoResponse.class);
//                if (userResponse.getData().getMoney() > payMoney) {
//                    switch (mOrder.getOrderType()) {
//                        case 1:
//                            //个人订单
//                            break;
//                        case 2:
//                            //统一付款
//                            break;
//                        case 3:
//                            //AA付款
//                            break;
//                        default:
//                            break;
//                    }
//                } else {
//                    //获取支付信息签名
//
////                    createOrder(mActivity, (int) (payMoney * 100), payType);
//                }
//            } else
            if (requestCode.equals(NET_ORDER_PAY_FRR)) {
                OrderPayResponse orderPayresponse = JsonHandler.getHandler().getTarget(responseString, OrderPayResponse.class);
                if (orderPayresponse.getData().getWantPay() > 0) {
                    //唤起支付
                    rechargeMoney = UtilHandler.getInstance().toDfSum(orderPayresponse.getData().getWantPay(), "00");
                    mHandler.sendEmptyMessageDelayed(HANDLER_SHOW_POP, 500);
                } else {
                    payOrders();
                }

            } else if (requestCode.equals(NET_CREATE_ORDER)) {
                if (payType == 1) {
                    PayWeixinInfoResponse payWeixinInfoResponse = JsonHandler.getHandler().getTarget(responseString, PayWeixinInfoResponse.class);
                    PayUtils.getInstance().payWeixin(mActivity, payWeixinInfoResponse.getData());
                } else {
                    PayZhifubaoResponse payZhifubaoInfoResponse = JsonHandler.getHandler().getTarget(responseString, PayZhifubaoResponse.class);
                    PayUtils.getInstance().payZhifubao(mActivity, payZhifubaoInfoResponse.getData(), mHandler);
                }

            } else if (requestCode.equals(NET_PAY_DIET)) {

            } else if (requestCode.equals(NET_PAY_GROUP)) {

            } else if (requestCode.equals(NET_PAY_AA)) {

            } else if (requestCode.equals(NET_PAY_MULTI)) {

            } else if (requestCode.equals(NET_PAY_ORDERS)) {
                if (mOrder.getUser().getMakeType() == 1) {
                    Intent intentPayOrder = new Intent(mActivity, Order.class);
                    intentPayOrder.putExtra("orderId", orderIds);
                    mActivity.startActivity(intentPayOrder);
                } else {
//                    Intent intentPayOrder = new Intent(mActivity, OrderDetailActivity.class);
//                    intentPayOrder.putExtra("orderId", orderIds);
//                    mActivity.startActivity(intentPayOrder);
                }
                finish();

            }
        } else {
            if (response != null) {
                ToastUtil.showToast(response.getMessage());
            }

        }
    }

    @Override
    public void requestFailure(String responseFailure, String requestCode) {
        ToastUtil.showToast("支付失败");

    }

    private void getUserInfo(Activity mActivity) {
        if (null == token) {
            token = new Cache(mActivity).getAMToken();
        }
        String url = SystemUtility.getUserInfo();
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put(Constant.TOKEN, token);
        requestNet(url, dataMap, NET_GET_USER_INFO);
    }

    private void orderPayFee() {
        if (null == token) {
            token = new Cache(mActivity).getAMToken();
        }
        String url = SystemUtility.orderPayFee();
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put(Constant.TOKEN, token);
        dataMap.put("orderIds", orderIds);
        requestNet(url, dataMap, NET_ORDER_PAY_FRR);
    }

    private void payOrders() {
        if (null == token) {
            token = new Cache(mActivity).getAMToken();
        }
        String url = SystemUtility.payOrders();
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put(Constant.TOKEN, token);
        dataMap.put("orderIds", orderIds);
        requestNet(url, dataMap, NET_PAY_ORDERS);
    }

    /**
     * 创建支付订单
     *
     * @param mActivity
     * @param money
     * @param payType
     */
    private void createOrder(Activity mActivity, int money, int payType) {
        if (null == token) {
            token = new Cache(mActivity).getAMToken();
        }
        PayInfo info = new PayInfo();
        info.setMoney(money);
        info.setPayType(payType);
        String url = SystemUtility.createOrder();
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put(Constant.TOKEN, token);
        Map<String, Object> formData = new HashMap<>();
        formData.put("money", money);
        formData.put("payType", payType);
        dataMap.put("formData", JsonHandler.getHandler().toJson(formData));
        requestNet(url, dataMap, NET_CREATE_ORDER);
    }

    private void payDiet(Activity mActivity, long orderId) {
        if (null == token) {
            token = new Cache(mActivity).getAMToken();
        }
        String url = SystemUtility.payDiet();
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put(Constant.TOKEN, token);
        dataMap.put("orderId", orderId);
        requestNet(url, dataMap, NET_PAY_DIET);
    }

    private void payGroup(Activity mActivity, long orderId) {
        if (null == token) {
            token = new Cache(mActivity).getAMToken();
        }
        String url = SystemUtility.payGroup();
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put(Constant.TOKEN, token);
        dataMap.put("groupOrderId", orderId);
        requestNet(url, dataMap, NET_PAY_GROUP);
    }

    private void payAA(Activity mActivity, long orderId) {
        if (null == token) {
            token = new Cache(mActivity).getAMToken();
        }
        String url = SystemUtility.payAA();
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put(Constant.TOKEN, token);
        dataMap.put("orderId", orderId);
        requestNet(url, dataMap, NET_PAY_AA);
    }

    private void payMulti(Activity mActivity, long orderId) {
        if (null == token) {
            token = new Cache(mActivity).getAMToken();
        }
        String url = SystemUtility.payMulti();
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put(Constant.TOKEN, token);
        dataMap.put("orderId", orderId);
        requestNet(url, dataMap, NET_PAY_MULTI);
    }


    //支付宝 支付信息回调
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case GlobalVariable.PAY_ZHIFUBAO:
//                    System.out.println("---支付宝回调：" + JsonHandler.getHandler().toJson(message.obj));
                    Map<String, String> result = (Map<String, String>) message.obj;
                    AlertDialog.Builder mDialog = new AlertDialog.Builder(PayActivity.this);
                    mDialog.setTitle("提示");
                    if (result != null && result.get("resultStatus").equals("9000")) {
                        mDialog.setMessage("支付成功");
                    } else {
                        mDialog.setMessage("支付失败");
                    }
                    mDialog.show();
                    mHandler.sendEmptyMessageDelayed(HANDLER_ZHIFUBAO_PAY, 2000);
                    break;
                case HANDLER_SHOW_POP:
                    PopupUtil.getInstances().show(mActivity, findViewById(R.id.ll_all), PayActivity.this);
                    break;
                case HANDLER_ZHIFUBAO_PAY:
                    if (mOrder.getUser().getMakeType() == 1) {
                        Intent intentPayOrder = new Intent(mActivity, Order.class);
                        intentPayOrder.putExtra("orderId", orderIds);
                        mActivity.startActivity(intentPayOrder);
                    } else {

                    }
                    finish();
                    break;
            }
            return false;
        }
    });

    @Override
    public void onListener(int type, Object object, boolean isTrue) {
        switch (type) {
            case R.id.rl_tableware_deposit_account:
                break;
            case R.id.ll_pay_weixin:
                payType = 1;
//                createOrder(mActivity, (int) (rechargeMoney * 100), payType);
                createOrder(mActivity, 1, payType);
                break;
            case R.id.ll_pay_zhifubao:
                payType = 2;
//                createOrder(mActivity, (int) (rechargeMoney * 100), payType);
                createOrder(mActivity, 1, payType);
                break;
        }
    }


    //支付信息
    private class PayInfo {
        private int money;
        private int payType;

        public int getMoney() {
            return money;
        }

        public void setMoney(int money) {
            this.money = money;
        }

        public int getPayType() {
            return payType;
        }

        public void setPayType(int payType) {
            this.payType = payType;
        }

    }

    private BroadcastReceiver mReciverSucceed = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            orderPayFee();
        }
    };

    private BroadcastReceiver mReciverFailed = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (mOrder.getUser().getMakeType() == 1) {
                Intent intentPayOrder = new Intent(mActivity, Order.class);
                intentPayOrder.putExtra("orderId", orderIds);
                mActivity.startActivity(intentPayOrder);
            } else {
//                    Intent intentPayOrder = new Intent(mActivity, OrderDetailActivity.class);
//                    intentPayOrder.putExtra("orderId", orderIds);
//                    mActivity.startActivity(intentPayOrder);
            }
            finish();
        }
    };

    @Override
    protected void onDestroy() {
        try {
            unregisterReceiver(mReciverSucceed);
            unregisterReceiver(mReciverFailed);
        } catch (Exception e) {

        }
        super.onDestroy();
    }
}
