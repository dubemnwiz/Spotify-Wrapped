package com.example.spotifysdk;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Objects;

public class SpotifyWrappedDbHelper extends SQLiteOpenHelper {
    // Database Info
    private static final String DATABASE_NAME = "spotifyWrapped.db";
    private static final int DATABASE_VERSION = 18;

    // Table Names
    private static final String TABLE_ARTISTS = "artists";
    private static final String TABLE_SONGS = "songs";
    private static final String TABLE_RECOM = "recom";
    private static final String TABLE_GENRES = "genres";
    private static final String TABLE_ALBUMS = "albums";

    // Common Columns
    private static final String NAME = "name";
    private static final String USERNAME = "username";
    private static final String IMAGE_URL = "image_url";
    private static final String COLUMN_ITEM_NAME = "item_name"; // Change this to match your item's data structure
    // Table names
    private static final String TABLE_VIEWMODEL_ITEMS = "viewmodel_items";

    // Common column names
    private static final String COLUMN_ID = "id";

    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_VIEWMODEL_ID = "viewmodel_id";
    private static final String SONG_LINK = "songlink";


    public SpotifyWrappedDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create viewmodel items table
        String CREATE_VIEWMODEL_ITEMS_TABLE = "CREATE TABLE " + TABLE_VIEWMODEL_ITEMS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_USERNAME + " TEXT," +
                COLUMN_ITEM_NAME + " TEXT," +
                COLUMN_DATE + " TEXT" + // Add a column for storing the date
                ")";
        db.execSQL(CREATE_VIEWMODEL_ITEMS_TABLE);

        // Create Artists Table
        String CREATE_ARTISTS_TABLE = "CREATE TABLE " + TABLE_ARTISTS +
                "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_VIEWMODEL_ID + " INTEGER," +
                NAME + " TEXT," +
                IMAGE_URL + " TEXT," +
                USERNAME + " TEXT" + // Add username column
                ")";
        db.execSQL(CREATE_ARTISTS_TABLE);

        // Create Songs Table
        String CREATE_SONGS_TABLE = "CREATE TABLE " + TABLE_SONGS +
                "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_VIEWMODEL_ID + " INTEGER," +
                SONG_LINK + " TEXT," +
                NAME + " TEXT," +
                IMAGE_URL + " TEXT," +
                USERNAME + " TEXT" + // Add username column
                ")";
        db.execSQL(CREATE_SONGS_TABLE);

        // Create Genres Table
        String CREATE_GENRES_TABLE = "CREATE TABLE " + TABLE_GENRES +
                "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY," +
                COLUMN_VIEWMODEL_ID + " INTEGER," +
                NAME + " TEXT," +
                IMAGE_URL + " TEXT," +
                USERNAME + " TEXT" + // Add username column
                ")";
        db.execSQL(CREATE_GENRES_TABLE);

        // Create Albums Table
        String CREATE_ALBUMS_TABLE = "CREATE TABLE " + TABLE_ALBUMS +
                "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                NAME + " TEXT," +
                IMAGE_URL + " TEXT," +
                USERNAME + " TEXT" + // Add username column
                ")";
        db.execSQL(CREATE_ALBUMS_TABLE);

        // Create Recom Table
        String CREATE_RECOM_TABLE = "CREATE TABLE " + TABLE_RECOM +
                "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_VIEWMODEL_ID + " INTEGER," +
                NAME + " TEXT," +
                IMAGE_URL + " TEXT," +
                USERNAME + " TEXT" + // Add username column
                ")";
        db.execSQL(CREATE_RECOM_TABLE);

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ARTISTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SONGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GENRES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALBUMS);

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VIEWMODEL_ITEMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECOM);
        onCreate(db);
    }

    public boolean insertArtist(String name, String imageUrl, long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, name);
        contentValues.put(IMAGE_URL, imageUrl);
        contentValues.put(COLUMN_VIEWMODEL_ID, id);
        long result = db.insert(TABLE_ARTISTS, null, contentValues);
        return result != -1;
    }

    public boolean insertSong(String name, String imageUrl, long id, String link) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        //contentValues.put(COLUMN_VIEWMODEL_ID); // Set the ViewModel ID
        contentValues.put(NAME, name);
        contentValues.put(IMAGE_URL, imageUrl);
        contentValues.put(COLUMN_VIEWMODEL_ID, id);
        contentValues.put(SONG_LINK, link);
        long result = db.insert(TABLE_SONGS, null, contentValues);
        return result != -1;
    }

    public boolean insertGenre(String name, String imageUrl, long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, name);
        contentValues.put(IMAGE_URL, imageUrl);
        contentValues.put(COLUMN_VIEWMODEL_ID, id);
        long result = db.insert(TABLE_GENRES, null, contentValues);
        return result != -1;
    }

    public boolean insertAlbum(String name, String imageUrl, long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, name);
        contentValues.put(IMAGE_URL, imageUrl);
        contentValues.put(COLUMN_ID, id);
        long result = db.insert(TABLE_ALBUMS, null, contentValues);

        return result != -1;
    }

    public boolean insertRecom(String name, String imageUrl, long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        //contentValues.put(COLUMN_VIEWMODEL_ID); // Set the ViewModel ID
        contentValues.put(NAME, name);
        contentValues.put(IMAGE_URL, imageUrl);
        contentValues.put(COLUMN_VIEWMODEL_ID, id);
        long result = db.insert(TABLE_RECOM, null, contentValues);
        return result != -1;
    }

    public Cursor getAllArtists(int artistsId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_ARTISTS + " WHERE " + COLUMN_VIEWMODEL_ID + " = ? LIMIT 6",
                new String[]{String.valueOf(artistsId)});
    }

    public Cursor getAllSongs(int songsId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_SONGS + " WHERE " + COLUMN_VIEWMODEL_ID + " = ? LIMIT 5",
                new String[]{String.valueOf(songsId)});
    }

    public Cursor getAllRecom(int songsId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_RECOM + " WHERE " + COLUMN_VIEWMODEL_ID + " = ? LIMIT 5",
                new String[]{String.valueOf(songsId)});
    }

    public Cursor getAllGenres(int genresId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_GENRES + " WHERE " + COLUMN_VIEWMODEL_ID + " = ? LIMIT 5",
                new String[]{String.valueOf(genresId)});
    }

    public Cursor getAllAlbums(int albumId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_ALBUMS + " WHERE " + COLUMN_ID + " = ?",
                new String[]{String.valueOf(albumId)});
    }

    // Insert ViewModel item associated with a user
    public long insertViewModelItem(String username, String id, String date) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_ITEM_NAME, id);
        values.put(COLUMN_DATE, date);

        long result = db.insert(TABLE_VIEWMODEL_ITEMS, null, values);
        Log.d("tag99", "insertViewModelItem: " + result);
        if (result == -1) {
            Log.e("tag99", "Failed to insert ViewModel item into database.");
            return result;
        } else {
            Log.d("tag99", "ViewModel item inserted into database successfully.");
            return result;
        }
    }


    // Get ViewModel items associated with a user
    public Cursor getViewModelItems(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_VIEWMODEL_ITEMS, null, COLUMN_USERNAME + " = ?", new String[]{username}, null, null, null);
    }
    public void deleteAllSavedWraps(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_VIEWMODEL_ITEMS, COLUMN_USERNAME + " = ?", new String[]{username});
    }
    // Static method to get the size of a table
    public int getTableSize(SQLiteDatabase db, String tableName) {
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + tableName, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int count = cursor.getInt(0);
            cursor.close();
            return count;
        } else {
            return 0;
        }
    }

    // Static method to get the size of all tables
    public String getAllTableSizes(SpotifyWrappedDbHelper dbHelper) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        StringBuilder sizes = new StringBuilder();

        sizes.append("Table: ").append(TABLE_ARTISTS).append(", Size: ").append(getTableSize(db, TABLE_ARTISTS)).append("\n");
        sizes.append("Table: ").append(TABLE_SONGS).append(", Size: ").append(getTableSize(db, TABLE_SONGS)).append("\n");
        sizes.append("Table: ").append(TABLE_GENRES).append(", Size: ").append(getTableSize(db, TABLE_GENRES)).append("\n");
        sizes.append("Table: ").append(TABLE_ALBUMS).append(", Size: ").append(getTableSize(db, TABLE_ALBUMS)).append("\n");

        return sizes.toString();
    }

    public static void logTableContents(SQLiteDatabase db, String tableName) {
        Cursor cursor = db.rawQuery("SELECT * FROM " + tableName, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                StringBuilder rowValues = new StringBuilder();
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    rowValues.append(cursor.getColumnName(i)).append(": ").append(cursor.getString(i)).append(", ");
                }
                if (Objects.equals(tableName, "artists")) {
                    Log.d("TableContents1", rowValues.toString());
                } else {
                    Log.d("TableContents", rowValues.toString());
                }
            } while (cursor.moveToNext());
            cursor.close();
        }
    }


}