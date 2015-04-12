package com.svidersky_rss.fragments;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.svidersky_rss.Constants;
import com.svidersky_rss.R;
import com.svidersky_rss.activity.ContentActivity;
import com.svidersky_rss.utils.DB;
import com.svidersky_rss.utils.FirstIndexShow;

/**
 * Created by Eren on 03.01.2015.
 */
public class ListFragment extends BaseFragment implements ListView.OnItemClickListener, FirstIndexShow {

    private boolean duelPane;
    private int checkPosition = 0;
    private static ListView listView;
    private DB db;
    private ProgressBar progressBar;
    private static ContentFragment info = null;
    public Menu mOptions;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.title_list, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (getResources().getBoolean(R.bool.isTable)) {
            setHasOptionsMenu(true);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View infoFrame = getActivity().findViewById(R.id.information);
        duelPane = infoFrame != null && infoFrame.getVisibility() == View.VISIBLE;
        if (savedInstanceState != null) {
            checkPosition = savedInstanceState.getInt("choice", 0);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = (ListView) view.findViewById(R.id.rss_feed);
        listView.setOnItemClickListener(this);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        if (Constants.flag) {
            db = new DB(getActivity());
            if (isTable())
                new GetData2(getActivity(), listView, progressBar, this).execute(db);
            else
                new GetData2(getActivity(), listView, progressBar).execute(db);
        } else {
            if (isTable())
                new GetData(getActivity(), listView, progressBar, this).execute();
            else
                new GetData(getActivity(), listView, progressBar).execute();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("choice", checkPosition);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        showContent(i);
    }

    @Override
    public void showIndex() {
        showContent(0);
    }

    public void showContent(int index) {
        checkPosition = index;
        if (duelPane) {
            listView.setItemChecked(index, true);
            info = (ContentFragment)
                    getFragmentManager().findFragmentById(R.id.information);
            if (info == null || (info.getIndex() != index || index == 0)) {
                info = ContentFragment.newInstance(index);
                db = new DB(getActivity());
                if(Constants.flag)
                    Constants.TwoFalg = true;
                else
                    Constants.TwoFalg = false;
                if(info.checkDB(db)){
                    MenuItem liked = mOptions.findItem(R.id.liked);
                    liked.setVisible(true);
                    MenuItem not_liked = mOptions.findItem(R.id.not_liked);
                    not_liked.setVisible(false);
                }else{
                    MenuItem not_liked = mOptions.findItem(R.id.not_liked);
                    not_liked.setVisible(true);
                    MenuItem liked = mOptions.findItem(R.id.liked);
                    liked.setVisible(false);
                }
                getFragmentManager().beginTransaction()
                        .replace(R.id.information, info)
                                //TRANSIT_FRAGMENT_FADE calls for the Fragment to fade away
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit();
            }
        } else {
            Intent intent = new Intent();
            intent.setClass(getActivity(), ContentActivity.class);
            intent.putExtra("index", index);
            startActivity(intent);
        }
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        mOptions = menu;
        if (getResources().getBoolean(R.bool.isTable)) {
            db = new DB(getActivity());
                menu.findItem(R.id.not_liked).setVisible(true);
                menu.findItem(R.id.liked).setVisible(false);
            menu.findItem(R.id.share).setVisible(true);
            menu.findItem(R.id.favourite).setVisible(true);
        }
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        final String[] items = {"facebook", "google+"};
        db = new DB(getActivity());
        switch (item.getItemId()) {
            case R.id.liked:
                info.deleteNews(db);
                item.setVisible(false);
                MenuItem not_liked = mOptions.findItem(R.id.not_liked);
                not_liked.setVisible(true);
                return true;
            case R.id.not_liked:
                info.addNews(db);
                item.setVisible(false);
                MenuItem liked = mOptions.findItem(R.id.liked);
                liked.setVisible(true);
                return true;
            case R.id.share:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.share);
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item) {
                            case 0:
                                info.shareFacebook();

                                break;
                            case 1:
                                info.shareGooglePlus();
                                break;
                        }
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
