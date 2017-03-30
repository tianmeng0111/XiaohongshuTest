package com.tm.example.xiaohongshutest.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tm.example.xiaohongshutest.R;

/**
 * Created by Tian on 2017/3/28.
 */

public class VpAdapter extends PagerAdapter {

    private static int[] imgs = {R.drawable.mine_notes_empty, R.drawable.mine_profile_empty};
    private static String[] titles = {"笔记·0", "专辑·0"};
    private ViewGroup[] vps;

    private Context context;
    public VpAdapter(Context context) {
        this.context = context;
        vps = new ViewGroup[imgs.length];
    }

    @Override
    public int getCount() {
        return imgs.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LinearLayout ll = new LinearLayout(context);
        ll.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setPadding(50, 20, 50, 50);
        ImageView iv = new ImageView(context);
        iv.setImageResource(imgs[position]);
        vps[position] = ll;
        ll.addView(iv);
        container.addView(ll);
        return ll;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(vps[position]);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
