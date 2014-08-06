package com.labelle.calllogsmanagement;

import de.quist.app.errorreporter.ExceptionReporter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.StrictMode;

public class NetworkChangeReceiver extends BroadcastReceiver {

	private ExceptionReporter reporter;

	@Override
	public void onReceive(final Context context, final Intent intent) {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		final Util util = new Util(context);
		try {
			reporter = ExceptionReporter.register(context);
		} catch (Exception e) {
			e.printStackTrace();
		}
		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				if (util.isInternetAvailable()) {

					ConnectivityManager connMgr = (ConnectivityManager) context
							.getSystemService(Context.CONNECTIVITY_SERVICE);

					final android.net.NetworkInfo wifi = connMgr
							.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

					if (wifi.isConnected())
						util.saveWifiStateToDB("Connected",
								Long.toString(System.currentTimeMillis()));

				} else {
					util.saveWifiStateToDB("Disconnected",
							Long.toString(System.currentTimeMillis()));
				}

			}
		}, 10000);

	}
}
