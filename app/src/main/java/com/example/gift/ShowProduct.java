package com.example.gift;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

public class ShowProduct extends AppCompatActivity {
    String o;
    TextView t1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_product);
        o=getIntent().getStringExtra("no");
        t1=(TextView)findViewById(R.id.product);

        DB_Conn obj = new DB_Conn();
        obj.execute();

    }
    class DB_Conn extends AsyncTask<String,Void,String>
    {
        String t="";
        ArrayList<HashMap<String,String>> arrayList=new ArrayList<>();

        @Override
        public String doInBackground(String...arg) //compulsory to implement
        {
            String r="";
            try {

                Connection con=DB_Connection.get_DBConnection();

                PreparedStatement pst= con.prepareStatement("select * from orders_products where orderno=?");
                pst.setString(1, o);
                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                    t = t + "\n " + rs.getString("pro_name");
                    t = t + "\nPrice: " + rs.getString("price");
                    t = t + "\nQty: " + rs.getString("qty");
                    t = t + "\n__________________________________________";

                }
                rs.close();


                con.close();
                r="success";



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

                t1.setText(t);

            }
        }
        @Override
        public void onPreExecute() //optional
        {
            // do something before start
        }

    }
}
