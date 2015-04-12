package com.svidersky_rss.activity;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.widget.FacebookDialog;

import com.google.android.gms.plus.PlusShare;
import com.svidersky_rss.Constants;
import com.svidersky_rss.R;
import com.svidersky_rss.fragments.ListFragment;
import com.svidersky_rss.service.NotificationService;
import com.svidersky_rss.utils.DB;


public class ListActivity extends BaseActivity {
    private DB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multi_activity);
        Intent intent = new Intent(this, NotificationService.class);
        startService(intent);
        mSettings = getSharedPreferences(Constants.APP_PREFERENCES,
                Context.MODE_PRIVATE);
        db = new DB(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.liked).setVisible(false);
        menu.findItem(R.id.share).setVisible(false);
        menu.findItem(R.id.favourite).setVisible(true);
        menu.findItem(R.id.not_liked).setVisible(false);
        if(getResources().getBoolean(R.bool.isTable)){
            menu.findItem(R.id.share).setVisible(false);
            menu.findItem(R.id.favourite).setVisible(false);
            menu.findItem(R.id.not_liked).setVisible(false);
            menu.findItem(R.id.liked).setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}