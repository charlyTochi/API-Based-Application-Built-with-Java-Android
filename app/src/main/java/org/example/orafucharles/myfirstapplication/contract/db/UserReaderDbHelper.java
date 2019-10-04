package org.example.orafucharles.myfirstapplication.contract.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.example.orafucharles.myfirstapplication.UserObject;

import static android.content.Context.MODE_PRIVATE;
import static org.example.orafucharles.myfirstapplication.contract.db.UserContract.UserEntry.TABLE_NAME;


public class UserReaderDbHelper extends SQLiteOpenHelper {


    SharedPreferences preferences;

    public static final int DATABASE_VERSION = 1;
     public static final String DATABASE_NAME = "aly.db";
    private SQLiteOpenHelper usersDBHelper;
    private SQLiteDatabase mDatabase;


    public UserReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {

        // Creates table for Menu item:- name,  with their corresponding id for relationships
        String CREATE_TABLE = "create table "+ TABLE_NAME +
                " ( " + UserContract.UserEntry.ALY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                UserContract.UserEntry.ALY_USERNAME+ " TEXT NOT NULL, "
                + UserContract.UserEntry.ALY_PASSWORD + " TEXT NOT NULL, " +
                UserContract.UserEntry.ALY_IMAGE + " TEXT );";
        db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(sqLiteDatabase);
    }


    public UserObject addUser(String username, String password, SQLiteDatabase database){
        //check if anybody is registered with this username
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE "
                + UserContract.UserEntry.ALY_USERNAME+ "=?", new String[]{username});
        //if no user exist
        if(cursor.getCount() == 0) {
            ContentValues contentValues = new ContentValues();
//        contentValues.put(UserContract.UserEntry.ALY_EMAIL, email);
            contentValues.put(UserContract.UserEntry.ALY_USERNAME, username);
            contentValues.put(UserContract.UserEntry.ALY_PASSWORD, password);
            database.insert(TABLE_NAME, null, contentValues);
            Log.d("Database operations", "Item added");
            // get user info from DB
            Cursor _cursor = database.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE "
                    + UserContract.UserEntry.ALY_USERNAME+ "=?", new String[]{username});

            if(_cursor.moveToFirst()) {
                int id = _cursor.getInt(_cursor.getColumnIndex(UserContract.UserEntry.ALY_ID));
                String _username = _cursor.getString(_cursor.getColumnIndex(UserContract.UserEntry.ALY_USERNAME));
//                String _email = _cursor.getString(_cursor.getColumnIndex(UserContract.UserEntry.ALY_EMAIL));

//                this user object was created to store the user values
                UserObject userObject = new UserObject(id, _username);
                return userObject;
            }else {
                return null;
            }
        }else{
            // username already exist
            return null;
        }
    }




    public Cursor readCategory(SQLiteDatabase database){
        String[] projections = {UserContract.UserEntry.ALY_ID, UserContract.UserEntry.ALY_USERNAME};

        Cursor cursor = database.query(TABLE_NAME,projections,null,null,null,null,null);

        return cursor;
    }

    public Cursor readUsername(Integer the_user_id, SQLiteDatabase database){
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE "
                + UserContract.UserEntry.ALY_ID+ "=?", new String[]{the_user_id.toString()});

        return cursor;
    }




    public UserObject checkUser(String username , String password,  SQLiteDatabase database){

//        mDatabase = usersDBHelper.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE "
                + UserContract.UserEntry.ALY_USERNAME+ "=?", new String[]{username});
        if(cursor.getCount() > 0){
            if(cursor.moveToFirst()){
                String user_pass = cursor.getString(cursor.getColumnIndex(UserContract.UserEntry.ALY_PASSWORD));

                if (user_pass.equals(password)){
                    // handle user's data persistence
                    int id = cursor.getInt(cursor.getColumnIndex(UserContract.UserEntry.ALY_ID));
                    String _username = cursor.getString(cursor.getColumnIndex(UserContract.UserEntry.ALY_USERNAME));
//                    String _email = cursor.getString(cursor.getColumnIndex(UserContract.UserEntry.ALY_EMAIL));
                    UserObject userObject = new UserObject(id, _username);
                    return userObject;

                }else{
                    Log.d("Database operations", "Password is not correct");
                    return null;
                }

            }else{
                Log.d("Database operations", "No data was returned");
                return null;
            }
        }else {
            Log.d("Database operations", "No user");
        }
        return null;
    }




    public boolean updateFunction(String id,String username, String image){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(UserContract.UserEntry.ALY_USERNAME, username);
        contentValues.put(UserContract.UserEntry.ALY_IMAGE, image);
        contentValues.put(UserContract.UserEntry.ALY_ID, id);
        db.update(TABLE_NAME, contentValues, "aly_id = ?" ,new String[] {id});
        return true;

    }

}
