package enroute.pallavi.chugh.bus_tracking_app;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.GetCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;



public class parentmapfull extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    String route;
    String longitude,latitude;
    int i=0;
    float lat,lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parentmapfull);

        if(Route_info.backpage==1)
        {
            route="r"+Route_info.route_no.toString();
        }
        else
        {
            route =parentactivity.route;
        }

        setUpMapIfNeeded();




        new Thread(new Runnable() {
            public void run() {
                while (i<1) {
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("liveloc");
                    query.whereEqualTo("rootname", route);
                    query.getFirstInBackground(new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject object, com.parse.ParseException e) {
                            if (object == null) {
                                Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_SHORT).show();
                                //longitude=object.getString("longitude");
                                //latitude=object.getString("latitude");
                                //Toast.makeText(getApplicationContext(),latitude+longitude,Toast.LENGTH_SHORT).show();
                            } else {
                                longitude=object.getString("longitude");
                                latitude=object.getString("latitude");
                                //Toast.makeText(getApplicationContext(),latitude+longitude,Toast.LENGTH_SHORT).show();
                                //Log.d("score", longitude+latitude);
                                lon= Float.parseFloat(longitude);
                                lat= Float.parseFloat(latitude);
                                mMap.clear();

                                mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title("Marker"));
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 18.0f));
                            }
                        }
                    });

                    // do something in the loop
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();




    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }



    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link com.google.android.gms.maps.SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(28.699840, 77.150785), 18.0f));
        mMap.addMarker(new MarkerOptions().position(new LatLng(28.699840, 77.150785)).title("Marker"));
    }
}
