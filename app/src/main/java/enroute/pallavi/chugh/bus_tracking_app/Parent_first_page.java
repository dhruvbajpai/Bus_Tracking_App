package enroute.pallavi.chugh.bus_tracking_app;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;


import enroute.pallavi.chugh.bus_tracking_app.fragments.DriverDetails;
import enroute.pallavi.chugh.bus_tracking_app.tabs.SlidingTabLayout;


public class Parent_first_page extends ActionBarActivity {

    Toolbar toolbar;
    public ViewPager mPager;
    public SlidingTabLayout mTabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_first_page);
        toolbar = (Toolbar) findViewById(R.id.app_bar_parent);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(parentactivity.sname + "'s" + " Details");
        mTabs = (SlidingTabLayout) findViewById(R.id.tabs);
        mTabs.setDistributeEvenly(true);
        mPager = (ViewPager) findViewById(R.id.mpager);
        mPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        mTabs.setViewPager(mPager);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_parent_first_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id==R.id.home)
        {
            NavUtils.navigateUpFromSameTask(this);
        }
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class MyPagerAdapter extends FragmentPagerAdapter{

        private String[] tabs = {"Student", "Teacher", "View Live","Driver"};
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return new fragstudent();


                case 1:
                    return new fragteacher();


                case 2:
                    return new fraglive();

               case 3:
                    return new DriverDetails();



                default:
                    break;
            }

            return null;
        }

        @Override
        public int getCount() {
            return 4;
        }
    }


}

