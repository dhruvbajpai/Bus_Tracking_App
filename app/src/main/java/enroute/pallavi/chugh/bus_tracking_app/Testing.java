package enroute.pallavi.chugh.bus_tracking_app;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Testing extends ActionBarActivity {

    public String[] test = {"abc","ca","how","are","you","myarem"};
    ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);
        lv = (ListView) findViewById(R.id.testing);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_multiple_choice,test);
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        lv.setAdapter(adapter);

    }

}
