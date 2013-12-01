package com.example.ipingle;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;
import com.baidu.mapapi.map.MKMapTouchListener;
import com.baidu.mapapi.map.MKMapViewListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.PopupClickListener;
import com.baidu.mapapi.map.PopupOverlay;
import com.baidu.mapapi.map.RouteOverlay;
import com.baidu.mapapi.map.TransitOverlay;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPlanNode;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKRoute;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKShareUrlResult;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.baidu.platform.comapi.basestruct.GeoPoint;

public class RoutePlanActivity extends Activity {
	private BMapManager mBMapManager = null;
	public static final String strKey = "sMW9h3hm9sSonRQ8Dd6cw03q";

	MKMapViewListener mMapListener = null;

	private MapController mMapController = null;

	Button mBtnDrive = null; // 驾车搜索
	Button mBtnTransit = null; // 公交搜索
	Button mBtnWalk = null; // 步行搜索
	Button mBtnCusRoute = null; // 自定义路线
	Button mBtnCusIcon = null; // 自定义起终点图标

	EditText editSt = null;
	EditText editEn = null;

	LinearLayout searchLinearLayout = null;

	// 浏览路线节点相关
	Button mBtnPre = null;// 上一个节点
	Button mBtnNext = null;// 下一个节点
	int nodeIndex = -2;// 节点索引,供浏览节点时使用
	MKRoute route = null;// 保存驾车/步行路线数据的变量，供浏览节点时使用
	TransitOverlay transitOverlay = null;// 保存公交路线图层数据的变量，供浏览节点时使用
	RouteOverlay routeOverlay = null;
	boolean useDefaultIcon = false;
	int searchType = -1;// 记录搜索的类型，区分驾车/步行和公交
	private PopupOverlay pop = null;// 弹出泡泡图层，浏览节点时使用
	private TextView popupText = null;// 泡泡view
	private View viewCache = null;

	// 地图相关，使用继承MapView的MyRouteMapView目的是重写touch事件实现泡泡处理
	// 如果不处理touch事件，则无需继承，直接使用MapView即可
	MapView mMapView = null; // 地图View
	// 搜索相关
	MKSearch mSearch = null; // 搜索模块，也可去掉地图模块独立使用

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (mBMapManager == null) {
			mBMapManager = new BMapManager(getApplicationContext());
		}

		if (!mBMapManager.init(strKey, new MyGeneralListener())) {
			Toast.makeText(getApplicationContext(), "BMapManager  初始化错误!",
					Toast.LENGTH_LONG).show();
		}

		setContentView(R.layout.activity_route_plan);
		CharSequence titleLable = "路线规划";
		setTitle(titleLable);
		// 初始化地图
		mMapView = (MapView) findViewById(R.id.bmapView);
		mMapView.setBuiltInZoomControls(false);
		GeoPoint point = new GeoPoint((int) (31.22 * 1E6), (int) (121.48 * 1E6));
		mMapView.getController().setCenter(point);
		mMapView.getController().setZoom(10);
		mMapView.getController().enableClick(true);

		mMapController = mMapView.getController();
		mMapController.enableClick(true);

		// 初始化按键
		mBtnDrive = (Button) findViewById(R.id.drive);
		mBtnTransit = (Button) findViewById(R.id.transit);
		mBtnWalk = (Button) findViewById(R.id.walk);
		mBtnPre = (Button) findViewById(R.id.pre);
		mBtnNext = (Button) findViewById(R.id.next);
		mBtnCusRoute = (Button) findViewById(R.id.custombutton);
		mBtnCusIcon = (Button) findViewById(R.id.customicon);
		mBtnPre.setVisibility(View.INVISIBLE);
		mBtnNext.setVisibility(View.INVISIBLE);

		editSt = (EditText) findViewById(R.id.start);
		editEn = (EditText) findViewById(R.id.end);

		searchLinearLayout = (LinearLayout) findViewById(R.id.searchLinearLayout);

		// 按键点击事件
		OnClickListener clickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (searchLinearLayout.getVisibility() == View.VISIBLE) {
					collapse(searchLinearLayout);
				}
				// 发起搜索
				SearchButtonProcess(v);
			}
		};
		OnClickListener nodeClickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 浏览路线节点
				nodeClick(v);
			}
		};
		OnClickListener customClickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 自设路线绘制示例
				intentToActivity();
			}
		};
		OnClickListener changeRouteIconListener = new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				changeRouteIcon();
			}

		};

		mBtnDrive.setOnClickListener(clickListener);
		mBtnTransit.setOnClickListener(clickListener);
		mBtnWalk.setOnClickListener(clickListener);
		mBtnPre.setOnClickListener(nodeClickListener);
		mBtnNext.setOnClickListener(nodeClickListener);
		mBtnCusRoute.setOnClickListener(customClickListener);
		mBtnCusIcon.setOnClickListener(changeRouteIconListener);
		// 创建 弹出泡泡图层
		createPaopao();

		// 地图点击事件处理
		mMapView.regMapTouchListner(new MKMapTouchListener() {
			@Override
			public void onMapClick(GeoPoint point) {
				if (pop != null) {
					pop.hidePop();
				}
			}

			@Override
			public void onMapDoubleClick(GeoPoint point) {
			}

			@Override
			public void onMapLongClick(GeoPoint point) {
			}

		});
		// 初始化搜索模块，注册事件监听
		mSearch = new MKSearch();
		mSearch.init(mBMapManager, new MySearchListener());

		mMapListener = new MKMapViewListener() {
			@Override
			public void onMapMoveFinish() {
			}

			@Override
			public void onClickMapPoi(MapPoi mapPoiInfo) {
				// mMapController.enableClick(true); 时，此回调才能被触发
				// String title = "";
				if (mapPoiInfo != null) {
					final String title = mapPoiInfo.strText;
					AlertDialog.Builder builder = new AlertDialog.Builder(
							RoutePlanActivity.this);
					builder.setTitle("将 " + title);

					builder.setItems(new String[] { "设为起点", "设为终点" },
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									switch (which) {
									case 0:
										editSt.setText(title);
										break;
									case 1:
										editEn.setText(title);
										break;
									default:
										break;
									}
									if (searchLinearLayout.getVisibility() == View.GONE) {
										expand(searchLinearLayout);
									}
									dialog.dismiss();
								}
							});
					builder.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							});
					builder.create().show();

					mMapController.animateTo(mapPoiInfo.geoPt);
				}
			}

			@Override
			public void onGetCurrentMap(Bitmap b) {
				// 当调用过 mMapView.getCurrentMap()后，此回调会被触发 可在此保存截图至存储设备
			}

			@Override
			public void onMapAnimationFinish() {
			}

			@Override
			public void onMapLoadFinish() {
				Toast.makeText(RoutePlanActivity.this, "地图加载完成",
						Toast.LENGTH_SHORT).show();
			}
		};
		mMapView.regMapViewListener(mBMapManager, mMapListener);
	}

	class MySearchListener implements MKSearchListener {
		@Override
		public void onGetDrivingRouteResult(MKDrivingRouteResult res, int error) {
			// 起点或终点有歧义，需要选择具体的城市列表或地址列表
			if (error == MKEvent.ERROR_ROUTE_ADDR) {
				// 遍历所有地址
				// ArrayList<MKPoiInfo> stPois =
				// res.getAddrResult().mStartPoiList;
				// ArrayList<MKPoiInfo> enPois =
				// res.getAddrResult().mEndPoiList;
				// ArrayList<MKCityListInfo> stCities =
				// res.getAddrResult().mStartCityList;
				// ArrayList<MKCityListInfo> enCities =
				// res.getAddrResult().mEndCityList;
				return;
			}
			// 错误号可参考MKEvent中的定义
			if (error != 0 || res == null) {
				Toast.makeText(RoutePlanActivity.this, "抱歉，未找到结果",
						Toast.LENGTH_SHORT).show();
				return;
			}

			searchType = 0;
			routeOverlay = new RouteOverlay(RoutePlanActivity.this, mMapView);
			// 此处仅展示一个方案作为示例
			routeOverlay.setData(res.getPlan(0).getRoute(0));
			// 清除其他图层
			mMapView.getOverlays().clear();
			// 添加路线图层
			mMapView.getOverlays().add(routeOverlay);
			// 执行刷新使生效
			mMapView.refresh();
			// 使用zoomToSpan()绽放地图，使路线能完全显示在地图上
			mMapView.getController().zoomToSpan(routeOverlay.getLatSpanE6(),
					routeOverlay.getLonSpanE6());
			mMapView.getController().setZoom(14);
			// 移动地图到起点
			mMapView.getController().animateTo(res.getStart().pt);
			// 将路线数据保存给全局变量
			route = res.getPlan(0).getRoute(0);
			// 重置路线节点索引，节点浏览时使用
			nodeIndex = -1;
			mBtnPre.setVisibility(View.VISIBLE);
			mBtnNext.setVisibility(View.VISIBLE);
		}

		@Override
		public void onGetTransitRouteResult(MKTransitRouteResult res, int error) {
			// 起点或终点有歧义，需要选择具体的城市列表或地址列表
			if (error == MKEvent.ERROR_ROUTE_ADDR) {
				// 遍历所有地址
				// ArrayList<MKPoiInfo> stPois =
				// res.getAddrResult().mStartPoiList;
				// ArrayList<MKPoiInfo> enPois =
				// res.getAddrResult().mEndPoiList;
				// ArrayList<MKCityListInfo> stCities =
				// res.getAddrResult().mStartCityList;
				// ArrayList<MKCityListInfo> enCities =
				// res.getAddrResult().mEndCityList;
				// Toast.makeText(RoutePlanActivity.this, "抱歉，未找到结果",
				// Toast.LENGTH_SHORT).show();
				return;
			}
			if (error != 0 || res == null) {
				Toast.makeText(RoutePlanActivity.this, "抱歉，未找到结果",
						Toast.LENGTH_SHORT).show();
				return;
			}

			searchType = 1;
			transitOverlay = new TransitOverlay(RoutePlanActivity.this,
					mMapView);
			// 此处仅展示一个方案作为示例
			transitOverlay.setData(res.getPlan(0));
			// 清除其他图层
			mMapView.getOverlays().clear();
			// 添加路线图层
			mMapView.getOverlays().add(transitOverlay);
			// 执行刷新使生效
			mMapView.refresh();
			// 使用zoomToSpan()绽放地图，使路线能完全显示在地图上
			mMapView.getController().zoomToSpan(transitOverlay.getLatSpanE6(),
					transitOverlay.getLonSpanE6());
			// 移动地图到起点
			mMapView.getController().animateTo(res.getStart().pt);
			// 重置路线节点索引，节点浏览时使用
			nodeIndex = 0;
			mBtnPre.setVisibility(View.VISIBLE);
			mBtnNext.setVisibility(View.VISIBLE);
		}

		@Override
		public void onGetWalkingRouteResult(MKWalkingRouteResult res, int error) {
			// 起点或终点有歧义，需要选择具体的城市列表或地址列表
			if (error == MKEvent.ERROR_ROUTE_ADDR) {
				// 遍历所有地址
				// ArrayList<MKPoiInfo> stPois =
				// res.getAddrResult().mStartPoiList;
				// ArrayList<MKPoiInfo> enPois =
				// res.getAddrResult().mEndPoiList;
				// ArrayList<MKCityListInfo> stCities =
				// res.getAddrResult().mStartCityList;
				// ArrayList<MKCityListInfo> enCities =
				// res.getAddrResult().mEndCityList;
				return;
			}
			if (error != 0 || res == null) {
				Toast.makeText(RoutePlanActivity.this, "抱歉，未找到结果",
						Toast.LENGTH_SHORT).show();
				return;
			}

			searchType = 2;
			routeOverlay = new RouteOverlay(RoutePlanActivity.this, mMapView);
			// 此处仅展示一个方案作为示例
			routeOverlay.setData(res.getPlan(0).getRoute(0));
			// 清除其他图层
			mMapView.getOverlays().clear();
			// 添加路线图层
			mMapView.getOverlays().add(routeOverlay);
			// 执行刷新使生效
			mMapView.refresh();
			// 使用zoomToSpan()绽放地图，使路线能完全显示在地图上
			mMapView.getController().zoomToSpan(routeOverlay.getLatSpanE6(),
					routeOverlay.getLonSpanE6());

			mMapView.getController().setZoom(14);
			// 移动地图到起点
			mMapView.getController().animateTo(res.getStart().pt);
			// 将路线数据保存给全局变量
			route = res.getPlan(0).getRoute(0);
			// 重置路线节点索引，节点浏览时使用
			nodeIndex = -1;
			mBtnPre.setVisibility(View.VISIBLE);
			mBtnNext.setVisibility(View.VISIBLE);

		}

		@Override
		public void onGetAddrResult(MKAddrInfo res, int error) {
			if (error != 0) {
				String str = String.format("错误号：%d", error);
				Toast.makeText(RoutePlanActivity.this, str, Toast.LENGTH_LONG)
						.show();
				return;
			}
			Toast.makeText(RoutePlanActivity.this, "heh", Toast.LENGTH_LONG)
					.show();
			// 地图移动到该点
			// mMapView.getController().animateTo(res.geoPt);
			if (res.type == MKAddrInfo.MK_GEOCODE) {
				// 地理编码：通过地址检索坐标点
				String strInfo = String.format("纬度：%f 经度：%f",
						res.geoPt.getLatitudeE6() / 1e6,
						res.geoPt.getLongitudeE6() / 1e6);
				Toast.makeText(RoutePlanActivity.this, strInfo,
						Toast.LENGTH_LONG).show();
			}
			if (res.type == MKAddrInfo.MK_REVERSEGEOCODE) {
				// 反地理编码：通过坐标点检索详细地址及周边poi
				String strInfo = res.strAddr;
				String city = strInfo.split("市")[0];
				Toast.makeText(RoutePlanActivity.this, strInfo + " " + city,
						Toast.LENGTH_LONG).show();
			}
		}

		@Override
		public void onGetPoiResult(MKPoiResult res, int arg1, int arg2) {
		}

		@Override
		public void onGetBusDetailResult(MKBusLineResult result, int iError) {
		}

		@Override
		public void onGetSuggestionResult(MKSuggestionResult res, int arg1) {
		}

		@Override
		public void onGetPoiDetailSearchResult(int type, int iError) {
		}

		@Override
		public void onGetShareUrlResult(MKShareUrlResult result, int type,
				int error) {

		}
	}

	// 常用事件监听，用来处理通常的网络错误，授权验证错误等
	class MyGeneralListener implements MKGeneralListener {

		@Override
		public void onGetNetworkState(int iError) {
			if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
				Toast.makeText(getApplicationContext(), "网络出错啦！",
						Toast.LENGTH_LONG).show();
			} else if (iError == MKEvent.ERROR_NETWORK_DATA) {
				Toast.makeText(getApplicationContext(), "输入正确的检索条件！",
						Toast.LENGTH_LONG).show();
			}
			// ...
		}

		@Override
		public void onGetPermissionState(int iError) {
			if (iError == MKEvent.ERROR_PERMISSION_DENIED) {
				// 授权Key错误：
				Toast.makeText(getApplicationContext(),
						"请在 DemoApplication.java文件输入正确的授权Key！",
						Toast.LENGTH_LONG).show();
			}
		}
	}

	/**
	 * 发起路线规划搜索示例
	 * 
	 * @param v
	 */
	void SearchButtonProcess(View v) {
		// 重置浏览节点的路线数据
		route = null;
		routeOverlay = null;
		transitOverlay = null;
		mBtnPre.setVisibility(View.INVISIBLE);
		mBtnNext.setVisibility(View.INVISIBLE);
		// 处理搜索按钮响应
		EditText editSt = (EditText) findViewById(R.id.start);
		EditText editEn = (EditText) findViewById(R.id.end);

		// 对起点终点的name进行赋值，也可以直接对坐标赋值，赋值坐标则将根据坐标进行搜索
		MKPlanNode stNode = new MKPlanNode();
		stNode.name = editSt.getText().toString();
		MKPlanNode enNode = new MKPlanNode();
		enNode.name = editEn.getText().toString();

		// 实际使用中请对起点终点城市进行正确的设定
		if (mBtnDrive.equals(v)) {
			mSearch.drivingSearch("", stNode, "", enNode);
		} else if (mBtnTransit.equals(v)) {
			// TODO
			mSearch.transitSearch("上海", stNode, enNode);
		} else if (mBtnWalk.equals(v)) {
			mSearch.walkingSearch("", stNode, "", enNode);
		}
	}

	/**
	 * 节点浏览示例
	 * 
	 * @param v
	 */
	public void nodeClick(View v) {
		viewCache = getLayoutInflater()
				.inflate(R.layout.custom_text_view, null);
		popupText = (TextView) viewCache.findViewById(R.id.textcache);
		if (searchType == 0 || searchType == 2) {
			// 驾车、步行使用的数据结构相同，因此类型为驾车或步行，节点浏览方法相同
			if (nodeIndex < -1 || route == null
					|| nodeIndex >= route.getNumSteps())
				return;

			// 上一个节点
			if (mBtnPre.equals(v) && nodeIndex > 0) {
				// 索引减
				nodeIndex--;
				// 移动到指定索引的坐标
				mMapView.getController().animateTo(
						route.getStep(nodeIndex).getPoint());
				// 弹出泡泡
				popupText.setBackgroundResource(R.drawable.popup);
				popupText.setText(route.getStep(nodeIndex).getContent());
				pop.showPopup(BMapUtil.getBitmapFromView(popupText), route
						.getStep(nodeIndex).getPoint(), 5);
			}
			// 下一个节点
			if (mBtnNext.equals(v) && nodeIndex < (route.getNumSteps() - 1)) {
				// 索引加
				nodeIndex++;
				// 移动到指定索引的坐标
				mMapView.getController().animateTo(
						route.getStep(nodeIndex).getPoint());
				// 弹出泡泡
				popupText.setBackgroundResource(R.drawable.popup);
				popupText.setText(route.getStep(nodeIndex).getContent());
				pop.showPopup(BMapUtil.getBitmapFromView(popupText), route
						.getStep(nodeIndex).getPoint(), 5);
			}
		}
		if (searchType == 1) {
			// 公交换乘使用的数据结构与其他不同，因此单独处理节点浏览
			if (nodeIndex < -1 || transitOverlay == null
					|| nodeIndex >= transitOverlay.getAllItem().size())
				return;

			// 上一个节点
			if (mBtnPre.equals(v) && nodeIndex > 1) {
				// 索引减
				nodeIndex--;
				// 移动到指定索引的坐标
				mMapView.getController().animateTo(
						transitOverlay.getItem(nodeIndex).getPoint());
				// 弹出泡泡
				popupText.setBackgroundResource(R.drawable.popup);
				popupText.setText(transitOverlay.getItem(nodeIndex).getTitle());
				pop.showPopup(BMapUtil.getBitmapFromView(popupText),
						transitOverlay.getItem(nodeIndex).getPoint(), 5);
			}
			// 下一个节点
			if (mBtnNext.equals(v)
					&& nodeIndex < (transitOverlay.getAllItem().size() - 2)) {
				// 索引加
				nodeIndex++;
				// 移动到指定索引的坐标
				mMapView.getController().animateTo(
						transitOverlay.getItem(nodeIndex).getPoint());
				// 弹出泡泡
				popupText.setBackgroundResource(R.drawable.popup);
				popupText.setText(transitOverlay.getItem(nodeIndex).getTitle());
				pop.showPopup(BMapUtil.getBitmapFromView(popupText),
						transitOverlay.getItem(nodeIndex).getPoint(), 5);
			}
		}

	}

	/**
	 * 创建弹出泡泡图层
	 */
	public void createPaopao() {
		// 泡泡点击响应回调
		PopupClickListener popListener = new PopupClickListener() {
			@Override
			public void onClickedPopup(int index) {
				Log.v("click", "clickapoapo");
			}
		};
		pop = new PopupOverlay(mMapView, popListener);
	}

	/**
	 * 跳转自设路线Activity
	 */
	public void intentToActivity() {
		// 跳转到自设路线演示demo
//		Intent intent = new Intent(this, MapActivity.class);
//		startActivity(intent);
	}

	/**
	 * 切换路线图标，刷新地图使其生效 注意： 起终点图标使用中心对齐.
	 */
	protected void changeRouteIcon() {
		Button btn = (Button) findViewById(R.id.customicon);
		if (routeOverlay == null && transitOverlay == null) {
			return;
		}
		if (useDefaultIcon) {
			if (routeOverlay != null) {
				routeOverlay.setStMarker(null);
				routeOverlay.setEnMarker(null);
			}
			if (transitOverlay != null) {
				transitOverlay.setStMarker(null);
				transitOverlay.setEnMarker(null);
			}
			btn.setText("自定义起终点图标");
			Toast.makeText(this, "将使用系统起终点图标", Toast.LENGTH_SHORT).show();
		} else {
			if (routeOverlay != null) {
				routeOverlay.setStMarker(getResources().getDrawable(
						R.drawable.icon_st));
				routeOverlay.setEnMarker(getResources().getDrawable(
						R.drawable.icon_en));
			}
			if (transitOverlay != null) {
				transitOverlay.setStMarker(getResources().getDrawable(
						R.drawable.icon_st));
				transitOverlay.setEnMarker(getResources().getDrawable(
						R.drawable.icon_en));
			}
			btn.setText("系统起终点图标");
			Toast.makeText(this, "将使用自定义起终点图标", Toast.LENGTH_SHORT).show();
		}
		useDefaultIcon = !useDefaultIcon;
		mMapView.refresh();

	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		mMapView.destroy();
		mSearch.destory();
		super.onDestroy();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mMapView.onSaveInstanceState(outState);

	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		mMapView.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.route_plan, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.searchItem:
			if (searchLinearLayout.getVisibility() == View.GONE) {
				expand(searchLinearLayout);
			}
			break;
		default:
			return super.onOptionsItemSelected(item);
		}
		return true;
	}

	private void expand(final View view) {
		view.measure(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		final int targtetHeight = view.getMeasuredHeight();

		view.getLayoutParams().height = 0;
		view.setVisibility(View.VISIBLE);
		Animation animation = new Animation() {
			@Override
			protected void applyTransformation(float interpolatedTime,
					Transformation t) {
				view.getLayoutParams().height = interpolatedTime == 1 ? LayoutParams.WRAP_CONTENT
						: (int) (targtetHeight * interpolatedTime);
				view.requestLayout();
			}

			@Override
			public boolean willChangeBounds() {
				return true;
			}
		};

		// 1dp/ms
		animation.setDuration((int) (targtetHeight / view.getContext()
				.getResources().getDisplayMetrics().density));
		view.startAnimation(animation);
	}

	private void collapse(final View view) {
		final int initialHeight = view.getMeasuredHeight();

		Animation animation = new Animation() {
			@Override
			protected void applyTransformation(float interpolatedTime,
					Transformation t) {
				if (interpolatedTime == 1) {
					view.setVisibility(View.GONE);
				} else {
					view.getLayoutParams().height = initialHeight
							- (int) (initialHeight * interpolatedTime);
					view.requestLayout();
				}
			}

			@Override
			public boolean willChangeBounds() {
				return true;
			}
		};

		// 1dp/ms
		animation.setDuration((int) (initialHeight / view.getContext()
				.getResources().getDisplayMetrics().density));
		view.startAnimation(animation);
	}
}
