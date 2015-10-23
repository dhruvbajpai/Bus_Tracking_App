package enroute.pallavi.chugh.bus_tracking_app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.rey.material.widget.Button;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import enroute.pallavi.chugh.bus_tracking_app.Classes.MySQLiteHelper;

public class Student_List_Add extends ActionBarActivity{

    Toolbar toolbar;
    ListView listView;
    EditText et;
    public static List<String> names = new ArrayList<>();
    Button b1,save;
    public static MyAdapter adapter;
    String[] columns = {MySQLiteHelper.COLUMN_ID, MySQLiteHelper.COLUMN_NAME, MySQLiteHelper.COLUMN_ROUTE};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Route number stored in LoaderStart.route_no
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student__list__add);
        toolbar = (Toolbar) findViewById(R.id.app_stud);
        setSupportActionBar(toolbar);
        getNamesFromDatabase(); // to get the list populated from before if made before for that route
        et = (EditText) findViewById(R.id.name);
        getSupportActionBar().setTitle("Route "+ LoaderStart.route_no+" List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listView = (ListView) findViewById(R.id.stu_list);
        save = (Button) findViewById(R.id.savecont);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Save",Toast.LENGTH_SHORT).show();
                addToDatabase();
                Log.d("TAG","ADDED TO DATABASE");
                Intent i = new Intent(Student_List_Add.this,Student_Marker.class);
                startActivity(i);
            }
        });

        b1= (Button) findViewById(R.id.add_student);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = et.getText().toString();
                if(!name.equals(new String(""))) {
                    names.add(name);
                    //addToDatabase(name);
                    Collections.sort(names,new SortIgnoreCase());
                    et.setText("");
                    adapter.notifyDataSetChanged();
                }

            }
        });
        adapter = new MyAdapter(this,R.layout.name_list_item,names);
        listView.setAdapter(adapter);




    }

    public void getNamesFromDatabase() {
       /* SQLiteDatabase mydatabase = openOrCreateDatabase("Checklist",MODE_PRIVATE,null);
        Cursor resultset = mydatabase.rawQuery("Select * FROM CHECKNAME;",null); //where ROUTE = "+ "'"+ LoaderStart.route_no.toString()+ "';",null);
        resultset.moveToFirst();
        for(int i=0;i<resultset.getCount();i++)
        {
            names.add(resultset.getString(0));
            resultset.moveToNext();
           // Toast.makeText(getApplicationContext(),names.get(i),Toast.LENGTH_SHORT).show();
        }*/
       /* String where = MySQLiteHelper.COLUMN_ROUTE + "=" + 1;
        String whererargs[] = null;
        String groupBy = null;
        String order = null;
        String having = null;
        SQLiteDatabase db = MySQLiteHelper.*/
        names.clear();
        Log.d("TAG","GET NAMES IN FIRST CALLED");
        MySQLiteHelper myhelper = new MySQLiteHelper(this);
        Cursor rs = myhelper.getData(LoaderStart.route_no);

        if(rs!=null) {
            rs.moveToFirst();

            while (!rs.isAfterLast()) {
                names.add(rs.getString(2));
                rs.moveToNext();
            }
            Collections.sort(names,new SortIgnoreCase());
        }


    }

    public void addToDatabase() {
       /* SQLiteDatabase mydatabase = openOrCreateDatabase("Checklist",MODE_PRIVATE,null);
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS CHECKNAME(NAME CHAR, ROUTE VARCHAR)");
//        mydatabase.execSQL("DELETE * FROM CHECKNAME WHERE ROUTE='"+LoaderStart.route_no.toString()+"';");
//        TO UPDATE THE DB WITH THE PATICULAR ROUTE NAMES ADDED/DELETED/
        Log.d("TAG","DONE DELETING");

       *//* for(int i=0;i<names.size();i++) {
            mydatabase.execSQL("INSERT INTO CHECKNAME VALUES('  " + names.get(i) + " ','" + LoaderStart.route_no.toString() + " ');");
        }*/
        Log.d("TAG","ADD NAMES IN FIRST CALLED");
        MySQLiteHelper myopenhelper = new MySQLiteHelper(this);
        myopenhelper.deleteRoute(LoaderStart.route_no);
        for(int i=0;i<names.size();i++)
        {
            myopenhelper.insertName(names.get(i),LoaderStart.route_no);
        }

    }

    public class SortIgnoreCase implements Comparator<Object>{
    @Override
    public int compare(Object lhs, Object rhs) {
        String s1 = (String)lhs;
        String s2 = (String)rhs;
        return s1.toLowerCase().compareTo(s2.toLowerCase());
    }
}

    public class MyAdapter extends ArrayAdapter<String>

    {
        List<String> names = new ArrayList<>();
        Context c;
        int resource;
        public MyAdapter(Context context, int resource, List<String> names) {
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
//                convertView = inflater.inflate(R.layout.name_list_item, null);
                convertView = inflater.inflate(R.layout.check_info_delete,null);
            }

            TextView tv = (TextView) convertView.findViewById(R.id.nameindex);
            tv.setText(String.valueOf(position+1)+"."+" "+ names.get(position).toString());

            Button delete = (Button) convertView.findViewById(R.id.delete_student);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Student_List_Add.names.remove(position);
                    Student_List_Add.adapter.notifyDataSetChanged();


                }
            });

            return convertView;
        }
    }

}
