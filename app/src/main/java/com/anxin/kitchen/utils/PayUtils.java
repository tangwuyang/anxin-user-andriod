package com.anxin.kitchen.utils;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import com.alipay.sdk.app.PayTask;
import com.anxin.kitchen.bean.Order.PayWeixinInfo;
import com.anxin.kitchen.user.wxapi.WXEntryActivity;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.Map;

/**
 * 支付工具类
 * Created by xujianjun on 2018/4/9.
 */
public class PayUtils{
    private static PayUtils mInstance;
    private IWXAPI mApi;//微信登陆API

    private String token;

    public static PayUtils getInstance() {
        if (mInstance == null) {
            mInstance = new PayUtils();
        }
        return mInstance;
    }

    /**
     * 微信支付统一接口
     *
     * @param mAcitivty
     */
    public void payWeixin(Activity mAcitivty, PayWeixinInfo info) {
        //商户服务器生成支付订单，先调用统一下单API(详见第7节)生成预付单，
        // 获取到prepay_id后将参数再次签名传输给APP发起支付。以下是调起微信支付的关键代码：
        mApi = WXAPIFactory.createWXAPI(mAcitivty, WXEntryActivity.WEIXIN_APP_ID, true);
        mApi.registerApp(WXEntryActivity.WEIXIN_APP_ID);

        PayReq request = new PayReq();
        request.appId = WXEntryActivity.WEIXIN_APP_ID;//应用ID
        request.partnerId = info.getPartnerId();//商户号
        request.prepayId = info.getPrepayid();//预支付交易会话ID
        request.packageValue = "Sign=WXPay";//扩展字段
        request.nonceStr = info.getNonceStr();//随机字符串
        request.timeStamp = info.getTimeStamp();//时间戳
        request.sign = info.getPaySign();//签名
        mApi.sendReq(request);

        //Activity 需要实现该回调 implements IWXAPIEventHandler
        //    public void onResp(BaseRespresp){
        //        if(resp.getType()== ConstantsAPI.COMMAND_PAY_BY_WX){
        //            Log.d(TAG,"onPayFinish,errCode="+resp.errCode);
        //            AlertDialog.Builderbuilder=newAlertDialog.Builder(this);
        //            builder.setTitle(R.string.app_tip);
        //        }
        //    }
        //        0	    成功	展示成功页面
        //        -1	错误	可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等。
        //        -2	用户取消	无需处理。发生场景：用户不支付了，点击取消，返回APP。
    }


    /**
     * 支付宝支付
     *
     * @param activity
     * @param info     订单信息 主要包含商户的订单信息，key=value形式，以&连接。
     * @param mHandler
     */
    public void payZhifubao(final Activity activity, final String info, final Handler mHandler) {

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(activity);
                Map<String, String> result = alipay.payV2(info, true);

                Message msg = new Message();
                msg.what = GlobalVariable.PAY_ZHIFUBAO;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

}
