package com.mad.appetit;

import static com.mad.lib.SharedClass.*;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    private String email;
    private String password;
    private String errMsg = " ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseAuth auth = FirebaseAuth.getInstance();

        findViewById(R.id.button).setOnClickListener(e -> {
            if(checkFields()){
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, task -> {
                            if (task.isSuccessful()) {
                                ROOT_UID = auth.getUid();

                                Intent i = new Intent();
                                setResult(1, i);

                                finish();
                            } else {
                                Toast.makeText(Login.this,"Wrong Username or Password", Toast.LENGTH_LONG).show();
                            }
                        });
            }
            else{
                Toast.makeText(Login.this, errMsg, Toast.LENGTH_LONG).show();
            }
        });
    }

    public boolean checkFields(){
        email = ((EditText)findViewById(R.id.mail)).getText().toString();
        password = ((EditText)findViewById(R.id.psw)).getText().toString();

        if(email.trim().length() == 0 || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            errMsg = "Invalid Mail";
            return false;
        }

        if(password.trim().length() == 0){
            errMsg = "Fill password";
            return false;
        }

        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putString(Name, ((EditText)findViewById(R.id.mail)).getText().toString());
        savedInstanceState.putString(Password, ((EditText)findViewById(R.id.psw)).getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        ((EditText)findViewById(R.id.mail)).setText(savedInstanceState.getString(Name));
        ((EditText)findViewById(R.id.psw)).setText(savedInstanceState.getString(Password));
    }
}