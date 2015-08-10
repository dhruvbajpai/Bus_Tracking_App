package enroute.arpit.dhruv.bus_tracking_app;

/**
 * Created by NETAN ARPIT on 8/3/2015.
 */

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

class TabsPagerAdapter extends FragmentPagerAdapter {

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                return new fragstudent();


            case 1:
                return new fragteacher();


            case 2:
                return new fraglive();



            default:
                break;
        }

        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 3;
    }

}