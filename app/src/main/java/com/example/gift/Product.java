package com.example.gift;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Map;

public class Product extends AppCompatActivity implements View.OnClickListener {
    String pno,name,price,Qty,amt,product="product";
    TextView t1,t2;
    ImageView iv;
    int total=0;
    ArrayAdapter adapter;
    Spinner spinner;
    Button b1,b2;
    Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        pno=getIntent().getStringExtra("pno");
        amt=getIntent().getStringExtra("amt");
        t1=(TextView)findViewById(R.id.product_name);
        t2=(TextView)findViewById(R.id.product_price);
        iv=(ImageView)findViewById(R.id.product_img);
        b1=(Button)findViewById(R.id.button);
        b2=(Button)findViewById(R.id.button1);
        spinner=(Spinner)findViewById(R.id.qty);
        String c[] = {"1","2","3","4","5"};
        adapter=new ArrayAdapter(this, android.R.layout.simple_spinner_item,c);
        spinner.setAdapter(adapter);
        spinner.setBackgroundColor(000);


        DB_Conn obj = new DB_Conn();
        obj.execute();
        b1.setOnClickListener(this);
        b2.setOnClickListener(this);

    }
    public void onClick(View v)
    {
        Qty=spinner.getSelectedItem().toString();
        total=(Integer.parseInt(amt)*Integer.parseInt(Qty));

        if(v.getId()==R.id.button1){
            Intent i=new Intent(this,DeliveryAddress.class);
            i.putExtra("pno",pno);
            i.putExtra("page",product);
            i.putExtra("qty",Qty);
            i.putExtra("total", total+"");
            i.putExtra("amt",t2.getText().toString());
            startActivity(i);
        }
        if(v.getId()==R.id.button)
        {
            DB_Conn1 obj = new DB_Conn1();
            obj.execute();
        }

    }


    class DB_Conn extends AsyncTask<String, Void, String> {

        @Override
        public String doInBackground(String... arg) //compulsory to implement
        {
            String r = "";

            try {
                Connection con=DB_Connection.get_DBConnection();
                PreparedStatement pst = con.prepareStatement("Select * from product where pno=?");
                pst.setString(1, pno);
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    name=rs.getString("name");
                    price=rs.getString("price");
                    byte b[] = rs.getBytes("pic");
                    bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                }

                con.close();
                r="success";


            } catch (Exception e) {
                e.printStackTrace();
            }
            return r;
        }

        public void onPostExecute(String result) //optional
        {
            //  do something after execution
            if (result.equals("success")) {

                t1.setText(name);
                t2.setText("₹"+price);
                iv.setImageBitmap(bitmap);
            }
        }

    }
    class DB_Conn1 extends AsyncTask<String, Void, String> {

        @Override
        public String doInBackground(String... arg) //compulsory to implement
        {
            SharedPreferences sp = getSharedPreferences("pf", Context.MODE_PRIVATE);
            String userid = sp.getString("userid", null);
            String r="";
            try {
                Connection con=DB_Connection.get_DBConnection();
                PreparedStatement pst = con.prepareStatement("select * from cart where pno=?");
                pst.setString(1, pno);
                ResultSet rs = pst.executeQuery();
                if(rs.next()==false) {
                    PreparedStatement pst1 = con.prepareStatement("select * from product where pno=?");
                    pst1.setString(1, pno);
                    ResultSet rs2 = pst1.executeQuery();
                    rs2.next();
                    PreparedStatement pst2 = con.prepareStatement("insert into cart values(?,?,?,?,?,?)");
                    pst2.setString(1, rs2.getString("pno"));
                    pst2.setString(2, rs2.getString("name"));
                    pst2.setString(3, rs2.getString("price"));
                    pst2.setString(4, Qty);
                    pst2.setBlob(5, rs2.getBlob("pic"));
                    pst2.setString(6, userid);
                    pst2.execute();
                    con.close();
                    r = "success";
                }
                else{
                    r = "failed";
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            return r;
        }

        public void onPostExecute(String result) //optional
        {
            //  do something after execution
            if (result.equals("success"))
            {
                Toast.makeText(Product.this,"Product added to cart",Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(Product.this,"Product already present in cart",Toast.LENGTH_SHORT).show();
            }
        }

    }
}
