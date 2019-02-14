package de.andreasschrade.androidtemplate.ui.base;

import android.app.Application;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import de.andreasschrade.androidtemplate.database.BmobDBHelper;
import de.andreasschrade.androidtemplate.model.Userinfo;
import de.andreasschrade.androidtemplate.util.BmobDataUtil;
import de.andreasschrade.androidtemplate.util.LogUtil;


/**
 * 在 mainfast 文件中注册
 * <application
 *   android:name="com.example.textexception.AppContext"
 *   android:allowBackup="true"
 *   .....
 *   ></application>
 * 
 * @author pei
 */
public class AppContext extends Application{

	@Override
	public void onCreate() {
		super.onCreate();
//		//bugly调试,第三个参数为SDK调试模式开关,建议在测试阶段建议设置成true，发布时设置为false
//		CrashReport.initCrashReport(getApplicationContext(), Constents.BUGLY_APP_ID, BuildConfig.DEBUG);


		//bmob数据库初始化
		BmobDBHelper.getInstance().init(this);
		//管理員初始化
		final Userinfo userinfo=new Userinfo();
		userinfo.setUsername("xff");
		userinfo.setPassword("123456");
		userinfo.setRole("admin");
		userinfo.setState("enable");
		BmobDataUtil.addUserinfo(userinfo);

//		BmobQuery<Userinfo> query = new BmobQuery<Userinfo>();
//		query.addWhereEqualTo("username", "xff");
//		//执行查询方法
//		query.findObjects(new FindListener<Userinfo>() {
//			@Override
//			public void done(List<Userinfo> object, BmobException e) {
//				if (e != null || object.size()==0) {
//					BmobDataUtil.addUserinfo(userinfo);
//				}
//			}
//		});
	}
}
