package com.labelle.calllogsmanagement;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

public class LoginActivity extends Activity {
	RelativeLayout loginLayout;
	EditText mobileNoEditText, verficationCodeEdit;
	Button logInButton;
	int deviceWidth, deviceHeight, mobileNoEditTextWidth,
			mobileNoEditTextHeight;
	int mobileNoEditTextHeightParams, smsHistoryButtonHeightParams,
			logInButtonHeightParms;
	String mobile, code, vCode, mobileNo;
	SharedPreferences sharedPreferences;
	SharedPreferences.Editor editor;
	Context ctx;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Display deviceDisplay = ((WindowManager) getSystemService(Context.WINDOW_SERVICE))
				.getDefaultDisplay();
		deviceWidth = deviceDisplay.getWidth();
		deviceHeight = deviceDisplay.getHeight();

		mobileNoEditTextHeight = (int) ((deviceHeight * 10) / (133.3333)); // 60
		mobileNoEditTextHeightParams = (int) ((deviceHeight * 10) / (26.6666));// 300
		smsHistoryButtonHeightParams = (int) ((deviceHeight * 10) / (21.0526));// 380
		logInButtonHeightParms = (int) ((deviceHeight * 10) / (17.3913));// 460

		loginLayout = new RelativeLayout(this);
		RelativeLayout.LayoutParams layoutparams = new RelativeLayout.LayoutParams(
				deviceWidth, deviceHeight);
		loginLayout.setBackgroundColor(Color.WHITE);
		loginLayout.setLayoutParams(layoutparams);

		mobileNoEditText = new EditText(this);
		RelativeLayout.LayoutParams mobileNoEditTextParams = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, mobileNoEditTextHeight);
		mobileNoEditTextParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		mobileNoEditTextParams
				.setMargins(0, mobileNoEditTextHeightParams, 0, 0);
		mobileNoEditText.setLayoutParams(mobileNoEditTextParams);
		mobileNoEditText.setHint("Enter your mobileNoEditText");
		mobileNoEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
		InputFilter[] FilterArray = new InputFilter[1];
		FilterArray[0] = new InputFilter.LengthFilter(10);
		mobileNoEditText.setFilters(FilterArray);

		verficationCodeEdit = new EditText(this);
		RelativeLayout.LayoutParams registerTextParams = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, mobileNoEditTextHeight);
		registerTextParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		verficationCodeEdit.setLayoutParams(registerTextParams);
		registerTextParams.setMargins(0, smsHistoryButtonHeightParams, 0, 0);
		verficationCodeEdit.setHint("Enter VerificationCode");

		logInButton = new Button(this);
		RelativeLayout.LayoutParams logInButtonParams = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, mobileNoEditTextHeight);
		logInButtonParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		logInButton.setLayoutParams(logInButtonParams);
		logInButtonParams.setMargins(0, logInButtonHeightParms, 0, 0);
		logInButton.setText("LogIn");

		// adding views to layout
		// loginLayout.addView(mobileNoEditText);
		loginLayout.addView(verficationCodeEdit);
		loginLayout.addView(logInButton);
		setContentView(loginLayout);

		logInButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// mobileNo = mobileNoEditText.getText().toString();
				vCode = verficationCodeEdit.getText().toString();
				if ((vCode.equals(code))) {

					Toast.makeText(getApplicationContext(),
							"Login successfull", Toast.LENGTH_SHORT).show();

					sharedPreferences = getApplicationContext()
							.getSharedPreferences("MyPref", 0);
					editor = sharedPreferences.edit();
					editor.putString("adminId", mobileNo);
					editor.commit();
					scheduleAlarm();

					verficationCodeEdit.setText("");
					verficationCodeEdit.setVisibility(View.GONE);
					logInButton.setVisibility(View.GONE);

				} else {
					verficationCodeEdit.setText("");
					verficationCodeEdit.setError("Please enter valid phone no");

				}
			}
		});

	}

	public void scheduleAlarm() {
		Intent intentAlarm = new Intent(this, UploadService.class);
		intentAlarm.putExtra("mobileNo", mobile);
		startService(intentAlarm);

	}

	@Override
	protected void onPause() {
		super.onPause();

		SharedPreferences prefs = getSharedPreferences("X", MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.putString("lastActivity", getClass().getName());
		editor.commit();
	}
}
