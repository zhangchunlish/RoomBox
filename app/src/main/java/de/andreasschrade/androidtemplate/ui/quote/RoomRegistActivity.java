package de.andreasschrade.androidtemplate.ui.quote;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.ButterKnife;
import butterknife.OnClick;
import de.andreasschrade.androidtemplate.GlobalParams;
import de.andreasschrade.androidtemplate.R;
import de.andreasschrade.androidtemplate.model.*;
import de.andreasschrade.androidtemplate.ui.SettingsActivity;
import de.andreasschrade.androidtemplate.ui.user.LoginActivity;
import de.andreasschrade.androidtemplate.ui.user.ModifyActivity;
import de.andreasschrade.androidtemplate.util.*;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.OnClickListener;
import de.andreasschrade.androidtemplate.ui.base.BaseActivity;

/**
 * 注册room
 * 
 */
public class RoomRegistActivity extends BaseActivity implements OnClickListener{

	protected static final String LOGTAG = LogUtil.makeLogTag(RoomRegistActivity.class);
	/**
	 * 房间编号输入框
	 */
	private EditText mEt_roomId;
	/**
	 * 位置信息输入框
	 */
	private EditText mEt_roomAddress;
	/**
	 * 用户名输入框
	 */
	private EditText mEt_userName;
	/**
	 * 密码输入框
	 */
	private EditText mEt_pwd;
	/**
	 * 重复密码输入框
	 */
//	private EditText mEt_rpwd;
	/**
	 * 注册按钮
	 */
	private Button mBtn_regist;
	private SpUtil sp;
	private String roomId="";
	private String roomAddress="";
	private String userName = "";
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
				//sp.saveString(Constants.XMPP_USERNAME, userName);
				//sp.saveString(Constants.XMPP_PASSWORD, userPwd);
				AppToast.getToast().show("注册成功");
				GlobalParams.ISLOGIN = true;
				Intent intent = new Intent(RoomRegistActivity.this, RoomListActivity.class);
				startActivity(intent);
				finish();
				break;
			case HandlerTypeUtils.WX_HANDLER_TYPE_LOAD_DATA_SAME:
				mEt_userName.requestFocus();
				AppToast.getToast().show("该房间已被注册");
				Animation shake = AnimationUtils.loadAnimation(RoomRegistActivity.this, R.anim.shake);
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
		setContentView(R.layout.activity_roomregist);

		ButterKnife.bind(this);
		setupToolbar();
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
//		actionBar.setTitle(R.string.text_menu_regist_room_title);
		
		mEt_roomId=(EditText) findViewById(R.id.cgt_et_regist_room_id);
		mEt_roomAddress=(EditText) findViewById(R.id.cgt_et_regist_room_address);
		mEt_userName = (EditText) findViewById(R.id.cgt_et_regist_room_username);
		mEt_pwd = (EditText) findViewById(R.id.cgt_et_regist_room_pwd);
//		mEt_rpwd = (EditText) findViewById(R.id.cgt_et_regist_room_rpwd);
		mBtn_regist = (Button) findViewById(R.id.cgt_btn_regist_room);
		mBtn_regist.setOnClickListener(this);
	}

	private void initData() {
		sp = new SpUtil(this);
		mEt_roomId.requestFocus();
		vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cgt_btn_regist_room:
			if (checkValidity()) {
				submitRegist();
			}
			break;

		default:
			break;
		}
	}

	private void getUserInfo() {
		roomId=mEt_roomId.getText().toString().trim();
		roomAddress=mEt_roomAddress.getText().toString().trim();
		userName = mEt_userName.getText().toString().trim();
		userPwd = mEt_pwd.getText().toString().trim();
//		userRpwd = mEt_rpwd.getText().toString().trim();
	}

	private boolean checkValidity() {
		getUserInfo();
		if (TextUtils.isEmpty(roomId)) {
			mEt_roomId.requestFocus();
			AppToast.getToast().show("房间编号不能为空");
			Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
			mEt_roomId.startAnimation(shake);
			vibrator.vibrate(300);
			return false;
		}
		if (TextUtils.isEmpty(userName)) {
			mEt_userName.requestFocus();
			AppToast.getToast().show("用户名不能为空");
			Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
			mEt_userName.startAnimation(shake);
			vibrator.vibrate(300);
			return false;
		}
		if (TextUtils.isEmpty(userPwd)) {
			mEt_pwd.requestFocus();
			AppToast.getToast().show("密码不能为空");
			Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
			mEt_pwd.startAnimation(shake);
			vibrator.vibrate(300);
			return false;
		}
		if (userPwd.length() < 6||userPwd.length()>8 ||!isNumeric(userPwd)) {
			mEt_pwd.requestFocus();
			AppToast.getToast().show("密码必须为6~8位数字");
			Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
			mEt_pwd.startAnimation(shake);
			vibrator.vibrate(300);
			return false;
		}
//		if (TextUtils.isEmpty(userRpwd)) {
//			mEt_rpwd.requestFocus();
//			AppToast.getToast().show("重复密码不能为空");
//			Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
//			mEt_rpwd.startAnimation(shake);
//			vibrator.vibrate(300);
//			return false;
//		}
//		if (userRpwd.length() < 6||userRpwd.length()>8 ||!isNumeric(userRpwd)) {
//			mEt_rpwd.requestFocus();
//			AppToast.getToast().show("密码必须为6~8位数字");
//			Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
//			mEt_rpwd.startAnimation(shake);
//			vibrator.vibrate(300);
//			return false;
//		}
//		if (!userPwd.equals(userRpwd)) {
//			mEt_rpwd.requestFocus();
//			AppToast.getToast().show("重复密码和密码不相同");
//			Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
//			mEt_rpwd.startAnimation(shake);
//			vibrator.vibrate(300);
//			return false;
//		}
		return true;
	}

	private void submitRegist() {
		new Thread(new Runnable() {
			@Override
			public void run() {
//				String result = XmppManager.getInstance().regist(userName, userPwd);
				Room room=new Room();
				room.setRoomid(roomId);
				room.setComment(roomAddress);
				room.setUsername(userName);
				room.setPassword(userPwd);
				room.setCreator(GlobalParams.USERNAME);
				BmobDataUtil.addRoom(room,handler);
			}
		}).start();
	}

//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		switch (item.getItemId()) {
//		case android.R.id.home://返回上一菜单页
//			AppToast.getToast().show("返回上一页");
//			Intent upIntent = NavUtils.getParentActivityIntent(this);
//			if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
//				TaskStackBuilder.create(this).addNextIntentWithParentStack(upIntent).startActivities();
//			} else {
//				upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				NavUtils.navigateUpTo(this, upIntent);
//			}
//			break;
//
//		default:
//			break;
//		}
//		return super.onOptionsItemSelected(item);
//	}
	/**
     * 利用正则表达式判断字符串是否是数字
     * @param str
     * @return
     */
    public boolean isNumeric(String str){
           Pattern pattern = Pattern.compile("[0-9]*");
           Matcher isNum = pattern.matcher(str);
           if( !isNum.matches() ){
               return false;
           }
           return true;
    }


//	@OnClick(R.id.fab)
//	public void onFabClicked(View view) {
//		Snackbar.make(view, "Hello Snackbar!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
//	}
	private void setupToolbar() {
		final android.support.v7.app.ActionBar ab = getActionBarToolbar();
		ab.setHomeAsUpIndicator(R.drawable.ic_menu);
		ab.setDisplayHomeAsUpEnabled(true);
	}
	/**
	 * 退出登录
	 */
	private void logout(){
		GlobalParams.ISLOGIN=false;
		GlobalParams.USERNAME="";
		Intent intent = new Intent(this, LoginActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(intent);
		//			finish();
	}
	/**
	 * 修改密码
	 f     */
	private void modifyPwd(){
		startActivity(new Intent(this, ModifyActivity.class));
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.sample_actions, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				openDrawer();
				return true;
			case R.id.user_logout:
				logout();
				return true;
			case R.id.pwd_modify:
				modifyPwd();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected int getSelfNavDrawerItem() {
		return R.id.nav_samples;
	}

	@Override
	public boolean providesActivityToolbar() {
		return true;
	}
}
