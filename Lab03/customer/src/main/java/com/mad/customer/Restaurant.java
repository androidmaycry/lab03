package com.mad.customer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class Restaurant extends Fragment {


    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter<RestaurantItem, RestaurantViewHolder> mAdapter;
    private RecyclerView.LayoutManager layoutManager;


    private static FirebaseRecyclerOptions<RestaurantItem> options =
            new FirebaseRecyclerOptions.Builder<RestaurantItem>()
                    .setQuery(FirebaseDatabase.getInstance().getReference().child("restaurants"),
                            RestaurantItem.class).build();


    public Restaurant() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurant, container, false);
        setHasOptionsMenu(true);

        recyclerView = view.findViewById(R.id.restaurant_recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new FirebaseRecyclerAdapter<RestaurantItem, RestaurantViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull RestaurantViewHolder holder, int position, @NonNull RestaurantItem model) {
                String key = getRef(position).getKey();
                holder.setData(model, position, key);
            }

            @NonNull
            @Override
            public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_item,parent,false);
                return new RestaurantViewHolder(view);
            }
        };
        recyclerView.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onStart() {
        super.onStart();
        mAdapter.startListening();
    }
    @Override
    public void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }

}
