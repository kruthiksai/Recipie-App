package com.kruthik.mvvmretrofit.util;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class recyclerviewspacing extends RecyclerView.ItemDecoration {
    private int vertical_spacing;

    public recyclerviewspacing(int vertical_spacing) {

        this.vertical_spacing = vertical_spacing;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        outRect.top=vertical_spacing;
    }
}
