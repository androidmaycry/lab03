package com.mad.customer;

import static com.mad.lib.SharedClass.CUSTOMER_PATH;
import static com.mad.lib.SharedClass.user;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mad.lib.User;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class NavApp extends AppCompatActivity implements
        Restaurant.OnFragmentInteractionListener,
        Profile.OnFragmentInteractionListener,
        Order.OnFragmentInteractionListener{


    public String UID;
    private static final String CheckPREF = "First Run";
    private Profile profile;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item ->  {

        switch (item.getItemId()) {
            case R.id.navigation_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Restaurant()).commit();
                return true;
            case R.id.navigation_profile:
                Bundle bundle = new Bundle();
                bundle.putString("UID",UID);
                Profile profile = new Profile();
                profile.setArguments(bundle);

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, profile).commit();
                return true;
            case R.id.navigation_reservation:
                Bundle bundle2 = new Bundle();
                bundle2.putString("UID",UID);
                Order orders = new Order();
                orders.setArguments(bundle2);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, orders).commit();
                return true;
        }
        return false;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_app);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new Restaurant()).commit();
        }
        Intent i  = getIntent();
        UID = i.getStringExtra("UID");

        getUserInfo();

        Log.d("UID",UID);
    }

    public void getUserInfo() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(CUSTOMER_PATH).child(UID);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.child("customer_info").getValue(User.class);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("MAIN", "Failed to read value.", error.toException());
            }
        });

    }
    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    @Override
    protected void onResume() {
        super.onResume();
    }
}

