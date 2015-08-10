package enroute.arpit.dhruv.bus_tracking_app;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.parse.FindCallback;
import com.parse.GetCallback;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.List;

public class Google_Map_Upload extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    Location l;
    ProgressDialog dialog;
    ArrayList<Float> latitudes;
    ArrayList<Float> longitudes;
    Integer size;
    ArrayList<LatLng> markersarray;
    ArrayList<Marker> markers;

    List<Polyline> plist;
    //GetRouteTask getRoute;
    int cam_f=0;
    boolean flag ;

    /// DISTANCE FINDING////////
    //GMapV2GetRouteDirection v2GetRouteDirection;
    Document document;
    LatLng fromPosition ;
    LatLng toPosition ;
    String distance;
    ParseObject ob;
    /// DISTANCE FINDING////////
    public void drawmarkers()
    {

        //Toast.makeText(getApplicationContext(),latitudes.get(0).toString(),Toast.LENGTH_SHORT).show();
        //SCHOOL POINTER//


            Bitmap.Config conf = Bitmap.Config.ARGB_8888;
            Bitmap bmp = Bitmap.createBitmap(180, 180, conf);
            Canvas canvas1 = new Canvas(bmp);

// paint defines the text color,
// stroke width, size
            Paint color = new Paint();
            color.setTextSize(10);
            color.setColor(Color.BLACK);

//modify canvas
            //canvas1.drawBitmap(BitmapFactory.decodeResource(getResources(),
             //       R.drawable.school), 0, 0, color);
            canvas1.drawText("Apeejay School", 30, 40, color);

//add marker to Map
            mMap.addMarker(new MarkerOptions().position(new LatLng(28.689224, 77.121460))
                    .icon(BitmapDescriptorFactory.fromBitmap(bmp))
                            // Specifies the anchor to be at a particular point in the marker image.
                    .anchor(0.5f, 1));


        //SCHOOL POINTER//
        for(int j=0;j<size;j++)
        {
            LatLng pos = markersarray.get(j);
            mMap.addMarker(new MarkerOptions().position(new LatLng(pos.latitude,pos.longitude)));

        }

    }

    public void updateUI(Location location)
    {
        mMap.clear();
        drawmarkers();


        mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(),location.getLongitude())).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()),17.0f));


    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google__map__upload);
        setUpMapIfNeeded();
        LocationManager locationManager ;

        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(true);
        criteria.setCostAllowed(true);
        String provider = locationManager.getBestProvider(criteria,true);
        latitudes = new ArrayList<Float>();
        longitudes = new ArrayList<Float>();
        markersarray = new ArrayList<LatLng>();
        markers = new ArrayList<Marker>();
        l = locationManager.getLastKnownLocation(provider);


        plist = new ArrayList<Polyline>();
      //  v2GetRouteDirection = new GMapV2GetRouteDirection();



        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading Route information...");
        dialog.setIndeterminate(false);
        dialog.setMessage("Marking points on map");
        dialog.show();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("r1");
        //query.whereEqualTo("playerName", "Dan Stemkoski");
        query.orderByAscending("s_name");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> scoreList, ParseException e) {
                if (e == null) {
                     size = scoreList.size();

                    for(int j=0;j<size;j++)
                    {
                        Float lt = Float.parseFloat(scoreList.get(j).get("latitude").toString());

                        //latitudes.add(lt);

                        Float lg = Float.parseFloat(scoreList.get(j).get("longitude").toString());
                        //longitudes.add(lg);

                        markersarray.add(new LatLng(lt,lg));// array of the lat lng points to be taken in consideration for generating the POLYLINES.

                        markers.add(mMap.addMarker(new MarkerOptions().position(new LatLng(lt,lg))));


                    }
                    {
                        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
                        Bitmap bmp = Bitmap.createBitmap(80, 80, conf);
                        Canvas canvas1 = new Canvas(bmp);

// paint defines the text color,
// stroke width, size
                        Paint color = new Paint();
                        color.setTextSize(35);
                        color.setColor(Color.BLACK);

//modify canvas
//                        canvas1.drawBitmap(BitmapFactory.decodeResource(getResources(),
  //                              R.drawable.school), 0, 0, color);
                        canvas1.drawText("Apeejay School", 30, 40, color);

//add marker to Map
                        markers.add(mMap.addMarker(new MarkerOptions().position(new LatLng(28.689224, 77.121460))
                                .icon(BitmapDescriptorFactory.fromBitmap(bmp))
                                        // Specifies the anchor to be at a particular point in the marker image.
                                .anchor(0.5f, 1)));

                    }
                   // markers.add(mMap.addMarker(new MarkerOptions().position().title("Apeejay School").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))));

                    Toast.makeText(getApplicationContext(),size.toString(),Toast.LENGTH_SHORT).show();




                    dialog.dismiss();
                    drawmarkers();
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    for (Marker marker : markers) {
                        builder.include(marker.getPosition());
                    }
                    LatLngBounds bounds = builder.build();
                    int padding =50;
                    CameraUpdate cu  = CameraUpdateFactory.newLatLngBounds(bounds,padding);
                    mMap.animateCamera(cu);



                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });


        //---------------------------------------------------------------------------FOR UPDATING REAL TIME DRIVER LOCATION ON INFO ON PARSE-------------------------------------------------------------------------------------------------------------
        //--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        final LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(final Location location) {

                updateUI(location);

                /*
                mMap.clear();

                mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(),location.getLongitude())));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()),17.0f));
                */


                ParseQuery<ParseObject> query = ParseQuery.getQuery("liveloc");
                //query.whereEqualTo("rootname", "r1");


                query.getInBackground("2CPL78c5U9",new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject parseObject, com.parse.ParseException e) {
                        if (e == null) {
                            // Now let's update it with some new data. In this case, only cheatMode and score
                            // will get sent to the Parse Cloud. playerName hasn't changed.
                            Double lat = location.getLatitude();
                            String latitude=lat.toString();
                            Double longi = location.getLongitude();
                            String longitude=longi.toString();

                            parseObject.put("longitude",longitude);
                            parseObject.put("latitude",latitude);
                            parseObject.saveInBackground();
                        }
                    }
                });
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        locationManager.requestLocationUpdates(provider,2000,5,locationListener);
        //--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
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
     * If it isn't installed {@link SupportMapFragment} (and
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
       // mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }
}
