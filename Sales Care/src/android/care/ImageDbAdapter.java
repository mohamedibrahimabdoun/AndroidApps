package android.care;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ImageDbAdapter {
	public static final String TABLE_NAME = "REGISTRATION";
	public static final String COL_ID = "_id";
	public static final String COL_DATE = "DATE";
	public static final String COL_NAME= "NAME";
	public static final String COL_MSISDN= "MSISDN";
	public static final String COL_IMSI= "IMSI";
	public static final String COL_SERAIL="SERAIL";
	public static final String COL_IDTYPES="IDTYPES";
	public static final String COL_ADDRESS= "ADDRESS";
	public static final String COL_FRONTIMGNAME= "FRONTIMAGE_NAME";
	public static final String COL_BACKIMGNAME= "BACKIMAGE_NAME";
	public static final String COL_FRONTIMAGE= "FRONT_IMAGE";
	public static final String COL_BACKIMAGE= "BACK_IMAGE";
	
	/*"CREATE TABLE DATAREGISTRATION ("
	+ "_id INTEGER PRIMARY KEY AUTOINCREMENT, "+"DATE varchar2, "+"NAME varchar2," 
	+"MSISDN varchar2, "+"SERAIL varchar2, "+"ADDRESS varchar2, "+ "IDTYPES varchar2,"
	+"IMSI varchar2,"+ "FRONTIMAGE_NAME varchar2,"+"BACKIMAGE_NAME varchar2,"
	+"FRONT_IMAGE BLOB,"+ "BACK_IMAGE BLOB"*/
	

	public static final String[] PROJECTION_ALL = new String[] { COL_ID,
		COL_DATE };

	//private Context mContext;
	private SQLiteDatabase mDb;
	private ImageDbHelper mDbHelper;

	public ImageDbAdapter(Context context) {
		//mContext = c;
		//mDbHelper = new ImageDbHelper(c);
		mDbHelper = new ImageDbHelper(context);
	}
	
	public ImageDbAdapter(Context context, String[] SQL) 
	{
		mDbHelper = new ImageDbHelper(context,SQL);
	}
	
	public void open() throws SQLException {
		mDb = mDbHelper.getWritableDatabase();
	}
	/*public ImageDbAdapter open() throws SQLException {
		mDbHelper = new ImageDbHelper(mContext);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}*/

	public void close() {
		mDbHelper.close();
	}

	/*public long insertimage(byte[] image) {
		return mDb.insert(TABLE_NAME, null, createContentValues(image));
	}*/
	public long insert( String DATE,String  NAME,String SERAIL,String MSISDN,String IMSI,String ADDRESS,String IDTYPE,
			String FrontImgName,String BackImgName,  byte[] FrontImageByte,
			 byte[] BackImageByte) {
		return mDb.insert(TABLE_NAME, null, createContentValues(DATE, NAME, SERAIL, MSISDN, IMSI, ADDRESS, IDTYPE, FrontImgName, BackImgName, FrontImageByte,BackImageByte ));
	}

	private ContentValues createContentValues(String DATE,String  NAME,String SERAIL,String MSISDN,String IMSI,String ADDRESS,String IDTYPES,
			String FrontImgName,String BackImgName,  byte[] FrontImageByte,
			 byte[] BackImageByte) {
		ContentValues cv = new ContentValues();
		//cv.put(COL_ID, ID);
		cv.put(COL_NAME ,NAME);
		cv.put(COL_DATE, DATE);
		cv.put(COL_IMSI, IMSI);
		cv.put(COL_MSISDN, MSISDN);
		cv.put(COL_ADDRESS, ADDRESS);
		cv.put(COL_IDTYPES, IDTYPES); //COL_IDTYPES
		cv.put(COL_SERAIL, SERAIL);
		cv.put(COL_FRONTIMGNAME, FrontImgName);
		cv.put(COL_BACKIMGNAME,BackImgName);
		cv.put(COL_FRONTIMAGE,FrontImageByte);
		cv.put(COL_BACKIMAGE, BackImageByte);			
		return cv;
	}
	
	public Cursor getAllPosinfo(String Table_Name)
	{ 
		String SQLQuery="SELECT * FROM "+Table_Name;
		mDb = mDbHelper.getReadableDatabase();
		Cursor c=mDb.rawQuery(SQLQuery, null);
		//c.close();
		return c;		
	}
	public Cursor getAllData(String Table_Name) {
		String SQLQuery = "SELECT * FROM " + Table_Name;
		mDb = mDbHelper.getReadableDatabase();
		Cursor c = mDb.rawQuery(SQLQuery, null);
		return c;
	}

	public Cursor getAll(String Table_Name) {
		String SQLQuery = "SELECT * FROM " + Table_Name;
		mDb = mDbHelper.getReadableDatabase();
		Cursor c = mDb.rawQuery(SQLQuery, null);
		return c;
	}
	public int getCountRow(String Table_Name) {
		String SQLQuery = "SELECT * FROM " + Table_Name;
		mDb = mDbHelper.getReadableDatabase();
		Cursor cur = mDb.rawQuery(SQLQuery, null);
		return cur.getCount();
	}
	
	public int DeleteRow(String Table_Name, String key_col, int ID) {
		// database.delete(Table_name, MySQLiteHelper.COLUMN_ID+ " = " + id,
		// null);
		int i = mDb.delete(Table_Name, key_col + "=" + ID, null);
		// return database.delete(Table_Name, getKey(ID)+"="+ rowindex, null)>0;
		return i;
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
		long insertId = mDb.insert(Table_name, null, values);
		MSG = Long.toString(insertId);
		return MSG;
	}

	public String CreateTable(String Table_Name, HashMap hm) {

		StringBuffer sb = new StringBuffer();
		sb.append("CREATE TABLE IF NOT EXISTS" + Table_Name + " (");
		Set s = hm.entrySet();
		Iterator it = s.iterator();
		while (it.hasNext()) {
			Map.Entry me = (Map.Entry) it.next();
			sb.append(" " + me.getKey() + " " + me.getValue() + ",");
		}
		sb.append(")");
		String SQL = sb.toString();
		String R_SQL = SQL.replace(",)", ");");
		return R_SQL;

	}
	public byte[] fetchSingle(int id) {
		Cursor queryCursor = mDb.query(TABLE_NAME, PROJECTION_ALL, COL_ID
				+ " = " + id, null, null, null, null);
		if (queryCursor == null) {
			return null;
		}
		byte[] image = null;
		if (queryCursor.moveToFirst()) {
			image = queryCursor.getBlob(queryCursor
					.getColumnIndexOrThrow(COL_FRONTIMAGE));
		}
		queryCursor.close();

		return image;

	}

}
