package com.strongloop.android.loopback.guide;

import java.util.List;
import java.util.Vector;

import com.strongloop.android.loopback.guide.lessons.LessonOneFragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

public class SlidePresenterActivity extends FragmentActivity {
	private PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide);
        getActionBar().hide();

        initializeSlides();
    }

    private void initializeSlides() {
        pagerAdapter = new FragmentListPagerAdapter(
        		getFragmentManager(), createSlideFragments());

        ViewPager pager = (ViewPager)super.findViewById(R.id.screen_pager);
        pager.setAdapter(this.pagerAdapter);
    }

    private List<Fragment> createSlideFragments() {
        List<Fragment> fragments = new Vector<Fragment>();
        fragments.add(new CoverFragment());
        fragments.add(new IntroductionFragment());

        fragments.add(new LessonOneFragment());
        // TODO - add lesson slides

        fragments.add(new FinaleFragment());
        fragments.add(new BackFragment());
        return fragments;
    }

    class FragmentListPagerAdapter extends FragmentStatePagerAdapter {
    	private List<Fragment> fragments;

        /**
         * @param fragmentManager
         * @param fragments
         */
        public FragmentListPagerAdapter(FragmentManager fragmentManager, List<Fragment> fragments) {
            super(fragmentManager);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }
}
