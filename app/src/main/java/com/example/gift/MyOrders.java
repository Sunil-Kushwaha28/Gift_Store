package com.example.gift;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyOrders extends AppCompatActivity {
    RecyclerView r;
    MyOrders_Adapter bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);
        r = (RecyclerView) findViewById(R.id.rect);
        r.setLayoutManager(new LinearLayoutManager(this));

        DB_Conn obj = new DB_Conn();
        obj.execute();

    }


    class DB_Conn extends AsyncTask<String, Void, String> {

        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();

        @Override
        public String doInBackground(String... arg) //compulsory to implement
        {
            String r = "";
            PreparedStatement pst;
            ResultSet rs;
            try {
                SharedPreferences sp = getSharedPreferences("pf", Context.MODE_PRIVATE);
                String userid = sp.getString("userid", null);
                Connection con = DB_Connection.get_DBConnection();

                    pst = con.prepareStatement("select * from user where emailid=?");
                    pst.setString(1, userid);
                    rs = pst.executeQuery();
                    rs.next();

                    PreparedStatement pst2=con.prepareStatement("select * from orders where userid=? order by orderdate desc");
                    pst2.setString(1,userid);
                    ResultSet rs1 = pst2.executeQuery();

                    while (rs1.next()){
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("name", rs.getString("name"));
                        hashMap.put("id", rs1.getString(1));
                        hashMap.put("date", rs1.getString(2));
                        arrayList.add(hashMap);
                    }



                con.close();

                if (arrayList.size() > 0) {
                    r = "success";
                } else {
                    r = "failure";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return r;
        }

        public void onPostExecute(String result) //optional
        {
            //  do something after execution
            if (result.equals("success")) {

                bd = new MyOrders_Adapter(MyOrders.this, arrayList);
                r.setAdapter(bd);


            } else {
                AlertDialog.Builder alert = new AlertDialog.Builder(MyOrders.this);
                alert.setTitle("Information");
                alert.setMessage("No Transactions to Display");
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface obj, int x) {
                        MyOrders.this.finish();
                    }
                });
                AlertDialog alertDialog = alert.create();
                alertDialog.show();

            }

        }
    }
}
