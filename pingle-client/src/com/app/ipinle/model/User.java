package com.app.ipinle.model;

public class User {

	private String id;
	private String sid;
	private String name;
	private String password;
	private String sign;
	
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
	
	public void setPassword(String pass){
		this.password = pass;
	}
	
	public String getPassword(){
		return this.password;
	}
	
	public void setIsLogin(boolean islogin){
		this.isLogin = isLogin;
	}
	
	public boolean getIsLogin(){
		return this.isLogin;	
	}
	
}
