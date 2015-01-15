package com.svidersky_rss.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.svidersky_rss.utils.DB;

/**
 * Created by Eren on 17.12.2014.
 */
public class ContentFragment extends BaseFragment {


    public static ContentFragment newInstance(int index) {
        ContentFragment f = new ContentFragment();

        Bundle args = new Bundle();
        args.putInt("index", index);

        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public int getShownIndex() {
        return getArguments().getInt("index", -1);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_fragment, container, false);
        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView titleTextView = (TextView) view.findViewById(R.id.titleTextView);
        titleTextView.setText(getTitle(getShownIndex()));
        TextView infoTextView = (TextView) view.findViewById(R.id.infoTextView);
        infoTextView.setText(Html.fromHtml(getDescription(getShownIndex())) +
                "\n Cсылка на видео: " + getVideo(getShownIndex()) + "\n\n");
        final DB db = new DB(getActivity());
        final Button button = (Button) view.findViewById(R.id.button);
        final int id = db.check(getTitle(getShownIndex()));
        if (id > 0) {
            button.setText("Delete news");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    db.removeNews(id);
                    Toast.makeText(getActivity(), "Remove news", Toast.LENGTH_SHORT).show();
                    button.setVisibility(View.GONE);
                }
            });
        } else {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    db.addNews(getTitle(getShownIndex()), getPicture(getShownIndex()), getVideo(getShownIndex()),
                            getUploaded(getShownIndex()), getDescription(getShownIndex()));
                    Toast.makeText(getActivity(), "Add news", Toast.LENGTH_SHORT).show();
                    button.setVisibility(View.GONE);
                }
            });
        }
    }
}
