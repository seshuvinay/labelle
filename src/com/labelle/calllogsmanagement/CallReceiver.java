package com.labelle.calllogsmanagement;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.http.NameValuePair;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnErrorListener;
import android.media.MediaRecorder.OnInfoListener;
import android.net.ConnectivityManager;
import android.os.Environment;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

@SuppressLint("InlinedApi")
public class CallReceiver extends BroadcastReceiver {

	private static final String AUDIO_RECORDER_FILE_EXT_3GP = ".3gp";
	private static final String AUDIO_RECORDER_FILE_EXT_MP4 = ".mp4";
	private static final String AUDIO_RECORDER_FILE_EXT_AMR = ".amr";
	SharedPreferences sharedPreferences;
	SharedPreferences.Editor editor;
	private static final String AUDIO_RECORDER_FOLDER = "Call Recordings";
	public MediaRecorder recorder = null;
	private int currentFormat = 2;

	private String file_exts[] = { AUDIO_RECORDER_FILE_EXT_3GP,
			AUDIO_RECORDER_FILE_EXT_MP4, AUDIO_RECORDER_FILE_EXT_AMR };
	TelephonyManager telManager;
	boolean recordStarted, callEndedState;
	private Context ctx;
	Context ctx2;
	Date date;
	Date callDate;

	private String callDuration = null;
	String mobileNo;
	ConnectivityManager conMgr;
	private long recordStartTime;
	PackageInfo pInfo = null;
	List<NameValuePair> submitionValuePairs = new ArrayList<NameValuePair>();
	int callWait;
	MyPhoneStateListener phoneListener;

	@Override
	public void onReceive(Context context, Intent intent) {
		ctx = context;

		if (intent.hasExtra(Intent.EXTRA_PHONE_NUMBER)
				&& !intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER).equals("")) {
			sharedPreferences = ctx.getSharedPreferences("MyPref", 0);
			editor = sharedPreferences.edit();
			editor.putString("phone",
					intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER));

			editor.commit();
		}

		intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
		if (phoneListener == null)
			phoneListener = new MyPhoneStateListener();
		TelephonyManager telephony = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		telephony.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);

	}

	public class MyPhoneStateListener extends PhoneStateListener {
		private String prevState;
		private String callType;

		private boolean recordState = false;

		public void onCallStateChanged(int state, String incomingNumber) {
			sharedPreferences = ctx.getSharedPreferences("MyPref", 0);
			prevState = sharedPreferences.getString("prev_state", "idle");

			switch (state) {
			case TelephonyManager.CALL_STATE_IDLE:
				if (prevState.equals("offhook")) {
					try {
						if (recorder != null && recordState) {
							recorder.stop();
							recorder.reset();
							recorder.release();
							recorder = null;

						}
					} catch (IllegalStateException e) {
						e.printStackTrace();
					}
					callDuration = Long.toString(System.currentTimeMillis()
							- sharedPreferences.getLong("record_start",
									System.currentTimeMillis()));
					callType = sharedPreferences.getString("call_type", "");

					String fileName = sharedPreferences.getString("file_path",
							"");
					Util util = new Util(ctx);
					util.saveCallToDB(sharedPreferences.getString("phone", ""),
							callDuration, Long.toString(sharedPreferences
									.getLong("record_start", 0)), callType,
							fileName);

					editor = sharedPreferences.edit();
					editor.putString("phone", "");
					editor.commit();

				}
				editor = sharedPreferences.edit();
				editor.putString("prev_state", "idle");
				editor.commit();

				Log.d("DEBUG", "IDLE");
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:
				if (prevState.equals("idle")) {
					editor = sharedPreferences.edit();
					editor.putString("call_type", "Outgoing Call");

				} else if (prevState.equals("ringing")) {
					editor = sharedPreferences.edit();
					editor.putString("call_type", "Incoming Call");
					editor.commit();
				}

				if (prevState.equals("idle") || prevState.equals("ringing")
						&& !recordState) {
					String filePath = "";
					try {
						recordState = true;
						recordStartTime = System.currentTimeMillis();
						editor = sharedPreferences.edit();
						editor.putLong("record_start", recordStartTime);
						filePath = getFilePath();
						MediaRecorder mRecorder = new MediaRecorder();
						mRecorder
								.setAudioSource(MediaRecorder.AudioSource.MIC);
						mRecorder
								.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
						mRecorder
								.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
						mRecorder.setOutputFile(filePath);
						mRecorder.setOnErrorListener(new OnErrorListener() {

							@Override
							public void onError(MediaRecorder mr, int what,
									int extra) {

							}
						});
						mRecorder.setOnInfoListener(new OnInfoListener() {

							@Override
							public void onInfo(MediaRecorder mr, int what,
									int extra) {

							}
						});
						mRecorder.prepare();

						mRecorder.start();
						recorder = mRecorder;
					} catch (Exception e) {
						e.printStackTrace();
					}

					editor.putString("file_path", filePath);
					editor.commit();
				}
				editor = sharedPreferences.edit();
				editor.putString("prev_state", "offhook");
				editor.commit();
				Log.d("DEBUG", "OFFHOOK");
				break;
			case TelephonyManager.CALL_STATE_RINGING:
				editor = sharedPreferences.edit();

				editor.putString("phone", incomingNumber);

				editor.putString("prev_state", "ringing");
				editor.commit();
				Log.d("DEBUG", "RINGING");
				break;
			}
		}
	}

	public String getFilePath() {
		String filepath = Environment.getExternalStoragePublicDirectory(
				android.os.Environment.DIRECTORY_MUSIC).getPath();
		File file = new File(filepath, AUDIO_RECORDER_FOLDER);
		if (!file.exists()) {

			file.mkdirs();
		}
		File file2 = new File(file, recordStartTime + file_exts[currentFormat]);
		return file2.getAbsolutePath();
	}

}
