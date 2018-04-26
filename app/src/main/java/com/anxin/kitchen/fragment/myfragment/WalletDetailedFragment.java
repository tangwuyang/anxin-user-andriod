package com.anxin.kitchen.fragment.myfragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.anxin.kitchen.MyApplication;
import com.anxin.kitchen.bean.AddressListBean;
import com.anxin.kitchen.bean.MoneyLogBean;
import com.anxin.kitchen.custom.view.slipview.PullToRefreshBase;
import com.anxin.kitchen.custom.view.slipview.PullToRefreshListView;
import com.anxin.kitchen.event.AsyncHttpRequestMessage;
import com.anxin.kitchen.fragment.HomeBaseFragment;
import com.anxin.kitchen.user.R;
import com.anxin.kitchen.utils.DateUtils;
import com.anxin.kitchen.utils.EventBusFactory;
import com.anxin.kitchen.utils.Log;
import com.anxin.kitchen.utils.StringUtils;
import com.anxin.kitchen.utils.SystemUtility;
import com.anxin.kitchen.utils.ToastUtil;
import com.anxin.kitchen.utils.UmengHelper;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.anxin.kitchen.MyApplication.mApp;

/**
 * 钱包明细界面
 */
public class WalletDetailedFragment extends HomeBaseFragment implements View.OnClickListener {
    private Log LOG = Log.getLog();
    private View view;
    private ImageView backBtn;//返回
    private int moneyPage = 1;
    private int maxPage = 0;
    private ListView moneyLoglistview;
    private List<MoneyLogBean> moneyLogBeanList = new ArrayList<>();
    private PullToRefreshListView recordsListView;
    private static final String sendGetMoneyLog_http = "sendGetMoneyLog";
    private mAdapter myAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBusFactory.getInstance().register(this);
        hideMainBottom();
        sendGetMoney(moneyPage);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.wallet_detailed_fragment, null);
        initView();//初始化界面控制
        return view;
    }

    private void initView() {
        backBtn = (ImageView) view.findViewById(R.id.back_btn);
        backBtn.setOnClickListener(this);
        recordsListView = (PullToRefreshListView) view.findViewById(R.id.PullToRefresh_lv);
        moneyLoglistview = recordsListView.getRefreshableView();
        myAdapter = new mAdapter();
        moneyLoglistview.setAdapter(myAdapter);
        recordsListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (recordsListView.getRefreshType() == 1) {
//                    LOG.e("--------getRefreshType------1---");
                    moneyPage = 1;
                    sendGetMoney(moneyPage);
                } else if (recordsListView.getRefreshType() == 2) {
//                    LOG.e("--------getRefreshType------2---");
                    if (moneyPage < maxPage) {
                        moneyPage++;
                        sendGetMoney(moneyPage);
                    } else {
                        recordsListView.onRefreshComplete();
                    }
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("WalletDetailedFragment");
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("WalletDetailedFragment");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                getFragmentManager().popBackStack();
                break;
            default:
                break;
        }
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
            case sendGetMoneyLog_http:
                if (requestStatus != null && requestStatus.equals(SystemUtility.RequestSuccess)) {
                    String code = StringUtils.parserMessage(responseMsg, "code");
                    String data = StringUtils.parserMessage(responseMsg, "data");
                    if (code != null && code.equals("1")) {
                        MoneyJason(data);
                    } else {
                        recordsListView.onRefreshComplete();
                    }
                }
                break;
        }
    }

    /**
     * 解析钱包明细信息
     */
    private void MoneyJason(String jason) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jason);
            JSONArray jsonArrayResult2 = jsonObject.getJSONArray("data");
            int AccountCount2 = jsonArrayResult2.length();
            if (AccountCount2 == 0) {
                recordsListView.onRefreshComplete();
                return;
            }
            String maxPage = jsonObject.getString("maxPage");
//            LOG.e("---------maxPage------" + maxPage);
            if (maxPage != null && maxPage.length() != 0) {
                this.maxPage = Integer.valueOf(maxPage);
            }
            String curPage = jsonObject.getString("curPage");
//            LOG.e("---------curPage------" + curPage);
            if (curPage != null && curPage.length() != 0) {
                if (curPage.equals("1") && this.maxPage != 0) {
                    moneyPage = 1;
                    moneyLogBeanList.clear();
                }
            }
            for (int j = 0; j < AccountCount2; j++) {
                String alarmMsg2 = jsonArrayResult2.getString(j);
                JSONObject jsonAlarm2 = new JSONObject(alarmMsg2);
                Iterator<?> it2 = jsonAlarm2.keys();
                String resultKey2 = "";
                String resultValue2 = null;
                MoneyLogBean moneyLogBean = new MoneyLogBean();
                while (it2.hasNext()) {
                    resultKey2 = (String) it2.next().toString();
                    resultValue2 = jsonAlarm2.getString(resultKey2).trim();
                    if (resultKey2 == null) {
                        resultKey2 = "";
                    }
                    if (resultValue2 == null) {
                        resultValue2 = "";
                    }
                    resultValue2 = resultValue2.trim();
                    if (resultKey2.equals("id")) {
                        moneyLogBean.setmID(resultValue2);
                    } else if (resultKey2.equals("moneyType")) {
                        moneyLogBean.setmMoneyType(resultValue2);
                    } else if (resultKey2.equals("changeType")) {
                        moneyLogBean.setmChangeType(resultValue2);
                    } else if (resultKey2.equals("userId")) {
                        moneyLogBean.setmUserID(resultValue2);
                    } else if (resultKey2.equals("balance")) {
                        moneyLogBean.setmBalance(resultValue2);
                    } else if (resultKey2.equals("money")) {
                        moneyLogBean.setmMoney(resultValue2);
                    } else if (resultKey2.equals("relationId")) {
                        moneyLogBean.setmRelationId(resultValue2);
                    } else if (resultKey2.equals("createTime")) {
                        moneyLogBean.setmCreateTime(resultValue2);
                    } else if (resultKey2.equals("name")) {
                        moneyLogBean.setmName(resultValue2);
                    } else if (resultKey2.equals("useType")) {
                        moneyLogBean.setmUserType(resultValue2);
                    } else if (resultKey2.equals("orderType")) {
                        moneyLogBean.setmOrderType(resultValue2);
                    } else if (resultKey2.equals("brand")) {
                        moneyLogBean.setmBrand(resultValue2);
                    } else if (resultKey2.equals("model")) {
                        moneyLogBean.setmModel(resultValue2);
                    }
                }
                moneyLogBeanList.add(moneyLogBean);
            }
            recordsListView.onRefreshComplete();
            myAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            MobclickAgent.reportError(MyApplication.getInstance(), e);
        }
    }

    class mAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return moneyLogBeanList.size();
        }

        @Override
        public Object getItem(int arg0) {
            return moneyLogBeanList.get(arg0);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View v, ViewGroup parent) {
            ViewHolder vh;
            if (v == null) {
                vh = new ViewHolder();
                v = LayoutInflater.from(getActivity()).inflate(R.layout.money_log_item, null);
                vh.mChangeType_tv = v.findViewById(R.id.mChangeType_tv);
                vh.mCreateTime_tv = v.findViewById(R.id.mCreateTime_tv);
                vh.money_tv = v.findViewById(R.id.money_tv);
                vh.moneyId_tv = v.findViewById(R.id.moneyId_tv);
                v.setTag(vh);
            } else {
                vh = (ViewHolder) v.getTag();
            }
            MoneyLogBean moneyLogBean = moneyLogBeanList.get(position);
            String money = moneyLogBean.getmMoney();
            String moneyId = moneyLogBean.getmID();
            String mChangeType = moneyLogBean.getmChangeType();
            String mCreateTime = moneyLogBean.getmCreateTime();
            vh.moneyId_tv.setText("ID:" + moneyId);
            vh.money_tv.setText(money);
            String type = "";
            switch (mChangeType) {
                case "1":
                    type = "充值";
                    break;
                case "2":
                    type = "提现";
                    break;
                case "3":
                    type = "AA订单支付";
                    break;
                case "4":
                    type = "点餐代付";
                    break;
                case "5":
                    type = "食疗订单支付";
                    break;
                case "6":
                    type = "资金转押金";
                    break;
                case "7":
                    type = "后台充值金额";
                    break;
                case "8":
                    type = "后台充值押金";
                    break;
                case "9":
                    type = "订单(多份)支付";
                    break;
                case "50":
                    type = "提现资金";
                    break;
                case "51":
                    type = "提现押金";
                    break;
                case "52":
                    type = "餐具赔偿";
                    break;
                case "53":
                    type = "订单退款";
                    break;
            }
            vh.mChangeType_tv.setText(type);
            String date = DateUtils.stampToDate(mCreateTime, "yyyy-MM-dd  HH:mm:ss");
            vh.mCreateTime_tv.setText(date);
            return v;
        }

        class ViewHolder {
            private TextView moneyId_tv, mCreateTime_tv, money_tv, mChangeType_tv;
        }
    }

    private void sendGetMoney(int page) {
        String urlPath = SystemUtility.sendGetMoneyLog(page + "");
        SystemUtility.requestNetGet(urlPath, sendGetMoneyLog_http);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusFactory.getInstance().unregister(this);
    }
}
