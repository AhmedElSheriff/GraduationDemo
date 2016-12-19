package com.example.android.graduationdemo.Firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Abshafi on 12/19/2016.
 */

public class FirebaseHelper {

    private static FirebaseDatabase mDatabase;
    private static DatabaseReference mDatabaseRef;
    /**
     * Create a new Database instance if first time initialization and setPersistence to cash our data offline .
     * @return our Firebase database instance .
     */
    public static FirebaseDatabase getDatabase() {
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
        }
        return mDatabase;
    }

}
