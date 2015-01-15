package com.svidersky_rss.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.svidersky_rss.service.NotificationService;
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

    private static ArrayList<Structure> list = new ArrayList<>();
    public static String url = "https://gdata.youtube.com/feeds/api/users/itsSokolOff/uploads?v=2&alt=jsonc&max-results=20";
    private static ListView listView;

    public String getTitle(int id) {
        return list.get(id).getTitle();
    }

    public String getDescription(int id) {
        return list.get(id).getDescription();
    }

    public String getPicture(int id) {
        return list.get(id).getPicture();
    }

    public String getUploaded(int id) {
        return list.get(id).getUploaded();
    }

    public String getVideo(int id) {
        return list.get(id).getVideo();
    }

    public static class GetData extends AsyncTask<Void, Void, Void> {

        private Context context;
        public JSONArray video = null;

        public GetData(Context context, ListView lw) {
            this.context = context;
            listView = lw;
        }

        public GetData(Context context) {
            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);
            Log.d("Response: ", "> " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONObject data = jsonObj.getJSONObject("data");
                    video = data.getJSONArray("items");
                    list.clear();
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
                        list.add(structure);
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
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            MyAdapter adapter = new MyAdapter(context, list);
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

        public int getCount() {
            return count;
        }

        public String getLastNews() {
            int last = list.size() - 1;
            return list.get(last).getTitle();
        }
    }

    public static class ShowNews {

        private ArrayList<Structure> arrayList;
        private Context context;

        public ShowNews(Context context, ArrayList<Structure> arrayList) {
            this.context = context;
            this.arrayList = arrayList;
            saveNews(arrayList);
        }

        public void saveNews(ArrayList<Structure> arrayList) {
            MyAdapter adapter = new MyAdapter(context, arrayList);
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }
}
