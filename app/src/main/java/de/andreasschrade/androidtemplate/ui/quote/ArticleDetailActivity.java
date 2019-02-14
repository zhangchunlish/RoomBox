package de.andreasschrade.androidtemplate.ui.quote;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import de.andreasschrade.androidtemplate.GlobalParams;
import de.andreasschrade.androidtemplate.R;
import de.andreasschrade.androidtemplate.model.Room;
import de.andreasschrade.androidtemplate.ui.base.BaseActivity;

/**
 * Simple wrapper for {@link ArticleDetailFragment}
 * This wrapper is only used in single pan mode (= on smartphones)
 * Created by Andreas Schrade on 14.12.2015.
 */
public class ArticleDetailActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Show the Up button in the action bar.
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ArticleDetailFragment fragment =  ArticleDetailFragment.newInstance(getIntent().getStringExtra(ArticleDetailFragment.ARG_ITEM_ID));
        getFragmentManager().beginTransaction().replace(R.id.article_detail_container, fragment).commit();
    }
    public void modifyClicked(View view){
        Intent modifyIntent = new Intent(GlobalParams.activity, RoomModifyActivity.class);
        modifyIntent.putExtra(ArticleDetailFragment.ARG_ITEM_ID, getIntent().getStringExtra(ArticleDetailFragment.ARG_ITEM_ID));
        startActivity(modifyIntent);
    }
    @Override
    public boolean providesActivityToolbar() {
        return false;
    }
}
