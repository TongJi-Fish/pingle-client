package com.app.ipinle.util;

import com.app.ipinle.model.User;


public class AppUser {

	static public boolean isLogin(){
		User user = User.getInstance();
		return user.getIsLogin();
	}
	
	static public void setLogin(boolean login){
		User user = User.getInstance();
		user.setIsLogin(login);
	}
	
	static public User getUser(){
		return User.getInstance();
	}
	
	static public void setUser(User us){
		User user = User.getInstance();
		user.setId(us.getId());
		user.setId_num(us.getId_num());
		user.setIsLogin(us.getIsLogin());
		user.setName(us.getName());
		user.setSid(us.getSid());
	}
}
