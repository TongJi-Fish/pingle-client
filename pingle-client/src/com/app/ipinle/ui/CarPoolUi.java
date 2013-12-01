package com.app.ipinle.ui;

import android.content.DialogInterface;

import android.os.Bundle;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.app.ipinle.base.BaseUi;
import com.example.ipingle.R;

public class CarPoolUi extends BaseUi {

	private Button bn1;
	private Button bn2;
	private Button ok;
	private Button back;
	private EditText startPoint;
	private EditText terminalPoint;
	public Message message = new Message();
	private LayoutInflater inflater;
	private GridView gv_sample;
	int count;
	int[] pageName;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		setContentView(R.layout.ui_choose_type);
		super.onCreate(savedInstanceState);
		
		pageName = new int[3];
		pageName[0] = R.layout.ui_choose_type;
		pageName[1] = R.layout.ui_choose_route;
		pageName[2] = R.layout.ui_choose_time;
		
		count = 0;
		bn1 = (Button)findViewById(R.id.drive_car);
		bn1.setOnClickListener(onClickListener);
		bn2 = (Button)findViewById(R.id.carpool);
		bn2.setOnClickListener(onClickListener);
	}
	
	OnClickListener onClickListener = new OnClickListener(){
		@Override
		public void onClick(View v){
			switch(v.getId()){
			case(R.id.drive_car):{
				message.setDrive(true);
				++count;
				setContentView(pageName[count]);
				ok = (Button)findViewById(R.id.make_sure);
				ok.setOnClickListener(onClickListener);
				back = (Button)findViewById(R.id.back_to_0);
				back.setOnClickListener(onClickListener);
				break;
				}
			case(R.id.carpool):{
				message.setDrive(false);
				++count;
				setContentView(pageName[count]);
				ok = (Button)findViewById(R.id.make_sure);
				ok.setOnClickListener(onClickListener);
				back = (Button)findViewById(R.id.back_to_0);
				back.setOnClickListener(onClickListener);
				break;
				}
			
			
			case(R.id.make_sure):{
				
				startPoint = (EditText)findViewById(R.id.start_point);
				terminalPoint = (EditText)findViewById(R.id.terminal_point);
				if(startPoint.getText().length()<=0||terminalPoint.getText().length()<=0){
					Toast.makeText(CarPoolUi.this, "请输入起点及终点",Toast.LENGTH_SHORT).show();
					break;
				}
				
				message.setStart_point(startPoint.getText().toString());
				message.setTerminal_point(terminalPoint.getText().toString());
				++count;
				setContentView(pageName[count]);
				break;
				}
			case(R.id.back_to_0):{
				--count;
				setContentView(pageName[count]);
				bn1 = (Button)findViewById(R.id.drive_car);
				bn1.setOnClickListener(onClickListener);
				bn2 = (Button)findViewById(R.id.carpool);
				bn2.setOnClickListener(onClickListener);
				break;
				}
			
			
			
			}
			
			//setContentView(R.layout.test);
			
			
		}
	};
	OnClickListener onClickListener2 = new OnClickListener(){
		@Override
		public void onClick(View v){
			
			setContentView(R.layout.ui_carpool);
			//bn1 = (Button)findViewById(R.id.test);
			//bn1.setOnClickListener(onClickListener1);
		}
	};
	

	class Message{
		private boolean isDrive;
		private String start_point;
		private String terminal_point;
		private Time t;
		private int number;
		
		public boolean isDrive() {
			return isDrive;
		}
		public void setDrive(boolean isDrive) {
			this.isDrive = isDrive;
		}
		public String getStart_point() {
			return start_point;
		}
		public void setStart_point(String start_point) {
			this.start_point = start_point;
		}
		public String getTerminal_point() {
			return terminal_point;
		}
		public void setTerminal_point(String terminal_point) {
			this.terminal_point = terminal_point;
		}
		public Time getT() {
			return t;
		}
		public void setT(Time t) {
			this.t = t;
		}
		public int getNumber() {
			return number;
		}
		public void setNumber(int number) {
			this.number = number;
		}
	}

}
