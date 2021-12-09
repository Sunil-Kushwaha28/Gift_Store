package com.example.gift;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CustomProduct extends AppCompatActivity implements View.OnClickListener {
    String pno,name,price;
    TextView t1,t2;
    ImageView iv;
    Button b1;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_product);
        pno=getIntent().getStringExtra("pno");
        t1=(TextView)findViewById(R.id.product_name);
        t2=(TextView)findViewById(R.id.product_price);
        iv=(ImageView)findViewById(R.id.product_img);
        b1=(Button)findViewById(R.id.button);

        DB_Conn obj = new DB_Conn();
        obj.execute();
        b1.setOnClickListener(this);
    }
    public void onClick(View v)
    {
        if(v.getId()==R.id.button){
            Intent i=new Intent(this,UploadPhoto.class);
            i.putExtra("pno",pno);
            i.putExtra("amt",t2.getText().toString());
            i.putExtra("name",t1.getText().toString());
            startActivity(i);
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
}
