package com.mbtex.palpay;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.mbtex.palpay.User.User;

import org.json.JSONObject;

public class DetailTabView extends AppCompatActivity {
    Dialog myDialog;
    User current_user;

    protected void submit_transaction(double amount, String transaction_type){
        JSONObject new_transaction_data = new JSONObject();
        try {
            new_transaction_data.put("transaction_type", transaction_type);
            new_transaction_data.put("amount", amount);
        }
        catch(Exception e){}
    }

    private void registerClickListeners() {
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tab_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        current_user = getIntent().getExtras().getParcelable("current_user");
        int tab_id = getIntent().getExtras().getInt("tab_id");

        registerClickListeners();

        User current_user;
        myDialog = new Dialog(this);
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
