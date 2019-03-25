package com.mbtex.palpay.Tabs;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import de.hdodenhof.circleimageview.CircleImageView;

public class TabTransactionRecyclerViewAdapter {
    private static final String TAG = "TabTransactionRecycler";

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView image;
        TabTransaction tab_transaction;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
