package com.app.ipinle.util;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.view.View;

import com.app.ipinle.base.BaseMessage;
import com.app.ipinle.model.User;

public class AppUtil {

	// ///////////////////////////////////////////////////////////////////////////////
	// ҵ���߼�

	/* ��ȡ Session Id */
	static public String getSessionId() {
		User customer = User.getInstance();
		return customer.getSid();
	}

	/* ��ȡ Message */
	static public BaseMessage getMessage(String jsonStr) throws Exception {
		BaseMessage message = new BaseMessage();
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(jsonStr);
			if (jsonObject != null) {
				message.setCode(jsonObject.getString("code"));
				message.setMessage(jsonObject.getString("message"));
				message.setResult(jsonObject.getString("result"));
			}
		} catch (JSONException e) {
			throw new Exception("Json format error");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return message;
	}

	/* �ж�int�Ƿ�Ϊ�� */
	static public boolean isEmptyInt(int v) {
		Integer t = new Integer(v);
		return t == null ? true : false;
	}

	/**
	 * 从view 得到图片
	 * 
	 * @param view
	 * @return
	 */
	public static Bitmap getBitmapFromView(View view) {
		view.destroyDrawingCache();
		view.measure(View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
				.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		view.setDrawingCacheEnabled(true);
		Bitmap bitmap = view.getDrawingCache(true);
		return bitmap;
	}
}
