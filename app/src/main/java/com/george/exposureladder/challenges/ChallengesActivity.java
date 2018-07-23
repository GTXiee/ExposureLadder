package com.george.exposureladder.challenges;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.george.exposureladder.R;

import java.util.ArrayList;
import java.util.List;

public class ChallengesActivity extends Fragment {

    public static final int REQUEST_EXIT = 0;

    private SectionsPageAdapter mSectionsPageAdapter;
    private ViewPager mViewPager;

//    private DrawerLayout mDrawerLayout;

    private FloatingActionButton mFab;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_challenges, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Challenges");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("Exposure", "Challenges Fragment Created");

        mFab = view.findViewById(R.id.challenges_fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleFAB();
            }
        });
//        mDrawerLayout = findViewById(R.id.drawer_layout);
//
//        Toolbar toolbar = view.findViewById(R.id.challenges_toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);

//        NavigationView navigationView = findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(
//                new NavigationView.OnNavigationItemSelectedListener() {
//                    @Override
//                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                        // highlights item to show selected
//                        item.setChecked(true);
//                        // close drawers when tapped
//                        mDrawerLayout.closeDrawers();
//
//                        switch (item.getItemId()) {
//                            case R.id.nav_challenges:
//                                return true;
//
//                            case R.id.nav_ladders:
//                                Intent intent = new Intent(ChallengesActivity.this, LadderActivity.class);
//                                startActivity(intent);
//                                finish();
//                                return true;
//                        }
//                        return false;
//                    }
//                }
//        );

        mSectionsPageAdapter = new SectionsPageAdapter(getActivity().getSupportFragmentManager());
//
        // Setup the ViewPager with the sections adapter
        mViewPager = view.findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        mFab.show();
                        break;
                    default:
                        mFab.hide();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }




//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.fragment_challenges);
//
//        mFab = findViewById(R.id.fab);
////        mDrawerLayout = findViewById(R.id.drawer_layout);
//
//        Toolbar toolbar = findViewById(R.id.challenges_toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
//
////        NavigationView navigationView = findViewById(R.id.nav_view);
////        navigationView.setNavigationItemSelectedListener(
////                new NavigationView.OnNavigationItemSelectedListener() {
////                    @Override
////                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
////                        // highlights item to show selected
////                        item.setChecked(true);
////                        // close drawers when tapped
////                        mDrawerLayout.closeDrawers();
////
////                        switch (item.getItemId()) {
////                            case R.id.nav_challenges:
////                                return true;
////
////                            case R.id.nav_ladders:
////                                Intent intent = new Intent(ChallengesActivity.this, LadderActivity.class);
////                                startActivity(intent);
////                                finish();
////                                return true;
////                        }
////                        return false;
////                    }
////                }
////        );

//        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
//
//        // Setup the ViewPager with the sections adapter
//        mViewPager = findViewById(R.id.container);
//        setupViewPager(mViewPager);
//
//        TabLayout tabLayout = findViewById(R.id.tabs);
//        tabLayout.setupWithViewPager(mViewPager);
//
//        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                switch (position) {
//                    case 0:
//                        mFab.show();
//                        break;
//                    default:
//                        mFab.hide();
//                        break;
//                }
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
//
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                mDrawerLayout.openDrawer(GravityCompat.START);
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    public void handleFAB() {
        Intent intent = new Intent(getContext(), ChallengePickerActivity.class);
        startActivity(intent);
    }
    








    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getChildFragmentManager());
        adapter.addFragment(new Tab1Fragment(), "Active");
        adapter.addFragment(new Tab2Fragment(), "Completed");
        viewPager.setAdapter(adapter);
    }

    public class SectionsPageAdapter extends FragmentPagerAdapter {

        private List<Fragment> mFragmentList = new ArrayList<>();
        private List<String> mFragmentTitleList = new ArrayList<>();

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        public SectionsPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }
    }


}
