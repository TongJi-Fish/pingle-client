package com.app.ipinle.ui;

import java.util.HashMap;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.app.ipinle.base.BaseMessage;
import com.app.ipinle.base.BaseUi;
import com.app.ipinle.base.C;
import com.example.ipingle.MainActivity;
import com.example.ipingle.R;

public class LoginUi extends BaseUi {

	private EditText userName;
	private EditText userPassword;
	private Button loginBtn;
	private Button registerBtn;
	
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
				this.doTaskAsync(C.task.login, C.api.login, urlParams);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public void doTaskRegister(){
		
	}
	
	@Override
	public void onTaskComplete(int taskId, BaseMessage message){
		//this.finish();
		this.forward(MainActivity.class);
	}
	
	@Override
	public void onNetworkError(int taskId){
		super.onTaskError();
		this.toast("error login");
		Log.i(C.debug.login, "login loginUiActivity error");
	}
	
}
