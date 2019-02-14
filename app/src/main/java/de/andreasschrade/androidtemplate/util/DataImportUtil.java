package de.andreasschrade.androidtemplate.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import net.sqlcipher.Cursor;
import android.content.Context;
import android.util.Log;

import de.andreasschrade.androidtemplate.Constants;
import de.andreasschrade.androidtemplate.model.*;
import de.andreasschrade.androidtemplate.database.*;

/**
 * @author alisa 数据增删改查
 */
public class DataImportUtil {
	public static final String dateformat_log = "yyyy-MM-dd HH:mm:ss";
	public static SimpleDateFormat sdf = new SimpleDateFormat(dateformat_log,
			Locale.CHINESE);

	// 新增room
	public static int addRoom(Context context, Room room) {
		try {
			if (room == null) {
				return -1;
			}
			List<Room> roomlist=getRoomByID(context,room.getRoomid());
			if(roomlist.size()>0){
				return 1;
			}
			LiteProvider pr = LiteProvider.getInstance(context);
			String sql = "insert into " + DBUtils.RB_ROOM
					+ " values(?,?,?,?,?,?,?,?,?)";
			room.setId(generateEightLengthString());
			room.setCreatetime(sdf.format(new Date()));
			room.setUpdatetime(sdf.format(new Date()));
			room.setState("1");
			String[] params = { room.getId(), room.getRoomid(),
					room.getCreatetime(), room.getUpdatetime(),
					room.getState(), room.getComment(), room.getUsername(),
					room.getPassword(),room.getCreator() };
			int res = pr.update(DBUtils.CONTENT_URI, null, sql, params);
			Log.d("DataImportUtil", sql + (res == 0 ? " 成功" : " 失败"));
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			Log.d("DataImportUtil", e.getMessage());
			return -1;
		}
	}

	// 删除room
	public static int deleteRoom(Context context, String id) {
		try {
			LiteProvider pr = LiteProvider.getInstance(context);
			String sql = "delete from " + DBUtils.RB_ROOM + " where id = ?";
			String[] params = { id };
			int res = pr.update(DBUtils.CONTENT_URI, null, sql, params);
			Log.d("DataImportUtil", sql + (res == 0 ? " 成功" : " 失败"));
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			Log.d("DataImportUtil", e.getMessage());
			return -1;
		}
	}

	// 更新room
	public static int updateRoom(Context context, Room room) {
		try {
			if (room == null) {
				return -1;
			}
			LiteProvider pr = LiteProvider.getInstance(context);
			room.setUpdatetime(sdf.format(new Date()));
			String sql = "update "
					+ DBUtils.RB_ROOM
					+ " set roomid = ?,updatetime = ?,state = ?,comment = ?,username = ?,password = ? where id = ?";
			String[] params = { room.getRoomid(), room.getUpdatetime(),
					room.getState(), room.getComment(), room.getUsername(),
					room.getPassword(), room.getId() };
			int res = pr.update(DBUtils.CONTENT_URI, null, sql, params);
			Log.d("DataImportUtil", sql + (res == 0 ? " 成功" : " 失败"));
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			Log.d("DataImportUtil", e.getMessage());
			return -1;
		}
	}

	// 查询room列表
	public static List<Room> getRoomList(Context context,String creator) {
		Cursor cursor = null;
		List<Room> infolist = new ArrayList<Room>();
		String[] params = {};
		try {
			LiteProvider pr = LiteProvider.getInstance(context);
			String sql="";
			if(Constants.BOXADMIN.equals(creator)){
				 sql="select * from "
						+ DBUtils.RB_ROOM + " a order by a.updatetime desc";
			}else{
				sql="select * from "
						+ DBUtils.RB_ROOM + " a  where a.creator = ? order by a.updatetime desc";
				params=new String []{creator};
			}
			cursor = pr.query(DBUtils.CONTENT_URI, null,sql ,
					params, null);
			while (cursor.moveToNext()) {
				Room room = new Room();
				room.setId(cursor.getString(0));
				room.setRoomid(cursor.getString(1));
				room.setCreatetime(cursor.getString(2));
				room.setUpdatetime(cursor.getString(3));
				room.setState(cursor.getString(4));
				room.setComment(cursor.getString(5));
				room.setUsername(cursor.getString(6));
				room.setPassword(cursor.getString(7));
				room.setCreator(cursor.getString(8));
				infolist.add(room);
			}
			return infolist;
		} catch (Exception e) {
			e.printStackTrace();
			Log.d("DataImportUtil", e.getMessage());
			return infolist;
		} finally {
			if (cursor != null)
				cursor.close();
		}
	}
	// 查询room列表--模糊查询
	public static List<Room> getRoomList(Context context,String creator,String queryStr) {
		Cursor cursor = null;
		List<Room> infolist = new ArrayList<Room>();
		String[] params = {};
		try {
			LiteProvider pr = LiteProvider.getInstance(context);
			String sql="";
			if(Constants.BOXADMIN.equals(creator)){
				sql="select * from "
						+ DBUtils.RB_ROOM + " a where a.roomid like ? order by a.updatetime desc";
				params=new String []{'%'+queryStr+'%'};
			}else{
				sql="select * from "
						+ DBUtils.RB_ROOM + " a  where a.creator = ? and a.roomid like ? order by a.updatetime desc";
				params=new String []{creator,'%'+queryStr+'%'};
			}
			cursor = pr.query(DBUtils.CONTENT_URI, null,sql ,
					params, null);
			while (cursor.moveToNext()) {
				Room room = new Room();
				room.setId(cursor.getString(0));
				room.setRoomid(cursor.getString(1));
				room.setCreatetime(cursor.getString(2));
				room.setUpdatetime(cursor.getString(3));
				room.setState(cursor.getString(4));
				room.setComment(cursor.getString(5));
				room.setUsername(cursor.getString(6));
				room.setPassword(cursor.getString(7));
				room.setCreator(cursor.getString(8));
				infolist.add(room);
			}
			return infolist;
		} catch (Exception e) {
			e.printStackTrace();
			Log.d("DataImportUtil", e.getMessage());
			return infolist;
		} finally {
			if (cursor != null)
				cursor.close();
		}
	}
	// 查询room-精确查询
	public static List<Room> getRoomByRoomID(Context context, String roomid) {
		Cursor cursor = null;
		List<Room> infoList = new ArrayList<Room>();
		String[] params = { roomid };
		try {
			LiteProvider pr = LiteProvider.getInstance(context);
			cursor = pr.query(DBUtils.CONTENT_URI, null, "select * from "
					+ DBUtils.RB_ROOM + " a where a.roomid=?", params, null);
			while (cursor.moveToNext()) {
				Room room = new Room();
				room.setId(cursor.getString(0));
				room.setRoomid(cursor.getString(1));
				room.setCreatetime(cursor.getString(2));
				room.setUpdatetime(cursor.getString(3));
				room.setState(cursor.getString(4));
				room.setComment(cursor.getString(5));
				room.setUsername(cursor.getString(6));
				room.setPassword(cursor.getString(7));
				infoList.add(room);
			}
			return infoList;
		} catch (Exception e) {
			e.printStackTrace();
			Log.d("DataImportUtil", e.getMessage());
			return infoList;
		} finally {
			if (cursor != null)
				cursor.close();
		}
	}
	// 查询room
	public static List<Room> getRoomByID(Context context, String id) {
		Cursor cursor = null;
		List<Room> infoList = new ArrayList<Room>();
		String[] params = { id };
		try {
			LiteProvider pr = LiteProvider.getInstance(context);
			cursor = pr.query(DBUtils.CONTENT_URI, null, "select * from "
					+ DBUtils.RB_ROOM + " a where a.id=?", params, null);
			while (cursor.moveToNext()) {
				Room room = new Room();
				room.setId(cursor.getString(0));
				room.setRoomid(cursor.getString(1));
				room.setCreatetime(cursor.getString(2));
				room.setUpdatetime(cursor.getString(3));
				room.setState(cursor.getString(4));
				room.setComment(cursor.getString(5));
				room.setUsername(cursor.getString(6));
				room.setPassword(cursor.getString(7));
				room.setCreator(cursor.getString(8));
				infoList.add(room);
			}
			return infoList;
		} catch (Exception e) {
			e.printStackTrace();
			Log.d("DataImportUtil", e.getMessage());
			return infoList;
		} finally {
			if (cursor != null)
				cursor.close();
		}
	}
	// 新增用户
	public static int addUserinfo(Context context, Userinfo userinfo) {
		try {
			if (userinfo == null) {
				return -1;
			}
			List<Userinfo> infolist = getUserByName(context,userinfo.getUsername());
			if(infolist.size()>0){
				return 1;
			}
			LiteProvider pr = LiteProvider.getInstance(context);
			String sql = "insert into " + DBUtils.RB_USERINFO
					+ " values(?,?,?,?,?,?,?)";
			userinfo.setUserid(generateEightLengthString());
			userinfo.setCreatetime(sdf.format(new Date()));
			userinfo.setUpdatetime(sdf.format(new Date()));
			userinfo.setRole("user");
			userinfo.setState("enable");
			String[] params = { userinfo.getUserid(), userinfo.getUsername(),
					userinfo.getPassword(), userinfo.getCreatetime(),
					userinfo.getUpdatetime(),userinfo.getRole(), userinfo.getState() };
			int res = pr.update(DBUtils.CONTENT_URI, null, sql, params);
			Log.d("DataImportUtil", sql + (res == 0 ? " 成功" : " 失败"));
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			Log.d("DataImportUtil", e.getMessage());
			return -1;
		}
	}

	// 删除用户
	public static int deleteUserinfo(Context context, String userid) {
		try {
			LiteProvider pr = LiteProvider.getInstance(context);
			String sql = "delete from " + DBUtils.RB_USERINFO
					+ " where userid = ?";
			String[] params = { userid };
			int res = pr.update(DBUtils.CONTENT_URI, null, sql, params);
			Log.d("DataImportUtil", sql + (res == 0 ? " 成功" : " 失败"));
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			Log.d("DataImportUtil", e.getMessage());
			return -1;
		}
	}

	// 更新用户
	public static int updateUserinfo(Context context, Userinfo userinfo) {
		try {
			if (userinfo == null) {
				return -1;
			}
			LiteProvider pr = LiteProvider.getInstance(context);
			userinfo.setUpdatetime(sdf.format(new Date()));
			String sql = "update "
					+ DBUtils.RB_USERINFO
					+ " set username = ?,password = ?,state = ? where userid = ?";
			String[] params = { userinfo.getUsername(), userinfo.getPassword(),
					userinfo.getState(), userinfo.getUserid() };
			int res = pr.update(DBUtils.CONTENT_URI, null, sql, params);
			Log.d("DataImportUtil", sql + (res == 0 ? " 成功" : " 失败"));
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			Log.d("DataImportUtil", e.getMessage());
			return -1;
		}
	}

	// 查询用户列表
	public static List<Userinfo> getUserinfoList(Context context) {
		Cursor cursor = null;
		List<Userinfo> infolist = new ArrayList<Userinfo>();
		String[] params = {};
		try {
			LiteProvider pr = LiteProvider.getInstance(context);
			cursor = pr.query(DBUtils.CONTENT_URI, null, "select * from "
					+ DBUtils.RB_USERINFO + " a order by a.updatetime desc",
					params, null);
			while (cursor.moveToNext()) {
				Userinfo userinfo = new Userinfo();
				userinfo.setUserid(cursor.getString(0));
				userinfo.setUsername(cursor.getString(1));
				userinfo.setPassword(cursor.getString(2));
				userinfo.setCreatetime(cursor.getString(3));
				userinfo.setUpdatetime(cursor.getString(4));
				userinfo.setRole(cursor.getString(5));
				userinfo.setState(cursor.getString(6));
				infolist.add(userinfo);
			}
			return infolist;
		} catch (Exception e) {
			e.printStackTrace();
			Log.d("DataImportUtil", e.getMessage());
			return infolist;
		} finally {
			if (cursor != null)
				cursor.close();
		}
	}

	// 查询用户
	public static List<Userinfo> getUserByID(Context context, String userid) {
		Cursor cursor = null;
		List<Userinfo> infoList = new ArrayList<Userinfo>();
		String[] params = { userid };
		try {
			LiteProvider pr = LiteProvider.getInstance(context);
			cursor = pr
					.query(DBUtils.CONTENT_URI, null, "select * from "
							+ DBUtils.RB_USERINFO + " a where a.userid=?",
							params, null);
			while (cursor.moveToNext()) {
				Userinfo userinfo = new Userinfo();
				userinfo.setUserid(cursor.getString(0));
				userinfo.setUsername(cursor.getString(1));
				userinfo.setPassword(cursor.getString(2));
				userinfo.setCreatetime(cursor.getString(3));
				userinfo.setUpdatetime(cursor.getString(4));
				userinfo.setRole(cursor.getString(5));
				userinfo.setState(cursor.getString(6));
				infoList.add(userinfo);
			}
			return infoList;
		} catch (Exception e) {
			e.printStackTrace();
			Log.d("DataImportUtil", e.getMessage());
			return infoList;
		} finally {
			if (cursor != null)
				cursor.close();
		}
	}
	// 查询用户
	public static List<Userinfo> getUserByName(Context context, String username) {
		Cursor cursor = null;
		List<Userinfo> infoList = new ArrayList<Userinfo>();
		String[] params = { username };
		try {
			LiteProvider pr = LiteProvider.getInstance(context);
			cursor = pr
					.query(DBUtils.CONTENT_URI, null, "select * from "
							+ DBUtils.RB_USERINFO + " a where a.username=?",
							params, null);
			while (cursor.moveToNext()) {
				Userinfo userinfo = new Userinfo();
				userinfo.setUserid(cursor.getString(0));
				userinfo.setUsername(cursor.getString(1));
				userinfo.setPassword(cursor.getString(2));
				userinfo.setCreatetime(cursor.getString(3));
				userinfo.setUpdatetime(cursor.getString(4));
				userinfo.setRole(cursor.getString(5));
				userinfo.setState(cursor.getString(6));
				infoList.add(userinfo);
			}
			return infoList;
		} catch (Exception e) {
			e.printStackTrace();
			Log.d("DataImportUtil", e.getMessage());
			return infoList;
		} finally {
			if (cursor != null)
				cursor.close();
		}
	}
	//登录
	public static boolean login(Context context, String username,
			String password) {
		Cursor cursor = null;
		boolean loginSuc = false;
		String[] params = { username, password };
		try {
			LiteProvider pr = LiteProvider.getInstance(context);
			cursor = pr.query(DBUtils.CONTENT_URI, null, "select * from "
					+ DBUtils.RB_USERINFO
					+ " a where a.username=? and password=?", params, null);
			if (cursor.getCount() > 0) {
				loginSuc = true;
			}
			return loginSuc;
		} catch (Exception e) {
			e.printStackTrace();
			Log.d("DataImportUtil", e.getMessage());
			return loginSuc;
		} finally {
			if (cursor != null)
				cursor.close();
		}
	}

	public static String generateEightLengthString() {
		Random random = new Random();
		Integer ivalue = random.nextInt(99999999);
		String svalue = "00000000" + ivalue.toString();
		return svalue.substring(svalue.length() - 8);
	}

}
