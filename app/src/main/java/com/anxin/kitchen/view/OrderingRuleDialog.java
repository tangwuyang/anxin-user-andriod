package com.anxin.kitchen.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.anxin.kitchen.interface_.OnGivedPermissionListener;
import com.anxin.kitchen.user.R;

/**
 * Created by 唐午阳 on 2018/3/1.
 */

public class OrderingRuleDialog extends Dialog{
    public OrderingRuleDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ordering_rule_layout);
        TextView cancelBt = (TextView) findViewById(R.id.cancel_tv);
        TextView openPermissionTv = (TextView) findViewById(R.id.open_right_tv);
        cancelBt.setOnClickListener(clickListener);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            OrderingRuleDialog.this.dismiss();
        }
    };

}
