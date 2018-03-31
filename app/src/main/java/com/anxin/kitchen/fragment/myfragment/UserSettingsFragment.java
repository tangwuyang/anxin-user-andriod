package com.anxin.kitchen.fragment.myfragment;

import android.Manifest;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.anxin.kitchen.MyApplication;
import com.anxin.kitchen.activity.ClipHeaderActivity;
import com.anxin.kitchen.activity.UserNameActivity;
import com.anxin.kitchen.custom.view.CustomDatePicker;
import com.anxin.kitchen.custom.view.SelectGenderPopupWindow;
import com.anxin.kitchen.custom.view.SelectPicPopupWindow;
import com.anxin.kitchen.fragment.HomeBaseFragment;
import com.anxin.kitchen.user.R;
import com.anxin.kitchen.utils.DateUtils;
import com.anxin.kitchen.utils.Log;
import com.anxin.kitchen.utils.MyService;
import com.anxin.kitchen.utils.SystemUtility;
import com.anxin.kitchen.utils.ToastUtil;
import com.anxin.kitchen.view.RoundedImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * 设置界面
 */
public class UserSettingsFragment extends HomeBaseFragment implements View.OnClickListener {
    private Log LOG = Log.getLog();
    private View view;
    private ImageView backBtn;//返回
    private TextView PreservationBtn;
    private RelativeLayout userIconBtn;//用户头像按钮
    private RelativeLayout userNameBtn;//用户姓名按钮
    private RelativeLayout userGenderBtn;//用户性别按钮
    private RelativeLayout userBirthdayBtn;//用户生日按钮
    private RoundedImageView userIcon;//用户头像
    private TextView userName;//用户名称
    private TextView userPhone;//用户电话
    private TextView userGender;//用户性别
    private TextView userBirthday;//用户生日
    private SelectPicPopupWindow menuWindowSelectPic;//选择头像图片弹窗
    private SelectGenderPopupWindow menuSelectGender;//选择用户性别弹窗
    private static final int CROP_PHOTO = 111;
    private static final int USER_NAME = 112;
    private static final String sendUpdateUser_http = "sendUpdateUser";
    private CustomDatePicker datePicker;
    private String date;
    private String UserDate = null;//用户生日标志位
    private int UserSex = 0;//用户性别标志位
    private String UserName = null;//用户姓名标志位

    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CAMERA_PERMISSIONS_REQUEST_CODE = 0x03;
    private static final int STORAGE_PERMISSIONS_REQUEST_CODE = 0x04;
    private static final int OUTPUT_X = 480;
    private static final int OUTPUT_Y = 480;
    private File fileUri = new File(Environment.getExternalStorageDirectory().getPath() + "/photo.jpg");
    private File fileCropUri = new File(Environment.getExternalStorageDirectory().getPath() + "/crop_photo.jpg");
    private Uri imageUri;
    private Uri cropImageUri;


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
        PreservationBtn = view.findViewById(R.id.PreservationBtn);
        PreservationBtn.setOnClickListener(this);

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
        userGender = (TextView) view.findViewById(R.id.user_gender);
        userBirthday = (TextView) view.findViewById(R.id.user_birthday);

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
        //获取本地用户性别
        String sex = mApp.getAccount().getUserSex();
        if (sex != null && sex.length() != 0) {
            if (sex.equals("1")) {
                userGender.setText("男");
            } else if (sex.equals("2"))
                userGender.setText("女");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        String time = sdf.format(new Date());
        //获取本地用户生日
        final String userDate = mApp.getAccount().getUserBirthdayTime();
        if (userDate != null && !userDate.equals("null")) {
            date = DateUtils.stampToDate(userDate, "yyyy-MM-dd");
            userBirthday.setText(date);
        } else
            date = time.split(" ")[0];
        /**
         * 设置年月日
         */
        datePicker = new CustomDatePicker(getActivity(), "请选择日期", new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) {
                date = time.split(" ")[0];
                userBirthday.setText(date);
                UserDate = DateUtils.dateToStamp(date);
            }
        }, "1900-01-01", time);
        datePicker.showSpecificTime(false); //显示时和分
        datePicker.setIsLoop(false);
        datePicker.setDayIsLoop(true);
        datePicker.setMonIsLoop(true);
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
            case R.id.PreservationBtn:
                if (UserName == null && UserDate == null && UserSex == 0)
                    return;
                sendUpdateAccount();
                break;
            case R.id.user_icon_rlt://修改用户头像
                menuWindowSelectPic = new SelectPicPopupWindow(getActivity(), itemsOnClick);
                menuWindowSelectPic.showAtLocation(getActivity().findViewById(R.id.RelativeLayout01),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.user_name_rlt://修改用户名
                Intent intent = new Intent();
                intent.setClass(getActivity(), UserNameActivity.class);
                intent.putExtra("userName", "");
                startActivityForResult(intent, USER_NAME);
                break;
            case R.id.user_gender_rlt://修改用户性别
                menuSelectGender = new SelectGenderPopupWindow(getActivity(), itemsGenderOnClick);
                menuSelectGender.showAtLocation(getActivity().findViewById(R.id.RelativeLayout01),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.user_birthday_rlt://修改用户生日
                datePicker.show(date);
                break;
            default:
                break;
        }
    }

    //保存用戶信息
    private void sendUpdateAccount() {
        JSONObject jsonObject = new JSONObject();
        try {
            if (UserName != null && UserName.length() != 0) {
                jsonObject.put("trueName", UserName);
                mApp.getAccount().setUserNickName(UserName);
                mApp.getAccount().setUserTrueName(UserName);
            }
            if (UserDate != null && UserDate.length() != 0) {
                jsonObject.put("birthdayTime", UserDate);
                mApp.getAccount().setUserBirthdayTime(UserDate);
            }
            if (UserSex != 0) {
                jsonObject.put("sex", UserSex);
                mApp.getAccount().setUserSex(UserSex + "");
            }
//            jsonObject.put("phone", phone);
//            jsonObject.put("province", Integer.valueOf(addressBean.getProvinceID()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String urlPath = SystemUtility.sendUpdateUser();
        Map<String, Object> dataMap = new HashMap();
        dataMap.put("token", SystemUtility.AMToken);
        dataMap.put("formData", jsonObject.toString());
//        Log.e("onEventMainThread", "----------dataMap--------------" + dataMap.toString());
        SystemUtility.requestNetPost(urlPath, dataMap, sendUpdateUser_http);
        getFragmentManager().popBackStack();
    }

    private View.OnClickListener itemsOnClick = new View.OnClickListener() {

        public void onClick(View v) {
            menuWindowSelectPic.dismiss();
            switch (v.getId()) {
                case R.id.btn_take_photo://打开相机
                    autoObtainCameraPermission();
                    break;
                case R.id.btn_pick_photo://打开系统相册
                    autoObtainStoragePermission();
                    break;
                default:
                    break;
            }

        }

    };

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
                        imageUri = FileProvider.getUriForFile(getActivity(), "com.anxin.kitchen.fileprovider", fileUri);
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

    private View.OnClickListener itemsGenderOnClick = new View.OnClickListener() {

        public void onClick(View v) {
            menuSelectGender.dismiss();
            switch (v.getId()) {
                case R.id.btn_take_photo://性别选择：男
                    userGender.setText("男");
                    UserSex = 1;
                    break;
                case R.id.btn_pick_photo://性别选择：女
                    userGender.setText("女");
                    UserSex = 2;
                    break;
                default:
                    break;
            }

        }

    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        LOG.e("onActivityResult: requestCode: " + requestCode + "  resultCode:" + resultCode);
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != getActivity().RESULT_OK) {
            LOG.e("onActivityResult: resultCode!=RESULT_OK");
            return;
        }
        switch (requestCode) {
            //相机返回
            case CODE_CAMERA_REQUEST:
                cropImageUri = Uri.fromFile(fileCropUri);
//                PhotoUtils.cropImageUri(this, imageUri, cropImageUri, 1, 1, OUTPUT_X, OUTPUT_Y, CODE_RESULT_REQUEST);
//                starCropPhoto(cropImageUri);
                cropImageUri(imageUri, cropImageUri, 1, 1, OUTPUT_X, OUTPUT_Y, CROP_PHOTO);
                break;
            //相册返回
            case CODE_GALLERY_REQUEST:

                if (hasSdcard()) {
                    cropImageUri = Uri.fromFile(fileCropUri);
                    Uri newUri = Uri.parse(SystemUtility.getPath(getActivity(), data.getData()));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        newUri = FileProvider.getUriForFile(getActivity(), "com.anxin.kitchen.fileprovider", new File(newUri.getPath()));
                    }
                    cropImageUri(newUri, cropImageUri, 1, 1, OUTPUT_X, OUTPUT_Y, CROP_PHOTO);
//                    starCropPhoto(newUri);
                } else {
                    ToastUtil.showToast("设备没有SD卡！");
                }
                break;
            case CROP_PHOTO:
                if (data != null) {
                    Bitmap bitmap = SystemUtility.getBitmapFromUri(cropImageUri, getActivity());
                    setPicToView(cropImageUri);
                }
                break;
            case USER_NAME:
                if (data != null) {
                    String name = data.getStringExtra("userName");
                    UserName = name;
                    userName.setText(name);
                }
                break;
            default:
                break;

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        LOG.e("onRequestPermissionsResult: ");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            //调用系统相机申请拍照权限回调
            case CAMERA_PERMISSIONS_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (hasSdcard()) {
                        imageUri = Uri.fromFile(fileUri);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            //通过FileProvider创建一个content类型的Uri
                            imageUri = FileProvider.getUriForFile(getActivity(), "com.anxin.kitchen.fileprovider", fileUri);
                        }
                        takePicture(imageUri, CODE_CAMERA_REQUEST);
                    } else {
                        ToastUtil.showToast("设备没有SD卡！");
                    }
                } else {
                    ToastUtil.showToast("请允许打开相机！！");
                }
                break;
            }
            //调用系统相册申请Sdcard权限回调
            case STORAGE_PERMISSIONS_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openPic(CODE_GALLERY_REQUEST);
                } else {
                    ToastUtil.showToast("请允许打操作SDCard！！");
                }
                break;
            default:
        }
    }

    //设置头像
    private void setPicToView(Uri uri) {
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
    public void starCropPhoto(Uri mSaveUri) {
//        userIcon.setImageURI(mSaveUri);
        Intent intent = new Intent();
        intent.setClass(getActivity(), ClipHeaderActivity.class);
        intent.setData(mSaveUri);
        intent.putExtra("side_length", 400);// 裁剪图片宽高
        startActivityForResult(intent, CROP_PHOTO);
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
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, requestCode);
    }
}
