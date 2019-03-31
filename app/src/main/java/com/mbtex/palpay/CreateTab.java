package com.mbtex.palpay;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mbtex.palpay.ApiManager.TabApiManager;
import com.mbtex.palpay.User.User;

import org.json.JSONException;
import org.json.JSONObject;


/*
 *   Create Tab activity housing the Create Tab functionality.
 */
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

            }

            private void addCreateTabData(JSONObject data) {
                EditText tab_name = (EditText) findViewById(R.id.new_tab_name);
                EditText other_user = (EditText) findViewById(R.id.tab_other_user);
                String[] users = other_user.getText().toString().split(",");


                // NOTE:
                //      - current implementation checks if we are creating Group Tab or single tab by checking how many users the
                //        Tab creator is adding ot the Tab.
                if (users.length < 1 ) {
                    Toast.makeText(CreateTab.this, "Include other user(s) to create a tab", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Boolean isGroupTab = false;
                    if (users.length > 1 ) {
                        try {
                            data.put("otheruser", other_user.getText().toString());
                            data.put("name", tab_name.getText().toString());
                            data.put("is_group_tab", "true");
                            isGroupTab = true;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                } else
                    {
                        try {
                            data.put("otheruser", other_user.getText().toString());
                            data.put("name", tab_name.getText().toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                TabApiManager.getTabApiManager(getCtx()).
                        create_new_tab(data, current_user, CreateTab.this, isGroupTab);

                }
            }
        });
    }
}
