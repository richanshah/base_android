package com.example.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ApplicationClass;
import com.example.R;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import static org.apache.commons.codec.binary.Hex.encodeHex;

public class Utils {

    public static void setLocale(Activity activity, String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        activity.getBaseContext().getResources().updateConfiguration(config,
                activity.getBaseContext().getResources().getDisplayMetrics());
    }

    public static String encode(String data) {
        Mac sha256_HMAC;
        String s = "";
        try {
            sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec("}{(*%$}&*^\\({#$%&@}^*$#{?}@^%#)(^%&".getBytes("UTF-8"), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            byte digest[] = sha256_HMAC.doFinal(data.getBytes("UTF-8"));
            s = new String(encodeHex(digest));
        } catch (NoSuchAlgorithmException | InvalidKeyException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return s;
    }

    public static boolean isConnectingToInternet(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Network[] networks = connectivityManager.getAllNetworks();
            NetworkInfo networkInfo;
            for (Network mNetwork : networks) {
                networkInfo = connectivityManager.getNetworkInfo(mNetwork);
                if (networkInfo.getState().equals(NetworkInfo.State.CONNECTED)) {
                    Logger.d("Network", "NETWORK NAME: " + networkInfo.getTypeName());
                    return true;
                }
            }
        } else {
            if (connectivityManager != null) {
                NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
                if (info != null) {
                    for (NetworkInfo anInfo : info) {
                        if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                            Logger.d("Network", "NETWORK NAME: " + anInfo.getTypeName());
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public static void ShareTheApp(Activity mActivity) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, mActivity.getResources().getString(R.string.app_name) + "\n\n" + "https://play.google.com/store/apps/details?id=" + mActivity.getPackageName());
        sendIntent.setType("text/plain");
        mActivity.startActivity(Intent.createChooser(sendIntent, mActivity.getResources().getText(R.string.share_via)));
    }

    public static void ShareInfo(Activity mActivity, String info) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, info + "\n\nDownload now at:\n" + "https://play.google.com/store/apps/details?id=" + mActivity.getPackageName());
        sendIntent.setType("text/plain");
        mActivity.startActivity(Intent.createChooser(sendIntent, mActivity.getResources().getText(R.string.share_via)));
    }

    //Image Real Path from URI
    public static String getRealPathFromURI(Context mContext, Uri contentURI) {
        String result;
//        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        String[] filePathColumn = {MediaStore.Images.ImageColumns.DATA};
        Cursor cursor = mContext.getContentResolver().query(contentURI, filePathColumn, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(filePathColumn[0]);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

  /*  public static void saveLanguageSelection(Context context, List<LanguageModel> ActivityList) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<LanguageModel>>() {
        }.getType();
        String strObject = gson.toJson(ActivityList, type);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putString("CategoryList", strObject).apply();
    }

    public static ArrayList<LanguageModel> getLanguageSelection(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        Gson gson = new Gson();
        Type type = new TypeToken<List<LanguageModel>>() {
        }.getType();

        return gson.fromJson(prefs.getString("CategoryList", ""), type);
    }

    public static void setFooterFlags(RecyclerView rvFooterFlags) {

        ParseLocalJSONFile objJsonRead = new ParseLocalJSONFile();
        ArrayList<LanguageModel> languageList = objJsonRead.getLanguagesList(ApplicationClass.getAppContext());

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(ApplicationClass.getAppContext(), LinearLayoutManager.HORIZONTAL, false);
        rvFooterFlags.setLayoutManager(layoutManager);
        rvFooterFlags.setAdapter(new FooterFlagAdapter(languageList));


    }*/

    // QR Code
   /* public static void generateQRCode(String qrpCode, AppCompatImageView iv) {
        ViewTreeObserver vto = iv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                iv.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                generateQRCode(qrpCode, iv, true);
            }
        });
    }*/

 /*   public static void generateQRCode(String qrpCode, AppCompatImageView iv, String imagePath, AppCompatImageView ivUser) {
        generateQRCode(qrpCode, iv, true);
        BaseBinder.setImageUrl(ivUser, imagePath);
        ivUser.setVisibility(View.VISIBLE);
    }

    public static void generateQRCode(String qrpCode, AppCompatImageView iv, boolean isExcludeSpace) {
        if (qrpCode != null && !qrpCode.equals("null")) {
            Logger.e("QRCode: ", qrpCode);
            QRCodeWriter writer = new QRCodeWriter();
            Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.MARGIN, 0);
//                EncryptionUtil obj = EncryptionUtil.getInstance();
//                BitMatrix bitMatrix = isExcludeSpace ? writer.encode(obj.getEncodedValue(qrpCode), BarcodeFormat.QR_CODE, iv.getWidth(), iv.getHeight(), hints) : writer.encode(obj.getEncodedValue(qrpCode), BarcodeFormat.QR_CODE, iv.getWidth(), iv.getHeight());

            BitMatrix bitMatrix = null;
            try {
                bitMatrix = isExcludeSpace ? writer.encode(CryptLib.getInstance().encryptPlainText(qrpCode), BarcodeFormat.QR_CODE, iv.getWidth(), iv.getHeight(), hints) : writer.encode(CryptLib.getInstance().encryptPlainText(qrpCode), BarcodeFormat.QR_CODE, iv.getWidth(), iv.getHeight());
//                bitMatrix = isExcludeSpace ? writer.encode(obj.encryptPlainTextWithRandomIV(qrpCode, "(Black{Pearl}_Jack%Sparrow!)~2@03"), BarcodeFormat.QR_CODE, iv.getWidth(), iv.getHeight(), hints) : writer.encode(obj.encryptPlainTextWithRandomIV(qrpCode, "(Black{Pearl}_Jack%Sparrow!)~2@03"), BarcodeFormat.QR_CODE, iv.getWidth(), iv.getHeight());
            } catch (Exception e) {
                e.printStackTrace();
            }

            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++) {
                int offset = y * width;
                for (int x = 0; x < width; x++) {
                    pixels[offset + x] = bitMatrix.get(x, y) ? ContextCompat.getColor(ApplicationClass.getAppContext(), R.color.blue) : WHITE;
                }
            }

            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);

            iv.setImageBitmap(bitmap);
        }
    }
*/
    public static void setCurrentDate(AppCompatTextView tvDay, AppCompatTextView tvDate) {
        String[] strDate = TimeStamp.getCurrentDateElements();
        tvDay.setText(strDate[0]);
        tvDate.setText(strDate[1]);
    }

  /*  public static void setCurrentDateAndFlag(AppCompatTextView tvDay, AppCompatTextView tvDate, AppCompatImageView ivFlag, boolean isFlagged) {
        setCurrentDate(tvDay, tvDate);

        if (isFlagged) {
            ivFlag.setImageResource(R.drawable.ic_vector_flag_red);
        }
    }*/

    /*public static BitmapDescriptor BitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        assert vectorDrawable != null;
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }*/

    public static String generateToken() {

        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        String currentDate = dateFormat.format(calendar.getTime());

        String tokenParams = "275229046203849302520".concat(currentDate);

        Logger.e("tokenParams: " + tokenParams);

        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest
                    .getInstance(MD5);
            digest.update(tokenParams.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            Logger.e("generateToken: " + hexString.toString());
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getFormattedNumber(String amount) {
        return getFormattedNumber(amount, true);
    }

    public static String getFormattedNumber(double amount) {
        return getFormattedNumber(amount, true);
    }

    public static String getFormattedNumber(String amount, boolean appendSymbol) {
        if (amount != null && amount.length() > 0) {
            try {
                double value = Double.parseDouble(amount);
                return getFormattedNumber(value, appendSymbol);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return "";
            }
        } else {
            return "";
        }
    }

    public static String getFormattedNumber(double amount, boolean appendSymbol) {
        SessionManager session = new SessionManager(ApplicationClass.getAppContext());
        DecimalFormat formatter = new DecimalFormat("0.00");
        return appendSymbol ? session.getDataByKey(SessionManager.KEY_CURRENCY, "$").concat(formatter.format(amount)) : formatter.format(amount);
    }

    public static void openDirectionInGoogleMap(Context context, double latitude, double longitude) {
        String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%f,%f", latitude, longitude);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        context.startActivity(mapIntent);
    }

    public static void openLinkInBrowser(Context mContext, String url) {
        if (!url.startsWith("http://") && !url.startsWith("https://") && url.contains("."))
            url = "http://" + url;
        if (url.length() > 0 && isValidUrl(url)) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            mContext.startActivity(browserIntent);
        } else {
            Toast.makeText(mContext, R.string.no_valid_url_available, Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isValidUrl(String url) {
        if (!url.startsWith("http://") && !url.startsWith("https://") && url.contains("."))
            url = "http://" + url;
        Pattern p = Pattern.compile("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");
        Matcher m = p.matcher(url.toLowerCase());
        return m.matches();
    }

  /*  public static String getWeekAndDayOfAd(float budget, float fee) {
        String weekAndDay = "";
        float weeks;
        if (budget > 0) {
            weeks = budget / fee;
            if (budget < fee) {
                weekAndDay = ApplicationClass.getAppContext().getResources().getString(R.string.lessthan_a_week);
            } else {
                int week = (int) weeks;
                int days = (int) (((weeks * 100) % 100) * 7 / 100);
                if (days == 1) {
                    weekAndDay = ApplicationClass.getAppContext().getString(week > 1 ? R.string.weeks_and_day : R.string.week_and_day, String.valueOf(week), String.valueOf(days));
                } else if (days > 1) {
                    weekAndDay = ApplicationClass.getAppContext().getString(week > 1 ? R.string.weeks_and_days : R.string.week_and_days, String.valueOf(week), String.valueOf(days));
                } else {
                    weekAndDay = ApplicationClass.getAppContext().getString(week > 1 ? R.string._weeks : R.string._week, String.valueOf(week));
                }
            }
        }
        return weekAndDay;
    }*/


    public static void setVectorForPreLollipop(TextView textView, int resourceId, Context activity, int position) {
        Drawable icon;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            icon = VectorDrawableCompat.create(activity.getResources(), resourceId, activity.getTheme());
        } else {
            icon = activity.getResources().getDrawable(resourceId, activity.getTheme());
        }
        switch (position) {
            case AppConstants.DRAWABLE_LEFT:
                textView.setCompoundDrawablesWithIntrinsicBounds(icon, null, null,
                        null);
                break;

            case AppConstants.DRAWABLE_RIGHT:
                textView.setCompoundDrawablesWithIntrinsicBounds(null, null, icon,
                        null);
                break;

            case AppConstants.DRAWABLE_TOP:
                textView.setCompoundDrawablesWithIntrinsicBounds(null, icon, null,
                        null);
                break;

            case AppConstants.DRAWABLE_BOTTOM:
                textView.setCompoundDrawablesWithIntrinsicBounds(null, null, null,
                        icon);
                break;
        }
    }

}