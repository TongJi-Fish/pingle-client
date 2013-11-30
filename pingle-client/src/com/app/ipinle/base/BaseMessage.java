package com.app.ipinle.base;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.app.ipinle.model.User;
import com.app.ipinle.util.AppUtil;

public class BaseMessage {
	
	private String code;
	private String message;
	private String resultSrc;
	
	/*@Override
	public String toString () {
		return code + " | " + message + " | " + resultSrc;
	}*/
	
	public String getCode () {
		return this.code;
	}
	
	public void setCode (String code) {
		this.code = code;
	}
	
	public String getMessage () {
		return this.message;
	}
	
	public void setMessage (String message) {
		this.message = message;
	}
	
	public String getResult () {
		return this.resultSrc;
	}
	
	public Object getResult (String modelName) throws Exception {
//		Object model = this.resultMap.get(modelName);
//		// catch null exception
//		if (model == null) {
//			throw new Exception("Message data is empty");
//		}
//		return model;
		
		//resultStr: {"User":{"id":"aaaaaayyyymmddxxxx",
		//"sid":"k66b46404orragqt7arkqbdd43","driver_id":null,"name":"jack"}}
		
		if (this.resultSrc.length() > 0) {
			JSONObject jsonObject = null;
			jsonObject = new JSONObject(this.resultSrc);
			Iterator<String> it = jsonObject.keys();
			while (it.hasNext()) {
				// initialize
				String jsonKey = it.next();
				String modelName1 = getModelName(jsonKey);
				String modelClassName = "com.app.demos.model." + modelName1;
				Log.i(C.debug.login, "modelClassName="+modelClassName);
				JSONArray modelJsonArray = jsonObject.optJSONArray(jsonKey);
				// JSONObject
				if (modelJsonArray == null) {
					JSONObject modelJsonObject = jsonObject.optJSONObject(jsonKey);
					if (modelJsonObject == null) {
						throw new Exception("Message result is invalid");
					}
					this.resultMap.put(modelName, json2model(modelClassName, modelJsonObject));
					Log.i(C.debug.login, "modelJsonArray is null");
				}
			}
		}
		
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public void setResult (String result) throws Exception {
		this.resultSrc = result;
//		if (result.length() > 0) {
//			JSONObject jsonObject = null;
//			jsonObject = new JSONObject(result);
//			Iterator<String> it = jsonObject.keys();
//			while (it.hasNext()) {
//				// initialize
//				String jsonKey = it.next();
//				String modelName = getModelName(jsonKey);
//				String modelClassName = "com.app.demos.model." + modelName;
//				JSONArray modelJsonArray = jsonObject.optJSONArray(jsonKey);
//				// JSONObject
//				if (modelJsonArray == null) {
//					JSONObject modelJsonObject = jsonObject.optJSONObject(jsonKey);
//					if (modelJsonObject == null) {
//						throw new Exception("Message result is invalid");
//					}
//					this.resultMap.put(modelName, json2model(modelClassName, modelJsonObject));
//				// JSONArray
//				} else {
//					ArrayList<BaseModel> modelList = new ArrayList<BaseModel>();
//					for (int i = 0; i < modelJsonArray.length(); i++) {
//						JSONObject modelJsonObject = modelJsonArray.optJSONObject(i);
//						modelList.add(json2model(modelClassName, modelJsonObject));
//					}
//					this.resultList.put(modelName, modelList);
//				}
//			}
//		}
	}
	
	// 获取模型名称
	private String getModelName (String str) {
		String[] strArr = str.split("\\W");
		if (strArr.length > 0) {
			str = strArr[0];
		}
		return AppUtil.ucfirst(str);
	}
	
	private Object json2model (String modelClassName, JSONObject modelJsonObject) throws Exception  {
		//
		if(modelClassName=="User"){
			User user = User.getInstance();
			user.setId(modelJsonObject.getString("id"));
			user.setId_num(modelJsonObject.getString("id_num"));
		}
		
		// auto-load model class
		BaseModel modelObj = (BaseModel) Class.forName(modelClassName).newInstance();
		Class<? extends BaseModel> modelClass = modelObj.getClass();
		// auto-setting model fields
		Iterator<String> it = modelJsonObject.keys();
		while (it.hasNext()) {
			String varField = it.next();
			String varValue = modelJsonObject.getString(varField);
			Field field = modelClass.getDeclaredField(varField);
			field.setAccessible(true); // have private to be accessable
			field.set(modelObj, varValue);
		}
		return modelObj;
	}
}
