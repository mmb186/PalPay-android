package com.mbtex.palpay;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.mbtex.palpay.ApiManager.TabApiManager;
import com.mbtex.palpay.User.User;

import org.json.JSONException;
import org.json.JSONObject;

public class CreateTab extends AppCompatActivity {
    private static final String TAG = "CreateTab";
    private User current_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_tab);

        current_user = getIntent().getExtras().getParcelable("current_user");

        registerClickListeners();
    }

    private CreateTab getCurrentActivity()
    {
        return this;
    }

    private Context getCtx() {
        return this.getApplicationContext();
    }

    private void registerClickListeners()
    {
        Log.d(TAG, "registerClickListeners: Registering Listerners");
        final Button createTabButton = (Button) findViewById(R.id.create_tab_button);

        createTabButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                JSONObject tabData = new JSONObject();

                addCreateTabData(tabData);

                TabApiManager.getTabApiManager(getCtx()).
                        create_new_tab(tabData, current_user, getCurrentActivity());
            }

            private void addCreateTabData(JSONObject data) {
                EditText tab_name = (EditText) findViewById(R.id.new_tab_name);
                EditText other_user = (EditText) findViewById(R.id.tab_other_user);

                try {
                    data.put("otheruser", other_user.getText().toString());
                    data.put("name", tab_name.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
