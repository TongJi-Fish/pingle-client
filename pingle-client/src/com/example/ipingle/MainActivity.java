package com.example.ipingle;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Button;

import com.app.ipinle.ui.LoginUi;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button button = (Button) findViewById(R.id.login);
		
		OnClickListener mOnClickListener = new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				forward(LoginUi.class);
			}
			
		};
		
		button.setOnClickListener((android.view.View.OnClickListener) mOnClickListener);
		
		Button mapButton = (Button) findViewById(R.id.mapButton);
		mapButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, MapActivity.class);
				startActivity(intent);
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	// function*********************************************************
	public void forward(Class<?> object){
		Intent intent = new Intent();
		intent.setClass(this, object);
		this.startActivity(intent);
	}

}
