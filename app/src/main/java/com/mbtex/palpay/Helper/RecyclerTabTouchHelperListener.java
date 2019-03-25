package com.mbtex.palpay.Helper;

import android.support.v7.widget.RecyclerView;

interface RecyclerTabTouchHelperListener {
    void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position);
}
