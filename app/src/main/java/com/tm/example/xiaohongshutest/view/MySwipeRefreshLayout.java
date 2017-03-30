package com.tm.example.xiaohongshutest.view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.tm.expandlayout.library.ExpandLayout;

/**
 * Created by Tian on 2017/3/30.
 */

public class MySwipeRefreshLayout extends SwipeRefreshLayout {

    private ExpandLayout expandLayout;
    public MySwipeRefreshLayout(Context context) {
        super(context);
    }

    public MySwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private float y;
    private float downY;
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        y = ev.getRawY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                float dy = y - downY;
                if (Math.abs(dy) > 0 && !expandLayout.isExpand()) {
                    return false;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    public void setScrollChild(ExpandLayout expandLayout) {
        this.expandLayout = expandLayout;
    }
}
