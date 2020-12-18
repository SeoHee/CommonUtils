package kyowon.co.kr.lib.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by 29074 on 2018-04-11.
 */

public class CommonUtil {

    private static final String TAG = CommonUtil.class.getSimpleName();

    public static void getHashKey(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e(TAG, "MY KEY HASH : " + Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private static Toast toast;

    public static void showToast(Context context, int bgResId, String message) {
        if (toast != null) {
            toast.cancel();
        }

        toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);

        int padding = (int) context.getResources().getDimension(R.dimen.px40);

        View view = toast.getView();
        if (bgResId != -1) {
            view.setBackgroundResource(bgResId);
        }
        view.setPadding(padding, padding, padding, padding);

        TextView txtMessage = (TextView) view.findViewById(android.R.id.message);
        txtMessage.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.px28));
//        txtMessage.setTextColor(ContextCompat.getColor(context, R.color.white));
        txtMessage.setText(message);
        txtMessage.setGravity(Gravity.CENTER);

        toast.show();
    }

//    /**
//     * wifi, 3g, lte 체크
//     */
//    public static boolean isNetworkConnected(Context context) {
//        boolean state = false;
//
//        ConnectivityManager cManager = null;
//        NetworkInfo wifi;
//
//        try {
//            cManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//            NetworkInfo info = cManager.getActiveNetworkInfo();
//
//            state = info != null && info.isConnected();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        cManager = null;
//
//        return state;
//    }
//
//    /**
//     * 와이파이 연결인지 확인
//     *
//     * @param context
//     * @return
//     */
//    public static boolean isWifiConnected(Context context) {
//        //For WiFi Check
//        ConnectivityManager cManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        return cManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
//    }

    /**
     * 네트워크 설정 화면으로 이동
     */
    public static void goToNetworkSetting(Context ctx) {
        Intent intent = new Intent("android.net.wifi.PICK_WIFI_NETWORK");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(intent);
    }

    /**
     * 구글 스토어 연결
     */
    public static void goToPlayStore(Context context, String pkgName) {
        try {
            Uri uri = Uri.parse("market://details?id=" + pkgName);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            //구글 플레이스토어 앱이 단말기 내에 존재하지 않을 경우 예외처리
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + pkgName));
            context.startActivity(intent);
        }
    }

    /**
     * 단말기 소프트웨어 버전정보 반환
     */
    public static String getDeviceSdkVersion() {
        return Build.VERSION.RELEASE;
    }


    /**
     * 현재 앱의 패키지명 가져오기
     */
    public static String getMyPackageName() {
        return "";//Todo
    }

    /**
     * app version name
     */
    public static String getVersionName(Context context, String packageName) {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(packageName, 0);
            return pi.versionName;
        } catch (NameNotFoundException e) {
            return "";
        }
    }

    /**
     * app version name
     */
    public static int getVersionCode(Context context, String packageName) {
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(packageName, 0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                return (int) pInfo.getLongVersionCode(); // avoid huge version numbers and you will be ok
            } else {
                //noinspection deprecation
                return pInfo.versionCode;
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * app version check
     */
    public static int compareVersion(String serverVer, String currentVer) {
        if (serverVer == null) {
            return -1;
        }

        if (currentVer == null) {
            return 1;
        }

        String[] serverVers = serverVer.split("[,\\.]");
        String[] currentVers = currentVer.split("[,\\.]");

        if (serverVers.length != currentVers.length) {
            return 1;
        }

        for (int i = 0; i < serverVers.length; i++) {
            String sv = serverVers[i];
            String cv = currentVers[i];

            if (isDigit(sv) && isDigit(cv)) {
                int svi = Integer.parseInt(sv);
                int cvi = Integer.parseInt(cv);

                if (svi != cvi) {
                    return svi > cvi ? 1 : -1;
                }
            }
            if (!sv.equals(cv)) {
                return 1;
            }
        }
        return 0;
    }

    public static boolean isDigit(String item) {
        return item.matches("\\d+");
    }

    /**
     * 단말기에 설치된 앱 있는지 여부 반환
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isPackageExisted(Context context, String packageName) {
        List<ApplicationInfo> packages = context.getPackageManager().getInstalledApplications(0);
        if (packages != null && packages.size() > 0) {
            for (ApplicationInfo appInfo : packages) {
                if (appInfo.packageName.equals(packageName) && appInfo.enabled) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 키패드 내리기 수행
     *
     * @param context
     * @param editTextView
     */
    public static void hideKeyPad(Context context, EditText editTextView) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editTextView.getWindowToken(), 0);
    }

    /**
     * 데이터 포맷 변형 후 반환
     *
     * @param value               변형 전 데이터 (1988.01.05)
     * @param orgFormatPattern    변형 전 데이터 포맷 (yyyy.MM.dd)
     * @param resultFormatPattern 변형 후 데이터 포맷 (MM.dd)
     * @return 변경 후 데이터 반환 (01.05)
     */
    public static String convertDateFormat(String value, String orgFormatPattern, String resultFormatPattern) {
        SimpleDateFormat orgDateFormat = new SimpleDateFormat(orgFormatPattern);
        SimpleDateFormat convertFormat = new SimpleDateFormat(resultFormatPattern, Locale.KOREAN);

        String resultDate = "";
        try {
            Date orgDate = orgDateFormat.parse(value);

            String convertStartString = convertFormat.format(orgDate);
            resultDate = convertStartString;
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultDate;
    }

    public static int getDpi(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return (int) (metrics.density * 160f);
    }

    /**
     * 스테이터스 바 배경 색상 변경
     *
     * @param activity
     * @param color
     */
    public static void changeStatusBarColor(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(activity.getResources().getColor(color));
        }
    }

    public static void setClipboard(Context context, String copyText) {
        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText("47코드", copyText);
        clipboard.setPrimaryClip(clip);
    }

    public static int getDrawableRscLength(Context context, int rscId, boolean isWidth) {
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), rscId, bitmapOptions);
        int w = bitmapOptions.outWidth;
        int h = bitmapOptions.outHeight;

        return isWidth ? w : h;
    }

    public static void startBrowser(Context context, String url) {
        try {
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                url = "http://" + url;
            }

            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            context.startActivity(i);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static int getDisplayLength(Activity activity, boolean isWidth) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return isWidth ? size.x : size.y;
    }

    public static String getDeviceUniqueKey(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }
}
