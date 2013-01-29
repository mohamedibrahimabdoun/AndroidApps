package android.map.pkg;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DataRegDataSource {

	private SQLiteDatabase database;
	private SqliteOpenhelper dbhelper;

	public DataRegDataSource(Context context) {

	}

	public DataRegDataSource(Context context, String[] SQL) {
		dbhelper = new SqliteOpenhelper(context, SQL);

	}

	public void open() throws SQLException {
		database = dbhelper.getWritableDatabase();
	}

	public void close() {
		dbhelper.close();
	}

	public String InsertRow(String Table_name, HashMap DataHashMap) {
		String MSG = null;
		ContentValues values = new ContentValues();

		Set s = DataHashMap.entrySet();
		Iterator it = s.iterator();
		while (it.hasNext()) {
			Map.Entry me = (Map.Entry) it.next();
			values.put(me.getKey().toString(), me.getValue().toString());
		}
		long insertId = database.insert(Table_name, null, values);
		MSG = Long.toString(insertId);
		return MSG;
	}

	public Cursor getAllData(String Table_Name) {
		String SQLQuery = "SELECT * FROM " + Table_Name;
		database = dbhelper.getReadableDatabase();
		Cursor c = database.rawQuery(SQLQuery, null);
		return c;
	}

	public int getCountRow(String Table_Name) {
		String SQLQuery = "SELECT * FROM " + Table_Name;
		database = dbhelper.getReadableDatabase();
		Cursor cur = database.rawQuery(SQLQuery, null);
		return cur.getCount();
	}

	public boolean ClearTable(String Table_Name, HashMap DataHashMap) {
		return database.delete(Table_Name, null, null) > 0;
	}

	public int DeleteRow(String Table_Name, String key_col, int ID) {
		// database.delete(Table_name, MySQLiteHelper.COLUMN_ID+ " = " + id,
		// null);
		int i = database.delete(Table_Name, key_col + "=" + ID, null);
		// return database.delete(Table_Name, getKey(ID)+"="+ rowindex, null)>0;
		return i;
	}
	
	/*public long insert(byte[] image) {
		return mDb.insert(TABLE_NAME, null, createContentValues(image));
	}*/

	public Cursor getAllRec(String Table_Name,String Col )
	{ 
		String SQLQuery="SELECT * FROM "+Table_Name +" WHERE MSISDN "+ Col;
		database = dbhelper.getReadableDatabase();
		Cursor c=database.rawQuery(SQLQuery, null);
		//c.close();
		return c;		
	}
	
	public Cursor getAllMSISDN(String Table_Name )
	{ 
		String SQLQuery="SELECT * FROM "+Table_Name ;
		database = dbhelper.getReadableDatabase();
		Cursor c=database.rawQuery(SQLQuery, null);
		return c;		
	}
	private ContentValues createContentValues(byte[] image) {
		ContentValues cv = new ContentValues();
		cv.put("blob_column", image);
		return cv;
	}
}
