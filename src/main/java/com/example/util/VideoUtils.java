package com.example.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.AnyRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;

import com.cisner.cisnerapp.BaseBinder;
import com.cisner.cisnerapp.R;
import com.cisner.cisnerapp.interfaces.OnSearchableDialog;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;
import com.yalantis.ucrop.model.AspectRatio;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VideoUtils {

    private static final int VIDEO_PICKER_SELECT = 1001;
    private static final int REQUEST_VIDEO_CAPTURE = 1002;
    private static Uri mImageCaptureUri;
    private static OnPickPhoto onPickPhoto;
    private static Activity activity;
    private static File filePath;

    public static void selectVideoDialog(final Activity activity, boolean ifRemoveShown, OnPickPhoto onPick) {
        onPickPhoto = onPick;
        VideoUtils.activity = activity;

        ArrayList<String> arrPickOptions = new ArrayList<>();
        arrPickOptions.add(BaseBinder.getLabel(activity.getString(R.string.gallery)));
        arrPickOptions.add(BaseBinder.getLabel(activity.getString(R.string.takevideo)));
        if (ifRemoveShown)
            arrPickOptions.add(activity.getString(R.string.remove));
        //  arrPickOptions.add(activity.getString(R.string.cancel));

        AlertUtils.showBottomSheetListDialog(activity, arrPickOptions, null, new OnSearchableDialog() {
            @Override
            public void onItemSelected(Object o) {
                String s = (String) o;
                if (s.equalsIgnoreCase(BaseBinder.getLabel(activity.getString(R.string.gallery)))) {
                    CheckPermissionUtils.isCheck(activity, CheckPermissionUtils.READ_EXTERNAL, CheckPermissionUtils.REQUEST_CODE_READ_EXTERNAL, permissionListener);
                } else if (s.equalsIgnoreCase(BaseBinder.getLabel(activity.getString(R.string.takevideo)))) {
                    CheckPermissionUtils.isCheck(activity, CheckPermissionUtils.CAMERA, CheckPermissionUtils.CAMERA_CODE, permissionListener);
                } else if (s.equalsIgnoreCase(activity.getString(R.string.remove))) {
                } else if (s.equalsIgnoreCase(activity.getString(R.string.cancel))) {
                }
            }
        });
    }

    public static void openGalleryPhoto(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        intent.setType("video/*");
        activity.startActivityForResult(intent, VIDEO_PICKER_SELECT);
    }

    public static void openCameraPhoto(Activity activity) {
        File mydir = new File(Environment.getExternalStorageDirectory() + "/" + activity.getString(R.string.app_name) + "/images/");
        if (!mydir.exists()) {
            mydir.mkdirs();
        }

        filePath = new File(mydir.getAbsolutePath() + "/" + String.valueOf(System.currentTimeMillis() + ".mp4"));
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N)
            mImageCaptureUri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".provider", filePath);
        else
            mImageCaptureUri = Uri.fromFile(filePath);

        Intent takePictureIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
            //to open default camera app only, it's not necessary
            if (getPackageCamera(activity) != null && !getPackageCamera(activity).equalsIgnoreCase(""))
                takePictureIntent.setPackage(getPackageCamera(activity));
            activity.startActivityForResult(takePictureIntent, REQUEST_VIDEO_CAPTURE);
        }
    }

    private static String getPackageCamera(Activity activity) {
        try {
            List<ApplicationInfo> list = activity.getPackageManager().getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
            for (int n = 0; n < list.size(); n++) {
                if ((list.get(n).flags & ApplicationInfo.FLAG_SYSTEM) == 1) {
                    if (list.get(n).loadLabel(activity.getPackageManager()).toString().equalsIgnoreCase("Camera")) {
                        return list.get(n).packageName;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void onActivityResult(int requestCode, int resultCode, Intent data, OnPickPhoto mOnPickPhoto, Context context, boolean isCrop, boolean isCircle) {
        if (resultCode != Activity.RESULT_OK) {
            if (onPickPhoto != null)
                onPickPhoto.onCancel();
            return;
        }
        switch (requestCode) {
            case VIDEO_PICKER_SELECT:
                Uri uri = data.getData();
                //processImage(context, uri, onPickPhoto, isCrop, isCircle);
                onPickPhoto.onPick(uri, FileUtils.getPath(context, uri));
                break;

            case UCrop.REQUEST_CROP:
                uri = UCrop.getOutput(data);
                if (uri != null) {
                    if (onPickPhoto != null) {
                        onPickPhoto.onPick(uri, FileUtils.getPath(context, uri));
                    }
                }
                break;
            case REQUEST_VIDEO_CAPTURE:
//                uri=data.getData();
                //processImage(context, uri, onPickPhoto, isCrop, isCircle);
                onPickPhoto.onPick(mImageCaptureUri, filePath.getAbsolutePath());
                mImageCaptureUri = null;
                break;
        }
    }

    public interface OnPickPhoto {
        public void onPick(Uri uri, String path);

        public void onCancel();
    }

    private static void processImage(Context context, Uri uri, OnPickPhoto mOnPickPhoto, boolean isCrop, boolean isCircle) {
        if (uri != null) {
            if (isCrop) {
                try {
                    File file = new File(context.getCacheDir(), System.currentTimeMillis() + ".png");
                    if (!file.exists())
                        file.createNewFile();
                    UCrop.Options options = new UCrop.Options();
                    options.setCircleDimmedLayer(isCircle);
                    options.setHideBottomControls(true);
                    options.setAspectRatioOptions(0, new AspectRatio(null, 1, 1));
                    options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.SCALE, UCropActivity.SCALE);
                    options.setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary));
                    options.setStatusBarColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
                    options.setActiveWidgetColor(ContextCompat.getColor(context, R.color.colorAccent));
                    UCrop.of(uri, Uri.fromFile(file))
//                            .withAspectRatio(16, 9)
                            .withOptions(options)
                            .start((Activity) context);
                } catch (IOException e) {
                    e.printStackTrace();
                    if (onPickPhoto != null) {
                        onPickPhoto.onPick(uri, FileUtils.getPath(context, uri));
                    }
                }
            } else {
                if (onPickPhoto != null) {
                    onPickPhoto.onPick(uri, FileUtils.getPath(context, uri));
                }
            }
        }
    }

    public static final Uri getUriOfDrawable(@NonNull Context context,
                                             @AnyRes int drawableId) {
        Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + context.getResources().getResourcePackageName(drawableId)
                + '/' + context.getResources().getResourceTypeName(drawableId)
                + '/' + context.getResources().getResourceEntryName(drawableId));
        return imageUri;
    }

    static CheckPermissionUtils.permissionListener permissionListener = new CheckPermissionUtils.permissionListener() {
        @Override
        public void onAllow(int requestCode) {
            switch (requestCode) {
                case CheckPermissionUtils.REQUEST_CODE_READ_EXTERNAL:
                    openGalleryPhoto(activity);
                    break;

                case CheckPermissionUtils.WRITE_CODE_READ_EXTERNAL:
                    CheckPermissionUtils.isCheck(activity, CheckPermissionUtils.CAMERA, CheckPermissionUtils.CAMERA_CODE, permissionListener);
                    break;

                case CheckPermissionUtils.CAMERA_CODE:
                    openCameraPhoto(activity);
                    break;
            }
        }

        @Override
        public void onDeny(int requestCode) {

        }
    };

    public static void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            permissionListener.onAllow(requestCode);
        } else
            permissionListener.onDeny(requestCode);
    }

}