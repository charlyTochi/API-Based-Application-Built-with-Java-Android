package org.example.orafucharles.myfirstapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.example.orafucharles.myfirstapplication.contract.db.UserReaderDbHelper;

//import org.example.orafucharles.myfirstapplication.Contact.db.ContactDbHelper;

public class Login extends AppCompatActivity {


    public SharedPreferences mPreferences;
    public SharedPreferences.Editor mEditor;
    public SharedPreferences preferences;

    SQLiteDatabase sqLiteDatabase;
    EditText mTextUsername;
    EditText mPassword;
    Button mButtonLogin;
    TextView mTextViewRegister;
    UserReaderDbHelper db;
    UserObject res;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        preferences = getSharedPreferences("user_values", MODE_PRIVATE);
        db = new UserReaderDbHelper(this);
        mTextUsername = findViewById(R.id.username);
        mPassword = findViewById(R.id.password);
        mButtonLogin = findViewById(R.id.signInButton);
        mTextViewRegister = findViewById(R.id.signUp);





        mTextViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent registerIntent = new Intent(Login.this, Register.class );
                startActivity(registerIntent);
            }
        });

        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPreferences.edit();
        checkSharedPreferences();








        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Validate() ){
                    Intent loginIntent = new Intent(Login.this, MainActivity.class );
                    startActivity(loginIntent);
                }else {
                    Toast.makeText(Login.this, "Login Error", Toast.LENGTH_SHORT).show();

                    mEditor.putString(getString(R.string.username), "");
                    mEditor.apply();


                    mEditor.putString(getString(R.string.password), "");
                    mEditor.apply();
                }
            }
        });
    }




    //        checking the shared preferences and set them accordingly
    private void checkSharedPreferences() {
        String username = mPreferences.getString(getString(R.string.username), "");
        String password = mPreferences.getString(getString(R.string.password), "");

        mTextUsername.setText(username);
        mPassword.setText(password);

        if (Validate()){
//            Toast.makeText(Login.this, "Added to shared preference", Toast.LENGTH_SHORT).show();
        }else{
//            Toast.makeText(Login.this, "Not Added to shared preference", Toast.LENGTH_SHORT).show();
        }
    }








    /* set edit text to error when empty */
    public static boolean hasText(EditText editText) {

        return hasText(editText, "Required");
    }

    /* check edit text length and set error message for required edit text
     * Custom Message */
    public static boolean hasText(EditText editText, String error_message) {

        String text = editText.getText().toString().trim();
        editText.setError(null);

        // length 0 means there is no text
        if (text.length() == 0) {
            editText.setError(error_message);
            return false;
        }

        return true;
    }



    private boolean Validate() {
        /* Validate all required edit text */
        // check if username is not null
        // check if password is not null
        // check if res is true;;
        boolean check = true;
        if (hasText(mTextUsername)) {
            if (hasText(mPassword)){
                db = new UserReaderDbHelper(this);
                sqLiteDatabase = db.getReadableDatabase();
                String user = mTextUsername.getText().toString().trim();
                String pwd = mPassword.getText().toString().trim();
                res = db.checkUser(user, pwd, sqLiteDatabase);
                if (res != null){
//                    sharedpreference using editor because we want to save the value its reurning
                    int user_id = res.getId();
                    String username = res.getmUsername();
                    // save in shared preference
                    mEditor.putInt("user_id", user_id);
                    mEditor.putString("username", username);
                    mEditor.apply();

                    return true;
//                    Toast.makeText(Login.this, "Field is there", Toast.LENGTH_LONG).show();

                }else {
                    check = false;
//                    Toast.makeText(Login.this, "no field in database", Toast.LENGTH_LONG).show();

                }
            }else {
                check = false;
//                Toast.makeText(Login.this, "Password is present", Toast.LENGTH_LONG).show();

            }
        }else {
//            Toast.makeText(Login.this, "username is present", Toast.LENGTH_LONG).show();

            check = false;
        }
        return check;
    }



}

