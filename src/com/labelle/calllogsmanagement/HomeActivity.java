package com.labelle.calllogsmanagement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import de.quist.app.errorreporter.ExceptionReporter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("SimpleDateFormat")
@SuppressWarnings("deprecation")
public class HomeActivity extends Activity {
	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	public RelativeLayout homeLayout;
	EditText registerEditText, secureCodeEditText;
	Button registerButton;
	ImageView labelleLableImage, labelleLableImageAfterLogin,
			telephoneImageView;
	TextView synchronizeTimeTextView, agentIdTextView, agentIdTitleTextView,
			callUsAtTextView, callUsNumberTextView, aboutusTitleTextView;
	int deviceWidth, deviceHeight, callHistoryButtonWidth,
			callHistoryButtonHeight, labelleLableAftreloginHeight,
			labelleLableAftreloginWidth;
	int callHistoryButtonHeightParams, smsHistoryButtonHeightParams,
			labelleLableHeightParams, registerButtonHeightParms,
			secureCodeEditTextHeightParams, aboutusTextWidthParams,
			aboutusTextHeightParams, fontSize, agentIdFontSize, callusFontSize,
			callusNumberFontSize, agentIdTextWidthParams,
			agentIdTextHeightParams, agentIdTitleTextWidthParams,
			labelleLableAftreloginHeightParams, aboutusTitleTextHeightParams,
			aboutusTitleTextWidthParams, telephoneHeightParams,
			telephoneWidthParams, callUsAtTextHeightParams,
			callUsAtTextWidthParams, callUsNumberHeightParams,
			callUsNumberWidthParams;
	String mobileNo, secureCode, verficationCode;
	JSONObject json = new JSONObject();
	ConnectivityManager conMgr;
	static String resultData, postData;
	public Dialog dialog, dialog1;
	SharedPreferences sharedPreferences;
	SharedPreferences.Editor editor;
	public String diaogDismissValue = "no";
	long synchronizeTime;
	DateFormat dateFormat;
	private ExceptionReporter reporter;

	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		Log.d("HomeActivityIntent", "Home Activity Called In SMSReceiver ");

		sharedPreferences = getApplicationContext().getSharedPreferences(
				"MyPref", 0);
		mobileNo = sharedPreferences.getString("adminId", "");
		if (mobileNo.equals("")) {
			dialog.dismiss();
		} else {
			startService(new Intent(HomeActivity.this, UploadService.class));
			registerButton.setVisibility(View.GONE);
			registerEditText.setVisibility(View.GONE);
			secureCodeEditText.setVisibility(View.GONE);
			labelleLableImage.setVisibility(View.GONE);
			agentIdTitleTextView.setText("AgentId:-" + mobileNo);

			sharedPreferences = getApplicationContext().getSharedPreferences(
					"MyPref", 0);
			/*
			 * synchronizeTime = sharedPreferences.getLong("synchronizeTime",
			 * synchronizeTime);
			 */
			// String synString=Long.toString(synchronizeTime);

			dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
			synchronizeTime = System.currentTimeMillis();

			String synString = dateFormat.format(synchronizeTime);

			synchronizeTimeTextView.setText(synString);
			/*
			 * aboutusTitleTextView .setVisibility(View.VISIBLE);
			 */

			labelleLableImageAfterLogin.setVisibility(View.VISIBLE);
			aboutusTitleTextView.setVisibility(View.VISIBLE);
			agentIdTextView.setVisibility(View.VISIBLE);
			agentIdTitleTextView.setVisibility(View.VISIBLE);
			synchronizeTimeTextView.setVisibility(View.VISIBLE);
			// Icon hiding Code...................
			/*
			 * PackageManager p = getPackageManager();
			 * p.setComponentEnabledSetting(getComponentName(),
			 * PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
			 * PackageManager.DONT_KILL_APP);
			 */
			dialog.dismiss();
		}
	} // E

	// 3600000
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			reporter = ExceptionReporter.register(HomeActivity.this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Display deviceDisplay = ((WindowManager) getSystemService(Context.WINDOW_SERVICE))
				.getDefaultDisplay();
		deviceWidth = deviceDisplay.getWidth();
		deviceHeight = deviceDisplay.getHeight();

		// scheduleAlarm();

		// int top=(int)((deviceHeight*190.4761)/800) ; //42

		callHistoryButtonHeight = (int) ((deviceHeight * 10) / (133.3333)); // 60
		aboutusTextWidthParams = (int) ((deviceWidth * 10) / (120)); // 40
		aboutusTextHeightParams = (int) ((deviceHeight * 10) / (19.5121)); // 410
		labelleLableHeightParams = (int) ((deviceHeight * 10) / (177.7777)); // 45
		labelleLableAftreloginWidth = (int) ((deviceWidth * 10) / (11.4285)); // 420
		labelleLableAftreloginHeight = (int) ((deviceHeight * 10) / (32)); // 250
		labelleLableAftreloginHeightParams = (int) ((deviceHeight * 10) / (320)); // 25
		smsHistoryButtonHeightParams = (int) ((deviceHeight * 10) / (26.2295));// 305
		secureCodeEditTextHeightParams = (int) ((deviceHeight * 10) / (19.5121));// 410
		registerButtonHeightParms = (int) ((deviceHeight * 10) / (15.5339));// 515
		fontSize = (int) ((deviceWidth * 10) / (228.5714));// 21
		agentIdFontSize = (int) ((deviceWidth * 10) / (154.8387));// 31
		callusFontSize = (int) ((deviceWidth * 10) / (94.1176));// 51
		callusNumberFontSize = (int) ((deviceWidth * 10) / (117.0731));// 41
		agentIdTextWidthParams = (int) ((deviceWidth * 10) / (30)); // 160
		agentIdTextHeightParams = (int) ((deviceHeight * 10) / (27.5862)); // 290
		agentIdTitleTextWidthParams = (int) ((deviceWidth * 10) / (240)); // 20
		aboutusTitleTextHeightParams = (int) ((deviceHeight * 10) / (22.8571)); // 350
		telephoneHeightParams = (int) ((deviceHeight * 10) / (12.5)); // 640
		telephoneWidthParams = (int) ((deviceWidth * 10) / (96)); // 50
		callUsAtTextHeightParams = (int) ((deviceHeight * 10) / (12.6984)); // 630
		callUsAtTextWidthParams = (int) ((deviceWidth * 10) / (40)); // 120
		callUsNumberHeightParams = (int) ((deviceHeight * 10) / (11.5942)); // 690

		homeLayout = new RelativeLayout(this);
		RelativeLayout.LayoutParams layoutparams = new RelativeLayout.LayoutParams(
				deviceWidth, deviceHeight);
		homeLayout.setBackgroundColor(Color.parseColor("#F2F2F2"));
		homeLayout.setLayoutParams(layoutparams);

		labelleLableImage = new ImageView(this);
		RelativeLayout.LayoutParams labelleImageParams = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		labelleImageParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		labelleLableImage.setBackgroundResource(R.drawable.labelle_lable);
		labelleImageParams.setMargins(0, labelleLableHeightParams, 0, 0);
		labelleLableImage.setLayoutParams(labelleImageParams);

		registerEditText = new EditText(this);
		RelativeLayout.LayoutParams registerTextParams = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		registerTextParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		registerEditText.setLayoutParams(registerTextParams);
		registerTextParams.setMargins(0, smsHistoryButtonHeightParams, 0, 0);
		registerEditText.setHint("Enter your mobileno");
		registerEditText.setBackgroundResource(R.drawable.edit_text);
		registerEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
		InputFilter[] FilterArray = new InputFilter[1];
		FilterArray[0] = new InputFilter.LengthFilter(10);
		registerEditText.setFilters(FilterArray);

		secureCodeEditText = new EditText(this);
		RelativeLayout.LayoutParams secureCodeTextParams = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		secureCodeTextParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		secureCodeEditText.setLayoutParams(secureCodeTextParams);
		secureCodeTextParams
				.setMargins(0, secureCodeEditTextHeightParams, 0, 0);
		secureCodeEditText.setBackgroundResource(R.drawable.edit_text);
		secureCodeEditText.setHint("Please enter SecureCode ");
		secureCodeEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
		InputFilter[] FilterArray1 = new InputFilter[1];
		FilterArray1[0] = new InputFilter.LengthFilter(10);
		secureCodeEditText.setFilters(FilterArray1);

		registerButton = new Button(this);
		RelativeLayout.LayoutParams registerButtonParams = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		registerButtonParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		registerButton.setLayoutParams(registerButtonParams);
		registerButtonParams.setMargins(0, registerButtonHeightParms, 0, 0);
		// registerButton.setText("Register");
		registerButton.setBackgroundResource(R.drawable.register_selection);

		synchronizeTimeTextView = new TextView(this);
		RelativeLayout.LayoutParams aboutusTextParams = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		aboutusTextParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		aboutusTextParams.setMargins(aboutusTextWidthParams,
				aboutusTextHeightParams, aboutusTextWidthParams, 0);
		synchronizeTimeTextView.setLayoutParams(aboutusTextParams);
		synchronizeTimeTextView.setTextColor(Color.BLACK);
		synchronizeTimeTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
				agentIdFontSize);
		synchronizeTimeTextView.setVisibility(View.GONE);

		agentIdTextView = new TextView(this);
		RelativeLayout.LayoutParams agentIdTextParams = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		agentIdTextParams.setMargins(agentIdTextWidthParams,
				agentIdTextHeightParams, 0, 0);
		agentIdTextView.setLayoutParams(agentIdTextParams);
		agentIdTextView.setTextColor(Color.parseColor("#0039F3"));
		agentIdTextView
				.setTextSize(TypedValue.COMPLEX_UNIT_PX, agentIdFontSize);
		agentIdTextView.setVisibility(View.GONE);

		labelleLableImageAfterLogin = new ImageView(this);
		RelativeLayout.LayoutParams labelleImageAfterLoginParams = new RelativeLayout.LayoutParams(
				labelleLableAftreloginWidth, labelleLableAftreloginHeight);
		labelleImageAfterLoginParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		labelleLableImageAfterLogin
				.setBackgroundResource(R.drawable.labelle_lable);
		labelleImageAfterLoginParams.setMargins(0,
				labelleLableAftreloginHeightParams, 0, 0);
		labelleLableImageAfterLogin
				.setLayoutParams(labelleImageAfterLoginParams);
		labelleLableImageAfterLogin
				.setBackgroundResource(R.drawable.labelle_lable_after_login);
		labelleLableImageAfterLogin.setVisibility(View.GONE);

		agentIdTitleTextView = new TextView(this);
		RelativeLayout.LayoutParams agentIdTitleTextParams = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		agentIdTitleTextParams.setMargins(agentIdTitleTextWidthParams,
				agentIdTextHeightParams, 0, 0);
		agentIdTitleTextParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		agentIdTitleTextView.setLayoutParams(agentIdTitleTextParams);
		agentIdTitleTextView.setTextColor(Color.parseColor("#0039F3"));
		agentIdTitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
				agentIdFontSize);
		agentIdTitleTextView.setVisibility(View.GONE);

		aboutusTitleTextView = new TextView(this);
		RelativeLayout.LayoutParams aboutusTitleTextParams = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		aboutusTitleTextParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		aboutusTitleTextParams.setMargins(agentIdTitleTextWidthParams,
				aboutusTitleTextHeightParams, 0, 0);
		aboutusTitleTextView.setLayoutParams(aboutusTitleTextParams);
		aboutusTitleTextView.setTextColor(Color.parseColor("#ED1C24"));
		aboutusTitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
				agentIdFontSize);
		aboutusTitleTextView.setText("Last synchronize time is:");
		aboutusTitleTextView.setVisibility(View.GONE);

		telephoneImageView = new ImageView(this);
		RelativeLayout.LayoutParams telephoneImageParams = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		telephoneImageView.setBackgroundResource(R.drawable.telephone);
		telephoneImageView.setLayoutParams(telephoneImageParams);
		telephoneImageParams.setMargins(telephoneWidthParams,
				telephoneHeightParams, 0, 0);
		telephoneImageView.setVisibility(View.GONE);

		callUsAtTextView = new TextView(this);
		RelativeLayout.LayoutParams callUsAtTextViewTextParams = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		// aboutusTitleTextParams
		// .addRule(RelativeLayout.CENTER_HORIZONTAL);
		callUsAtTextViewTextParams.setMargins(callUsAtTextWidthParams,
				callUsAtTextHeightParams, 0, 0);
		callUsAtTextView.setLayoutParams(callUsAtTextViewTextParams);
		callUsAtTextView.setTextColor(Color.parseColor("#ED1C24"));
		callUsAtTextView
				.setTextSize(TypedValue.COMPLEX_UNIT_PX, callusFontSize);
		callUsAtTextView.setText("Call us at");
		callUsAtTextView.setVisibility(View.GONE);

		callUsNumberTextView = new TextView(this);
		RelativeLayout.LayoutParams callUsNumberTextParams = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		// aboutusTitleTextParams
		// .addRule(RelativeLayout.CENTER_HORIZONTAL);
		callUsNumberTextParams.setMargins(callUsAtTextWidthParams,
				callUsNumberHeightParams, 0, 0);
		callUsNumberTextView.setLayoutParams(callUsNumberTextParams);
		callUsNumberTextView.setTextColor(Color.parseColor("#ED1C24"));
		callUsNumberTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
				callusNumberFontSize);
		callUsNumberTextView.setText("1800-209-5292.");
		callUsNumberTextView.setVisibility(View.GONE);

		// agentIdTextView.setText(mobileNo);
		agentIdTitleTextView.setText("AgentId:" + mobileNo);

		homeLayout.addView(aboutusTitleTextView);
		homeLayout.addView(synchronizeTimeTextView);
		homeLayout.addView(callUsAtTextView);
		homeLayout.addView(callUsNumberTextView);
		homeLayout.addView(telephoneImageView);
		homeLayout.addView(labelleLableImageAfterLogin);
		homeLayout.addView(agentIdTextView);
		homeLayout.addView(agentIdTitleTextView);

		// key board return back by touching on screen
		homeLayout.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent ev) {
				hideKeyboard(view);
				return false;
			}
		});
		sharedPreferences = getApplicationContext().getSharedPreferences(
				"MyPref", 0);
		mobileNo = sharedPreferences.getString("adminId", "");
		// diaogDismissValue = sharedPreferences.getInt("dialogDismissValue",
		// -1);
		// mobileNo = "9553256325";
		if (mobileNo.equals("")) {

			homeLayout.addView(labelleLableImage);
			homeLayout.addView(registerEditText);
			homeLayout.addView(secureCodeEditText);
			homeLayout.addView(registerButton);
			setContentView(homeLayout);
		} else {
			scheduleAlarm(mobileNo);
			sharedPreferences = getApplicationContext().getSharedPreferences(
					"MyPref", 0);
			synchronizeTime = sharedPreferences.getLong("synchronizeTime", 0);
			if (synchronizeTime != 0) {
				dateFormat = new SimpleDateFormat(
						"EEE MMM dd HH:mm:ss zzz yyyy");
				String synString = dateFormat.format(synchronizeTime);
				synchronizeTimeTextView.setText(synString);
			} else
				synchronizeTimeTextView.setVisibility(View.GONE);
			// agentIdTextView.setText(mobileNo);
			agentIdTitleTextView.setText("AgentId:-" + mobileNo);

			aboutusTitleTextView.setVisibility(View.VISIBLE);
			// callUsAtTextView.setVisibility(View.VISIBLE);
			// callUsNumberTextView.setVisibility(View.VISIBLE);
			synchronizeTimeTextView.setVisibility(View.VISIBLE);
			// telephoneImageView.setVisibility(View.VISIBLE);
			labelleLableImageAfterLogin.setVisibility(View.VISIBLE);
			agentIdTextView.setVisibility(View.VISIBLE);
			agentIdTitleTextView.setVisibility(View.VISIBLE);
			setContentView(homeLayout);

		}

		// Buttion actions

		registerButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				mobileNo = registerEditText.getText().toString();
				secureCode = secureCodeEditText.getText().toString();

				if (mobileNo.matches("") || secureCode.matches("")) {
					Toast.makeText(getApplicationContext(),
							"Fields should not empty", Toast.LENGTH_SHORT)
							.show();
				} else {
					if (registerEditText.length() < 10) {
						registerEditText.setText("");
						secureCodeEditText.setText("");
						registerEditText
								.setError("Please enter valid phone no");

					} else {
						mobileNo = registerEditText.getText().toString();
						secureCode = secureCodeEditText.getText().toString();
						String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
						Random rnd = new Random();
						StringBuilder sb = new StringBuilder(4);
						for (int i = 0; i < 4; i++) {
							sb.append(AB.charAt(rnd.nextInt(AB.length())));
						}
						verficationCode = sb.toString();
						sharedPreferences = getApplicationContext()
								.getSharedPreferences("MyPref", 0);
						editor = sharedPreferences.edit();
						editor.putString("verficationCode", verficationCode);
						editor.putString("smsSenderId", mobileNo);
						editor.putString("registerNo", mobileNo);
						editor.commit();

						loadData();

					}

				}

			}
		});
	}

	public void scheduleAlarm(String mobileNo) {
		Intent intent = new Intent(HomeActivity.this, UploadService.class);
		intent.putExtra("mobileNo", mobileNo);
		startService(intent);
	}

	protected void loadData() {

		conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (conMgr.getActiveNetworkInfo() != null
				&& conMgr.getActiveNetworkInfo().isAvailable()
				&& conMgr.getActiveNetworkInfo().isConnected()) {

			new GetPostMail().execute();

		} else {
			registerEditText.setText("");
			secureCodeEditText.setText("");
			Toast.makeText(getApplicationContext(), "No internet connection",
					Toast.LENGTH_SHORT).show();

		}

	}

	private class GetPostMail extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(HomeActivity.this, "",
					"Loading...Please Wait....");
		}

		@Override
		protected String doInBackground(String... arg0) {

			String urlMail = "http://www.labelle.in/Android/register.php";

			HttpPost httpPost = new HttpPost(urlMail);

			nameValuePairs.add(new BasicNameValuePair("mobile", mobileNo));
			try {
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			} catch (UnsupportedEncodingException e) {

				e.printStackTrace();
			}

			try {

				HttpClient httpclient = new DefaultHttpClient();
				HttpResponse response = httpclient.execute(httpPost);
				HttpEntity entity11 = response.getEntity();
				InputStream is = entity11.getContent();

				resultData = convertStreamToStringMail(is);

			} catch (ClientProtocolException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(String jsonresultMail) {

			if (resultData == null) {

			} else {

				try {

					JSONObject js = new JSONObject(resultData);
					String mobile = js.getString("mobile");
					String code = js.getString("code");
					String s = js.getString("success");
					Log.d("hi", "Response is" + mobile);
					Log.d("hi", "Response is" + code);

					if (s.equals("1")) {
						if (secureCode.equals(code) && mobileNo.equals(mobile)) {

							SmsManager smsMgr = SmsManager.getDefault();
							smsMgr.sendTextMessage(mobileNo, null,
									"VerficationCode:" + verficationCode, null,
									null);

							// sendSMS(mobileNo,
							// "VerficationCode:"+verficationCode);

						} else {

							Toast.makeText(getApplicationContext(),
									"Invalid mobileno and code",
									Toast.LENGTH_SHORT).show();
							secureCodeEditText.setText("");
							registerEditText.setText("");
							dialog.dismiss();
						}

						/*
						 * Intent loginIntent = new Intent(HomeActivity.this,
						 * LoginActivity.class);
						 * loginIntent.putExtra("mobileNo", mobile);
						 * loginIntent.putExtra("verificationCode", code);
						 * loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						 * startActivity(loginIntent);
						 * registerEditText.setText("");
						 */

					} else {
						Toast.makeText(getApplicationContext(),
								"logIn failure", Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					Log.d("", "Error while parsing the results!");
					e.printStackTrace();
				}
			}

			// dialog.dismiss();
			super.onPostExecute(jsonresultMail);
		}

		private String convertStreamToStringMail(InputStream is) {

			BufferedReader reader = new BufferedReader(
					new InputStreamReader(is));
			StringBuilder sb = new StringBuilder();

			String line = null;
			try {
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return sb.toString();
		}// convert to string
	}

	protected void hideKeyboard(View view) {
		InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		in.hideSoftInputFromWindow(view.getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
	}

	/*
	 * private void sendSMS(String phoneNumber, String message) { String SENT =
	 * "SMS_SENT"; String DELIVERED = "SMS_DELIVERED"; PendingIntent sentPI =
	 * PendingIntent.getBroadcast(this, 0, new Intent( SENT), 0); PendingIntent
	 * deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(DELIVERED),
	 * 0); // ---when the SMS has been sent--- registerReceiver(new
	 * BroadcastReceiver() {
	 * 
	 * @Override public void onReceive(Context arg0, Intent arg1) { switch
	 * (getResultCode()) { case Activity.RESULT_OK:
	 * Toast.makeText(getBaseContext(), "SMS sent", Toast.LENGTH_SHORT).show();
	 * break; case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
	 * Toast.makeText(getBaseContext(), "Generic failure",
	 * Toast.LENGTH_SHORT).show(); dialog.dismiss(); break; case
	 * SmsManager.RESULT_ERROR_NO_SERVICE: Toast.makeText(getBaseContext(),
	 * "No service", Toast.LENGTH_SHORT).show(); dialog.dismiss();
	 * 
	 * break; case SmsManager.RESULT_ERROR_NULL_PDU:
	 * Toast.makeText(getBaseContext(), "Null PDU", Toast.LENGTH_SHORT).show();
	 * dialog.dismiss();
	 * 
	 * break; case SmsManager.RESULT_ERROR_RADIO_OFF:
	 * Toast.makeText(getBaseContext(), "Radio off", Toast.LENGTH_SHORT).show();
	 * dialog.dismiss();
	 * 
	 * break; } } }, new IntentFilter(SENT)); // ---when the SMS has been
	 * delivered--- registerReceiver(new BroadcastReceiver() {
	 * 
	 * @Override public void onReceive(Context arg0, Intent arg1) { switch
	 * (getResultCode()) { case Activity.RESULT_OK:
	 * Toast.makeText(getBaseContext(), "SMS delivered",
	 * Toast.LENGTH_SHORT).show(); Log.d("Delivere report", "SMS delivered");
	 * 
	 * break; case Activity.RESULT_CANCELED: Toast.makeText(getBaseContext(),
	 * "SMS not delivered", Toast.LENGTH_SHORT).show(); break; } } }, new
	 * IntentFilter(DELIVERED)); SmsManager sms = SmsManager.getDefault();
	 * sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI); }
	 */
}
