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
import com.svidersky_rss.utils.ConnChecker;
import com.svidersky_rss.utils.DB;
import com.svidersky_rss.utils.FirstIndexShow;
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
public abstract class BaseFragment extends Fragment {

    private static ArrayList<Structure> listAll = new ArrayList<>();
    public static ArrayList<Structure> listFavorite = new ArrayList<>();
    private static ListView listView;
    protected UiLifecycleHelper uiHelper;
    private static FirstIndexShow please;
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
                (activeSession.isOpened() || activeSession.isClosed())) {
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

    public boolean isTable() {
        return getResources().getBoolean(R.bool.isTable);
    }


    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (state.isOpened()) {
            Log.i(TAG, "Logged in...");

        } else if (state.isClosed()) {
            Log.i(TAG, "Logged out...");
        }
    }


    public String getTitle(int id) {
        return listAll.get(id).getTitle();
    }

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

    public String getTitleDB(int id) {
        return listFavorite.get(id).getTitle();
    }

    public String getDescriptionDB(int id) {
        return listFavorite.get(id).getDescription();
    }

    public String getPictureDB(int id) {
        return listFavorite.get(id).getPicture();
    }

    public String getUploadedDB(int id) {
        return listFavorite.get(id).getUploaded();
    }

    public String getVideoDB(int id) {
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
        public GetData(Context context, ListView lw, ProgressBar progressBar, FirstIndexShow pleas) {
            this.context = context;
            listView = lw;
            this.progressBar = progressBar;
            please = pleas;
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            ServiceHandler sh = new ServiceHandler();
            if (ConnChecker.isOnline(context)) {
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
                            String picture = thumbnail.getString("hqDefault");
                            Structure structure = new Structure(title, picture, video, uploaded, description);
                            listAll.add(structure);
                            count++;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("ServiceHandler", "Couldn't get any data from the url");
                    Toast.makeText(context, "Немає інету, братіш провірь підключення!", Toast.LENGTH_LONG).show();
                }
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
            if ((listAll.size()==0)){
                Toast.makeText(context, "Please, check internet connection", Toast.LENGTH_LONG).show();
            }
            MyAdapter adapter = new MyAdapter(context, listAll);
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            if(context.getResources().getBoolean(R.bool.isTable)){
                please.showIndex();
            }
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

        public GetData2(Context context, ListView lw, ProgressBar progressBar, FirstIndexShow ples) {
            this.context = context;
            listView = lw;
            this.progressBar = progressBar;
            please = ples;
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