package com.example.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;

import android.util.Base64;
import android.widget.Toast;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import com.example.R;
import com.example.ui.base.BaseBinder;
import com.example.webservice.ResponseUtils;
import com.google.gson.JsonObject;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.AnyRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageUtils {

    public static final int IMAGE_PICKER_SELECT = 1001;
    public static final int REQUEST_IMAGE_CAPTURE = 1002;
    private static Uri mImageCaptureUri;
    private static OnPickPhoto onPickPhoto;
    private static Activity activity;

    public static String ImageToBase64String(String imagePath) {
        String encodedImage = null;
        try {
            Bitmap bm = BitmapFactory.decodeFile(imagePath);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 50, baos);
            byte[] byteArrayImage = baos.toByteArray();
            encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encodedImage;
    }

    public static void selectImageDialog(final Activity activity, boolean ifRemoveShown, boolean isCover, OnPickPhoto onPick) {
        onPickPhoto = onPick;
        ImageUtils.activity = activity;

        ArrayList<String> arrPickOptions = new ArrayList<>();
        arrPickOptions.add(BaseBinder.getLabel(activity.getString(R.string.gallery)));
        arrPickOptions.add(BaseBinder.getLabel(activity.getString(R.string.camera)));

        if (ifRemoveShown)
            arrPickOptions.add(BaseBinder.getLabel(activity.getString(R.string.remove)));
        //  arrPickOptions.add(BaseBinder.getLabel(activity.getString(R.string.cancel)));

        AlertUtils.showBottomSheetListDialog(activity, arrPickOptions, null, new OnSearchableDialog() {
            @Override
            public void onItemSelected(Object o) {
                String s = (String) o;
                if (s.equalsIgnoreCase(BaseBinder.getLabel(activity.getString(R.string.gallery)))) {
                    CheckPermissionUtils.isCheck(activity, CheckPermissionUtils.READ_EXTERNAL, CheckPermissionUtils.REQUEST_CODE_READ_EXTERNAL, permissionListener);
                } else if (s.equalsIgnoreCase(BaseBinder.getLabel(activity.getString(R.string.camera)))) {
                    CheckPermissionUtils.isCheck(activity, CheckPermissionUtils.CAMERA, CheckPermissionUtils.CAMERA_CODE, permissionListener);
                } else if (s.equalsIgnoreCase(BaseBinder.getLabel(activity.getString(R.string.remove)))) {
                    //call api
                    if (isCover) {
                        removePicture(activity, "cover", isCover);
                    } else {
                        removePicture(activity, "avatar", isCover);
                    }
                } else if (s.equalsIgnoreCase(BaseBinder.getLabel(activity.getString(R.string.cancel)))) {
                }
            }
        });
    }

    public static void openGallery(final Activity activity, OnPickPhoto onPick) {
        onPickPhoto = onPick;
        ImageUtils.activity = activity;
        CheckPermissionUtils.isCheck(activity, CheckPermissionUtils.READ_EXTERNAL, CheckPermissionUtils.REQUEST_CODE_READ_EXTERNAL, permissionListener);
    }

    public static void openGalleryPhoto(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(intent, IMAGE_PICKER_SELECT);
    }

    public static void openCameraPhoto(Activity activity) {
        File mydir = new File(Environment.getExternalStorageDirectory() + "/" + activity.getString(R.string.app_name) + "/images/");
        if (!mydir.exists()) {
            mydir.mkdirs();
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N)
            mImageCaptureUri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".provider", new File(mydir.getAbsolutePath() + "/" + String.valueOf(System.currentTimeMillis() + ".jpeg")));
        else
            mImageCaptureUri = Uri.fromFile(new File(mydir.getAbsolutePath() + "/" + String.valueOf(System.currentTimeMillis() + ".jpeg")));
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
            //to open default camera app only, it's not necessary
            if (getPackageCamera(activity) != null && !getPackageCamera(activity).equalsIgnoreCase(""))
                takePictureIntent.setPackage(getPackageCamera(activity));
            activity.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
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
            case IMAGE_PICKER_SELECT:
                Uri uri = data.getData();
                processImage(context, uri, onPickPhoto, isCrop, isCircle);
                break;

            case UCrop.REQUEST_CROP:
                uri = UCrop.getOutput(data);
                if (uri != null) {
                    if (onPickPhoto != null) {
                        onPickPhoto.onPick(uri, FileUtils.getPath(context, uri));
                    }
                }
                break;
            case REQUEST_IMAGE_CAPTURE:
                uri = mImageCaptureUri;
                if (uri == null && data != null)
                    uri = data.getData();
                processImage(context, uri, onPickPhoto, isCrop, isCircle);
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

    public static void removePicture(Activity context, String remove, boolean isCover) {
        Utils.showProgressBar(context);

        JSONObject requestParams = new JSONObject();
        try {
            requestParams.put("user_id", Utils.getUserId(context));
            requestParams.put("s", Utils.getSessionValue(context));
            requestParams.put("remove_type", remove);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Utils.printLog("Request Params", requestParams.toString());
        Call<JsonObject> labelResponse = ApiClient.getService()
                .callPostMethod(BuildConfig.URL_COMMON + BaseUrl.REMOVE_COVER_AVATAR, ResponseUtils.getRequestData(requestParams.toString()));
        labelResponse.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null) {
                    Utils.printLog("Response", response.body().toString());
                    if (isCover) {
                        onPickPhoto.onPick(null, BaseUrl.COVER_URL);
                    } else {
                        onPickPhoto.onPick(null, BaseUrl.AVATAR_URL);
                    }
                }

                Utils.dismissProgressBar();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Utils.dismissProgressBar();
            }
        });

    }

    public static void saveImage(Context context, Bitmap b, File file) {
        FileOutputStream foStream;
        try {
            foStream = new FileOutputStream(file);
            b.compress(Bitmap.CompressFormat.PNG, 100, foStream);
            foStream.close();
            FileUtils.scanFile(context, file);
        } catch (Exception e) {
            MyLg.d("saveImage", "Exception 2, Something went wrong!");
            e.printStackTrace();
        }
    }

    public static void downloadImage(String url) {
        downloadImage(url, null);
    }

    public static void downloadImage(String url, CallbackTask thumbhnailCallback) {

        GlideApp.with(Cisner.getInstance())
                .asBitmap()
                .load(url)
                .into(new SimpleTarget<Bitmap>() {

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                    }

                    @SuppressLint("StaticFieldLeak")
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        if (resource != null) {
                            new AsyncTask<Void, Void, File>() {
                                @Override
                                protected File doInBackground(Void... voids) {
                                    try {
                                        File file = null;
                                        if (thumbhnailCallback != null)
                                            file = FolderUtils.getThumbnailFile();
                                        else
                                            file = FolderUtils.getImageFile();
                                        saveImage(Cisner.getInstance(), resource, file);
                                        return file;
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    return null;
                                }

                                @Override
                                protected void onPostExecute(File file) {
                                    super.onPostExecute(file);
                                    if (file == null)
                                        return;
                                    if (thumbhnailCallback != null)
                                        thumbhnailCallback.onSuccess(file);
                                    else
                                        Toast.makeText(Cisner.getInstance(), "saved", Toast.LENGTH_SHORT).show();

                                }
                            }.execute();

                        }
                    }
                });
//        GlideApp
//                .with(Cisner.getInstance())
//                .load(url)
//                .asBitmap()
//                .toBytes(Bitmap.CompressFormat.JPEG, 80)
//                .into(new SimpleTarget<byte[]>() {
//                    @Override
//                    public void onResourceReady(final byte[] resource, GlideAnimation<? super byte[]> glideAnimation) {
//                        new AsyncTask<Void, Void, Void>() {
//                            @Override
//                            protected Void doInBackground(Void... params) {
//                                File sdcard = Environment.getExternalStorageDirectory();
//                                File file = new File(sdcard + "/YourDir/imageName.jpg");
//                                File dir = file.getParentFile();
//                                try {
//                                    if (!dir.mkdirs() && (!dir.exists() || !dir.isDirectory())) {
//                                        throw new IOException("Cannot ensure parent directory for file " + file);
//                                    }
//                                    BufferedOutputStream s = new BufferedOutputStream(new FileOutputStream(file));
//                                    s.write(resource);
//                                    s.flush();
//                                    s.close();
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//                                return null;
//                            }
//                        }.execute();
//                    }
//                })
//        ;
    }

}