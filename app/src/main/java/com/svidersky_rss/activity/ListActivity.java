package com.svidersky_rss.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import com.svidersky_rss.R;
import com.svidersky_rss.service.NotificationService;


public class ListActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multi_activity);
        Intent intent = new Intent(this, NotificationService.class);
        startService(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.share).setVisible(false);
        return true;
    }
}