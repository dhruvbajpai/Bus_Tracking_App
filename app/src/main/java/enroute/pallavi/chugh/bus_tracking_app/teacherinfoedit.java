package enroute.pallavi.chugh.bus_tracking_app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class teacherinfoedit extends Activity {

    ImageView imageView1,Bit;
    RoundImage roundedImage;
    Button btedit;
    TextView name,phn,user,pass,route;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacherinfoedit);

        name= (TextView)findViewById(R.id.tvname);
        phn= (TextView)findViewById(R.id.tvphn);
        user= (TextView)findViewById(R.id.tvuser);
        pass= (TextView)findViewById(R.id.tvpass);
        route= (TextView)findViewById(R.id.tvroute);
        name.setText(Route_info.teacher);
        phn.setText(Route_info.teachphn);
        user.setText(Route_info.teachuser);
        pass.setText(Route_info.teachpass);
        route.setText(Route_info.route);

        imageView1 = (ImageView) findViewById(R.id.iv);
        //Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.person);

        roundedImage = new RoundImage(Route_info.image);
        imageView1.setImageDrawable(roundedImage);

        btedit =(Button)findViewById(R.id.save);
        btedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),editteacher.class);
                startActivity(i);

            }
        });
    }



}
