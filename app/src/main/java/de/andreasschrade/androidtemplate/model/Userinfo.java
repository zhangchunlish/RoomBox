package de.andreasschrade.androidtemplate.model;

import cn.bmob.v3.BmobObject;

/**
 * @author alisa
 * 用户信息
 */
public class Userinfo extends BmobObject {
	
	private String userid;//编号
	private String username;//用户名
	private String password;//密码
	private String createtime;//创建时间
	private String updatetime;//更新时间
	private String state;//状态
	private String role;//角色
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
}
