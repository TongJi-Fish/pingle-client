package com.app.ipinle.model;

import com.app.ipinle.base.*;

public class User extends BaseModel{

	// model columns
	public final static String COL_ID = "id";
	public final static String COL_SID = "sid";
	public final static String COL_NAME = "name";
	public final static String ID_NUM = "id_num";
	public final static String DRIVER_ID = "driver_id";
	
	private String id;
	private String sid;
	private String name;
	private String id_num;
	private String driver_id;
	
	// default is no login
	private boolean isLogin = false;
	
	//singal instance for login
	static private User user = null;
	
	static public User getInstance(){
		if(User.user == null)
			User.user = new User();
		return User.user;
	}
	
	public User(){ }
	
	public void setId(String id){
		this.id = id;
	}
	
	public String getId(){
		return this.id;
	}
	
	public void setSid(String sid){
		this.sid = sid;
	}
	
	public String getSid(){
		return this.sid;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public void setIsLogin(boolean login){
		this.isLogin = login;
	}
	
	public boolean getIsLogin(){
		return this.isLogin;	
	}

	public String getId_num() {
		return id_num;
	}

	public void setId_num(String id_num) {
		this.id_num = id_num;
	}

	public String getDriver_id() {
		return driver_id;
	}

	public void setDriver_id(String driver_id) {
		this.driver_id = driver_id;
	}
	
}
