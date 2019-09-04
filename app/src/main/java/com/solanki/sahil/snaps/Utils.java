package com.solanki.sahil.snaps;

import com.google.firebase.database.FirebaseDatabase;

public class Utils {
    private static FirebaseDatabase mData;

    public static FirebaseDatabase getDatabase() {
        if (mData == null) {

            mData = FirebaseDatabase.getInstance();
            mData.setPersistenceEnabled(true);
        }
        return mData;
    }
}
