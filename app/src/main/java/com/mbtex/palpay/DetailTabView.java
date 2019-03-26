package com.mbtex.palpay;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ToggleButton;

import com.mbtex.palpay.User.User;

public class DetailTabView extends AppCompatActivity {
    User current_user;

    private void registerClickListeners() {
        final DetailTabView localContext = this;
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tab_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        current_user = getIntent().getExtras().getParcelable("current_user");
        int tab_id = getIntent().getExtras().getInt("tab_id");

    }

    public void newTransactionPopup(View v) {
        TextView txtClose;
        ToggleButton btnToggle;
        EditText editAmount;
        Button btnConfirm;
        boolean pay = true;
        myDialog.setContentView(R.layout.create_transaction_popup);
        txtClose = (TextView) myDialog.findViewByID(R.id.txtClose);
        btnToggle = (ToggleButton) myDialog.findViewByID(R.id.btnPayLoan);
        editAmount = (EditText) myDialog.findViewByID(R.id.txtAmount);
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
                    isChecked = false;
                    pay = false;
                }
                else{
                    isChecked = true;
                    pay = true;
                }
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if(submit_transaction()) {
                    myDialog.dismiss();
                }
            }
        }
    }
}
