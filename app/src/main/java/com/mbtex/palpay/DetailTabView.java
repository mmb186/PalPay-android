package com.mbtex.palpay;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.ToggleButton;

import android.widget.Toast;
import com.mbtex.palpay.ApiManager.TabApiManager;
import com.mbtex.palpay.ApiManager.VolleyCallBack;
import com.mbtex.palpay.Helper.GeneralHelpers;
import com.mbtex.palpay.Tabs.Tab;
import com.mbtex.palpay.Tabs.TabTransaction;
import com.mbtex.palpay.Tabs.TabTransactionRecyclerViewAdapter;
import com.mbtex.palpay.User.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import org.json.JSONObject;

public class DetailTabView extends AppCompatActivity {

    private static final String TAG = "DetailTabView";
    User current_user;
    Dialog myDialog;
    private ArrayList<TabTransaction> tab_transactions = new ArrayList<>();
    private ArrayList<String> imgURLS = new ArrayList<>();
    int _tabId;

    protected void submit_transaction(double amount, String transaction_type){
        JSONObject new_transaction_data = new JSONObject();
        try {
            new_transaction_data.put("transaction_type", transaction_type);
            new_transaction_data.put("amount", amount);
        }
        catch(Exception e){}
    }

    private void registerClickListeners() {
        Log.d(TAG, "registerClickListeners: Register click listeners");
        final DetailTabView localContext = this;
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.btn_new_transaction);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_transaction_popup(view);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: On click Listeners");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tab_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        current_user = getIntent().getExtras().getParcelable("current_user");
        this._tabId = getIntent().getExtras().getInt("tab_id");

        registerClickListeners();
        myDialog = new Dialog(this);

        initTransactionList();
    }

    @Override
    protected void onStart() {
        super.onStart();
//        addTabTransactionsToTransactionList();
    }

    private void addTabTransactionsToTransactionList() {
        Log.d(TAG, "addTabTransactionsToTransactionList: Getting TabAPIManager");

        TabApiManager.getTabApiManager(this.getApplicationContext())
                .get_tab_transactions(
                        current_user,
                        this._tabId,
                        tab_transactions,
                        new InitiateRecyclerViewCommand()
                );
    }


    private void initTransactionList() {
        TEAMPprepareImages();
        TEMPcreateDummyTransaction();
        Log.d(TAG, "initTransactionList: ");
        initRecyclerView();


    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init recyclerview.");
        RecyclerView recyclerView = findViewById(R.id.detail_tab_recycler_view);
        TabTransactionRecyclerViewAdapter adapter = new TabTransactionRecyclerViewAdapter(
                getApplication(),
                tab_transactions,
                current_user);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        initiateSwipeFunctionality(adapter, recyclerView);
    }

    private void initiateSwipeFunctionality(final TabTransactionRecyclerViewAdapter adapter, final RecyclerView recyclerView) {
        ItemTouchHelper.SimpleCallback tabTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
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
                String new_tab_status = "";


                if (transaction_swiped.getStatus().equals(TabTransaction.PENDING))
                {
                    Log.d(TAG, "onSwiped: Updated Tab " + transaction_swiped.get_id() + " status");

                    if (swipeDirection == ItemTouchHelper.LEFT)
                    {
                        // Declining Tab
                        new_tab_status = TabTransaction.DECLINED;
                        Log.d(TAG, "onSwiped: SWIPED LEFT");
                        adapter.removeTransaction(position);
                        adapter.notifyItemRemoved(position);
                        Toast.makeText(DetailTabView.this, "SWIPED LEFT", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        // approving Tab
                        new_tab_status = TabTransaction.APPROVED;
                        Log.d(TAG, "onSwiped: Swiped Right");
                        Toast.makeText(DetailTabView.this, "SWIPED RIGHT", Toast.LENGTH_SHORT).show();
                        transaction_swiped.updateTransactionStatus(new_tab_status);

                        adapter.notifyItemChanged(position);

                    }
                    updateAPIWithNewTransactionStatus(transaction_swiped.get_id(), transaction_swiped.getUserTransactionStatus());

                } else {
                    if (swipeDirection == ItemTouchHelper.LEFT)
                        Toast.makeText(DetailTabView.this, "Cannot Delete Active Tab.", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(DetailTabView.this, "Tab is Already Active.", Toast.LENGTH_SHORT).show();

                    adapter.notifyItemChanged(position);
                }
            }
        };

        ItemTouchHelper tabTouchHelper = new ItemTouchHelper(tabTouchCallback);
        tabTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void updateAPIWithNewTransactionStatus(int id, String status) {
        Log.d(TAG, "updateAPIWithNewTransactionStatus: Updated Status" + status);
        JSONObject tabTransactionStatusData = new JSONObject();

        try {
            tabTransactionStatusData.put("transaction_id", id);
            tabTransactionStatusData.put("status", status);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        TabApiManager.getTabApiManager(getApplicationContext())
                .updateTransactionStatus(tabTransactionStatusData, current_user, DetailTabView.this);

        // TODO: API CALL FOR STATUS CHANGE
    }

    private void TEMPcreateDummyTransaction() {
        for (int i = 0; i< imgURLS.size(); i ++ ) {
            tab_transactions.add(new TabTransaction(
                    imgURLS.get(i),
                    "Username",
                    "PENDING",
                    "PENDING",
                    "25/3/2019",
                    "B",
                    (float) 18.75,
                    45 ));
        }
    }

    private void TEAMPprepareImages() {
        imgURLS.add("https://c7.uihere.com/files/348/800/890/computer-icons-avatar-user-login-avatar.jpg");
        imgURLS.add("http://www.epsomps.vic.edu.au/wp-content/uploads/2016/09/512x512-1-300x300.png");
        imgURLS.add("data:image/png;base64,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");
        imgURLS.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTraWBXv4yZXxfZOS8llQbEllO9LIPga92NJHc4iUxRmYHh9NG-");
        imgURLS.add("http://shinobi-software.com/images/geek.png");
    }

    class InitiateRecyclerViewCommand implements VolleyCallBack {
        @Override
        public void onSuccessCallBack(String... args) {
            initRecyclerView();
        }
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
                        myDialog.dismiss();
                    }
                }
                catch (Exception e){}
            }
        });
        myDialog.show();
    }
}
