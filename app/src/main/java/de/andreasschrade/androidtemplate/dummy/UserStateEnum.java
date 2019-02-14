package de.andreasschrade.androidtemplate.dummy;

public enum UserStateEnum {
	ENABLE("enable","可用"),
	DISABLE("disable","停用");

	private String  code;
	private String message;
	UserStateEnum(String code, String message){
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
		for (UserStateEnum item:values()){
			if(item.getCode()==code){
				return item.getMessage();
			}
		}
		return "";
	}
}
