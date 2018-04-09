package com.anxin.kitchen.view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.anxin.kitchen.activity.CreateGroupActivity;
import com.anxin.kitchen.activity.PreserveActivity;
import com.anxin.kitchen.activity.SetCountActivity;
import com.anxin.kitchen.bean.GroupBean;
import com.anxin.kitchen.bean.SearchGroupBean;
import com.anxin.kitchen.interface_.OnGivedPermissionListener;
import com.anxin.kitchen.user.R;
import com.anxin.kitchen.utils.PrefrenceUtil;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by 唐午阳 on 2018/3/1.
 */

public class ChoseGroupDialog extends Dialog{
    private Context mContext;
    private OnGivedPermissionListener customDialogListener;
    View.OnClickListener setCountListener;
    private PrefrenceUtil prefrenceUtil;
    private String mGroupList;
    private SearchGroupBean bean;
    public ChoseGroupDialog(Context context, View.OnClickListener setCountListener) {
        super(context);
        mContext = context;
        this.setCountListener = setCountListener;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chose_group_dialog_layout);
        prefrenceUtil = new PrefrenceUtil(mContext);
        mGroupList = prefrenceUtil.getGroups();
        ((PreserveActivity)mContext).myLog("---------------->"+mGroupList);
        Gson gson = new Gson();
        bean = gson.fromJson(mGroupList,SearchGroupBean.class);
        ImageView cancelImg = findViewById(R.id.cancel_img);
        TextView setcount = (TextView) findViewById(R.id.cancel_tv);
        TextView createNewGroup = (TextView) findViewById(R.id.open_right_tv);
        ListView groupLv = findViewById(R.id.group_lv);
        groupLv.setAdapter(new GroupAdapter());
        setcount.setOnClickListener(setCountListener);
        createNewGroup.setOnClickListener(clickListener3);
        cancelImg.setOnClickListener(clickListener);
    }
   private View.OnClickListener clickListener2 = new View.OnClickListener() {
       @Override
       public void onClick(View view) {
           Intent intent = new Intent(getContext(), SetCountActivity.class);
           getContext().startActivity(intent);
           ChoseGroupDialog.this.dismiss();
       }
   };
    private View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            ChoseGroupDialog.this.dismiss();
        }
    };


    private View.OnClickListener clickListener3 = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, CreateGroupActivity.class);
            mContext.startActivity(intent);
            ChoseGroupDialog.this.dismiss();
        }
    };

    class GroupAdapter extends BaseAdapter{
        List<SearchGroupBean.Data> groupList = bean.getData();
        @Override
        public int getCount() {
            return groupList.size();
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
            SearchGroupBean.Data group = groupList.get(i);
            view = LayoutInflater.from(mContext).inflate(R.layout.group_simple_item,viewGroup,false);
            TextView groupnameTv = view.findViewById(R.id.group_name_tv);
            TextView groupNumTv = view.findViewById(R.id.group_nums_tv);
            groupnameTv.setText(group.getGroupName());
            groupNumTv.setText(group.getGroupNum()+"人");
            return view;
        }
    }
}
