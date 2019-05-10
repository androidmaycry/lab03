package com.mad.customer;

import android.os.Bundle;



import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static com.mad.lib.SharedClass.ROOT_UID;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseAuth auth = FirebaseAuth.getInstance();

        TextView email = findViewById(R.id.email);
        TextView password = findViewById(R.id.password);

        findViewById(R.id.sign_up).setOnClickListener(e -> {
            Intent i = new Intent(this, SignUp.class);
            startActivityForResult(i,1);
        });

        if(auth.getCurrentUser()!= null){
            ROOT_UID = auth.getUid();

            Intent i = new Intent(MainActivity.this,NavApp.class);
            startActivity(i);

            finish();
        }

        findViewById(R.id.sign_in).setOnClickListener(e -> {
            auth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("success", "signInWithEmail:success");
                            ROOT_UID = auth.getUid();

                            Intent i = new Intent(MainActivity.this,NavApp.class);
                            i.putExtra("ROOT_UID",auth.getUid());
                            startActivityForResult(i,10);

                            finish();
                        } else {
                            Toast.makeText(MainActivity.this,"Wrong Username or Password",Toast.LENGTH_LONG).show();
                        }
                    });
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == 1){
            Intent fragment = new Intent(this, NavApp.class);
            startActivity(fragment);
            finish();
        }
    }
}


