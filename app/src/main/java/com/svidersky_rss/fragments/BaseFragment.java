package com.svidersky_rss.fragments;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInstaller;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.svidersky_rss.Constants;
import com.svidersky_rss.R;
import com.svidersky_rss.utils.DB;
import com.svidersky_rss.utils.ServiceHandler;
import com.svidersky_rss.utils.Structure;
import com.svidersky_rss.adapters.MyAdapter;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Eren on 03.01.2015.
 */
public class BaseFragment extends Fragment {

    private static ArrayList<Structure> listAll = new ArrayList<>();
    public static ArrayList<Structure> listFavorite = new ArrayList<>();
    private static ListView listView;

    protected UiLifecycleHelper uiHelper;
    private Session activeSession = Session.getActiveSession();
    private static final String TAG = "Facebook";
    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiHelper = new UiLifecycleHelper(getActivity(), callback);
        uiHelper.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (activeSession != null &&
                (activeSession.isOpened() || activeSession.isClosed()) ) {
            onSessionStateChange(activeSession, activeSession.getState(), null);
        }
        uiHelper.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }

    public boolean isTable(){
        return getResources().getBoolean(R.bool.isTable);
    }


    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (state.isOpened()) {
            Log.i(TAG, "Logged in...");

        } else if (state.isClosed()) {
            Log.i(TAG, "Logged out...");
        }
    }


    public String getTitle(int id) { return listAll.get(id).getTitle();}

    public String getDescription(int id) {
        return listAll.get(id).getDescription();
    }

    public String getPicture(int id) {
        return listAll.get(id).getPicture();
    }

    public String getUploaded(int id) {
        return listAll.get(id).getUploaded();
    }

    public String getVideo(int id) {
        return listAll.get(id).getVideo();
    }

    public String getTitleF(int id) { return listFavorite.get(id).getTitle();}

    public String getDescriptionF(int id) {
        return listFavorite.get(id).getDescription();
    }

    public String getPictureF(int id) {
        return listFavorite.get(id).getPicture();
    }

    public String getUploadedF(int id) {
        return listFavorite.get(id).getUploaded();
    }

    public String getVideoF(int id) {
        return listFavorite.get(id).getVideo();
    }

    public static class GetData extends AsyncTask<Void, Void, Void> {

        private Context context;
        public JSONArray video = null;
        private static int count = 0;
        private ProgressBar progressBar;


        public GetData(Context context, ListView lw, ProgressBar progressBar) {
            this.context = context;
            listView = lw;
            this.progressBar = progressBar;
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            ServiceHandler sh = new ServiceHandler();
            String jsonStr = sh.makeServiceCall(Constants.url, ServiceHandler.GET);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONObject data = jsonObj.getJSONObject("data");
                    video = data.getJSONArray("items");
                    listAll.clear();
                    count = 0;
                    for (int i = 0; i < video.length(); i++) {
                        JSONObject items = video.getJSONObject(i);
                        String title = items.getString("title");
                        String uploaded = items.getString("uploaded");
                        String description = items.getString("description");
                        JSONObject thumbnail = items.getJSONObject("thumbnail");
                        JSONObject content = items.getJSONObject("player");//content
                        String video = content.getString("mobile");
                        String picture = thumbnail.getString("sqDefault");
                        Structure structure = new Structure(title, picture, video, uploaded, description);
                        listAll.add(structure);
                        count++;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
                Toast.makeText(context, "Немає інету, братіш провірь підключення!", Toast.LENGTH_LONG);
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setIndeterminate(true);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setVisibility(View.GONE);
            MyAdapter adapter = new MyAdapter(context, listAll);
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

        public int getCount() {
            return count;
        }

        public String getLastNews() {
            int last = listAll.size() - 1;
            return listAll.get(last).getTitle();
        }
    }

    public static class GetData2 extends AsyncTask<DB, Void, Void> {

        private Context context;
        private ProgressBar progressBar;

        public GetData2(Context context, ListView lw, ProgressBar progressBar) {
            this.context = context;
            listView = lw;
            this.progressBar = progressBar;
        }

        @Override
        protected Void doInBackground(DB... params) {
            listFavorite.clear();
            listFavorite = params[0].getNews();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setIndeterminate(true);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setVisibility(View.GONE);
            MyAdapter adapter = new MyAdapter(context, listFavorite);
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }
}