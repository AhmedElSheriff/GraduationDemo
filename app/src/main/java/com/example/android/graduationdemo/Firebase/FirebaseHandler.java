package com.example.android.graduationdemo.Firebase;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.android.graduationdemo.User;
import com.example.android.graduationdemo.callbacks.AddLatLng;
import com.example.android.graduationdemo.callbacks.GetLocationData;
import com.example.android.graduationdemo.callbacks.GetUserData;
import com.example.android.graduationdemo.callbacks.LoginCallBack;
import com.example.android.graduationdemo.callbacks.UserAvailability;
import com.example.android.graduationdemo.data.EmergencyLocation;
import com.example.android.graduationdemo.data.PendingRequests;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
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

    public static void addNewRequest(final PendingRequests request, String userEmail, final AddLatLng listener)
    {
        String mEmail = userEmail.substring(0,userEmail.indexOf("@"));
        final String emailNode = mEmail.replace(".","");
        FirebaseHelper.getDatabase().getReference().child("PendingRequests").child(emailNode).setValue(request).addOnCompleteListener(new OnCompleteListener<Void>() {
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

        FirebaseHelper.getDatabase().getReference("allUsers").child(emailNode).child("lastRequest").setValue(request).addOnCompleteListener(new OnCompleteListener<Void>() {
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

    public static void getUserInfo(String useremail, final GetUserData listener)
    {

        String email =useremail.substring(0,useremail.indexOf("@"));
        final String emailNode = email.replace(".","");

        FirebaseHelper.getDatabase().getReference("allUsers").child(emailNode).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                listener.getUserData(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    public static void addNewUser(FirebaseAuth auth, final User user, final Context context, final LoginCallBack listener)
    {
        auth.createUserWithEmailAndPassword(user.getUserEmail(),user.getUserPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(context,"User Created",Toast.LENGTH_SHORT).show();
                    listener.onLoggedIn();
                }

                else {
                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                    listener.onFail();
                }
            }
        });

        String email = user.getUserEmail().substring(0,user.getUserEmail().indexOf("@"));
        final String emailNode = email.replace(".","");
        FirebaseHelper.getDatabase().getReference("allUsers").child(emailNode).setValue(user);
    }

    public static void signIn(FirebaseAuth auth, final User user, final Context context, final LoginCallBack listener)
    {
        auth.signInWithEmailAndPassword(user.getUserEmail(),user.getUserPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(context,"Signed In",Toast.LENGTH_SHORT).show();
                    listener.onLoggedIn();
                }

                else {
                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                    Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    listener.onFail();
                }
            }
        });
    }

    public static void signOut(FirebaseAuth auth, final Context context)
    {
        auth.signOut();
        Toast.makeText(context,"Signed Out",Toast.LENGTH_SHORT).show();
    }

    public static void checkIfUserAvailable(final String userEmail, final UserAvailability listener)
    {
        String email = userEmail.substring(0,userEmail.indexOf("@"));
        final String emailNode = email.replace(".","");
        FirebaseHelper.getDatabase().getReference("allUsers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot child:dataSnapshot.getChildren())
                {
                    String temp = child.getKey();
                    if(temp.equals(emailNode))
                    {
                        listener.onSearchComplete(true);
                        return;
                    }
                }
                listener.onSearchComplete(false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
