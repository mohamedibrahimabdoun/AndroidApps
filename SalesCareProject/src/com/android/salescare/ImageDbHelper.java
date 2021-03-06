package com.android.salescare;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ImageDbHelper extends SQLiteOpenHelper {
	public static final String 	DB_NAME = "REGISTRATION_DB.db";
	public static final int DB_VERSION = 1;
	public static  String[] SQLCOMMAND;

	private static final String SQL_CREATE = "CREATE TABLE REGISTRATION ("
		+ "_id INTEGER PRIMARY KEY AUTOINCREMENT, "+"DATE varchar2, "+"NAME varchar2," 
		+ "MSISDN varchar2, "+"SERAIL varchar2, "+"ADDRESS varchar2, "+ "IDTYPES varchar2,"
		+"IMSI varchar2,"+ "FRONTIMAGE_NAME varchar2,"+"BACKIMAGE_NAME varchar2,"
		+"FRONT_IMAGE blob,"+ "BACK_IMAGE blob"
		+");";
	private static final String SQL_DROP = "DROP TABLE IF EXISTS REGISTRATION;";

	public ImageDbHelper(Context c) {
		super(c, DB_NAME, null, DB_VERSION);
	}
	
	public ImageDbHelper(Context context,String[] SQL) 
	{
		super(context, DB_NAME, null, DB_VERSION);
		SQLCOMMAND=SQL;
		
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		
		db.execSQL(SQL_CREATE);
		for (int i = 0; i <= SQLCOMMAND.length - 1; i++) {
			db.execSQL(SQLCOMMAND[i].toString());
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(SQL_DROP);
		onCreate(db);
	}

}
