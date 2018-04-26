package com.anxin.kitchen.activity;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.anxin.kitchen.MyApplication;
import com.anxin.kitchen.adapter.MyBaseAdapter;
import com.anxin.kitchen.bean.AddressBean;
import com.anxin.kitchen.common.PlayerManager;
import com.anxin.kitchen.event.AddressListEvent;
import com.anxin.kitchen.event.AsyncHttpRequestMessage;
import com.anxin.kitchen.user.R;
import com.anxin.kitchen.utils.DateUtils;
import com.anxin.kitchen.utils.EventBusFactory;
import com.anxin.kitchen.utils.Log;
import com.anxin.kitchen.utils.StringUtils;
import com.anxin.kitchen.utils.SystemUtility;
import com.anxin.kitchen.utils.ToastUtil;
import com.anxin.kitchen.view.HorizontalScrollMenu;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ViewKitchenActivity extends BaseActivity implements View.OnClickListener, PlayerManager.PlayerStateListener {
    private Log LOG = Log.getLog();
    private HorizontalScrollMenu hsm_container;
    private ImageView mBackImg;
    public static final String GET_KITCHEN_LIST = "GET_KITCHEN_LIST";
    private PlayerManager player = null;
    private ImageView playBtn;
    private ProgressBar progressbar_iv;
    private TextView openTime_tv;//厨房监控打开时间
    private TextView endTime_tv;//厨房监控关闭时间
    private View playBack;
    private String playUrl = null;
    private List<KitChenBean> kitChenList = new ArrayList<>();
    private String[] names = new String[]{};
    private MenuAdapter menuAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBusFactory.getInstance().register(this);
        setContentView(R.layout.activity_view_kitchen);
        ((TextView) findViewById(R.id.title_tv)).setText("厨师视频");
        initView();
        getKitchenIdList();
    }

    public void initView() {
        player = new PlayerManager(ViewKitchenActivity.this);
        hsm_container = (HorizontalScrollMenu) findViewById(R.id.hsm_container);
        hsm_container.setSwiped(false);
        progressbar_iv = findViewById(R.id.progressbar_iv);
        playBack = findViewById(R.id.playBack);
        playBtn = findViewById(R.id.play_btn);
        playBtn.setOnClickListener(this);
        openTime_tv = findViewById(R.id.openTime_tv);
        endTime_tv = findViewById(R.id.endTime_tv);
        mBackImg = findViewById(R.id.back_img);
        menuAdapter = new MenuAdapter();
        hsm_container.setAdapter(menuAdapter);
        mBackImg.setOnClickListener(this);
    }

    private void getKitchenIdList() {
        String kitchenId = MyApplication.getInstance().getKitchenId();
        if (kitchenId != null && kitchenId.length() != 0)
            SystemUtility.requestNetGet(SystemUtility.getKitchenList(kitchenId), GET_KITCHEN_LIST);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
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
//        LOG.e("----------requestCode------" + requestCode);
//        LOG.e("----------responseMsg------" + responseMsg);
//        LOG.e("----------requestStatus------" + requestStatus);
        switch (requestCode) {
            //验证码发送
            case GET_KITCHEN_LIST:
                //网络请求返回成功
                if (requestStatus != null && requestStatus.equals(SystemUtility.RequestSuccess)) {
                    //解析验证码返回
                    kitchenListJason(responseMsg);
                }
                break;
        }
    }

    private void initPlayer() {
        player.setFullScreenOnly(false);
        player.setScaleType(PlayerManager.SCALETYPE_FILLPARENT);
        player.playInFullScreen(false);
        player.setPlayerStateListener(this);
        player.play(playUrl);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_img:
                this.finish();
                break;
            case R.id.play_btn:
//                if (player == null) {
//                    initPlayer();
//                }
                if (playUrl != null) {
                    initPlayer();
//                    player.play(playUrl);
                    playBtn.setVisibility(View.GONE);
                    progressbar_iv.setVisibility(View.VISIBLE);
                }
                break;
        }
    }


    @Override
    public void onComplete() {
//        LOG.e("----------------onComplete------------");
    }

    @Override
    public void onError() {
        playBtn.setVisibility(View.VISIBLE);
        progressbar_iv.setVisibility(View.GONE);
//        LOG.e("----------------onError------------");
    }

    @Override
    public void onLoading() {
//        LOG.e("----------------onLoading------------");
        progressbar_iv.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPlay() {
        progressbar_iv.setVisibility(View.GONE);
        playBack.setVisibility(View.GONE);
//        LOG.e("----------------onPlay------------");
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (player != null && player.isPlaying()) {
            playBtn.setVisibility(View.VISIBLE);
            player.stop();
        }
    }

    private void kitchenListJason(String jason) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jason);
            String data = jsonObject.getString("data");
            String data2 = new JSONObject(data).getString("data");
            JSONArray jsonArrayResult2 = new JSONArray(data2);
            String nameList = "";
            int AccountCount2 = jsonArrayResult2.length();
            for (int j = 0; j < AccountCount2; j++) {
                String alarmMsg2 = jsonArrayResult2.getString(j);
                JSONObject jsonAlarm2 = new JSONObject(alarmMsg2);
                Iterator<?> it2 = jsonAlarm2.keys();
                String resultKey2 = "";
                String resultValue2 = null;
                KitChenBean kitChenBean = new KitChenBean();
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
                    if (resultKey2.equals("status")) {
                        kitChenBean.setStatus(resultValue2);
                    } else if (resultKey2.equals("id")) {
                        kitChenBean.setId(resultValue2);
                    } else if (resultKey2.equals("openTime")) {
                        kitChenBean.setOpenTime(resultValue2);
                    } else if (resultKey2.equals("endTime")) {
                        kitChenBean.setEndTime(resultValue2);
                    } else if (resultKey2.equals("kitchenId")) {
                        kitChenBean.setKitchenID(resultValue2);
                    } else if (resultKey2.equals("link")) {
                        kitChenBean.setLink(resultValue2);
                    } else if (resultKey2.equals("monitorName")) {
                        nameList = nameList + resultValue2 + ",";
                        kitChenBean.setMonitorName(resultValue2);
                    } else if (resultKey2.equals("createTime")) {
                        kitChenBean.setCreateTime(resultValue2);
                    } else if (resultKey2.equals("type")) {
                        kitChenBean.setType(resultValue2);
                    }
                }
                kitChenList.add(kitChenBean);
            }
            names = nameList.split(",");
            menuAdapter.notifyDataSetChanged();
//            LOG.e("------nameList--------" + nameList);
//            LOG.e("------names--------" + names.toString());
        } catch (JSONException e) {
            MobclickAgent.reportError(MyApplication.getInstance(), e);
        }
    }

    class MenuAdapter extends MyBaseAdapter {

        @Override
        public List<String> getMenuItems() {
            // TODO Auto-generated method stub
            return Arrays.asList(names);
        }

        @Override
        public List<View> getContentViews() {
            // TODO Auto-generated method stub
            List<View> views = new ArrayList<View>();
            for (String str : names) {
                View v = LayoutInflater.from(ViewKitchenActivity.this).inflate(
                        R.layout.kitchen_content_view, null);
                TextView tv = (TextView) v.findViewById(R.id.tv_content);
                tv.setText(str);
                views.add(v);
            }
            return views;
        }

        @Override
        public void onPageChanged(int position, boolean visitStatus) {
            // TODO Auto-generated method stub
            KitChenBean kitChenBean = kitChenList.get(position);
            playUrl = kitChenBean.getLink();
            LOG.e("--------playUrl---------" + playUrl);
            playBtn.setVisibility(View.VISIBLE);
            playBack.setVisibility(View.VISIBLE);
            progressbar_iv.setVisibility(View.GONE);
            String opentime = DateUtils.stampToDate(kitChenBean.getOpenTime(), "HH:mm");
            String endtime = DateUtils.stampToDate(kitChenBean.getEndTime(), "HH:mm");
            openTime_tv.setText(opentime);
            endTime_tv.setText(endtime);
            if (player != null && player.isPlaying()) {
                player.stop();
            }
//            Toast.makeText(ViewKitchenActivity.this,
//                    "内容页：" + (position + 1) + " 访问状态：" + visitStatus,
//                    Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusFactory.getInstance().unregister(this);
    }

    /**
     * 厨房视频
     * status = 1,
     * id = 18,
     * openTime = 28825,
     * endTime = 28828,
     * kitchenId = 1,
     * link = "rtmp://gz2.play.95787.com/sx/gKPPh2ZDgdm0oQMzffzXemxqG",
     * type = 1,
     * monitorName = "测试监控点",
     * createTime = 1524153908,
     */
    class KitChenBean {

        private String status;
        private String id;
        private String openTime;
        private String endTime;
        private String kitchenID;
        private String link;
        private String type;
        private String monitorName;
        private String createTime;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getOpenTime() {
            return openTime;
        }

        public void setOpenTime(String openTime) {
            this.openTime = openTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getKitchenID() {
            return kitchenID;
        }

        public void setKitchenID(String kitchenID) {
            this.kitchenID = kitchenID;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getMonitorName() {
            return monitorName;
        }

        public void setMonitorName(String monitorName) {
            this.monitorName = monitorName;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }
    }
}

