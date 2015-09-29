package enroute.pallavi.chugh.bus_tracking_app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;

import java.util.ArrayList;
import java.util.HashMap;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.view.CardListView;


public class tfragstudent extends android.support.v4.app.Fragment {



    final  HashMap<Integer,Bitmap> hm = new HashMap<Integer,Bitmap>();
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //View v = super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_tfragstudent, container, false);

        ArrayList<Card> cards = new ArrayList<Card>();

        //Create a card


        Card card = new Card(v.getContext());
        CardHeader cardHeader = new CardHeader(v.getContext());
        card.addCardHeader(cardHeader);
        card.setTitle("This is a new card");


        for (int i = 0; i < teacheractivity.names.size(); i++) {
            cards.add(card);
        }

        CardArrayAdapter madapter = new TustomCardAdapter(v.getContext(), cards, teacheractivity.names, teacheractivity.phone);

        CardListView listview = (CardListView) v.findViewById(R.id.myList);

        if (listview != null) {
            listview.setAdapter(madapter);
        }


        return v;
    }

    /*public class TcustomCardAdapter extends CardArrayAdapter{




}*/

    private class TustomCardAdapter extends CardArrayAdapter {
        Context c;
        ArrayList<String> names;
        ArrayList<String> phones;


        public TustomCardAdapter(Context context, ArrayList<Card> cards, ArrayList<String> names, ArrayList<String> phone) {
            super(context, cards);
            c = context;
            this.names = names;
            this.phones = phone;
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) c.getSystemService(c.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.t_stdfrag_a, null);

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
            //Button edit = (Button) convertView.findViewById(R.id.edit);
            /*edit.setOnClickListener(new View.OnClickListener() {
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
                    startActivity(i);
                    finish();

                }
            });*/
            String name = names.get(position).toString();
            String phone = phones.get(position).toString();
            String address = teacheractivity.address.get(position).toString();


            tv.setText(name);
            tv1.setText(phone);
            st_address.setText(address);



            //final Integer temp_p = position;


            ParseFile fileoject = (ParseFile) teacheractivity.the_route.get(position).get("photo");

            if(hm.get(position)== null)
            {
                fileoject.getDataInBackground(new GetDataCallback() {
                    @Override
                    public void done(byte[] bytes, ParseException e) {
                        if (e == null)

                        {

                            Log.d("parse", "success" + position);
                            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            //f_bmap.set(position, bmp);
                            hm.put(position,bmp);
                            user_image.setImageBitmap(bmp);
                        }
                    }
                });

            }
            else
            {
                user_image.setImageBitmap(hm.get(position));
            }


            return convertView;

        }
    }
}
