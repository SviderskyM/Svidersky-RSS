package com.svidersky_rss.fragments;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.svidersky_rss.R;
import com.svidersky_rss.activity.ContentActivity;

/**
 * Created by Eren on 03.01.2015.
 */
public class ListFragment extends BaseFragment implements ListView.OnItemClickListener {
    private boolean duelPane;
    private int checkPosition = -1;
    private ListView listView;

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

        if (duelPane) {
            listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            showDetails(checkPosition);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = (ListView) view.findViewById(R.id.rss_feed);
        listView.setOnItemClickListener(this);

        new GetData(getActivity(), listView).execute();
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

            if (info == null) {
                info = ContentFragment.newInstance(-1);
            } else {
                info = ContentFragment.newInstance(index);
            }
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.information, info);
            //TRANSIT_FRAGMENT_FADE calls for the Fragment to fade away
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();
        } else {
            //Launch a new Activity
            Intent intent = new Intent();
            //define the class activity to call
            intent.setClass(getActivity(), ContentActivity.class);
            intent.putExtra("index", index);
            startActivity(intent);
        }
    }
}
