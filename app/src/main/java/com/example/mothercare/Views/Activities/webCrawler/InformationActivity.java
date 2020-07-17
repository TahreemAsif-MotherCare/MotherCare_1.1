package com.example.mothercare.Views.Activities.webCrawler;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.mothercare.BaseActivity;
import com.example.mothercare.R;

public class InformationActivity extends BaseActivity {

    FragmentPagerAdapter adapterViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        ViewPager vpPager = (ViewPager) findViewById(R.id.vpPager);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_information;
    }

    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 4;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return TrimesterOneFragment.newInstance();
                case 1:
                    return TrimesterTwoFragment.newInstance();
                case 2:
                    return TrimesterThreeFragment.newInstance();
                case 3:
                    return LabourAndBirthFragment.newInstance();
                default:
                    return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Trimester 1";
                case 1:
                    return "Trimester 2";
                case 2:
                    return "Trimester 3";
                case 3:
                    return "Labour and Birth";
                default:
                    return null;
            }
        }

    }
}
