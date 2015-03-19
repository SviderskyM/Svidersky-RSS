package com.svidersky_rss.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.svidersky_rss.R;
import com.svidersky_rss.fragments.BaseFragment;
import com.svidersky_rss.utils.DB;

/**
 * Created by Eren on 10.02.2015.
 */
public class BaseActivity extends ActionBarActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_launcher);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.favourite:
                startActivity(new Intent(this, Favorite.class));
                return true;
            case R.id.profile:
                changeActivity(ProfileActivity.class);
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }
    public void changeActivity(Class<?> cls) {
        Intent i = new Intent(this, cls);
        startActivity(i);
        finish();
    }
}