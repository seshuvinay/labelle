package com.labelle.calllogsmanagement;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "labelle_db";
	private static final int DATABASE_VERSION = 1;
	private static final String CREATE_WIFI_STATUSES_QUERY = "create table if not exists wifi_statuses(_id integer primary key autoincrement,wifi_status text,timestamp text);";
	private static final String CREATE_SMS_TABLE_QUERY = "create table if not exists smses(_id integer primary key autoincrement,message text,number text,type text,timestamp text);";
	private static final String CREATE_CALLS_TABLE_QUERY = "create table if not exists calls(_id integer primary key autoincrement,duration text,number text,type text,filepath text,timestamp text);";

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_WIFI_STATUSES_QUERY);
		db.execSQL(CREATE_SMS_TABLE_QUERY);
		db.execSQL(CREATE_CALLS_TABLE_QUERY);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onCreate(db);

	}

}
