package com.labelle.calllogsmanagement;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBAdapter {
	private Context context;
	private SQLiteDatabase database;
	private DBHelper dbHelper;

	public DBAdapter(Context context) {
		this.context = context;

	}

	public DBAdapter open() throws SQLException {
		dbHelper = new DBHelper(context);
		database = dbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		dbHelper.close();

	}

	public void insertSMS(String message, String type, String number,
			String timestamp) {
		ContentValues values = new ContentValues();
		values.put("message", message);
		values.put("number", number);
		values.put("type", type);
		values.put("timestamp", timestamp);
		database.insert("smses", null, values);
	}

	public void insertCall(String duration, String type, String number,
			String timestamp, String filename) {
		ContentValues values = new ContentValues();
		values.put("duration", duration);
		values.put("number", number);
		values.put("type", type);
		values.put("timestamp", timestamp);
		values.put("filepath", filename);
		database.insert("calls", null, values);
	}

	public void insertWifiStatus(String status, String timestamp) {
		ContentValues values = new ContentValues();
		values.put("wifi_status", status);
		values.put("timestamp", timestamp);
		database.insert("wifi_statuses", null, values);
	}

	public Cursor getWifiStatuses() {
		return database.query("wifi_statuses", null, null, null, null, null,
				null);
	}

	public Cursor getSmses() {
		return database.query("smses", null, null, null, null, null, null);
	}

	public Cursor getCalls() {
		return database.query("calls", null, null, null, null, null, null);
	}

	public void deleteWifiStatus(String id) {
		database.delete("wifi_statuses", "_id=" + id, null);
	}

	public void deleteSms(String id) {
		database.delete("smses", "_id=" + id, null);
	}

	public void deleteCall(String id) {
		database.delete("calls", "_id=" + id, null);
	}
}
