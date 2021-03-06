package enroute.pallavi.chugh.bus_tracking_app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;


public class Route_info extends ActionBarActivity {
    static int backpage;
    static Integer route_no = 0;
    static String route, teacher, teachphn, teachuser, teachid, teachpass;
    ProgressDialog d;
    String teachphotoname;
    TextView rt, tchr_name,std_cnt;
    Button std, bttch, viewlive,p_day_register;
    static int i;
    static Bitmap image;
    Toolbar toolbar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_info);
        toolbar = (Toolbar) findViewById(R.id.app_bar_rt_info);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Route Information");
        //getSupportActionBar().hide();
        std = (Button) findViewById(R.id.students);
        p_day_register = (Button)findViewById(R.id.p_day);
        p_day_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("p_check");
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> parseObjects, ParseException e) {
                        boolean flag = true;// for checking if the p_check table already contains the given route or not
                        for(int i=0;i<parseObjects.size();i++)
                        {
                            if(parseObjects.get(i).get("name").toString().equals("r"+route_no.toString()))
                            {
                                flag = false;
                            }
                        }
                        if (flag)
                        {
                            ParseObject p = new ParseObject("p_check");
                            p.put("name","r"+route_no.toString());
                            p.saveInBackground();
                            Toast.makeText(getApplicationContext(),"Registered for Route Recalculation",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"This route already Registered",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        std_cnt = (TextView) findViewById(R.id.student_count);
        std.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = 1;
                mediator.tr_flag=0;
                Intent i = new Intent(getApplicationContext(), mediator.class);
                i.putExtra("rt_number", route_no);
                startActivity(i);
            }
        });

        bttch = (Button) findViewById(R.id.btteach);
        bttch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = 2;
                Intent i = new Intent(getApplicationContext(), mediator.class);
                i.putExtra("rt_number", route_no);
                startActivity(i);
            }
        });
        viewlive = (Button) findViewById(R.id.live);
        viewlive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backpage = 1;
                Intent i = new Intent(getApplicationContext(), parentmapfull.class);
                startActivity(i);
            }
        });
        //   Parse.enableLocalDatastore(this);
        // Parse.initialize(this, "1Jrskl4dgS112TdPVInJXwVNr8z5OXjWX0ZwKhOo", "2dGzy0gSBLmwffakLISmKlUN1Nkzhgw3gqkWLGlZ");
        if (mediator.tr_flag == 0) {
            overridePendingTransition(R.anim.slidefromleft, R.anim.slidetoleft);
        } else if (mediator.tr_flag == 1) {
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_right);
        }


        rt = (TextView) findViewById(R.id.text2);
        tchr_name = (TextView) findViewById(R.id.teacher_name);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                route_no = 1;
            } else
                route_no = extras.getInt("rt_number");

        } else
            route_no = (Integer) savedInstanceState.getSerializable("rt_number");

        //route  = "1";//route_no.toString();
        rt.setText(route_no.toString());
        d = new ProgressDialog(this);
        d.setIndeterminate(false);
        d.setMessage("Loading Route Info");
        d.setTitle("Server Connect");
        d.show();
        ParseQuery<ParseObject> q = ParseQuery.getQuery("r"+route_no.toString());
        try {
            int cnt = q.count();

            std_cnt.setText(String.valueOf(cnt));

        } catch (Exception e) {
            std_cnt.setText("No Count available");
            e.printStackTrace();
        }
        ParseQuery<ParseObject> query = ParseQuery.getQuery("route_teacher");
        query.whereEqualTo("route_no", route_no.toString());
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (object == null) {
                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                    Log.d("score", "The getFirst request failed.");
                } else {

                    teachuser = object.getString("username");

                    teachpass = object.getString("password");
                    teachid = object.getObjectId();
                    teacher = object.getString("teacher_name");
                    teachphn = object.getString("phone");

                    tchr_name.setText(teacher);
                    Log.d("score", "Retrieved the object.");

                    ParseFile fileObject = object.getParseFile("photo");
                    if (fileObject == null) {
                        image = BitmapFactory.decodeResource(getResources(), R.drawable.profilepic);

                    } else {
                        byte[] file = new byte[0];
                        try {
                            file = fileObject.getData();
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }
                        image = BitmapFactory.decodeByteArray(file, 0, file.length);
                    }

                }
                d.dismiss();
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_route_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //android:parentActivityName="enroute.pallavi.chugh.bus_tracking_app.AdminPage"
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                mediator.tr_flag = 1;
                //Intent i = new Intent(Route_info.this,AdminPage.class);
                //startActivity(i);
                //finish();
                //Route_info.this.overridePendingTransition(R.anim.slide_from_right,R.anim.slide_to_right);
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_settings:
                return true;

        }


        return super.onOptionsItemSelected(item);
    }
}
