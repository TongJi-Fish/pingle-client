package com.app.ipinle.ui;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.ipingle.R;

public class InitScreenUi extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.ui_init_screen);
		
		final Intent intent = new Intent(this,LoginUi.class);
		
		Timer timer = new Timer();
		TimerTask task = new TimerTask(){
			@Override
			public void run(){
				startActivity(intent);
				finish();
			}
		};
		
		timer.schedule(task, 2*1000);
	}
}