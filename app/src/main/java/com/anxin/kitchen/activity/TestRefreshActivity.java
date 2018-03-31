package com.anxin.kitchen.activity;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.anxin.kitchen.user.R;
import com.anxin.kitchen.view.RefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;

public class TestRefreshActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener,
        RefreshLayout.OnLoadListener {

    private RefreshLayout swipeLayout;
    private ListView listView;
    private MyAdapter adapter;
    private ArrayList<HashMap<String, String>> list;
    private View header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_refresh);

        initView();
        setData();
        setListener();
    }


    /**
     * 设置监听
     */
    private void setListener() {
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setOnLoadListener(this);
    }



    private void initView() {
        header = getLayoutInflater().inflate(R.layout.refresh_item, null);
        swipeLayout = (RefreshLayout) findViewById(R.id.swipe_container);
        swipeLayout.setColorSchemeResources(R.color.color_bg,R.color.shallow_text_color,R.color.white,R.color.color_gold);
    }


    /**
     * 添加数据
     */
    private void setData() {
        list = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < 10; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("itemImage", i+"默认");
            map.put("itemText", i+"默认");
            list.add(map);
        }
        listView = (ListView) findViewById(R.id.list);
        //listView.addHeaderView(header);
        adapter = new MyAdapter(this, list);
        listView.setAdapter(adapter);
    }
    @Override
    public void onRefresh() {
        swipeLayout.postDelayed(new Runnable() {

            @Override
            public void run() {
                // 更新数据  更新完后调用该方法结束刷新
                list.clear();
                for (int i = 0; i < 8; i++) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("itemImage", i+"刷新");
                    map.put("itemText", i+"刷新");
                    list.add(map);
                }
                adapter.notifyDataSetChanged();
                swipeLayout.setRefreshing(false);
            }
        }, 2000);

    }

    @Override
    public void onLoad() {
        swipeLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 更新数据  更新完后调用该方法结束刷新
                swipeLayout.setLoading(false);
                for (int i = 1; i < 10; i++) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("itemImage", i+"更多");
                    map.put("itemText", i+"更多");
                    list.add(map);
                }
                adapter.notifyDataSetChanged();
            }
        }, 2000);
    }


    public class MyAdapter extends BaseAdapter {
        public ArrayList<HashMap<String, String>> list;
        public Context context;
        public LayoutInflater layoutInflater;

        public MyAdapter(Context context, ArrayList<HashMap<String, String>> list) {
            this.context = context;
            this.list = list;
            layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;
            ViewHolder holder = null;
            if (convertView == null) {
                view = layoutInflater.inflate(R.layout.refresh_item, null);
                holder = new ViewHolder();
                holder.txt  =(TextView) view.findViewById(R.id.textView1);
                view.setTag(holder);
            } else {
                view = convertView;
                holder = (ViewHolder) view.getTag();
            }
            holder.txt.setText(list.get(position).get("itemText"));
            return view;
        }

        class ViewHolder {
            TextView txt;
        }

    }

}
