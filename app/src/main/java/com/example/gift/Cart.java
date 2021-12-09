package com.example.gift;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.TextView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Cart extends AppCompatActivity {
    RecyclerView r;
    Cart_Adapter bd;
    TextView t1;
    Toolbar toolbar;
    ImageView iv;
    Button b1;
    String cart="cart";
    int total=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        r=(RecyclerView)findViewById(R.id.rc);
        r.setLayoutManager(new LinearLayoutManager(this));
        t1=(TextView)findViewById(R.id.txt_total);
        iv=(ImageView)findViewById(R.id.image);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(" My Cart");


        DB_Conn obj = new DB_Conn();
        obj.execute();

        b1=(Button)findViewById(R.id.button9);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Cart.this, DeliveryAddress.class);
                i.putExtra("amt", total+"");
                i.putExtra("page", cart);
                startActivity(i);
            }
        });

    }
    class DB_Conn extends AsyncTask<String,Void,String>
    {

        ArrayList<HashMap<String,String>> arrayList=new ArrayList<>();
        ArrayList<HashMap<String, Bitmap>> arrayList1=new ArrayList<>();

        @Override
        public String doInBackground(String...arg) //compulsory to implement
        {
            String r="";
            Bitmap bitmap;
            try {
                SharedPreferences sp = getSharedPreferences("pf", Context.MODE_PRIVATE);
                String userid = sp.getString("userid", null);

                Connection con=DB_Connection.get_DBConnection();
                PreparedStatement pst = con.prepareStatement("select * from cart where userid=?");
                pst.setString(1,userid);
                ResultSet rs = pst.executeQuery();
                while(rs.next()) {
                    HashMap<String, String> hashMap = new HashMap<>();
                    HashMap<String, Bitmap> hashMap1 = new HashMap<>();
                    hashMap.put("pno", rs.getString("pno"));
                    hashMap.put("name", rs.getString("pro_name"));
                    hashMap.put("price", rs.getString("price"));
                    hashMap.put("qty", rs.getString("qty"));
                    byte b[] = rs.getBytes("pic");
                    bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                    hashMap1.put("image", bitmap);
                    arrayList.add(hashMap);
                    arrayList1.add(hashMap1);
                    total = total + (rs.getInt("price")*rs.getInt("qty"));
                }
                
                con.close();
                if(arrayList.size()>0){
                    r="success";
                }
                else{
                    r="failure";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return r;
        }
        @Override
        public void onProgressUpdate(Void...arg0)
        {

        }
        @Override
        public void onPostExecute(String result)
        {
            if(result.equals("success"))
            {

                bd=new Cart_Adapter(Cart.this,arrayList,arrayList1);
                r.setAdapter(bd);
                t1.setText(total+"");
            }
            else
                {
                iv.setImageResource(R.drawable.box);
                b1.setEnabled(false);
                b1.setBackgroundResource(R.drawable.disable_button);
            }

        }
        @Override
        public void onPreExecute()
        {
            // do something before start
        }

    }
}
