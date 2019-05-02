package com.mad.appetit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class RecyclerAdapterDish extends RecyclerView.Adapter<RecyclerAdapterDish.MyViewHolder> {
    private final DailyOffer dailyOffer;
    private ArrayList<DailyOfferItem> mData;
    private LayoutInflater mInflater;
    private Context context;
    private static final String MyOFFER = "Daily_Offer";

    public RecyclerAdapterDish(Context context, ArrayList<DailyOfferItem> data,DailyOffer dailyOffer){
        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.dailyOffer = dailyOffer;
        this.mData = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View view = mInflater.inflate(R.layout.dailyoffer_listview, parent, false);

        view.findViewById(R.id.delete_offer).setOnClickListener(e -> {
            int pos = getPositionView(((TextView)view.findViewById(R.id.dish_name)).getText().toString());
            dailyOffer.deleteDish(pos);

        });

        view.findViewById(R.id.edit_offer).setOnClickListener(h -> {
            int pos = getPositionView(((TextView)view.findViewById(R.id.dish_name)).getText().toString());
            dailyOffer.editDailyOffer(pos);
        });

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        DailyOfferItem currentObj = mData.get(position);

        myViewHolder.setData(currentObj, position);
    }

    public void deleteDish(int pos){
        mData.remove(pos);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    private int getPositionView(String name){
        int pos = 0;

        for(DailyOfferItem d : mData){
            if(d.getName().equals(name)){
                return pos;
            }
            pos++;
        }

        return pos;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView dishPhoto;
        TextView dishName, dishDesc, dishPrice, dishQuantity;
        int position;
        DailyOfferItem current;

        private MyViewHolder(View itemView){
            super(itemView);

            dishName = itemView.findViewById(R.id.dish_name);
            dishDesc = itemView.findViewById(R.id.dish_desc);
            dishPrice = itemView.findViewById(R.id.dish_price);
            dishQuantity = itemView.findViewById(R.id.dish_quant);
            dishPhoto = itemView.findViewById(R.id.dish_image);
        }

        private void setData(DailyOfferItem current, int position){
            this.dishName.setText(current.getName());
            this.dishDesc.setText(current.getDesc());
            this.dishPrice.setText(String.valueOf(current.getPrice()) + " $");
            this.dishQuantity.setText(String.valueOf(current.getQuantity()));
            if(current.getPhotoPath() != null)
                    //setPhoto(current.getPhotoPath());
                    Glide.with(context).load(current.getPhotoPath()).into(dishPhoto);
            this.position = position;
            this.current = current;
        }

    }
}