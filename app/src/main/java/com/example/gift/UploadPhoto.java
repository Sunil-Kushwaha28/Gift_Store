package com.example.gift;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;

public class UploadPhoto extends AppCompatActivity implements View.OnClickListener {
    Button b1, b2;
    ImageView iv;
    Uri selectedImage;
    String pno,name,price;
    int n = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_photo);
        b1=(Button)findViewById(R.id.button16);
        b2=(Button)findViewById(R.id.button);
        iv=(ImageView)findViewById(R.id.imageView2);
        price=getIntent().getStringExtra("amt");
        pno=getIntent().getStringExtra("pno");
        name=getIntent().getStringExtra("name");
        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.button16){
            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(i, 1);
        }
        else{
            DB_Conn obj=new DB_Conn();
            obj.execute();
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            selectedImage = data.getData();
            try {


                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                ByteArrayOutputStream bao = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, bao);
                byte[] ba = bao.toByteArray();
                int size = ba.length;
                iv.setImageBitmap(bitmap);


            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    class DB_Conn extends AsyncTask<Void,Void,String>
    {

        @Override
        public String doInBackground(Void...arg) //compulsory to implement
        {
            SharedPreferences sp = getSharedPreferences("pf", Context.MODE_PRIVATE);
            boolean l = sp.getBoolean("loggedin", false);
            String userid = sp.getString("userid", null);
            String r="";
            try {
                InputStream iStream = getContentResolver().openInputStream(selectedImage);
                Connection con=DB_Connection.get_DBConnection();
                PreparedStatement pst = con.prepareStatement("select max(no) from custom_photo");

                ResultSet rs = pst.executeQuery();
                rs.next();
                String v = rs.getString(1);

                if (v != null) {
                    n = Integer.parseInt(v) + 1;
                }

                pst = con.prepareStatement("insert into custom_photo values(?,?,?,?,?)");
                pst.setString(1, n+"");
                pst.setString(2, userid);
                pst.setString(3,name);
                pst.setString(4,new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()) );
                pst.setBlob(5, iStream);

                pst.execute();

                r = "success";


                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return r;
        }
        @Override
        public void onProgressUpdate(Void...arg0) //optional
        {

        }
        @Override
        public void onPostExecute(String result) //optional
        {
            //  do something after execution
            if(result.equals("success"))
            {

                AlertDialog.Builder alert = new AlertDialog.Builder(UploadPhoto.this);
                alert.setTitle("Success");
                alert.setMessage("Photo uploaded successfully");
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface obj, int x) {
                        UploadPhoto.this.finish();

                    }


                });
                AlertDialog alertDialog = alert.create();
                alertDialog.show();
                Intent i=new Intent(UploadPhoto.this,DeliveryAddress.class);
                i.putExtra("pno",pno);
                i.putExtra("amt",price);
                startActivity(i);

            }


        }


        @Override
        public void onPreExecute() //optional
        {
            // do something before start
        }

    }
}
