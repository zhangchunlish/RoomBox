package de.andreasschrade.androidtemplate.ui.quote;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;

import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;


import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnSwipeListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import de.andreasschrade.androidtemplate.GlobalParams;
import de.andreasschrade.androidtemplate.R;
import de.andreasschrade.androidtemplate.adapter.*;
import de.andreasschrade.androidtemplate.dummy.ResultCode;
import de.andreasschrade.androidtemplate.model.*;
import de.andreasschrade.androidtemplate.ui.SettingsActivity;
import de.andreasschrade.androidtemplate.ui.base.BaseActivity;
import de.andreasschrade.androidtemplate.ui.user.LoginActivity;
import de.andreasschrade.androidtemplate.ui.user.ModifyActivity;
import de.andreasschrade.androidtemplate.util.AppToast;
import de.andreasschrade.androidtemplate.util.BmobDataUtil;
import de.andreasschrade.androidtemplate.util.DataImportUtil;
import de.andreasschrade.androidtemplate.util.L;
import de.andreasschrade.androidtemplate.util.LogUtil;

/**
 * Lists all available quotes. This Activity supports a single pane (= smartphones) and a two pane mode (= large screens with >= 600dp width).
 *
 * Created by Andreas Schrade on 14.12.2015.
 */
public class RoomListActivity extends BaseActivity{
    private static final String LOGTAG = LogUtil.makeLogTag(RoomListActivity.class);

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
                    List<Room> roomList= (List<Room>) msg.obj;
                    initData(roomList);
                    break;
                case 1:
                    List<Room> list= (List<Room>) msg.obj;
                    filterRoomList(list);
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
        setContentView(R.layout.activity_roomlist);
        setupToolbar();
        listView=(SwipeMenuListView)findViewById(R.id.room_listView);
        BmobDataUtil.getRoomList(GlobalParams.USERNAME,handler);
    }
    public void roomRegist(View view){
        startActivity(new Intent(this, RoomRegistActivity.class));
    }
    public void initData(final List<Room> data){
        final RoomListAdapter adapter = new RoomListAdapter(this, data);

        listView.setAdapter(adapter);
        listView.setEmptyView(findViewById(R.id.room_listview_empty));
        listView.setTextFilterEnabled(true);

        //设置搜索文本监听
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
//            @Override
//            public boolean onQueryTextSubmit(String query){
//                return false;
//            }
//            public boolean onQueryTextChange(String newText){
//                if(!TextUtils.isEmpty(newText)){
//                    listView.setFilterText(newText);
//                }else{
//                    listView.clearTextFilter();
//                }
//                return false;
//            }
//        });
//// 4. 设置点击键盘上的搜索按键后的操作（通过回调接口）
//        // 参数 = 搜索框输入的内容
//        searchView.setOnClickSearch(new ICallBack() {
//            @Override
//            public void SearchAciton(String string) {
//                System.out.println("我收到了" + string);
//            }
//        });
//
//        // 5. 设置点击返回按键后的操作（通过回调接口）
//        searchView.setOnClickBack(new bCallBack() {
//            @Override
//            public void BackAciton() {
//                finish();
//            }
//        });
        // step 1. create a MenuCreator
        SwipeMenuCreator creator = new SwipeMenuCreator()
        {
            @Override
            public void create(SwipeMenu menu)
            {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9, 0xCE)));
                // set item width
                openItem.setWidth(dp2px(90));
                // set item title
                openItem.setTitle("打开");
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

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
                Room device = data.get(position);
                switch (index)
                {
                    case 0:
                        // open
                        open(device);
                        break;
                    case 1:
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

                Room device = data.get(position);
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
    private void open(Room item)
    {
        Intent detailIntent = new Intent(this, ArticleDetailActivity.class);
        detailIntent.putExtra(ArticleDetailFragment.ARG_ITEM_ID, item.getObjectId());
        startActivity(detailIntent);
    }

    /**
     * 删除
     * @param item
     */
    private boolean delete(Room item)
    {
        ResultCode res=BmobDataUtil.deleteRoom(item);
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
        final Context that=this;
        getMenuInflater().inflate(R.menu.sample_actions, menu);
        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem item=menu.findItem(R.id.search).setVisible(true);
        SearchView searchView = (SearchView)item.getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query) {
                getRoomList(query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                getRoomList(newText);
                return false;
            }
        });
        return true;
    }
    public void getRoomList(String query){
        if(!TextUtils.isEmpty(query)){
            BmobDataUtil.getRoomList(GlobalParams.USERNAME,query,handler);
        }else{
            BmobDataUtil.getRoomList(GlobalParams.USERNAME,handler);
        }
    }
    public void filterRoomList(List<Room> data){
        RoomListAdapter adapter = new RoomListAdapter(this, data);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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
        return R.id.nav_quotes;
    }

    @Override
    public boolean providesActivityToolbar() {
        return true;
    }
}
