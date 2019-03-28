package com.mbtex.palpay;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.mbtex.palpay.ApiManager.TabApiManager;
import com.mbtex.palpay.ApiManager.VolleyCallBack;
import com.mbtex.palpay.Helper.GeneralHelpers;
import com.mbtex.palpay.Helper.LocalActivityState;
import com.mbtex.palpay.Tabs.Tab;
import com.mbtex.palpay.Tabs.TabRecyclerViewAdapter;
import com.mbtex.palpay.User.User;

import org.fabiomsr.moneytextview.MoneyTextView;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class Dashboard extends AppCompatActivity {
    private static final String TAG = "Dashboard";
    private ArrayList<Tab> my_tabs = new ArrayList<>();
    private User current_user;
    private DashboardState localState;

    private double _balance;

    class DashboardState extends LocalActivityState {
        private String userName;
        private float tabBalance;

        public DashboardState(String tabName, float tabBalance) {
            super();
            this.userName = tabName;
            this.tabBalance = tabBalance;
        }

        @Override
        public void updateState(Object... arguments) {
            this.tabBalance = (float) arguments[0];
        }

        public String getUserName() {
            return userName;
        }

        public float getTabBalance() {
            return tabBalance;
        }
    }


    private void registerClickListeners() {
        final Dashboard localContext = this;
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        current_user = getIntent().getExtras().getParcelable("current_user");

        localState = new DashboardState(current_user.getUserName(), 0.0f);

        Log.d(TAG, "onCreate: Registered Click Listeners");
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        setContentView(R.layout.activity_dashboard);

        addTabsToTabList();
        registerClickListeners();
    }

    private void addTabsToTabList() {
        Log.d(TAG, "initiateRecyclerViewTabs: Initiating List View Items");
        TabApiManager.getTabApiManager(this.getApplicationContext()).
                get_user_tabs(
                        current_user,
                        Dashboard.this,
                        my_tabs,
                        new InitiateRecyclerViewCommand(),
                        localState
                );
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init recyclerView");

        RecyclerView recyclerView = findViewById(R.id.tabs_recycler_view);
        final TabRecyclerViewAdapter adapter = new TabRecyclerViewAdapter(this, my_tabs, current_user);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        ItemTouchHelper.SimpleCallback tabTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            public static final float ALPHA_FULL = 0.7f;
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            private int convertDpToPx(int dp) {
                return Math.round(dp * (getResources().getDisplayMetrics().xdpi / DisplayMetrics.DENSITY_DEFAULT));
            }

            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                Log.d(TAG, "onChildDraw: ");
                float SWIPE_RIGHT_TRANSLATION_CONSTANT = 125;
                float SWIPE_LEFT_TRANSLATION_CONSTANT = 50;
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    Log.d(TAG, "onChildDraw: Action is swipe");

                    View itemView = viewHolder.itemView;

                    Paint p = new Paint();
                    Bitmap icon;


                    if (dX  > 0) {
                        /* swiping right () */
                        Log.d(TAG, "onChildDraw: Swipe Right. Accept");
                        p.setARGB(255, 0, 255, 0);


                        // Draw Rectangle
                        c.drawRect((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom(), p);

                        // Draw Icon;
                        icon = GeneralHelpers.getBitmapFromDrawable(getApplicationContext(), R.drawable.ic_check_white_24dp);
                        c.drawBitmap(
                            icon,
                            (float) itemView.getLeft() + convertDpToPx(16) - icon.getWidth() + SWIPE_RIGHT_TRANSLATION_CONSTANT,
                            (float) itemView.getTop() + ((float) itemView.getBottom() - (float) itemView.getTop() - icon.getHeight()) /2,
                            p
                        );
                    } else {
                        /* Swiping Left (Declining) */
                        Log.d(TAG, "onChildDraw: Swipe Left. Decline");

                        // Draw Rectangle
                        p.setARGB(255,255, 0, 0);
                        c.drawRect(
                                (float) itemView.getRight() + dX, (float) itemView.getTop(),
                                (float) itemView.getRight(),
                                (float) itemView.getBottom(),
                                p
                        );

                        // Draw Icon
                        icon = GeneralHelpers.getBitmapFromDrawable(getApplicationContext(), R.drawable.ic_close_white_24dp);
                        float top_pt = (float) itemView.getRight() + convertDpToPx(16) - icon.getWidth() - SWIPE_LEFT_TRANSLATION_CONSTANT;
                        float left_pt = ((float) itemView.getTop() + ( ((float) itemView.getBottom() - (float) itemView.getTop() - icon.getHeight())/2));
                        c.drawBitmap(
                                icon,
                                top_pt,
                                left_pt,
                                p
                        );
                    }

                    // Fade out view after item is swiped out of parent
                    final float alpha = ALPHA_FULL - Math.abs(dX);
                    viewHolder.itemView.setAlpha(alpha);
                    viewHolder.itemView.setTranslationX(dX);
                } else {
                    Log.d(TAG, "onChildDraw: Action is not swipe");
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int swipeDirection) {
                int position = viewHolder.getAdapterPosition();
                Tab tab_swiped = adapter.getTab(position);
                String new_tab_status = "";

                if (tab_swiped.getStatus().equals(Tab.TAB_PENDING))
                {
                    Log.d(TAG, "onSwiped: Updated Tab " + tab_swiped.getName() + "status");

                    if (swipeDirection == ItemTouchHelper.LEFT)
                    {
                    // Declining Tab
                        new_tab_status = Tab.TAB_INACTIVE;
                        Log.d(TAG, "onSwiped: SWIPED LEFT");
                        adapter.removeTab(position);
                        adapter.notifyItemRemoved(position);
                        Toast.makeText(Dashboard.this, "SWIPED LEFT", Toast.LENGTH_SHORT).show();
                    }
                    else
                        {
                    // approving Tab
                        new_tab_status = Tab.TAB_APPROVED;
                        Log.d(TAG, "onSwiped: Swiped Right");
                        Toast.makeText(Dashboard.this, "SWIPED RIGHT", Toast.LENGTH_SHORT).show();
                        tab_swiped.updateStatus(new_tab_status);

                        adapter.notifyItemChanged(position);
                    }
                    updateAPIWithNewStatus(tab_swiped.getId(), tab_swiped.getUserTabStatus());

                } else {
                    if (swipeDirection == ItemTouchHelper.LEFT)
                        Toast.makeText(Dashboard.this, "Cannot Delete Active Tab.", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(Dashboard.this, "Tab is Already Active.", Toast.LENGTH_SHORT).show();
                    adapter.notifyItemChanged(position);
                }
            }
        };

        ItemTouchHelper tabTouchHelper = new ItemTouchHelper(tabTouchCallback);
        tabTouchHelper.attachToRecyclerView(null);
        tabTouchHelper.attachToRecyclerView(recyclerView);
    }

    class InitiateRecyclerViewCommand implements VolleyCallBack {
        @Override
        public void onSuccessCallBack(String... args) {
            updateGlobalTabBalance();
            initRecyclerView();
        }
    }

    private void updateGlobalTabBalance() {
        MoneyTextView globalBalance = (MoneyTextView)  findViewById(R.id.tab_view_balance);
        globalBalance.setAmount(localState.getTabBalance());


//        if ((int) localState.getTabBalance() > 0.0f) {
//            globalBalance.setBaseColor(R.color.positiveBalance);
//            globalBalance.setDecimalsColor(R.color.positiveBalance);
//            globalBalance.setSymbolColor(R.color.positiveBalance);
//        } else if (localState.getTabBalance() < 0.0f ) {
//            globalBalance.setBaseColor(R.color.negativeBalance);
//            globalBalance.setDecimalsColor(R.color.negativeBalance);
//            globalBalance.setSymbolColor(R.color.negativeBalance);
//        }
    }


    void updateAPIWithNewStatus(int tab_id, String newStatus) {
        JSONObject tabData = new JSONObject();

        try {
            tabData.put("tab_id", Integer.toString(tab_id));
            tabData.put("tab_status", newStatus);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        TabApiManager.getTabApiManager(getApplicationContext())
                .update_tab_status(tabData, current_user, Dashboard.this);
    }
}
