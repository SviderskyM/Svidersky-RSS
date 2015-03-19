package com.svidersky_rss.fragments;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.text.Html;
import android.view.LayoutInflater;

import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.widget.FacebookDialog;
import com.google.android.gms.plus.PlusShare;
import com.svidersky_rss.Constants;
import com.svidersky_rss.R;
import com.svidersky_rss.utils.DB;

import java.util.ArrayList;

/**
 * Created by Eren on 17.12.2014.
 */
public class ContentFragment extends BaseFragment {

    private DB db;
    private int id;

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

    public int getIndex() {
        return getArguments().getInt("index", 0);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
        db = new DB(getActivity());
        final TextView titleTextView = (TextView) view.findViewById(R.id.titleTextView);
        final TextView infoTextView = (TextView) view.findViewById(R.id.infoTextView);
        final Button button = (Button) view.findViewById(R.id.button);
        if (Constants.flag) {
            titleTextView.setText(getTitleF(getIndex()));
            infoTextView.setText(Html.fromHtml(getDescriptionF(getIndex()) +
                    "\n Cсылка на видео: " + getVideoF(getIndex()) + "\n\n"));
            button.setText("Delete with favorite");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    db.removeNews(db.check(getTitleF(getIndex())));
                    Toast.makeText(getActivity(), "Remove news", Toast.LENGTH_SHORT).show();
                    button.setVisibility(View.GONE);
                }
            });
        } else {
            titleTextView.setText(getTitle(getIndex()));
            infoTextView.setText(Html.fromHtml(getDescription(getIndex())) +
                    "\n Cсылка на видео: " + getVideo(getIndex()) + "\n\n");
            if (db.check(getTitle(getIndex())) < 0) {
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        db.addNews(getTitle(getIndex()), getPicture(getIndex()), getVideo(getIndex()),
                                getUploaded(getIndex()), getDescription(getIndex()));
                        Toast.makeText(getActivity(), "Add news", Toast.LENGTH_SHORT).show();
                        button.setVisibility(View.GONE);
                    }
                });
            } else {
                button.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final String[] items = {"facebook", "google+"};
        if (!Constants.flag) {
            switch (item.getItemId()) {
                case R.id.share:
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(R.string.share);
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {
                            // Do something with the selection
                            switch (item) {
                                case 0:
                                    shareFacebook();
                                    break;
                                case 1:
                                    shareGooglePlus();
                                    break;
                            }
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();

                default:
                    return super.onOptionsItemSelected(item);
            }
        }
        else return super.onOptionsItemSelected(item);
    }

    private void shareFacebook() {
        if (FacebookDialog.canPresentShareDialog(getActivity().getApplicationContext(),
                FacebookDialog.ShareDialogFeature.SHARE_DIALOG)) {
            // Publish the post using the Share Dialog
            FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(getActivity())
                    .setPicture(getPicture(getIndex()))
                    .setLink(getVideo(getIndex()))
                    .build();

            uiHelper.trackPendingDialogCall(shareDialog.present());
        } else {
            // Fallback. For example, publish the post using the Feed Dialog
            String sharerUrl = "https://www.facebook.com/sharer/sharer.php?u=" + getVideo(getIndex());
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
            startActivity(intent);
        }
    }

    private void shareGooglePlus() {
        try {
            Intent shareIntent = new PlusShare.Builder(getActivity())
                    .setType("text/plain")
                    .setText(getTitle(getIndex()))
                    .setContentUrl(Uri.parse(getVideo(getIndex())))
                    .getIntent();

            startActivityForResult(shareIntent, 0);
        } catch (ActivityNotFoundException e) {
            String sharerUrl = "https://plus.google.com/share?url=" + getVideo(getIndex());
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
            startActivity(intent);
        }
    }
}