package enroute.arpit.dhruv.bus_tracking_app;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by Dhruv on 16-Jul-15.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "1Jrskl4dgS112TdPVInJXwVNr8z5OXjWX0ZwKhOo", "2dGzy0gSBLmwffakLISmKlUN1Nkzhgw3gqkWLGlZ");
    }
}
