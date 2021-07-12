package com.liskovsoft.sharedutils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;

import com.liskovsoft.sharedutils.mylogger.Log;

import java.util.Locale;

public class Analytics {
    private static final String TAG = "Analytics";
    private final static String ACTION_ACTIVITY_STARTED = "ACTION_ACTIVITY_STARTED";
    private final static String ACTION_YOUTUBE_VIDEO_START = "ACTION_YOUTUBE_VIDEO_START";
    private final static String ACTION_YOUTUBE_VIDEO_START_ERROR = "ACTION_YOUTUBE_VIDEO_START_ERROR";


    public static void sendActivityStarted(@NonNull Context context, String activityName) {
        Log.d("Analytics", "sendActivityStarted: " + activityName);
        PackageInfo packageInfo = getPackageInfo(context);
        Intent intent = new Intent(ACTION_ACTIVITY_STARTED);
        intent.putExtra("app_name", packageInfo.packageName);
        intent.putExtra("app_version",
                String.format(Locale.US, "%s-%d",
                        packageInfo.versionName,
                        packageInfo.versionCode));
        intent.putExtra("activity_name", activityName);
        context.sendBroadcast(intent);
    }

    public static void sendVideoStart(@NonNull Context context, @NonNull String videoId, String videoName) {
        android.util.Log.d(TAG, "sendVideoStart() called with: context = [" + context + "], videoId = [" + videoId + "], videoName = [" + videoName + "]");
        PackageInfo packageInfo = getPackageInfo(context);
        Intent intent = new Intent(ACTION_YOUTUBE_VIDEO_START);
        intent.putExtra("app_name", packageInfo.packageName);
        intent.putExtra("app_version",
                String.format(Locale.US, "%s-%d",
                        packageInfo.versionName,
                        packageInfo.versionCode));
        intent.putExtra("video_id", videoId);
        intent.putExtra("video_name", videoName);
        context.sendBroadcast(intent);
    }

    public static void sendVideoStartError(@NonNull Context context, @NonNull String videoId, String videoName, String errorMessage) {
        android.util.Log.d(TAG, "sendVideoStartError() called with: context = [" + context + "], videoId = [" + videoId + "], videoName = [" + videoName + "], errorMessage = [" + errorMessage + "]");
        PackageInfo packageInfo = getPackageInfo(context);
        Intent intent = new Intent(ACTION_YOUTUBE_VIDEO_START_ERROR);
        intent.putExtra("app_name", packageInfo.packageName);
        intent.putExtra("app_version",
                String.format(Locale.US, "%s-%d",
                        packageInfo.versionName,
                        packageInfo.versionCode));
        intent.putExtra("video_id", videoId);
        intent.putExtra("video_name", videoName);
        intent.putExtra("error_message", errorMessage);
        context.sendBroadcast(intent);
    }

    private static PackageInfo getPackageInfo(@NonNull Context context) {
        PackageInfo packageInfo = new PackageInfo();
        try {
            packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageInfo;
    }
}