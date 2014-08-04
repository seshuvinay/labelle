package com.labelle.calllogsmanagement;

import java.util.Date;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class SMSReceiver extends BroadcastReceiver {
	Context ctx;

	static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";
	static final String ACTION1 = "android.provider.Telephony.SMS_SENT";
	String mobileNo;
	Date date;

	long preferenceLongTime = 500;

	SharedPreferences sharedPreferences;
	SharedPreferences.Editor editor;
	String verficationCode;

	int smsType = 0;
	String SMSType;

	public void onReceive(Context context, Intent intent) {
		this.ctx = context;

		SMSType = intent.getAction();

		Bundle bundle = intent.getExtras();
		if (intent.getAction().equals(ACTION1)) {

			if (bundle != null) {
				smsType = 2;
				sharedPreferences = ctx.getApplicationContext()
						.getSharedPreferences("MyPref", 0);
				mobileNo = sharedPreferences.getString("adminId", "");

			}
		} else {
			Object[] pdus = (Object[]) bundle.get("pdus");
			final SmsMessage[] messages = new SmsMessage[pdus.length];
			for (int i = 0; i < pdus.length; i++) {
				messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
			}

			if (messages.length > -1) {
				String message = "";
				String number = "";
				String time = "";
				String type = "Received";
				for (SmsMessage message1 : messages) {
					message = message + message1.getMessageBody();
					number = message1.getDisplayOriginatingAddress();
					time = Long.toString(message1.getTimestampMillis());
				}
				if (!message.contains("VerficationCode")) {
					try {
						sendToserver(message, number, time, type, ctx);
					} catch (NameNotFoundException e) {
						e.printStackTrace();
					}
				} else {
					abortBroadcast();
					sharedPreferences = ctx.getApplicationContext()
							.getSharedPreferences("MyPref", 0);
					Editor edit = sharedPreferences.edit();

					edit.putString("adminId", number);
					edit.commit();
					verficationCode = sharedPreferences.getString(
							"verficationCode", "");
					if (message.split("VerficationCode:")[1]
							.equals(verficationCode)) {
						Intent callingIntent = new Intent(ctx,
								HomeActivity.class);
						callingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						callingIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

						ctx.startActivity(callingIntent);

					}

				}

			}
		}

	}

	private void sendToserver(String message, String number, String time,
			String type, Context ctx) throws NameNotFoundException {
		Util util = new Util(ctx);
		if (util.isInternetAvailable()) {
			util.sendSMSToServer(message, number, time, type, "0");
		} else {
			util.saveSMSToDB(message, number, time, type);
		}
	}

}
