package com.example.cybercell.bms_upload_pic;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
         private static String NAMESPACE = "http://tempuri.org/";
    //private static final String URL = "http://10.0.3.2:5419/DemoService.asmx";
private static final String URL = "http://164.100.180.82/mobpdms/demoservice.asmx";
// private static final String URL = "http://10.135.32.152/demo/DemoService.asmx";
//  private static final String URL = "http://10.0.3.2:2959/Demoservice/DemoService.asmx";
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    SessionManagement session;
    String serverreponse = " ";
    SoapPrimitive response;
    String toiletencodedImage, rampencodedImage, waterencodedImage, furnitureencodedImage, electricencodedImage, shadeencodedImage, telephoneencodedImage;

    String dis_code,ac_code,ps_code;
    String lattitude, longitude;
    private Uri fileUri;
    private File imagesfolder;
    private String pic_type,file_name;
    byte[] byteArray;
    String bmfdate;
    String Rdate, Teldate, Tdate, Sdate, Edate, Fdate, Wdate;
    int Telflag, Tflag, Rflag, Fflag, Wflag, Sflag, Eflag=0;

    ImageButton pic_toilet, pic_water, pic_telephone, pic_furniture, pic_ramp, pic_electric, pic_shade_teen;
    TextView txt_take_pic_toilet, txt_take_pic_water, txt_take_pic_telephone, txt_take_pic_electric, txt_take_pic_ramp, txt_take_pic_furniture, txt_take_pic_shade_teen;
    TextView acname,districtname,psname;
    private ProgressDialog pDialog;
    private String TAG = "BMF";

    //Class to get Lattitude and Longitude
    GPSTracker gps;

   // public static final String PREFS_NAME = "LoginPrefs";


    Button btn_uplaod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        txt_take_pic_toilet = (TextView) findViewById(R.id.txt_take_pic_toilet);
        txt_take_pic_water = (TextView) findViewById(R.id.txt_take_pic_water);
        txt_take_pic_telephone = (TextView) findViewById(R.id.txt_take_pic_telephone);
        txt_take_pic_electric = (TextView) findViewById(R.id.txt_take_pic_electric);
        txt_take_pic_ramp = (TextView) findViewById(R.id.txt_take_pic_ramp);
        txt_take_pic_furniture = (TextView) findViewById(R.id.txt_take_pic_furniture);
        txt_take_pic_shade_teen = (TextView) findViewById(R.id.txt_take_pic_shade_teen);


        pic_toilet = (ImageButton) findViewById(R.id.pic_toilet);
        pic_water = (ImageButton) findViewById(R.id.pic_water);
        pic_telephone = (ImageButton) findViewById(R.id.pic_telephone);
        pic_electric = (ImageButton) findViewById(R.id.pic_electric);
        pic_furniture = (ImageButton) findViewById(R.id.pic_furniture);
        pic_ramp = (ImageButton) findViewById(R.id.pic_ramp);
        pic_shade_teen = (ImageButton) findViewById(R.id.pic_shade_teen);
        btn_uplaod = (Button) findViewById(R.id.btn_upload);

        acname=(TextView)findViewById(R.id.txt_ac);
        districtname=(TextView)findViewById(R.id.txt_dis);
        psname=(TextView)findViewById(R.id.txt_pollings);
        session=new SessionManagement(getApplicationContext());

        // Get district Name from session
        HashMap<String, String> user = session.getDistrict();

        districtname.setText(user.get(SessionManagement.KEY_DISNAME) + " (" + user.get(SessionManagement.KEY_DISID) + ")");
        dis_code = user.get(SessionManagement.KEY_DISID);
        // Get PS and AC Name from session

        HashMap<String,String> acps=session.getACPS();
        acname.setText(acps.get(SessionManagement.KEY_ACNAME));
        psname.setText(acps.get(SessionManagement.KEY_PSNAME));
        ac_code = acps.get(SessionManagement.KEY_ACID);
        ps_code = acps.get(SessionManagement.KEY_PSID);

      /*  pic_toilet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pic_toilet.setScaleType(ImageView.ScaleType.FIT_XY);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
            }
        });*/

        txt_take_pic_toilet.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                selectimage();
                pic_type = "T";
            }
        });
        txt_take_pic_water.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                selectimage();
                pic_type = "W";
            }
        });
        txt_take_pic_telephone.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                selectimage();
                pic_type = "H";
            }
        });
        txt_take_pic_electric.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                selectimage();
                pic_type = "E";
            }
        });
        txt_take_pic_ramp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                selectimage();
                pic_type = "R";
            }
        });
        txt_take_pic_furniture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                selectimage();
                pic_type = "F";
            }
        });
        txt_take_pic_shade_teen.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                selectimage();
                pic_type = "ST";
            }
        });

        btn_uplaod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncCallWS tast = new AsyncCallWS();
                tast.execute();
                redirect();
            }
        });
        getpolllocation();
        //getdatetime();
    }
    public void redirect(){
        Intent intent=new Intent(this,SuccessActivity.class);
        startActivity(intent);
    }

    private String getdatetime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss aa");
        bmfdate=String.valueOf(dateformat.format(c.getTime()));
        return bmfdate;
    }

    private void getpolllocation() {
        // create class object
        gps = new GPSTracker(MainActivity.this);

        // check if GPS enabled
        if (gps.canGetLocation()) {

            lattitude = String.valueOf(gps.getLatitude());
            longitude = String.valueOf(gps.getLongitude());
            /*lattitude = "got";
            longitude = "got it";*/

            // \n is for new line
            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + lattitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.title_menu, menu);
        return true;
    }


    public void selectimage(){

        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Add Image");
        builder.setItems(options, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int item){
                if (options[item].equals("Take Photo")) {

                        if(pic_type=="T"){
                            captureImage("T.JPEG");
                    }
                        else if(pic_type=="W"){
                            captureImage("water_001.JPEG");
                        }
                        else if(pic_type=="H"){
                            captureImage("telephone_001.JPEG");
                        }
                        else if(pic_type=="R"){
                            captureImage("ramp_001.JPEG");
                        }
                        else if(pic_type=="E"){
                        captureImage("electric_001.JPEG");
                    }
                        else if(pic_type=="F"){
                            captureImage("furniture_001.JPEG");
                        }
                        else if(pic_type=="ST"){
                            captureImage("shade_teen_001.JPEG");
                        }
                }
                else if (options[item].equals("Choose from Gallery")) {
                    if(pic_type=="T"){
                        ChooseImage();
                    }
                    else if(pic_type=="W"){
                        ChooseImage();
                    }
                    else if(pic_type=="H"){
                        ChooseImage();
                    }
                    else if(pic_type=="R"){
                        ChooseImage();
                    }
                    else if(pic_type=="E"){
                        ChooseImage();
                    }
                    else if(pic_type=="F"){
                        ChooseImage();
                    }
                    else if(pic_type=="ST"){
                        ChooseImage();
                    }
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    public void captureImage(String filename)
    {
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String folder_name="/BMFIMAGES/" + dis_code +"/"+ac_code+"/"+ps_code;
        imagesfolder=new File(Environment.getExternalStorageDirectory(),folder_name);
        imagesfolder.mkdirs();
        File image=new File(imagesfolder,filename);
        fileUri= Uri.fromFile(image);
        file_name=filename;
        intent.putExtra(MediaStore.EXTRA_OUTPUT,fileUri);
        startActivityForResult(intent,CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }
    public void ChooseImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
        }
    protected void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        if(requestCode==CAMERA_CAPTURE_IMAGE_REQUEST_CODE)
        {
            if(resultCode==RESULT_OK){
                Bitmap b=BitmapFactory.decodeFile(fileUri.getPath());
                Bitmap out=Bitmap.createScaledBitmap(b,180,240,false);
                File f=new File(imagesfolder,file_name);
                FileOutputStream fout;
                try
                {
                    fout=new FileOutputStream(f);
                    out.compress(Bitmap.CompressFormat.JPEG,80,fout);
                    fout.flush();
                    fout.close();
                    b.recycle();
                    out.recycle();
                }
                catch (Exception e)
                {

                }
                previewCapturedImage(fileUri,pic_type);
            }else if (resultCode==RESULT_CANCELED){
                Toast.makeText(getApplicationContext(),"User Cancelled Image Capture",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(),"Sorry!!! Failed to Capture Image",Toast.LENGTH_SHORT).show();
            }
        }
        else if(requestCode==1) {
            if (resultCode == RESULT_OK) {
                SelectFromGalleryResult(data,pic_type);
            }
        }
    }

    private void SelectFromGalleryResult(Intent data,String pic_show) {
        Bitmap bm = null;
        Uri selectedImage = data.getData();
        String[] filePathColumn = { MediaStore.Images.Media.DATA };

        Cursor cursor = getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inSampleSize=6;
        final Bitmap bitmap=BitmapFactory.decodeFile(picturePath,options);

        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byteArray = stream.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
            switch (pic_show) {
                case "T":
                    pic_toilet.setVisibility(View.VISIBLE);
                    pic_toilet.setImageBitmap(bitmap);
                    toiletencodedImage = Base64.encodeToString(byteArray, Base64.NO_WRAP);
                    break;
                case "W":
                    pic_water.setVisibility(View.VISIBLE);
                    pic_water.setImageBitmap(bitmap);
                    waterencodedImage = Base64.encodeToString(byteArray, Base64.NO_WRAP);
                    break;
                case "E":
                    pic_electric.setVisibility(View.VISIBLE);
                    pic_electric.setImageBitmap(bitmap);
                    electricencodedImage = Base64.encodeToString(byteArray, Base64.NO_WRAP);
                    break;
                case "H":
                    pic_telephone.setVisibility(View.VISIBLE);
                    pic_telephone.setImageBitmap(bitmap);
                    telephoneencodedImage = Base64.encodeToString(byteArray, Base64.NO_WRAP);
                    break;
                case "R":
                    pic_ramp.setVisibility(View.VISIBLE);
                    pic_ramp.setImageBitmap(bitmap);
                    rampencodedImage = Base64.encodeToString(byteArray, Base64.NO_WRAP);
                    break;
                case "F":
                    pic_furniture.setVisibility(View.VISIBLE);
                    pic_furniture.setImageBitmap(bitmap);
                    furnitureencodedImage = Base64.encodeToString(byteArray, Base64.NO_WRAP);
                    break;
                case "ST":
                    pic_shade_teen.setVisibility(View.VISIBLE);
                    pic_shade_teen.setImageBitmap(bitmap);
                    shadeencodedImage = Base64.encodeToString(byteArray, Base64.NO_WRAP);
                    break;
            }
        }
        else {
            Toast.makeText(getApplicationContext(), "There's an error if this code doesn't work, thats all I know", Toast.LENGTH_SHORT).show();
        }
    }
    private void previewCapturedImage(Uri url, String pic_show) {
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inSampleSize=4;
        final Bitmap bitmap=BitmapFactory.decodeFile(fileUri.getPath(),options);
        try {
            switch(pic_show) {
                case "T":pic_toilet.setVisibility(View.VISIBLE);
                    pic_toilet.setImageBitmap(bitmap);
                    Tdate=getdatetime();
                    if (bitmap != null) {
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        byteArray = stream.toByteArray();
                        toiletencodedImage = Base64.encodeToString(byteArray, Base64.NO_WRAP);
                        Tflag=1;
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"There's an error if this code doesn't work, thats all I know",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case "W":pic_water.setVisibility(View.VISIBLE);
                    pic_water.setImageBitmap(bitmap);
                    Wdate=getdatetime();
                    if (bitmap != null) {
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        byteArray = stream.toByteArray();
                        waterencodedImage = Base64.encodeToString(byteArray, Base64.NO_WRAP);
                        Wflag=1;
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"There's an error if this code doesn't work, thats all I know",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case "E":pic_electric.setVisibility(View.VISIBLE);
                    pic_electric.setImageBitmap(bitmap);
                    Edate=getdatetime();
                    if (bitmap != null) {
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        byteArray = stream.toByteArray();
                        electricencodedImage = Base64.encodeToString(byteArray, Base64.NO_WRAP);
                        Eflag=1;
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"There's an error if this code doesn't work, thats all I know",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case "H":pic_telephone.setVisibility(View.VISIBLE);
                    pic_telephone.setImageBitmap(bitmap);
                    Teldate=getdatetime();
                    if (bitmap != null) {
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        byteArray = stream.toByteArray();
                        telephoneencodedImage = Base64.encodeToString(byteArray, Base64.NO_WRAP);
                        Telflag=1;
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"There's an error if this code doesn't work, thats all I know",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case "R":pic_ramp.setVisibility(View.VISIBLE);
                    pic_ramp.setImageBitmap(bitmap);
                    Rdate=getdatetime();
                    if (bitmap != null) {
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        byteArray = stream.toByteArray();
                        rampencodedImage = Base64.encodeToString(byteArray, Base64.NO_WRAP);
                        Rflag=1;
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"There's an error if this code doesn't work, thats all I know",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case "F":pic_furniture.setVisibility(View.VISIBLE);
                    pic_furniture.setImageBitmap(bitmap);
                    Fdate=getdatetime();
                    if (bitmap != null) {
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        byteArray = stream.toByteArray();
                        furnitureencodedImage = Base64.encodeToString(byteArray, Base64.NO_WRAP);
                        Fflag=1;
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"There's an error if this code doesn't work, thats all I know",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case "ST":pic_shade_teen.setVisibility(View.VISIBLE);
                    pic_shade_teen.setImageBitmap(bitmap);
                    Sdate=getdatetime();
                    if (bitmap != null) {
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        byteArray = stream.toByteArray();
                        shadeencodedImage = Base64.encodeToString(byteArray, Base64.NO_WRAP);
                        Sflag=1;
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"There's an error if this code doesn't work, thats all I know",Toast.LENGTH_SHORT).show();
                    }
                    break;
            }

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private class AsyncCallWS extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            try {
                SoapObject request = new SoapObject(NAMESPACE, "UploadBMFImage");
                PropertyInfo e_ID = new PropertyInfo();
                e_ID.setName("ramp");
                e_ID.setValue(rampencodedImage);
                e_ID.setType(double.class);
                PropertyInfo f_ID = new PropertyInfo();
                f_ID.setName("water");
                f_ID.setValue(waterencodedImage);
                f_ID.setType(double.class);
                PropertyInfo g_ID = new PropertyInfo();
                g_ID.setName("telephone");
                g_ID.setValue(telephoneencodedImage);
                g_ID.setType(double.class);
                PropertyInfo h_ID = new PropertyInfo();
                h_ID.setName("toilet");
                h_ID.setValue(toiletencodedImage);
                h_ID.setType(double.class);
                PropertyInfo i_ID = new PropertyInfo();
                i_ID.setName("electricity");
                i_ID.setValue(electricencodedImage);
                i_ID.setType(double.class);
                PropertyInfo j_ID = new PropertyInfo();
                j_ID.setName("furniture");
                j_ID.setValue(furnitureencodedImage);
                j_ID.setType(double.class);
                PropertyInfo k_ID = new PropertyInfo();
                k_ID.setName("shade");
                k_ID.setValue(shadeencodedImage);
                k_ID.setType(double.class);
                PropertyInfo dis_ID = new PropertyInfo();
                dis_ID.setName("Dis");
                dis_ID.setValue(dis_code);
                dis_ID.setType(double.class);
                PropertyInfo AC_ID = new PropertyInfo();
                AC_ID.setName("AC");
                AC_ID.setValue(ac_code);
                AC_ID.setType(double.class);
                PropertyInfo PS_ID = new PropertyInfo();
                PS_ID.setName("PS");
                PS_ID.setValue(ps_code);
                PS_ID.setType(double.class);
                PropertyInfo Lat_ID= new PropertyInfo();
                Lat_ID.setName("Lat");
                Lat_ID.setValue(lattitude);
                Lat_ID.setType(double.class);
                PropertyInfo Long_ID = new PropertyInfo();
                Long_ID.setName("Long");
                Long_ID.setValue(longitude);
                Long_ID.setType(double.class);
                PropertyInfo Rdate_ID = new PropertyInfo();
                Rdate_ID.setName("Rdate");
                Rdate_ID.setValue(Rdate);
                Rdate_ID.setType(double.class);
                PropertyInfo Teldate_ID = new PropertyInfo();
                Teldate_ID.setName("Teldate");
                Teldate_ID.setValue(Teldate);
                Teldate_ID.setType(double.class);
                PropertyInfo Tdate_ID = new PropertyInfo();
                Tdate_ID.setName("Tdate");
                Tdate_ID.setValue(Tdate);
                Tdate_ID.setType(double.class);
                PropertyInfo Sdate_ID = new PropertyInfo();
                Sdate_ID.setName("Sdate");
                Sdate_ID.setValue(Sdate);
                Sdate_ID.setType(double.class);
                PropertyInfo Edate_ID = new PropertyInfo();
                Edate_ID.setName("Edate");
                Edate_ID.setValue(Edate);
                Edate_ID.setType(double.class);
                PropertyInfo Fdate_ID = new PropertyInfo();
                Fdate_ID.setName("Fdate");
                Fdate_ID.setValue(Fdate);
                Fdate_ID.setType(double.class);
                PropertyInfo Wdate_ID = new PropertyInfo();
                Wdate_ID.setName("Wdate");
                Wdate_ID.setValue(Wdate);
                Wdate_ID.setType(double.class);
                PropertyInfo Telflag_ID = new PropertyInfo();
                Telflag_ID.setName("Telflag");
                Telflag_ID.setValue(Telflag);
                Telflag_ID.setType(double.class);
                PropertyInfo Tflag_ID = new PropertyInfo();
                Tflag_ID.setName("Tflag");
                Tflag_ID.setValue(Tflag);
                Tflag_ID.setType(double.class);
                PropertyInfo Rflag_ID = new PropertyInfo();
                Rflag_ID.setName("Rflag");
                Rflag_ID.setValue(Rflag);
                Rflag_ID.setType(double.class);
                PropertyInfo Fflag_ID = new PropertyInfo();
                Fflag_ID.setName("Fflag");
                Fflag_ID.setValue(Fflag);
                Fflag_ID.setType(double.class);
                PropertyInfo Wflag_ID = new PropertyInfo();
                Wflag_ID.setName("Wflag");
                Wflag_ID.setValue(Wflag);
                Wflag_ID.setType(double.class);
                PropertyInfo Sflag_ID = new PropertyInfo();
                Sflag_ID.setName("Sflag");
                Sflag_ID.setValue(Sflag);
                Sflag_ID.setType(double.class);
                PropertyInfo Eflag_ID = new PropertyInfo();
                Eflag_ID.setName("Eflag");
                Eflag_ID.setValue(Eflag);
                Eflag_ID.setType(double.class);
                request.addProperty(e_ID);
                request.addProperty(f_ID);
                request.addProperty(g_ID);
                request.addProperty(h_ID);
                request.addProperty(i_ID);
                request.addProperty(j_ID);
                request.addProperty(k_ID);
                request.addProperty(dis_ID);
                request.addProperty(AC_ID);
                request.addProperty(PS_ID);
                request.addProperty(Lat_ID);
                request.addProperty(Long_ID);
                request.addProperty(Rdate_ID);
                request.addProperty(Teldate_ID);
                request.addProperty(Tdate_ID);
                request.addProperty(Sdate_ID);
                request.addProperty(Edate_ID);
                request.addProperty(Fdate_ID);
                request.addProperty(Wdate_ID);
                request.addProperty(Telflag_ID);
                request.addProperty(Tflag_ID);
                request.addProperty(Rflag_ID);
                request.addProperty(Fflag_ID);
                request.addProperty(Wflag_ID);
                request.addProperty(Sflag_ID);
                request.addProperty(Eflag_ID);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);
                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL, 60000);
                androidHttpTransport.call(NAMESPACE + "UploadBMFImage", envelope);
                SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
                serverreponse = response.toString();
                }
            catch (InterruptedIOException iioe)	{
                Toast.makeText(getApplicationContext(), iioe.toString(),Toast.LENGTH_LONG).show();
            }
            catch (IOException ioe) {
                Toast.makeText(getApplicationContext(), ioe.toString(),Toast.LENGTH_LONG).show();
            }
            catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.toString(),Toast.LENGTH_LONG).show();
                }
        return  serverreponse;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.i(TAG, "onPostExecute");
            try {
                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Images Uploaded",Toast.LENGTH_LONG).show();
                    pDialog.setCancelable(true);
                    pDialog.show();
                 }

            }
            catch (Exception e){}
        }

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please Wait...");
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.i(TAG, "onProgressUpdate");
        }
    }
}
