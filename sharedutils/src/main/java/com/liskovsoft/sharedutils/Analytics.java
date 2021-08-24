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
    private final static String ACTION_YOUTUBE_VIDEO_STARTING = "ACTION_YOUTUBE_VIDEO_STARTING";
    private final static String ACTION_YOUTUBE_VIDEO_STARTED = "ACTION_YOUTUBE_VIDEO_STARTED";
    private final static String ACTION_YOUTUBE_VIDEO_START_ERROR = "ACTION_YOUTUBE_VIDEO_START_ERROR";
    private static final String ACTION_APP_UNCAUGHT_EXCEPTION = "ACTION_APP_UNCAUGHT_EXCEPTION";
    private static PackageInfo sPackageInfo = null;
    private static Context sContext = null;

    public static void init(@NonNull Context context) {
        sContext = context;
        sPackageInfo = getPackageInfo(context);
    }

    public static void sendActivityStarted(String activityName) {
        final Intent intent = createIntent(ACTION_YOUTUBE_ACTIVITY_STARTED, sPackageInfo);
        intent.putExtra("activity_name", activityName);
        sContext.sendBroadcast(intent);
    }

    public static void sendVideoStarting(@NonNull String videoId, String videoName) {
        final Intent intent = createIntent(ACTION_YOUTUBE_VIDEO_STARTING, sPackageInfo);
        intent.putExtra("video_id", videoId);
        intent.putExtra("video_name", videoName);
        sContext.sendBroadcast(intent);
    }

    public static void sendVideoStarted(@NonNull String videoId, String videoName) {
        final Intent intent = createIntent(ACTION_YOUTUBE_VIDEO_STARTED, sPackageInfo);
        intent.putExtra("video_id", videoId);
        intent.putExtra("video_name", videoName);
        sContext.sendBroadcast(intent);
    }

    public static void sendVideoStartError(@NonNull String videoId, String videoName, String errorMessage) {
        final Intent intent = createIntent(ACTION_YOUTUBE_VIDEO_START_ERROR, sPackageInfo);
        intent.putExtra("video_id", videoId);
        intent.putExtra("video_name", videoName);
        intent.putExtra("error_message", errorMessage);
        sContext.sendBroadcast(intent);
    }

    public static void sendAppCrash(String name, String trace, String log) {
        Intent intent = createIntent(ACTION_APP_UNCAUGHT_EXCEPTION, sPackageInfo);
        intent.putExtra("trace", trace);
        intent.putExtra("name", name);
        intent.putExtra("logs", log);
        sContext.sendBroadcast(intent);
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