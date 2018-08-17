package com.example.arex.mytvtest;

//import android.app.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

//import android.support.v4.app.FragmentPagerAdapter;
//import android.support.v4.app.FragmentPagerAdapter;

public class MainViewPager extends Fragment {
    private ViewPager mViewPager;
    public final static int BACKGROUND_PAGE = 0;
    public final static int HOME_PAGE = 1;
    public final static int WEBVIEW_PAGE = 2;
    //    private List visList = new ArrayList();
    private ArrayList<Integer> visList = new ArrayList<>();
    private static final String visTemplate = "http://10.0.0.188:8080/#/visviewer/%d?corpid=2&token=eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI3Iiwic3ViIjoiemhhbmdwZW5nIiwiZXhwIjoxNTM0OTkyMzMwLCJ1dCI6Ik1FTUJFUiIsImR0IjoiUEMifQ.Bql4RwNTCXZQ55JBbNhhuras46olgeFspuiDmiefVlQ";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        visList.add(198);
        visList.add(215);
        visList.add(217);
        visList.add(219);
        visList.add(161);
        Log.d("MainViewPager", visList.toString());

        View view = inflater.inflate(R.layout.main_viewpager, container, false);
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.setAdapter(new FragmentPagerAdapter(getFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                int visId = visList.get(position);
                String visUrl = String.format(visTemplate, visId);
//                Bundle args = new Bundle();
//                args.putInt("visId", visId);
//                return WebViewFragment.newInstance(args);
                return ClientWebViewFragment.build(visUrl);
            }

            @Override
            public int getCount() {
                return visList.size();
            }
        });
        mViewPager.setCurrentItem(1);
        return view;
    }

    public void setPage(int pageIndex) {
        mViewPager.setCurrentItem(pageIndex);
    }

    public void setPrevPage() {
        int currPos = mViewPager.getCurrentItem();
        if (currPos > 0) {
            mViewPager.setCurrentItem(currPos - 1);
        }
    }

    public void setNextPage() {
        int currPos = mViewPager.getCurrentItem();
        if (currPos < visList.size() - 1) {
            mViewPager.setCurrentItem(currPos + 1);
        }
    }
}
