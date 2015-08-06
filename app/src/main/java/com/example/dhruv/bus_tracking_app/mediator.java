package com.example.dhruv.bus_tracking_app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


public class mediator extends Activity {

    Integer route_no;
    String route;
    ProgressDialog mProgressDialog;
    String[] name;
    String[] numbers;
    int count=0;
    Intent u;
    List<ParseObject> ob;
    static String tr = "p";
    static ArrayList<String> names,phone,cls,longi,latti,rollno,address;
    static List<ParseObject> the_route;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mediator);
//        getSupportActionBar().hide();
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                route_no = 1;
            } else
                route_no = extras.getInt("rt_number");

        } else
            route_no = (Integer) savedInstanceState.getSerializable("rt_number");

        route_no++;
        route = route_no.toString();

        Log.d("a",route);
        //////////////////////////////////////////////////////////////////////////////////////////
        names = new ArrayList<String>();
        phone = new ArrayList<String>();
        cls = new ArrayList<String>();
        address = new ArrayList<String>();
        longi = new ArrayList<String>();
        latti = new ArrayList<String>();
        rollno = new ArrayList<String>();
        /////////////////////////////////////////////////////////////////////////////////////////
        Toast.makeText(this,"ROUTE " + route, Toast.LENGTH_SHORT).show();
        //-------------------------------------------------------MY WAY--------------------------
        mProgressDialog = new ProgressDialog(mediator.this);
        // Set progressdialog title
        mProgressDialog.setTitle("Please Wait.." +
                "\nLoading Student Information");
        // Set progressdialog message
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setIndeterminate(false);
        // Show progressdialog
        mProgressDialog.show();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("r"+route);
        //query.whereEqualTo("playerName", "Dan Stemkoski");
        query.orderByAscending("s_name");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> scoreList, ParseException e) {
                if (e == null) {
                    Integer i = scoreList.size();
                    String s = i.toString();
                    Toast.makeText(getApplicationContext(),s+" students",Toast.LENGTH_SHORT).show();
                    the_route = scoreList;
                    if(Route_info.i==1) {
                        u = new Intent(getApplicationContext(), parse_check.class);
                    }
                    else if(Route_info.i==2)
                    {
                        u = new Intent(getApplicationContext(), teacherinfoedit.class);
                    }
                    for(int k=0;k<i;k++)
                    {
                        names.add(scoreList.get(k).get("s_name").toString());
                        cls.add(scoreList.get(k).get("class").toString());
                        latti.add(scoreList.get(k).get("latitude").toString());
                        longi.add(scoreList.get(k).get("longitude").toString());
                        rollno.add(scoreList.get(k).get("rollno").toString());
                        address.add(scoreList.get(k).get("Address").toString());
                        //Toast.makeText(get)
                        //Log.d("latitude_med",latti.get(0).toString()+scoreList.get(k).get("latitude").toString());
                        //Log.d("longitude_med",longi.get(0).toString()+scoreList.get(k).get("longitude").toString());
                        phone.add(scoreList.get(k).get("phone_no").toString());
                    }

                    //Log.d("ad",latti.toString());

                    Toast.makeText(getApplicationContext(), names.get(1),Toast.LENGTH_SHORT).show();
                    //u.putStringArrayListExtra("names_list",names);
                    //u.putStringArrayListExtra("phone_list",phone);
                    u.putExtra("name_list", names);
                    u.putExtra("phone_list",phone);
                    u.putExtra("route",route);
                    u.putExtra("roll",rollno);
                    mProgressDialog.dismiss();
                    startActivity(u);
                    Log.d("score", "Retrieved " + scoreList.size() + " scores");
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });





    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mediator, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public class RemoteDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(mediator.this);
            // Set progressdialog title
            mProgressDialog.setTitle("Parse.com Custom ListView Tutorial");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // Create the array

            try {
                // Locate the class table named "Country" in Parse.com
                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                        "r"+route);
                // Locate the column named "ranknum" in Parse.com and order list
                // by ascending
                query.orderByAscending("s_name");
                ob = query.find();
                for (ParseObject student : ob) {
                    // Locate images in flag column
                    name[count] = student.get("s_name").toString();
                    numbers[count] = student.get("phone_no").toString();
                    count++;

                    /*ParseQuery<ParseObject> query1 = ParseQuery.getQuery("User");
                    query1.whereEqualTo("username", Route_info.teachuser);
                    query1.getFirstInBackground(new GetCallback<ParseObject>() {
                        public void done(ParseObject object, ParseException e) {
                            if (object == null) {
                                Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_SHORT).show();
                                Log.d("score", "The getFirst request failed.");
                            } else {
                                teachpass = object.getString("password");
                                //Log.d("score", "Retrieved the object.");
                            }
                        }
                    });*/


                }
            } catch (ParseException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
                Integer i = name.length;
            String s = i.toString();

            Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
            mProgressDialog.dismiss();
        }
    }
}

