package com.mad.customer;

import com.mad.lib.User;
import com.mad.lib.SharedClass.*;

import com.mad.lib.OrderItem;
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

import static com.mad.lib.SharedClass.ACCEPTED_ORDER_PATH;
import static com.mad.lib.SharedClass.user;

public class Confirm extends AppCompatActivity {

    private ArrayList<String> removed = new ArrayList<>();
    private String time = "";
    private String tot;
    private String restAddr;
    private String photo;
    private ArrayList<String> keys;
    private ArrayList<String> names;
    private ArrayList<String> prices;
    private ArrayList<String> nums;
    private String key;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);

        getIncomingIntent();

        findViewById(R.id.confirm_order_button).setOnClickListener(e->{
            time = ((EditText)findViewById(R.id.time_edit)).getText().toString();
            if(time.trim().length() > 0){

                //TODO controlla formato orario
                DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(ACCEPTED_ORDER_PATH);
                HashMap<String, Object> orderMap = new HashMap<>();

                orderMap.put(myRef.push().getKey(), new OrderItem(user.getName(), user.getAddr(), restAddr, user.getPhone(), time,tot, photo, names));
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
    }

    private void getIncomingIntent (){
        keys = getIntent().getStringArrayListExtra("keys");
        names = getIntent().getStringArrayListExtra("names");
        prices = getIntent().getStringArrayListExtra("prices");
        nums = getIntent().getStringArrayListExtra("nums");
        key = getIntent().getStringExtra("key");
        restAddr = getIntent().getStringExtra("raddr");
        photo = getIntent().getStringExtra("photo");

        recyclerView = findViewById(R.id.dish_conf_recyclerview);
        mAdapter = new ConfirmRecyclerAdapter(this, names, prices, nums, Confirm.this);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(layoutManager);

        tot =calcoloTotale(prices, nums);
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
        tot =calcoloTotale(prices, nums);
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
