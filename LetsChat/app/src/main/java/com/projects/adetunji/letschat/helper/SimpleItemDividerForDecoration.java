package com.projects.adetunji.letschat.helper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.projects.adetunji.letschat.R;

/**
 * Created by adetunji on 21/01/2018.
 */

public class SimpleItemDividerForDecoration extends RecyclerView.ItemDecoration{

    private Drawable mpDivider;

    public SimpleItemDividerForDecoration(Context context) {
        mpDivider = ContextCompat.getDrawable(context, R.drawable.simple_line_divider);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + mpDivider.getIntrinsicHeight();

            mpDivider.setBounds(left, top, right, bottom);
            mpDivider.draw(c);
        }
    }
}
