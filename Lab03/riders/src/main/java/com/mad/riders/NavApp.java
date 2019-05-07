package com.mad.riders;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class NavApp extends AppCompatActivity implements
        Orders.OnFragmentInteractionListener,
        Home.OnFragmentInteractionListener,
        Profile.OnFragmentInteractionListener{


    public String UID;
    private static final String CheckPREF = "First Run";
    private Profile profile;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item ->  {

        switch (item.getItemId()) {
            case R.id.navigation_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Home()).commit();
                return true;
            case R.id.navigation_profile:
                Bundle bundle = new Bundle();
                bundle.putString("UID",UID);
                Profile profile = new Profile();
                profile.setArguments(bundle);

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, profile).commit();
                return true;
            case R.id.navigation_reservation:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Orders()).commit();
                return true;
        }
        return false;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new Home()).commit();
        }
        Intent i  = getIntent();
        UID = i.getStringExtra("UID");

        Log.d("UID",UID);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    @Override
    protected void onResume() {
        super.onResume();
    }
}
