package com.mbtex.palpay;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.mbtex.palpay.ApiManager.TabApiManager;
import com.mbtex.palpay.ApiManager.VolleyCallBack;
import com.mbtex.palpay.Helper.LocalActivityState;
import com.mbtex.palpay.Helper.GeneralHelpers;
import com.mbtex.palpay.Tabs.TabTransaction;
import com.mbtex.palpay.Tabs.TabTransactionRecyclerViewAdapter;
import com.mbtex.palpay.User.User;

import org.fabiomsr.moneytextview.MoneyTextView;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class DetailTabView extends AppCompatActivity {



    private static final String TAG = "DetailTabView";
    User current_user;
    Dialog myDialog;
    private ArrayList<TabTransaction> tab_transactions = new ArrayList<>();
    private ArrayList<String> imgURLS = new ArrayList<>();
    int _tabId;
    FloatingActionButton fab;
    DetailTabViewState localTabState;
    SwipeRefreshLayout refereshLayout;

    class  DetailTabViewState extends LocalActivityState {
        private String tabName;
        private float tabBalance;

        public DetailTabViewState(String tabName, float tabBalance) {
            super();
            this.tabName = tabName;
            this.tabBalance = tabBalance;
        }

        @Override
        public void updateState(Object... arguments) {
            this.tabName = (String) arguments[0];
            this.tabBalance = (float) arguments[1];
        }

        public String getTabName() {
            return tabName;
        }

        public float getTabBalance() {
            return tabBalance;
        }
    }

    private void registerClickListeners() {
        Log.d(TAG, "registerClickListeners: Register click listeners");
        final DetailTabView localContext = this;
        this.fab = (FloatingActionButton) findViewById(R.id.btn_new_transaction);
        this.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_transaction_popup(view);
            }
        });

        refereshLayout = findViewById(R.id.detail_tab_list_wipe_container);
        refereshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onPostResume();
            }
        });


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: On click Listeners");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tab_view);


        current_user = getIntent().getExtras().getParcelable("current_user");
        this._tabId = getIntent().getExtras().getInt("tab_id");

        localTabState = new DetailTabViewState("", 0.0f);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPostResume() {

        setContentView(R.layout.activity_detail_tab_view);
        registerClickListeners();
        myDialog = new Dialog(this);
        addTabTransactionsToTransactionList();

        super.onPostResume();
    }

    private void addTabTransactionsToTransactionList() {
        Log.d(TAG, "addTabTransactionsToTransactionList: Getting TabAPIManager");

        TabApiManager.getTabApiManager(this.getApplicationContext())
                .get_tab_transactions(
                        current_user,
                        this._tabId,
                        tab_transactions,
                        new InitiateRecyclerViewCommand(),
                        localTabState
                );
    }

    private void initTransactionList() {
        Log.d(TAG, "initTransactionList: ");
        initRecyclerView();
    }


    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init RecyclerView.");
        RecyclerView recyclerView = findViewById(R.id.detail_tab_recycler_view);
        final TabTransactionRecyclerViewAdapter adapter = new TabTransactionRecyclerViewAdapter(
                getApplication(),
                tab_transactions,
                current_user);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ItemTouchHelper.SimpleCallback tabTransactionTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            private static final float ALPHA_FULL = 1.0f;
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
                TabTransaction transaction_swiped = adapter.getTabTransaction(position);
                String new_transaction_status = "";

                if (transaction_swiped.getUserTransactionStatus().equals(TabTransaction.PENDING))
                {
                    Log.d(TAG, "onSwiped: Updated Tab " + transaction_swiped.getTransactionId() + " status");

                    if (swipeDirection == ItemTouchHelper.LEFT)
                    {
                        // Declining Tab
                        new_transaction_status = TabTransaction.DECLINED;
                        Log.d(TAG, "onSwiped: SWIPED LEFT");
                        adapter.removeTransaction(position);
                        adapter.notifyItemRemoved(position);
                        Toast.makeText(DetailTabView.this, "SWIPED LEFT", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        // approving Tab
                        new_transaction_status = TabTransaction.APPROVED;
                        Log.d(TAG, "onSwiped: Swiped Right");
                        Toast.makeText(DetailTabView.this, "SWIPED RIGHT", Toast.LENGTH_SHORT).show();
                        transaction_swiped.updateTransactionStatus(new_transaction_status);
                        adapter.notifyItemChanged(position);
                    }
                    updateAPIWithNewTransactionStatus(transaction_swiped.getTransactionId(), transaction_swiped.getUserTransactionStatus());

                } else {
                    if (swipeDirection == ItemTouchHelper.LEFT)
                        Toast.makeText(DetailTabView.this, "Cannot Decline an approved transaction.", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(DetailTabView.this, "You've already Approved this Transaction.", Toast.LENGTH_SHORT).show();
                    adapter.notifyItemChanged(position);
                }
            }
        };

        ItemTouchHelper tabTouchHelper = new ItemTouchHelper(tabTransactionTouchCallback);
        tabTouchHelper.attachToRecyclerView(null);
        tabTouchHelper.attachToRecyclerView(recyclerView);

        updateTabInfo();
    }

    private void updateAPIWithNewTransactionStatus(int id, String status) {
        Log.d(TAG, "updateAPIWithNewTransactionStatus: Updated Status" + status);
        JSONObject tabTransactionStatusData = new JSONObject();

        try {
            tabTransactionStatusData.put("tab_transaction_id", Integer.toString(id));
            tabTransactionStatusData.put("tab_transaction_status", status);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        TabApiManager.getTabApiManager(getApplicationContext())
                .updateTransactionStatus(tabTransactionStatusData, current_user, DetailTabView.this);

    }


    public void show_transaction_popup(View v) {
        TextView txtClose;
        final ToggleButton btnToggle;
        final EditText editAmount;
        Button btnConfirm;
        myDialog.setContentView(R.layout.create_transaction_popup);
        txtClose = (TextView) myDialog.findViewById(R.id.txtClose);
        btnToggle = (ToggleButton) myDialog.findViewById(R.id.btnPayLoan);
        editAmount = (EditText) myDialog.findViewById(R.id.txtAmount);
        btnConfirm = (Button) myDialog.findViewById(R.id.btnConfirm);
        txtClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        btnToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    isChecked = false; //false means loan or withdrawal
                }
                else{
                    isChecked = true;   //if true it means they are making a payment or deposit
                }
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String amount = editAmount.getText().toString();
                String type;
                double dubAmount;
                if(btnToggle.isChecked()){
                    type = "DEPOSIT";
                }
                else{
                    type = "WITHDRAW";
                }

                try{
                    dubAmount = Double.parseDouble(amount);
                    if (dubAmount >= 1.0) {
                        submit_transaction(dubAmount, type);
                    } else
                        Toast.makeText(DetailTabView.this, "Transaction amount is too small", Toast.LENGTH_SHORT).show();
                }
                catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(DetailTabView.this, "Amount Must be a number", Toast.LENGTH_SHORT).show();
                }
            }
        });
        myDialog.show();
    }

    protected void submit_transaction(double amount, String transaction_type){
        JSONObject new_transaction_data = new JSONObject();
        try {
            new_transaction_data.put("transaction_type", transaction_type);
            new_transaction_data.put("amount", amount);
            new_transaction_data.put("tab_id", this._tabId);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        TabApiManager.getTabApiManager(getApplicationContext())
                .createNewTabTransaction(new_transaction_data,
                        current_user,
                        DetailTabView.this,
                        new ReloadTransactionListCallback());
        myDialog.dismiss();
    }

    class InitiateRecyclerViewCommand implements VolleyCallBack {
        @Override
        public void onSuccessCallBack(String... args) {
            initRecyclerView();
        }
    }

    private void updateTabInfo() {
        TextView tabName = (TextView) findViewById(R.id.detail_tab_name);
        MoneyTextView tabBalance = (MoneyTextView) findViewById(R.id.tab_view_balance);

        tabName.setText(localTabState.getTabName());
        tabBalance.setAmount(localTabState.getTabBalance());
    }


    class ReloadTransactionListCallback implements VolleyCallBack {
        @Override
        public void onSuccessCallBack(String... args) {
            onPostResume();
        }
    }
}
