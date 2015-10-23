package enroute.pallavi.chugh.bus_tracking_app.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import enroute.pallavi.chugh.bus_tracking_app.LoaderStart;
import enroute.pallavi.chugh.bus_tracking_app.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RouteList extends Fragment {

    List<String> routelist = new ArrayList<>();
    Spinner sp ;
    public RouteList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        for(int i=1;i<=LoaderStart.no_of_route;i++)
            routelist.add("Route "+i);
        sp = (Spinner) getActivity().findViewById(R.id.route_spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,routelist);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter);

        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_route_list, container, false);



    }


}
