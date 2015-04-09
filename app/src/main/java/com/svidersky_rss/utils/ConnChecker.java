package com.svidersky_rss.utils;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by Eren on 09.04.2015.
 */
public class ConnChecker {
    public static boolean isOnline(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            return cm.getActiveNetworkInfo().isConnectedOrConnecting();
        } catch (Exception e) {
            return false;
        }
    }
}