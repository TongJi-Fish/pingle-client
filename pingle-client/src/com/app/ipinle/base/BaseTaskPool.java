package com.app.ipinle.base;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.util.Log;

import com.app.ipinle.util.AppClient;

public class BaseTaskPool {
	
	// task thread pool
	static private ExecutorService taskPool;
	
	// for HttpUtil.getNetType
	private Context context;
	
	public BaseTaskPool (BaseUi ui) {
		this.context = ui.getContext();
		taskPool = Executors.newCachedThreadPool();
	}
	
	// http post task with params
	public void addTask (int taskId, String taskUrl, HashMap<String, String> taskArgs, BaseTask baseTask, int delayTime) {
		baseTask.setId(taskId);
		try {
			taskPool.execute(new TaskThread(context, taskUrl, taskArgs, baseTask, delayTime));
		} catch (Exception e) {
			taskPool.shutdown();
		}
	}
	
	// http post task without params
	public void addTask (int taskId, String taskUrl, BaseTask baseTask, int delayTime) {
		baseTask.setId(taskId);
		try {
			taskPool.execute(new TaskThread(context, taskUrl, null, baseTask, delayTime));
		} catch (Exception e) {
			taskPool.shutdown();
		}
	}
	
	// custom task
	public void addTask (int taskId, BaseTask baseTask, int delayTime) {
		baseTask.setId(taskId);
		try {
			taskPool.execute(new TaskThread(context, null, null, baseTask, delayTime));
		} catch (Exception e) {
			taskPool.shutdown();
		}
	}
	
	// task thread logic
	private class TaskThread implements Runnable {
		private Context context;
		private String taskUrl;
		private HashMap<String, String> taskArgs;
		private BaseTask baseTask;
		private int delayTime = 0;
		public TaskThread(Context context, String taskUrl, HashMap<String, String> taskArgs, BaseTask baseTask, int delayTime) {
			this.context = context;
			this.taskUrl = taskUrl;
			this.taskArgs = taskArgs;
			this.baseTask = baseTask;
			this.delayTime = delayTime;
			//Log.i(C.debug.BaseTaskPool,this.context.toString()+" "+this.taskUrl+" "+this.taskArgs.toString()+" "+this.baseTask.toString());
		}
		@Override
		public void run() {
			try {
				baseTask.onStart();
				String httpResult = null;
				// set delay time
				if (this.delayTime > 0) {
					Thread.sleep(this.delayTime);
				}
				try {
					// remote task
					if (this.taskUrl != null) {
						// init app client
						AppClient client = new AppClient(this.taskUrl);
//						if (HttpUtil.WAP_INT == HttpUtil.getNetType(context)) {
//							client.useWap();
//						}
						// http get
						if (taskArgs == null) {
							httpResult = client.get();
						// http post
						} else {
							Log.i(C.debug.login, "before do post");
							httpResult = client.post(this.taskArgs);
						}
					}
					// remote task
					if (httpResult != null) {
						Log.i(C.debug.login, "not null----after do post before complete");
						baseTask.onComplete(httpResult);
					// local task
					} else {
						Log.i(C.debug.login, "is null----after do post before complete");
						baseTask.onComplete();
					}
				} catch (Exception e) {
					baseTask.onError(e.getMessage());
					Log.i(C.debug.login,"error:"+e.toString()+"message:"+e.getMessage().toString());
					Log.i(C.debug.login,"error get e 你好");
					Log.i(C.debug.login, "login baseTaskPool error1");
				}
			} catch (Exception e) {
				e.printStackTrace();
				baseTask.onError(e.getMessage());
				Log.i(C.debug.login, "login baseTaskPool error2");
			} finally {
				try {
					baseTask.onStop();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}