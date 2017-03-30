package com.tm.expandlayout.library;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

/**
 * Created by Tian on 2017/3/30.
 */

public class HeaderLayout extends FrameLayout {
    private static final String TAG = "HeaderLayout";

    private int scaledTouchSlop;

    private OnHeaderScrollListener listener;
    public HeaderLayout(@NonNull Context context) {
        super(context);
        init(context);
    }

    public HeaderLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HeaderLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        scaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    float downY = 0;
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        float y = ev.getRawY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                float dy = y - downY;
                if (Math.abs(dy) > scaledTouchSlop) {
                    if (dy > 0) {
                        return false;
                    }
                    return true;
                } else {
                    return false;
                }
        }
        return super.onInterceptTouchEvent(ev);
    }

    private float y;
    private float laseY;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        y = event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                laseY = y;
                return true;
            case MotionEvent.ACTION_MOVE:
                float dy = y - laseY;
                if (listener != null) {
                    listener.onHeaderScroll(dy);
                    laseY = y;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (listener != null) {
                    listener.onFling();
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    public void setTopMargin(int topMargin) {
        getMarginLayoutParams().topMargin = topMargin;
        requestLayout();
    }

    private MarginLayoutParams getMarginLayoutParams() {
        return (MarginLayoutParams) this.getLayoutParams();
    }

    public interface OnHeaderScrollListener {
        public void onHeaderScroll(float dy);
        public void onFling();
    }

    public void setOnHeaderScrollListener(OnHeaderScrollListener listener) {
        this.listener = listener;
    }





}
