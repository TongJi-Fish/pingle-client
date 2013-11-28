package com.app.ipinle.base;

import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class BaseUi extends Activity {

	protected BaseApp app;
	protected BaseHandler handler;
	protected BaseTaskPool taskPool;
	protected boolean showLoadBar = false;
	protected boolean showDebugMsg = true;
	
	/** Called when the activity is first created. */
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		// debug memory
		//debugMemory("onCreate");
		// async task handler
		this.handler = new BaseHandler(this);
		// init task pool
		this.taskPool = new BaseTaskPool(this);
		// init application
		//this.app = (BaseApp) this.getApplicationContext();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////
	// util method
	
	public Context getContext () {
		return this;
	}
	
	public void toast(String msg){
		Toast.makeText(this, "msg", Toast.LENGTH_LONG);
	}
	
	public void hideLoadBar(){
	}
	
	public void showLoadBar(){
	}
	
	public void onNetworkError(int taskId){
	}
	
	
	////////////////////////////////////////////////////////////////////////////////////////////////
	// logic method
	
	public void sendMessage (int what, int taskId, String data) {
		Bundle b = new Bundle();
		b.putInt("task", taskId);
		b.putString("data", data);
		Message m = new Message();
		m.what = what;
		m.setData(b);
		handler.sendMessage(m);
		if(what==BaseTask.NETWORK_ERROR)
		Log.i(C.debug.login, "what = NETWORK_ERROR");
		Log.i(C.debug.login, "sendMessage");
	}
	
	public void doTaskAsync (int taskId, String taskUrl, HashMap<String, String> taskArgs) {
		showLoadBar();
		taskPool.addTask(taskId, taskUrl, taskArgs, new BaseTask(){
			@Override
			public void onComplete (String httpResult) {
				sendMessage(BaseTask.TASK_COMPLETE, this.getId(), httpResult);
			}
			@Override
			public void onError (String error) {
				sendMessage(BaseTask.NETWORK_ERROR, this.getId(), null);
			}
		}, 0);
	}
	
	public void forward(Class<?> classObject){
		Intent intent = new Intent();
		intent.setClass(this, classObject);
		startActivity(intent);
	}
	
	public void onTaskComplete(int taskId){
	}
	
	public void onTaskComplete(int taskId, BaseMessage message){
	}
	
	public void onTaskError(){	
		Log.i(C.debug.login,"base ui error");
	}
}