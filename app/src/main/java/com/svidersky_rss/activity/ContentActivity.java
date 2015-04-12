package com.svidersky_rss.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.svidersky_rss.R;
import com.svidersky_rss.fragments.ContentFragment;
import com.svidersky_rss.utils.DB;


/**
 * Created by Eren on 17.12.2014.
 */
public class ContentActivity extends BaseActivity {
    private ContentFragment contentFragment ;
    private DB db;
    private Menu mOptionsMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DB(this);
        if (getResources().getBoolean(R.bool.isTable)) {
            finish();
            return;
        }
        else getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (savedInstanceState == null) {
            contentFragment = new ContentFragment();
            contentFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().add(android.R.id.content, contentFragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        mOptionsMenu= menu;
        menu.findItem(R.id.favourite).setVisible(false);
        menu.findItem(R.id.share).setVisible(true);
        menu.findItem(R.id.profile).setVisible(false);
        if(contentFragment.checkDB(db)){
            menu.findItem(R.id.not_liked).setVisible(false);
            menu.findItem(R.id.liked).setVisible(true);
        }
        else {
            menu.findItem(R.id.not_liked).setVisible(true);
            menu.findItem(R.id.liked).setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.share:
                return contentFragment.onOptionsItemSelected(item);
            case R.id.liked:
                contentFragment.deleteNews(db);
                item.setVisible(false);
                MenuItem not_liked = mOptionsMenu.findItem(R.id.not_liked);
                not_liked.setVisible(true);
                return true;
            case R.id.not_liked:
                contentFragment.addNews(db);
                item.setVisible(false);
                MenuItem liked = mOptionsMenu.findItem(R.id.liked);
                liked.setVisible(true);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
