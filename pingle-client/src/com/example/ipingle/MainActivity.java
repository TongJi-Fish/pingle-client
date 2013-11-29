package com.example.ipingle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.app.ipinle.ui.LoginUi;
import com.app.ipinle.ui.TemplateUi;
public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button button = (Button) findViewById(R.id.login);
		
		OnClickListener mOnClickListener = new OnClickListener(){

			@Override
			public void onClick(View v) {
				forward(LoginUi.class);
				forward(LoginUi.class);
				//forward(TemplateUi.class);
			}
			
		};
		
		button.setOnClickListener((android.view.View.OnClickListener) mOnClickListener);
		
		Button routePlanButton = (Button) findViewById(R.id.routePlanButton);
		routePlanButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, RoutePlanActivity.class);
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
