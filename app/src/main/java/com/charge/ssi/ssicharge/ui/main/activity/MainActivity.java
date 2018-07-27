package com.charge.ssi.ssicharge.ui.main.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.charge.ssi.ssicharge.R;
import com.charge.ssi.ssicharge.ui.main.fragment.CommunityFragment;
import com.charge.ssi.ssicharge.ui.main.fragment.HomeFragment;
import com.charge.ssi.ssicharge.ui.main.fragment.MyFragment;
import com.charge.ssi.ssicharge.ui.main.fragment.PileShareFragment;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends FragmentActivity {

    @BindView(R.id.bnve)
    BottomNavigationViewEx bnve;
    @BindView(R.id.vp)
    ViewPager vp;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.activity_with_view_pager)
    RelativeLayout activityWithViewPager;

    private static final String TAG = MainActivity.class.getSimpleName();

    private VpAdapter adapter;

    private List<Fragment> fragments;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        context = this;
        initData();
        initView();
        initEvent();
    }

    /**
     * create fragments
     */
    private void initData() {
        fragments = new ArrayList<>(4);

        // create music fragment and add it
        HomeFragment musicFragment = new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", getString(R.string.home));
        musicFragment.setArguments(bundle);

        // create backup fragment and add it
        CommunityFragment backupFragment = new CommunityFragment();
        bundle = new Bundle();
        bundle.putString("title", getString(R.string.community));
        backupFragment.setArguments(bundle);

        // create friends fragment and add it
        PileShareFragment favorFragment = new PileShareFragment();
        bundle = new Bundle();
        bundle.putString("title", getString(R.string.pile_share));
        favorFragment.setArguments(bundle);

        // create friends fragment and add it
        MyFragment visibilityFragment = new MyFragment();
        bundle = new Bundle();
        bundle.putString("title", getString(R.string.my));
        visibilityFragment.setArguments(bundle);


        // add to fragments for adapter
        fragments.add(musicFragment);
        fragments.add(backupFragment);
        fragments.add(favorFragment);
        fragments.add(visibilityFragment);
    }

    private void initView() {
        bnve.enableItemShiftingMode(false);
        bnve.enableShiftingMode(false);
        bnve.enableAnimation(false);

        // set adapter
        adapter = new VpAdapter(getSupportFragmentManager(), fragments);
        vp.setAdapter(adapter);
    }

    /**
     * set listeners
     */
    private void initEvent() {
        // set listener to change the current item of view pager when click bottom nav item
        bnve.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            private int previousPosition = -1;

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int position = 0;
                int checkColor = context.getResources().getColor(R.color.toolbar);

                switch (item.getItemId()) {

                    case R.id.i_home:
                        position = 0;
                        break;
                    case R.id.i_community:
                        position = 1;
                        break;
                    case R.id.i_pile_share:
                        position = 2;
                        break;
                    case R.id.i_my:
                        position = 3;
                        break;
                    case R.id.i_empty: {
                        return false;
                    }
                }

                if (previousPosition != position) {
                    vp.setCurrentItem(position, false);
                    previousPosition = position;
                    Log.e(TAG, "-----bnve-------- previous item:" + bnve.getCurrentItem() + " current item:" + position + " ------------------");
                }

                return true;
            }
        });

        // set listener to change the current checked item of bottom nav when scroll view pager
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.i(TAG, "-----ViewPager-------- previous item:" + bnve.getCurrentItem() + " current item:" + position + " ------------------");
                if (position >= 2) {// 2 is center
                    position++;// if page is 2, need set bottom item to 3, and the same to 3 -> 4
                }
                bnve.setCurrentItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // center item click listener
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Center", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * view pager adapter
     */
    private static class VpAdapter extends FragmentPagerAdapter {
        private List<Fragment> data;

        public VpAdapter(FragmentManager fm, List<Fragment> data) {
            super(fm);
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Fragment getItem(int position) {
            return data.get(position);
        }
    }
}
