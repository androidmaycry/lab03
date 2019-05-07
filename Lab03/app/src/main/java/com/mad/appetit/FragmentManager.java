package com.mad.appetit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.net.Uri;
import android.support.design.widget.BottomNavigationView;

public class FragmentManager extends AppCompatActivity implements DailyOffer.OnFragmentInteractionListener,
        Reservation.OnFragmentInteractionListener, Home.OnFragmentInteractionListener,
        Profile.OnFragmentInteractionListener{

    private static final String CheckPREF = "First Run";
    private Profile profile;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item ->  {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Home()).commit();
                return true;
            case R.id.navigation_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Profile()).commit();
                return true;
            case R.id.navigation_dailyoffer:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DailyOffer()).commit();
                return true;
            case R.id.navigation_reservation:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Reservation()).commit();
                return true;
        }
        return false;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_manager);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new Home()).commit();

        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /*@Override
    protected void onResume() {
        super.onResume();
        SharedPreferences first_check = getSharedPreferences(CheckPREF, 0);
        Log.d("MAIN","resuming");

        if(first_check.getBoolean("firsRun",false)){
            first_check.edit().clear().commit();
            goHome();
        }
    }

    public void goHome() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Home()).commit();
        BottomNavigationView navigationView = (BottomNavigationView) findViewById(R.id.navigation);
        navigationView.getMenu().getItem(3).setChecked(true);
        navigationView.getMenu().getItem(2).setChecked(false);
        navigationView.getMenu().getItem(1).setChecked(false);
        navigationView.getMenu().getItem(0).setChecked(false);
    }*/

    /*private final String DISHES_PATH = "dishes/";

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference(DISHES_PATH);
    Map<String, Object> test = new HashMap<String, Object>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_manager);

        findViewById(R.id.button).setOnClickListener(e -> {
            test.put("carbonara", new Dish("pasta con porco dio", 22));
            myRef.updateChildren(test);
        });

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                List<Dish> dishes = new ArrayList<>();
                for(DataSnapshot d : dataSnapshot.getChildren())
                    dishes.add(d.getValue(Dish.class));

                for(Dish d : dishes)
                    Log.d("MAIN", "Value is: " + d.getDescription());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("MAIN", "Failed to read value.", error.toException());
            }
        });
    }*/
}
