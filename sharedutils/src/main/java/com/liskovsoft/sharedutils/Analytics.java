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
    private final static String ACTION_YOUTUBE_ACTIVITY_STARTED = "ACTION_YOUTUBE_ACTIVITY_STARTED";
    private final static String ACTION_YOUTUBE_VIDEO_START = "ACTION_YOUTUBE_VIDEO_START";
    private final static String ACTION_YOUTUBE_VIDEO_START_ERROR = "ACTION_YOUTUBE_VIDEO_START_ERROR";
    private static final String ACTION_APP_UNCAUGHT_EXCEPTION = "ACTION_APP_UNCAUGHT_EXCEPTION";

    public static void sendActivityStarted(@NonNull Context context, String activityName) {
        Log.d("Analytics", "sendActivityStarted: " + activityName);
        final Intent intent = createIntent(ACTION_YOUTUBE_ACTIVITY_STARTED, getPackageInfo(context));
        intent.putExtra("activity_name", activityName);
        context.sendBroadcast(intent);
    }

    public static void sendVideoStart(@NonNull Context context, @NonNull String videoId, String videoName) {
        Log.d(TAG, "sendVideoStart() called with: context = [" + context + "], videoId = [" + videoId + "], videoName = [" + videoName + "]");
        final Intent intent = createIntent(ACTION_YOUTUBE_VIDEO_START, getPackageInfo(context));
        intent.putExtra("video_id", videoId);
        intent.putExtra("video_name", videoName);
        context.sendBroadcast(intent);
    }

    public static void sendVideoStartError(@NonNull Context context, @NonNull String videoId, String videoName, String errorMessage) {
        Log.d(TAG, "sendVideoStartError() called with: context = [" + context + "], videoId = [" + videoId + "], videoName = [" + videoName + "], errorMessage = [" + errorMessage + "]");
        final Intent intent = createIntent(ACTION_YOUTUBE_VIDEO_START_ERROR, getPackageInfo(context));
        intent.putExtra("video_id", videoId);
        intent.putExtra("video_name", videoName);
        intent.putExtra("error_message", errorMessage);
        context.sendBroadcast(intent);
    }

    public static void sendAppCrash(@NonNull Context context, String name, String trace, String log) {
        Intent intent = createIntent(ACTION_APP_UNCAUGHT_EXCEPTION, getPackageInfo(context));
        intent.putExtra("trace", trace);
        intent.putExtra("name", name);
        intent.putExtra("logs", log);
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

    private static Intent createIntent(String actionName, PackageInfo packageInfo) {
        Intent intent = new Intent(actionName);
        intent.putExtra("app_name", packageInfo.packageName);
        intent.putExtra("app_version_name", packageInfo.versionName);
        intent.putExtra("app_version_code", packageInfo.versionCode);
        return intent;
    }
}