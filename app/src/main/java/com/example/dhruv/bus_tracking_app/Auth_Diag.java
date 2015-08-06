package com.example.dhruv.bus_tracking_app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

/**
 * Created by Dhruv on 14-Jul-15.
 */
public class Auth_Diag extends Dialog {


    public Activity c;
    public Dialog d;
    public Button ok;
    String usr, pass, getUser, getPass;
    EditText u, p;

    public Auth_Diag(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.auth_diag);
        u = (EditText) findViewById(R.id.username);
        p = (EditText) findViewById(R.id.password);
        ok = (Button) findViewById(R.id.btnok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ParseUser.logInInBackground(u.getText().toString(), p.getText().toString(), new LogInCallback() {
                    public void done(ParseUser user, ParseException e) {
                        if (user != null) {
                            Intent i = new Intent(c.getApplicationContext(),AdminPage.class);
                            c.startActivity(i);

                        } else {
                            Toast.makeText(c.getApplicationContext(),"Sign In Failed",Toast.LENGTH_SHORT).show();

                            // Signup failed. Look at the ParseException to see what happened.
                        }
                    }
                });


            }
        });

        //ok.setOnClickListener(this);

    /*public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.auth_diag,null);
        builder.setView(v);

        Dialog dialog = builder.create();
        dialog.

        return dialog;

    }*/
    }

}