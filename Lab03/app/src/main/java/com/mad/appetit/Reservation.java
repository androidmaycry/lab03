package com.mad.appetit;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Vector;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Reservation.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Reservation#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Reservation extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView,recyclerView_accepted;
    private RecyclerView.Adapter mAdapter,mAdapter_accepted;
    private RecyclerView.LayoutManager layoutManager;

    private SharedPreferences reservation_data;

    /* FOR SIMULATION */
    String [] names = {"Carlo Negri", "Federico Gianno", "Davide Gallotti", "Marco Longo"};
    String [] addrs = {"Via Luserna di Rora' 14", "Via Luserna di Rora' 14", "Via Cesana 63", "Via Germanasca 12"};
    String [] cells = {"334 8400234", "327 12983405", "334 9072123", "338 76212343"};
    String [] times = {"12:30", "12:34", "12:45", "13:25"};
    Integer [] imgs = {R.drawable.person, R.drawable.person, R.drawable.person, R.drawable.person};

    String [][] order = {   {"ChickenBurger x4","Cotoletta di maiale x3"},
                            {"Pizza Margherita x5"},
                            {"Insalata con mandorle x1", "Pizza Napoli x4"},
                            {"Pasta Amatriciana x1", "Pasta ai 4 formaggi x3"}
                         };

    ArrayList<ReservationItem> items = new ArrayList<ReservationItem>();
    ArrayList<ReservationItem> accepted_items = new ArrayList<ReservationItem>();

    ArrayList [] orders_list = new ArrayList[4];

    private Reservation.OnFragmentInteractionListener mListener;
    private RecyclerAdapterOrdered mAdapter_ordered;
    private RecyclerView recyclerView_ordered;

    public Reservation() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Reservation.
     */
    // TODO: Rename and change types and number of parameters
    public static Reservation newInstance(String param1, String param2) {
        Reservation fragment = new Reservation();
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

        for(int i=0;i<4;i++){
            orders_list[i] = new ArrayList<String>();
            for(String s : order[i]){
                orders_list[i].add(s);
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String name;
        String address;
        String cell;
        String time;
        String s;
        int img;
        Integer num = 0,num_order=0;

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reservation, container, false);

        reservation_data = getContext().getSharedPreferences("reservation_data",0);
        while(( name = reservation_data.getString("Name "+ num.toString(),null)) != null) {

            num_order=0;
            ArrayList<String> order_list = new ArrayList<String>();
            Log.d("NUMBER",name);
            address = reservation_data.getString("Address "+ num.toString(),null);
            cell = reservation_data.getString("Cell "+ num.toString(),null);
            img = reservation_data.getInt("Photo " + num.toString(),0);
            time = reservation_data.getString("Time "+ num.toString(),null);

            while((s = reservation_data.getString("Order " + num.toString() + "," + num_order.toString(),null))!= null){
                order_list.add(s);
                num_order++;
            }

            ReservationItem r = new ReservationItem(name, address, cell, img,time,order_list);
            items.add(r);

            num++;
        }

        num = 0;
        reservation_data = getContext().getSharedPreferences("reservation_data_accepted",0);
        while(( name = reservation_data.getString("Name "+ num.toString(),null)) != null) {

            num_order=0;
            ArrayList<String> order_list = new ArrayList<String>();
            address = reservation_data.getString("Address "+ num.toString(),null);
            cell = reservation_data.getString("Cell "+ num.toString(),null);
            img = reservation_data.getInt("Photo " + num.toString(),0);
            time = reservation_data.getString("Time "+ num.toString(),null);

            while((s = reservation_data.getString("Order " + num.toString() + "," + num_order.toString(),null))!= null){
                order_list.add(s);
                num_order++;
            }

            ReservationItem r = new ReservationItem(name, address, cell, img,time,order_list);
            accepted_items.add(r);
            num++;
        }

        if(num == 0 && items.size() == 0){

                for (int i = 0; i < 4; i++) {
                    ReservationItem r = new ReservationItem(names[i], addrs[i], cells[i], imgs[i],times[i],orders_list[i]);
                    items.add(r);
                }
        }

        recyclerView = view.findViewById(R.id.ordered_list);
        mAdapter = new RecyclerAdapterReservation(getContext(), items,this,0);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(layoutManager);

        Log.d("SHARED",": Okay");

        recyclerView_accepted = view.findViewById(R.id.reservation_list_accepted);
        mAdapter_accepted = new RecyclerAdapterReservation(getContext(), accepted_items,this,1);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView_accepted.setAdapter(mAdapter_accepted);
        recyclerView_accepted.setLayoutManager(layoutManager);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void acceptOrder(int pos){

        AlertDialog reservationDialog = new AlertDialog.Builder(this.getContext()).create();
        LayoutInflater inflater = LayoutInflater.from(this.getContext());
        final View view = inflater.inflate(R.layout.reservation_dialog, null);


        view.findViewById(R.id.button_confirm).setOnClickListener(e ->{
            ReservationItem item = items.remove(pos);
            accepted_items.add(item);
            mAdapter_accepted.notifyItemInserted(accepted_items.size());

            saveState("reservation_data",0);
            saveState("reservation_data_accepted",1);;

            mAdapter.notifyItemRemoved(pos);
            reservationDialog.dismiss();
        });

        view.findViewById(R.id.button_cancel).setOnClickListener(e ->{
            reservationDialog.dismiss();
        });

        reservationDialog.setView(view);
        reservationDialog.setTitle("Confirm Reservation?");

        reservationDialog.show();
    }

    public void removeOrder(int pos){

        AlertDialog reservationDialog = new AlertDialog.Builder(this.getContext()).create();
        LayoutInflater inflater = LayoutInflater.from(this.getContext());
        final View view = inflater.inflate(R.layout.reservation_dialog, null);

        view.findViewById(R.id.button_confirm).setOnClickListener(e ->{
            items.remove(pos);
            mAdapter.notifyItemRemoved(pos);
            saveState("reservation_data",0);
            reservationDialog.dismiss();
        });
        view.findViewById(R.id.button_cancel).setOnClickListener(e ->{
            reservationDialog.dismiss();
        });

        reservationDialog.setView(view);
        reservationDialog.setTitle("Delete Reservation?");

        reservationDialog.show();
    }

    public void viewOrder(int pos,int flag){

        ReservationItem i;
        AlertDialog reservationDialog = new AlertDialog.Builder(this.getContext()).create();
        LayoutInflater inflater = LayoutInflater.from(this.getContext());
        final View view = inflater.inflate(R.layout.dishes_list_dialog, null);

        if(flag == 0){
            i = items.get(pos);
        }
        else{
            i = accepted_items.get(pos);
        }

        recyclerView_ordered = view.findViewById(R.id.ordered_list);
        mAdapter_ordered = new RecyclerAdapterOrdered(reservationDialog.getContext(),i.getOrder());
        layoutManager = new LinearLayoutManager(reservationDialog.getContext());
        recyclerView_ordered.setAdapter(mAdapter_ordered);
        recyclerView_ordered.setLayoutManager(layoutManager);

        view.findViewById(R.id.back).setOnClickListener(e ->{
            reservationDialog.dismiss();
        });

        reservationDialog.setView(view);
        reservationDialog.setTitle("Order");

        reservationDialog.show();

    }

    private void saveState(String nameFile, int flag){

        Integer num = 0,num_order=0;

        reservation_data = getContext().getSharedPreferences(nameFile,0);
        SharedPreferences.Editor editor = reservation_data.edit();

        editor.clear().apply();

        ArrayList<ReservationItem> temp;
        if(flag == 0){
            temp = items;
        }
        else{
            temp = accepted_items;
        }

        for(ReservationItem i : temp){
            num_order = 0;
            editor.putString("Name " + num.toString(), i.getName());
            editor.putString("Address " + num.toString(), i.getAddr());
            editor.putString("Cell " + num.toString(), i.getCell());
            editor.putInt("Photo " + num.toString(), i.getImg());
            editor.putString("Time " + num.toString(), i.getTime());

            ArrayList<String> order = i.getOrder();
            for(String s : order){
                editor.putString("Order "+num.toString()+","+ num_order.toString(),s);
                num_order++;
            }

            num++;
        }

        editor.apply();
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

    public void seeItemList(ReservationItem item){

    }
}
