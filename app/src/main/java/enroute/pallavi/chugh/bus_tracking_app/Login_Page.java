package enroute.pallavi.chugh.bus_tracking_app;

import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.blunderer.materialdesignlibrary.activities.ViewPagerActivity;
import com.blunderer.materialdesignlibrary.handlers.ActionBarHandler;
import com.blunderer.materialdesignlibrary.handlers.ViewPagerHandler;
import com.rey.material.widget.CheckBox;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;





public class Login_Page extends Activity implements View.OnClickListener {


    EditText user, pass;
    Button signin, register;
    android.app.ProgressDialog loading;
    SharedPreferences preferences;
    CheckBox ck,auto;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login__page);
        user = (EditText) findViewById(R.id.username);
        pass = (EditText) findViewById(R.id.password);
        signin = (Button) findViewById(R.id.signin);
        ck = (CheckBox) findViewById(R.id.rem_me);
        auto = (CheckBox) findViewById(R.id.automatic);
        register = (Button) findViewById(R.id.register);
        signin.setOnClickListener(this);
        register.setOnClickListener(this);
        Log.d("TAG", "here");
        //ck.setChecked(true);
        // auto.setChecked(true);
        // auto.setChecked(false);
        //Log.d("TAG", String.valueOf(ck.isCheck()));
        preferences = getSharedPreferences("preference", Context.MODE_PRIVATE);
        if (preferences.contains("username")) {
            user.setText(preferences.getString("username", ""));
        }
        if (preferences.contains("password")) {
            pass.setText(preferences.getString("password", ""));
        }


        if (preferences.contains("ischeck")) {
            if (preferences.getBoolean("ischeck", true))
                ck.setChecked(true);
            else
                ck.setChecked(false);
        }
        if (preferences.contains("isauto"))// for automatic sign in
        {
            if (preferences.getBoolean("isauto", false)) {
                auto.setChecked(true); // to show automatic check box ticked
                if (preferences.getBoolean("ischeck", false)) {
                    String u = preferences.getString("username", "");
                    String p = preferences.getString("password", "");
                    login(u, p);// automatically logged in
                }

            } else
                auto.setChecked(false);
        }
    }







    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login__page, menu);
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
        switch (v.getId()) {
            case R.id.signin:
               // Toast.makeText(this, "Signin", Toast.LENGTH_SHORT).show();

                login(user.getText().toString(), pass.getText().toString());
                break;
            case R.id.register:

              //  ck.setChecked(true);

                Toast.makeText(this, "Register", Toast.LENGTH_SHORT).show();
                break;
        }


    }

    public void login(String username, String password) {

        loading = new android.app.ProgressDialog(this);
        loading.show();
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    String s = user.get("type").toString();
                    Log.d("asd", s);

                    if (s.equals("admin")) {

                        ParseQuery<ParseObject> query = ParseQuery.getQuery("liveloc");
                        try {

                            Auth_Diag.noofroute = query.count();//to count the number of routes using the liveloc table

                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }
                        loading.dismiss();

                        rememberme(ck.isChecked(),auto.isChecked());
                        Intent i = new Intent(getApplicationContext(), AdminPage.class);
                        startActivity(i);
                    }
                    if (s.equals("parent")) {// if the account is a parent account then it has a student id associated with it in the USER TABLE.
                        Auth_Diag.studentid = user.get("studentid").toString();// if its not a parent account then it the studentId field in the user table holds NULL value.
                        Intent i = new Intent(getApplicationContext(), parentactivity.class);

                        i.putExtra("route", user.get("route").toString());

                        rememberme(ck.isChecked(), auto.isChecked());
                        loading.dismiss();
                        startActivity(i);
                    }

                    if (s.equals("teacher")) {// if the account is a parent account then it has a student id associated with it in the USER TABLE.
                        Auth_Diag.teacherid = user.get("teacherid").toString();// if its not a parent account then it the studentId field in the user table holds NULL value.
                        Intent i = new Intent(getApplicationContext(), teacheractivity.class);
                        rememberme(ck.isChecked(),auto.isChecked());
                        loading.dismiss();
                        startActivity(i);
                    }

                    if (s.equals("driver")) {// if the account is a parent account then it has a student id associated with it in the USER TABLE.
                        //String route=user.get("route").toString();// if its not a parent account then it the studentId field in the user table holds NULL value.
                        Intent i = new Intent(getApplicationContext(), Google_Map_Upload.class);

                        i.putExtra("route", user.get("route").toString());
                        rememberme(ck.isChecked(),auto.isChecked());
                        loading.dismiss();
                        startActivity(i);
                    }
                    if (s.equals("loader")) {// if the account is a parent account then it has a student id associated with it in the USER TABLE.
                        //String route=user.get("route").toString();// if its not a parent account then it the studentId field in the user table holds NULL value.
                        Intent i = new Intent(getApplicationContext(), LoaderStart.class);
                        ParseQuery<ParseObject> query = ParseQuery.getQuery("liveloc");
                        int q=0;
                        try {// put internet not working clause
                            q = query.count();
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }
                        i.putExtra("count",q);
                        //i.putExtra("route", user.get("route").toString());
                        rememberme(ck.isChecked(),auto.isChecked());
                        loading.dismiss();
                        startActivity(i);
                    }
                } else {
                    loading.dismiss();
                    //Activity a = new Activity();


//                    snackbar.show();
                    Toast.makeText(getApplicationContext(), "Sign In Failed", Toast.LENGTH_SHORT).show();

                    // Signup failed. Look at the ParseException to see what happened.
                }
            }
        });

    }

    private void rememberme(Boolean b, Boolean auto) {

        if(b)
        {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("username",user.getText().toString());
        editor.putString("password",pass.getText().toString());
        editor.putBoolean("ischeck",true);
        editor.commit();

    }
        else
        {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("username","");
            editor.putString("password","");
            editor.putBoolean("ischeck",false);
            editor.commit();
        }
        if(auto)
        {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("isauto", true);
            editor.commit();
        }
        else
        {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("isauto", false);
            editor.commit();
        }

        }
}
