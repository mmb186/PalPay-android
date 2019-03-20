package com.mbtex.palpay;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.mbtex.palpay.Tabs.Tab;
import com.mbtex.palpay.Tabs.TabRecyclerViewAdapter;
import com.mbtex.palpay.User.User;

import java.util.ArrayList;

public class Dashboard extends AppCompatActivity {
    private static final String TAG = "Dashboard";
    private ArrayList<Tab> my_tabs = new ArrayList<>();
    private User current_user;


    private void registerClickListeners() {
        final Dashboard localContext = this;
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG) .setAction("Action", null).show();
                startActivity(current_user.createIntentAndAddSelf(localContext, CreateTab.class));
            }
        });
    }

    public void handleNewTabClick(View view) {

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.dashboard_toolbar);
        setSupportActionBar(toolbar);

        current_user = getIntent().getExtras().getParcelable("current_user");

        registerClickListeners();
        Log.d(TAG, "onCreate: Registered Click Listeners");
        addTabsToTabList();
        initRecyclerView();
    }

    private void addTabsToTabList() {
        Log.d(TAG, "initiateRecyclerViewTabs: Initiating List View Items");

        my_tabs.add(new Tab("Grocery", "ACTIVE", 32f, 1));
        my_tabs.add(new Tab("Roommates", "ACTIVE", 45.2f,2));
        my_tabs.add(new Tab("Johnny", "ACTIVE", 15.4f,3));
        my_tabs.add(new Tab("Test", "PENDING", 105.32f,4));
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init recyclerView");

        RecyclerView recyclerView = findViewById(R.id.tabs_recycler_view);
        TabRecyclerViewAdapter adapter = new TabRecyclerViewAdapter(this, my_tabs);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

}
