package enroute.pallavi.chugh.bus_tracking_app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.view.CardListView;


public class parse_check extends ActionBarActivity {


    TextView tv, route_no;
    String s;
    String route, cls, rollno, rowid;
    Toolbar toolbar;
    //static ArrayList<String> names,phones;
    static ArrayList<Bitmap> f_bmap = new ArrayList<Bitmap>();
    HashMap<Integer,Bitmap> hm = new HashMap<Integer,Bitmap>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parse_check);
        //getSupportActionBar().hide();
        toolbar = (Toolbar) findViewById(R.id.app_bar_me_p);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (mediator.tr_flag == 0) {
            overridePendingTransition(R.anim.slidefromleft, R.anim.slidetoleft);
        } else if (mediator.tr_flag == 1) {
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_right);
        }
        getSupportActionBar().setTitle("Student Info");
        route_no = (TextView) findViewById(R.id.route_num);

        //Bundle b = this.getIntent().getExtras();
        //route = (String) b.get("route");

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                route = "1";
            } else
                route = extras.getString("route");

        } else
            route = (String) savedInstanceState.getSerializable("route");

        route_no.setText("Route " + route);



        //mediator.names = (ArrayList<String>)b.getStringArrayList("name_list");
        //mediator.phone = (ArrayList<String>)b.getStringArrayList("phone_list");
        //mediator.rollno = (ArrayList<String>)b.getStringArrayList("phone_list");

/*
        ArrayList<String> names = getIntent().getStringArrayListExtra("names_list");
        ArrayList<String> phones = getIntent().getStringArrayListExtra("phone_list");
        Integer f = names.size();
        Toast.makeText(this,"Size is : " + f.toString(),Toast.LENGTH_SHORT).show();*/


        ArrayList<Card> cards = new ArrayList<Card>();

        //Create a card


        Card card = new Card(this);
        CardHeader cardHeader = new CardHeader(this);
        card.addCardHeader(cardHeader);
        card.setTitle("This is a new card");

/*
        CardExpand cardExpand = new CardExpand(this);
        cardExpand.setTitle("This is the expanded card");
        card.addCardExpand(cardExpand);
        CardViewNative cardView = (CardViewNative)findViewById(R.id.carddemo);
        cardView.setCard(card);*/


        for (int i = 0; i < mediator.names.size(); i++) {
            cards.add(card);
        }

        CardArrayAdapter madapter = new CustomCardAdapter(this, cards, mediator.names, mediator.phone);

        CardListView listview = (CardListView) findViewById(R.id.myList);

        if (listview != null) {
            listview.setAdapter(madapter);
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_parse_check, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id)
        {
            case android.R.id.home:
                mediator.tr_flag=1;

                NavUtils.navigateUpFromSameTask(this);
                break;
            case R.id.action_settings:
                return true;

        }



        return super.onOptionsItemSelected(item);
    }


    public class CustomCardAdapter extends CardArrayAdapter {

        Context c;
        ArrayList<String> names;
        ArrayList<String> phones;

        public CustomCardAdapter(Context context, List<Card> cards) {
            super(context, cards);
            c = context;
        }

        public CustomCardAdapter(Context context, ArrayList<Card> cards, ArrayList<String> names, ArrayList<String> phone) {
            super(context, cards);
            c = context;
            this.names = names;
            this.phones = phone;
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) c.getSystemService(c.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.list_card_thumbnail_layout, null);

            }

            //////bhai idhar se dekh yahan yeh log chal rhe hain means i hav cls rollno route bhi, den parse query me r1 table bheji and where clause lgaya
            /////toh if i remove rowid walli line toh toast sirf chal rhe hain as in complete  ata hai
            ////but if i add row id walli line and log neeche toh error hai
            ///n baaki kuch samajh na aye toh call kardiyo

            //CardHeader ch = (CardHeader)convertView.findViewById(R.id.card_header_layout);

            TextView tv = (TextView) convertView.findViewById(R.id.name);
            TextView tv1 = (TextView) convertView.findViewById(R.id.ph_number);
            TextView st_address = (TextView) convertView.findViewById(R.id.address);
            final ImageView user_image = (ImageView) convertView.findViewById(R.id.user);
            Button edit = (Button) convertView.findViewById(R.id.edit);
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cls = mediator.cls.get(position);
                    rollno = mediator.rollno.get(position);
                    Log.d("ab", cls);
                    Log.d("ab", rollno);
                    Log.d("ab", route);
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("r" + route);
                    query.whereEqualTo("class", cls);
                    query.whereEqualTo("rollno", rollno);
                    try {
                        List<ParseObject> lPo = query.find();
                        Log.d("asd", "size" + lPo.size());
                        if (lPo.size() > 0) {
                            //Log.d("asd",lPo.get(0).getObjectId());
                            rowid = lPo.get(0).getObjectId();
                            //Log.d("asd","r"+rollno);
                        }
                    } catch (ParseException e) {
                        Log.d("asd", e.getLocalizedMessage());
                    }

                    Intent i = new Intent(getApplicationContext(), studentinfoedit.class);
                    i.putExtra("routeno", route);
                    i.putExtra("rowno", rowid);
                    i.putExtra("studentno", position);
                    mediator.tr_flag=0;
                    startActivity(i);
                    //finish();

                }
            });
            String name = names.get(position).toString();
            String phone = phones.get(position).toString();
            String address = mediator.address.get(position).toString();


            tv.setText(name);
            tv1.setText(phone);
            st_address.setText(address);




                    try {


                        if (hm.get(position) == null) {
                            ParseFile fileoject = (ParseFile) mediator.the_route.get(position).get("photo");
                            fileoject.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] bytes, ParseException e) {
                                    if (e == null)

                                    {
                                        Log.d("parse", "success" + position);
                                        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                        //f_bmap.set(position, bmp);
                                        hm.put(position, bmp);
                                        user_image.setImageBitmap(bmp);
                                    }
                                }
                            });
                        } else {
                            user_image.setImageBitmap(hm.get(position));
                        }
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }



            return convertView;
        }
    }

}
