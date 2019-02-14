package de.andreasschrade.androidtemplate.util;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import net.sqlcipher.Cursor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import de.andreasschrade.androidtemplate.Constants;
import de.andreasschrade.androidtemplate.database.LiteProvider;
import de.andreasschrade.androidtemplate.dummy.ResultCode;
import de.andreasschrade.androidtemplate.dummy.UserRoleEnum;
import de.andreasschrade.androidtemplate.model.Room;
import de.andreasschrade.androidtemplate.model.Userinfo;

/**
 * Bmob数据存储
 * Created by alisa on 2019/1/29.
 */

public class BmobDataUtil {

    /**
     * 添加Room
     **/
    public static void addRoom(final Room room,final Handler handler) {
        if (room == null) {
           handler.sendEmptyMessage(HandlerTypeUtils.WX_HANDLER_TYPE_LOAD_DATA_FAIL);
        }
        BmobQuery<Room> query = new BmobQuery<Room>();
        query.addWhereContains("roomid", room.getRoomid());
        //执行查询方法
        query.findObjects(new FindListener<Room>() {
            @Override
            public void done(List<Room> object, BmobException e) {
                if (e == null) {
                    if(object.size()==0){
                        room.save(new SaveListener<String>() {
                            @Override
                            public void done(String objectId, BmobException e) {
                                if (e == null) {
                                    //添加成功
                                    handler.sendEmptyMessage(HandlerTypeUtils.WX_HANDLER_TYPE_LOAD_DATA_SUCCESS);
                                    Log.d("BmobDataUtil", "========objectId=" + objectId);
                                } else {
                                    //添加失败
                                    handler.sendEmptyMessage(HandlerTypeUtils.WX_HANDLER_TYPE_LOAD_DATA_FAIL);
                                    Log.d("BmobDataUtil", "========e=" + e.getErrorCode() + "   errorMessage=" + e.getMessage());
                                }
                            }
                        });
                    }else{
                        handler.sendEmptyMessage(HandlerTypeUtils.WX_HANDLER_TYPE_LOAD_DATA_SAME);
                    }
                } else {
                    handler.sendEmptyMessage(HandlerTypeUtils.WX_HANDLER_TYPE_LOAD_DATA_FAIL);
                    LogUtil.logI("BmobDataUtil", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    /**
     * 删除整条数据
     **/
    public static ResultCode deleteRoom(Room room) {
        final ResultCode res=new ResultCode();
        if (room == null) {
            res.setStatus(-1);
            return res;
        }
        room.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    res.setStatus(-1);
                    Log.d("BmobDataUtil", "===删除成功===");
                } else {
                    res.setStatus(0);
                    Log.d("BmobDataUtil", "删除失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
        return res;
    }

    // 更新room
    public static ResultCode updateRoom(Room room) {
        final ResultCode res=new ResultCode();
        if (room == null) {
            res.setStatus(-1);
            return res;
        }
        room.update(room.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    res.setStatus(-1);
                    Log.d("BmobDataUtil", "===更新成功===");
                } else {
                    res.setStatus(0);
                    Log.d("BmobDataUtil", "更新失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
        return res;
    }
    // 查询room列表
    /**
     * 查询多条数据,查询的数据条数最多500
     **/
    public static void getRoomList(String creator,final Handler handler) {
        BmobQuery<Room> query = new BmobQuery<Room>();
        if (Constants.BOXADMIN.equals(creator)) {
        } else {
            query.addWhereEqualTo("creator", creator);
        }
        query.order("-updatedAt");
        //返回500条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(500);
        //执行查询方法
        query.findObjects(new FindListener<Room>() {
            @Override
            public void done(List<Room> object, BmobException e) {
                if (e == null) {
                    Message message = handler.obtainMessage();
                    message.what = 0;
                    //以消息为载体
                    message.obj = object;//这里的list就是查询出list
                    //向handler发送消息
                    handler.sendMessage(message);
                } else {
                    LogUtil.logI("BmobDataUtil", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }
    // 查询room列表
    /**
     * 查询多条数据,查询的数据条数最多500
     **/
    public static void getRoomList(String creator, String queryStr,final Handler handler) {
        final ResultCode res=new ResultCode();
        BmobQuery<Room> query = new BmobQuery<Room>();
        if (Constants.BOXADMIN.equals(creator)) {
        } else {
            query.addWhereEqualTo("creator", creator);
        }
        query.addWhereEqualTo("roomid", queryStr);
        query.order("-updatedAt");
        //返回500条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(500);
        //执行查询方法
        query.findObjects(new FindListener<Room>() {
            @Override
            public void done(List<Room> object, BmobException e) {
                if (e == null) {
                    Message message = handler.obtainMessage();
                    message.what = 1;
                    //以消息为载体
                    message.obj = object;//这里的list就是查询出list
                    //向handler发送消息
                    handler.sendMessage(message);
                } else {
                    LogUtil.logI("BmobDataUtil", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    /**
     * 单个查询
     **/
    public static void getRoomByObjectId(String objectId,final Handler handler) {
        new BmobQuery<Room>().getObject(objectId, new QueryListener<Room>() {
            @Override
            public void done(Room room, BmobException e) {
                if (e == null) {
                    Message message = handler.obtainMessage();
                    message.what = 0;
                    //以消息为载体
                    message.obj = room;//这里的list就是查询出list
                    //向handler发送消息
                    handler.sendMessage(message);
                } else {
                    Log.d("BmobDataUtil", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    public static void getRoomByRoomID(String roomid,final Handler handler) {
        BmobQuery<Room> query = new BmobQuery<Room>();
        query.addWhereContains("roomid", roomid);
        query.order("-updatedAt");
        //返回500条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(500);
        //执行查询方法
        query.findObjects(new FindListener<Room>() {
            @Override
            public void done(List<Room> object, BmobException e) {
                if (e == null) {
                    Message message = handler.obtainMessage();
                    message.what = 0;
                    //以消息为载体
                    message.obj = object;//这里的list就是查询出list
                    //向handler发送消息
                    handler.sendMessage(message);
                } else {
                    LogUtil.logI("BmobDataUtil", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    // 新增用户
    public static ResultCode addUserinfo(final Userinfo userinfo) {
        final ResultCode res=new ResultCode();
        if (userinfo == null) {
            res.setStatus(-1);
            return res;
        }
        BmobQuery<Userinfo> query = new BmobQuery<Userinfo>();
        query.addWhereEqualTo("username", userinfo.getUsername());
        //执行查询方法
        query.findObjects(new FindListener<Userinfo>() {
            @Override
            public void done(List<Userinfo> object, BmobException e) {
                if (e == null) {
                    if(object.size()==0){
                        userinfo.save(new SaveListener<String>() {
                            @Override
                            public void done(String objectId, BmobException e) {
                                if (e == null) {
                                    //添加成功
                                    res.setStatus(0);
                                    Log.d("BmobDataUtil", "========objectId=" + objectId);
                                } else {
                                    //添加失败
                                    res.setStatus(-1);
                                    Log.d("BmobDataUtil", "========e=" + e.getErrorCode() + "   errorMessage=" + e.getMessage());
                                }
                            }
                        });
                    }else{
                        res.setStatus(1);
                    }
                } else {
                    res.setStatus(-1);
                    LogUtil.logI("BmobDataUtil", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
        return res;
    }
    // 删除用户
    public static ResultCode deleteUserinfo(Userinfo userinfo) {
        final ResultCode res=new ResultCode();
        if (userinfo == null) {
            res.setStatus(-1);
            return res;
        }
        userinfo.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    res.setStatus(0);
                    Log.d("BmobDataUtil", "===删除成功===");
                } else {
                    res.setStatus(-1);
                    Log.d("BmobDataUtil", "删除失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
        return res;
    }
    // 更新用户
    public static ResultCode updateUserinfo(Userinfo userinfo) {
        final ResultCode res=new ResultCode();
        if (userinfo == null) {
            res.setStatus(-1);
            return res;
        }
        userinfo.update(userinfo.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    res.setStatus(0);
                    Log.d("BmobDataUtil", "===更新成功===");
                } else {
                    res.setStatus(-1);
                    Log.d("BmobDataUtil", "更新失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
        return res;
    }
    // 查询room列表
    /**
     * 查询多条数据,查询的数据条数最多500
     **/
    public static void getUserinfoList(final Handler handler) {
        BmobQuery<Userinfo> query = new BmobQuery<Userinfo>();
        query.order("-updatedAt");
        //返回500条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(500);
        //执行查询方法
        query.findObjects(new FindListener<Userinfo>() {
            @Override
            public void done(List<Userinfo> object, BmobException e) {
                if (e == null) {
                    Message message = handler.obtainMessage();
                    message.what = 0;
                    //以消息为载体
                    message.obj = object;//这里的list就是查询出list
                    //向handler发送消息
                    handler.sendMessage(message);
                } else {
                    LogUtil.logI("BmobDataUtil", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }
    // 查询用户
    public static void getUserByID(String objectId,final Handler handler) {
        final ResultCode res=new ResultCode();
        new BmobQuery<Userinfo>().getObject(objectId, new QueryListener<Userinfo>() {
            @Override
            public void done(Userinfo user, BmobException e) {
                if (e == null) {
                    Message message = handler.obtainMessage();
                    message.what = 0;
                    //以消息为载体
                    message.obj = user;//这里的list就是查询出list
                    //向handler发送消息
                    handler.sendMessage(message);
                    //获得createdAt数据创建时间（注意是：createdAt，不是createAt）
                    Log.d("BmobDataUtil", "======user  username=" + user.getUsername());
                } else {
                    Log.d("BmobDataUtil", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }
    // 查询用户
    public static void getUserByName(String username,final Handler handler) {
        BmobQuery<Userinfo> query = new BmobQuery<Userinfo>();
        query.addWhereEqualTo("username", username);
        query.order("-updatedAt");
        //返回500条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(500);
        //执行查询方法
        query.findObjects(new FindListener<Userinfo>() {
            @Override
            public void done(List<Userinfo> object, BmobException e) {
                if (e == null) {
                    Message message = handler.obtainMessage();
                    message.what = 0;
                    //以消息为载体
                    message.obj = object;//这里的list就是查询出list
                    //向handler发送消息
                    handler.sendMessage(message);
                } else {
                    LogUtil.logI("BmobDataUtil", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }
    //登录
    public static void login(String username,String password,final Handler handler) {
        BmobQuery<Userinfo> query = new BmobQuery<Userinfo>();
        query.addWhereEqualTo("username", username);
        query.addWhereEqualTo("password", password);
        query.order("-updatedAt");
        //执行查询方法
        query.findObjects(new FindListener<Userinfo>() {
            @Override
            public void done(List<Userinfo> object, BmobException e) {
                if (e == null) {
                    if(object.size()>0){
                        handler.sendEmptyMessage(HandlerTypeUtils.WX_HANDLER_TYPE_LOAD_DATA_SUCCESS);
                    }else{
                        handler.sendEmptyMessage(HandlerTypeUtils.WX_HANDLER_TYPE_LOAD_DATA_FAIL);
                    }
                } else {
                    handler.sendEmptyMessage(HandlerTypeUtils.WX_HANDLER_TYPE_LOAD_DATA_FAIL);
                    LogUtil.logI("BmobDataUtil", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

}
