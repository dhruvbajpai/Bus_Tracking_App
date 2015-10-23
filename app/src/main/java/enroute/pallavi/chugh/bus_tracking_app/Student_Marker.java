package enroute.pallavi.chugh.bus_tracking_app;

import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.rey.material.widget.Button;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import enroute.pallavi.chugh.bus_tracking_app.Classes.MySQLiteHelper;

public class Student_Marker extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener {


    private GoogleMap mMap;
    TextView tv;
    ListView checkview;
    Button upload;
     public static CheckListAdapter adapter;
   // public static ArrayAdapter<String> adapter;
    public static int priority_count = 1;
    public static int number_of_checked = 0;
    public static  List<String> checked_list = new ArrayList<>();
    public static List<CheckBox> boxlist = new ArrayList<>();
    String[] columns = {MySQLiteHelper.COLUMN_ID,MySQLiteHelper.COLUMN_ROUTE,MySQLiteHelper.COLUMN_NAME};
    static List<String> names = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student__marker);
        tv = (TextView) findViewById(R.id.priority);
        tv.setText("PRIORITY " + priority_count);
        upload = (Button) findViewById(R.id.upload_selection);
        upload.setOnClickListener(this);
        checkview = (ListView) findViewById(R.id.checklist_view);
       /* List<String> leme = new ArrayList<>();
        leme.add(new String("Raman"));
        leme.add(new String("Naman"));
        leme.add(new String("Kamal"));
        leme.add(new String("Samal"));
        uploadNamesList(leme);
       */
        getFromDatabase();
        Collections.sort(names, new SortwithIgnoreCase());

//        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_multiple_choice,names);
        checkview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        adapter = new CheckListAdapter(this, R.layout.checklist_item,names);
        checkview.setAdapter(adapter);
        Log.d("TAG", "RETURNED FROM TO DATABASE");



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//       SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
    }




    public void getFromDatabase() {
        Log.d("TAG","STARTED IN FUNCTION DATABASE");
        /*SQLiteDatabase mydatabase = openOrCreateDatabase("Checklist",MODE_PRIVATE,null);
        Cursor resultset = mydatabase.rawQuery("Select * FROM CHECKNAME;",null); //where ROUTE = "+ "'"+ LoaderStart.route_no.toString()+ "';",null);
        resultset.moveToFirst();
        for(int i=0;i<resultset.getCount();i++)
        {
            names.add(resultset.getString(0));
            resultset.moveToNext();
            Toast.makeText(getApplicationContext(),names.get(i),Toast.LENGTH_SHORT).show();
        }
        Log.d("TAG", "RETURNED IN FUNCTION DATABASE");*/
        names.clear();
        MySQLiteHelper myhelper = new MySQLiteHelper(this);
        Cursor rs = myhelper.getData(LoaderStart.route_no);

        if(rs!=null) {
            rs.moveToFirst();

            while (!rs.isAfterLast()) {
                names.add(rs.getString(2));
                rs.moveToNext();
            }

        }

        Log.d("TAG", String.valueOf(rs.getCount()));
        Toast.makeText(getApplicationContext(),String.valueOf(rs.getCount()),Toast.LENGTH_SHORT).show();


    }
public class CheckListAdapter extends ArrayAdapter<String>
{
    List<String> names = new ArrayList<>();
    Context c;
    int resource;
    public CheckListAdapter(Context context, int resource, List<String> names) {
        super(context, resource);
        c = context;
        this.resource = resource;
        this.names = names;
    }

    @Override
    public int getCount() {
        return names.size();
    }


    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if(convertView==null)
        {
            LayoutInflater inflater = (LayoutInflater) c.getSystemService(c.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.checklist_item, null);
        }

        CheckBox ck  = (CheckBox) convertView.findViewById(R.id.check_me);
        ck.setText(names.get(position).toString());

        ck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    Toast.makeText(getApplicationContext(),String.valueOf(position)+" : "+names.get(position),Toast.LENGTH_SHORT).show();

                    checked_list.add(String.valueOf(position));
                    number_of_checked++;
                }else
                {

                    checked_list.remove(String.valueOf(position));
                    number_of_checked--;
                }
                Log.d("TAG","-------------checked_list---------");
                for(int i=0;i<number_of_checked;i++) {

                    Log.d("TAG", checked_list.get(i).toString()+" : "+ names.get(Integer.parseInt(checked_list.get(i))));
                }
                Log.d("TAG","-------------checked_list---------");

            }
        });
      //  TextView tv = (TextView) convertView.findViewById(R.id.check_name);
       // tv.setText(names.get(position).toString());

        /*Button delete = (Button) convertView.findViewById(R.id.delete_student);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Student_List_Add.names.remove(position);
                Student_List_Add.adapter.notifyDataSetChanged();


            }
        });*/

        return convertView;
    }
}


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    public class SortwithIgnoreCase implements Comparator<Object>
    {
        @Override
        public int compare(Object lhs, Object rhs) {
            String s1 = (String)lhs;
            String s2 = (String)rhs;
            return s1.toLowerCase().compareTo(s2.toLowerCase());
        }
    }
    public void upload_click() {
//        ArrayList<String> checked_names = new ArrayList<>();

        if (!checked_list.isEmpty()) {
            getDataAndUpload(checked_list, names);
//            /*Log.d("TAG", "--------------------------------");
            for (int i = 0; i < checked_list.size(); i++) {
                Log.d("TAG", names.get(Integer.parseInt(checked_list.get(i))));
                names.remove(Integer.parseInt(checked_list.get(i)));
            }
            Log.d("TAG", "--------------------------------");

      /*      SparseBooleanArray checked = checkview.getCheckedItemPositions();
            for(int i=0;i<checkview.getAdapter().getCount();i++)
            {
                if(checked.get(i)==true)
                {
                    checked_names.add(String.valueOf(names.get(i)));
                    names.remove(i);
                }
            }
            checkview.clearChoices();*/
//        Log.d("TAG", "--------------------------------");
//        for(int i=0;i<checked_names.size();i++)
//            Log.d("TAG", "Name: "+i+" "+ checked_names.get(i)+"\n");
//        Log.d("TAG", "--------------------------------");

            adapter.notifyDataSetChanged();
            uncheckAllChildrenCascade(checkview);
            //checked_list.clear();
            number_of_checked =0 ;

        }else
        {
            Toast.makeText(getApplicationContext(),"NO STUDENT SELECTED",Toast.LENGTH_SHORT).show();
        }


//        checked_list.clear();
        }

    public void getDataAndUpload(List<String> checkedlist, List<String> names) {
    }


    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.upload_selection:
                upload_click();
                break;
        }
    }

    private void uncheckAllChildrenCascade(ViewGroup vg) {
        for (int i = 0; i < vg.getChildCount(); i++) {
            View v = vg.getChildAt(i);
            if (v instanceof CheckBox) {
                ((CheckBox) v).setChecked(false);
            } else if (v instanceof ViewGroup) {
                uncheckAllChildrenCascade((ViewGroup) v);
            }
        }
    }
    /*@Override
    public void onBackPressed() {
        // Do Here what ever you want do on back press;
    }*/
    public void uploadNamesList(List<String> namesList)
    {
//        ParseQuery<ParseObject> query = ParseQuery.getQuery("r"+LoaderStart.route_no);

        List<ParseObject> objectList= new ArrayList<>();
        for(int i=0;i<namesList.size();i++)
        {
            ParseObject name = new ParseObject("r"+ LoaderStart.route_no);
            name.put("s_name",namesList.get(i));
            objectList.add(name);
        }

        ParseObject.saveAllInBackground(objectList);
        Log.d("TAG","UPLOADED");


    }

}

