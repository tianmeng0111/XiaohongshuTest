package com.tm.example.xiaohongshutest;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tm.example.xiaohongshutest.adapter.VpAdapter;
import com.tm.example.xiaohongshutest.utils.Utils;
import com.tm.example.xiaohongshutest.view.MySwipeRefreshLayout;
import com.tm.expandlayout.library.ExpandLayout;
import com.tm.expandlayout.library.HeaderLayout;
import com.tm.expandlayout.library.OnScrollListener;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private int statusbarHeight = 0;
    private Toolbar toolbar;
    private TextView tvTitle;
    private View statusbar;

    private ExpandLayout expandLayout;
    private HeaderLayout headerLayout;
    private View statusbar1;

    private TabLayout tabLayout;
    private ViewPager vp;

    private View layoutContent;
    private View LayoutHeader;

    private int headerHeight;

    private MySwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Utils.setTranslucentMode(MainActivity.this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");

        statusbar = findViewById(R.id.statusbar);
        //在4.4以上计算状态栏高度，因为界面会覆盖状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            statusbarHeight = Utils.getStatusBarHeight(this);
        }
        setSupportActionBar(toolbar);


        initView();
    }

    private void initView() {
        expandLayout = (ExpandLayout) findViewById(R.id.expandlayout);
        headerLayout = (HeaderLayout) findViewById(R.id.headerlayout);

        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        vp = (ViewPager) findViewById(R.id.vp);
        VpAdapter adapter = new VpAdapter(MainActivity.this);
        vp.setAdapter(adapter);
        tabLayout.setupWithViewPager(vp);

        layoutContent = findViewById(R.id.layout_content);
        LayoutHeader = findViewById(R.id.layout_top);
        int metricsHeight = Utils.getMetricsHeight(MainActivity.this);
        layoutContent.getLayoutParams().height = metricsHeight - statusbarHeight;


        statusbar1 = LayoutHeader.findViewById(R.id.statusbar1);
        //在4.4以上 需要预留状态栏高度
        statusbar.getLayoutParams().height = statusbarHeight;
        statusbar1.getLayoutParams().height = statusbarHeight;

        LayoutHeader.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                headerHeight = LayoutHeader.getMeasuredHeight();
                expandLayout.setHeaderHeight(headerHeight);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    LayoutHeader.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });

        expandLayout.setToolbarHeight(toolbar.getLayoutParams().height + statusbarHeight);
        expandLayout.setExpandHeader(headerLayout);

        tvTitle = (TextView) findViewById(R.id.tv_title);
        expandLayout.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScroll(int scrollDistance) {
                tvTitle.setAlpha((float)scrollDistance / headerHeight);
            }
        });

        swipeRefreshLayout = (MySwipeRefreshLayout) findViewById(R.id.swipe);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.text_selected));
        swipeRefreshLayout.setScrollChild(expandLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (swipeRefreshLayout.isRefreshing()) {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                }, 3000);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void clickTest(View view) {
        if (view instanceof TextView) {
            Toast.makeText(MainActivity.this, ((TextView) view).getText().toString(), Toast.LENGTH_SHORT).show();
            return;
        }
        if (view instanceof Button) {
            Toast.makeText(MainActivity.this, ((Button) view).getText().toString(), Toast.LENGTH_SHORT).show();
            return;
        }
        if (view instanceof ImageView) {
            Toast.makeText(MainActivity.this, "图片", Toast.LENGTH_SHORT).show();
            return;
        }
        if (view instanceof ViewGroup) {
            ViewGroup vp = (ViewGroup) view;
            for (int i = 0; i < vp.getChildCount(); i++) {
                View childAt = vp.getChildAt(i);
                if (childAt instanceof TextView) {
                    Toast.makeText(MainActivity.this, ((TextView) childAt).getText().toString(), Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }
    }
}
