package de.andreasschrade.androidtemplate.dummy;

import java.util.ArrayList;
import java.util.List;

import de.andreasschrade.androidtemplate.model.Room;
import de.andreasschrade.androidtemplate.model.Userinfo;

/**
 * 返回結果
 * Created by alisa on 2019/1/29.
 */

public class ResultCode{
    private int status;
    private String msg;
    private List<Room> roomList=new ArrayList<Room>();
    private List<Userinfo> userinfoList=new ArrayList<Userinfo>();

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<Room> getRoomList() {
        return roomList;
    }

    public void setRoomList(List<Room> roomList) {
        this.roomList = roomList;
    }

    public List<Userinfo> getUserinfoList() {
        return userinfoList;
    }

    public void setUserinfoList(List<Userinfo> userinfoList) {
        this.userinfoList = userinfoList;
    }
}
