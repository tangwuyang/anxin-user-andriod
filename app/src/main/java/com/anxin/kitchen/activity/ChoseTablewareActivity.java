package com.anxin.kitchen.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.anxin.kitchen.bean.TablewareBean;
import com.anxin.kitchen.interface_.RequestNetListener;
import com.anxin.kitchen.user.R;
import com.anxin.kitchen.utils.Constant;
import com.anxin.kitchen.utils.StringUtils;
import com.anxin.kitchen.utils.SystemUtility;
import com.anxin.kitchen.view.MyListView;

import java.util.HashMap;
import java.util.Map;

public class ChoseTablewareActivity extends BaseActivity implements View.OnClickListener ,RequestNetListener{
    private static final String GET_TABLEWARE = "GET_TABLEWARE";
    private TextView viewKitchen;
    private TextView mUnifyPayTv;
    private TextView mEachPayTv;
    private ListView mTablewareLv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chose_tableware);
        initView();
        getTableWare();
        getSendCost();
    }

    //获取配送费
    private void getSendCost() {

    }

    //获取所有餐具
    private void getTableWare() {
        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("page",-1);
        requestNet(SystemUtility.getTablewareListUrl(),dataMap,GET_TABLEWARE);
    }

    private void initView() {
        setTitle("预约点餐");
        findViewById(R.id.view_kitchen).setVisibility(View.VISIBLE);
        ImageView back_img = (ImageView) findViewById(R.id.back_img);
        mUnifyPayTv = findViewById(R.id.unify_pay_tv);
        mEachPayTv = findViewById(R.id.each_pay_tv);
        mTablewareLv = findViewById(R.id.chose_tableware_lv);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        viewKitchen = findViewById(R.id.view_kitchen);
        viewKitchen.setOnClickListener(this);
        mUnifyPayTv.setOnClickListener(this);
        mEachPayTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.view_kitchen:
                startNewActivity(ViewKitchenActivity.class);
                break;
            case R.id.unify_pay_tv:
                break;
            case R.id.each_pay_tv:
                break;
            default:
                break;
        }
    }

    @Override
    public void requestSuccess(String responseString, String requestCode) {
        super.requestSuccess(responseString, requestCode);
        String status = StringUtils.parserMessage(responseString, Constant.REQUEST_STATUS);
        if (requestCode==GET_TABLEWARE && status.equals(Constant.REQUEST_SUCCESS)){
            String tempSt = responseString.substring(responseString.indexOf("\"data\":"),responseString.lastIndexOf("}"));
            String tablewareSt = tempSt.substring(tempSt.indexOf(":",0)+1);
            TablewareBean bean = mGson.fromJson(tablewareSt,TablewareBean.class);
            mTablewareLv.setAdapter(new TablewareAdapter(bean));
        }
    }

    @Override
    public void requestFailure(String responseFailure, String requestCode) {
        super.requestFailure(responseFailure, requestCode);

    }

    class TablewareAdapter extends BaseAdapter{
        private TablewareBean tablewareBean = null;

        public TablewareAdapter(TablewareBean tablewareBean) {
            this.tablewareBean = tablewareBean;
        }

        @Override
        public int getCount() {
            return tablewareBean.getData().size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            TablewareBean.Data tableware = tablewareBean.getData().get(i);
            view = LayoutInflater.from(ChoseTablewareActivity.this).inflate(R.layout.chose_tablemare_item,viewGroup,false);
            TextView tablewareNameTv = view.findViewById(R.id.tableware_title_tv);
            TextView tablewareGmTv = view.findViewById(R.id.tablew_g_m_tv);
            TextView tablewareNumsTv = view.findViewById(R.id.tableware_nums_tv);
            TextView tableMoney = view.findViewById(R.id.tableware_money_tv);
            tablewareNameTv.setText(tableware.getName());
            tablewareGmTv.setText("(押金"+tableware.getDeposit()+"元/份)");
            tableMoney.setText("￥"+tableware.getUsePrice());
            return view;
        }
    }
}
