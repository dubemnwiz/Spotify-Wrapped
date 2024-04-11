package com.example.spotifysdk;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SpotifyWrappedDbHelper extends SQLiteOpenHelper {
    // Database Info
    private static final String DATABASE_NAME = "spotifyWrapped.db";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_ARTISTS = "artists";
    private static final String TABLE_SONGS = "songs";
    private static final String TABLE_GENRES = "genres";
    private static final String TABLE_ALBUMS = "albums";

    // Common Columns
    private static final String NAME = "name";
    private static final String IMAGE_URL = "image_url";

    public SpotifyWrappedDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Artists Table
        String CREATE_ARTISTS_TABLE = "CREATE TABLE " + TABLE_ARTISTS +
                "(" +
                NAME + " TEXT," +
                IMAGE_URL + " TEXT" +
                ")";
        db.execSQL(CREATE_ARTISTS_TABLE);

        // Create Songs Table
        String CREATE_SONGS_TABLE = "CREATE TABLE " + TABLE_SONGS +
                "(" +
                NAME + " TEXT," +
                IMAGE_URL + " TEXT" +
                ")";
        db.execSQL(CREATE_SONGS_TABLE);

        // Create Genres Table
        String CREATE_GENRES_TABLE = "CREATE TABLE " + TABLE_GENRES +
                "(" +
                NAME + " TEXT," +
                IMAGE_URL + " TEXT" +
                ")";
        db.execSQL(CREATE_GENRES_TABLE);

        // Create Albums Table
        String CREATE_ALBUMS_TABLE = "CREATE TABLE " + TABLE_ALBUMS +
                "(" +
                NAME + " TEXT," +
                IMAGE_URL + " TEXT" +
                ")";
        db.execSQL(CREATE_ALBUMS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ARTISTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SONGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GENRES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALBUMS);
        onCreate(db);
    }

    public boolean insertArtist(String name, String imageUrl) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, name);
        contentValues.put(IMAGE_URL, imageUrl);
        long result = db.insert(TABLE_ARTISTS, null, contentValues);
        return result != -1;
    }

    public boolean insertSong(String name, String imageUrl) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, name);
        contentValues.put(IMAGE_URL, imageUrl);
        long result = db.insert(TABLE_SONGS, null, contentValues);
        return result != -1;
    }

    public boolean insertGenre(String name, String imageUrl) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, name);
        contentValues.put(IMAGE_URL, imageUrl);
        long result = db.insert(TABLE_GENRES, null, contentValues);
        return result != -1;
    }

    public boolean insertAlbum(String name, String imageUrl) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, name);
        contentValues.put(IMAGE_URL, imageUrl);
        long result = db.insert(TABLE_ALBUMS, null, contentValues);
        return result != -1;
    }

    public Cursor getAllArtists() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_ARTISTS, null);
    }

    public Cursor getAllSongs() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_SONGS, null);
    }

    public Cursor getAllGenres() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_GENRES, null);
    }

    public Cursor getAllAlbums() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_ALBUMS, null);
    }
}