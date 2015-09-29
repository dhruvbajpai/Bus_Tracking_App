package enroute.pallavi.chugh.bus_tracking_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.GetCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by NETAN ARPIT on 2/15/2015.
 */
public class fragteacherlive extends android.support.v4.app.Fragment {

    GoogleMap mMap; // Might be null if Google Play services APK is not available.
    MapView m;
    String latitude,longitude;
    Float lat,lon;
    int i=0;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_fragteacherlive,container,false);
        m = (MapView) v.findViewById(R.id.mapView);
        m.onCreate(savedInstanceState);

        mMap = m.getMap();
        mMap.getUiSettings().setMyLocationButtonEnabled(false);

        FloatingActionButton addroute = (FloatingActionButton) v.findViewById(R.id.live);
        addroute.setTitle("Full Screen");
        addroute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(container.getContext(), "route", Toast.LENGTH_SHORT).show();
                Route_info.backpage=0;
                Intent i = new Intent(container.getContext(),teachermapfull.class);
                startActivity(i);
            }
        });

        new Thread(new Runnable() {
            public void run() {
                while (i<1) {
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("liveloc");
                    query.whereEqualTo("rootname", "r1");
                    query.getFirstInBackground(new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject object, com.parse.ParseException e) {
                            if (object == null) {
                                Toast.makeText(container.getContext(), "failed", Toast.LENGTH_SHORT).show();
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
                        Thread.sleep(8000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();



        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        m.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        m.onPause();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        m.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        m.onLowMemory();
    }

}
