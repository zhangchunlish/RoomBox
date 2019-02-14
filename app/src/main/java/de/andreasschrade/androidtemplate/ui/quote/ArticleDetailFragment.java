package de.andreasschrade.androidtemplate.ui.quote;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.Bind;
import de.andreasschrade.androidtemplate.GlobalParams;
import de.andreasschrade.androidtemplate.R;
import de.andreasschrade.androidtemplate.dummy.DummyContent;
import de.andreasschrade.androidtemplate.ui.SettingsActivity;
import de.andreasschrade.androidtemplate.ui.base.BaseActivity;
import de.andreasschrade.androidtemplate.ui.base.BaseFragment;
import de.andreasschrade.androidtemplate.model.*;
import de.andreasschrade.androidtemplate.ui.user.LoginActivity;
import de.andreasschrade.androidtemplate.ui.user.ModifyActivity;
import de.andreasschrade.androidtemplate.util.BmobDataUtil;
import de.andreasschrade.androidtemplate.util.DataImportUtil;
import java.util.List;
/**
 * Shows the quote detail page.
 *
 * Created by Andreas Schrade on 14.12.2015.
 */
public class ArticleDetailFragment extends BaseFragment {

    /**
     * The argument represents the dummy item ID of this fragment.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content of this fragment.
     */
    private Room dummyItem;

    @Bind(R.id.cgt_et_regist_room_address)
    TextView roomAddress;

    @Bind(R.id.cgt_et_regist_room_id)
    TextView roomId;

    @Bind(R.id.cgt_et_regist_room_username)
    TextView userName;

    @Bind(R.id.cgt_et_regist_room_pwd)
    TextView pwd;

    @Bind(R.id.cgt_et_regist_room_creator)
    TextView creator;

    @Bind(R.id.backdrop)
    ImageView backdropImg;

    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;

//    @Bind(R.id.cgt_btn_modify_room)
//    Button mBtn_modify;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    dummyItem=(Room) msg.obj;
                    if (dummyItem != null) {
                        loadBackdrop();
                        collapsingToolbar.setTitle(dummyItem.getRoomid());
                        roomId.setText(dummyItem.getRoomid());
                        roomAddress.setText(dummyItem.getComment());
                        userName.setText(dummyItem.getUsername());
                        pwd.setText(dummyItem.getPassword());
                        creator.setText(dummyItem.getCreator());
                    }
                    break;

            }
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // load dummy item by using the passed item ID.
            String id=getArguments().getString(ARG_ITEM_ID);
            BmobDataUtil.getRoomByObjectId(id,handler);
        }
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflateAndBind(inflater, container, R.layout.fragment_article_detail);

        if (!((BaseActivity) getActivity()).providesActivityToolbar()) {
            // No Toolbar present. Set include_toolbar:
            ((BaseActivity) getActivity()).setToolbar((Toolbar) rootView.findViewById(R.id.toolbar));
        }
//        mBtn_modify.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                modifyClicked(view);
//            }
//        });
        return rootView;
    }
    public void modifyClicked(View view){
        Intent modifyIntent = new Intent(GlobalParams.activity, RoomModifyActivity.class);
        modifyIntent.putExtra(ArticleDetailFragment.ARG_ITEM_ID, dummyItem.getId());
        startActivity(modifyIntent);
    }
    private void loadBackdrop() {
        Glide.with(this).load(R.drawable.p1).centerCrop().into(backdropImg);
    }
    /**
     * 退出登录
     */
    private void logout(){
        GlobalParams.ISLOGIN=false;
        GlobalParams.USERNAME="";
        Intent intent = new Intent(GlobalParams.activity, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        //			finish();
    }
    /**
     * 修改密码
     f     */
    private void modifyPwd(){
        startActivity(new Intent(GlobalParams.activity, ModifyActivity.class));
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.sample_actions, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.user_logout:
                logout();
                return true;
            case R.id.pwd_modify:
                modifyPwd();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static ArticleDetailFragment newInstance(String itemID) {
        ArticleDetailFragment fragment = new ArticleDetailFragment();
        Bundle args = new Bundle();
        args.putString(ArticleDetailFragment.ARG_ITEM_ID, itemID);
        fragment.setArguments(args);
        return fragment;
    }

    public ArticleDetailFragment() {}
}
