package enroute.arpit.dhruv.bus_tracking_app;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

//import com.parse.ParseObject;


public class MainActivity extends Activity implements View.OnClickListener {

    Button b1,b2,b3,b4;
    static String type;
    Toolbar toolbar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Enable Local Datastore.
        /*toolbar = (Toolbar) findViewById(R.id.app_bar_rt_admin);
        setSupportActionBar(toolbar);
        getSupportActionBar().hide();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/



        //Parse.enableLocalDatastore(this);
        //Parse.initialize(this, "1Jrskl4dgS112TdPVInJXwVNr8z5OXjWX0ZwKhOo", "2dGzy0gSBLmwffakLISmKlUN1Nkzhgw3gqkWLGlZ");

        /*ParseObject testObject = new ParseObject("TestObject");
        testObject.put("foo", "bar");
        testObject.saveInBackground();*/

      /*  ParseUser user = new ParseUser();
        user.setUsername("dbcoolster");
        user.setPassword("password");
        user.setEmail("dbcoolster@gmail.com");
        user.put("type","admin");
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e==null)
                {
                    Toast.makeText(getBaseContext(),"Signed UP",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getBaseContext(),"Signup Failed",Toast.LENGTH_SHORT).show();
                }
            }
        });*/



       // this.getSupportActionBar().hide();
        b1 = (Button) findViewById(R.id.admin_mode);
        b1.setOnClickListener(this);
        b2 = (Button) findViewById(R.id.teacher_mode);
        b2.setOnClickListener(this);
        b3 = (Button) findViewById(R.id.parent_mode);
        b3.setOnClickListener(this);
        b4 = (Button) findViewById(R.id.driver_mode);
        b4.setOnClickListener(this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
    @Override
    public void onClick(View v) {
        Toast.makeText(this,"Button Clicked",Toast.LENGTH_SHORT).show();
        //Dialog dialog = new Dialog(
        Auth_Diag dialog = new Auth_Diag(this);
        dialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;

        dialog.getWindow();
        dialog.show();


        switch(v.getId())
        {
            case R.id.admin_mode:
                type="admin";
                break;
            case R.id.teacher_mode:
                type="teacher";
                break;
            case R.id.parent_mode:
                type="parent";
                break;
            case R.id.driver_mode:
                type="driver";
                break;


        }
    }
}


