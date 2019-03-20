package com.mbtex.palpay.Tabs;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mbtex.palpay.R;

import org.fabiomsr.moneytextview.MoneyTextView;

import java.util.ArrayList;

public class TabRecyclerViewAdapter extends RecyclerView.Adapter<TabRecyclerViewAdapter.ViewHolder>{
    private static final String TAG = "TabRecyclerViewAdapter";

    private ArrayList<Tab> tabs = new ArrayList<>();
    private Context ctx;

    public TabRecyclerViewAdapter(Context ctx, ArrayList<Tab> tabs) {
        this.tabs = tabs;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from (viewGroup.getContext()).inflate(R.layout.layout_tab_listitem, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        Log.d(TAG, "onBindViewHolder called.");

        viewHolder.tab_name.setText(tabs.get(position).getName());
        viewHolder.tab_status.setText(tabs.get(position).getStatus());
        viewHolder.tab_balance.setAmount(tabs.get(position).getBalance());

        viewHolder.tab_parent_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clickedON: " + tabs.get(position).getName());

                Toast.makeText(
                    ctx,
                    tabs.get(position).getName() + " " + Integer.toString(tabs.get(position).getId()),
                    Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return tabs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tab_name;
        TextView tab_status;
        MoneyTextView tab_balance;
        RelativeLayout tab_parent_layout;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tab_name = itemView.findViewById(R.id.tab_name);
            tab_status = itemView.findViewById(R.id.tab_status);
            tab_balance = itemView.findViewById(R.id.tab_balance);
            tab_parent_layout = itemView.findViewById(R.id.tab_parent_layout);


        }
    }
}
