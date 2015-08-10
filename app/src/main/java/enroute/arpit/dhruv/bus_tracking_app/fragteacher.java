package enroute.arpit.dhruv.bus_tracking_app;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by NETAN ARPIT on 2/15/2015.
 */
public class fragteacher extends android.support.v4.app.Fragment {

    TextView name,phn,route;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_fragteacher,container,false);

        name = (TextView)v.findViewById(R.id.name);
        phn = (TextView)v.findViewById(R.id.phn);
        route = (TextView)v.findViewById(R.id.route);

        name.setText(parentactivity.tname);
        phn.setText(parentactivity.tphone);
        route.setText(parentactivity.route.substring(1));

        return v;
    }
}
