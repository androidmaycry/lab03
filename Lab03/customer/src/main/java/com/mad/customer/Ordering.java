package com.mad.customer;

import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

class ViewHolderDailyOffer extends RecyclerView.ViewHolder{
    private ImageView dishPhoto;
    private TextView dishName, dishDesc, dishPrice, dishQuantity;
    private DishItem current;
    private int position;

    ViewHolderDailyOffer(View itemView){
        super(itemView);

        dishName = itemView.findViewById(R.id.dish_name);
        dishDesc = itemView.findViewById(R.id.dish_desc);
        dishPrice = itemView.findViewById(R.id.dish_price);
        //dishQuantity = itemView.findViewById(R.id.dish_quant);
        dishPhoto = itemView.findViewById(R.id.dish_image);
    }

    void setData(DishItem current, int position){
        InputStream inputStream = null;

        this.dishName.setText(current.getName());
        this.dishDesc.setText(current.getDesc());
        this.dishPrice.setText(current.getPrice() + " €");
        //this.dishQuantity.setText(String.valueOf(current.getQuantity()));
        if(current.getPhotoUri() != null) {
            try{
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);

                inputStream = new URL(current.getPhotoUri()).openStream();
                if(inputStream != null)
                    Glide.with(itemView.getContext()).load(current.getPhotoUri()).into(dishPhoto);
                else
                    Glide.with(itemView.getContext()).load(R.drawable.hamburger).into(dishPhoto);
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }

        this.position = position;
        this.current = current;
    }
}

public class Ordering extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter<DishItem, ViewHolderDailyOffer> mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private static FirebaseRecyclerOptions<DishItem> options =
            new FirebaseRecyclerOptions.Builder<DishItem>()
                    .setQuery(FirebaseDatabase.getInstance().getReference().child("dishes"),
                            DishItem.class).build();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordering);
        recyclerView = findViewById(R.id.dish_recyclerview);
        //recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new FirebaseRecyclerAdapter<DishItem, ViewHolderDailyOffer>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolderDailyOffer holder, int position, @NonNull DishItem model) {
                holder.setData(model, position);
            }

            @NonNull
            @Override
            public ViewHolderDailyOffer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dish_item,parent,false);
                return new ViewHolderDailyOffer(view);
            }
        };
        recyclerView.setAdapter(mAdapter);
        getIncomingIntent();
    }

    private void getIncomingIntent(){

        String myName = getIntent().getStringExtra("name");
        String myAddr = getIntent().getStringExtra("addr");
        Long myCell = getIntent().getLongExtra("cell",0);
        String myDescription = getIntent().getStringExtra("description");
        String myEmail = getIntent().getStringExtra("email");
        String myOpening = getIntent().getStringExtra("opening");
        String myImg = getIntent().getStringExtra("img");

        setFields(myName, myAddr, myCell, myDescription, myEmail, myOpening, myImg);

    }

    private void setFields (String name, String addr, Long cell, String description, String email, String opening, String img){
        TextView mname = findViewById(R.id.textView5);
        TextView maddr = findViewById(R.id.textView);
        TextView mcell = findViewById(R.id.textView3);
        TextView mdescription = findViewById(R.id.textView6);
        TextView memail = findViewById(R.id.textView2);
        TextView mopening = findViewById(R.id.textView4);
        ImageView mimg = findViewById(R.id.imageView);

        mname.setText(name);
        maddr.setText(addr);
        mcell.setText(cell.toString());
        mdescription.setText(description);
        memail.setText(email);
        mopening.setText(opening);
        Picasso.get()
                .load(img)
                .resize(150, 150)
                .centerCrop()
                .into(mimg);


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
