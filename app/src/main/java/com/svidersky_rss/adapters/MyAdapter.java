package com.svidersky_rss.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;

import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.svidersky_rss.R;
import com.svidersky_rss.fragments.BaseFragment;
import com.svidersky_rss.utils.Structure;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Eren on 16.12.2014.
 */
public class MyAdapter extends BaseAdapter {

    Context ctx;
    LayoutInflater lInflater;
    ArrayList<Structure> objects;

    public MyAdapter(Context context, ArrayList<Structure> structures) {
        ctx = context;
        objects = structures;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return objects.size();
    }


    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = lInflater.inflate(R.layout.item, parent, false);
            holder = new ViewHolder();
            holder.date = (TextView) convertView.findViewById(R.id.date);
            holder.tvDescription = (TextView) convertView.findViewById(R.id.tvDescription);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        Structure s = getStructure(position);
        holder.tvDescription.setText(s.getTitle());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(s.getUploaded());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.date.setText(date.toString());
        return convertView;
    }
    static class ViewHolder {
        TextView tvDescription;
        TextView date;
    }
    Structure getStructure(int position) {
        return ((Structure) getItem(position));
    }

}