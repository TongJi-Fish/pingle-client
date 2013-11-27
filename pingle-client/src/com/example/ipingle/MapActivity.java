package com.example.ipingle;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.RouteOverlay;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPlanNode;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKShareUrlResult;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.baidu.platform.comapi.basestruct.GeoPoint;

public class MapActivity extends Activity {
	private BMapManager mBMapMan = null;
	private MapView mMapView = null;

	MKSearch mMKSearch = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBMapMan = new BMapManager(getApplication());
		mBMapMan.init("sMW9h3hm9sSonRQ8Dd6cw03q", null);
		setContentView(R.layout.activity_map);
		mMapView = (MapView) findViewById(R.id.bmapsView);
		mMapView.setBuiltInZoomControls(true);

		MapController mMapController = mMapView.getController();
		GeoPoint point = new GeoPoint((int) (39.915 * 1E6),
				(int) (116.404 * 1E6));
		mMapController.setCenter(point);
		mMapController.setZoom(12);

		mMKSearch = new MKSearch();
		mMKSearch.init(mBMapMan, new MySearchListener());// 注意，MKSearchListener只支持一个，以最后一次设置为准

		MKPlanNode start = new MKPlanNode();
		start.pt = new GeoPoint((int) (39.915 * 1E6), (int) (116.404 * 1E6));
		MKPlanNode end = new MKPlanNode();
		end.pt = new GeoPoint(40057031, 116307852);
		mMKSearch.setDrivingPolicy(MKSearch.ECAR_TIME_FIRST);
		mMKSearch.drivingSearch(null, start, null, end);
	}

	class MySearchListener implements MKSearchListener {
		@Override
		public void onGetAddrResult(MKAddrInfo result, int iError) {
			// 返回地址信息搜索结果
		}

		@Override
		public void onGetDrivingRouteResult(MKDrivingRouteResult result,
				int iError) {
			if (result == null) {
				return;
			}
			RouteOverlay routeOverlay = new RouteOverlay(MapActivity.this,
					mMapView);
			routeOverlay.setData(result.getPlan(0).getRoute(0));
			mMapView.getOverlays().add(routeOverlay);
			mMapView.refresh();
		}

		@Override
		public void onGetPoiResult(MKPoiResult result, int type, int iError) {
			// 返回poi搜索结果
		}

		@Override
		public void onGetTransitRouteResult(MKTransitRouteResult result,
				int iError) {
			// 返回公交搜索结果
		}

		@Override
		public void onGetWalkingRouteResult(MKWalkingRouteResult result,
				int iError) {
			// 返回步行路线搜索结果
		}

		@Override
		public void onGetBusDetailResult(MKBusLineResult result, int iError) {
			// 返回公交车详情信息搜索结果
		}

		@Override
		public void onGetShareUrlResult(MKShareUrlResult result, int type,
				int error) {
			// 在此处理短串请求返回结果.
		}

		@Override
		public void onGetPoiDetailSearchResult(int arg0, int arg1) {

		}

		@Override
		public void onGetSuggestionResult(MKSuggestionResult arg0, int arg1) {

		}
	}

	@Override
	protected void onDestroy() {
		mMapView.destroy();
		if (mBMapMan != null) {
			mBMapMan.destroy();
			mBMapMan = null;
		}
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		if (mBMapMan != null) {
			mBMapMan.stop();
		}
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		if (mBMapMan != null) {
			mBMapMan.start();
		}
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.map, menu);
		return true;
	}

}
