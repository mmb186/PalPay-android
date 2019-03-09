package com.mbtex.palpay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SignUp extends AppCompatActivity {

    public void registerButtonListeners()
    {
        Log.i("info", "Regsiter sign up Click Listeners");
        final Button signup_button = (Button) findViewById(R.id.sign_up_button);


        final TextView login_redirect = (TextView) findViewById(R.id.log_in_redirect);
        login_redirect.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v){
                startActivity(new Intent(SignUp.this, LoginActivity.class));
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        registerButtonListeners();
    }
}
