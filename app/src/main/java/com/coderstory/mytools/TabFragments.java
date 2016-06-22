package com.coderstory.mytools;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;



import java.util.ArrayList;
import java.util.List;


public class TabFragments extends Fragment implements ViewPager.OnPageChangeListener,
        TabHost.OnTabChangeListener {

    public static final int TAB_LOGIN = 0;
    public static final int TAB_REG = 1;
    private TabHost tabHost;
    private int currentTab = TAB_LOGIN;
    private ViewPager viewPager;
    private List<Fragment> fragments;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, null);
        tabHost = (TabHost) rootView.findViewById(android.R.id.tabhost);
        viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        viewPager.setOnPageChangeListener(this);
        fragments = new ArrayList<>();
        fragments.add(new BackupApp());
        fragments.add(new restoreApp());
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
        TabFragmentPageAdapter pageAdapter = new TabFragmentPageAdapter(getChildFragmentManager(),
                fragments, getArguments());
        pageAdapter.notifyDataSetChanged();
        viewPager.setAdapter(pageAdapter);
        setupTabs();
    }

    private void setupTabs() {
        tabHost.setup();
        tabHost.addTab(newTab(R.string.tab_1));
        tabHost.addTab(newTab(R.string.tab_2));

        for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {

            // tabHost.setBackgroundResource(R.drawable.tab_selector);
            final View view = tabHost.getTabWidget().getChildTabViewAt(i);


            final View textView = view.findViewById(android.R.id.title);
            ((TextView) textView).setTextColor(getResources().getColor(R.color.tableColor));
            ((TextView) textView).setHeight(100);
            ((TextView) textView).setTextSize(20);
            ((TextView) textView).setTextSize(20);
            ((TextView) textView).setSingleLine(true);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                tabHost.getTabWidget().getChildAt(i)
                        .findViewById(android.R.id.icon);
                tabHost.getTabWidget().getChildAt(i).getLayoutParams().height = 75;

            } else {

                // reduce height of the tab
                view.getLayoutParams().height *= 0.77;

                ((TextView) textView).setGravity(Gravity.CENTER);
                textView.getLayoutParams().height =  ViewGroup.LayoutParams.MATCH_PARENT;
                textView.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
            }

        }
        tabHost.setOnTabChangedListener(TabFragments.this);
        tabHost.setCurrentTab(currentTab);
    }

    private TabHost.TabSpec newTab(int titleId) {
        TabHost.TabSpec tabSpec = tabHost.newTabSpec(getString(titleId));
        tabSpec.setIndicator(getString(titleId));
        tabSpec.setContent(new TabFactory(getActivity()));
        return tabSpec;
    }

    @Override
    public void onPageScrollStateChanged(int position) {

    }

    @Override
    public void onPageScrolled(int position, float arg1, int arg2) {

    }
 //选择了某个标签
    @Override
    public void onPageSelected(int position) {
        tabHost.setCurrentTab(position);
    }

    @Override
    public void onTabChanged(String tabId){
        currentTab = tabHost.getCurrentTab();
        viewPager.setCurrentItem(currentTab);
        updateTab();
    }

    @SuppressWarnings("unused")
    private void updateTab() {
        switch (currentTab) {
            case TAB_REG:
                restoreApp login = (restoreApp) fragments.get(currentTab);
                break;
            case TAB_LOGIN:
                BackupApp register = (BackupApp) fragments
                        .get(currentTab);
                break;
        }
    }

    class TabFactory implements TabHost.TabContentFactory {

        private final Context context;

        public TabFactory(Context context) {
            this.context = context;
        }

        @Override
        public View createTabContent(String tag) {
            View v = new View(context);
            v.setMinimumHeight(0);
            v.setMinimumWidth(0);
            return v;
        }

    }


}
