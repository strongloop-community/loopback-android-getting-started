package com.strongloop.android.loopback.guide;

import java.util.List;
import java.util.Vector;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.strongloop.android.loopback.guide.lessons.LessonOneFragment;
import com.strongloop.android.loopback.guide.lessons.LessonThreeFragment;
import com.strongloop.android.loopback.guide.lessons.LessonTwoFragment;
import com.strongloop.android.loopback.guide.lessons.LessonUserFragment;

public class SlidePresenterActivity extends FragmentActivity {
    private PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide);

        initializeSlides();
    }

    private void initializeSlides() {
        pagerAdapter = new FragmentListPagerAdapter(
                getSupportFragmentManager(), createSlideFragments());

        final ViewPager pager = (ViewPager)super.findViewById(R.id.screen_pager);
        pager.setAdapter(this.pagerAdapter);
    }

    private List<Fragment> createSlideFragments() {
        final List<Fragment> fragments = new Vector<Fragment>();
        fragments.add(new CoverFragment());
        fragments.add(new IntroductionFragment());

        fragments.add(new LessonUserFragment());
        fragments.add(new LessonOneFragment());
        fragments.add(new LessonTwoFragment());
        fragments.add(new LessonThreeFragment());

        fragments.add(new FinaleFragment());
        fragments.add(new BackFragment());
        return fragments;
    }

    class FragmentListPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragments;

        /**
         * @param fragmentManager
         * @param fragments
         */
        public FragmentListPagerAdapter(final FragmentManager fragmentManager, final List<Fragment> fragments) {
            super(fragmentManager);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(final int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }
}
