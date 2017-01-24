package com.example.android.graduationdemo.Firebase;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.android.graduationdemo.data.EmergencyLocation;
import com.example.android.graduationdemo.data.PendingRequests;
import com.example.android.graduationdemo.callbacks.AddLatLng;
import com.example.android.graduationdemo.callbacks.GetLocationData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Abshafi on 12/19/2016.
 */

public class FirebaseHandler {

    public static void addNewEmergency(final EmergencyLocation location, final AddLatLng listener) {
        FirebaseHelper.getDatabase().getReference().child("Emergency Locations").child("userName").setValue(location).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Log.v("Status", "Added successfuly");
                    listener.onAdded();
                }
                else
                    listener.onFailed(task.getException().getMessage());
            }
        });
    }

    public static void addNewRequest(final PendingRequests request, final AddLatLng listener)
    {
        FirebaseHelper.getDatabase().getReference().child("PendingRequests").setValue(request).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Log.v("Status", "Added successfuly");
                    listener.onAdded();
                }
                else
                    listener.onFailed(task.getException().getMessage());
            }
        });
    }
    public static void getEmergencyLocation(final GetLocationData listener)
    {
        FirebaseHelper.getDatabase().getReference("Emergency Locations").child("userName").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String mLat,mLng;

                EmergencyLocation emergencyLocation = dataSnapshot.getValue(EmergencyLocation.class);
                listener.getLocationData(emergencyLocation);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
