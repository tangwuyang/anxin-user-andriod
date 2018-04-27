package com.anxin.kitchen.activity;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anxin.kitchen.bean.HotBean;
import com.anxin.kitchen.bean.LabelBean;
import com.anxin.kitchen.interface_.RequestNetListener;
import com.anxin.kitchen.user.R;
import com.anxin.kitchen.utils.Constant;
import com.anxin.kitchen.utils.PrefrenceUtil;
import com.anxin.kitchen.utils.StringUtils;
import com.anxin.kitchen.utils.SystemUtility;
import com.anxin.kitchen.view.WaitingDialog;
import com.google.gson.reflect.TypeToken;
import com.lcodecore.ILabel;
import com.lcodecore.LabelLayout;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchMealActivityActivity extends BaseActivity implements View.OnClickListener, RequestNetListener {

    private static final String HOT_FOOD = "HOT_FOOD";
    private EditText mSearchView;
    private TextView mSearchTv;
    private ImageView mNoContentsImg;
    private LinearLayout mSignLl;
    private LabelLayout mHotLabelLy;
    private WaitingDialog mDialog;
    private PrefrenceUtil prefrenceUtil;
    private List<ILabel> historySearch;
    private LabelLayout mHistLabelLv;
    private ImageView mBackImg;
    private LinearLayout mLableLl;
    private EditText mSearch_et;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_meal_activity);
        initView();
        setTitle("搜  索");
        showDialog();
        getHistory();
        requestHotMeals();
    }

    private void getHistory() {
        String histList =  prefrenceUtil.getHisSearchList();
        if (null!=histList && !"null".equals(histList)){
            historySearch = mGson.fromJson(histList,new TypeToken<List<LabelBean>>(){}.getType());
            myLog("------------------>"+historySearch.size());

            mHistLabelLv.setLabels(historySearch);
            mHistLabelLv.setMaxCheckCount(1);
            mHistLabelLv.setOnCheckChangedListener(new LabelLayout.OnCheckChangeListener() {
                @Override
                public void onCheckChanged(ILabel label, boolean isChecked) {
                    if (isChecked) {
                        mHotLabelLy.setVisibility(View.GONE);
                        mNoContentsImg.setVisibility(View.VISIBLE);
                        mHotLabelLy.setSelected(false);

                    }
                }

                @Override
                public void onBeyondMaxCheckCount() {

                }
            });
        }
    }

    private void showDialog() {
        if (null == mDialog) {
            mDialog = new WaitingDialog(this, 100);
            mDialog.show();
            mDialog.startAnimation();
        } else {
            mDialog.startAnimation();
            mDialog.show();
        }
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

    //请求热搜商品
    private void requestHotMeals() {
        requestNet(SystemUtility.getHotFoodsUrl(), null, HOT_FOOD);
    }

    private void initView() {
        mSearch_et = findViewById(R.id.search_et);
        mLableLl = findViewById(R.id.tag_ll);
        mBackImg = findViewById(R.id.back_img);
        mBackImg.setOnClickListener(this);
        prefrenceUtil = new PrefrenceUtil(this);
        mSearchView = findViewById(R.id.search_et);
        mSearchTv = findViewById(R.id.search_tv);
        mNoContentsImg = findViewById(R.id.no_content_img);
        mHotLabelLy = findViewById(R.id.hot_layout);
        mHistLabelLv = findViewById(R.id.history_layout);
        mSearchTv.setOnClickListener(this);
        mSearchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String numSt = mSearchView.getText().toString();
                if (numSt == null || numSt.length() < 1) {
                    mNoContentsImg.setVisibility(View.GONE);
                    mHotLabelLy.setVisibility(View.VISIBLE);
                    mLableLl.setVisibility(View.VISIBLE);
                    return;
                }

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myLog("--------------->"+mGson.toJson(historySearch));
        prefrenceUtil.setHisSearchList(mGson.toJson(historySearch));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_tv:
                boolean hasItInHis = false;
                String searchSt = mSearch_et.getText().toString();
                if (null != historySearch && historySearch.size()>0){
                    for (ILabel lable1:historySearch){
                        if (null != lable1 && null != lable1.getName() ) {
                            if (lable1.getName().equals(searchSt)) {
                                hasItInHis = true;
                                historySearch.set(0, lable1);
                                break;
                            }
                        }
                    }
                    if (!hasItInHis){
                        LabelBean bean = new LabelBean();
                        bean.setName(searchSt);
                        bean.setId("sdas");
                        myLog("--------------");
                        historySearch.add(bean);
                    }
                }else {
                    historySearch = new ArrayList<>();
                    LabelBean bean = new LabelBean();
                    bean.setName(searchSt);
                    bean.setId("sdas");

                    historySearch.add(bean);
                }

                mLableLl.setVisibility(View.GONE);
                mHotLabelLy.setVisibility(View.GONE);
                mNoContentsImg.setVisibility(View.VISIBLE);
                break;
            case R.id.back_img:
                finish();
                break;
        }
    }

    @Override
    public void requestSuccess(String responseString, String requestCode) {
        super.requestSuccess(responseString, requestCode);
        closeDialog();
        String status = StringUtils.parserMessage(responseString, Constant.REQUEST_STATUS);
        if (requestCode == HOT_FOOD && status.equals(Constant.REQUEST_SUCCESS)) {
            HotBean bean = mGson.fromJson(responseString, HotBean.class);
            List<ILabel> lableList = new ArrayList<>();
            for (HotBean.Data data :
                    bean.getData()) {
                LabelBean labelBean = new LabelBean();
                labelBean.setId(String.valueOf(data.getId()));
                labelBean.setName(data.getKeywords());
                lableList.add(labelBean);
            }
            mHotLabelLy.setLabels(lableList);
            mHotLabelLy.setMaxCheckCount(1);
            mHotLabelLy.setOnCheckChangedListener(new LabelLayout.OnCheckChangeListener() {
                @Override
                public void onCheckChanged(ILabel label, boolean isChecked) {
                    boolean hasItInHis = false;
                    if (isChecked) {
                        for (ILabel lable1:historySearch){
                            if (lable1.getName().equals(label.getName())){
                                hasItInHis = true;
                                historySearch.set(0,lable1);
                                break;
                            }
                        }
                        if (!hasItInHis){
                            historySearch.add(label);
                        }
                        mLableLl.setVisibility(View.GONE);
                        mHotLabelLy.setVisibility(View.GONE);
                        mNoContentsImg.setVisibility(View.VISIBLE);
                        mHotLabelLy.setSelected(false);

                    }
                }

                @Override
                public void onBeyondMaxCheckCount() {

                }
            });
        }
    }

    private void closeDialog() {
        if (null != mDialog) {
            mDialog.stopAnimation();
            mDialog.dismiss();
        }
    }

    @Override
    public void requestFailure(String responseFailure, String requestCode) {
        super.requestFailure(responseFailure, requestCode);
    }
}
