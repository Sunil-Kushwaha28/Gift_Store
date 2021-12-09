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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

public class ViewTransaction extends AppCompatActivity {
    RecyclerView r;
    ViewTransaction_Adapter bd;
    String oid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_transaction);
        DB_Conn obj = new DB_Conn();
        obj.execute();
        r=(RecyclerView)findViewById(R.id.rect);
        r.setLayoutManager(new LinearLayoutManager(this));
    }
    class DB_Conn extends AsyncTask<String,Void,String>
    {

        ArrayList<HashMap<String,String>> arrayList=new ArrayList<>();

        @Override
        public String doInBackground(String...arg) //compulsory to implement
        {
            String r="";
            String t="";
            PreparedStatement pst;
            ResultSet rs;
            try {

                Connection con=DB_Connection.get_DBConnection();

                    pst = con.prepareStatement("select * from orders");
                    rs = pst.executeQuery();
                while(rs.next()) {
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("oid", rs.getString("orderno"));
                    hashMap.put("odate", rs.getString("orderdate"));
                    hashMap.put("address", rs.getString("address"));
                    hashMap.put("mode", rs.getString("payment_mode"));
                    hashMap.put("total", rs.getString("grand_total"));
                    hashMap.put("userid", rs.getString("userid"));
                    oid = rs.getString("orderno");
                    arrayList.add(hashMap);
                }


                pst.close();
                rs.close();


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
        public void onProgressUpdate(Void...arg0) //optional
        {

        }
        @Override
        public void onPostExecute(String result) //optional
        {
            //  do something after execution
            if(result.equals("success"))
            {

                bd=new ViewTransaction_Adapter(ViewTransaction.this,arrayList);

                r.setAdapter(bd);


            }
            else{
                AlertDialog.Builder alert = new AlertDialog.Builder(ViewTransaction.this);
                alert.setTitle("Information");
                alert.setMessage("No Transactions to Display");
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface obj, int x) {
                        ViewTransaction.this.finish();
                    }
                });
                AlertDialog alertDialog = alert.create();
                alertDialog.show();

            }


        }


        @Override
        public void onPreExecute() //optional
        {
            // do something before start
        }

    }


}
