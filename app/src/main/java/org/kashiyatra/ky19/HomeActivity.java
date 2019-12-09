package org.kashiyatra.ky19;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.appbar.AppBarLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.core.view.GravityCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.common.GoogleApiAvailability;

import org.kashiyatra.ky19.fragments.AboutFragment;
import org.kashiyatra.ky19.fragments.EventsFragment;
import org.kashiyatra.ky19.fragments.MapFragment;
import org.kashiyatra.ky19.fragments.ScheduleFragment;
import org.kashiyatra.ky19.fragments.SponsorsFragment;


import static java.lang.Math.min;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences prefs;
    FloatingActionMenu materialDesignFAM;
    FloatingActionButton floatingActionButton1, floatingActionButton2, floatingActionButton5, floatingActionButton6;
    DrawerLayout mDrawerLayout;
    private TabsPagerAdapter mTabsPagerAdapter;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private AppBarLayout appBarLayout;
    private int back_count = 0;
    private boolean isLoggedIn;

    public TabLayout getTabLayout() {
        mTabLayout = findViewById(R.id.tabs);
        return mTabLayout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        GoogleApiAvailability.getInstance().makeGooglePlayServicesAvailable(this);

//        titleTextView.setTypeface(Typeface.createFromAsset(getAssets(), "opensans_semibold.ttf"));

        prefs = getSharedPreferences(SplashActivity.storeUserDetails, Context.MODE_PRIVATE);
        isLoggedIn = prefs.getBoolean("isLoggedIn", false);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        final int screenWidth = displayMetrics.widthPixels;
        appBarLayout = findViewById(R.id.app_bar);
        appBarLayout.post(new Runnable() {
            @Override
            public void run() {
                int heightPx = screenWidth / 3;
                setAppBarOffset(heightPx);
            }
        });
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                float range = appBarLayout.getTotalScrollRange();
                float initial = range - screenWidth / 3;
                float alpha = min((range + verticalOffset) / initial, 1);
            }
        });

        final NavigationView navigationView = findViewById(R.id.nav_view);
        final View headerView = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.getMenu().getItem(0).setChecked(true);

        //Set up NavigationView Header with User Info

        materialDesignFAM = findViewById(R.id.social_floating_menu);
        floatingActionButton1 = findViewById(R.id.floating_facebook);
        floatingActionButton2 = findViewById(R.id.floating_twitter);
        floatingActionButton5 = findViewById(R.id.floating_instagram);
        floatingActionButton6 = findViewById(R.id.floating_youtube);

        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openURL("https://www.facebook.com/kashiyatra.IITBHU");
            }
        });
        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openURL("https://twitter.com/KY_IITBHU");
            }
        });
        floatingActionButton5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openURL("https://www.instagram.com/kashiyatra_iitbhu");
            }
        });
        floatingActionButton6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openURL("https://www.youtube.com/kashiyatraiitbhu");
            }
        });

        mTabsPagerAdapter = new TabsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.viewpager);
        mViewPager.setAdapter(mTabsPagerAdapter);

        mTabLayout = findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                navigationView.getMenu().getItem(tab.getPosition()).setChecked(true);
                if (tab.getPosition() == 3) {
                    AppBarLayout appBarLayout = findViewById(R.id.app_bar);
                    appBarLayout.setExpanded(false, true);
                    if (materialDesignFAM.isOpened()) {
                        materialDesignFAM.close(true);
                    }
                    materialDesignFAM.setVisibility(View.GONE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (tab.getPosition() == 3) {
                    materialDesignFAM.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

//        changeTabsFont();
    }

    private void setAppBarOffset(int offsetPx) {
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
        AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
        behavior.onNestedPreScroll((CoordinatorLayout) findViewById(R.id.home_coordinator_layout), appBarLayout, null, 0, offsetPx, new int[]{0, 0}, 0);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (!isLoggedIn) {
            super.onBackPressed();
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            overridePendingTransition(R.anim.pull_left, R.anim.push_right);
            startActivity(intent);
        } else if (back_count == 0) {
            back_count = 1;
            Toast.makeText(this, "Tap back again to exit", Toast.LENGTH_SHORT).show();
            Thread tim = new Thread() {
                public void run() {
                    try {
                        sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        back_count = 0;
                    }
                }
            };
            tim.start();

        } else {
            finish();
            super.onBackPressed();
        }
    }

    public void call(View v) {
        String uri = "tel:" + ((TextView) v).getText();
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(uri));
        startActivity(intent);
    }

    public void mail(View view) {
        String[] email = new String[]{(String) ((TextView) view).getText()};
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, email);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Query regarding Kashiyatra");
        startActivity(intent);
    }

    public void mailToKy(View view) {
        String[] email = new String[]{"kashiyatra@iitbhu.ac.in"};
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, email);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Query regarding Kashiyatra");
        startActivity(intent);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_profile) {
            Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
            startActivity(intent);
            return true;
        }
        else if(id == R.id.action_updates){
            Intent intent = new Intent(getApplicationContext(), UpdatesActivity.class);
            startActivity(intent);
            return true;
        }
        else if(id == R.id.action_helpline){
            Intent intent = new Intent(getApplicationContext(), HelplineActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent;
        switch (id) {
            case R.id.nav_about:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.nav_schedule:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.nav_events:
                mViewPager.setCurrentItem(2);
                break;
            case R.id.nav_location:
                mViewPager.setCurrentItem(3);
                break;
            case R.id.nav_sponsors:
                mViewPager.setCurrentItem(4);
                break;
            default:
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    void Logout() {
//        LoginManager.getInstance().logOut();
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();
        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.pull_left, R.anim.push_right);
        finish();
    }

    public void openURL(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    public float convertDpToPx(Context context, float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

//    private void changeTabsFont() {
//
//        ViewGroup vg = (ViewGroup) mTabLayout.getChildAt(0);
//        int tabsCount = vg.getChildCount();
//        for (int j = 0; j < tabsCount; j++) {
//            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
//            int tabChildsCount = vgTab.getChildCount();
//            for (int i = 0; i < tabChildsCount; i++) {
//                View tabViewChild = vgTab.getChildAt(i);
//                if (tabViewChild instanceof TextView) {
//                    ((TextView) tabViewChild).setTypeface(Typeface.createFromAsset(getAssets(), "BebasNeue Regular.ttf"), Typeface.NORMAL);
//                    ((TextView) tabViewChild).setTextSize(getResources().getDimension(R.dimen.desc_text_size) * 1.5f);
//                }
//            }
//        }
//    }

    private class TabsPagerAdapter extends FragmentPagerAdapter {

        private TabsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return AboutFragment.newInstance();
                case 1:
                    return ScheduleFragment.newInstance();
                case 2:
                    return EventsFragment.newInstance();
                case 3:
                    return MapFragment.newInstance();
                case 4:
                    return SponsorsFragment.newInstance();
                default:
                    return AboutFragment.newInstance();
            }
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "About";
                case 1:
                    return "Schedule";
                case 2:
                    return "Events";
                case 3:
                    return "Location";
                case 4:
                    return "Sponsors";
                default:
                    return "About";
            }
        }

    }
}