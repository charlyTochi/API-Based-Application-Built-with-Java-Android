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



public class Register extends AppCompatActivity {

    UserReaderDbHelper db;
    SQLiteDatabase sqLiteDatabase;
    EditText mTextUsername;
    EditText mPassword;
    EditText mButtonConfirmPassword;
    Button mButtonSignUp;
    TextView mTextViewLogin;
    UserObject val;
    SharedPreferences preferences;
    public SharedPreferences.Editor mEditor;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = preferences.edit();


         db = new UserReaderDbHelper(this);

        mTextUsername = findViewById(R.id.edittext_username);
        mPassword = findViewById(R.id.edittext_password);
        mButtonConfirmPassword = findViewById(R.id.confirmPassword);
        mButtonSignUp = findViewById(R.id.signUpButton);
        mTextViewLogin = findViewById(R.id.textview_signIn);

        mTextViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent LoginIntent = new Intent(Register.this, Login.class);
                startActivity(LoginIntent);
            }
        });



        mButtonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Validate()) {
                    Toast.makeText(Register.this, "You have registered", Toast.LENGTH_SHORT).show();
                    Intent moveLogin = new Intent(Register.this, Login.class);
                    startActivity(moveLogin);
                }

            }



//                    long val = db.addUser(user, pwd);
//                    if (val > 0){
//                        Toast.makeText(Register.this, "You have registered", Toast.LENGTH_LONG).show();
//
//                    }else{
//                        Toast.makeText(Register.this, "Registration Error", Toast.LENGTH_LONG).show();
//
//                    }


//                }else{
//                    Toast.makeText(Register.this, "Password is not matching", Toast.LENGTH_LONG).show();
//
//
//                }













        });
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

        String user = mTextUsername.getText().toString().trim();
        String pwd = mPassword.getText().toString().trim();
        String cnf_pwd = mButtonConfirmPassword.getText().toString().trim();

        boolean check = false;
        /* Validate all required edit text */
        if (hasText(mTextUsername)){
            if (hasText(mPassword)) {
                if (hasText(mButtonConfirmPassword)){
                    if (pwd.equals(cnf_pwd)) {
                        db = new UserReaderDbHelper(this);
                        sqLiteDatabase = db.getReadableDatabase();
                        val = db.addUser(user, pwd, sqLiteDatabase);
                        if (val != null){
                            int user_id = val.getId();
                            String username = val.getmUsername();
                            // save in shared preference
                            mEditor.putInt("user_id", user_id);
                            mEditor.putString("username", username);

                            mEditor.apply();


                            //navigate to dashboard


                            Intent registerIntent = new Intent(Register.this, MainActivity.class );
                            startActivity(registerIntent);

                            Toast.makeText(Register.this, "You have registered", Toast.LENGTH_SHORT).show();
                        }else{
                            // error encountered while adding user
                        }

                    }else{
                        Toast.makeText(Register.this, "Passwords fields does not match", Toast.LENGTH_SHORT).show();
                        check = false;}
                }else{ check = false; }
            }else{check = false; }
        }else{ check = false; }



        return check;
    }
}
