package de.andreasschrade.androidtemplate.ui.quote;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import butterknife.OnClick;
import de.andreasschrade.androidtemplate.R;
import de.andreasschrade.androidtemplate.dummy.DummyContent;
import de.andreasschrade.androidtemplate.ui.base.BaseActivity;
import de.andreasschrade.androidtemplate.GlobalParams;
import de.andreasschrade.androidtemplate.util.*;
import de.andreasschrade.androidtemplate.ui.user.LoginActivity;

/**
 * Lists all available quotes. This Activity supports a single pane (= smartphones) and a two pane mode (= large screens with >= 600dp width).
 *
 * Created by Andreas Schrade on 14.12.2015.
 */
public class ListActivity extends BaseActivity implements ArticleListFragment.Callback {
    private static final String LOGTAG = LogUtil.makeLogTag(ListActivity.class);

    /**
     * Whether or not the activity is running on a device with a large screen
     */
    private boolean twoPaneMode;

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
        setContentView(R.layout.activity_list);

        setupToolbar();

        if (isTwoPaneLayoutUsed()) {
            twoPaneMode = true;
            LogUtil.logD("TEST","TWO POANE TASDFES");
            enableActiveItemState();
        }

        if (savedInstanceState == null && twoPaneMode) {
            setupDetailFragment();
        }
    }
    public void roomRegist(View view){
        startActivity(new Intent(this, RoomRegistActivity.class));
    }
    /**
     * Called when an item has been selected
     *
     * @param id the selected quote ID
     */
    @Override
    public void onItemSelected(String id) {
        if (twoPaneMode) {
            // Show the quote detail information by replacing the DetailFragment via transaction.
            ArticleDetailFragment fragment = ArticleDetailFragment.newInstance(id);
            getFragmentManager().beginTransaction().replace(R.id.article_detail_container, fragment).commit();
        } else {
            // Start the detail activity in single pane mode.
            Intent detailIntent = new Intent(this, ArticleDetailActivity.class);
            detailIntent.putExtra(ArticleDetailFragment.ARG_ITEM_ID, id);
            startActivity(detailIntent);
        }
    }

    private void setupToolbar() {
        final ActionBar ab = getActionBarToolbar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
    }
//    @OnClick(R.id.fab)
//    public void onFabClicked(View view) {
//        // Start the roomregist activity in single pane mode.
//        Intent registIntent = new Intent(this, RoomRegistActivity.class);
//        startActivity(registIntent);
//    }
    private void setupDetailFragment() {
        ArticleDetailFragment fragment =  ArticleDetailFragment.newInstance(DummyContent.ITEMS.get(0).id);
        getFragmentManager().beginTransaction().replace(R.id.article_detail_container, fragment).commit();
    }

    /**
     * Enables the functionality that selected items are automatically highlighted.
     */
    private void enableActiveItemState() {
        ArticleListFragment fragmentById = (ArticleListFragment) getFragmentManager().findFragmentById(R.id.article_list);
        fragmentById.getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    /**
     * Is the container present? If so, we are using the two-pane layout.
     *
     * @return true if the two pane layout is used.
     */
    private boolean isTwoPaneLayoutUsed() {
        return findViewById(R.id.article_detail_container) != null;
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
