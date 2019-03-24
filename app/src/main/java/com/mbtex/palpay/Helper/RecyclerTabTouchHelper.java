package com.mbtex.palpay.Helper;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

public class RecyclerTabTouchHelper extends ItemTouchHelper.SimpleCallback {

    private RecyclerTabTouchHelperListener touchListener;

    public RecyclerTabTouchHelper(int dragDirs, int swipeDirs, RecyclerTabTouchHelperListener listener) {
        super(dragDirs, swipeDirs);
        this.touchListener = listener;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        if (touchListener != null)
            touchListener.onSwiped(viewHolder, direction, viewHolder.getAdapterPosition());
    }

}
