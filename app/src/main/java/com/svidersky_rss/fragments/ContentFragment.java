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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.widget.FacebookDialog;
import com.google.android.gms.plus.PlusShare;
import com.squareup.picasso.Picasso;
import com.svidersky_rss.Constants;
import com.svidersky_rss.R;
import com.svidersky_rss.utils.DB;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eren on 17.12.2014.
 */
public class ContentFragment extends BaseFragment {

    private DB db;
    private int id;
    private ArrayList<String> parserList = new ArrayList();

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
        final ImageView imageView = (ImageView) view.findViewById(R.id.picture);
        if (Constants.flag) {
            titleTextView.setText(getTitleF(getIndex()));
            Picasso.with(getActivity()).load(getPictureF(getIndex()))
                    .resize(700, 600)
                    .placeholder(R.drawable.ic_launcher)
                    .error(R.drawable.ic_launcher)
                    .into(imageView);
            parserList = parser(getDescriptionF(getIndex()));
            if (parserList.size() != 0) {
                infoTextView.setText(parserList.get(0) +"\n"
                        + parserList.get(1) + "\n"
                        + parserList.get(2) + "\n"
                        + parserList.get(3) + "\n"
                        + parserList.get(4) + "\n"
                        + parserList.get(5) + "\n"
                        + parserList.get(6) + "\n"
                        + parserList.get(7)
                        + "\nCсылка на видео: " + getVideoF(getIndex()) + "\n\n");
            } else {
                infoTextView.setText(Html.fromHtml(getDescriptionF(getIndex())) +
                        "\nCсылка на видео: " + getVideoF(getIndex()) + "\n\n");
            }
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
            Picasso.with(getActivity()).load(getPicture(getIndex()))
                    .resize(700, 600)
                    .placeholder(R.drawable.ic_launcher)
                    .error(R.drawable.ic_launcher)
                    .into(imageView);
            parserList = parser(getDescription(getIndex()));
            if (parserList.size() != 0) {
                infoTextView.setText(parserList.get(0) +"\n"
                        + parserList.get(1) + "\n"
                        + parserList.get(2) + "\n"
                        + parserList.get(3) + "\n"
                        + parserList.get(4) + "\n"
                        + parserList.get(5) + "\n"
                        + parserList.get(6) + "\n"
                        + parserList.get(7)
                        + "\nCсылка на видео: " + getVideo(getIndex()) + "\n\n");
            } else {
                infoTextView.setText(Html.fromHtml(getDescription(getIndex())) +
                        "\nCсылка на видео: " + getVideo(getIndex()) + "\n\n");
            }
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
        } else return super.onOptionsItemSelected(item);
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

    public ArrayList<String> parser(String description) {
        //int start = description.indexOf("Канал");
        int vk = description.indexOf("Группа");
        int tw = description.indexOf("Твиттер");
        int fb = description.indexOf("Фейсбук");
        int in = description.indexOf("Инстаграм");
        int subchannel = description.indexOf("Второй канал");
        int rekl = description.indexOf("Заказ");
        int end = description.indexOf("25144");
        if (vk != -1 && tw != -1 && fb != -1 && in != -1 && subchannel != -1
                && rekl != -1 && end != -1) {
            String start = description.substring(0, vk - 2);
            String vkontakte = description.substring(vk, tw - 1);
            String twitter = description.substring(tw, fb - 1);
            String facebok = description.substring(fb, in - 1);
            String instagram = description.substring(in, subchannel - 1);
            String subch = description.substring(subchannel, rekl - 1);
            String reklama = description.substring(rekl, end + 5);
            String ends = description.substring(end + 6);
            parserList.add(start);
            parserList.add(vkontakte);
            parserList.add(twitter);
            parserList.add(facebok);
            parserList.add(instagram);
            parserList.add(subch);
            parserList.add(reklama);
            parserList.add(ends);
        }
        return parserList;
    }
}