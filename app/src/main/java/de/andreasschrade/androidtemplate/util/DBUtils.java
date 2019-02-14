package de.andreasschrade.androidtemplate.util;

import android.net.Uri;

public class DBUtils {
	public static final String DBNAME = "roombox";// 数据库名称
	public static final String RB_ROOM = "rb_room"; //房间信息
	public static final String RB_LOGINFO = "rb_loginfo";// 日志信息
	public static final String RB_USERINFO = "rb_userinfo"; // 用户信息
	public static final String RB_NEXTNO = "rb_nextno";
	
	public static final String SECRETKEY = "zyf"; // v1
	public static final int VERSION = 1; // v1
	public static final String AUTHORITY = "net.cgt.weixin";
	public static final int ITEM = 1;
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
			+ "/" + DBNAME);
}
