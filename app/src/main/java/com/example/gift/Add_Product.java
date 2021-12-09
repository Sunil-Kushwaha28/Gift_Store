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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;

public class Add_Product extends AppCompatActivity implements View.OnClickListener {
    Connection con;
    int n = 1;
    static EditText txt_mno, txt_name, txt_price;
    String pno, name, price, category;
    Uri selectedImage;
    ImageView iv;
    Button b1,b2;
    Spinner s;
    ArrayAdapter adapter1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__product);
        s = (Spinner) findViewById(R.id.spinner);
        String c[] = {"Plants", "Flowers", "Sweet", "Customized", "Toys & Games", "Cake","Home Decoration","Jewelry"};
        adapter1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, c);
        s.setAdapter(adapter1);

        txt_mno = (EditText) findViewById(R.id.txt_userid);
        txt_name = (EditText) findViewById(R.id.editText7);
        txt_price = (EditText) findViewById(R.id.editText8);
        iv=(ImageView)findViewById(R.id.image_view);

        b1 = (Button) findViewById(R.id.add);
        b2 = (Button) findViewById(R.id.gallery);
        b1.setOnClickListener(this);
        b2.setOnClickListener(this);

        DB_Conn1 obj = new DB_Conn1();
        obj.execute();

    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.add)
        {
            name = txt_name.getText().toString();
            price = txt_price.getText().toString();
            pno = txt_mno.getText().toString().substring(12);
            category = s.getSelectedItem().toString();

                if (!name.isEmpty()) {
                    if (!price.isEmpty() && Integer.parseInt(price) > 0) {

                        DB_Conn obj = new DB_Conn();
                        obj.execute();

                    } else {
                        txt_price.setError("Value not entered or invalid value entered");
                    }
                } else {
                    txt_name.setError("Value is required");
                }

        }
        else{
            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(i, 1);
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
    class DB_Conn1 extends AsyncTask<Void, Void, String> {

        @Override
        public String doInBackground(Void... arg) //compulsory to implement
        {
            String r = "";
            try {

                con = DB_Connection.get_DBConnection();
                PreparedStatement pst = con.prepareStatement("select max(pno) from product");
                ResultSet rs = pst.executeQuery();
                rs.next();
                String v = rs.getString(1);

                if (v != null) {
                    n = Integer.parseInt(v) + 1;

                }
                r = "success";
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return r;
        }


        @Override
        public void onPostExecute(String result) //optional
        {   //  do something after execution
            if (result.equals("success")) {
                txt_mno.setText("Product No: " + n);

            }
        }

    }


    class DB_Conn extends AsyncTask<Void, Void, String> {

        @Override
        public String doInBackground(Void... arg) //compulsory to implement
        {
            String r = "";
            try {
                InputStream iStream = getContentResolver().openInputStream(selectedImage);
                Connection con = DB_Connection.get_DBConnection();

                PreparedStatement pst = con.prepareStatement("insert into product values(?,?,?,?,?)");
                pst.setString(1, pno);
                pst.setString(2, name);
                pst.setInt(3, Integer.parseInt(price));
                pst.setString(4, category);
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
        public void onPostExecute(String result) //optional
        {
            //  do something after execution
            if (result.equals("success")) {

                AlertDialog.Builder alert = new AlertDialog.Builder(Add_Product.this);
                alert.setTitle("Success");
                alert.setMessage("Product added successfully");
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface obj, int x) {
                        Add_Product.this.finish();

                    }
                });
                AlertDialog alertDialog = alert.create();
                alertDialog.show();

            }

        }

    }


}
