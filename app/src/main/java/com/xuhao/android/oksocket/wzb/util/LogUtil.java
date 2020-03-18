package com.xuhao.android.oksocket.wzb.util;

import android.util.Log;

/**
 * @author wzb<wangzhibin_x@qq.com>
 * @date May 8, 2017 2:52:59 PM
 */
public class LogUtil {

	/**
	 * 打印开关
	 */
	private static boolean logSwitch = false;
	/**
	 * 打印标记
	 */
	private static String printFlag = "wzb =====> ";

	public static void openLog() {
		logSwitch = true;
	}

	public static void closeLog() {
		logSwitch = false;
	}

	public static boolean isOpenLog() {
		return logSwitch;
	}

	public static void logDebugMessage(String message) {
		if (logSwitch) {
			Log.i("debug", printFlag + message);
		}
	}

	public static void logMessage(String tag, String message) {
		if (logSwitch) {
			Log.i(tag, printFlag + message);
		}
	}

	public static void logSystemMessage(String message) {
		if (logSwitch) {
			Log.i("system", printFlag + message);
		}
	}

	public static void logErrorMessage(String error) {
		if (logSwitch) {
			Log.i("error", printFlag + error);
			Log.e("error", printFlag + error);
		}
	}

	public static void logErrorMessage(String tag, String error, Throwable e) {
		if (logSwitch) {
			Log.e(tag, printFlag + error, e);
		}
	}

}
