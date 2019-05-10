package com.mad.customer;

import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.Query;
import com.mad.lib.Restaurateur;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SearchView;
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

import static android.view.View.INVISIBLE;


public class Restaurant extends Fragment {


    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter<Restaurateur, RestaurantViewHolder> mAdapter;
    private RecyclerView.LayoutManager layoutManager;


    private static FirebaseRecyclerOptions<Restaurateur> options =
            new FirebaseRecyclerOptions.Builder<Restaurateur>()
                    .setQuery(FirebaseDatabase.getInstance().getReference().child("restaurants"),
                            Restaurateur.class).build();


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

        mAdapter = new FirebaseRecyclerAdapter<Restaurateur, RestaurantViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull RestaurantViewHolder holder, int position, @NonNull Restaurateur model) {
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
        final MenuItem searchItem = menu.findItem(R.id.search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Toast.makeText(getContext(), query, Toast.LENGTH_LONG).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                onStop();
                if(newText.length()==0){
                    options =
                            new FirebaseRecyclerOptions.Builder<Restaurateur>()
                                    .setQuery(FirebaseDatabase.getInstance().getReference().child("restaurants"),
                                            Restaurateur.class).build();
                }
                else {
                    options =
                            new FirebaseRecyclerOptions.Builder<Restaurateur>()
                                    .setQuery(FirebaseDatabase.getInstance().getReference().child("restaurants"), new SnapshotParser<Restaurateur>(){
                                        @NonNull
                                        @Override
                                        public Restaurateur parseSnapshot(@NonNull DataSnapshot snapshot) {
                                            Restaurateur searchRest = new Restaurateur();

                                            if (snapshot.child("name").exists() && snapshot.child("name").getValue().toString().contains(newText)) {

                                                if (snapshot.child("photoUri").getValue() != null) {
                                                    searchRest = new Restaurateur(snapshot.child("mail").getValue().toString(),
                                                            snapshot.child("name").getValue().toString(),
                                                            snapshot.child("addr").getValue().toString(),
                                                            snapshot.child("cuisine").getValue().toString(),
                                                            snapshot.child("openingTime").getValue().toString(),
                                                            snapshot.child("phone").getValue().toString(),
                                                            snapshot.child("photoUri").getValue().toString());
                                                } else {
                                                    searchRest = new Restaurateur(snapshot.child("mail").getValue().toString(),
                                                            snapshot.child("name").getValue().toString(),
                                                            snapshot.child("addr").getValue().toString(),
                                                            snapshot.child("cuisine").getValue().toString(),
                                                            snapshot.child("phone").getValue().toString(),
                                                            snapshot.child("openingTime").getValue().toString(),
                                                            null);
                                                }
                                            }
                                            Log.d("PROVA", ""+newText+" and "+searchRest);
                                            return searchRest;
                                        }
                                    }).build();
                }
                mAdapter = new FirebaseRecyclerAdapter<Restaurateur, RestaurantViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull RestaurantViewHolder holder, int position, @NonNull Restaurateur model) {
                        String key = getRef(position).getKey();
                        if(model.getName().equals("")){
                            holder.itemView.findViewById(R.id.restaurant).setLayoutParams(new FrameLayout.LayoutParams(0,0));
                            //holder.itemView.setLayoutParams(new ConstraintLayout.LayoutParams(0,0));
                        }
                        else {
                            holder.setData(model, position, key);
                        }
                    }

                    @NonNull
                    @Override
                    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_item,parent,false);
                        return new RestaurantViewHolder(view);
                    }
                };
                recyclerView.setAdapter(mAdapter);
                onStart();
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();



        switch(id){
            case R.id.search:
                //TODO ricerca

                /*SearchView sv = (SearchView) item.getActionView();
                String ciao = (String) sv.getQuery();

                Log.d("CIAO", ""+ciao);*/

                //Toast.makeText(getContext(), ciao, Toast.LENGTH_LONG).show();

                return true;

            default:
                return super.onOptionsItemSelected(item);


        }
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

    public interface OnFragmentInteractionListener {
    }
}
