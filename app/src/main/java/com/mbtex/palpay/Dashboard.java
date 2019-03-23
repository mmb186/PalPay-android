package com.mbtex.palpay;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

import com.mbtex.palpay.ApiManager.TabApiManager;
import com.mbtex.palpay.ApiManager.VolleyCallBack;
import com.mbtex.palpay.Tabs.Tab;
import com.mbtex.palpay.Tabs.TabRecyclerViewAdapter;
import com.mbtex.palpay.User.User;

import java.util.ArrayList;

public class Dashboard extends AppCompatActivity {
    private static final String TAG = "Dashboard";
    private ArrayList<Tab> my_tabs = new ArrayList<>();
    private User current_user;

//    TODO: Create Summary Chart!
    private double _balance;


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
    }

    private void addTabsToTabList() {
        Log.d(TAG, "initiateRecyclerViewTabs: Initiating List View Items");
        TabApiManager.getTabApiManager(this.getApplicationContext()).
                get_user_tabs(
                        current_user,
                        Dashboard.this,
                        my_tabs,
                        new InitiateRecyclerViewCommand()
                );
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init recyclerView");

        RecyclerView recyclerView = findViewById(R.id.tabs_recycler_view);
        TabRecyclerViewAdapter adapter = new TabRecyclerViewAdapter(this, my_tabs);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    class InitiateRecyclerViewCommand implements VolleyCallBack {
        @Override
        public void onSuccessCallBack(String... args) {
            initRecyclerView();
            enableSwipe();
        }
    }

    public void enableSwipe() {
        ItemTouchHelper.SimpleCallback tabTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int swipeDirection) {
                int position = viewHolder.getAdapterPosition();

                if (swipeDirection == ItemTouchHelper.LEFT )
                {
//                    final Model tabToDelete =
                }

            }
        };

    }


}
