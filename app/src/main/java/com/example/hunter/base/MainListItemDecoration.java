package com.example.hunter.base;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class MainListItemDecoration extends RecyclerView.ItemDecoration {

    private int itemDecoration;

    public MainListItemDecoration(int itemDecoration) {
        this.itemDecoration = itemDecoration;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
//        if (position == 0) {
//            outRect.top = itemDecoration * 2;
//        }
        if (state.getItemCount() - 1 == position) {
            outRect.top = itemDecoration * 2;
        }
        outRect.bottom = itemDecoration;
    }
}
