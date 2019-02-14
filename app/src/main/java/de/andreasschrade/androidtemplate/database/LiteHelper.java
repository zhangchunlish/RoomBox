package de.andreasschrade.androidtemplate.database;

import java.io.File;

import de.andreasschrade.androidtemplate.util.DBUtils;
import de.andreasschrade.androidtemplate.util.InitDB;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;


import android.content.Context;
import android.util.Log;

public class LiteHelper extends SQLiteOpenHelper {

	private Context ctx;

	public LiteHelper(Context context) {
		super(context, DBUtils.DBNAME, null,DBUtils.VERSION);
		SQLiteDatabase.loadLibs(context);
		this.ctx = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.i("SQLite", "Create");
		initAll(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion >= newVersion){
			return;
		}else{
			//升级备份数据库
			//TODO
		}
		
	}

	public static void removeAll(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + DBUtils.RB_ROOM);
		db.execSQL("DROP TABLE IF EXISTS " + DBUtils.RB_LOGINFO);
		db.execSQL("DROP TABLE IF EXISTS " + DBUtils.RB_USERINFO);
	}

	
	public static void initAll(SQLiteDatabase db) {
		db.execSQL(InitDB.CREATE_ROOM);
		db.execSQL(InitDB.CREATE_LOGINFO);
		db.execSQL(InitDB.CREATE_USERINFO);
		db.execSQL(InitDB.CREATE_NEXTNO);
		db.execSQL(InitDB.INIT_DATA);
	}

	
}
