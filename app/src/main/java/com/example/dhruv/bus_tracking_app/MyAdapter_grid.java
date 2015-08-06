package com.example.dhruv.bus_tracking_app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardGridArrayAdapter;
import it.gmariotti.cardslib.library.view.CardViewNative;
import it.gmariotti.cardslib.library.view.ForegroundLinearLayout;

/**
 * Created by Dhruv on 16-Jul-15.
 */
public class MyAdapter_grid extends CardGridArrayAdapter {

    Context c ;
    int pos=0;
    int i;
    String[] colors = {"#B2000000","#808080","#e91e63","#ec407a","#805677fc","#80738ffe",
            "#3f51b5","#303f9f","#F06292","#E91E63","#C2185B"};
    Integer[] numbers = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18};
    public MyAdapter_grid(Context context, List<Card> cards) {
        super(context, cards);
        c = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        pos = position;
        if (convertView==null)
        {
            LayoutInflater inflater = (LayoutInflater)c.getSystemService(c.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.grid_ele,null);

        }
        //ForegroundLinearLayout l = (ForegroundLinearLayout)convertView.findViewById(R.id.card_main_layout);
        LinearLayout ll = (LinearLayout)convertView.findViewById(R.id.cardcolor);

        i=position;
        if(i>10)
        {i=i-10;}
        ll.setBackgroundColor(Color.parseColor(colors[i]));
        TextView tv = (TextView)convertView.findViewById(R.id.text2);
        tv.setText("Route "+ numbers[position].toString());

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent q = new Intent(getContext(), Route_info.class);
                q.putExtra("rt_number", position + 1);
                Toast.makeText(getContext(), String.valueOf(position + 1), Toast.LENGTH_SHORT).show();
                c.startActivity(q);
            }
        });
        /*convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent q = new Intent(c,Route_info.class);
                q.putExtra("rt_number",position);
                c.startActivity(q);
            }
        });
*/
        return convertView;
    }


}