package com.lonuery.mealordering;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBase {

	MyDataBase dataBaseHelper;
	Context context;
	private static final String dataBaseName="Lonuery.db";
	private static final int DATABASE_VERSION=1;
	private static final String tableName= "loginInfo";
	
	public DataBase(Context context){
		this.context = context;
		dataBaseHelper = new MyDataBase(context, dataBaseName, null, DATABASE_VERSION);
	}
	
	public void insertData(String account,String psw){
		SQLiteDatabase database = dataBaseHelper.getWritableDatabase();
		String str = "insert into "+tableName+" values (?,?)";
		
		String obj[] = {account,psw};
		try {
			database.execSQL(str,obj);
			Log.i("insertData", "’À∫≈±£¥Ê≥…π¶");
		} catch (SQLException e) {
			e.printStackTrace();
			Log.i("insertData", "’À∫≈±£¥Ê ß∞‹");
		}finally{
			database.close();
		}
	}
	
	public List<LoginInfo> queryData(String tableName){	
		SQLiteDatabase database = dataBaseHelper.getWritableDatabase();
		String str = "select * from "+tableName;
		
		List<LoginInfo> list = new ArrayList<LoginInfo>();
		Cursor cursor = database.rawQuery(str, null);
		if(cursor.moveToFirst()){
			do {
				LoginInfo mode = new LoginInfo();
				mode.setAccount(cursor.getString(0));
				mode.setPsw(cursor.getString(1));
				list.add(mode);
			}while(cursor.moveToNext());
		}
		try{
			cursor.close();
			database.close();
		}catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(cursor!=null){
				try {
					cursor.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return list;
	}
	
	class MyDataBase extends SQLiteOpenHelper{

		public MyDataBase(Context context, String name, CursorFactory factory,
				int version) {
			super(context, name, factory, version);			
		}
		
		@Override
		public void onCreate(SQLiteDatabase db) {
			createInfoTable(db,"loginInfo");
		}
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			
		}
		
		public void createInfoTable(SQLiteDatabase db,String tableName){
			String str = "CREATE TABLE "+ tableName +"(" +
					"account VARCHAR PRIMARY KEY," +
					"password VARCHAR" +
					")";
			try{
				db.execSQL(str);
			} catch (SQLException e){
				e.printStackTrace();
			}
		}			
	}
}
