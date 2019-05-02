package com.mad.appetit;

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

import org.w3c.dom.Text;

import java.util.ArrayList;

public class RecyclerAdapterReservation extends RecyclerView.Adapter<RecyclerAdapterReservation.MyViewHolder> {
    private ArrayList<ReservationItem> items;
    private LayoutInflater mInflater;
    private Reservation reservation;
    private int flag;

    public RecyclerAdapterReservation(Context context, ArrayList<ReservationItem> items, Reservation reservation,int flag){
        mInflater = LayoutInflater.from(context);
        this.items = items;
        this.reservation = reservation;
        this.flag = flag;
    }

    @NonNull
    @Override
    public RecyclerAdapterReservation.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent,int position) {
        View view =  mInflater.inflate(R.layout.reservation_listview, parent, false);

        ImageView confirm = view.findViewById(R.id.confirm_reservation);
        ImageView delete = view.findViewById(R.id.delete_reservation);
        ImageView viewOrder = view.findViewById(R.id.open_reservation);


        if(flag == 1) {
            confirm.setVisibility(View.INVISIBLE);
            delete.setVisibility(View.INVISIBLE);
        }
        confirm.setOnClickListener(e->{
            TextView text = view.findViewById(R.id.listview_cellphone);
            int pos = findPos(text.getText().toString());
            reservation.acceptOrder(pos);
        });

        delete.setOnClickListener( e ->{
            TextView text = view.findViewById(R.id.listview_cellphone);
            int pos = findPos(text.getText().toString());
            reservation.removeOrder(pos);
        });

        view.setOnClickListener(e->{
            TextView text = view.findViewById(R.id.listview_cellphone);
            int pos = findPos(text.getText().toString());
            reservation.viewOrder(pos,flag);
        });

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterReservation.MyViewHolder myViewHolder,int position) {

        ReservationItem mCurrent = items.get(position);
        myViewHolder.name.setText(mCurrent.getName());
        myViewHolder.addr.setText(mCurrent.getAddr());
        myViewHolder.cell.setText(mCurrent.getCell());
        myViewHolder.img.setImageResource(mCurrent.getImg());
        myViewHolder.time.setText(mCurrent.getTime());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name;
        TextView addr;
        TextView cell;
        ImageView img;
        TextView time;

        public MyViewHolder(View itemView){
            super(itemView);
            name = itemView.findViewById(R.id.listview_name);
            addr = itemView.findViewById(R.id.listview_address);
            cell = itemView.findViewById(R.id.listview_cellphone);
            img = itemView.findViewById(R.id.profile_image);
            time = itemView.findViewById(R.id.textView_time);
        }
    }

    public int findPos(String cell){
        int num = 0;

        for(ReservationItem i : items){
            Log.d("Compare",cell);
            if(cell.equals(i.getCell())){
                return num;
            }
            num++;
        }

        return 0;
    }
}
