package enroute.pallavi.chugh.bus_tracking_app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;


public class parentactivity extends Activity {

    ProgressDialog mProgressDialog;
    static String route,studentid;
    static String sname,sclass,srollno,sphone,slatti,slongi,tusername,tname,tphone;
    static Bitmap studentimage,teacherimage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parentactivity);

        mProgressDialog = new ProgressDialog(parentactivity.this);
        // Set progressdialog title
        mProgressDialog.setTitle("Please Wait.." +
                "\nLoading Information");
        // Set progressdialog message
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setIndeterminate(false);
        // Show progressdialog
        mProgressDialog.show();


        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                route = "1";
            } else
                route= extras.getString("route");
        } else
            route= (String) savedInstanceState.getSerializable("route");


        //Log.d("asd", Auth_Diag.studentid);
        ParseQuery<ParseObject> query = ParseQuery.getQuery(route);
        //query.orderByAscending("s_name");
        query.getInBackground(Auth_Diag.studentid,new GetCallback<ParseObject>() {

            public void done(ParseObject scoreList, ParseException e) {
                if (e == null) {


                    sname=scoreList.getString("s_name").toString();
                    Log.d("asd",sname);
                    sclass=scoreList.getString("class").toString();
                    Log.d("asd",sclass);
                    srollno=scoreList.getString("rollno").toString();
                    Log.d("asd",srollno);
                    sphone=scoreList.getString("phone_no").toString();
                    Log.d("asd",sphone);
                    slatti=scoreList.getString("latitude").toString();
                    Log.d("asd",slatti);
                    slongi=scoreList.getString("longitude").toString();
                    Log.d("asd",slongi);

                    ParseFile fileObject = scoreList.getParseFile("photo");
                    if(fileObject==null)
                    {
                        studentimage = BitmapFactory.decodeResource(getResources(), R.drawable.profilepic);

                    }
                    else {
                        byte[] file = new byte[0];
                        try {
                            file = fileObject.getData();
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }
                        studentimage = BitmapFactory.decodeByteArray(file, 0, file.length);
                    }

                } else {
                    Toast.makeText(getApplicationContext(),"failed1",Toast.LENGTH_SHORT).show();
                }
            }

            });


        ParseQuery<ParseObject> query1 = ParseQuery.getQuery("route_teacher");
        query1.whereEqualTo("route_no", route.substring(1));
        //query.orderByAscending("s_name");
        query1.getFirstInBackground(new GetCallback<ParseObject>() {

            public void done(ParseObject scoreList, ParseException e) {
                if (e == null) {



                    //Intent i = new Intent(getApplicationContext(),parentpageone.class);
                    Intent i = new Intent(getApplicationContext(),Parent_first_page.class);
                    tusername=scoreList.getString("username").toString();
                    tname=scoreList.getString("teacher_name").toString();
                    tphone=scoreList.getString("phone").toString();
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
                    mProgressDialog.dismiss();
                    startActivity(i);
                } else {
                    Toast.makeText(getApplicationContext(),"failed2",Toast.LENGTH_SHORT).show();
                }
            }

        });

    }
}