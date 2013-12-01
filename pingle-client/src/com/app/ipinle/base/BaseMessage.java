package com.app.ipinle.base;

public class BaseMessage {
	
	private String code;
	private String message;
	private String resultSrc;
	
	@Override
	public String toString () {
		return code + " | " + message + " | " + resultSrc;
	}
	
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
}
