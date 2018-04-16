package com.anxin.kitchen.activity;

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
import com.anxin.kitchen.utils.StringUtils;
import com.anxin.kitchen.utils.SystemUtility;
import com.anxin.kitchen.view.WaitingDialog;
import com.lcodecore.ILabel;
import com.lcodecore.LabelLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchMealActivityActivity extends BaseActivity implements View.OnClickListener,RequestNetListener {

    private static final String HOT_FOOD = "HOT_FOOD";
    private EditText mSearchView;
    private TextView mSearchTv;
    private ImageView mNoContentsImg;
    private LinearLayout mSignLl;
    private LabelLayout mHotLabelLy;
    private WaitingDialog mDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_meal_activity);
        initView();
        setTitle("搜  索");
        showDialog();
        requestHotMeals();
    }

    private void showDialog(){
        if (null == mDialog){
            mDialog = new WaitingDialog(this);
            mDialog.show();
            mDialog.startAnimation();
        }else {
            mDialog.startAnimation();
            mDialog.show();
        }
    }

    //请求热搜商品
    private void requestHotMeals() {
        requestNet(SystemUtility.getHotFoodsUrl(),null,HOT_FOOD);
    }

    private void initView() {
        mSearchView = findViewById(R.id.search_et);
        mSearchTv = findViewById(R.id.search_tv);
        mNoContentsImg = findViewById(R.id.no_content_img);
        mHotLabelLy = findViewById(R.id.hot_layout);
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
                if (numSt==null || numSt.length()<1){
                    mNoContentsImg.setVisibility(View.GONE);
                    mHotLabelLy.setVisibility(View.VISIBLE);
                    return;
                }

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.search_tv:
                mHotLabelLy.setVisibility(View.GONE);
                mNoContentsImg.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void requestSuccess(String responseString, String requestCode) {
        super.requestSuccess(responseString, requestCode);
        closeDialog();
        String status = StringUtils.parserMessage(responseString, Constant.REQUEST_STATUS);
        if (requestCode==HOT_FOOD && status.equals(Constant.REQUEST_SUCCESS)){
            HotBean bean = mGson.fromJson(responseString,HotBean.class);
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
                    if (isChecked){
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
        if (null!=mDialog){
            mDialog.stopAnimation();
            mDialog.dismiss();
        }
    }

    @Override
    public void requestFailure(String responseFailure, String requestCode) {
        super.requestFailure(responseFailure, requestCode);
    }
}
