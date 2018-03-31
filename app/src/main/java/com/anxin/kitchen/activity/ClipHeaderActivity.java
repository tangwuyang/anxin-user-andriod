package com.anxin.kitchen.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.anxin.kitchen.MyApplication;
import com.anxin.kitchen.custom.view.BitmapUtil;
import com.anxin.kitchen.custom.view.ClipView;
import com.anxin.kitchen.custom.view.CommonUtil;
import com.anxin.kitchen.user.R;
import com.anxin.kitchen.utils.Log;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

public class ClipHeaderActivity extends BaseActivity implements OnTouchListener {
    private static final Log LOG = Log.getLog();
    private ImageView srcPic;
    private ImageButton bt_ok;
    private ClipView clipview;

    private Matrix matrix = new Matrix();
    private Matrix savedMatrix = new Matrix();

    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    private int mode = NONE;
    private PointF start = new PointF();
    private PointF mid = new PointF();
    private float oldDist = 1f;
    private Bitmap bitmap;
    private int side_length;// 裁剪区域边长
    private ImageView iv_back;
    protected MyApplication mApp;
    private int wmHeight;
    private int wmWidth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_clipheader);
        init();
        mApp = (MyApplication) getApplication();
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        wmHeight = wm.getDefaultDisplay().getHeight();
        wmWidth = wm.getDefaultDisplay().getWidth();
        if (wmHeight > 1900)
            wmHeight = wmHeight - 250;
        else {
            wmHeight = wmHeight - 100;
        }
    }

    private void init() {
        side_length = getIntent().getIntExtra("side_length", 200);
        iv_back = (ImageButton) findViewById(R.id.cancel);
        srcPic = (ImageView) findViewById(R.id.src_pic);
        clipview = (ClipView) findViewById(R.id.clipView);
        bt_ok = (ImageButton) findViewById(R.id.bt_ok);

        srcPic.setOnTouchListener(this);
        ViewTreeObserver observer = clipview.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            public void onGlobalLayout() {
                clipview.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                initSrcPic();
            }
        });

        bt_ok.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                generateUriAndReturn();
            }
        });

        iv_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initSrcPic() {
        Uri uri = getIntent().getData();
        String path = uri.getPath();
        if (TextUtils.isEmpty(path)) {
            return;
        }
//        // 原图可能很大，现在手机照出来都3000*2000左右了，直接加载可能会OOM
//        // 这里 decode 出 720*1280 左右的照片
//        bitmap = BitmapUtil.decodeSampledBitmap(path, 720, 1280);
        bitmap = loadBitmap(path, true);
        if (bitmap == null) {
            Bitmap mPhoto = null;
            try {
                mPhoto = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            bitmap = mPhoto;
            if (bitmap == null)
                return;
        }
        matrix.postTranslate(0, 0);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                matrix.postTranslate((wmWidth - bitmap.getWidth()) / 2, (wmHeight - bitmap.getHeight()) / 2);
                savedMatrix.set(matrix);
                srcPic.setImageBitmap(bitmap);
                srcPic.setScaleType(ScaleType.MATRIX);
                srcPic.setImageMatrix(matrix);

            }
        });

    }

    /**
     *  从给定路径加载图片
     */
    public static Bitmap loadBitmap(String imgpath) {
        return BitmapFactory.decodeFile(imgpath);
    }

    /**
     *  从给定的路径加载图片，并指定是否自动旋转方向
     */
    public static Bitmap loadBitmap(String imgpath, boolean adjustOritation) {
        Bitmap bm = loadBitmap(imgpath);
        // 原图可能很大，现在手机照出来都3000*2000左右了，直接加载可能会OOM
        // 这里 decode 出 720*1280 左右的照片
        bm = BitmapUtil.decodeSampledBitmap(imgpath, 720, 1280);
        if (!adjustOritation) {
            return bm;
        } else {
            int digree = 0;
            ExifInterface exif = null;
            try {
                exif = new ExifInterface(imgpath);
            } catch (Exception e) {
                e.printStackTrace();
                exif = null;
            }
            if (exif != null) {
                // 读取图片中相机方向信息
                int ori = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
                // 计算旋转角度
                switch (ori) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        digree = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        digree = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        digree = 270;
                        break;
                    default:
                        digree = 0;
                        break;
                }
            }
            if (digree != 0) {
                //旋转图片
                Matrix m = new Matrix();
                m.postRotate(digree);
                bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);
            }
            return bm;
        }
    }

    public boolean onTouch(View v, MotionEvent event) {
        ImageView view = (ImageView) v;
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                savedMatrix.set(matrix);
                start.set(event.getX(), event.getY());
                mode = DRAG;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = spacing(event);
                if (oldDist > 10f) {
                    savedMatrix.set(matrix);
                    midPoint(mid, event);
                    mode = ZOOM;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == DRAG) {
                    matrix.set(savedMatrix);
                    matrix.postTranslate(event.getX() - start.x, event.getY() - start.y);
                } else if (mode == ZOOM) {
                    float newDist = spacing(event);
                    if (newDist > 10f) {
                        matrix.set(savedMatrix);
                        float scale = newDist / oldDist;
                        matrix.postScale(scale, scale, mid.x, mid.y);
                    }
                }
                break;
        }
        view.setImageMatrix(matrix);
        return true;
    }

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    private Bitmap getZoomedCropBitmap() {

        srcPic.setDrawingCacheEnabled(true);
        srcPic.buildDrawingCache();

        Rect rect = clipview.getClipRect();

        Bitmap cropBitmap = null;
        Bitmap zoomedCropBitmap = null;
        try {
            cropBitmap = Bitmap.createBitmap(srcPic.getDrawingCache(), rect.left, rect.top, rect.width(),
                    rect.height());
            zoomedCropBitmap = BitmapUtil.zoomBitmap(cropBitmap, side_length, side_length);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (cropBitmap != null) {
            cropBitmap.recycle();
        }

        // 释放资源
        srcPic.destroyDrawingCache();

        return zoomedCropBitmap;
    }

    private void generateUriAndReturn() {
        String email = MyApplication.mApp.getCache().getUserPhone();
        Bitmap zoomedCropBitmap = getZoomedCropBitmap();
        if (zoomedCropBitmap == null) {
//            LOG.e("zoomedCropBitmap == null");
            return;
        }
        String path = getCacheDir() + File.separator + email + ".jpg";
        Uri mSaveUri = Uri.fromFile(new File(path));

        if (mSaveUri != null) {
            OutputStream outputStream = null;
            try {
                outputStream = getContentResolver().openOutputStream(mSaveUri);
                if (outputStream != null) {
                    zoomedCropBitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
                }
            } catch (IOException ex) {
                // TODO: report error to caller
                LOG.e("Cannot open file: " + mSaveUri, ex);
            } finally {
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            Intent intent = new Intent();
            intent.setData(mSaveUri);
            MyApplication.mApp.getCache().setAccountImageURI(mApp.getCache().getUserPhone(), path);
            ClipHeaderActivity.this.setResult(this.RESULT_OK, intent);
            ClipHeaderActivity.this.finish();
        }
    }

}