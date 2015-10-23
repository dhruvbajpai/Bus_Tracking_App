package enroute.pallavi.chugh.bus_tracking_app;

import android.app.Activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.rey.material.widget.Button;

import java.util.ArrayList;
import java.util.List;

import enroute.pallavi.chugh.bus_tracking_app.fragments.RouteList;

public class LoaderStart extends ActionBarActivity {

    List<String> routelist = new ArrayList<>();
    Spinner sp ;
    Toolbar toolbar;
    Button b1;
    public static Integer no_of_route = 0;
    public static Integer route_no = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loader_start);
        toolbar = (Toolbar) findViewById(R.id.app_loader);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Select Route");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        b1 = (Button)findViewById(R.id.route_next);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = sp.getSelectedItemPosition() + 1; //route selected number
                route_no = i;
                Intent u = new Intent(LoaderStart.this, Student_List_Add.class);
                u.putExtra("route", String.valueOf(i));
                startActivity(u);
                //Toast.makeText(getApplicationContext(),String.valueOf(i),Toast.LENGTH_SHORT).show();
            }
        });
      /*  ParseQuery<ParseObject> query = ParseQuery.getQuery("liveloc");
        try {
            no_of_route = query.count();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        */
        Intent j = getIntent();
        no_of_route = j.getIntExtra("count",20);

      //  Toast.makeText(getApplicationContext(),no_of_route.toString(),Toast.LENGTH_SHORT).show();

        for(int i=1;i<=LoaderStart.no_of_route;i++)
            routelist.add("Route "+i);
        sp = (Spinner)findViewById(R.id.route_spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_item,routelist);

        adapter.setDropDownViewResource(R.layout.spinner_item);
        sp.setAdapter(adapter);

       /* RouteList a = new RouteList();
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        transaction.add(R.id.frag,a);
        transaction.commit();
*/


    }

}
