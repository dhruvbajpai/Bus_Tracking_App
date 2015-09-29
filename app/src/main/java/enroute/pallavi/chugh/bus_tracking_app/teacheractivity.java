package enroute.pallavi.chugh.bus_tracking_app;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


public class teacheractivity extends Activity {

    ProgressDialog mProgressDialog;
    static String route,studentid;
    static String tusername,tname,tphone,tpass,troute;
    static Bitmap studentimage,teacherimage;
    static ArrayList<String> names,phone,cls,longi,latti,rollno,address;
    static List<ParseObject> the_route;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacheractivity);


        //////////////////////////////////////////////////////////////////////////////////////////
        names = new ArrayList<String>();
        phone = new ArrayList<String>();
        cls = new ArrayList<String>();
        address = new ArrayList<String>();
        longi = new ArrayList<String>();
        latti = new ArrayList<String>();
        rollno = new ArrayList<String>();
        /////////////////////////////////////////////////////////////////////////////////////////



        mProgressDialog = new ProgressDialog(teacheractivity.this);
        // Set progressdialog title
        mProgressDialog.setTitle("Please Wait.." +
                "\nLoading Information");
        // Set progressdialog message
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setIndeterminate(false);
        // Show progressdialog
        mProgressDialog.show();


/*
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                route = "1";
            } else
                route= extras.getString("route");
        } else
            route= (String) savedInstanceState.getSerializable("route");
*/


        //Log.d("asd", Auth_Diag.studentid);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("route_teacher");
        //query.orderByAscending("s_name");
        query.getInBackground(Auth_Diag.teacherid,new GetCallback<ParseObject>() {

            public void done(ParseObject scoreList, ParseException e) {
                if (e == null) {


                    tusername=scoreList.getString("username");
                    Log.d("asd",tusername);
                    tpass=scoreList.getString("password");
                    Log.d("asd",tpass);
                    tname=scoreList.getString("teacher_name");
                    Log.d("asd",tname);
                    tphone=scoreList.getString("phone");
                    Log.d("asd",tphone);
                    troute=scoreList.getString("route_no");
                    Log.d("asd",troute);
                    ParseFile fileObject1 = scoreList.getParseFile("photo");
                    if(fileObject1==null)
                    {
                        teacherimage = BitmapFactory.decodeResource(getResources(), R.drawable.profilepic);

                    }
                    else {
                        byte[] file = new byte[0];
                        try {
                            file = fileObject1.getData();
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }
                        teacherimage = BitmapFactory.decodeByteArray(file, 0, file.length);
                    }
                } else {
                    Toast.makeText(getApplicationContext(),"failed1",Toast.LENGTH_SHORT).show();
                }
            }

        });


        ParseQuery<ParseObject> query1 = ParseQuery.getQuery("r1");
        //query.whereEqualTo("playerName", "Dan Stemkoski");
        query1.orderByAscending("s_name");
        query1.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> scoreList, ParseException e) {
                if (e == null) {
                    Integer i = scoreList.size();
                    String s = i.toString();
                    Toast.makeText(getApplicationContext(),s+" students",Toast.LENGTH_SHORT).show();
                    the_route = scoreList;
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
                    Intent k = new Intent(getApplicationContext(),teacher_first_page.class);
                    mProgressDialog.dismiss();
                    startActivity(k);
                    Log.d("score", "Retrieved " + scoreList.size() + " scores");
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });



    }
}