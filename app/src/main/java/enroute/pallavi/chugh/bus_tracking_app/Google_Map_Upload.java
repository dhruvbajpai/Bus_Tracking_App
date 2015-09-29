package enroute.pallavi.chugh.bus_tracking_app;

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
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.TextView;
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

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import enroute.pallavi.chugh.bus_tracking_app.Classes.MyJSONParse;
import enroute.pallavi.chugh.bus_tracking_app.Classes.TimenDistance;

public class Google_Map_Upload extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    Location l;
    ProgressDialog dialog;
    ArrayList<Float> latitudes;
    ArrayList<Float> longitudes;
    Integer size;
    ArrayList<LatLng> markersarray;
    ArrayList<Marker> markers;
    boolean marker_flag = false;

    List<ParseObject> the_table = new ArrayList<>();

    String key1_dbcoolster = "AIzaSyCCVkfMFqSE838nAiMrRnx4kcoydtn4a-Y";
    String key2_wisemasses = "AIzaSyBLbR8skvduOHTOE4agMeDFhXj4QoUqt1g";
    boolean key_flag= false;
    int time_constraint = 300;// second for notifications
    int stop_done_counter = 0;   /// FOR NORMAL DAYS....WITHOUT PRIORITY CHECK DAY
    ArrayList<Integer> alreadyUpdatedPriorityList = new ArrayList<>();

    HashMap<Integer,TimenDistance> p_check_map = new HashMap<>();
    HashMap<Integer,TimenDistance> p_check_map_previous = new HashMap<>();
    int priority_to_put=1;
    int stop_done_on_priority_day=0;
    Location current_location = null;
    TextView t_distance, t_time;
    String route = "r4";
    List<Polyline> plist;
    //GetRouteTask getRoute;
    int cam_f = 0;
    boolean flag;
    // at location 0 the next hashmap specifies distance and time in
    HashMap<Integer, TimenDistance> hm = new HashMap<>();

    /// DISTANCE FINDING////////
    //GMapV2GetRouteDirection v2GetRouteDirection;
    Document document;
    LatLng fromPosition;
    LatLng toPosition;
    String distance;
    ParseObject ob;


      protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google__map__upload);
        setUpMapIfNeeded();
        LocationManager locationManager;

        t_distance = (TextView) findViewById(R.id.distance);
        t_time = (TextView) findViewById(R.id.time);


        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                route = "r3";
            } else
                route = extras.getString("route");

        } else
            route = (String) savedInstanceState.getSerializable("route");


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(true);
        criteria.setCostAllowed(true);
        String provider = locationManager.getBestProvider(criteria, true);
        latitudes = new ArrayList<Float>();
        longitudes = new ArrayList<Float>();
        markersarray = new ArrayList<LatLng>();
        markers = new ArrayList<Marker>();
        l = locationManager.getLastKnownLocation(provider);

        current_location = new Location(provider);
          current_location.setLatitude(28.694951);
          current_location.setLongitude(77.134583);
        plist = new ArrayList<Polyline>();
        //  v2GetRouteDirection = new GMapV2GetRouteDirection();


        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading Route information...");
        dialog.setIndeterminate(false);
        dialog.setMessage("Marking points on map");
        dialog.show();
        ParseQuery<ParseObject> query = ParseQuery.getQuery(route);
        //query.whereEqualTo("playerName", "Dan Stemkoski");
        query.orderByAscending("Priority"); ///////////////////// TO CHANGE FOR MORNING/EVENING
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> scoreList, ParseException e) {
                if (e == null) {
                    size = scoreList.size();
                    the_table = scoreList;
                    for (int j = 0; j < size; j++) {
                        Float lt = Float.parseFloat(scoreList.get(j).get("latitude").toString());

                        //latitudes.add(lt);

                        Float lg = Float.parseFloat(scoreList.get(j).get("longitude").toString());
                        //longitudes.add(lg);

                        markersarray.add(new LatLng(lt, lg));// array of the lat lng points to be taken in consideration for generating the POLYLINES.

                        markers.add(mMap.addMarker(new MarkerOptions().position(new LatLng(lt, lg))));


                    }
                    marker_flag = true;


                        addSchoolMarker();
                         Toast.makeText(getApplicationContext(), size.toString(), Toast.LENGTH_SHORT).show();


                    dialog.dismiss();
                    drawmarkers();
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    for (Marker marker : markers) {
                        builder.include(marker.getPosition());
                    }
                    LatLngBounds bounds = builder.build();
                    int padding = 50;
                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
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

                current_location = location;
                updateUI(location);
                /*
                mMap.clear();

                mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(),location.getLongitude())));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()),17.0f));
                */

               ParseQuery<ParseObject> query = ParseQuery.getQuery("liveloc");
                query.whereEqualTo("rootname", route);
                query.getFirstInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject parseObject, com.parse.ParseException e) {
                        if (e == null) {
                            // Now let's update it with some new data. In this case, only cheatMode and score
                            // will get sent to the Parse Cloud. playerName hasn't changed.
                            Double lat = location.getLatitude();
                            String latitude = lat.toString();
                            Double longi = location.getLongitude();
                            String longitude = longi.toString();

                            parseObject.put("longitude", longitude);
                            parseObject.put("latitude", latitude);
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
        locationManager.requestLocationUpdates(provider, 2000, 5, locationListener);
        //--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


        /////////////////////////////////////////////////////////////////////////--------------------MAIN LOGIC--------------------------------------/////////////////////////////////////////////////

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                if(isPriorityCheckDay())
                {
                    priorityCheckDay() ;
                    Log.d("TAG","priorityCheckDay() ;");
                }
                else {
                    calculateNotification();
                    Log.d("TAG","NORMAL DAY");
                    // Notification time and Google API query executes here
                }
               // calculateNotification();
            }
        });
        //thread.start();



    }


    public boolean isPriorityCheckDay()
    {
        List<ParseObject> results = new ArrayList<>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("p_check");
        try{
             results =query.find();

            for(int i=0;i<query.count();i++)
            {
                if (route.equals(results.get(i).get("name").toString()))
                {
                    Log.d("PDAY","TRUE");
                    return true;
                }
            }

        }catch (Exception e)
        {
            e.printStackTrace();
        }


        return false;

    }


    public void priorityCheckDay(){


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                stop_done_on_priority_day = 0; // to be increamented for stopping useless calculations... Stops done till now in the BUS RUN
                sleepUntilMarkerGenerated();
                Log.d("TILL HERE","1");
                int loop_count =0;


                while(true) //change this condition// till the time last location occurs
                {
                    loop_count++;       /// loop for the thread..  i.e maintains how many times the GET REQUEST WAS MADE
                    Location location = current_location;

                    Log.d("TILL HERE", "2");

                    for (int i = 0; i < markersarray.size(); i++) {
                        if (loop_count == 1) {
                            Log.d("TILL HERE", "3");
                            LatLng to = markersarray.get(i);
                            String url = makeURL(new LatLng(location.getLatitude(), location.getLongitude()), to);

                            String res = GET(url);

                            TimenDistance current = new TimenDistance(MyJSONParse.TimeInMins(res), MyJSONParse.DistanceInKm(res), MyJSONParse.TimeVal(res), MyJSONParse.Dist_val(res));
                            p_check_map.put(i, current);
                            p_check_map_previous.put(i,current);
                        } else if (loop_count > 1)// The previous HashMap has values .ie. Its not null
                        {
                            if (p_check_map.get(i).isPriority_set() == false) {
                                Log.d("TILL HERE", "3");
                                LatLng to = markersarray.get(i);
                                String url = makeURL(new LatLng(location.getLatitude(), location.getLongitude()), to);

                                String res = GET(url);

                                TimenDistance current = new TimenDistance(MyJSONParse.TimeInMins(res), MyJSONParse.DistanceInKm(res), MyJSONParse.TimeVal(res), MyJSONParse.Dist_val(res));

                                TimenDistance prev = p_check_map_previous.get(i);

                                p_check_map.put(i, average_object(prev, current));    /// UPDATE OF DISTANCE-TIME MAP by taking 2 value average
                                p_check_map_previous.put(i,current);
                                Log.d("TILL HERE", "4");
                                Log.d("TAG", "Distance of " + String.valueOf(i) + " is " + p_check_map.get(i).getD_values());
                            }

                        }


                        // p_check_map stores the Distance_Time data of the corresponding row of the route(based on index values)
                    }

                    sendSMSifRequired();

                    priorityUpdateCall();


                    try {
                        Thread.sleep(10000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }
        });
        thread.start();

    }

    public void sendSMSifRequired()
    {
        //CHECK FOR ALL AND SEND MESSAGE TO WHEREVER REQUIRED////
        for(int j=0;j<markersarray.size();j++)
        {
            Integer time = Integer.parseInt(p_check_map.get(j).getT_value());

            if(time<300&&p_check_map.get(j).isSmsSent()==false)
            {

                Log.d("TAG","Send message to "+ j);
                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    //smsManager.sendTextMessage("9910908279", null, "Your child "+the_table.get(j).get("s_name")+"will reach the bus stop in 5 mins", null, null);
                    //smsManager.sendTextMessage("9899881387", null, "Your child "+the_table.get(j).get("s_name")+"will reach the bus stop in 5 mins", null, null);
                    p_check_map.get(j).setSmsSent(true);
                    Toast.makeText(getApplicationContext(), "Message Sent",
                            Toast.LENGTH_LONG).show();
                } catch (Exception ex) {
                    Toast.makeText(getApplicationContext(),
                            ex.getMessage().toString(),
                            Toast.LENGTH_LONG).show();
                    ex.printStackTrace();
                }
            }
        }
        //CHECK FOR ALL AND SEND MESSAGE TO WHEREVER REQUIRED////
    }

    public TimenDistance average_object(TimenDistance a1, TimenDistance a2)
    {


        Integer avg_dis_val = (Integer.parseInt(a1.getD_values())+Integer.parseInt(a2.getD_values()))/2;

        Integer avg_time_val = (Integer.parseInt(a1.getT_value()))+Integer.parseInt(a2.getT_value())/2;



        String time = (int)avg_time_val/60 + " min";

        String distance = avg_dis_val/1000 +" km";

        boolean a1_p = a1.isPriority_set();
        boolean a2_p = a2.isPriority_set();

        boolean a1_sms = a1.isSmsSent();
        boolean a2_sms = a2.isSmsSent();

        TimenDistance result = new TimenDistance(time,distance,String.valueOf(avg_time_val),String.valueOf(avg_dis_val));

        result.setSmsSent(a1_sms||a2_sms);
        result.setPriority_set(a1_p || a2_p);// if one of the object i.e. previous object has priority set(= true) then new updated object should also have that.

        return result;
    }

    public void priorityUpdateCall()
    {
        for(int i=0;i<p_check_map.size();i++)
        {
            Integer distance = Integer.parseInt(p_check_map.get(i).getD_values());
            Log.d("TAG","Distance to check with 200 is : "+ distance);
            if(distance<200)
            {
                p_check_map.get(i).setPriority_set(true);
                updatePriority(i);

               /* if(!alreadyUpdatedPriorityList.contains(i)) {
                    updatePriority(i);
                    alreadyUpdatedPriorityList.add(i);
                }*/
                ///DO SOMETHING ABOUT THE stop_update_counter here... it has to be not just incremented..it has to be changed..something has to be done here..
            }
        }
    }

    public void updatePriority(int index_on_db)
    {
        List<ParseObject> objectList = new ArrayList<>();

        ParseQuery<ParseObject> query = ParseQuery.getQuery(route);
        query.orderByAscending("Priority");
        try{
            objectList = query.find();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        Log.d("Priority PUT","Priority: "+ priority_to_put+" queued");
        objectList.get(index_on_db).put("Priority",priority_to_put);
        objectList.get(index_on_db).saveInBackground();
        Log.d("Priority PUT","Priority: "+ priority_to_put+" uploaded");

        priority_to_put++;
    }



    public void sleepUntilMarkerGenerated() {
        while (!marker_flag) {
            Log.d("TAG", String.valueOf(marker_flag));
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private void calculateNotification() {
        Thread thread =   new Thread(new Runnable() {
            @Override
            public void run() {

                Log.d("TAG MAKER FLAG",String.valueOf(marker_flag));
                ///THREAD TO RUN ONLY AFTER MARKERS ARE SET

                sleepUntilMarkerGenerated();


                while (stop_done_counter < markersarray.size()) {

                    int first = stop_done_counter;// the index values for getting location coordinates of the next two stops from markersarray
                    int second = first + 1;

                    LatLng l_first = markersarray.get(first);
                    LatLng l_second = markersarray.get(second);

                    LatLng from = new LatLng(current_location.getLatitude(), current_location.getLongitude());

                    String first_url = makeURL(from, l_first);
                    Log.d("TAG","from: "+from + " to: "+ l_first+" URL: "+ first_url);
                    new HttpTask().execute("0", first_url);

                    String second_url = makeURL(from, l_second);
                    new HttpTask().execute("1", second_url);

                    //if(Integer.parseInt(hm.get(0).getT_value())<200)

                    try {
                        Thread.sleep(10000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }


            }
        });

        thread.start();
    }

    private void addSchoolMarker() {
        Marker school = mMap.addMarker(new MarkerOptions().position(new LatLng(28.689224, 77.121460)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)).title("School"));
        school.showInfoWindow();
        markers.add(school);
    }

    public  String makeURL(LatLng from, LatLng to) {
        String base = "https://maps.googleapis.com/maps/api/distancematrix/json?";

        String from_lat = String.valueOf(from.latitude);
        String from_lon = String.valueOf(from.longitude);

        String to_lat = String.valueOf(to.latitude);
        String to_lon = String.valueOf(to.longitude);

        String origin = base + "origins=" + from_lat + "," + from_lon;//"28.694326,77.149902";
        // String origin = base +"origins=" + "28.580037,77.183290";//"28.694326,77.149902";

        String key = "AIzaSyCCVkfMFqSE838nAiMrRnx4kcoydtn4a-Y";

        String destination = "";

        if(!key_flag){
             destination = origin + "&destinations=" + to_lat + "," + to_lon + "&key=" + key1_dbcoolster; //"AIzaSyCCVkfMFqSE838nAiMrRnx4kcoydtn4a-Y";//"AIzaSyBLbR8skvduOHTOE4agMeDFhXj4QoUqt1g";-----"28.703944,77.146044"
            key_flag=true;
        }else
        {
            destination = origin + "&destinations=" + to_lat + "," + to_lon + "&key=" + key2_wisemasses; //"AIzaSyCCVkfMFqSE838nAiMrRnx4kcoydtn4a-Y";//"AIzaSyBLbR8skvduOHTOE4agMeDFhXj4QoUqt1g";-----"28.703944,77.146044"
            key_flag=false;
        }



        //|28.580037,77.183290
        //AIzaSyCCVkfMFqSE838nAiMrRnx4kcoydtn4a-Y
        //  String result = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=Vancouver+BC|Seattle&destinations=San+Francisco|Victoria+BC&mode=bicycling&language=fr-FR";
        return destination;
    }

    public static String GET(String url) {
        InputStream inputStream = null;
        String result = "";

        try {
            //create HTTP client
            HttpClient httpClient = new DefaultHttpClient();

            //make a get request to the given URL
            HttpResponse httpResponse = httpClient.execute(new HttpGet(url));

            //receive response as inputstream
            inputStream = httpResponse.getEntity().getContent();

            //convert inputstream to string
            if (inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String convertInputStreamToString(InputStream inputStream) throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null) {
            result += line;
        }
        inputStream.close();
        return result;

    }

    public class HttpTask extends AsyncTask<String, Void, Void> {
        String res = null, abc;

        int stop_number = 0;

        @Override
        protected Void doInBackground(String... params) {

            stop_number = Integer.parseInt(params[0]);
            res = GET(params[1]);//the url

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            try {
                //JSONArray jsonArray = new JSONArray(res);
               /* JSONObject jsonObject = new JSONObject(res);
                //abc = jsonObject.getString("destination_addresses");

                JSONArray jo = jsonObject.getJSONArray("rows");


                JSONObject j = jo.getJSONObject(0);

                JSONArray param = j.getJSONArray("elements");

                JSONObject elements = param.getJSONObject(0);

                JSONObject d_time = elements.getJSONObject("duration");

                JSONObject d_distance = elements.getJSONObject("distance");*/

                String time = MyJSONParse.TimeInMins(res);
                String distance = MyJSONParse.DistanceInKm(res);
                String t_value = MyJSONParse.TimeVal(res);
                String d_value = MyJSONParse.Dist_val(res);

                hm.put(stop_number, new TimenDistance(time, distance, t_value, d_value));


                if (Integer.parseInt(hm.get(stop_number).getT_value()) < time_constraint)// value of first
                    Toast.makeText(getApplicationContext(), "5 mins left to first location", Toast.LENGTH_SHORT).show();

                if(stop_number==0) {
                    t_distance.setText(d_value+ "m");
                    t_time.setText(t_value+ "sec");
                }


              /*  if (Integer.parseInt(hm.get(1).getT_value()) < time_constraint)// value of first
                    Toast.makeText(getApplicationContext(), "5 mins left to second location", Toast.LENGTH_SHORT).show();*/
                //abc = jo.getString("")
                /*for(int i=0;i<jsonArray.length();i++)
                {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                     abc = jsonObject.getJSONObject("rows").getJSONObject("elements").getJSONObject("duration").getString("text");
                }*/
                Log.d("TAG", "Time to "+ stop_number+" : " + time.toString() + "  Distance: " + distance);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //tv.setText(abc);
            super.onPostExecute(aVoid);
        }
    }




    /////////////////////////////////////////////////////////////////////////--------------------MAIN LOGIC--------------------------------------/////////////////////////////////////////////////
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

   /* private String getDirectionsUrl(LatLng origin, LatLng dest) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Sensor enabled
        String sensor = "sensor=false";
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        return url;
    }*/


    public void drawmarkers() {

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
        //for (int j = 0; j < size; j++) {
        //    LatLng pos = markersarray.get(j);
        //    mMap.addMarker(new MarkerOptions().position(new LatLng(pos.latitude, pos.longitude)));

        //}

    }
    public void updateUI(Location location) {
        mMap.clear();
        drawmarkers();


        mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 17.0f));
    }
}
