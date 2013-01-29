package android.map.pkg;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SqliteOpenhelper extends SQLiteOpenHelper {

	public static final String DB_NAME = "TRADERS_DB.db";
	public static final int DB_VERSION = 1;
	public static String[] SQLCOMMAND;

	public SqliteOpenhelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	public SqliteOpenhelper(Context context, String[] SQL) {
		super(context, DB_NAME, null, DB_VERSION);
		SQLCOMMAND = SQL;

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		for (int i = 0; i <= SQLCOMMAND.length - 1; i++) {
			db.execSQL(SQLCOMMAND[i].toString());
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion == 1 && newVersion == 2) {
			// Upgrade the database
			Log.w(SqliteOpenhelper.class.getName(),
					"Upgrading database from version " + oldVersion + " to "
							+ newVersion + ", which will destroy all old data");
			/*
			 * for (int i=0;i<=SQLCOMMAND.length-1;i++){
			 * db.execSQL("DROP TABLE IF EXISTS" + SQLCOMMAND[i].toString()); }
			 * onCreate(db);
			 */
		}
	}

}
