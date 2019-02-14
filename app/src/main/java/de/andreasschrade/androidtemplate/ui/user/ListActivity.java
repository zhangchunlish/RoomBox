package de.andreasschrade.androidtemplate.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.*;
import android.graphics.drawable.ColorDrawable;
import android.graphics.Color;
import android.util.TypedValue;
import java.util.List;
import android.support.v7.app.ActionBar;

import de.andreasschrade.androidtemplate.Constants;
import de.andreasschrade.androidtemplate.GlobalParams;
import de.andreasschrade.androidtemplate.R;
import de.andreasschrade.androidtemplate.dummy.ResultCode;
import de.andreasschrade.androidtemplate.model.Userinfo;
import de.andreasschrade.androidtemplate.ui.base.BaseActivity;
import de.andreasschrade.androidtemplate.ui.quote.RoomListActivity;
import de.andreasschrade.androidtemplate.util.AppToast;
import de.andreasschrade.androidtemplate.util.BmobDataUtil;
import de.andreasschrade.androidtemplate.util.DataImportUtil;
import de.andreasschrade.androidtemplate.util.HandlerTypeUtils;
import de.andreasschrade.androidtemplate.util.L;
import de.andreasschrade.androidtemplate.util.LogUtil;
import de.andreasschrade.androidtemplate.adapter.UserListAdapter;
import com.baoyz.swipemenulistview.*;
import com.baoyz.swipemenulistview.SwipeMenuListView.*;

/**
 * Lists all available quotes. This Activity supports a single pane (= smartphones) and a two pane mode (= large screens with >= 600dp width).
 *
 * Created by Andreas Schrade on 14.12.2015.
 */
public class ListActivity extends BaseActivity{
    private static final String LOGTAG = LogUtil.makeLogTag(ListActivity.class);

    /**
     * Whether or not the activity is running on a device with a large screen
     */
    private boolean twoPaneMode;
    private SwipeMenuListView listView;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    List<Userinfo> userinfoList= (List<Userinfo>) msg.obj;
                    initData(userinfoList);
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (!GlobalParams.ISLOGIN) {
            L.d(LOGTAG, "ListActivity--->isLogin=" + GlobalParams.ISLOGIN);
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            //			finish();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userlist);
        setupToolbar();
        listView=(SwipeMenuListView)findViewById(R.id.user_listView);
        BmobDataUtil.getUserinfoList(handler);
    }

    public void initData(final List<Userinfo> data){
        final UserListAdapter adapter = new UserListAdapter(this, data);

        listView.setAdapter(adapter);
        listView.setEmptyView(findViewById(R.id.user_listview_empty));


        // step 1. create a MenuCreator
        SwipeMenuCreator creator = new SwipeMenuCreator()
        {
            @Override
            public void create(SwipeMenu menu)
            {
                // create "open" item
//                SwipeMenuItem openItem = new SwipeMenuItem(getApplicationContext());
//                // set item background
//                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9, 0xCE)));
//                // set item width
//                openItem.setWidth(dp2px(90));
//                // set item title
//                openItem.setTitle("打开");
//                // set item title fontsize
//                openItem.setTitleSize(18);
//                // set item title font color
//                openItem.setTitleColor(Color.WHITE);
//                // add to menu
//                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(dp2px(90));
                // set a iconc
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

        // set creator
        listView.setMenuCreator(creator);

        // step 2. listener item click event
        listView.setOnMenuItemClickListener(new OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index)
            {
                Userinfo device = data.get(position);
                switch (index)
                {
//                    case 0:
//                        // open
//                        open(device);
//                        break;
                    case 0:
                        // delete
                        boolean suc=delete(device);
                        if(suc){
                            data.remove(position);
                            adapter.notifyDataSetChanged();
                        }
                        break;
                }
                return true;
            }
        });

        // set SwipeListener
        listView.setOnSwipeListener(new OnSwipeListener()
        {
            @Override
            public void onSwipeStart(int position)
            {
                // swipe start
            }

            @Override
            public void onSwipeEnd(int position)
            {
                // swipe end
            }
        });

        listView.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                Userinfo device = data.get(position);
                open(device);
            }
        });
    }
    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    /**
     * 打开
     * @param item
     */
    private void open(Userinfo item)
    {

    }

    /**
     * 删除
     * @param item
     */
    private boolean delete(Userinfo item)
    {
        if("admin".equals(item.getRole())){
            AppToast.getToast().show("管理员不可删除");
            return false;
        }
        ResultCode res=BmobDataUtil.deleteUserinfo(item);
        if(res.getStatus()==0){
            AppToast.getToast().show("删除成功");
            return true;
        }else{
            AppToast.getToast().show("删除失败");
            return false;
        }
    }
    private void setupToolbar() {
        final ActionBar ab = getActionBarToolbar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sample_actions, menu);
        return true;
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
     * */
    private void modifyPwd(){
        startActivity(new Intent(this, ModifyActivity.class));
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
        return R.id.nav_user;
    }

    @Override
    public boolean providesActivityToolbar() {
        return true;
    }
}
