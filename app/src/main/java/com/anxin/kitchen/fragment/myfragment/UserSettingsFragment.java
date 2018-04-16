package com.anxin.kitchen.fragment.myfragment;

import android.Manifest;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.anxin.kitchen.activity.ClipPictureActivity;
import com.anxin.kitchen.activity.UserNameActivity;
import com.anxin.kitchen.custom.view.ClipHeaderActivity;
import com.anxin.kitchen.custom.view.CustomDatePicker;
import com.anxin.kitchen.custom.view.SelectGenderPopupWindow;
import com.anxin.kitchen.custom.view.SelectPicPopupWindow;
import com.anxin.kitchen.event.AsyncHttpRequestMessage;
import com.anxin.kitchen.event.OnSaveBitmapEvent;
import com.anxin.kitchen.fragment.HomeBaseFragment;
import com.anxin.kitchen.user.R;
import com.anxin.kitchen.utils.DateUtils;
import com.anxin.kitchen.utils.EventBusFactory;
import com.anxin.kitchen.utils.GetImagePath;
import com.anxin.kitchen.utils.Log;
import com.anxin.kitchen.utils.MyService;
import com.anxin.kitchen.utils.StringUtils;
import com.anxin.kitchen.utils.SystemUtility;
import com.anxin.kitchen.utils.ToastUtil;
import com.anxin.kitchen.view.RoundedImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
    private static final int OUTPUT_X = 80;
    private static final int OUTPUT_Y = 80;
    private File fileUri;
    private File fileCropUri;
    private File headClipFile;// 裁剪后的头像
    private Uri imageUri;
    private Uri cropImageUri = null;
    public static final String HEAD_ICON_DIC = Environment
            .getExternalStorageDirectory()
            + File.separator + "anxin";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBusFactory.getInstance().register(this);
        fileUri = new File(HEAD_ICON_DIC, "photo.jpg");
        fileCropUri = new File(HEAD_ICON_DIC, "crop_photo.jpg");
        headClipFile = new File(HEAD_ICON_DIC, "clipIcon.jpg");
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
        UserName = mApp.getAccount().getUserTrueName();
        if (UserName != null && UserName.length() != 0) {
            userName.setText(UserName);
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
        initHeadIconFile();
        super.onResume();
    }

    /**
     * 监听网络请求返回
     *
     * @param asyncHttpRequestMessage
     */
    public void onEventMainThread(AsyncHttpRequestMessage asyncHttpRequestMessage) {
        String requestCode = asyncHttpRequestMessage.getRequestCode();
        String responseMsg = asyncHttpRequestMessage.getResponseMsg();
        String requestStatus = asyncHttpRequestMessage.getRequestStatus();
        String codeToKen = StringUtils.parserMessage(responseMsg, "code");
        switch (requestCode) {
            //验证码发送
            case "getHeadIconHttp":
                //网络请求返回成功
                if (requestStatus != null && requestStatus.equals(SystemUtility.RequestSuccess)) {
                    //解析验证码返回
                    String code = StringUtils.parserMessage(responseMsg, "code");
                    String data = StringUtils.parserMessage(responseMsg, "data");
                    if (code != null && code.equals("1")) {
                        EventBusFactory.getInstance().post(new OnSaveBitmapEvent(data, mApp.getAccount().getUserPhone()));
                        sendUpdateAccount(data);
                    }
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                getFragmentManager().popBackStack();
                break;
            case R.id.PreservationBtn:
                if (UserName == null && UserDate == null && UserSex == 0 && cropImageUri == null)
                    return;
                if (cropImageUri != null) {
                    SystemUtility.setHeadIcon(cropImageUri);
                } else
                    sendUpdateAccount(null);
                break;
            case R.id.user_icon_rlt://修改用户头像
                menuWindowSelectPic = new SelectPicPopupWindow(getActivity(), itemsOnClick);
                menuWindowSelectPic.showAtLocation(getActivity().findViewById(R.id.RelativeLayout01),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.user_name_rlt://修改用户名
                Intent intent = new Intent();
                intent.setClass(getActivity(), UserNameActivity.class);
                intent.putExtra("userName", UserName);
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
    private void sendUpdateAccount(String pahtURL) {
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
            if (pahtURL != null && pahtURL.length() != 0) {
                jsonObject.put("userLogo", pahtURL);
                mApp.getAccount().setUserLogoPathURL(pahtURL);
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
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//设置Action为拍照
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        this.startActivityForResult(intent, requestCode);
    }

    /**
     * @param requestCode 打开相册的请求码
     */
    private void openPic(int requestCode) {
//        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
////        photoPickerIntent.setType("image/*");
//        photoPickerIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
//        startActivityForResult(photoPickerIntent, requestCode);
        Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
        openAlbumIntent.setType("image/*");
        startActivityForResult(openAlbumIntent, requestCode);
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
//            LOG.e("onActivityResult: resultCode!=RESULT_OK");
            return;
        }
        switch (requestCode) {
            //相机返回
            case CODE_CAMERA_REQUEST:
                clipPhotoBySelf(fileUri.getAbsolutePath());
                break;
            //相册返回
            case CODE_GALLERY_REQUEST:
                if (data != null) {
                    String filePath = "";
                    Uri originalUri = data.getData(); // 获得图片的uri
//                        Log.i(TAG, "originalUri : " + originalUri);
                    if (originalUri != null) {
                        filePath = GetImagePath.getPath(getActivity(), originalUri);
                    }
//                        Log.i(TAG, "filePath : " + filePath);
                    if (filePath != null && filePath.length() > 0) {
                        //clipPhotoBySystem(originalUri);
                        //调用自定义裁剪
                        clipPhotoBySelf(filePath);
                    }
                }
                break;
            case CROP_PHOTO:
                if (data != null) {
                    cropImageUri = Uri.fromFile(headClipFile);
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

    private void initHeadIconFile() {
        headClipFile = new File(HEAD_ICON_DIC);
        if (!headClipFile.exists()) {
            boolean mkdirs = headClipFile.mkdirs();
        }
        fileUri = new File(HEAD_ICON_DIC, "photo.jpg");
        fileCropUri = new File(HEAD_ICON_DIC, "crop_photo.jpg");
        headClipFile = new File(HEAD_ICON_DIC, "clipIcon.jpg");
    }

    /**
     * 调用自定义切图方法
     *
     * @param filePath
     */
    protected void clipPhotoBySelf(String filePath) {
        //进入裁剪页面,此处用的是自定义的裁剪页面而不是调用系统裁剪
        Intent intent = new Intent(getActivity(), ClipPictureActivity.class);
        intent.putExtra(ClipPictureActivity.IMAGE_PATH_ORIGINAL, filePath);
        intent.putExtra(ClipPictureActivity.IMAGE_PATH_AFTER_CROP,
                headClipFile.getAbsolutePath());
        startActivityForResult(intent, CROP_PHOTO);

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
                            imageUri = FileProvider.getUriForFile(getActivity(), "com.anxin.kitchen.user.fileprovider", fileUri);
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
//            LOG.e("------------setPicToView-----return---");
            return;
        }
        userIcon.setImageURI(uri);
//        SystemUtility.setHeadIcon(uri);
//        mApp.getCache().setAccountImageURI(mApp.getCache().getUserPhone(), uri.getPath());
//        EventBusFactory.postEvent(new ViewUpdateHeadIconEvent());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusFactory.getInstance().unregister(this);
    }
}
