package com.example.movieapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.movieapp.MainActivity;
import com.example.movieapp.Models.Movie;
import com.example.movieapp.Models.User;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper instance;
    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "MovieApp.db";
    public static final String TABLE_USER = "Users";
    public static final String COLUMN_USER_ID = "id";
    public static final String COLUMN_USER_NAME = "name";
    public static final String COLUMN_USER_EMAIL = "email";
    public static final String COLUMN_USER_PASSWORD = "passwrod";
    public static final String COLUMN_USER_PROFILE_PICTURE = "profile_picture";
    public static final String CREATE_TABLE_USER = "CREATE TABLE " + TABLE_USER + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_USER_NAME + " TEXT, "
            + COLUMN_USER_EMAIL + " TEXT, "
            +COLUMN_USER_PASSWORD + " TEXT, "
            + COLUMN_USER_PROFILE_PICTURE + " BLOB"
            + ")";
    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;


    public static final String TABLE_MOVIE = "movie";
    public static final String COLUMN_MOVIE_ID = "movie_id";
    public static final String COLUMN_MOVIE_TITLE = "title";
    public static final String COLUMN_MOVIE_OVERVIEW = "overview";
    public static final String COLUMN_MOVIE_POSTER_PATH = "poster_path";
    public static final String COLUMN_MOVIE_USER_ID = "user_id";
    public static final String CREATE_MOVIE_TABLE =
            "CREATE TABLE " + TABLE_MOVIE + " ("
                    + COLUMN_MOVIE_ID + " INTEGER PRIMARY KEY, "
                    + COLUMN_MOVIE_TITLE + " TEXT, "
                    + COLUMN_MOVIE_OVERVIEW + " TEXT, "
                    + COLUMN_MOVIE_POSTER_PATH + " TEXT, "
                    + COLUMN_MOVIE_USER_ID + " INTEGER"
                    + ")";
    private String DROP_MOVIE_TABLE = "DROP TABLE IF EXISTS " + TABLE_MOVIE;

    private DatabaseHelper(Context context){
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DatabaseHelper getInstance(Context context){
        if(instance == null){
            instance = new DatabaseHelper(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_USER_TABLE);
        db.execSQL(DROP_MOVIE_TABLE);
        onCreate(db);
    }

    public void addUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getUserName());
        values.put(COLUMN_USER_EMAIL, user.getUserEmail());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());
        values.put(COLUMN_USER_PROFILE_PICTURE, user.getProfilePicture());

        db.insert(TABLE_USER,null,values);
        db.close();
    }

    public List<User> getAllUser(){
        String[] columns = {
                COLUMN_USER_ID,
                COLUMN_USER_NAME,
                COLUMN_USER_EMAIL,
                COLUMN_USER_PASSWORD,
                COLUMN_USER_PROFILE_PICTURE
        };
        String sortOrder = COLUMN_USER_NAME + " ASC";
        List<User> userList = new ArrayList<User>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns, //columns to return
                null, //columns for the Where cluse
                null, //The values for the WHERE clause
                null, //group the rows
                null,  //filter by row groups
                sortOrder);  //The sort order
        if(cursor.moveToFirst()){
            do{
                User user = new User();
                user.setUserId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID))));
                user.setUserName(cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)));
                user.setUserEmail(cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PASSWORD)));
                user.setProfilePicture(cursor.getBlob(cursor.getColumnIndex(COLUMN_USER_PROFILE_PICTURE)));
                userList.add(user);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return userList;
    }


    public void updateUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getUserName());
        values.put(COLUMN_USER_EMAIL, user.getUserEmail());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());
        values.put(COLUMN_USER_PROFILE_PICTURE, user.getProfilePicture());
        //update the row
        db.update(TABLE_USER, values,COLUMN_USER_ID + " =?",
                new String[]{String.valueOf(user.getUserId())});
        db.close();
    }

    public void deleteUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USER,COLUMN_USER_ID + " =?",
                new String[]{String.valueOf(user.getUserId())});
        db.close();
    }

    public boolean checkUser(String email){
        String[] columns = {COLUMN_USER_ID};
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_USER_EMAIL + " =?";
        String[] selectionArgs = {email};
        Cursor cursor = db.query(TABLE_USER,//Table to query
                columns,                     //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,               //group the rows
                null,                //filter by row groups
                null);              //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        if (cursorCount > 0 ){
            return true;
        }
        return false;
    }

    public boolean checkUser(String email, String password) {

        String[] columns = {COLUMN_USER_ID};
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_USER_EMAIL + " = ?" + " AND " + COLUMN_USER_PASSWORD + " = ?";
        String[] selectionArgs = {email, password};
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }
        return false;
    }

    public User getUserByEmail(String email){
        User user = new User();
        String[] columns = {
                COLUMN_USER_ID,
                COLUMN_USER_NAME,
                COLUMN_USER_EMAIL,
                COLUMN_USER_PASSWORD,
                COLUMN_USER_PROFILE_PICTURE
        };
        String selection = COLUMN_USER_EMAIL + " = ?";
        String[] selectionArgs = {email};
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns, //columns to return
                selection, //columns for the Where cluse
                selectionArgs, //The values for the WHERE clause
                null, //group the rows
                null,  //filter by row groups
                null);  //The sort order
        if(cursor.moveToFirst()){
                user.setUserId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID))));
                user.setUserName(cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)));
                user.setUserEmail(cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PASSWORD)));
                user.setProfilePicture(cursor.getBlob(cursor.getColumnIndex(COLUMN_USER_PROFILE_PICTURE)));
        }
        cursor.close();
        db.close();
        return user;
    }

    public void addMovie(Movie movie){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MOVIE_ID, movie.getId());
        values.put(COLUMN_MOVIE_TITLE, movie.getTitle());
        values.put(COLUMN_MOVIE_OVERVIEW, movie.getOverview());
        values.put(COLUMN_MOVIE_POSTER_PATH, movie.getPosterPath());
        values.put(COLUMN_MOVIE_USER_ID, MainActivity.currentUserID);

        db.insert(TABLE_MOVIE, null, values);
        db.close();
    }

    public List<Movie> getMovies(){
        List<Movie> movies = new ArrayList<>();

        String[] projection = new String[]{COLUMN_MOVIE_ID, COLUMN_MOVIE_TITLE, COLUMN_MOVIE_OVERVIEW, COLUMN_MOVIE_POSTER_PATH};
        String[] selectionArgs = new String[]{String.valueOf(MainActivity.currentUserID)};

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_MOVIE,
                projection, COLUMN_MOVIE_USER_ID + " =? ", selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {

            while(cursor.moveToNext()){
                Movie movie = new Movie(cursor.getInt(cursor.getColumnIndex(COLUMN_MOVIE_ID)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_MOVIE_TITLE)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_MOVIE_OVERVIEW)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_MOVIE_POSTER_PATH)));

                movies.add(movie);
            }

            cursor.close();
            db.close();
            return movies;
        }

        cursor.close();
        db.close();
        return null;
    }

    public void deleteMovie(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = COLUMN_MOVIE_ID + " =? and " + COLUMN_MOVIE_USER_ID + " =?";
        String[] selectionArgs = new String[]{Integer.toString(id), Integer.toString(MainActivity.currentUserID)};
        db.delete(TABLE_MOVIE, whereClause, selectionArgs);
        db.close();
    }
}
