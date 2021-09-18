package com.example.myapplication.utils;

import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

/**
 * LogUtils common log print
 */
public class LogUtils {
    private static final String TAG_LOG = "LogUtil";

    private static final HiLogLabel LABEL_LOG = new HiLogLabel(0, 0, LogUtils.TAG_LOG);

    private static final String LOG_FORMAT = "%{public}s: %{public}s";

    private LogUtils() {
    }

    /**
     * Print error log
     *
     * @param tag log tag
     * @param msg log message
     */
    public static void error(String tag, String msg) {
        HiLog.error(LABEL_LOG, LOG_FORMAT, tag, msg);
    }
}
