package com.anxin.kitchen.utils;

import android.view.Gravity;
import android.widget.Toast;

import com.anxin.kitchen.MyApplication;

public class ToastUtil {

    public static final String TAG = ToastUtil.class.getSimpleName();
    public static Toast toast;


    public static void showToast(String text) {
        showToast(text, Toast.LENGTH_SHORT);
    }

    public static void showToastCenter(String text) {
        showToastCenter(text, Toast.LENGTH_SHORT);
    }

    public static void showToast(int id) {
        showToast(id, Toast.LENGTH_SHORT);
    }

    public synchronized static void showToast(String text, int duration) {
        try {
            if (toast != null) {
                toast.setText(text);
                toast.setDuration(duration);
            } else {
                toast = Toast.makeText(MyApplication.getInstance(), text, duration);
            }
            toast.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized static void showToastCenter(String text, int duration) {
        try {
            if (toast != null) {
                toast.setText(text);
                toast.setDuration(duration);
            } else {
                toast = Toast.makeText(MyApplication.getInstance(), text, duration);
            }
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized static void showToast(int id, int duration) {
        try {
            if (toast != null) {
                toast.setText(id);
                toast.setDuration(duration);
            } else {
                toast = Toast.makeText(MyApplication.getInstance(), id, duration);
            }
            toast.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
