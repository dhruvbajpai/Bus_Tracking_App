package com.example.dhruv.bus_tracking_app;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class addstudent extends ActionBarActivity implements View.OnClickListener{
    static ImageView imageView;
    static int RESULT_LOAD_IMG;
    static int check = 0;
    ParseFile file;
    int bmsize;
    static String filename="",extension;
    static Bitmap bm;
    static dialogstudent dialog;
    static Uri mCapturedImageURI;
    ImageButton imagechange;
    Button save;
    String root;
    EditText name,phn,cls,roll,route;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addstudent);

        imageView =(ImageView)findViewById(R.id.showimage);
        save = (Button)findViewById(R.id.btsave);
        save.setOnClickListener(this);
        name= (EditText)findViewById(R.id.name);
        cls = (EditText)findViewById(R.id.cls);
        roll = (EditText)findViewById(R.id.rollno);
        phn= (EditText)findViewById(R.id.phn);
        route= (EditText)findViewById(R.id.route);
        imagechange = (ImageButton)findViewById(R.id.upload);
        imagechange.setOnClickListener(this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_addstudent, menu);
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

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.upload)
        {
            //Log.d("asd", "tttbeforeclick");
            dialog = new dialogstudent(this);
            dialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;

            dialog.getWindow();
            dialog.show();
            //Log.d("asd","tttclick");
        }

        if(v.getId()==R.id.btsave)
        {

            root=route.getText().toString();
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
                //bmsize = sizeOf(bm);
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



            ParseObject imgupload = new ParseObject("r"+root);
            if(filename!="") {
                            //imgupload.put("photo", file);

                        }else{
                            //imgupload.remove("photo");

                        }

                        imgupload.put("s_name", name.getText().toString());
                        imgupload.put("class", cls.getText().toString());
                        imgupload.put("rollno", roll.getText().toString());
                        imgupload.put("phone", phn.getText().toString());
                        imgupload.saveInBackground();
                        Toast.makeText(getApplicationContext(),"uploaded",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {Toast.makeText(getApplicationContext(),"failed",Toast.LENGTH_SHORT).show();}



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
            Toast.makeText(getApplicationContext(), capturedImageFilePath, Toast.LENGTH_SHORT).show();
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

}
