package com.app.ipinle.util;

import org.json.JSONException;
import org.json.JSONObject;

import com.app.ipinle.base.BaseMessage;
import com.app.ipinle.model.User;

public class AppUtil {

	/////////////////////////////////////////////////////////////////////////////////
	// 业务逻辑
	
	/* 获取 Session Id */
	static public String getSessionId () {
		User customer = User.getInstance();
		return customer.getSid();
	}
	
	/* 获取 Message */
	static public BaseMessage getMessage (String jsonStr) throws Exception {
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
	
	/* 判断int是否为空 */
	static public boolean isEmptyInt (int v) {
		Integer t = new Integer(v);
		return t == null ? true : false;
	}
}
