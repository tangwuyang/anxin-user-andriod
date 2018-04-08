package com.anxin.kitchen.fragment.loginfragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.anxin.kitchen.MyApplication;
import com.anxin.kitchen.activity.AddNewLocationActivity;
import com.anxin.kitchen.activity.ClipHeaderActivity;
import com.anxin.kitchen.activity.LocationActivity;
import com.anxin.kitchen.activity.UserNameActivity;
import com.anxin.kitchen.bean.AddressBean;
import com.anxin.kitchen.custom.view.SelectPicPopupWindow;
import com.anxin.kitchen.event.ViewUpdateHeadIconEvent;
import com.anxin.kitchen.user.R;
import com.anxin.kitchen.utils.Log;
import com.anxin.kitchen.utils.MyService;
import com.anxin.kitchen.utils.SystemUtility;
import com.anxin.kitchen.utils.ToastUtil;
import com.anxin.kitchen.view.RoundedImageView;

import java.io.File;

/**
 * 完善用户资料界面
 */
public class AddUserDataFragment extends Fragment implements View.OnClickListener {
    private Log LOG = Log.getLog();
    private View view;
    private RelativeLayout userIconBtn;//用户头像按钮
    private RelativeLayout userNameBtn;//用户名称按钮
    private RelativeLayout userAddressBtn;//用户地址
    private RoundedImageView userIcon;//用户头像
    private TextView userName;//用户名称
    private EditText userAddressEdit;//用户详细地址
    private TextView skipBtn;//跳过，下一步按钮
    private Button completeBtn;//完成按钮
    private SelectPicPopupWindow menuWindowSelectPic;
    private MyApplication mApp = null;
    private String username;
    private static final int CODE_GALLERY_REQUEST = 0xa0;

    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CAMERA_PERMISSIONS_REQUEST_CODE = 0x03;
    private static final int STORAGE_PERMISSIONS_REQUEST_CODE = 0x04;
    private static final int OUTPUT_X = 80;
    private static final int OUTPUT_Y = 80;
    private static final int LOCATION_NAME = 103;
    private static final int CROP_PHOTO = 111;
    private static final int USER_NAME = 112;
    private File fileUri;
    private File fileCropUri;
    private Uri imageUri;
    private Uri cropImageUri;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mApp == null)
            mApp = MyApplication.getInstance();
        fileUri = new File(getActivity().getExternalCacheDir().getPath() + "anxin/photo.jpg");
        fileCropUri = new File(getActivity().getExternalCacheDir().getPath() + "anxin/crop_photo.jpg");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.add_user_data_fragment, null);
        initView();//初始化界面控制
        return view;
    }

    private void initView() {
        userIconBtn = (RelativeLayout) view.findViewById(R.id.user_icon_rlt);//用户头像栏
        userNameBtn = (RelativeLayout) view.findViewById(R.id.user_name_rlt);//用户名称栏
        userAddressBtn = (RelativeLayout) view.findViewById(R.id.user_address_rlt);//用户地址
        userAddressEdit = (EditText) view.findViewById(R.id.user_address_edit);
        userAddressBtn.setOnClickListener(this);
        userNameBtn.setOnClickListener(this);
        userIconBtn.setOnClickListener(this);

        completeBtn = (Button) view.findViewById(R.id.completeBtn);//完成按钮
        completeBtn.setOnClickListener(this);
        skipBtn = (TextView) view.findViewById(R.id.skip_btn);//跳过按钮
        skipBtn.setOnClickListener(this);
        userIcon = (RoundedImageView) view.findViewById(R.id.user_icon);//用户头像
        userName = (TextView) view.findViewById(R.id.user_name);//用户名称
        updateUserAcount();
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.skip_btn:
                getActivity().finish();
                break;
            case R.id.completeBtn:
                getActivity().finish();
                break;
            case R.id.user_icon_rlt://修改用户头像
                showChooseDialog();
                break;
            case R.id.user_name_rlt://修改用户名
                Intent intent = new Intent();
                intent.setClass(getActivity(), UserNameActivity.class);
                intent.putExtra("userName", username);
                getActivity().startActivityForResult(intent, USER_NAME);
                break;
            case R.id.user_address_rlt:
                Intent intent2 = new Intent();
                intent2.setClass(getActivity(), LocationActivity.class);
                startActivityForResult(intent2, LOCATION_NAME);
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
                    autoObtainCameraPermission();
//                    imageUri = Uri.fromFile(fileUri);
//                    takePicture(imageUri, CODE_CAMERA_REQUEST);
                    break;
                case R.id.btn_pick_photo://打开系统相册
                    autoObtainStoragePermission();
                    break;
                default:
                    break;
            }

        }

    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        LOG.e("onActivityResult: requestCode: " + requestCode + "  resultCode:" + resultCode);
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != getActivity().RESULT_OK) {
//            LOG.e("onActivityResult: resultCode!=RESULT_OK");
            return;
        }
        switch (requestCode) {
            //相机返回
            case CODE_CAMERA_REQUEST:
                cropImageUri = Uri.fromFile(fileCropUri);
//                starCropPhoto(cropImageUri);
                cropImageUri(imageUri, cropImageUri, 1, 1, OUTPUT_X, OUTPUT_Y, CROP_PHOTO);
                break;
            //相册返回
            case CODE_GALLERY_REQUEST:
                if (hasSdcard()) {
                    cropImageUri = Uri.fromFile(fileCropUri);
                    Uri newUri = Uri.parse(SystemUtility.getPath(getActivity(), data.getData()));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        newUri = FileProvider.getUriForFile(getActivity(), "com.anxin.kitchen.user.fileprovider", new File(newUri.getPath()));
                    }
                    cropImageUri(newUri, cropImageUri, 1, 1, OUTPUT_X, OUTPUT_Y, CROP_PHOTO);
//                    starCropPhoto(newUri);
                } else {
                    ToastUtil.showToast("设备没有SD卡！");
                }
                break;
            case CROP_PHOTO:
                if (data != null) {
//                    Bitmap bitmap = SystemUtility.getBitmapFromUri(cropImageUri, getActivity());
                    setPicToView(cropImageUri);
                }
                break;
            case USER_NAME:
                if (resultCode == getActivity().RESULT_OK) {
                    if (data != null) {
                        String name = data.getStringExtra("userName");
                        if (name != null && name.length() != 0) {
                            userName.setText(name);
                        }
                    }
                }
                break;
            case LOCATION_NAME:
                if (resultCode == getActivity().RESULT_OK) {
                    AddressBean addressBean = (AddressBean) data.getSerializableExtra("AddressBean");
//                    Log.i("", "--------addressBean-------ww--" + addressBean.toString());
                }
                break;
            default:
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //设置头像
    private void setPicToView(Uri uri) {
        if (uri == null) {
//            LOG.e("------------setPicToView-----return---");
            return;
        }
        userIcon.setImageURI(uri);
        SystemUtility.setHeadIcon(uri);
//        mApp.getCache().setAccountImageURI(mApp.getCache().getUserPhone(), uri.getPath());
//        EventBusFactory.postEvent(new ViewUpdateHeadIconEvent());
    }


    //更新用户信息
    private void updateUserAcount() {
        //获取本地用户名称
        username = mApp.getCache().getNickName();
        if (username != null && username.length() != 0) {
            userName.setText(username);
        }
        //获取本地缓存头像
        String mImageURI = mApp.getCache().getAccountImageURI(mApp.getCache().getUserPhone());
        if (mImageURI != null && !mImageURI.isEmpty()) {
            Uri mSaveUri = Uri.fromFile(new File(mImageURI));
            userIcon.setImageURI(mSaveUri);
        }
    }
    /**
     * 动态申请sdcard读写权限
     */
    private void autoObtainStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSIONS_REQUEST_CODE);
            } else {
                openPic(CODE_GALLERY_REQUEST);
            }
        } else {
            openPic(CODE_GALLERY_REQUEST);
        }
    }

    /**
     * 申请访问相机权限
     */
    private void autoObtainCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {
                    ToastUtil.showToast("您已经拒绝过一次");
                }
                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_PERMISSIONS_REQUEST_CODE);
            } else {//有权限直接调用系统相机拍照
                if (hasSdcard()) {
                    imageUri = Uri.fromFile(fileUri);
                    //通过FileProvider创建一个content类型的Uri
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        imageUri = FileProvider.getUriForFile(getActivity(), "com.anxin.kitchen.user.fileprovider", fileUri);
                    }
                    takePicture(imageUri, CODE_CAMERA_REQUEST);
                } else {
                    ToastUtil.showToast("设备没有SD卡！");
                }
            }
        }
    }

    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    private void takePicture(Uri imageUri, int requestCode) {
        //调用系统相机
        Intent intentCamera = new Intent();
        intentCamera.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        //将拍照结果保存至photo_file的Uri中，不保留在相册中
        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intentCamera, requestCode);

    }

    /**
     * @param requestCode 打开相册的请求码
     */
    private void openPic(int requestCode) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
//        photoPickerIntent.setType("image/*");
        photoPickerIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(photoPickerIntent, requestCode);
    }
    public void onEventMainThread(ViewUpdateHeadIconEvent event) {//头像修改监听
        updateUserAcount();
    }

    /**
     * @param orgUri      剪裁原图的Uri
     * @param desUri      剪裁后的图片的Uri
     * @param aspectX     X方向的比例
     * @param aspectY     Y方向的比例
     * @param width       剪裁图片的宽度
     * @param height      剪裁图片高度
     * @param requestCode 剪裁图片的请求码
     */
    private void cropImageUri(Uri orgUri, Uri desUri, int aspectX, int aspectY, int width, int height, int requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setDataAndType(orgUri, "image/*");
        //发送裁剪信号
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", aspectX);
        intent.putExtra("aspectY", aspectY);
        intent.putExtra("outputX", width);
        intent.putExtra("outputY", height);
        intent.putExtra("scale", true);
        //将剪切的图片保存到目标Uri中
        intent.putExtra(MediaStore.EXTRA_OUTPUT, desUri);
        //是否是圆形裁剪区域，设置了也不一定有效
        intent.putExtra("circleCrop", true);
        //1-false用uri返回图片
        //2-true直接用bitmap返回图片（此种只适用于小图片，返回图片过大会报错）
        intent.putExtra("return-data", true);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, requestCode);
    }
}
