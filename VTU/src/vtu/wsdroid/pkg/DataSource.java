package vtu.wsdroid.pkg;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DataSource {

	private SQLiteDatabase database;
	private SQLiteHelper dbhelper;
	
	
	public DataSource(Context context) 
	{
		dbhelper = new SQLiteHelper(context);
	}

	public DataSource(Context context, String[] SQL) 
	{
		dbhelper = new SQLiteHelper(context,SQL);
	}

	public void open() throws SQLException {
		database = dbhelper.getWritableDatabase();
		//database = dbhelper.getReadableDatabase();
	}
	
	public void close() 
	{
		dbhelper.close();
	}

	
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
		
//	String query = "SELECT A.MSISDN FROM TRADER_NUMBERS_DTL A, TRADERS.TRADER_DATA B WHERE A.TID =
	//B.TID AND SUBSTR(A.MSISDN,1,2) IN ('99','92') AND B.DISTID IN(SELECT TID FROM TRADERS.TRADER_DATA WHERE DISTID = (SELECT TID FROM TRADER_NUMBERS_DTL WHERE MSISDN ='"+sender+"'))";

	/*public Cursor getFiterable(String Table_Name,String Col )
	{ 
		String SQLQuery="SELECT MSISDN FROM "+Table_Name +" WHERE MSISDN LIKE"+ Col +"% ;";
		database = dbhelper.getReadableDatabase();
		Cursor c=database.rawQuery(SQLQuery, null);
		//c.close();
		return c;		
	}*/
	
	public Cursor getFiterable(String Table_Name,String Col )
	{ 
		String SQLQuery="SELECT MSISDN FROM "+Table_Name +" WHERE MSISDN LIKE'"+ Col +"'% ;";
		database = dbhelper.getReadableDatabase();
		Cursor c=database.rawQuery(SQLQuery, null);
		//c.close();
		return c;		
	}
	public String InsertRow(String Table_name, HashMap DataHashMap) 
	{
		String MSG = null;
		ContentValues values = new ContentValues();

		Set s = DataHashMap.entrySet();
		Iterator it = s.iterator();
		while (it.hasNext()) {
			Map.Entry me = (Map.Entry) it.next();
			values.put(me.getKey().toString(), me.getValue().toString());
		}
		long insertId = database.insert(Table_name, null, values);
        MSG=Long.toString(insertId);
		return MSG;
	}
	}
