package enroute.pallavi.chugh.bus_tracking_app.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import enroute.pallavi.chugh.bus_tracking_app.R;
import enroute.pallavi.chugh.bus_tracking_app.parentactivity;


public class DriverDetails extends android.support.v4.app.Fragment {

    private String mParam1;
    private String mParam2;
    TextView tv_name,tv_phone,tv_route;




    public DriverDetails() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

            View v=inflater.inflate(R.layout.fragment_driver_details,container,false);

            tv_name = (TextView)v.findViewById(R.id.name);
            tv_phone = (TextView)v.findViewById(R.id.phn);
            tv_route = (TextView)v.findViewById(R.id.route);

            tv_name.setText(parentactivity.driver_name);
            tv_phone.setText(parentactivity.driver_phone);
            tv_route.setText(parentactivity.driver_route);
            return v;
    }






}
