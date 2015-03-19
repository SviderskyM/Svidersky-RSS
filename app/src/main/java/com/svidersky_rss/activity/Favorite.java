package com.svidersky_rss.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.svidersky_rss.Constants;
import com.svidersky_rss.R;
import com.svidersky_rss.fragments.BaseFragment;
import com.svidersky_rss.fragments.ListFragment;
import com.svidersky_rss.utils.DB;

/**
 * Created by Eren on 10.02.2015.
 */
public class Favorite extends BaseActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Constants.flag = true;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.multi_activity);
    }
    @Override
    protected void onStop() {
        super.onStop();
        Constants.flag = false;
    }
    @Override
    protected void onResume() {
        super.onResume();
        Constants.flag = true;
    }
    @Override
    protected void onStart() {
        super.onStart();
        Constants.flag = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.favourite).setVisible(false);
        menu.findItem(R.id.share).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }
}
