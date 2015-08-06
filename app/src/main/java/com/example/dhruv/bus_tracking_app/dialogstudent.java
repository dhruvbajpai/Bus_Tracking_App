package com.example.dhruv.bus_tracking_app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

/**
 * Created by Dhruv on 14-Jul-15.
 */
public class dialogstudent extends Dialog implements View.OnClickListener{


    private static final int CAMERA_REQUEST = 1888;
    public Activity c;
    public Dialog d;
    Button camera,gallery,remove;
    public dialogstudent(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_dialogstudent);

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
            addstudent.check = 1;
            Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            c.startActivityForResult(galleryIntent, addstudent.RESULT_LOAD_IMG);

        }
        if(v.getId()==R.id.button1)
        {
            addstudent.check = 0;

            addstudent.filename = "temp.jpg";
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, addstudent.filename);
            addstudent.mCapturedImageURI = c.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, addstudent.mCapturedImageURI);
            c.startActivityForResult(cameraIntent, CAMERA_REQUEST);

        }
        if(v.getId()==R.id.button3)
        {
            addstudent.filename="";
            addstudent.extension="";
            addstudent.imageView.setImageBitmap(BitmapFactory.decodeResource(c.getResources(),R.drawable.profilepic));
            addstudent.dialog.dismiss();
        }
    }
}