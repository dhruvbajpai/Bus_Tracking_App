package enroute.pallavi.chugh.bus_tracking_app;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.Button;

/**
 * Created by Dhruv on 14-Jul-15.
 */
public class Diag extends Dialog implements View.OnClickListener{


    private static final int CAMERA_REQUEST = 1888;
    public Activity c;
    public Dialog d;
    Button camera,gallery,remove;
    public Diag(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_diag);

        camera = (Button)findViewById(R.id.button2);
        camera.setOnClickListener(this);
        gallery = (Button)findViewById(R.id.button1);
        gallery.setOnClickListener(this);
        remove = (Button)findViewById(R.id.button3);
        remove.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.button2)
        {
            editteacher.check = 1;
            Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            c.startActivityForResult(galleryIntent, editteacher.RESULT_LOAD_IMG);

        }
        if(v.getId()==R.id.button1)
        {
            editteacher.check = 0;

            editteacher.filename = "temp.jpg";
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, editteacher.filename);
            editteacher.mCapturedImageURI = c.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, editteacher.mCapturedImageURI);
            c.startActivityForResult(cameraIntent, CAMERA_REQUEST);

        }
        if(v.getId()==R.id.button3)
        {
            editteacher.filename="";
            editteacher.extension="";
            Route_info.image = BitmapFactory.decodeResource(c.getResources(),R.drawable.profilepic);
            editteacher.imageView.setImageBitmap(Route_info.image);
            editteacher.dialog.dismiss();
        }
    }
}