package com.mbtex.palpay;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mbtex.palpay.User.User;

public class CreateTab extends AppCompatActivity {
    private User current_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_tab);

        current_user = getIntent().getExtras().getParcelable("current_user");
    }
}
