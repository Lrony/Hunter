package com.example.hunter.base;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class AboutListItemDecoration extends RecyclerView.ItemDecoration {

    private int itemDecoration;

    public AboutListItemDecoration(int itemDecoration) {
        this.itemDecoration = itemDecoration;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        if (position == 0) {
            outRect.top = itemDecoration * 2;
        } else {
            outRect.top = itemDecoration;
        }
        outRect.left = itemDecoration;
        outRect.right = itemDecoration;
    }
}
