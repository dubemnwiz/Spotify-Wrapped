package com.example.spotifysdk;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class SpotifyWrappedDbHelper extends SQLiteOpenHelper {
    // Database Info
    private static final String DATABASE_NAME = "spotifyWrapped.db";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_SPOTIFY_WRAPPED = "spotifyWrappedEntries";

    // SpotifyWrappedEntries Table Columns
    private static final String ID = "id";
    private static final String ARTIST_NAME = "artistName";
    private static final String SONG_NAME = "songName";
    private static final String IMAGE_URL = "imageUrl";

    public SpotifyWrappedDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_SPOTIFY_WRAPPED_ENTRIES_TABLE = "CREATE TABLE " + TABLE_SPOTIFY_WRAPPED +
                "(" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ARTIST_NAME + " TEXT," +
                SONG_NAME + " TEXT," +
                IMAGE_URL + " TEXT" +
                ")";

        db.execSQL(CREATE_SPOTIFY_WRAPPED_ENTRIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SPOTIFY_WRAPPED);
        onCreate(db);
    }

    public boolean insertSpotifyWrappedEntry(String artistName, String songName, String imageUrl) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ARTIST_NAME, artistName);
        contentValues.put(SONG_NAME, songName);
        contentValues.put(IMAGE_URL, imageUrl);
        long result = db.insert(TABLE_SPOTIFY_WRAPPED, null, contentValues);
        return result != -1;
    }

    public Cursor getAllSpotifyWrappedEntries() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_SPOTIFY_WRAPPED, null);
    }
}
