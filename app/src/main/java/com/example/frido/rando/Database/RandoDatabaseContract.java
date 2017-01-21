package com.example.frido.rando.Database;

import android.provider.BaseColumns;

/**
 * Created by fjmar on 1/20/2017.
 */

public class RandoDatabaseContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private RandoDatabaseContract() {
    }



    /* Inner class that defines the table contents */
    public static class RandoDatabase implements BaseColumns {
        public static final String TABLE_NAME = "RandoObjects";
        public static final String COLUMN_URL = "url";
        public static final String COLUMN_THUMBNAIL_ID = "thumbnail_ID";


    }
}
