package com.chauk.masbahati.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.chauk.masbahati.R;

import java.util.ArrayList;

/**
 * Created by delaroy on 3/21/17.
 */
public class Methods {

    public void setColorTheme(){

        switch (Constant.color){
            case 0xffF44336:
                Constant.theme = R.style.AppTheme_red;
                break;
            case 0xffE91E63:
                Constant.theme = R.style.AppTheme_pink;
                break;
            case 0xff9C27B0:
                Constant.theme = R.style.AppTheme_darpink;
                break;
            case 0xff673AB7:
                Constant.theme = R.style.AppTheme_violet;
                break;
            case 0xff2196F3:
                Constant.theme = R.style.AppTheme_indigo;
                break;
            case 0xff3F51B5:
                Constant.theme = R.style.AppTheme_blue;
                break;
            case 0xff03A9F4:
                Constant.theme = R.style.AppTheme_skyblue;
                break;
            case 0xff4CAF50:
                Constant.theme = R.style.AppTheme_green;
                break;
            case 0xffFF9800:
                Constant.theme = R.style.AppTheme_orange;
                break;
            case 0xff9E9E9E:
                Constant.theme = R.style.AppTheme_grey;
                break;
            case 0xff795548:
                Constant.theme = R.style.AppTheme_brown;
                break;
            case 0xff000000:
                Constant.theme = R.style.AppTheme_black;
                break;
            case 0xff00BCD4:
                Constant.theme = R.style.AppTheme_cyan;
                break;
            case 0xff009688:
                Constant.theme = R.style.AppTheme_teal;
                break;
            case 0xff8BC34A:
                Constant.theme = R.style.AppTheme_lightgreen;
                break;
            case 0xffCDDC39:
                Constant.theme = R.style.AppTheme_lime;
                break;
            case 0xffFFEB3B:
                Constant.theme = R.style.AppTheme_yellow;
                break;
            case 0xffFFC107:
                Constant.theme = R.style.AppTheme_amber;
                break;
            case 0xffFF5722:
                Constant.theme = R.style.AppTheme_deeporange;
                break;
            case 0xff607D8B:
                Constant.theme = R.style.AppTheme_bluegrey;
                break;
            default:
                Constant.theme = R.style.AppTheme;
                break;
        }
    }

    public static String getVersion(Context context){
        PackageInfo pinfo = null;
        try {
            pinfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
//        int versionNumber = pinfo.versionCode;
        return pinfo != null ? pinfo.versionName : null;
    }

    public static boolean isNetworkAvailable(Activity activity) {
        ConnectivityManager connectivity = (ConnectivityManager) activity
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static ArrayList<String> getListColor(){
        ArrayList<String> listColor = new ArrayList<>();
        listColor.add("#F44336");
        listColor.add("#E91E63");
        listColor.add("#9C27B0");
        listColor.add("#673AB7");
        listColor.add("#3F51B5");
        listColor.add("#2196F3");
        listColor.add("#03A9F4");
        listColor.add("#00BCD4");
        listColor.add("#009688");
        listColor.add("#4CAF50");
        listColor.add("#8BC34A");
        listColor.add("#CDDC39");
        listColor.add("#FFEB3B");
        listColor.add("#FFC107");
        listColor.add("#FF9800");
        listColor.add("#FF5722");
        listColor.add("#795548");
        listColor.add("#9E9E9E");
        listColor.add("#607D8B");
        listColor.add("#000000");
        return listColor;
    }

    public static int convertPixelsToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    public void getPrayerTime(){
//        OkHttpClient client = new OkHttpClient();
//
//        Request request = new Request.Builder()
//                .url("https://muslimsalat.p.rapidapi.com/beirut.json")
//                .get()
//                .addHeader("x-rapidapi-host", "muslimsalat.p.rapidapi.com")
//                .addHeader("x-rapidapi-key", "9b06f4c72amsh80e31d0283b10aap135c9ajsna4324e4819b5")
//                .build();
    }
}
