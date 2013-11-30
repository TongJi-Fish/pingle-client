package com.app.ipinle.ui;

import java.util.HashMap;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.ipinle.base.BaseMessage;
import com.app.ipinle.base.BaseUi;
import com.app.ipinle.base.C;
import com.app.ipinle.model.User;
import com.app.ipinle.util.AppUser;
import com.example.ipingle.R;

public class LoginUi extends BaseUi {

	private EditText userName;
	private EditText userPassword;
	private Button loginBtn;
	private Button registerBtn;
	private long exitTime = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		initial();
	}
	
	public void initial(){
		this.userName = (EditText) findViewById(R.id.userName);
		this.userPassword = (EditText) findViewById(R.id.userPassword);
		this.loginBtn = (Button) findViewById(R.id.login);
		this.loginBtn.setOnClickListener(mOnClickListener);
//		this.registerBtn = (Button) findViewById(R.id.btnRegister);
//		this.registerBtn.setOnClickListener(mOnClickListener);
	}
	
	OnClickListener mOnClickListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.login:
				doTaskLogin();
				break;
//			case R.id.btnRegister:
//				doTaskRegister();
//				break;
			}
		}
		
	};
	
	public void doTaskLogin(){
		if(this.userName.getText().length()>0 && this.userPassword.getText().length()>0){
			HashMap<String, String> urlParams = new HashMap<String,String>();
			urlParams.put("name", this.userName.getText().toString());
			urlParams.put("pwd", this.userPassword.getText().toString());
			try{
				this.showLoadBar();
				this.doTaskAsync(C.task.login, C.api.login, urlParams);
			}catch(Exception e){
				e.printStackTrace();
			}
		}else if(this.userName.getText().length()>0){
			this.toast("请输入密码！");
		}else{
			this.toast("请输入用户名！");
		}
	}
	
	public void doTaskRegister(){
		
	}
	
	@Override
	public void onTaskComplete(int taskId, BaseMessage message){
		super.onTaskComplete(taskId, message);
		switch (taskId) {
			case C.task.login:
				User user = null;
				// login logic
				try {
					user = (User) message.getResult("User");
					// login success
					if (user != null && user.getName() != null) {
						AppUser.setUser(user);
						AppUser.setLogin(true);
					// login fail
					} else {
						AppUser.setUser(user); // set sid
						AppUser.setLogin(false);
						this.toast(C.err.loginFailed);
					}
				} catch (Exception e) {
					e.printStackTrace();
					Log.i(C.debug.login, "2");
					this.toast(e.getMessage());
				}
				// turn to index
				if (AppUser.isLogin()) {
					// start service
					//BaseService.start(this, NoticeService.class);
					// turn to index
					// close load bar
					this.hideLoadBar();
					this.forward(TemplateUi.class);
				}
				break;
		}
	}
	
	@Override
	public void onNetworkError(int taskId){
		super.onTaskError();
		this.hideLoadBar();
	}
	
	/*
	 * (non-Javadoc)click back twice and leave this application
	 * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){

	    if((System.currentTimeMillis()-exitTime) > 2000){
	        Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();                                
	        exitTime = System.currentTimeMillis();
	    }
	    else{
	        finish();
	        System.exit(0);
	        }

	    return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
}
