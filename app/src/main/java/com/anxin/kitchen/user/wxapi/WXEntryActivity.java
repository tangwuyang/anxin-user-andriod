package com.anxin.kitchen.user.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.anxin.kitchen.activity.LoginActivity;
import com.anxin.kitchen.utils.Log;
import com.anxin.kitchen.utils.SystemUtility;
import com.anxin.kitchen.utils.ToastUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.socialize.weixin.view.WXCallbackActivity;


import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * 微信登录页面
 *
 * @author kevin_chen 2016-12-10 下午19:03:45
 * @version v1.0
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private Log LOG = Log.getLog();
    private static final String APP_SECRET = "75bf85878994d867fd4bcec28ccca5aa";
    private IWXAPI mWeixinAPI;
    public static final String WEIXIN_APP_ID = "wx634128d6db8c15cd";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWeixinAPI = WXAPIFactory.createWXAPI(this, WEIXIN_APP_ID, true);
        mWeixinAPI.handleIntent(this.getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        mWeixinAPI.handleIntent(intent, this);//必须调用此句话
    }

    //微信发送的请求将回调到onReq方法
    @Override
    public void onReq(BaseReq req) {
//        LOG.e("-----onReq");
        switch (req.getType()) {
            case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
                LOG.e("-------------COMMAND_GETMESSAGE_FROM_WX");
                break;
            case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
                LOG.e("-------------COMMAND_SHOWMESSAGE_FROM_WX");
                break;
            default:
                break;
        }

    }

    //发送到微信请求的响应结果
    @Override
    public void onResp(BaseResp resp) {
//        LOG.e("-------------onResp");
//        LOG.e("-------------errCode"+resp.errCode);
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                LOG.e("-------ERR_OK");
                //发送成功
                String transaction = resp.transaction;
                if (transaction != null && transaction.equals("kitchen_share")) {
                    finish();
                    break;
                }
                SendAuth.Resp sendResp = (SendAuth.Resp) resp;
                if (sendResp != null) {
                    String state = sendResp.state;
                    String code = sendResp.code;
                    getAccess_token(code);
                }
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                finish();
                LOG.e("---------ERR_USER_CANCEL");
                //发送取消
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                LOG.e("------------ERR_AUTH_DENIED");
                //发送被拒绝
                break;
            default:
                //发送返回
                LOG.e("------------发送返回");
                break;
        }
//        finish();
    }

    /**
     * 获取openid accessToken值用于后期操作
     *
     * @param code 请求码
     */
    private void getAccess_token(final String code) {
        String path = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
                + WEIXIN_APP_ID
                + "&secret="
                + APP_SECRET
                + "&code="
                + code
                + "&grant_type=authorization_code";
//        LOG.e("----getAccess_token：" + path);
        //网络请求，根据自己的请求方式
        AsyncHttpClient client = new AsyncHttpClient();
        MySSLSocketFactory sf = SystemUtility.getMySSLSocketFactory();
        client.setURLEncodingEnabled(true);
        client.setSSLSocketFactory(sf);
        client.get(path, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = "";
                if (responseBody != null)
                    result = new String(responseBody);
//                LOG.e("-------getAccess_token_result:" + result);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    String openid = jsonObject.getString("openid").toString().trim();
                    String access_token = jsonObject.getString("access_token").toString().trim();
                    getUserMesg(access_token, openid);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable
                    error) {

            }
        });
    }


    /**
     * 获取微信的个人信息
     *
     * @param access_token
     * @param openid
     */
    private void getUserMesg(final String access_token, final String openid) {
        String path = "https://api.weixin.qq.com/sns/userinfo?access_token="
                + access_token
                + "&openid="
                + openid;
//        LOG.e("----------getUserMesg：" + path);
        //网络请求，根据自己的请求方式
        AsyncHttpClient client = new AsyncHttpClient();
        MySSLSocketFactory sf = SystemUtility.getMySSLSocketFactory();
        client.setURLEncodingEnabled(true);
        client.setSSLSocketFactory(sf);
        client.get(path, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = "";
                if (responseBody != null)
                    result = new String(responseBody);
//                LOG.e("--------getUserMesg_result用户基本信息:" + result);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    String nickname = jsonObject.getString("nickname");
                    String headimgurl = jsonObject.getString("headimgurl");
                    String wxID = jsonObject.getString("openid");
//
//                    LOG.e("用户基本信息:");
//                    LOG.e("nickname:" + nickname);
//                    LOG.e("headimgurl:" + headimgurl);
                    LoginActivity.openID = wxID;
                    LoginActivity.userNickName = nickname;
                    LoginActivity.userLogoPath = headimgurl;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                finish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable
                    error) {

            }
        });
    }
}
