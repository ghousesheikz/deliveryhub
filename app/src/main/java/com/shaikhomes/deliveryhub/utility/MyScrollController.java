package com.shaikhomes.deliveryhub.utility;

import androidx.recyclerview.widget.RecyclerView;

public abstract class MyScrollController extends RecyclerView.OnScrollListener {
    static final float MINIMUM = 25;
    int ScrollDist = 0;
    boolean isVisible = true;

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if (isVisible && ScrollDist > MINIMUM) {
            hide();
            ScrollDist = 0;
            isVisible = false;
        } else if (!isVisible && ScrollDist < -MINIMUM) {
            show();
            ScrollDist = 0;
            isVisible = true;
        }

        if ((isVisible && dy > 0) || (!isVisible && dy < 0)) {
            ScrollDist += dy;
        }

    }

    public abstract void show();

    public abstract void hide();
}
