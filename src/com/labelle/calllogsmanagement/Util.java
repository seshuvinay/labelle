package com.labelle.calllogsmanagement;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.StrictMode;

public class Util {
	Context ctx;
	private SharedPreferences sharedPreferences;

	public Util(Context ctx) {
		this.ctx = ctx;
		sharedPreferences = ctx.getApplicationContext().getSharedPreferences(
				"MyPref", 0);
	}

	public boolean isInternetAvailable() {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		try {
			InetAddress ipAddr = InetAddress.getByName("labelle.in");

			if (ipAddr.equals("")) {
				return false;
			} else {
				return true;
			}

		} catch (Exception e) {
			return false;
		}

	}

	public void sendSMSToServer(String message, String number, String time,
			String type, String id) throws NameNotFoundException {

		AsyncHttpClient client = new AsyncHttpClient();
		sharedPreferences = ctx.getApplicationContext().getSharedPreferences(
				"MyPref", 0);
		RequestParams params = new RequestParams();
		params.put("message", message);
		params.put("number", number);
		params.put("sms_time", time);
		params.put("sms_date", getDate(time));
		params.put("version", getVersion());
		params.put("sms_id", id);
		params.put("type", type);
		params.put("mynumber", sharedPreferences.getString("adminId", ""));
		client.post("http://www.labelle.in/Android/save_sms.php", params,
				new AsyncHttpResponseHandler() {
					@Override
					@Deprecated
					public void onSuccess(String content) {
						DBAdapter dba = new DBAdapter(ctx);
						dba.open();
						// TODO delete
						JSONObject obj;
						try {
							obj = new JSONObject(content);
							dba.deleteSms(obj.getString("sms_id"));
						} catch (Exception e) {
							e.printStackTrace();
						}

						dba.close();
						super.onSuccess(content);
					}

				});

	}

	public void saveSMSToDB(String message, String number, String time,
			String type) {
		DBAdapter dba = new DBAdapter(ctx);
		dba.open();
		dba.insertSMS(message, type, number, time);
		dba.close();
	}

	public void saveCallToDB(String number, String duration, String time,
			String type, String recordingFileName) {
		DBAdapter dba = new DBAdapter(ctx);
		dba.open();
		dba.insertCall(duration, type, number, time, recordingFileName);
		dba.close();
	}

	public void sendCallToServer(String number, String duration, String time,
			String type, String recordingFileName, String id)
			throws NameNotFoundException {
		sharedPreferences = ctx.getApplicationContext().getSharedPreferences(
				"MyPref", 0);
		AsyncHttpClient client = new AsyncHttpClient();
		File myFile = new File(recordingFileName);
		RequestParams params = new RequestParams();
		params.put("duration", getDurationBreakdown(Long.parseLong(duration)));
		params.put("number", number);
		params.put("call_time", time);
		params.put("call_date", getDate(time));
		params.put("type", type);
		params.put("call_id", id);
		params.put("version", getVersion());
		params.put("mynumber", sharedPreferences.getString("adminId", ""));
		try {
			params.put("file", myFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		client.post("http://www.labelle.in/Android/save_call.php", params,
				new AsyncHttpResponseHandler() {
					@Override
					@Deprecated
					public void onSuccess(String content) {
						DBAdapter dba = new DBAdapter(ctx);
						dba.open();
						// TODO delete
						JSONObject obj;
						try {
							obj = new JSONObject(content);
							dba.deleteCall(obj.getString("call_id"));
							
						} catch (Exception e) {
							e.printStackTrace();
						}

						dba.close();
						super.onSuccess(content);
					}

				});
	}

	private String getDate(String time) {
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat(
				"EEE MMM dd HH:mm:ss zzz yyyy");
		return format.format(date);
	}

	private String getVersion() throws NameNotFoundException {
		PackageInfo pInfo = ctx.getPackageManager().getPackageInfo(
				ctx.getPackageName(), 0);
		return pInfo.versionName;

	}

	public void sendWifiStateToServer(String connected, String time, String id) {
		sharedPreferences = ctx.getApplicationContext().getSharedPreferences(
				"MyPref", 0);
		AsyncHttpClient client = new AsyncHttpClient();

		RequestParams params = new RequestParams();

		params.put("wifi", connected);
		params.put("submit_time", time);
		params.put("mynumber", sharedPreferences.getString("adminId", ""));

		params.put("wifi_id", id);
		client.post("http://www.labelle.in/Android/save_wifi.php", params,
				new AsyncHttpResponseHandler() {

					@Override
					@Deprecated
					public void onSuccess(String content) {
						DBAdapter dba = new DBAdapter(ctx);
						dba.open();
						// TODO delete
						JSONObject obj;
						try {
							obj = new JSONObject(content);
							dba.deleteWifiStatus(obj.getString("wifi_id"));
						} catch (Exception e) {
							e.printStackTrace();
						}

						dba.close();
						super.onSuccess(content);
					}

				});
	}

	public void saveWifiStateToDB(String connected, String time) {
		DBAdapter dba = new DBAdapter(ctx);
		dba.open();
		dba.insertWifiStatus(connected, time);
		dba.close();
	}

	/**
	 * Convert a millisecond duration to a string format
	 * 
	 * @param millis
	 *            A duration to convert to a string form
	 * @return A string of the form "X Days Y Hours Z Minutes A Seconds".
	 */
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressLint("InlinedApi")
	public static String getDurationBreakdown(long millis) {
		if (millis < 0) {
			throw new IllegalArgumentException(
					"Duration must be greater than zero!");
		}

		long days = TimeUnit.MILLISECONDS.toDays(millis);
		millis -= TimeUnit.DAYS.toMillis(days);
		long hours = TimeUnit.MILLISECONDS.toHours(millis);
		millis -= TimeUnit.HOURS.toMillis(hours);
		long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
		millis -= TimeUnit.MINUTES.toMillis(minutes);
		long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

		StringBuilder sb = new StringBuilder(64);
		sb.append(days);
		sb.append("  ");
		sb.append(hours);
		sb.append(" : ");
		sb.append(minutes);
		sb.append(" : ");
		sb.append(seconds);
		sb.append(" ");

		return (sb.toString());
	}

}
