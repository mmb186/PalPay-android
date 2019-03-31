package com.mbtex.palpay.Tabs;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mbtex.palpay.DetailTabView;
import com.mbtex.palpay.R;
import com.mbtex.palpay.User.User;

import org.fabiomsr.moneytextview.MoneyTextView;

import java.util.ArrayList;


/**
 * Recycler View Class for User Tabs.
 *      - Defines OnClick Functionality and how to bind dataToView
 */
public class TabRecyclerViewAdapter extends RecyclerView.Adapter<TabRecyclerViewAdapter.ViewHolder>{
    private static final String TAG = "TabRecyclerViewAdapter";

    private ArrayList<Tab> tabs_list = new ArrayList<>();
    private Context ctx;
    User current_user;

    public TabRecyclerViewAdapter(Context ctx, ArrayList<Tab> tabs_list, User current_user) {
        this.tabs_list = tabs_list;
        this.ctx = ctx;
        this.current_user = current_user;
    }

    /*
    * Removes Tab form list when declined
    * */
    public void removeTab(int position) {
        tabs_list.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, tabs_list.size());
    }


    public void onItemDismiss(int position) {
        notifyItemChanged(position);
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

        viewHolder.tab_name.setText(tabs_list.get(position).getName());
        viewHolder.tab_status.setText(tabs_list.get(position).getStatus());
        viewHolder.tab_balance.setAmount(tabs_list.get(position).getBalance());


        // Implementation to change tab balance colour depending on tab standings.
        //      - It currently changes to the wrong colour.
        // TODO: Get this to change to the right colour.
//        if ( tabs_list.get(position).getBalance() > 0.0f) {
//            viewHolder.tab_balance.setBaseColor(R.color.positiveBalance);
//            viewHolder.tab_balance.setDecimalsColor(R.color.positiveBalance);
//            viewHolder.tab_balance.setSymbolColor(R.color.positiveBalance);
//        } else if ( tabs_list.get(position).getBalance() < 0.0f) {
//            viewHolder.tab_balance.setBaseColor(R.color.negativeBalance);
//            viewHolder.tab_balance.setDecimalsColor(R.color.negativeBalance);
//            viewHolder.tab_balance.setSymbolColor(R.color.negativeBalance);
//        }


        viewHolder.tab_parent_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clickedON: " + tabs_list.get(position).getName());


                if (getTab(position).getStatus().equals("ACTIVE")) {
                    Intent intent = current_user.createIntentAndAddSelf((AppCompatActivity) ctx, DetailTabView.class);
                intent.putExtra("tab_id", getTab(position).getId());
                ctx.startActivity(intent);
                } else{
                    String cannotAccessTabMessage = "";
                    if(getTab(position).getUserTabStatus().equals("APPROVED"))
                        cannotAccessTabMessage = "All Users have not approved the Tab yet";
                    else
                        cannotAccessTabMessage = "Approve tab before you can access";
                    Toast.makeText(
                            ctx,
                            cannotAccessTabMessage,
                            Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return tabs_list.size();
    }

    public Tab getTab(int position) {
        return tabs_list.get(position);
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
