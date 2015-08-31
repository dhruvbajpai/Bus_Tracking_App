package enroute.pallavi.chugh.bus_tracking_app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

//Change the markers to visible non visible type after downloading them ONCE....Download ONCE ONLY...
// ADD aync to getfromlocation method so that it works in a new thread....   UI SMOOTHER
//ADD dialogbox to activity.
// ADD AUTO GENERATE ROUTE FEATURES
public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    AutoCompleteTextView locationview;
    CheckBox ck;
    Button set, save, show_markers,al_mar;
    TextView info;
    ImageView iv;
    int x, y;
    boolean geoflag, are_markers_marked = false;
    boolean flag, show_flag = true,al_flag=false;
    ProgressDialog dialog;
    LatLng centre_location, p_position;
    Marker toOrange;
    DownloadTask placesDownloadTask;
    DownloadTask placeDetailsDownloadTask;
    ParserTask placesParserTask;
    ParserTask placeDetailsParserTask;
    ParseQuery<ParseObject> query;
    ParseObject ob;
    GoogleMap googleMap;
    ArrayList<LatLng> markersarray;
    ArrayList<Marker> markers;
    Marker pos_marker, prev_marker;
    boolean route_gen_flag=false,marker_gen_flag=false;
    final int PLACES = 0;
    final int PLACES_DETAILS = 1;
    boolean checkboxflag = false;
    MarkerOptions o;
    Integer size;// (The size of the array list of markers)

    Double p_lat, p_lon;
    LinkedList<Polyline> lpoly= new LinkedList<>();
    HashMap<Integer, List<LatLng>> point_list = new HashMap<>();


    ///CREATE A SAVE BUTTON AND SAVE THE COORDINATES OF THE LOCATION ON PARSE TABLE IN STRING FIELDS.
    Integer position, prevact;

    void generate_markers()
    {



        for(int i=0;i<markersarray.size();i++)
        {
           /* if(i==position+1)   //////////////////////////////////////////////////////////////////////////////*//**************************************************************************
                continue;*/
            mMap.addMarker(new MarkerOptions().position(new LatLng(markersarray.get(i).latitude,markersarray.get(i).longitude)));
            //markers.get(i).setVisible(true);
        }

        //This has to be below other marker generation to see the blue marker for school as markersarray also contains the school location.(for route calculation)
        Marker school = mMap.addMarker(new MarkerOptions().position(new LatLng(28.689224, 77.121460))    //SCHOOL MARKER
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)).title("Apeejay School"));
        school.showInfoWindow();



        if (prevact == 1) {
            prev_marker = mMap.addMarker(new MarkerOptions().position(new LatLng(p_lat, p_lon)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)).title("Current Saved Stop"));
            //prev_marker.showInfoWindow();
            markers.add(prev_marker);
        }
        info.setText("Bus Stops Marked in Orange");
        animate_bound();


    }
    void animate_bound()
    {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker marker : markers) {
            builder.include(marker.getPosition());
        }
        LatLngBounds bounds = builder.build();
        int padding = 150;
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mMap.animateCamera(cu);
    }

    void ifchecktrue()  // function to draw polylines if auto check on
    {
        //Log.d("here", "is checked:true");
        Log.d("marker","1");
        if(marker_gen_flag) {
            Log.d("marker","2");
            if (route_gen_flag == false) {///route generated for the first time
                route_gen_flag = true;
                info.setText("Generating Route form server...");
                connectAsyncTask ck = new connectAsyncTask();
                ck.execute();
            } else {
                Log.d("here", "Polyline made second");
                //for (int i = 0; i < point_list.size(); i++) {   //lpoly.get(i).setVisible(true);
                   // drawpoly(point_list.get(i));
                    new CountDownTimer((point_list.size()+3)*100, 100) {
                        int q=0;
                        public void onFinish() {
                            // When timer is finished
                            // Execute your code here
                        }

                        public void onTick(long millisUntilFinished) {
                            if(q<point_list.size()) {
                                drawpoly(point_list.get(q));
                                q++;
                            }
                            // millisUntilFinished    The amount of time until finished.
                        }
                    }.start();


                //}

            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
        al_mar = (Button)(findViewById(R.id.already_set));
        al_mar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                al_flag= true;
                set.setVisibility(View.GONE);
                iv.setVisibility(View.GONE);

                //show_markers.setVisibility(View.GONE);
            }
        });
        info = (TextView) findViewById(R.id.info);
        ck = (CheckBox) findViewById(R.id.gen_route);
        ck.setVisibility(View.GONE);
       ck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
              // if (show_flag)
               //{
                   if (isChecked == true)//Checked state of the button for route generation
                   {    Log.d("marker","start");
                       checkboxflag =true;
                       if(!show_flag)
                       ifchecktrue();


                   } else {
                       checkboxflag = false;
                        if(!show_flag) {
                            mMap.clear();
                            generate_markers();

                            //animate_bound();
                        }
                   }
//           }

           }

       });

        /*ck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // for (int i = 0; i < markersarray.size() - 1; i++) {
                    // String makeurl = makeURL(markersarray.get(i).latitude,markersarray.get(i).longitude,markersarray.get(i+1).latitude,markersarray.get(i+1).longitude);

                //}
            }
        });*/
        /*if(savedInstanceState==null)
        {*/
           /* Bundle extras = getIntent().getExtras();
            position = extras.getInt("position");
            p_lat  = Double.parseDouble(mediator.latti.get(position));
            p_lon  = Double.parseDouble(mediator.longi.get(position));
            mMap.addMarker(new MarkerOptions().position(new LatLng(p_lat,p_lon)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)).title("Current Saved Stop")).showInfoWindow();*/


        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                position = 0;
                prevact = -1;
            } else {
                position = extras.getInt("position");
                prevact = extras.getInt("activityname");
            }

        } else {
            position = (Integer) savedInstanceState.getSerializable("position");
            prevact = (Integer) savedInstanceState.getSerializable("activityname");

        }
        ///////////////////////TODELETE/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
       // position = 3;
        //prevact = 1;

        ///////////////////////TODELETE/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

// 0 for addstudent-add location
// 1 for studentinfoedit-editlocation
// 2 for addstudent-edit location
        if (prevact == 1) {
            info.setText("Previous Bus Stop Marked on Map");
//////////////////////////////////////////////////////////////////////TO DELETE/////////////////////////////////////////
           // p_lat = 28.689102;
            //p_lon = 77.142498;
//////////////////////////////////////////////////////////////////////TO DELETE/////////////////////////////////////////

            p_lat = Double.parseDouble(mediator.latti.get(position));

            p_lon = Double.parseDouble(mediator.longi.get(position));
            prev_marker = mMap.addMarker(new MarkerOptions().position(new LatLng(p_lat, p_lon)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)).title("Current Saved Stop"));
            prev_marker.showInfoWindow();

        } else if (prevact == 0) {
            info.setText("Set your desired location by moving the map");

        }


         /*   if(extras==null)
            {
                position =0;
                prevact =0;
            }
            else
                position = Integer.parseInt(savedInstanceState.getSerializable("position").toString());
            prevact =  Integer.parseInt(savedInstanceState.getSerializable("activityname").toString());
        }else {
            position = (Integer) savedInstanceState.getSerializable("position");
            prevact = (Integer) savedInstanceState.getSerializable("activityname");
        }

        if(prevact==1)
        {    p_lat  = Double.parseDouble(mediator.latti.get(position));

             p_lon  = Double.parseDouble(mediator.longi.get(position));
            mMap.addMarker(new MarkerOptions().position(new LatLng(p_lat,p_lon)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)).title("Current Saved Stop")).showInfoWindow();

        }*/


        flag = true;
        geoflag = true;
        iv = (ImageView) findViewById(R.id.img_pointer);

    /*    x = (iv.getLeft() + iv.getRight()) / 2;
        y = iv.getBottom();*/


        save = (Button) findViewById(R.id.btn_save);
        markersarray = new ArrayList<LatLng>();
        markers = new ArrayList<Marker>();
        locationview = (AutoCompleteTextView) findViewById(R.id.location);
        set = (Button) findViewById(R.id.set_pointer);
        show_markers = (Button) findViewById(R.id.show_markers);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//CLICKED SAVE BUTTON
                if (pos_marker != null) {//IF POSITION IS DEFINED AND STORED IN pos_marker

                    LatLng ab = pos_marker.getPosition();
                    Double lat = ab.latitude;
                    Double lon = ab.longitude;
                    if (prevact == 0)//coming from addstudent-add map location
                    {

                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("lat", lat);
                        returnIntent.putExtra("lon", lon);
                        returnIntent.putExtra("address", locationview.getText().toString());
                        setResult(RESULT_OK, returnIntent);
                        finish();

                    } else if (prevact == 1) {//from studentinfoedit
                        mediator.address.set(position, locationview.getText().toString());
                        mediator.latti.set(position, lat.toString());
                        mediator.longi.set(position, lon.toString());
                        finish();
                    }
                } else {
                    finish();
                }
            }
        });


        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                try{
                    toOrange.setIcon(BitmapDescriptorFactory.defaultMarker());
                }catch(Exception e)
                {
                    e.printStackTrace();
                }

                toOrange = marker;// To make the previous selection to default color
                pos_marker = marker;
                info.setText("Selected Marker shown in Green");
                marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                marker.setTitle("This Marker Selected");
                marker.showInfoWindow();

                return false;
            }
        });


        show_markers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ck.setVisibility(View.VISIBLE);

                if (show_flag) {// show flag clicked for the first time
                    dialog = new ProgressDialog(getBaseContext());
                    dialog.setTitle("Loading Route information...");
                    dialog.setIndeterminate(false);
                    dialog.setMessage("Marking points on map");
                    info.setText("Marking Stop Points on Map....");
                    //dialog.show();
                    if(!(markersarray.size()>0)) {                      ////CHECK IF THE BUTTON IS CLICKED FOR THE FIRST TIME...QUERY EXECUTION ONLY ONCE
                        // USE object retreived in MEdiator   the_route instaed of another query for this//
                        ParseQuery<ParseObject> query = ParseQuery.getQuery("r1");
                        //query.whereEqualTo("playerName", "Dan Stemkoski");
                        query.orderByAscending("Priority");
                        query.findInBackground(new FindCallback<ParseObject>() {
                            public void done(List<ParseObject> scoreList, ParseException e) {
                                if (e == null) {

                                    size = scoreList.size();
                                    Log.d("after", size.toString());
                                    Toast.makeText(getApplicationContext(), "Showing Route Stops On Map", Toast.LENGTH_SHORT).show();


                                    Marker school = mMap.addMarker(new MarkerOptions().position(new LatLng(28.689224, 77.121460))    //SCHOOL MARKER
                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)).title("Apeejay School"));
                                    school.showInfoWindow();
                                    markers.add(school);
                                    markersarray.add(new LatLng(28.689224, 77.121460));

                                    for (int j = 0; j < size; j++) {

                                        Float lt = Float.parseFloat(scoreList.get(j).get("latitude").toString());



                                        Float lg = Float.parseFloat(scoreList.get(j).get("longitude").toString());


                                        markersarray.add(new LatLng(lt, lg));// array of the lat lng points to be taken in consideration for generating the POLYLINES.
                                       /* if(j==position)
                                            continue;       ///SO THAT THE CURRENT SAVED LOCATION POINTER IS NOT CREATED AGAIN.*/
                                        ///////////////////////////////////////////////////////////////////////////////**************************************************************************
                                        markers.add(mMap.addMarker(new MarkerOptions().position(new LatLng(lt, lg))));
                                        //markers.add(new MarkerOptions().position(new LatLng(lt, lg)));

                                    }
                                    animate_bound();// for animating to the marker space
                                    //ck.callOnClick();

                                    marker_gen_flag=true;

                                    //markers.add(mMap.addMarker(new MarkerOptions().position(new LatLng(lt,lg))));
                                    // dialog.dismiss();
                                    // drawmarkers();

                                    ///ADD PREVIOUS LOCATION MARKER RECEIVED FROM PREVIOUS ACTIVITY IF COMING FROM PREV ACTIVITY
                                    if (prevact == 1) {
                                        prev_marker = mMap.addMarker(new MarkerOptions().position(new LatLng(p_lat, p_lon)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)).title("Current Saved Stop"));
                                        //prev_marker.showInfoWindow();
                                        markers.add(prev_marker);
                                    }
                                    info.setText("Bus Stops Marked in Orange");




                                }
                            }
                        });
                    }
                    else
                    {
                        generate_markers();

                    }
                    if(checkboxflag)/// TO GENERATE THE ROUTE ON THE MAP ON MARKER SHOW PRESS IF CHECKBOX IS TICKED
                    {
                        Log.d("marker","checkboxflag");
                        Log.d("marker",String.valueOf(marker_gen_flag));
                        ifchecktrue();
                    }
                    show_markers.setText("Remove Route Markers");
                    show_flag = false;

                } else if (!show_flag) {//
                    mMap.clear();
                    // REMOVE MARKERS EXECUTION IF SET POINTER WAS CLICEKD PREVIOUSLY
                    if (!flag) {
                        double lat = pos_marker.getPosition().latitude;
                        double lon = pos_marker.getPosition().longitude;
                        LatLng l = new LatLng(lat, lon);
                        pos_marker = mMap.addMarker(new MarkerOptions().position(l).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                    }
                    //Toast.makeText(getApplicationContext(),"lat: "+l+"long: "+lon,Toast.LENGTH_SHORT).show();
                    show_markers.setText("Show Route Markers");
                    show_flag = true;


                }
                ///ADD PREVIOUS LOCATION MARKER RECEIVED FROM PREVIOUS ACTIVITY
                if (prevact == 1) {
                    prev_marker = mMap.addMarker(new MarkerOptions().position(new LatLng(p_lat, p_lon)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)).title("Current Saved Stop"));
                    prev_marker.showInfoWindow();
                }


            }


        });


        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //*Projection projection = mMap.getProjection();
                Point point = new Point(x, y);
                //LatLng ll = projection.fromScreenLocation(point);*//*
                if (geoflag == true)
                    geoflag = false;
                else if (geoflag == false)
                    geoflag = true;
                if (flag) {// Set button Clicked for the first time

                    info.setText("Click \"Save\" to save this location");

                    pos_marker = mMap.addMarker(new MarkerOptions().position(centre_location).title("Student Bus Stop").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

                    iv.setVisibility(View.GONE);
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(centre_location.latitude + 0.0002f, centre_location.longitude + 0.0002f), 17.0f));
                    Toast.makeText(getApplicationContext(), "Location marked", Toast.LENGTH_SHORT).show();
                    set.setText("Remove pointer");
                    flag = false;

                } else// set already clciked.... EXECUTION ON CLICK ON REMOVE POINTER BELOW
                {
                    if (show_flag) {// show route markers not clickd... text = "show route markers"
                        Log.d("after", "show_flag true");

                        mMap.clear();//  ONLY ONE POINTER....Pos_marker exists on map here...which will be cleared.
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(centre_location.latitude, centre_location.longitude), 16.0f));
                    }

                    // REMOVE POINTER EXECUTION WHEN ROUTE MARKERS ALREADY PRESENT
                    // HIDE THE POSITION MARKER AND ANIMATE THE MAP TO latlng boundaries
                    else if (!show_flag) {
                        Log.d("after", "show_flag false");
                        //mMap.clear();
                        pos_marker.setVisible(false);
                        LatLngBounds.Builder builder = new LatLngBounds.Builder();
                        for (Marker marker : markers) {
                            builder.include(marker.getPosition());
                        }
                        LatLngBounds bounds = builder.build();
                        int padding = 50;
                        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                        mMap.animateCamera(cu);
                      /*  markers.add(mMap.addMarker(new MarkerOptions().position(new LatLng(28.689224, 77.121460))    //SCHOOL MARKER
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))));
                        for(int i=0;i<markersarray.size();i++)
                            mMap.addMarker(new MarkerOptions().position(new LatLng(markersarray.get(i).latitude,markersarray.get(i).longitude)));*/

                    }
                    set.setText("Set here");
                    info.setText("Pointer showing Location to save");
                    Toast.makeText(getApplicationContext(), "Location Marker Removed", Toast.LENGTH_SHORT).show();
                    iv.setVisibility(View.VISIBLE);
                    // mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(centre_location.latitude, centre_location.longitude), 16.0f));
                    flag = true;
                    pos_marker = null;


                    ///ADD PREVIOUS LOCATION MARKER RECEIVED FROM PREVIOUS ACTIVITY
                    if (prevact == 1) {
                        prev_marker = mMap.addMarker(new MarkerOptions().position(new LatLng(p_lat, p_lon)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)).title("Current Saved Stop"));
                        prev_marker.showInfoWindow();
                    }
                    pos_marker = null;
                }


            }
        });


        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                if (prevact == 1) {
                    if(prev_marker!=null)
                        prev_marker.setVisible(true);
                    if(!al_flag)// because this if selecting pointers executes after show window of selecting pointer executes
                    prev_marker.showInfoWindow();
                  /*  else if(al_flag)// Selecting already made markers
                    {


                    }*/

                }
                centre_location = mMap.getCameraPosition().target;
                //locationview.setText(centre_location.toString());

                //Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.ENGLISH);
                if (geoflag == true) {// position pointer not set yet

                    if(!al_flag)
                    info.setText("Pointer Showing Location to Save");
                    else
                    info.setText("Select Marker from Map");
                    try {

                        //Place your latitude and longitude
                        new AsyncTask<Void, Void, Void>() {
                            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.ENGLISH);
                            List<Address> addresses;

                            protected Void doInBackground(Void... params) {

                                try {
                                    addresses = geocoder.getFromLocation(centre_location.latitude, centre_location.longitude, 1);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                return null;
                            }

                            @Override
                            protected void onPostExecute(Void aVoid) {
                                if (addresses != null) {

                                    Address fetchedAddress = addresses.get(0);
                                    StringBuilder strAddress = new StringBuilder();

                                    for (int i = 0; i < fetchedAddress.getMaxAddressLineIndex(); i++) {
                                        strAddress.append(fetchedAddress.getAddressLine(i)).append(" ");
                                    }

                                    locationview.setText(strAddress.toString());

                                } else
                                    locationview.setText("No location found..!");
                                super.onPostExecute(aVoid);
                            }
                        }.execute();
                        //List<Address> addresses = geocoder.getFromLocation(centre_location.latitude, centre_location.longitude, 1);


                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Could not get address..!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });


        /////////////////////////////////////////////FORWARD GEOCODING CODE////////////////////////////////////////////////////////////////////////////////////////////////////
        locationview.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Creating a DownloadTask to download Google Places matching "s"
                placesDownloadTask = new DownloadTask(PLACES);

                // Getting url to the Google Places Autocomplete api
                String url = getAutoCompleteUrl(s.toString());

                // Start downloading Google Places
                // This causes to execute doInBackground() of DownloadTask class
                placesDownloadTask.execute(url);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });

        // Setting an item click listener for the AutoCompleteTextView dropdown list
        locationview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int index,
                                    long id) {

                ListView lv = (ListView) arg0;
                SimpleAdapter adapter = (SimpleAdapter) arg0.getAdapter();

                HashMap<String, String> hm = (HashMap<String, String>) adapter.getItem(index);

                // Creating a DownloadTask to download Places details of the selected place
                placeDetailsDownloadTask = new DownloadTask(PLACES_DETAILS);

                // Getting url to the Google Places details api
                String url = getPlaceDetailsUrl(hm.get("reference"));

                // Start downloading Google Place Details
                // This causes to execute doInBackground() of DownloadTask class
                placeDetailsDownloadTask.execute(url);

            }
        });
    }
    /////////////////////////////////////////////FORWARD GEOCODING CODE///////////////////////////////////////////////////////////////////////////////////////////////////

    void drawmarkers() {
        for (int j = 0; j < size; j++) {
            LatLng pos = markersarray.get(j);
            mMap.addMarker(new MarkerOptions().position(new LatLng(pos.latitude, pos.longitude)));

        }
        mMap.addMarker(new MarkerOptions().position(new LatLng(28.689224, 77.121460))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));


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
        LatLng l = new LatLng(28.695693, 77.151955);
        //googleMap.addMarker(new MarkerOptions().position(l).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(l, 14));
        //   mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }

    private String getAutoCompleteUrl(String place) {

        // Obtain browser key from https://code.google.com/apis/console
        String key = "key=AIzaSyDzJdVftvsItK72Vwrb9iDRM9DgtJvEzL0";

        // place to be be searched
        String input = "input=" + place;

        // place type to be searched
        String types = "types=geocode";

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = input + "&" + types + "&" + sensor + "&" + key;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/place/autocomplete/" + output + "?" + parameters;

        return url;
    }

    private String getPlaceDetailsUrl(String ref) {

        // Obtain browser key from https://code.google.com/apis/console
        String key = "key=AIzaSyDzJdVftvsItK72Vwrb9iDRM9DgtJvEzL0";

        // reference of place
        String reference = "reference=" + ref;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = reference + "&" + sensor + "&" + key;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/place/details/" + output + "?" + parameters;

        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception while downloading url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        private int downloadType = 0;

        // Constructor
        public DownloadTask(int type) {
            this.downloadType = type;
        }

        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            switch (downloadType) {
                case PLACES:
                    // Creating ParserTask for parsing Google Places
                    placesParserTask = new ParserTask(PLACES);

                    // Start parsing google places json data
                    // This causes to execute doInBackground() of ParserTask class
                    placesParserTask.execute(result);

                    break;

                case PLACES_DETAILS:
                    // Creating ParserTask for parsing Google Places
                    placeDetailsParserTask = new ParserTask(PLACES_DETAILS);

                    // Starting Parsing the JSON string
                    // This causes to execute doInBackground() of ParserTask class
                    placeDetailsParserTask.execute(result);
            }
        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String, String>>> {

        int parserType = 0;

        public ParserTask(int type) {
            this.parserType = type;
        }

        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<HashMap<String, String>> list = null;

            try {
                jObject = new JSONObject(jsonData[0]);

                switch (parserType) {
                    case PLACES:
                        PlaceJSONParser placeJsonParser = new PlaceJSONParser();
                        // Getting the parsed data as a List construct
                        list = placeJsonParser.parse(jObject);
                        break;
                    case PLACES_DETAILS:
                        PlaceDetailsJSONParser placeDetailsJsonParser = new PlaceDetailsJSONParser();
                        // Getting the parsed data as a List construct
                        list = placeDetailsJsonParser.parse(jObject);
                }

            } catch (Exception e) {
                Log.d("Exception", e.toString());
            }
            return list;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> result) {

            switch (parserType) {
                case PLACES:
                    String[] from = new String[]{"description"};
                    int[] to = new int[]{android.R.id.text1};

                    // Creating a SimpleAdapter for the AutoCompleteTextView
                    SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), result, android.R.layout.simple_list_item_1, from, to);

                    // Setting the adapter
                    locationview.setAdapter(adapter);
                    break;
                case PLACES_DETAILS:
                    HashMap<String, String> hm = result.get(0);

                    // Getting latitude from the parsed data
                    final double latitude = Double.parseDouble(hm.get("lat"));

                    // Getting longitude from the parsed data
                    final double longitude = Double.parseDouble(hm.get("lng"));


                    //Toast.makeText(getApplicationContext(),"Latitude: "+ latitude+ "longitude: "+longitude,Toast.LENGTH_SHORT).show();

                    LatLng point = new LatLng(latitude, longitude);

                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(point, 14);
                    mMap.animateCamera(cameraUpdate);

                    //CameraUpdate cameraPosition = CameraUpdateFactory.newLatLng(point);
                    //CameraUpdate cameraZoom = CameraUpdateFactory.zoomBy(15);

                    // Showing the user input location in the Google Map
//                    googleMap.moveCamera(cameraPosition);
                    //                  googleMap.animateCamera(cameraZoom);


                   /* MarkerOptions options = new MarkerOptions();
                    options.position(point);
                    options.title("Position");
                    options.snippet(String.valueOf(point))
                            .draggable(true);
*/

                    /*googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                        @Override
                        public void onMarkerDragStart(Marker marker) {

                        }

                        @Override
                        public void onMarkerDrag(Marker marker) {


                        }

                        @Override
                        public void onMarkerDragEnd(Marker marker) {


                            LatLng p = marker.getPosition();
                            marker.remove();
                            CameraUpdate cameraPosition = CameraUpdateFactory.newLatLng(p);
                            googleMap.moveCamera(cameraPosition);

                            MarkerOptions options = new MarkerOptions();
                            options.position(p);
                            options.title("Position");
                            options.snippet(String.valueOf(p))
                                    .draggable(true);

                            googleMap.addMarker(options);
                        }
                    });*/


                    // Adding the marker in the Google Map
                    // googleMap.addMarker(options);

                    break;
            }
        }
    }

    ///MAKES THE URL TO MAKE THE HTTP REQUEST BY THE JSONPARSER CLASS
    public String makeURL(double sourcelat, double sourcelog, double destlat, double destlog) {
        StringBuilder urlString = new StringBuilder();
        urlString.append("http://maps.googleapis.com/maps/api/directions/json");
        urlString.append("?origin=");// from
        urlString.append(Double.toString(sourcelat));
        urlString.append(",");
        urlString
                .append(Double.toString(sourcelog));
        urlString.append("&destination=");// to
        urlString
                .append(Double.toString(destlat));
        urlString.append(",");
        urlString.append(Double.toString(destlog));
        urlString.append("&sensor=false&mode=driving&alternatives=true");
        return urlString.toString();
    }

    ///RETURNS A LIST OF LATLNG POINTS TO BE THEN MARKED TO CREATE ONE POLYLINE BY drawPoly function
    public List<LatLng> getPath(String result) {
        List<LatLng> list = new ArrayList<>();

        try {
            //Tranform the string into a json object
            final JSONObject json = new JSONObject(result);
            JSONArray routeArray = json.getJSONArray("routes");
            JSONObject routes = routeArray.getJSONObject(0);
            JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
            String encodedString = overviewPolylines.getString("points");
            list = decodePoly(encodedString);
            //return list;
        } catch (JSONException e) {

        }
        return list;
    }

    //// DRAWS A POLYLINE AFTER RECEIVING A LIST OF LATLNG POINTS
    public void drawpoly(List<LatLng> list) {
        for (int z = 0; z < list.size() - 1; z++) {
            LatLng src = list.get(z);
            LatLng dest = list.get(z + 1);
            Polyline line = mMap.addPolyline(new PolylineOptions()
                    .add(new LatLng(src.latitude, src.longitude), new LatLng(dest.latitude, dest.longitude))
                    .width(4)
                    .color(Color.BLUE).geodesic(true));
            Polyline add = line;
            lpoly.add(add);

        }
    }

    //USED BY GETPATH TO DECODE THE DATA RECEIVED
    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

    private class connectAsyncTask extends AsyncTask<Void, Void, String> {
        private ProgressDialog progressDialog;
        String url;
        Integer ia;
      /* connectAsyncTask(Integer i, String urlPass){
            url = urlPass;
            ia =i;
        }*/

        HashMap<Integer, String> h_url = new HashMap<>();
        HashMap<Integer, String> h_json = new HashMap<>();
//        HashMap<Integer, List<LatLng>> point_list = new HashMap<>();

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog = new ProgressDialog(MapsActivity.this);
            progressDialog.setMessage("Fetching route, Please wait...");
            progressDialog.setIndeterminate(true);
            Log.d("here","1");
            //progressDialog.show();

            for (int i = 0; i < markersarray.size() - 1; i++) {
                h_url.put(i, makeURL(markersarray.get(i).latitude, markersarray.get(i).longitude, markersarray.get(i + 1).latitude, markersarray.get(i + 1).longitude));
            }

        }

        @Override
        protected String doInBackground(Void... params) {
            for (int j = 0; j < markersarray.size() - 1; j++) {
                JSONParser jParser = new JSONParser();
                String json = jParser.getJSONFromUrl(h_url.get(j));
                List<LatLng> result = getPath(json);
                Log.d("SIZE= ",String.valueOf(markersarray.size()));
                point_list.put(j, result);
                Log.d("here","Did: "+j);

            }
            return new String();
            // return result;
            /*for(int j=0;j<markersarray.size()-1;j++) {

                h_json.put(j,json);
                List<LatLng> s = getPath(h_json.get(j));
                drawpoly(s);*/

            //   return json;
            //}
            // return new String();

        }

        protected void onPostExecute(String result) {
            Log.d("here","OnPost");

            new CountDownTimer((point_list.size()+3)*100, 100) {///dont know why increase +2,+3 works here..have to check
                int q=0;
                public void onFinish() {
                    // When timer is finished
                    // Execute your code here
                }

                public void onTick(long millisUntilFinished) {
                    if(q<markersarray.size()-1) {///////////////////////////////////////////////////////////////////////////////**************************************************************************
                        drawpoly(point_list.get(q));
                        q++;
                    }
                    // millisUntilFinished    The amount of time until finished.
                }
            }.start();
           /* for (int k = 0; k < markersarray.size() - 1; k++) {
                drawpoly(point_list.get(k));
                Log.d("here", "drawed" + k);
            }*/
            info.setText("Route Marked on Map");
            /*for(int k=0;k<markersarray.size()-1;k++)
            {

            }*/
            /*
            if(result!=null){
                List<LatLng> l  = getPath(result);
                drawpoly(l);
            }*/
            // progressDialog.hide();
            Toast.makeText(getApplicationContext(), "Done Routing", Toast.LENGTH_SHORT).show();
        }
    }
}



