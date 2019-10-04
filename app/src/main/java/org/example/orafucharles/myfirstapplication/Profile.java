package org.example.orafucharles.myfirstapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.example.orafucharles.myfirstapplication.contract.db.UserContract;
import org.example.orafucharles.myfirstapplication.contract.db.UserReaderDbHelper;

import java.sql.Blob;

//import org.example.orafucharles.myfirstapplication.Contact.db.ContactDbHelper;

public class Profile extends AppCompatActivity {

    UserReaderDbHelper myDb;
    EditText mFname;
    EditText mLname;
    TextView mTvName;
    EditText musername;
    EditText mId;
    Button mUpdate;
    SharedPreferences preferences;

    public String the_username;
    public String the_userid;
    public String the_image;
    public String username;
    public String passWord;
    private Uri filePath;
    String imagePath;

    ImageView mImageView;
    Button mchangePicture;



    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        myDb = new UserReaderDbHelper(this);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        username = preferences.getString("username", "No name found");
        the_userid  = String.valueOf(preferences.getInt("user_id", 0));
        imagePath = preferences.getString("image", "no image");
        passWord = preferences.getString("password", "No password found");



        musername = findViewById(R.id.uusername);
        mTvName = findViewById(R.id.tv_name);
        mUpdate = findViewById(R.id.update);
        mId = findViewById(R.id.id);
        mImageView = findViewById(R.id.profileImage);
        mchangePicture = findViewById(R.id.changePicture);

//        Toast.makeText(Profile.this, "uid "+the_userid, Toast.LENGTH_SHORT).show();

        viewUserDetails();
        UpdateData();
        mTvName.setText(the_username);


        mchangePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check run time permission
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (checkCallingOrSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_DENIED){
                        //permission not granted, request it.
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        // show popup for rentime permission
                        requestPermissions(permissions, PERMISSION_CODE);

                    }

                    else {
                        //permission already granted
                        pickImageFromGallery();
                    }
                }

                else {
                    //sysytem is less than marshmallow
                    pickImageFromGallery();
                }
            }
        });

    }

    private void pickImageFromGallery() {
        //intent to pick image

        Intent  intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    //handle result of runtime permission


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_CODE:{
                if (grantResults.length >0  && grantResults [0] ==
                PackageManager.PERMISSION_GRANTED){

                    //permission was granted
                    pickImageFromGallery();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();

                }
            }
        }

    }

    //HANDLE RESULT OF PICKED IMAGE


    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_PICK_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            Uri selectedImageUri = data.getData();
            imagePath = getPath(selectedImageUri);

            Toast.makeText(Profile.this, getPath(selectedImageUri), Toast.LENGTH_LONG).show();


            BitmapFactory.Options options = new BitmapFactory.Options();
            // down sizing image as it throws OutOfMemory Exception for larger images
            // options.inSampleSize = 10;

//                creating a temporary place to store your folder
            final Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
            mImageView.setImageBitmap(bitmap);

        }
    }




    public void UpdateData(){
        mUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean  isUpdate = myDb.updateFunction(mId.getText().toString(), musername.getText().toString(), imagePath );

            if (isUpdate){
                viewUserDetails();
                Toast.makeText(getApplicationContext(), "Profile has been updated", Toast.LENGTH_SHORT).show();

            }else {
                Toast.makeText(getApplicationContext(), "Failure", Toast.LENGTH_SHORT).show();

            }
            }
        });
}



    public void viewUserDetails() {

        SQLiteDatabase database = myDb.getReadableDatabase();
        Cursor cursor = myDb.readUsername(Integer.valueOf(the_userid), database);

        if(cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                the_username = cursor.getString(cursor.getColumnIndex(UserContract.UserEntry.ALY_USERNAME));
                the_userid = String.valueOf(cursor.getInt(cursor.getColumnIndex(UserContract.UserEntry.ALY_ID)));
                the_image = cursor.getString(cursor.getColumnIndex(UserContract.UserEntry.ALY_IMAGE));
            }

            musername.setText(the_username);

//            this logic was written to avoid crash since a user does not set profile image on registeration
            if (the_image != null) {
                mImageView.setImageURI(Uri.parse(the_image));
            }
            mId.setText(the_userid+"");
            mTvName.setText(the_username);

//            image path was set to the image to avoid image not dispalying when other fields will be updating
            imagePath = the_image;
        }else {
            // msg
//            Toast.makeText(getApplicationContext(), "no detail", Toast.LENGTH_SHORT).show();

        }

    }


    //method to get the file path from uri
    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        assert cursor != null;
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        assert cursor != null;
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }
}
