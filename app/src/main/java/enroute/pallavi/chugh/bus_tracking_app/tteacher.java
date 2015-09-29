package enroute.pallavi.chugh.bus_tracking_app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by NETAN ARPIT on 2/15/2015.
 */
public class tteacher extends android.support.v4.app.Fragment {

    TextView name,phn,route,username;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_tteacher,container,false);

        name = (TextView)v.findViewById(R.id.name);
        phn = (TextView)v.findViewById(R.id.phn);
        route = (TextView)v.findViewById(R.id.route);
        username = (TextView)v.findViewById(R.id.username);


        name.setText(teacheractivity.tname);
        phn.setText(teacheractivity.tphone);
        route.setText(teacheractivity.troute);
        username.setText(teacheractivity.tusername);

        return v;
    }
}
