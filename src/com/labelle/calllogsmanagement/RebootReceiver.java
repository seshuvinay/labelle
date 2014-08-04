package com.labelle.calllogsmanagement;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class RebootReceiver extends BroadcastReceiver {
	Context ctx;
	String mobileNo;
	SharedPreferences sharedPreferences;
	SharedPreferences.Editor editor;
	TelephonyManager telManager;
	int callState;

	@Override
	public void onReceive(Context context, Intent intent) {

		this.ctx = context;
		telManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);

		sharedPreferences = ctx.getApplicationContext().getSharedPreferences(
				"MyPref", 0);
		mobileNo = sharedPreferences.getString("adminId", "");
		if (mobileNo == "") {
			Toast.makeText(ctx.getApplicationContext(),
					"Please login to Voice Recorder Application",
					Toast.LENGTH_SHORT).show();
		} else {

			scheduleAlarm();
		}

	}

	public void scheduleAlarm() {
		Intent intentAlarm = new Intent(ctx, UploadService.class);
		ctx.startService(intentAlarm);

	}

}
