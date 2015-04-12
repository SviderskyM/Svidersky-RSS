package com.svidersky_rss.fragments;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.view.LayoutInflater;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.widget.FacebookDialog;
import com.google.android.gms.plus.PlusShare;
import com.squareup.picasso.Picasso;
import com.svidersky_rss.Constants;
import com.svidersky_rss.R;
import com.svidersky_rss.utils.DB;

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
        final ImageView imageView = (ImageView) view.findViewById(R.id.picture);
        if (Constants.flag) {
            Constants.TwoFalg = true;
            titleTextView.setText(getTitleDB(getIndex()));
            Picasso.with(getActivity()).load(getPictureDB(getIndex()))
                    .resize(700, 600)
                    .placeholder(R.drawable.ic_launcher)
                    .error(R.drawable.ic_launcher)
                    .into(imageView);
            infoTextView.setText(getDescriptionDB(getIndex())
                    + "\n\n"
                    + "Cсылка на видео: "
                    + getVideoDB(getIndex()));
        } else {
            Constants.TwoFalg = false;
            titleTextView.setText(getTitle(getIndex()));
            Picasso.with(getActivity()).load(getPicture(getIndex()))
                    .resize(700, 600)
                    .placeholder(R.drawable.ic_launcher)
                    .error(R.drawable.ic_launcher)
                    .into(imageView);
            infoTextView.setText(getDescription(getIndex())
                    + "\n\n"
                    + "Cсылка на видео: "
                    + getVideo(getIndex()));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final String[] items = {"facebook", "google+"};
        switch (item.getItemId()) {
            case R.id.share:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.share);
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
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
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void shareFacebook() {
        if (FacebookDialog.canPresentShareDialog(getActivity().getApplicationContext(),
                FacebookDialog.ShareDialogFeature.SHARE_DIALOG)) {
            FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(getActivity())
                    .setPicture(getPicture(getIndex()))
                    .setLink(getVideo(getIndex()))
                    .build();

            uiHelper.trackPendingDialogCall(shareDialog.present());
        } else {
            String sharerUrl = "https://www.facebook.com/sharer/sharer.php?u=" + getVideo(getIndex());
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
            startActivity(intent);
        }
    }

    public void shareGooglePlus() {
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

    public boolean checkDB(DB db) {

        if (Constants.TwoFalg){
            if (db.check(getTitleDB(getIndex())) > 0)
                return true;
            else
                return false;
        }else {
            if (db.check(getTitle(getIndex())) > 0)
                return true;
            else
                return false;
        }
    }

    public void deleteNews(DB db) {
        if (Constants.TwoFalg) {
            db.removeNews(db.check(getTitleDB(getIndex())));
        } else {
            db.removeNews(db.check(getTitle(getIndex())));
        }

    }

    public void addNews(DB db) {
        if (Constants.TwoFalg) {
            db.addNews(getTitleDB(getIndex()), getPictureDB(getIndex()), getVideoDB(getIndex()),
                    getUploadedDB(getIndex()), getDescriptionDB(getIndex()));
        } else {
            db.addNews(getTitle(getIndex()), getPicture(getIndex()), getVideo(getIndex()),
                    getUploaded(getIndex()), getDescription(getIndex()));
        }
    }
}