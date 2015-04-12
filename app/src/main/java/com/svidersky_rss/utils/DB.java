package com.svidersky_rss.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Eren on 14.01.2015.
 */
public class DB extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "news";
    public static final String TABLE_NAME = "video";

    public static final class COLUMNS {
        public static final String ID = "_id";
        public static final String TITLE = "title";
        public static final String PICTURE = "picture";
        public static final String VIDEO = "video";
        public static final String UPLOADED = "uploaded";
        public static final String DESCRIPTION = "description";

    }

    private static final String CREATE_TABLE = "create table "
            + TABLE_NAME + " ( "
            + COLUMNS.ID + " integer primary key autoincrement, "
            + COLUMNS.TITLE + " TEXT, "
            + COLUMNS.PICTURE+ " TEXT, "
            + COLUMNS.VIDEO + " TEXT, "
            + COLUMNS.UPLOADED + " TEXT, "
            + COLUMNS.DESCRIPTION + " TEXT)";

    public DB(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public boolean addNews(String title, String picture, String video, String uploaded, String description) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(COLUMNS.TITLE, title);
            cv.put(COLUMNS.PICTURE, picture);
            cv.put(COLUMNS.VIDEO, video);
            cv.put(COLUMNS.UPLOADED, uploaded);
            cv.put(COLUMNS.DESCRIPTION, description);
            db.insert(DB.TABLE_NAME, null, cv);
            db.close();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public ArrayList<Structure> getNews() {
        ArrayList<Structure> arrayList = new ArrayList<>();
        Structure structure ;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMNS.ID, COLUMNS.TITLE, COLUMNS.PICTURE,
                        COLUMNS.VIDEO, COLUMNS.UPLOADED, COLUMNS.DESCRIPTION}, null, null, null, null,
                null);
        if (cursor.moveToFirst()) {
            do {
                structure = new Structure(cursor.getString(1), cursor.getString(2), cursor.getString(3),
                        cursor.getString(4), cursor.getString(5));
                arrayList.add(structure);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return arrayList;
    }

    public void removeNews(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, COLUMNS.ID + "=" + id, null);
        db.close();
    }

    public int check(String title) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMNS.ID, COLUMNS.TITLE}, null, null, null, null,
                COLUMNS.ID);
        if (cursor.moveToFirst()) {
            do {
                String temp = (cursor.getString(1));
                if (title.equals(temp)) {
                    return cursor.getInt(0);
                }
            } while (cursor.moveToNext());
        }
        return -1;
    }
}
