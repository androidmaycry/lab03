package com.mad.appetit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DailyOffer#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DailyOffer extends Fragment {
    private static final String MyOFFER = "Daily_Offer";
    private static boolean OnStartup = false;

    private RecyclerView recyclerView;
    private RecyclerAdapterDish mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private OnFragmentInteractionListener mListener;

    private static final String Name = "keyName";
    private static final String Description = "keyDescription";
    private static final String Price = "keyEuroPrice";
    private static final String Photo ="keyPhotoPath";
    private static final String Quantity = "keyQuantity";
    private static final String NItem = "NItemKey";
    private static ArrayList<DailyOfferItem> dataList;
    private SharedPreferences dishes_data;

    public DailyOffer() {
        // Required empty public constructor
    }

    public static DailyOffer newInstance() {
        DailyOffer fragment = new DailyOffer();
        Bundle args = new Bundle();

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dailyoffer, container, false);

        getData();

        mAdapter = new RecyclerAdapterDish(getContext(), dataList,this);
        layoutManager = new LinearLayoutManager(getContext());

        recyclerView = view.findViewById(R.id.dish_list);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(layoutManager);

        OnStartup = true;

        return view;
    }

    public void editDailyOffer(int pos){
        Log.d("DailyOffer: ", "edit()");

        Intent editOffer = new Intent(getContext(), EditOffer.class);
        editOffer.putExtra("Existing", pos);
        startActivityForResult(editOffer, 0);
    }

    private void getData(){
        SharedPreferences offer_data = Objects.requireNonNull(this.getActivity()).getSharedPreferences(MyOFFER, MODE_PRIVATE);

        Integer num = 0;

        String name;
        String desc;
        String photoPath;
        float price;
        int quantity;

        dataList = new ArrayList<>();
        dishes_data = getContext().getSharedPreferences("dishes",0);
        while(( name = dishes_data.getString("Name " + num.toString(), null)) != null){
            desc = dishes_data.getString("Desc " + num.toString(), null);
            photoPath = dishes_data.getString("Photo " + num.toString(), null);
            price = dishes_data.getFloat("Price " + num.toString(), 0);
            quantity = dishes_data.getInt("Quantity " + num.toString(), 0);
            DailyOfferItem i = new DailyOfferItem(name,desc,price,quantity,photoPath);
            dataList.add(i);
            num++;
        }
    }

    public void deleteDish(int pos){

        AlertDialog reservationDialog = new AlertDialog.Builder(this.getContext()).create();
        LayoutInflater inflater = LayoutInflater.from(this.getContext());
        final View view = inflater.inflate(R.layout.reservation_dialog, null);

        view.findViewById(R.id.button_confirm).setOnClickListener(e ->{

            SharedPreferences offer_data = getContext().getSharedPreferences(MyOFFER, MODE_PRIVATE);
            SharedPreferences.Editor editor = offer_data.edit();
            editor.putString(Integer.toString(pos), "null");
            editor.commit();

            dataList.remove(pos);
            mAdapter.notifyItemRemoved(pos);

            reservationDialog.dismiss();
        });
        view.findViewById(R.id.button_cancel).setOnClickListener(e ->{
            reservationDialog.dismiss();
        });

        reservationDialog.setView(view);
        reservationDialog.setTitle("Delete Dish?");

        reservationDialog.show();
    }
    public void saveData(){
        Integer num = 0;

        dishes_data = getContext().getSharedPreferences("dishes",0);
        SharedPreferences.Editor editor = dishes_data.edit();
        editor.clear().apply();

        ArrayList<DailyOfferItem> temp = dataList;
        for(DailyOfferItem i : temp){
            editor.putString("Name " + num.toString(), i.getName());
            editor.putString("Desc " + num.toString(), i.getDesc());
            editor.putString("Photo " + num.toString(), i.getPhotoPath());
            editor.putFloat("Price " + num.toString(), i.getPrice());
            editor.putInt("Quantity "  + num.toString(), i.getQuantity());
            num++;
        }

        editor.apply();
    }

    @Override
    public void onResume() {
        super.onResume();
        saveData();
    }

    @Override
    public void onPause() {
        super.onPause();
        saveData();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_daily, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.add:
                Intent edit_profile = new Intent(getContext(), EditOffer.class);
                startActivityForResult(edit_profile, 0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        super.onActivityResult(requestCode, resultCode, data);

        if(data != null && resultCode == 1){
            getIntentData(data, false);
            //mAdapter.notifyDataSetChanged();
            mAdapter.notifyItemInserted(dataList.size());
        }
        else if(data != null && resultCode == 2){
            getIntentData(data, true);
            mAdapter.notifyDataSetChanged();
            //mAdapter.notifyItemChanged(data.getIntExtra("EDIT_PHOTO_POS", 0));
        }
    }

    public void getIntentData(Intent data, boolean editing){
        DailyOfferItem item = new DailyOfferItem();

        item.setName(data.getStringExtra(Name));
        item.setDesc(data.getStringExtra(Description));
        item.setPrice(data.getFloatExtra(Price, 0));
        item.setQuantity(data.getIntExtra(Quantity, 0));
        item.setPhotoPath(data.getStringExtra(Photo));

        if(editing){
            Log.d("DELETING: ", "Item n " + data.getIntExtra("EDIT_PHOTO_POS", 0));
            dataList.set(data.getIntExtra("EDIT_PHOTO_POS", 0), item);
        }
        else
            dataList.add(item);
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}