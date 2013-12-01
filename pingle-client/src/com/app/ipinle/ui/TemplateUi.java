package com.app.ipinle.ui;

import com.example.ipingle.R;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;
import android.widget.Toast;

public class TemplateUi extends ActivityGroup {

	public static final String TAB_BUS_ROUTE = "busroute";
	public static final String TAB_CARPOOL = "carpool";
	public static final String TAB_NAVI = "navigation";
	private RadioGroup group;
	private TabHost tabHost;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 setContentView(R.layout.ui_template);

		iniTab();
	}

	public void iniTab() {
		tabHost = (TabHost) findViewById(R.id.myTabHost);
		if (tabHost != null) {
			// 如果没有继承TabActivity时，通过该种方法加载启动tabHost
			tabHost.setup(this.getLocalActivityManager());

			tabHost.addTab(tabHost.newTabSpec(TAB_BUS_ROUTE)
					.setIndicator(TAB_BUS_ROUTE).setIndicator("VIEW")
					.setContent(new Intent(this, ShowBusRouteUi.class)));
			tabHost.addTab(tabHost.newTabSpec(TAB_CARPOOL)
					.setIndicator(TAB_CARPOOL)
					.setContent(new Intent(this, CarPoolUi.class)));
			tabHost.addTab(tabHost.newTabSpec(TAB_NAVI).setIndicator(TAB_NAVI)
					.setContent(new Intent(this, NavigationUi.class)));
			group = (RadioGroup) findViewById(R.id.main_radio);

			if (group != null) {
				group.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						switch (checkedId) {
						case R.id.radio_button0:
							tabHost.setCurrentTabByTag(TAB_BUS_ROUTE);
							//forward(ShowBusRouteUi.class);
							break;
						case R.id.radio_button1:
							tabHost.setCurrentTabByTag(TAB_CARPOOL);
							// forward(CarPoolUi.class);
							break;
						case R.id.radio_button2:
							tabHost.setCurrentTabByTag(TAB_NAVI);
							// forward(NavigationUi.class);
							break;

						default:
							break;
						}
					}
				});
			} else {
				Toast.makeText(this, "group is null", Toast.LENGTH_LONG).show();
			}
		} else {
			Toast.makeText(this, "tabHost is null", Toast.LENGTH_LONG).show();
		}
	}

}
