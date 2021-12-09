package com.example.gift;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class OrderDetails extends AppCompatActivity {
    TextView t1, t2, t3, t4, t5,t6,t7;
    String date,name,orderid,address,amount,type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        t1 = (TextView) findViewById(R.id.name);

        t2 = (TextView) findViewById(R.id.orderid);
        t3 = (TextView) findViewById(R.id.date);
        t4 = (TextView) findViewById(R.id.address);
        t5 = (TextView) findViewById(R.id.pro_list);
        t6 = (TextView) findViewById(R.id.type1);
        t7 = (TextView) findViewById(R.id.amount);

        DB_Conn obj = new DB_Conn();
        obj.execute();

        orderid=getIntent().getStringExtra("oid");
        date=getIntent().getStringExtra("date");
        name=getIntent().getStringExtra("name");
        t1.setText(name);
        t2.setText(orderid);
        t3.setText(date);
    }
    class DB_Conn extends AsyncTask<String, Void, String> {
        String t="";


        @Override
        public String doInBackground(String... arg) //compulsory to implement
        {
            String r = "";
            try {
                Connection con=DB_Connection.get_DBConnection();

                PreparedStatement pst = con.prepareStatement("select * from orders where orderno=?");
                pst.setString(1, orderid);
                ResultSet rs = pst.executeQuery();
                rs.next();
                    address = rs.getString("address");
                    type = rs.getString("payment_mode");
                    amount=rs.getString("grand_total");


                PreparedStatement pst2 = con.prepareStatement("select * from orders_products where orderno=?");
                pst2.setString(1, orderid);
                ResultSet rs1 = pst2.executeQuery();

                while (rs1.next()) {
                    t = t + "\n " + rs1.getString("pro_name");
                    t = t + "\nPrice: " + rs1.getString("price");
                    t = t + "\nQty: " + rs1.getString("qty");
                    t = t + "\n__________________________________________";

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
                t4.setText(address);
                t5.setText(t);
                t6.setText(type);
                t7.setText("Rs "+amount+"/-");

            }
        }

    }
}
