package de.andreasschrade.androidtemplate.ui.user;

import de.andreasschrade.androidtemplate.ui.base.BaseActivity;
import de.andreasschrade.androidtemplate.Constants;
import de.andreasschrade.androidtemplate.GlobalParams;
import de.andreasschrade.androidtemplate.model.Userinfo;
import de.andreasschrade.androidtemplate.ui.quote.ListActivity;
import de.andreasschrade.androidtemplate.ui.quote.RoomListActivity;
import de.andreasschrade.androidtemplate.util.*;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import de.andreasschrade.androidtemplate.R;
import android.app.ActionBar;

/**
 * 注册
 * 
 */
public class RegistActivity extends BaseActivity implements View.OnClickListener{

	protected static final String LOGTAG = LogUtil.makeLogTag(RegistActivity.class);
	/**
	 * 用户名输入框
	 */
	private EditText mEt_userName;
	/**
	 * 昵称输入框
	 */
	private EditText mEt_nickName;
	/**
	 * 密码输入框
	 */
	private EditText mEt_pwd;
	/**
	 * 重复密码输入框
	 */
	private EditText mEt_rpwd;
	/**
	 * 注册按钮
	 */
	private Button mBtn_regist;
	private SpUtil sp;
	private String userName = "";
	private String nickName = "";
	private String userPwd = "";
	private String userRpwd = "";
	private Vibrator vibrator; 
	private Context context;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case HandlerTypeUtils.WX_HANDLER_HTTP_EXCEPTION:
				AppToast.getToast().show("网络异常");
				break;
			case HandlerTypeUtils.WX_HANDLER_TYPE_LOAD_DATA_SUCCESS:
				L.i(LOGTAG, "regist success");
				sp.saveString(Constants.XMPP_USERNAME, userName);
				sp.saveString(Constants.XMPP_PASSWORD, userPwd);
				AppToast.getToast().show("注册成功");
				GlobalParams.ISLOGIN = true;
				GlobalParams.USERNAME=userName;
				Intent intent = new Intent(RegistActivity.this, RoomListActivity.class);
				startActivity(intent);
				finish();
				break;
			case HandlerTypeUtils.WX_HANDLER_TYPE_LOAD_DATA_SAME:
				mEt_userName.requestFocus();
				AppToast.getToast().show("该用户名已被注册");
				Animation shake = AnimationUtils.loadAnimation(RegistActivity.this, R.anim.shake);
				mEt_userName.startAnimation(shake);
				vibrator.vibrate(300);
				break;
			case HandlerTypeUtils.WX_HANDLER_TYPE_LOAD_DATA_FAIL:
				AppToast.getToast().show("注册失败");
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_regist);
		// Show the Up button in the action bar.
		if (getSupportActionBar() != null) {
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		}
		//显示软体键盘
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		context=this;
		init();
	}

	private void init() {
		initView();
		initData();
	}

	private void initView() {
//		ActionBar actionBar = getActionBar();
//		actionBar.setDisplayHomeAsUpEnabled(true);
//		actionBar.setDisplayShowHomeEnabled(false);
//		actionBar.setDisplayShowTitleEnabled(true);
//		actionBar.setTitle(R.string.text_menu_regist_title);

		mEt_userName = (EditText) findViewById(R.id.cgt_et_regist_userName);
//		mEt_nickName = (EditText) findViewById(R.id.cgt_et_regist_nickName);
		mEt_pwd = (EditText) findViewById(R.id.cgt_et_regist_pwd);
		mEt_rpwd = (EditText) findViewById(R.id.cgt_et_regist_rpwd);
		mBtn_regist = (Button) findViewById(R.id.cgt_btn_regist_user);
		mBtn_regist.setOnClickListener(this);
	}

	private void initData() {
		sp = new SpUtil(this);
		mEt_userName.requestFocus();
		vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cgt_btn_regist_user:
			if (checkValidity()) {
				submitRegist();
			}
			break;

		default:
			break;
		}
	}

	private void getUserInfo() {
		userName = mEt_userName.getText().toString().trim();
//		nickName = mEt_nickName.getText().toString().trim();
		userPwd = mEt_pwd.getText().toString().trim();
		userRpwd = mEt_rpwd.getText().toString().trim();
	}

	private boolean checkValidity() {
		getUserInfo();
		if (TextUtils.isEmpty(userName)) {
			mEt_userName.requestFocus();
			AppToast.getToast().show("用户名不能为空");
			Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
			mEt_userName.startAnimation(shake);
			vibrator.vibrate(300);
			return false;
		}
//		if (TextUtils.isEmpty(nickName)) {
//			mEt_nickName.requestFocus();
//			AppToast.getToast().show("昵称不能为空");
//			Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
//			mEt_nickName.startAnimation(shake);
//			vibrator.vibrate(300);
//			return false;
//		}
		if (TextUtils.isEmpty(userPwd)) {
			mEt_pwd.requestFocus();
			AppToast.getToast().show("密码不能为空");
			Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
			mEt_pwd.startAnimation(shake);
			vibrator.vibrate(300);
			return false;
		}
		if (userPwd.length() < 6) {
			mEt_pwd.requestFocus();
			AppToast.getToast().show("密码格式不正确");
			Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
			mEt_pwd.startAnimation(shake);
			vibrator.vibrate(300);
			return false;
		}
		if (TextUtils.isEmpty(userRpwd)) {
			mEt_rpwd.requestFocus();
			AppToast.getToast().show("重复密码不能为空");
			Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
			mEt_rpwd.startAnimation(shake);
			vibrator.vibrate(300);
			return false;
		}
		if (userRpwd.length() < 6) {
			mEt_rpwd.requestFocus();
			AppToast.getToast().show("重复密码格式不正确");
			Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
			mEt_rpwd.startAnimation(shake);
			vibrator.vibrate(300);
			return false;
		}
		if (!userPwd.equals(userRpwd)) {
			mEt_rpwd.requestFocus();
			AppToast.getToast().show("重复密码和密码不相同");
			Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
			mEt_rpwd.startAnimation(shake);
			vibrator.vibrate(300);
			return false;
		}
		return true;
	}

	private void submitRegist() {
		new Thread(new Runnable() {
			@Override
			public void run() {
//				String result = XmppManager.getInstance().regist(userName, userPwd);
				Userinfo userinfo=new Userinfo();
				userinfo.setUsername(userName);
				userinfo.setPassword(userPwd);
				userinfo.setState("enable");
				userinfo.setRole("user");
				String result= BmobDataUtil.addUserinfo(userinfo).getStatus()+"";
//				if (result.equals("0")) {// 0、 服务器没有返回结果
//					handler.sendEmptyMessage(HandlerTypeUtils.WX_HANDLER_HTTP_EXCEPTION);
//				} 
				if (result.equals("0")) {// 0、注册成功 
					handler.sendEmptyMessage(HandlerTypeUtils.WX_HANDLER_TYPE_LOAD_DATA_SUCCESS);
				} else if (result.equals("1")) {// 1、这个帐号已经存在 
					handler.sendEmptyMessage(HandlerTypeUtils.WX_HANDLER_TYPE_LOAD_DATA_SAME);
				} else if (result.equals("-1")) {// -1、注册失败
					handler.sendEmptyMessage(HandlerTypeUtils.WX_HANDLER_TYPE_LOAD_DATA_FAIL);
				}
			}
		}).start();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home://返回上一菜单页
			AppToast.getToast().show("返回上一页");
			Intent upIntent = NavUtils.getParentActivityIntent(this);
			if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
				TaskStackBuilder.create(this).addNextIntentWithParentStack(upIntent).startActivities();
			} else {
				upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				NavUtils.navigateUpTo(this, upIntent);
			}
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	public boolean providesActivityToolbar() {
		return false;
	}
}
