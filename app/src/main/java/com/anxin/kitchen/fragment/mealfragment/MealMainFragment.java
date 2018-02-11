package com.anxin.kitchen.fragment.mealfragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.anxin.kitchen.activity.MessageCenterActivity;
import com.anxin.kitchen.activity.RecoveryMealActivity;
import com.anxin.kitchen.fragment.HomeBaseFragment;
import com.anxin.kitchen.user.R;
import com.anxin.kitchen.utils.Log;
import com.anxin.kitchen.view.CustomGridView;

import static android.content.ContentValues.TAG;

/**
 * 点餐主界面
 */
public class MealMainFragment extends HomeBaseFragment implements View.OnClickListener {
    private Log LOG = Log.getLog();
    private View view;
    private RecyclerView mPreserverRv;
    private LinearLayoutManager mLiearManager;
    private ImageView mMessageImg;  //消息中心
    private ImageView mRecoveryMealImg;   //康复食疗
    private ImageView mPreserverMealImg;  //预约点餐
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.meal_main_fragment, null);
        mPreserverRv = view.findViewById(R.id.preserver_rv);

        mMessageImg = view.findViewById(R.id.message_img);
        mRecoveryMealImg = view.findViewById(R.id.recovery_meal_img);
        mPreserverMealImg = view.findViewById(R.id.preserver_meal_img);
        return view;
    }

    private void initView() {
        mPreserverRv = getView().findViewById(R.id.preserver_rv);
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        setListener();
        setAdapter();
    }

    private void setListener() {
        mMessageImg.setOnClickListener(this);
        mPreserverMealImg.setOnClickListener(this);
        mRecoveryMealImg.setOnClickListener(this);
    }

    //设置点餐适配器
    private void setAdapter() {
        mLiearManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        PreserverAdapter adapter = new PreserverAdapter();
        mPreserverRv.setLayoutManager(mLiearManager);
        mPreserverRv.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.message_img:
                startNewActivity(MessageCenterActivity.class);
                break;
            case R.id.preserver_meal_img:
                break;
            case R.id.recovery_meal_img:
                startNewActivity(RecoveryMealActivity.class);
                break;
            default:
                break;
        }
    }

    private void startNewActivity(Class classType) {
        Intent intent = new Intent(getContext(), classType);
        startActivity(intent);
    }


    private class PreserverAdapter extends RecyclerView.Adapter<PreserverAdapter.ViewHolderw>{


        @Override
        public ViewHolderw onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.preserver_item,parent,false);
            ViewHolderw holder = new ViewHolderw(view);
            holder.mPreserverDayRv = view.findViewById(R.id.preserver_rv);
            return holder;

        }

        @Override
        public void onBindViewHolder(ViewHolderw holder, int position) {
            GridLayoutManager layoutManage = new GridLayoutManager(getContext(), 2);
           // holder.mPreserverDayRv.setLayoutManager(layoutManage);
            holder.mPreserverDayRv.setAdapter(new PreserverGridAdapter());
        }

        @Override
        public int getItemCount() {
            return 2;
        }

        public class ViewHolderw extends RecyclerView.ViewHolder {
            private TextView dateTv;
            private CustomGridView mPreserverDayRv;
            public ViewHolderw(View itemView) {
                super(itemView);
            }
        }
    }

    private class PreserverFoodAdapter extends RecyclerView.Adapter<PreserverFoodAdapter.ViewHolder>{

        @Override
        public PreserverFoodAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.preserver_food_item,parent,false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(PreserverFoodAdapter.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 4;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public ViewHolder(View itemView) {
                super(itemView);
            }
        }
    }

    private class PreserverGridAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return 4;
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
            view = LayoutInflater.from(getContext()).inflate(R.layout.preserver_food_item,viewGroup,false);
            return view;
        }
    }

}
