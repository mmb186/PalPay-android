package com.mbtex.palpay;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mbtex.palpay.ApiManager.AuthorizationApiManager;

import org.json.JSONException;
import org.json.JSONObject;

public class SignUp extends AppCompatActivity {

    Button signup_button;
    EditText username;
    EditText email;
    EditText first_name;
    EditText last_name;
    EditText password;
    EditText confirm_password;


    public Context getCtx() {
        return this.getApplicationContext();
    }

    public void registerButtonListeners()
    {
        Log.i("info", "Register sign up Click Listeners");
        final Button signup_button = (Button) findViewById(R.id.sign_up_button);
        this.getApplicationContext();
        signup_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                JSONObject signUpData = new JSONObject();
                addSignUpFormValues(signUpData);

                AuthorizationApiManager authManager = AuthorizationApiManager.getAuthorizationManager(getCtx());
                authManager.registerUser(signUpData);
            }
        });


        final TextView login_redirect = (TextView) findViewById(R.id.log_in_redirect);
        login_redirect.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v){
                startActivity(new Intent(SignUp.this, LoginActivity.class));
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signup_button = (Button) findViewById(R.id.sign_up_button);
        username = (EditText) findViewById(R.id.username_sign_up);
        email = (EditText) findViewById(R.id.email_sign_up);
        first_name = (EditText) findViewById(R.id.first_name_sign_up);
        last_name = (EditText) findViewById(R.id.last_name_sign_up);
        password = (EditText) findViewById(R.id.password_sign_up);
        confirm_password = (EditText) findViewById(R.id.confirm_password_sign_up);

        registerButtonListeners();
    }

    public void addSignUpFormValues(JSONObject data) {

        try {
            data.put("email", email.getText().toString());
            data.put("username", username.getText().toString());
            data.put("password", password.getText().toString());
            data.put("confirm_password", confirm_password.getText().toString());
            data.put("first_name", first_name.getText().toString());
            data.put("last_name", last_name.getText().toString());
        }
        catch(JSONException e) {
            e.printStackTrace();
        }

    }

}
