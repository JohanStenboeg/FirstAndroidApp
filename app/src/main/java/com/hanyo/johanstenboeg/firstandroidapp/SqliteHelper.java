package com.hanyo.johanstenboeg.firstandroidapp;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqliteHelper extends SQLiteOpenHelper {
    //Database Name
    public static final String database_Name = "FeelsbadmanDB";

    //Database version
    public static final int database_Version = 1;

    //Table Name
    public static final String table_Users = "Users";

    //Table users columns
    //Id Column (primaryKey)
    public static final String key_Id = "Id";

    //Column user name
    public static final String key_User_Name = "Username";

    //Column email
    public static final String key_Email = "Email";

    //Column password
    public static final String key_Password = "Password";

    //SQL for creating users table
    public static final String sql_Table_users = " CREATE TABLE " + table_Users
            + " ( "
            + key_Id + " INTEGER PRIMARY KEY, "
            + key_User_Name + " TEXT, "
            + key_Email + " TEXT, "
            + key_Password + " TEXT"
            + " ) ";


    public SqliteHelper(Context context) {
        super(context, database_Name, null, database_Version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //Create table when onCreate gets called
        sqLiteDatabase.execSQL(sql_Table_users);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //drop table to create new one if database version updated
        sqLiteDatabase.execSQL(" DROP TABLE IF EXISTS " + table_Users);
    }

    //Using this method we can add users to user table
    public void addUser(User user) {
        //get writeable database
        SQLiteDatabase db = this.getWritableDatabase();

        //Create content values to insert
        ContentValues values = new ContentValues();

        //Put username in @values
        values.put(key_User_Name, user.userName);

        //put email in @values
        values.put(key_Email, user.email);

        //Put password in @values
        values.put(key_Password, user.password);

        //insert row
        long todo_id = db.insert(table_Users, null, values);
    }

    public User Authenticator(User user) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor curser = db.query(table_Users,//Selecting table
                new String[]{key_Id, key_User_Name, key_Email, key_Password},//Selecting colums want to query
                key_Email + "=?",
                new String[]{user.email},//where clause
                null, null, null);
        if (curser != null && curser.moveToFirst() && curser.getCount() > 0) {
            //If curser has value then in user database there is user associated with this given email
            User user1 = new User(curser.getString(0), curser.getString(1), curser.getString(2), curser.getString(3));

            //Match both passwords check thye are same or not
            if (user.password.equalsIgnoreCase(user1.password)) {
                return user1;
            }
        }
        //if user password does not match or there is no record with that email then return @false.
        return null;

    }

    public boolean isEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(table_Users, //Selecting table
                new String[]{key_Id, key_User_Name, key_Email, key_Password}, //selecting columns want to query
                key_Email + "=?",
                new String[]{email}, //where clause
                null, null, null);
        if (cursor != null && cursor.moveToFirst() && cursor.getCount() > 0) {
            //If curser has value then in user database there is user associated with that email
            return true;
        }
        //if Email does not exist return false
        return false;
    }
}

