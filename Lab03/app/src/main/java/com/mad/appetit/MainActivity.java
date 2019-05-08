package com.mad.appetit;

import static  com.mad.lib.SharedClass.ROOT_UID;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseAuth auth = FirebaseAuth.getInstance();

        if(auth.getCurrentUser() == null){

            findViewById(R.id.login).setOnClickListener(e -> {
                Intent login = new Intent(this, Login.class);
                startActivityForResult(login, 1);
            });

            findViewById(R.id.sigin).setOnClickListener(h -> {
                Intent signin = new Intent(this, SignIn.class);
                startActivityForResult(signin, 1);
            });
        }
        else{
            ROOT_UID = auth.getCurrentUser().getUid();

            Intent fragment = new Intent(this, FragmentManager.class);
            startActivity(fragment);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data != null && resultCode == 1){
            Intent fragment = new Intent(this, FragmentManager.class);
            startActivity(fragment);
            finish();
        }
    }
}