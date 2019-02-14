package de.andreasschrade.androidtemplate.database;

import java.util.List;

import de.andreasschrade.androidtemplate.util.DBUtils;
import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteStatement;

import org.json.JSONArray;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.net.Uri;
import android.util.Log;

public class LiteProvider extends ContentProvider {

	public static final boolean READ_DATABASE = true;
	public static final boolean WRITE_DATABASE = true;
	// 静态引用
    public volatile static LiteProvider mInstance;
	LiteHelper helper;
	SQLiteDatabase db;

	private static final UriMatcher uriMatcher;

	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(DBUtils.AUTHORITY, DBUtils.DBNAME,
				DBUtils.ITEM);
	}
	public LiteProvider(Context context) {
    	helper = new LiteHelper(context);
    }
    public LiteProvider() {
    }
    /**
     * 获取单例引用
     * @param context
     * @return
     */
    public static LiteProvider getInstance(Context context) {
    	LiteProvider inst = mInstance;
        if (inst == null) {
            synchronized (LiteProvider.class) {
                inst = mInstance;
                if (inst == null) {
                    inst = new LiteProvider(context);
                    mInstance = inst;
                }
            }
        }
        return inst;
    }

	public int delete(Uri uri, String arg1, String[] arg2) {
		db = helper.getWritableDatabase(DBUtils.SECRETKEY);
		LiteHelper.removeAll(db);
		LiteHelper.initAll(db);
		return 0;
	}
	public String getType(Uri uri) {
		return DBUtils.AUTHORITY;
	}

	public Uri insert(Uri uri, ContentValues cv) {
		db = helper.getWritableDatabase(DBUtils.SECRETKEY);
		String sql = cv.getAsString("sql");
		SQLiteStatement stat = db.compileStatement(sql);
		boolean flag = false;
		db.beginTransaction();
		try {
			JSONArray values = new JSONArray(cv.getAsString("values"));
			Log.d("======db======", values.toString());
			for (int i = 0; i < values.length(); i++) {
				JSONArray arr = values.getJSONArray(i);
				for (int j = 0; j < arr.length(); j++) {
					stat.bindString(j+1, arr.getString(j));
				}
				stat.executeInsert();
			}
			db.setTransactionSuccessful();
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
			db.close();
		}
		if (!flag) {
			throw new IllegalStateException();
		}

		return null;
	}

	public boolean onCreate() {
		//helper = new LiteHelper(this.getContext());
		return true;
	}

	// 查询操作
	public Cursor query(Uri uri, String[] arg1, String sql, String[] params,
			String arg4) {
		db = helper.getReadableDatabase(DBUtils.SECRETKEY);
		return db.rawQuery(sql, params);
	}

	// 更新，插入操作
	public int update(Uri arg0, ContentValues arg1, String sql, String[] params) {
		try {
			db = helper.getWritableDatabase(DBUtils.SECRETKEY);
			db.execSQL(sql, params);
			db.close();
		} catch (Exception e) {
			Log.e("SQLite", e.getMessage());
			e.printStackTrace();
			return -1;
		}
		return 0;
	}
	public int updateBatch(Uri arg0, ContentValues arg1, String sql,List<String> idlist) {
		db = helper.getWritableDatabase(DBUtils.SECRETKEY);
		db.beginTransaction();
	    try {
			for (int i = 0; i < idlist.size(); i++) {
				db.execSQL(sql, new String[]{idlist.get(i)});
			}
			return 0;
		} catch (Exception e) {
		    Log.e("SQLite", e.getMessage());
			e.printStackTrace();
			return -1;
		}finally{
		    db.setTransactionSuccessful();
			db.endTransaction();
			db.close();
		}
	}
}
