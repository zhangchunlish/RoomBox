package de.andreasschrade.androidtemplate.util;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.andreasschrade.androidtemplate.Constants;

public class InitDB {
	/**
	 * 房间表
	 */
	public static final String CREATE_ROOM = "CREATE TABLE "
			+ DBUtils.RB_ROOM + "(ID TEXT PRIMARY KEY ," 
			+ "ROOMID TEXT,"
			+ "CREATETIME TEXT," 
			+ "UPDATETIME TEXT," 
			+ "STATE TEXT," 
			+ "COMMENT TEXT," 
			+ "USERNAME TEXT," 
			+ "PASSWORD TEXT,"
			+ "CREATOR TEXT)";
	/**
	 * 日志信息
	 */
	public static final String CREATE_LOGINFO = "CREATE TABLE "
			+ DBUtils.RB_LOGINFO
			+ "(logid TEXT PRIMARY KEY,"
			+ "USERID TEXT ,ROOMID TEXT, CREATETIME TEXT, COMMENT TEXT)";
	/**
	 * 用户信息表
	 */
	public static final String CREATE_USERINFO = "CREATE TABLE "
			+ DBUtils.RB_USERINFO
			+ "(USERID TEXT PRIMARY KEY ,"
			+ "USERNAME TEXT NOT NULL," 
			+ "PASSWORD TEXT NOT NULL," 
			+ "CREATETIME TEXT NOT NULL," 
			+ "UPDATETIME TEXT NOT NULL,"
			+ "ROLE TEXT NOT NULL,"
			+ "STATE TEXT NOT NULL)";
	/**
	 * 下个序号表0000-9999
	 */
	public static final String CREATE_NEXTNO = "CREATE TABLE "
			+ DBUtils.RB_NEXTNO + "(id TEXT PRIMARY KEY ," + "no TEXT)";


	public static final String INIT_DATA = "insert into "
			+ DBUtils.RB_USERINFO + " values('1','"+ Constants.BOXADMIN+"','"+ Constants.BOXPASSWORD+"','"+DataImportUtil.sdf.format(new Date())+"','"+DataImportUtil.sdf.format(new Date())+"','admin','enable')";


}