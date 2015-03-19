package com.svidersky_rss.fragments;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.svidersky_rss.Constants;
import com.svidersky_rss.R;
import com.svidersky_rss.activity.ContentActivity;
import com.svidersky_rss.adapters.MyAdapter;
import com.svidersky_rss.utils.DB;
import com.svidersky_rss.utils.Structure;

import java.util.ArrayList;

/**
 * Created by Eren on 03.01.2015.
 */
public class ListFragment extends BaseFragment implements ListView.OnItemClickListener {

    private boolean duelPane;
    private int checkPosition = 0;
    private static ListView listView;
    private DB db;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.title_list, container, false);
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
        if(Constants.flag){
            db = new DB(getActivity());
            new GetData2(getActivity(), listView, progressBar).execute(db);
        }
        else {
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
        showDetails(i);
    }

    void showDetails(int index) {
        checkPosition = index;
        if (duelPane) {
            listView.setItemChecked(index, true);
            ContentFragment info = (ContentFragment)
                    getFragmentManager().findFragmentById(R.id.information);
            if (info == null || (info.getIndex() != index || index == 0)) {
                info = ContentFragment.newInstance(index);
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
}
