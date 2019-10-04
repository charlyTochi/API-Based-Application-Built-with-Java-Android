package org.example.orafucharles.myfirstapplication.contract.db;

import android.provider.BaseColumns;

import java.sql.Blob;

public final class UserContract {

    private UserContract(){}

    public static class UserEntry implements BaseColumns {
        public static final String TABLE_NAME = "aly_table";
        public static final String ALY_ID = "aly_id";
        public static final String ALY_USERNAME = "aly_username";
        public static final String ALY_PASSWORD = "aly_password";
        public static final String ALY_IMAGE = "image";

    }
}
