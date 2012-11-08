package vtu.wsdroid.pkg;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteHelper extends SQLiteOpenHelper {

	public static final String DB_NAME = "SERVICES.db";
	public static final int DB_VERSION = 1;
	// Table attributes
	public static String[] SQLCOMMAND;

	public SQLiteHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	public SQLiteHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	public SQLiteHelper(Context context, String[] SQL) {
		super(context, DB_NAME, null, DB_VERSION);
		SQLCOMMAND = SQL;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		for (int i=0;i<=SQLCOMMAND.length-1;i++){	
			db.execSQL(SQLCOMMAND[i].toString());
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		if(oldVersion == 1 && newVersion == 2)
		{
			// Upgrade the database
			Log.w(SQLiteHelper.class.getName(),
					"Upgrading database from version " + oldVersion + " to "
							+ newVersion + ", which will destroy all old data");
	}
	}

}
