package com.mad.customer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

import static android.support.constraint.Constraints.TAG;

public class RecyclerAdapterRestaurant extends RecyclerView.Adapter<RecyclerAdapterRestaurant.MyViewHolder> {
    private ArrayList<RestaurantItem> items;
    private LayoutInflater mInflater;
    private Restaurant restaurant;
    private OnNoteListener mOnNoteListener;
    private int flag;


    public RecyclerAdapterRestaurant(Context context, ArrayList<RestaurantItem> items){
        mInflater = LayoutInflater.from(context);
        this.items = items;
    }

    @NonNull
    @Override
    public RecyclerAdapterRestaurant.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent,int position) {
        View view =  mInflater.inflate(R.layout.restaurant_item, parent, false);
        return new MyViewHolder(view, mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterRestaurant.MyViewHolder myViewHolder,int position) {

        RestaurantItem mCurrent = items.get(position);
        myViewHolder.name.setText(mCurrent.getName());
        myViewHolder.addr.setText(mCurrent.getAddr());
        myViewHolder.cuisine.setText(mCurrent.getCuisine());
        myViewHolder.opening.setText(mCurrent.getOpening());
        Picasso.get()
                .load(mCurrent.getImg())
                .resize(150, 150)
                .centerCrop()
                .into(myViewHolder.img);
        //myViewHolder.img.setImageResource(mCurrent.getImg());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView name;
        TextView addr;
        TextView cuisine;
        TextView opening;
        ImageView img;
        OnNoteListener mOnNoteListener;


        public MyViewHolder(View itemView, OnNoteListener onNoteListener){
            super(itemView);
            name = itemView.findViewById(R.id.listview_name);
            addr = itemView.findViewById(R.id.listview_address);
            cuisine = itemView.findViewById(R.id.listview_cuisine);
            img = itemView.findViewById(R.id.restaurant_image);
            opening = itemView.findViewById(R.id.listview_opening);
            mOnNoteListener = onNoteListener;

            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            Toast.makeText(view.getContext(), "Item"+getAdapterPosition(), Toast.LENGTH_LONG).show();
        }


    }
    public interface OnNoteListener{
        void onNoteClick(int position);
    }
}
