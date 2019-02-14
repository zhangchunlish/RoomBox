package de.andreasschrade.androidtemplate.ui;

import java.util.Timer;
import java.util.TimerTask;

import de.andreasschrade.androidtemplate.GlobalParams;
import de.andreasschrade.androidtemplate.R;
import de.andreasschrade.androidtemplate.ui.quote.*;
import de.andreasschrade.androidtemplate.util.*;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

/**
 * 欢迎界面
 * 
 */
public class SplashActivity extends Activity {

	protected static final String LOGTAG = LogUtil.makeLogTag(SplashActivity.class);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		GlobalParams.activity = this;

//		SpUtil sp = new SpUtil(this);
//		String lastTiem = sp.getString(Constants.LAST_TIME, "0");
//		// 从开机到现在的毫秒数（手机睡眠的时间不包括在内）
//		long timeInterval = SystemClock.uptimeMillis() - Integer.parseInt(lastTiem);
//
//		// 一定期限内,自动登陆
//		if (timeInterval < Constants.AUTO_LOGIN) {
//			GlobalParams.ISLOGIN = true;
//		} else {
//			GlobalParams.ISLOGIN = false;
//		}
//		sp.saveString(Constants.LAST_TIME, String.valueOf(SystemClock.uptimeMillis()));

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);//无标题
		setContentView(R.layout.activity_splash);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//全屏

		//加载表情数据
		new Thread(new Runnable() {
            @Override
            public void run() {
            	L.i(LOGTAG, "加载表情数据");
                FaceConversionUtil.getInstace().getFileText(SplashActivity.this);
            }
        }).start();
		
		TimerTask timerTask = new TimerTask() {

			@Override
			public void run() {
				Intent intent = new Intent(SplashActivity.this, RoomListActivity.class);
				startActivity(intent);
				finish();
			}
		};
		new Timer().schedule(timerTask, 1000);
	}
}
