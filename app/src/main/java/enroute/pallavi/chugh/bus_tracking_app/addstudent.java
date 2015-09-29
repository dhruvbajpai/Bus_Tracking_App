package enroute.pallavi.chugh.bus_tracking_app;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class addstudent extends ActionBarActivity implements View.OnClickListener, OnMapReadyCallback {
    static ImageView imageView;
    static int RESULT_LOAD_IMG;
    static int check = -1;
    ParseFile file;
    int bmsize;
    static String filename = "", extension;
    static Bitmap bm;
    static dialogstudent dialog;
    static Uri mCapturedImageURI;
    ImageButton imagechange;
    Button save,edit_loc;
    EditText name, phn, cls, roll, route;
    TextView address_info;
    ScrollView sv;

    public GoogleMap mMap;
    SupportMapFragment mapFragment;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addstudent);
        toolbar = (Toolbar) findViewById(R.id.app_bar_me_add);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add New Student");
        sv = (ScrollView)findViewById(R.id.my_scroll);
        imageView = (ImageView) findViewById(R.id.showimage);
        save = (Button) findViewById(R.id.btsave);
        save.setOnClickListener(this);
        edit_loc = (Button) findViewById(R.id.editloc);
        name = (EditText) findViewById(R.id.name);
        cls = (EditText) findViewById(R.id.cls);
        roll = (EditText) findViewById(R.id.rollno);
        phn = (EditText) findViewById(R.id.phn);
        route = (EditText) findViewById(R.id.route);
        address_info = (TextView)findViewById(R.id.st_address_info);
        imagechange = (ImageButton) findViewById(R.id.upload);
        imagechange.setOnClickListener(this);
        edit_loc.setOnClickListener(this);
        mapFragment =(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.student_map);
        mapFragment.getMapAsync(this);
        mMap = mapFragment.getMap();
        mapFragment.getView().setVisibility(View.GONE);
        address_info.setVisibility(View.GONE);
        /*FragmentManager manager = getFragmentManager();
        manager.beginTransaction().hide(mapFragment).commit();*/

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

        if(id==R.id.home)
        {
            NavUtils.navigateUpFromSameTask(this);
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void onClick(View v) {



        if(v.getId() == R.id.editloc)
        {

            int k = 0;
            Intent i = new Intent(this, MapsActivity.class);
            i.putExtra("activityname", k);
            i.putExtra("position", -1);
            startActivityForResult(i, 1);

        }
        if (v.getId() == R.id.upload) {
            //Log.d("asd", "tttbeforeclick");
            dialog = new dialogstudent(this);
            dialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;

            dialog.getWindow();
            dialog.show();
            //Log.d("asd","tttclick");
        }

        if (v.getId() == R.id.btsave) {


            Log.d("asd", filename);


        /*if(filename=="") {
            Toast.makeText(getApplicationContext(), "a", Toast.LENGTH_SHORT).show();
            bm = BitmapFactory.decodeResource(getResources(),
                    R.drawable.ic_launcher);
            filename="ic_launcher";
            extension="png";
            // Convert it to byte
        }*/

            if (filename != "") {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                //bmsize = sizeOf(bm);
                Log.d("asd", String.valueOf(bmsize));

                Toast.makeText(getApplicationContext(), String.valueOf(bmsize), Toast.LENGTH_SHORT).show();
                if (extension == "png") {
                    // Compress image to lower quality scale 1 - 100
                    bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
                } else {
                    bm.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                }
                byte[] image = stream.toByteArray();
                file = new ParseFile(filename, image);
                // Upload the image into Parse Cloud
                //file.saveInBackground();
                try {
                    file.save();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
            // Create the ParseFile


            ParseObject imgupload = new ParseObject("r" + route);
            if (filename != "") {
                imgupload.put("photo", file);

            } else {
                //imgupload.remove("photo");

            }

            imgupload.put("s_name", name.getText().toString());
            imgupload.put("class", cls.getText().toString());
            imgupload.put("rollno", roll.getText().toString());
            imgupload.put("phone", phn.getText().toString());
            imgupload.saveInBackground();
            Toast.makeText(getApplicationContext(), "uploaded", Toast.LENGTH_SHORT).show();
        } else {
            //Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_SHORT).show();
        }


    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Double lat = data.getDoubleExtra("lat", 0.0);
                Double lon = data.getDoubleExtra("lon", 0.0);
                String address = data.getStringExtra("address");
                mapFragment.getView().setVisibility(View.VISIBLE);
                address_info.setText(address);
                address_info.setVisibility(View.VISIBLE);
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(new LatLng(lat,lon)).title("Bus Stop Marked")).showInfoWindow();
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat + 0.0005, lon), 16.0f));
                edit_loc.setText("Edit Location");
                Handler h = new Handler();

                h.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        sv.smoothScrollTo(0, 600);
                    }
                }, 1000);
                Toast.makeText(getApplicationContext(), "Lat: " + lat + "Long: " + lon, Toast.LENGTH_SHORT).show();
            }
            if (resultCode == RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
        if (check == 0) {
            try {
                bm = MediaStore.Images.Media.getBitmap(
                        getApplicationContext().getContentResolver(), mCapturedImageURI);
            } catch (IOException e) {
                e.printStackTrace();
            }
            imageView.setImageBitmap(bm);
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = managedQuery(mCapturedImageURI, projection, null, null, null);
            int column_index_data = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String capturedImageFilePath = cursor.getString(column_index_data);
            Toast.makeText(getApplicationContext(), capturedImageFilePath, Toast.LENGTH_SHORT).show();
            //File file = new File(capturedImageFilePath);
            //bm = BitmapFactory.decodeFile(file.getAbsolutePath());
            filename = capturedImageFilePath.substring(capturedImageFilePath.lastIndexOf("/") + 1);
            Log.d("asd", filename);
            extension = filename.substring(filename.lastIndexOf(".") + 1);
            Log.d("asd", extension);
            dialog.dismiss();
            check = -1;

        } else {
            try {
                // When an Image is picked
                if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                        && null != data) {
                    // Get the Image from data

                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    // Get the cursor
                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String imgDecodableString = cursor.getString(columnIndex);
                    Log.d("asd", imgDecodableString);
                    filename = imgDecodableString.substring(imgDecodableString.lastIndexOf("/") + 1);
                    Log.d("asd", filename);
                    extension = filename.substring(filename.lastIndexOf(".") + 1);
                    Log.d("asd", extension);
                    cursor.close();
                    //Toast.makeText(this,imgDecodableString,Toast.LENGTH_SHORT).show();
                    //ImageView imgView = (ImageView) findViewById(R.id.imageView1);
                    // Set the Image in ImageView after decoding the String
                    bm = BitmapFactory.decodeFile(imgDecodableString);
                    imageView.setImageBitmap(bm);
                    dialog.dismiss();
                    check = -1;
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

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
