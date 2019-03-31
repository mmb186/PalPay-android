package com.mbtex.palpay.Tabs;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mbtex.palpay.R;
import com.mbtex.palpay.User.User;

import org.fabiomsr.moneytextview.MoneyTextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Recycler View Class for Tab Transactions.
 *      - Defines OnClick Functionality and how to bind dataToView
 */
public class TabTransactionRecyclerViewAdapter extends RecyclerView.Adapter<TabTransactionRecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "TabTransactionRecycler";

    private ArrayList<TabTransaction> _transaction_list = new ArrayList<>();
    private Context _ctx;
    private User current_user;

    public TabTransactionRecyclerViewAdapter(Context _ctx, ArrayList<TabTransaction> _transaction_list, User current_user) {
        this._transaction_list = _transaction_list;
        this._ctx = _ctx;
        this.current_user = current_user;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from (viewGroup.getContext()).inflate(R.layout.layour_tab_detail_listitem, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        Log.d(TAG, "onBindViewHolder: Added new Item to View");

        viewHolder.transaction_owner.setText(_transaction_list.get(position).getUsername());
        viewHolder.transaction_date.setText(_transaction_list.get(position).getDate());
        viewHolder.transaction_status.setText(_transaction_list.get(position).getStatus());
        viewHolder.transaction_type.setText(_transaction_list.get(position).getType());
        viewHolder.transaction_amount.setAmount(_transaction_list.get(position).getAmount());

        // loading the image
        Glide.with(_ctx)
                .asBitmap()
                .load(this._transaction_list.get(position).getImageSource())
                .into(viewHolder.image);

        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked on: " + _transaction_list.get(position).getUsername());


                Toast.makeText(
                        _ctx,
                        _transaction_list.get(position).getUsername() + " " + Integer.toString(_transaction_list.get(position).getTransactionId()),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    public TabTransaction getTabTransaction(int position) {
        return this._transaction_list.get(position);
    }

    @Override
    public int getItemCount() {
        return _transaction_list.size();
    }

    public void removeTransaction(int position) {
        _transaction_list.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, _transaction_list.size());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView image;
        TextView transaction_owner;
        TextView transaction_date;
        TextView transaction_status;
        TextView transaction_type;
        MoneyTextView transaction_amount;
        RelativeLayout parentLayout;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.user_transaction_image);
            this.transaction_owner = itemView.findViewById(R.id.transaction_owner);
            this.transaction_amount = itemView.findViewById(R.id.transaction_amount);
            this.transaction_date = itemView.findViewById(R.id.transaction_date);
            this.transaction_status = itemView.findViewById(R.id.transaction_status);
            this.transaction_type = itemView.findViewById(R.id.transaction_type);
            this.parentLayout = itemView.findViewById(R.id.transaction_parent_layout);
        }
    }
}
