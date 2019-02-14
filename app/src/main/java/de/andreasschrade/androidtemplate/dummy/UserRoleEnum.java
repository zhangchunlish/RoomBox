package de.andreasschrade.androidtemplate.dummy;

public enum UserRoleEnum {
	ADMIN("admin","管理员"),
	USER("user","普通用户");

	private String  code;
	private String message;
	UserRoleEnum(String code,String message){
		this.code=code;
		this.message=message;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String  code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public static String enumName(String code){
		for (UserRoleEnum item:values()){
			if(item.getCode()==code){
				return item.getMessage();
			}
		}
		return "";
	}
}
