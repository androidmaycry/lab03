package com.mad.riders;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Orders.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Orders#newInstance} factory method to
 * create an instance of this fragment.
 */


class ViewHolder extends RecyclerView.ViewHolder {
    public TextView restaurantAddr;
    public TextView customerAdrr;
    public TextView toPay;
    public View view;

    public ViewHolder(View itemView) {
        super(itemView);
        restaurantAddr = itemView.findViewById(R.id.listview_address);
        customerAdrr = itemView.findViewById(R.id.listview_name);
        toPay = itemView.findViewById(R.id.listview_toPay);
        view = itemView;
    }


    public void setRestaurantAddr(String string) {
        restaurantAddr.setText(string);
    }


    public void setCustomerAdrr(String string) {
        customerAdrr.setText(string);
    }

    public void setToPay(double toPay){
        Double num = toPay;
        this.toPay.setText(num.toString()+ "$");
    }

    public View getView(){return view;}
}

class Order{
    public String orderID;
    public String restaurantAddr;
    public String customerAddr;
    public double toPay;

    // Constructor for Firebase
    public Order(){}

    public Order(String orderID,String RestaurantAddr,String CustomerAddr, double toPay){
        this.orderID = orderID;
        this.restaurantAddr = RestaurantAddr;
        this.customerAddr = CustomerAddr;
        this.toPay = toPay;
    }

    public String getRestaurantAddr(){return restaurantAddr;}
    public void setRestaurantAddr(String restaurantAddr){ this.restaurantAddr = restaurantAddr;}
    public String getCustomerAddr(){return customerAddr;}
    public void setCustomerAddr(String customerAddr){ this.customerAddr = customerAddr;}
    public double getToPay(){return toPay;}
    public void setToPay(){this.toPay = toPay;}
    public String getOrderID(){return orderID;}

}

public class Orders extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final String RIDERS_PATH = "riders/users/";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView.LayoutManager layoutManager;

    private Orders.OnFragmentInteractionListener mListener;
    private FirebaseRecyclerAdapter<Order, ViewHolder> mAdapter_done;
    private FirebaseRecyclerAdapter<Order, ViewHolder> mAdapter_pending;
    private String UID;
    private FirebaseRecyclerAdapter<Order, ViewHolder> mAdapter_accepted;
    private LinearLayoutManager layoutManager2;

    public Orders() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Orders.
     */
    // TODO: Rename and change types and number of parameters
    public static Orders newInstance(String param1, String param2) {
        Orders fragment = new Orders();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_orders, container, false);
        UID = getArguments().getString("UID");

        view.findViewById(R.id.delivered).setOnClickListener(e->{
                deliveredOrder();
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference query = database.getReference("riders/users/"+UID+"/pending/");
        DatabaseReference query2 = FirebaseDatabase.getInstance().getReference("riders/users/"+UID+"/delivering/");

        FirebaseRecyclerOptions<Order> options =
                new FirebaseRecyclerOptions.Builder<Order>()
                        .setQuery(query, new SnapshotParser<Order>() {
                            @NonNull
                            @Override
                            public Order parseSnapshot(@NonNull DataSnapshot snapshot) {
                                Log.d("PARSE",snapshot.getValue().toString());
                                Order order = new Order(snapshot.getKey()
                                        ,(String)snapshot.child("addr").getValue()
                                        ,(String)snapshot.child("name").getValue()
                                        ,10.75);
                                return order;
                            }
                        }).build();

        FirebaseRecyclerOptions<Order> options2 =
                new FirebaseRecyclerOptions.Builder<Order>()
                        .setQuery(query2, new SnapshotParser<Order>() {
                            @NonNull
                            @Override
                            public Order parseSnapshot(@NonNull DataSnapshot snapshot) {
                                Order order = new Order(snapshot.getKey()
                                        ,(String)snapshot.child("customerAddr").getValue()
                                        ,(String)snapshot.child("restaurantAddr").getValue()
                                        ,10.75);
                                return order;
                            }
                        }).build();


        mAdapter_pending = new FirebaseRecyclerAdapter<Order, ViewHolder>(options2) {
            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.reservation_listview, parent, false);

                return new ViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(ViewHolder holder, int position, Order model) {
                // Bind the Chat object to the ChatHolder
                // ...
                holder.setCustomerAdrr(model.getCustomerAddr());
                holder.setRestaurantAddr(model.getRestaurantAddr());
                holder.setToPay(model.getToPay());
                holder.getView().findViewById(R.id.confirm_reservation)
                        .setOnClickListener(e -> acceptOrder(position,model));
            }
        };


        layoutManager = new LinearLayoutManager(getContext());
        RecyclerView recyclerView = view.findViewById(R.id.order_list_pending);
        recyclerView.setAdapter(mAdapter_pending);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter_accepted = new FirebaseRecyclerAdapter<Order, ViewHolder>(options) {
            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.reservation_listview, parent, false);

                return new ViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(ViewHolder holder, int position, Order model) {
                // Bind the Chat object to the ChatHolder
                // ...
                holder.setCustomerAdrr(model.getCustomerAddr());
                holder.setRestaurantAddr(model.getRestaurantAddr());
                holder.setToPay(model.getToPay());
                holder.getView().findViewById(R.id.confirm_reservation)
                        .setOnClickListener(e -> acceptOrder(position,model));
                Log.d("RECYCLER","SONO ENTRATO");
            }
        };


        layoutManager = new LinearLayoutManager(getContext());
        RecyclerView recyclerView2 = view.findViewById(R.id.order_delivering);
        recyclerView2.setAdapter(mAdapter_accepted);
        recyclerView2.setLayoutManager(layoutManager);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void acceptOrder(int pos,Order done){

        AlertDialog reservationDialog = new AlertDialog.Builder(this.getContext()).create();
        LayoutInflater inflater = LayoutInflater.from(this.getContext());
        final View view = inflater.inflate(R.layout.reservation_dialog, null);


        view.findViewById(R.id.button_confirm).setOnClickListener(e ->{
        DatabaseReference query2 = FirebaseDatabase.getInstance().getReference(RIDERS_PATH+"/"+UID+"/pending/");
        DatabaseReference query = FirebaseDatabase.getInstance().getReference(RIDERS_PATH);
        Map<String,Object> order = new HashMap<String,Object>();
        order.put(done.getOrderID(),done);
        query.child(UID).child("delivering").updateChildren(order);
        query2.removeValue();
        reservationDialog.dismiss();

    });

        view.findViewById(R.id.button_cancel).setOnClickListener(e ->{
        DatabaseReference query = FirebaseDatabase.getInstance().getReference(RIDERS_PATH+"/"+UID+"/pending/");
        query.removeValue();
        reservationDialog.dismiss();
    });

        reservationDialog.setView(view);
        reservationDialog.setTitle("Confirm Orders?");

        reservationDialog.show();
    }

    public void deliveredOrder(){

        AlertDialog reservationDialog = new AlertDialog.Builder(this.getContext()).create();
        LayoutInflater inflater = LayoutInflater.from(this.getContext());
        final View view = inflater.inflate(R.layout.reservation_dialog, null);


        view.findViewById(R.id.button_confirm).setOnClickListener(e ->{
            DatabaseReference query = FirebaseDatabase.getInstance().getReference(RIDERS_PATH+"/"+UID+"/delivering/");
            query.removeValue();
            reservationDialog.dismiss();
        });

        view.findViewById(R.id.button_cancel).setOnClickListener(e ->{
            reservationDialog.dismiss();
        });

        reservationDialog.setView(view);
        reservationDialog.setTitle("Order delivered?");

        reservationDialog.show();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapter_pending.startListening();
    }
    @Override
    public void onStop() {
        super.onStop();
        mAdapter_pending.stopListening();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

}
