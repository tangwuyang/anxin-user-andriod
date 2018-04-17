package com.anxin.kitchen.user.wxapi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.anxin.kitchen.user.R;
import com.anxin.kitchen.utils.Constant;
import com.anxin.kitchen.utils.JsonHandler;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
    public static final String WEIXIN_APP_ID = "wx634128d6db8c15cd";

    private final int HANDLER_FINISH_PAY = 2000;

    private IWXAPI api;
    private AlertDialog.Builder mDialog;
    private boolean isSucceed = false;//支付是否成功

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);

        api = WXAPIFactory.createWXAPI(this, WEIXIN_APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    //微信 支付信息回调
    @Override
    public void onReq(BaseReq baseReq) {
        System.out.println("---微信支付:" + JsonHandler.getHandler().toJson(baseReq));
    }

    @Override
    public void onResp(BaseResp baseResp) {
        System.out.println("---微信支付2:" + JsonHandler.getHandler().toJson(baseResp));
        if (baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            mDialog = new AlertDialog.Builder(this);
            mDialog.setTitle("提示");
            if (baseResp.errCode == 0) {
                mDialog.setMessage("支付成功");
                isSucceed = true;
            } else {
                mDialog.setMessage("支付失败");
                isSucceed = false;
            }
            mDialog.show();
            mHandler.sendEmptyMessageDelayed(HANDLER_FINISH_PAY, 2000);

        }

    }

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case HANDLER_FINISH_PAY:
                    finish();
                    Intent intent = new Intent();
                    if (isSucceed) {
                        intent.setAction(Constant.BROADCAST_PAY_SUCCEED);
                    } else {
                        intent.setAction(Constant.BROADCAST_PAY_FAILED);
                    }
                    sendBroadcast(intent);
                    break;
                default:
                    break;
            }
            return false;
        }
    });

}