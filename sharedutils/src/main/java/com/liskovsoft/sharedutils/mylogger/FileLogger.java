package com.liskovsoft.sharedutils.mylogger;

import android.content.Context;
import android.os.Handler;
import com.liskovsoft.sharedutils.R;
import com.liskovsoft.sharedutils.helpers.AppInfoHelpers;
import com.liskovsoft.sharedutils.helpers.FileHelpers;
import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.sharedutils.helpers.MessageHelpers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

class FileLogger extends MyLogger {
    private final Context mContext;
    private final String mCustomLabel;
    private final MyLogger mFallbackLogger;
    private BufferedWriter mWriter;
    private Handler mHandler;
    private static final long FLUSH_TIME_MS = 1_000;

    public FileLogger(Context context, String customLabel) {
        mContext = context;
        mCustomLabel = customLabel;
        mFallbackLogger = new SystemLogger();

        startWatchDog();

        MessageHelpers.showLongMessage(
                mContext,
                mContext.getString(R.string.log_stored_in_path, getLogPath(mContext)));
    }

    @Override
    public void d(String tag, String msg) {
        append(String.format("DEBUG: %s: %s", tag, msg));
        mFallbackLogger.d(tag, msg);
    }

    @Override
    public void i(String tag, String msg) {
        append(String.format("INFO: %s: %s", tag, msg));
        mFallbackLogger.i(tag, msg);
    }

    @Override
    public void w(String tag, String msg) {
        append(String.format("WARN: %s: %s", tag, msg));
        mFallbackLogger.w(tag, msg);
    }

    @Override
    public void e(String tag, String msg) {
        append(String.format("ERROR: %s: %s", tag, msg));
        mFallbackLogger.e(tag, msg);
    }

    private void append(String text) {
        try {
            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = getWriter();
            buf.append(text);
            buf.newLine();
        } catch (IOException | NullPointerException e) {
            MessageHelpers.showMessage(mContext, "Can't initialize log file " + e.getMessage());
            e.printStackTrace();
        }
    }

    private BufferedWriter getWriter() throws IOException {
        if (mWriter == null) {
            File logFile = getLogFile(mContext);

            if (!logFile.exists()) {
                logFile.createNewFile();
            }

            //BufferedWriter for performance, true to set append to file flag
            mWriter = new BufferedWriter(new FileWriter(logFile, false));
            writeLogHeader();

            MessageHelpers.showLongMessage(mContext, mContext.getString(R.string.log_to_file_started, getLogPath(mContext)));
        }

        return mWriter;
    }

    private static String getLogPath(Context context) {
        return getLogFile(context).toString();
    }

    private static File getLogFile(Context context) {
        return new File(FileHelpers.getCacheDir(context), "log.txt");
    }

    private void writeLogHeader() {
        String time = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.US).format(new Date());
        String version = String.format("%s (%s)", AppInfoHelpers.getAppVersionName(mContext), mCustomLabel);
        String deviceName = Helpers.getDeviceName();
        append("----------------------------------------------------");
        append("----------- STARTING LOG");
        append("----------- " +     time);
        append("----------- " +     version);
        append("----------- " +     deviceName);
        append("----------------------------------------------------");
    }

    private void writeLogcatHeader() {
        append("---------------------------------------");
        append("------- STARTING LOGCAT DUMP ----------");
        append("---------------------------------------");
    }

    private void writeLogcatFooter() {
        append("---------------------------------------");
        append("-------- ENDING LOGCAT DUMP -----------");
        append("---------------------------------------");
    }

    private void dumpLogcat() {
        writeLogcatHeader();

        try {
            BufferedReader bufferedReader = Helpers.exec("logcat", "-d"); // dump logcat content

            String line;
            while ((line = bufferedReader.readLine()) != null){
                append(line);
            }

            Helpers.exec("logcat", "-c"); // clear logcat
        } catch (IOException e) {
            e.printStackTrace();
        }

        writeLogcatFooter();
    }

    @Override
    public void flush() {
        if (mWriter != null) {
            try {
                dumpLogcat();
                mWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getLogType() {
        return Log.LOG_TYPE_FILE;
    }

    private void startWatchDog() {
        if (mHandler == null) {
            mHandler = new Handler();
        }

        mHandler.postDelayed(() -> {this.flush(); startWatchDog();}, FLUSH_TIME_MS);
    }
}
