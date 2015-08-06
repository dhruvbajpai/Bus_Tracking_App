package com.example.dhruv.bus_tracking_app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by NETAN ARPIT on 2/15/2015.
 */
public class fragstudent extends android.support.v4.app.Fragment {

    TextView name,cls,phn,root,rollno;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_fragstudent,container,false);

        name= (TextView)v.findViewById(R.id.name);
        cls= (TextView)v.findViewById(R.id.cls);
        phn= (TextView)v.findViewById(R.id.phn);
        root= (TextView)v.findViewById(R.id.route);
        rollno= (TextView)v.findViewById(R.id.rollno);

        name.setText(parentactivity.sname);
        cls.setText(parentactivity.sclass);
        phn.setText(parentactivity.sphone);
        root.setText(parentactivity.route.substring(1));
        rollno.setText(parentactivity.srollno);


        return v;


    }
}
