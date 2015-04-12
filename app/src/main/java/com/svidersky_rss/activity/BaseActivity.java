package com.svidersky_rss.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.svidersky_rss.Constants;
import com.svidersky_rss.R;

/**
 * Created by Eren on 10.02.2015.
 */
public class BaseActivity extends ActionBarActivity {
    SharedPreferences mSettings;

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
        SharedPreferences mySharedPreferences = getSharedPreferences
                (Constants.APP_PREFERENCES, Context.MODE_PRIVATE);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.favourite:
                startActivity(new Intent(this, FavoriteActivity.class));
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