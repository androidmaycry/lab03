package com.mad.customer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.HashMap;

public class Confirm extends AppCompatActivity {

    private ArrayList<String> removed = new ArrayList<>();
    private String time = "";
    ArrayList<String> keys;
    ArrayList<String> names;
    ArrayList<String> prices;
    ArrayList<String> nums;
    String key;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);

        findViewById(R.id.confirm_order_button).setOnClickListener(e->{
            time = ((EditText)findViewById(R.id.time_edit)).getText().toString();
            if(time.trim().length() > 0){
                DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("order");
                HashMap<String, Object> orderMap = new HashMap<>();

                orderMap.put(myRef.push().getKey(), new DishItem(names.get(0), "", Float.parseFloat(prices.get(0)), Integer.parseInt(nums.get(0)), ""));
                myRef.updateChildren(orderMap);

                setResult(1);
                finish();
            }
            else
                Toast.makeText(this, "Please select desired time", Toast.LENGTH_LONG).show();
        });
        findViewById(R.id.back_order_button).setOnClickListener(w->{
            setRemovedItem();
            finish();
        });
        getIncomingIntent();
    }

    private void getIncomingIntent (){
        keys = getIntent().getStringArrayListExtra("keys");
        names = getIntent().getStringArrayListExtra("names");
        prices = getIntent().getStringArrayListExtra("prices");
        nums = getIntent().getStringArrayListExtra("nums");
        key = getIntent().getStringExtra("key");

        recyclerView = findViewById(R.id.dish_conf_recyclerview);
        mAdapter = new ConfirmRecyclerAdapter(this, names, prices, nums, Confirm.this);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(layoutManager);

        String tot =calcoloTotale(prices, nums);
        TextView totale = findViewById(R.id.totale);
        totale.setText(tot + " €");
    }
    private String calcoloTotale (ArrayList<String> prices, ArrayList<String> nums){
        float tot=0;
        for(int i=0; i<prices.size(); i++){
            float price = Float.parseFloat(prices.get(i));
            float num = Float.parseFloat(nums.get(i));
            tot=tot+(price*num);
        }
        return Float.toString(tot);
    }
    public void deleteItem (int index){
        keys.remove(index);
        removed.add(Integer.toString(index));
        String tot =calcoloTotale(prices, nums);
        TextView totale = findViewById(R.id.totale);
        totale.setText(tot + " €");
        if (Float.parseFloat(tot) == 0){
            setRemovedItem();
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        setRemovedItem();
        finish();
    }

    public void setRemovedItem(){
        Intent intent = new Intent();
        intent.putStringArrayListExtra("removed", removed);
        setResult(0, intent);
    }
}
