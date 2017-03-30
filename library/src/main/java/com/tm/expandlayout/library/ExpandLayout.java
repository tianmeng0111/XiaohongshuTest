package com.tm.expandlayout.library;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;

/**
 * Created by Tian on 2017/3/28.
 */

public class ExpandLayout extends FrameLayout implements HeaderLayout.OnHeaderScrollListener{
    private static final String TAG = "ExpandLayout";

    private int scaleTouchSlop;
    private View headerView;
    private Animator animator;

    private int toolbarHeight;
    private int headerHeight = 0;
    private boolean draging = false;

    private boolean isExpand = true;
    private int canFlingHeight;

    private OnScrollListener listener;

    private static final float PARALLAX_FACTOR = 0.5f;

    public ExpandLayout(@NonNull Context context) {
        super(context);
        init(context);
    }

    public ExpandLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ExpandLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        scaleTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        y = ev.getRawY();
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                lastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                float dy = y - lastY;
                if (Math.abs(dy) > scaleTouchSlop) {
                    lastY = y;
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    private float y;
    private float lastY;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        y = event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastY = y;
                return true;
            case MotionEvent.ACTION_MOVE:
                float dy = y - lastY;
                if (Math.abs(dy) > scaleTouchSlop && !draging) {
                    draging = true;
                }
                if (draging) {
                    setTopMargin((int) (getTopMargin() + dy));
                    lastY = y;
                }
                return true;
            case MotionEvent.ACTION_UP:
                fling();
                break;
        }
        return super.onTouchEvent(event);
    }

    private void fling() {
        int start = getTopMargin();
        int end;
        if (canFlingHeight <= 0) {
            canFlingHeight = toolbarHeight;
        }
        if (isExpand) {
            if ((headerHeight - getTopMargin()) > canFlingHeight) {
                end = toolbarHeight;
            } else {
                end = headerHeight;
            }
        } else {
            if ((getTopMargin() - toolbarHeight) > canFlingHeight) {
                end = headerHeight;
            } else {
                end = toolbarHeight;
            }
        }
        startAnim(start, end);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    private void startAnim(int start, int end) {
        if (animator == null || !animator.isRunning()) {
            animator = ObjectAnimator.ofInt(this, "TopMargin", start, end);
//        animtor = ObjectAnimator.ofFloat(tv, View.TRANSLATION_Y, 0, 500);
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            animator.setDuration(200);
            animator.start();
        }
    }

    public int getTopMargin() {
        MarginLayoutParams params = getMarginLayoutParams();
        return params.topMargin;
    }

    /**
     * 实现ObjectAnimator中动画自定义，必须实现public方法，方法名必须是这个
     * @param topMargin
     */
    public void setTopMargin(int topMargin) {
        MarginLayoutParams params = getMarginLayoutParams();
        params.topMargin = topMargin;
        if (getTopMargin() >= headerHeight) {
            params.topMargin = headerHeight;
            isExpand = true;
        }

        if (getTopMargin() <= toolbarHeight) {
            params.topMargin = toolbarHeight;
            isExpand = false;
        }

        if (listener != null) {
            listener.onScroll(headerHeight - getTopMargin());
        }

        if (headerView != null) {
            if (getTopMargin() <= toolbarHeight) {
                headerView.setAlpha(0);
            } else {
                headerView.setAlpha(1f);
            }
        }

        if (headerView != null) {
            if (headerView instanceof HeaderLayout) {
                ((HeaderLayout) headerView).setTopMargin((int) (-(headerHeight - getTopMargin()) * PARALLAX_FACTOR));
            }
        }

        requestLayout();
    }

    private MarginLayoutParams getMarginLayoutParams() {
        return (MarginLayoutParams) this.getLayoutParams();
    }

    /**
     * 设置头的高度
     * @param height
     */
    public void setHeaderHeight(int height) {
        setTopMargin(height);
        this.headerHeight = height;
        setTopMargin(headerHeight);
    }

    /**
     * 设置toolbar高度
     * @param height
     */
    public void setToolbarHeight(int height) {
        this.toolbarHeight = height;
    }

    /**
     * 设置一个视差效果的头
     * @param view
     */
    public void setExpandHeader(View view) {
        this.headerView = view;
        if (headerView instanceof HeaderLayout) {
            ((HeaderLayout) headerView).setOnHeaderScrollListener(this);//设置联动
        }
    }

    public boolean isExpand() {
        return isExpand;
    }

    /**
     * 设置可以滚动的距离，若不设置，默认toolbarHeight
     * @param canFlingHeight
     */
    public void setCanFlingHeight(int canFlingHeight) {
        this.canFlingHeight = canFlingHeight;
    }

    public void setOnScrollListener(OnScrollListener listener) {
        this.listener = listener;
    }

    @Override
    public void onHeaderScroll(float dy) {
        setTopMargin((int) (getTopMargin() + dy));
    }

    @Override
    public void onFling() {
        fling();
    }
}
