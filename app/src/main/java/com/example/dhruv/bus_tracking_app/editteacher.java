package com.example.dhruv.bus_tracking_app;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class editteacher extends Activity implements View.OnClickListener {

    static ImageView imageView;
    static int RESULT_LOAD_IMG;
    static int check = 0;
    ParseFile file;
    int bmsize;
    static String filename="",extension;
    static Bitmap bm;
    static Diag dialog;
    static Uri mCapturedImageURI;
    ImageButton imagechange;
    Button save;
    EditText name,phn,user,pass,route;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editteacher);
        Toast.makeText(this,Route_info.teachid+"abc",Toast.LENGTH_SHORT).show();
        imageView =(ImageView)findViewById(R.id.showimage);
        imageView.setImageBitmap(Route_info.image);
        save = (Button)findViewById(R.id.btsave);
        save.setOnClickListener(this);
        name= (EditText)findViewById(R.id.etname);
        user= (EditText)findViewById(R.id.etuser);
        pass= (EditText)findViewById(R.id.etpass);
        phn= (EditText)findViewById(R.id.etphn);
        route= (EditText)findViewById(R.id.etroute);
        name.setText(Route_info.teacher);
        phn.setText(Route_info.teachphn);
        user.setText(Route_info.teachuser);
        pass.setText(Route_info.teachpass);
        route.setText(Route_info.route);
        imagechange = (ImageButton)findViewById(R.id.change);
        imagechange.setOnClickListener(this);
        Log.d("asd","ttt");
    }
        @Override
    public void onClick(View v) {
    if(v.getId()==R.id.change)
    {Log.d("asd","tttbeforeclick");
        dialog = new Diag(this);
        dialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;

        dialog.getWindow();
        dialog.show();
        Log.d("asd","tttclick");
    }
    if(v.getId()==R.id.btsave)
    {


        Log.d("asd",filename);
        /*if(filename=="") {
            Toast.makeText(getApplicationContext(), "a", Toast.LENGTH_SHORT).show();
            bm = BitmapFactory.decodeResource(getResources(),
                    R.drawable.ic_launcher);
            filename="ic_launcher";
            extension="png";
            // Convert it to byte
        }*/

        if(filename!="") {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmsize = sizeOf(bm);
            Log.d("asd",String.valueOf(bmsize));

            Toast.makeText(getApplicationContext(),String.valueOf(bmsize),Toast.LENGTH_SHORT).show();
            if (extension == "png") {
                // Compress image to lower quality scale 1 - 100
                bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
            } else {
                bm.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            }
            byte[] image = stream.toByteArray();
            file = new ParseFile(filename, image);
            // Upload the image into Parse Cloud
            file.saveInBackground();

        }
        // Create the ParseFile



        ParseQuery<ParseObject> query = ParseQuery.getQuery("route_teacher");
        String a =Route_info.teachid;
        query.getInBackground(a, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject imgupload, com.parse.ParseException e) {
                if (e == null) {
                    if(filename!="") {
                        imgupload.put("photo", file);

                    }else{
                        imgupload.remove("photo");

                    }

                    imgupload.put("username", user.getText().toString());
                    imgupload.put("password", pass.getText().toString());
                    imgupload.put("teacher_name", name.getText().toString());
                    imgupload.put("phone", phn.getText().toString());
                    imgupload.put("route_no", route.getText().toString());
                    imgupload.saveInBackground();
                    Toast.makeText(getApplicationContext(),"uploaded",Toast.LENGTH_SHORT).show();
                }
                else
                {Toast.makeText(getApplicationContext(),"failed",Toast.LENGTH_SHORT).show();}
            }
        });

    }

  }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (check == 0) {
            try {
                bm = MediaStore.Images.Media.getBitmap(
                        getApplicationContext().getContentResolver(),mCapturedImageURI);
            } catch (IOException e) {
                e.printStackTrace();
            }
            imageView.setImageBitmap(bm);
            String[] projection = { MediaStore.Images.Media.DATA};
            Cursor cursor = managedQuery(mCapturedImageURI, projection, null, null, null);
            int column_index_data = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String capturedImageFilePath = cursor.getString(column_index_data);
            Toast.makeText(getApplicationContext(),capturedImageFilePath,Toast.LENGTH_SHORT).show();
            //File file = new File(capturedImageFilePath);
            //bm = BitmapFactory.decodeFile(file.getAbsolutePath());
            filename=capturedImageFilePath.substring(capturedImageFilePath.lastIndexOf("/")+1);
            Log.d("asd",filename);
            extension=filename.substring(filename.lastIndexOf(".")+1);
            Log.d("asd",extension);
            dialog.dismiss();

        }
        else
        {
            try {
                // When an Image is picked
                if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                        && null != data) {
                    // Get the Image from data

                    Uri selectedImage = data.getData();
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };

                    // Get the cursor
                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String imgDecodableString = cursor.getString(columnIndex);
                    Log.d("asd",imgDecodableString);
                    filename=imgDecodableString.substring(imgDecodableString.lastIndexOf("/")+1);
                    Log.d("asd",filename);
                    extension=filename.substring(filename.lastIndexOf(".")+1);
                    Log.d("asd",extension);
                    cursor.close();
                    //Toast.makeText(this,imgDecodableString,Toast.LENGTH_SHORT).show();
                    //ImageView imgView = (ImageView) findViewById(R.id.imageView1);
                    // Set the Image in ImageView after decoding the String
                    bm = BitmapFactory.decodeFile(imgDecodableString);
                    imageView.setImageBitmap(bm);
                    dialog.dismiss();
                } else {
                    Toast.makeText(this, "You haven't picked Image",
                            Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                        .show();
            }

        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    protected int sizeOf(Bitmap data) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR1) {
            return data.getRowBytes() * data.getHeight();
        } else {
            return data.getByteCount();
        }
    }
}