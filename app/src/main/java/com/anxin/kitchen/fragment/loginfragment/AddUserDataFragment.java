package com.anxin.kitchen.fragment.loginfragment;

import android.Manifest;
import android.app.Activity;
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
import com.anxin.kitchen.activity.ClipPictureActivity;
import com.anxin.kitchen.activity.LocationActivity;
import com.anxin.kitchen.activity.LoginActivity;
import com.anxin.kitchen.activity.UserNameActivity;
import com.anxin.kitchen.bean.AddressBean;
import com.anxin.kitchen.custom.view.SelectPicPopupWindow;
import com.anxin.kitchen.event.AsyncHttpRequestMessage;
import com.anxin.kitchen.event.OnSaveBitmapEvent;
import com.anxin.kitchen.event.ViewUpdateHeadIconEvent;
import com.anxin.kitchen.user.R;
import com.anxin.kitchen.utils.EventBusFactory;
import com.anxin.kitchen.utils.GetImagePath;
import com.anxin.kitchen.utils.Log;
import com.anxin.kitchen.utils.MyService;
import com.anxin.kitchen.utils.StringUtils;
import com.anxin.kitchen.utils.SystemUtility;
import com.anxin.kitchen.utils.ToastUtil;
import com.anxin.kitchen.view.RoundedImageView;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 完善用户资料界面
 */
public class AddUserDataFragment extends Fragment implements View.OnClickListener {
    private Log LOG = Log.getLog();
    private View view;
    private RelativeLayout userIconBtn;//用户头像按钮
    private RelativeLayout userNameBtn;//用户名称按钮
    private RelativeLayout userAddressBtn;//用户地址
    private TextView userAddress_tv;//用户地址
    private RoundedImageView userIcon;//用户头像
    private TextView userName;//用户名称
    private EditText userAddressEdit;//用户详细地址
    private TextView skipBtn;//跳过，下一步按钮
    private Button completeBtn;//完成按钮
    private String UserName = null;//用户姓名标志位
    private SelectPicPopupWindow menuWindowSelectPic;
    private MyApplication mApp = null;
    private String username;
    private Activity mActivity;
    private static final String sendUpdateUser_http = "sendUpdateUser";
    private static final int LOCATION_NAME = 103;
    private static final int USER_NAME = 112;
    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CAMERA_PERMISSIONS_REQUEST_CODE = 0x03;
    private static final int STORAGE_PERMISSIONS_REQUEST_CODE = 0x04;
    private static final int CROP_PHOTO = 111;
    private File fileUri;
    private File fileCropUri;
    private File headClipFile;// 裁剪后的头像
    private Uri imageUri;
    private Uri cropImageUri = null;
    public static final String HEAD_ICON_DIC = Environment
            .getExternalStorageDirectory()
            + File.separator + "anxin";
    private AddressBean addressBean = null;//送餐地址
    private String userAddress;//地址详情

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mApp == null)
            mApp = MyApplication.getInstance();
        EventBusFactory.getInstance().register(this);
        fileUri = new File(HEAD_ICON_DIC, "photo.jpg");
        fileCropUri = new File(HEAD_ICON_DIC, "crop_photo.jpg");
        headClipFile = new File(HEAD_ICON_DIC, "clipIcon.jpg");
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
        userAddress_tv = view.findViewById(R.id.user_address);
        updateUserAcount();
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("AddUserdata");
        initHeadIconFile();
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("AddUserdata");
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

    public void finishToLastActivity() {
        Intent intent = new Intent();
        intent.putExtra("loginTag",true);
        mActivity.setResult(201,intent);
        mActivity.finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.skip_btn:
                mActivity = getActivity();
                finishToLastActivity();
                break;
            case R.id.completeBtn:
                userAddress = userAddressEdit.getText().toString();
                if (UserName == null && (addressBean == null || userAddress == null || userAddress.length() == 0)) {
                    return;
                }
                if (cropImageUri != null) {
                    SystemUtility.setHeadIcon(cropImageUri,getActivity());
                } else {
                    sendAddAddressHttp(UserName, mApp.getAccount().getUserPhone(), userAddress);
                    sendUpdateAccount(null);
                }
                break;
            case R.id.user_icon_rlt://修改用户头像
                showChooseDialog();
                break;
            case R.id.user_name_rlt://修改用户名
                Intent intent = new Intent();
                intent.setClass(getActivity(), UserNameActivity.class);
                intent.putExtra("userName", username);
                startActivityForResult(intent, USER_NAME);
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
                        sendAddAddressHttp(UserName, mApp.getAccount().getUserPhone(), userAddress);
                        sendUpdateAccount(data);
                    }
                }
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
            if (pahtURL != null && pahtURL.length() != 0) {
                jsonObject.put("userLogo", pahtURL);
                mApp.getAccount().setUserLogoPathURL(pahtURL);
            }
//            jsonObject.put("phone", phone);
//            jsonObject.put("province", Integer.valueOf(addressBean.getProvinceID()));
        } catch (JSONException e) {
            MobclickAgent.reportError(MyApplication.getInstance(), e);
        }
        String urlPath = SystemUtility.sendUpdateUser();
        Map<String, Object> dataMap = new HashMap();
        dataMap.put("token", SystemUtility.AMToken);
        dataMap.put("formData", jsonObject.toString());
//        Log.e("onEventMainThread", "----------dataMap--------------" + dataMap.toString());
        SystemUtility.requestNetPost(urlPath, dataMap, sendUpdateUser_http);
//        getFragmentManager().popBackStack();
        getActivity().finish();
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
//        LOG.e("onActivityResult: requestCode: " + requestCode + "  resultCode:" + resultCode);
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
                    userIcon.setImageURI(cropImageUri);
                }
                break;
            case USER_NAME:
                    if (data != null) {
                        String name = data.getStringExtra("userName");
                            UserName = name;
                            userName.setText(name);
                    }
                break;
            case LOCATION_NAME:
                if (resultCode == getActivity().RESULT_OK) {
                    addressBean = (AddressBean) data.getSerializableExtra("AddressBean");
//                    Log.i("", "--------addressBean-------ww--" + addressBean.toString());
                    userAddress_tv.setTextColor(getResources().getColor(R.color.black));
                    userAddress_tv.setText(addressBean.getStreetName());
                }
                break;
            default:
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    //更新用户信息
    private void updateUserAcount() {
        //获取本地用户名称
        username = mApp.getCache().getNickName();
        if (username != null && username.length() != 0) {
            userName.setText(username);
            if (!username.equals("暂无")) {
                UserName = username;
            }
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
                imageUri = Uri.fromFile(fileUri);
                //通过FileProvider创建一个content类型的Uri
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    imageUri = FileProvider.getUriForFile(getActivity(), "com.anxin.kitchen.user.fileprovider", fileUri);
                }
                takePicture(imageUri, CODE_CAMERA_REQUEST);
            }
        }
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
                    imageUri = Uri.fromFile(fileUri);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        //通过FileProvider创建一个content类型的Uri
                        imageUri = FileProvider.getUriForFile(getActivity(), "com.anxin.kitchen.user.fileprovider", fileUri);
                    }
                    takePicture(imageUri, CODE_CAMERA_REQUEST);
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

    public void onEventMainThread(ViewUpdateHeadIconEvent event) {//头像修改监听
        updateUserAcount();
    }

    /**
     * 添加地址信息
     *
     * @param name
     * @param phone
     * @param address
     */
    private void sendAddAddressHttp(String name, String phone, String address) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("isDefault", 0);
            jsonObject.put("contactName", name);
            jsonObject.put("phone", phone);
            jsonObject.put("province", Integer.valueOf(addressBean.getProvinceID()));
            jsonObject.put("city", Integer.valueOf(addressBean.getCityID()));
            jsonObject.put("district", Integer.valueOf(addressBean.getDistrictID()));
            jsonObject.put("street", addressBean.getStreetName());
            jsonObject.put("address", address);
            jsonObject.put("longitude", addressBean.getLongitude());
            jsonObject.put("latitude", addressBean.getLatitude());
        } catch (JSONException e) {
            MobclickAgent.reportError(MyApplication.getInstance(), e);
        }
        String urlPath = SystemUtility.sendAddAddress();
        Map<String, Object> dataMap = new HashMap();
        dataMap.put("token", SystemUtility.AMToken);
        dataMap.put("formData", jsonObject.toString());
//        Log.e("onEventMainThread", "----------dataMap--------------" + dataMap.toString());
        SystemUtility.requestNetPost(urlPath, dataMap, "");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusFactory.getInstance().unregister(this);
    }
}
