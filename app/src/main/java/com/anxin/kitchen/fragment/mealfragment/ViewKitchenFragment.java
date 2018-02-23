package com.anxin.kitchen.fragment.mealfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anxin.kitchen.user.R;

/**
 * Created by 唐午阳 on 2018/2/21.
 * 可视厨房碎片
 */

public class ViewKitchenFragment extends Fragment {
    @Nullable
    @Override
    public View getView() {
        return super.getView();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.view_kitchen_fragment,container,false);

        return view;
    }
}
