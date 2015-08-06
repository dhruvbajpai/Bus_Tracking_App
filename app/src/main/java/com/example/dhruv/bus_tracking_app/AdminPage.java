package com.example.dhruv.bus_tracking_app;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardGridArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.view.CardGridView;
import it.gmariotti.cardslib.library.view.CardViewNative;


public class AdminPage extends ActionBarActivity{

    TextView tv1;
  Toolbar toolbar;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);

        if(mediator.tr =="f")
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        else {
            Toast.makeText(this,"hey",Toast.LENGTH_SHORT).show();
            overridePendingTransition(R.anim.slide_out, R.anim.slide_in);
        }
        //getSupportActionBar().hide();
        toolbar = (Toolbar)findViewById(R.id.app_bar_me);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Current Route Grid");

        //-----------------Populate the cardview------------------
        ArrayList<Card> cards = new ArrayList<Card>();

        //Create a Card
        Card card = new Card(this);

        //Create a CardHeader
        CardHeader header = new CardHeader(this);
        //Add Header to card
        card.addCardHeader(header);
        for(int i=0;i<17;i++)
        {

            cards.add(card);
        }

        CardGridArrayAdapter mCardArrayAdapter = new MyAdapter_grid(this,cards);

        CardGridView gridView = (CardGridView) this.findViewById(R.id.myGrid);
        if (gridView!=null){

            gridView.setAdapter(mCardArrayAdapter);

        }

       /* gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent q = new Intent(getApplicationContext(),Route_info.class);
                q.putExtra("rt_number",position);
                startActivity(q);
            }
        });*/

        FloatingActionButton addroute = (FloatingActionButton) findViewById(R.id.addroute);
        addroute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "route", Toast.LENGTH_SHORT).show();
            }
        });

        FloatingActionButton addteach = (FloatingActionButton) findViewById(R.id.addteach);
        addteach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"teacher",Toast.LENGTH_SHORT).show();
            }
        });

        FloatingActionButton addstud = (FloatingActionButton) findViewById(R.id.addstud);
        addstud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"student",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(),addstudent.class);
                startActivity(i);
            }
        });
        /*gridView.setClickable(true);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent q = new Intent(getApplicationContext(),Route_info.class);
                q.putExtra("rt_number",position);
                startActivity(q);
            }
        });*/
        //-----------------Populate the cardview------------------



        //tv1 = (TextView)findViewById(R.id.tv1);
       /* ParseUser currentuser = ParseUser.getCurrentUser();
        if(currentuser!=null)
        {
            tv1.setText(currentuser.get("Name").toString());
        }
        else
            tv1.setText("NULL CURRENT USER");*/

        /*Card card = new Card(this);
        CardHeader header = new CardHeader(this);
        card.addCardHeader(header);

        Card c = new Card(this);
        c.setSwipeable(true);
        c.addCardHeader(header);
*/
        /*CardViewNative cardView = (CardViewNative)findViewById(R.id.carddemo);
        cardView.setCard(card);
        cardView.setCard(c);
*/
//        CardGridView cardView = (CardGridView)findViewById(R.id.carddemo);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_admin_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
