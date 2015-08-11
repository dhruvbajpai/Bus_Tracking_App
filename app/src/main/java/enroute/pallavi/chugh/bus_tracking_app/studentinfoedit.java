package enroute.pallavi.chugh.bus_tracking_app;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.GetCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;


public class studentinfoedit extends ActionBarActivity implements View.OnClickListener{


    private GoogleMap mMap;
    int studentno;
    String name,phn,cls,root,longitutde,latitude,rowid,rollno,my_add;
    EditText etname,etphn,etcls,etroute,etroll;
    Float longi,latti;
    Button btsave,btedit;
    TextView address_info;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studentinfoedit);
        //getSupportActionBar().hide();
        toolbar = (Toolbar) findViewById(R.id.app_bar_s_edit);
        setSupportActionBar(toolbar);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        getSupportActionBar().setTitle("Student Info");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        longitutde =  mediator.longi.get(studentno).toString();
        latitude =  mediator.latti.get(studentno).toString();
        setUpMapIfNeeded();
        address_info = (TextView) findViewById(R.id.address_info);
        etname = (EditText)findViewById(R.id.etname);
        etphn = (EditText)findViewById(R.id.etphn);
        etroll = (EditText)findViewById(R.id.etroll);
        etcls = (EditText)findViewById(R.id.etcls);
        etroute = (EditText)findViewById(R.id.etroute);
        btsave = (Button)findViewById(R.id.save);
        btsave.setOnClickListener(this);
        btedit=(Button)findViewById(R.id.edit);
        btedit.setOnClickListener(this);



        if(savedInstanceState==null)
        {
            Bundle extras = getIntent().getExtras();
            if(extras==null)
            {
                studentno=0;
                root="0";
            }
            else
                studentno = extras.getInt("studentno");
                root=extras.getString("routeno");
                rowid=extras.getString("rowno");
        }else {
            studentno = (Integer) savedInstanceState.getSerializable("studentno");
            root = (String) savedInstanceState.getSerializable("routeno");
            rowid = (String) savedInstanceState.getSerializable("rowno");
        }
        name =  mediator.names.get(studentno).toString();    //names.get(position).toString();
        phn =  mediator.phone.get(studentno).toString();
        rollno = mediator.rollno.get(studentno).toString();
        cls =  mediator.cls.get(studentno).toString();



        etname.setText(name);
        etphn.setText(phn);
        etroll.setText(rollno);
        etcls.setText(cls);
        etroute.setText(root);
        address_info.setText(mediator.address.get(studentno).toString());



    }


    @Override
    protected void onResume() {
        super.onResume();

        longitutde =  mediator.longi.get(studentno).toString();
        latitude =  mediator.latti.get(studentno).toString();
        Double lat = Double.parseDouble(latitude);
        Double lon = Double.parseDouble(longitutde);
        address_info.setText(mediator.address.get(studentno).toString());
        //mMap.addMarker(new MarkerOptions().position(new LatLng(lat,lon)));
        setUpMap();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_studentinfoedit, menu);
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
            /*Intent i = new Intent(getApplicationContext(),parse_check.class);
            Integer j = studentno;
            String s = j.toString();
            i.putExtra("route",j);
            startActivity(i);*/
            finish();
        }
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        //Toast.makeText(this,"bla",Toast.LENGTH_SHORT).show();
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.my_map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        //Log.d("abc","map");
        mMap.clear();
        longi=Float.parseFloat(longitutde);
        latti=Float.parseFloat(latitude);
        //Log.d("longitude",longi.toString());
        //Log.d("latitude",latti.toString());
        //Toast.makeText(this,"bla1",Toast.LENGTH_SHORT).show();
        //Toast.makeText(getApplicationContext(),longi.toString(),Toast.LENGTH_SHORT).show();

        mMap.addMarker(new MarkerOptions().position(new LatLng(latti,longi)).title(name+"'s Stop")).showInfoWindow();
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latti+0.0005,longi), 16.0f));
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.save)
        {

            name=etname.getText().toString();
            phn=etphn.getText().toString();
            rollno=etroll.getText().toString();
            cls=etcls.getText().toString();
            root=etroute.getText().toString();
            latitude=mediator.latti.get(studentno);
            longitutde=mediator.longi.get(studentno);

            ParseQuery<ParseObject> query = ParseQuery.getQuery("r"+root);
            Log.d("abc",rowid);
// Retrieve the object by id
            query.getInBackground(rowid, new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject parseObject, com.parse.ParseException e) {
                    if (e == null) {
                        // Now let's update it with some new data. In this case, only cheatMode and score
                        // will get sent to the Parse Cloud. playerName hasn't changed.


                        parseObject.put("s_name",name);
                        parseObject.put("class",cls);
                        parseObject.put("rollno",rollno);
                        parseObject.put("latitude",latitude);
                        parseObject.put("longitude",longitutde);
                        parseObject.put("phone_no",phn);
                        parseObject.put("Address",mediator.address.get(studentno).toString());
                        parseObject.saveInBackground();
                        Toast.makeText(getApplicationContext(),"saved",Toast.LENGTH_SHORT).show();

                        Intent i = new Intent(getApplicationContext(),AdminPage.class);
                        startActivity(i);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"failed",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        if(v.getId()==R.id.edit)
        {
            int k =1;
            Intent i = new Intent(this,MapsActivity.class);
            i.putExtra("activityname",k);
            i.putExtra("position",studentno);
            startActivity(i);
        }
    }
}
