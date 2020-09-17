package com.chauk.masbahati.utils;

import android.app.Activity;
import android.content.pm.ActivityInfo;

/**
 * Created by Chauk 2020
 */
public class ScreenUtils {
    /**
     * Locks the device window in actual screen mode.
     */
    public static void lockOrientation(Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
    }

    /**
     * Unlocks the device window in user defined screen mode.
     */
    public static void unlockOrientation(Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
    }

}