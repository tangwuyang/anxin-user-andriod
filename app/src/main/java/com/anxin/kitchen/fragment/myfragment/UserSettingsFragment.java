package com.anxin.kitchen.fragment.myfragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.anxin.kitchen.activity.ClipHeaderActivity;
import com.anxin.kitchen.custom.view.SelectPicPopupWindow;
import com.anxin.kitchen.fragment.HomeBaseFragment;
import com.anxin.kitchen.user.R;
import com.anxin.kitchen.utils.Log;
import com.anxin.kitchen.utils.SystemUtility;
import com.anxin.kitchen.view.RoundedImageView;

import java.io.File;

/**
 * 设置界面
 */
public class UserSettingsFragment extends HomeBaseFragment implements View.OnClickListener {
    private Log LOG = Log.getLog();
    private View view;
    private ImageView backBtn;//返回
    private RelativeLayout userIconBtn;//用户头像按钮
    private RelativeLayout userNameBtn;//用户姓名按钮
    private RelativeLayout userGenderBtn;//用户性别按钮
    private RelativeLayout userBirthdayBtn;//用户生日按钮
    private RoundedImageView userIcon;//用户头像
    private TextView userName;//用户名称
    private TextView userPhone;//用户电话
    private SelectPicPopupWindow menuWindowSelectPic;
    private static final int RESULT_CAPTURE = 122;
    private static final int RESULT_PICK = 133;
    private static final int CROP_PHOTO = 111;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideMainBottom();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.user_setting_fragment, null);
        initView();//初始化界面控制
        return view;
    }

    private void initView() {
        backBtn = (ImageView) view.findViewById(R.id.back_btn);
        backBtn.setOnClickListener(this);

        userIconBtn = (RelativeLayout) view.findViewById(R.id.user_icon_rlt);
        userNameBtn = (RelativeLayout) view.findViewById(R.id.user_name_rlt);
        userGenderBtn = (RelativeLayout) view.findViewById(R.id.user_gender_rlt);
        userBirthdayBtn = (RelativeLayout) view.findViewById(R.id.user_birthday_rlt);
        userBirthdayBtn.setOnClickListener(this);
        userGenderBtn.setOnClickListener(this);
        userNameBtn.setOnClickListener(this);
        userIconBtn.setOnClickListener(this);

        userIcon = (RoundedImageView) view.findViewById(R.id.user_icon);
        userName = (TextView) view.findViewById(R.id.user_name);
        userPhone = (TextView) view.findViewById(R.id.user_phone);
        //获取本地用户名称
        String name = mApp.getCache().getNickName();
        if (name != null && name.length() != 0) {
            userName.setText(name);
        }
        //获取本地缓存头像
        String mImageURI = mApp.getCache().getAccountImageURI(mApp.getCache().getUserPhone());
        if (mImageURI != null && !mImageURI.isEmpty()) {
            Uri mSaveUri = Uri.fromFile(new File(mImageURI));
            userIcon.setImageURI(mSaveUri);
        }
        //获取本地用户号码
        String phone = mApp.getCache().getUserPhone();
        if (phone != null && phone.length() != 0) {
            userPhone.setText(phone);
        }
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                getFragmentManager().popBackStack();
                break;
            case R.id.user_icon_rlt://修改用户头像
                showChooseDialog();
                break;
            case R.id.user_name_rlt://修改用户名
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                UserNameSetFragment userNameSetFragment = new UserNameSetFragment();
                ft.replace(R.id.content_frame, userNameSetFragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(null);
                ft.commit();
                break;
            case R.id.user_gender_rlt://修改用户性别
                break;
            case R.id.user_birthday_rlt://修改用户生日
                break;
            default:
                break;
        }
    }

    private void showChooseDialog() {
        menuWindowSelectPic = new SelectPicPopupWindow(getActivity(), itemsOnClick);
        menuWindowSelectPic.showAtLocation(getActivity().findViewById(R.id.RelativeLayout01),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    private View.OnClickListener itemsOnClick = new View.OnClickListener() {

        public void onClick(View v) {
            menuWindowSelectPic.dismiss();
            switch (v.getId()) {
                case R.id.btn_take_photo://打开相机
//                    String state = Environment.getExternalStorageState();
//                    if (state.equals(Environment.MEDIA_MOUNTED)) {
//                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//启动系统相机
//                        Uri photoUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory().getPath(), SystemUtility.PHOTO_FILE_NAME)); // 传递路径
//                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
//                        getActivity().startActivityForResult(intent, RESULT_CAPTURE);
//                    } else {
//                    }
                    Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 启动系统相机
//                    intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(intent1, RESULT_CAPTURE);
                    break;
                case R.id.btn_pick_photo://打开系统相册
                    Intent intent = new Intent(Intent.ACTION_PICK, null);
                    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    getActivity().startActivityForResult(intent, RESULT_PICK);
                    break;
                default:
                    break;
            }

        }

    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        LOG.e("------------requestCode--------" + requestCode);
        switch (requestCode) {
            case RESULT_CAPTURE:
                if (resultCode == getActivity().RESULT_OK) {
//                    File temp = new File(Environment.getExternalStorageDirectory() + "/" + SystemUtility.PHOTO_FILE_NAME);
                    Bundle bundle = data.getExtras(); // 从data中取出传递回来缩略图的信息，图片质量差，适合传递小图片
                    Bitmap bitmap = (Bitmap) bundle.get("data"); // 将data中的信息流解析为Bitmap类型
//                    userIcon.setImageBitmap(bitmap);// 显示图片
                    LOG.e("------------RESULT_CAPTURE--------");
                    Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bitmap, null, null));
                    starCropPhoto(uri);
                }

                break;
            case RESULT_PICK:
                if (resultCode == getActivity().RESULT_OK) {
                    LOG.e("------------RESULT_PICK--------");
                    starCropPhoto(data.getData());
                }
                break;
            case CROP_PHOTO:
                if (resultCode == getActivity().RESULT_OK) {
                    if (data != null) {
                        setPicToView(data);
                    }
                }
                break;
            default:
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //设置头像
    private void setPicToView(Intent picdata) {
        Uri uri = picdata.getData();
        if (uri == null) {
            LOG.e("------------setPicToView-----return---");
            return;
        }
        userIcon.setImageURI(uri);
//        try {
//            SystemUtility.setHeadIcon(uri);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        EventBusFactory.postEvent(new ViewUpdateHeadIconEvent());
    }

    //修改头像
    public void starCropPhoto(Uri uri) {
        if (uri == null) {
            LOG.e("------------starCropPhoto-----return---");
            return;
        }
        Intent intent = new Intent();
        intent.setClass(getActivity(), ClipHeaderActivity.class);
        intent.setData(uri);
        intent.putExtra("side_length", 400);// 裁剪图片宽高
        getActivity().startActivityForResult(intent, CROP_PHOTO);
    }
}
