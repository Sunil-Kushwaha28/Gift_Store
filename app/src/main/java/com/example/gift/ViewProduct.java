package com.example.gift;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

public class ViewProduct extends AppCompatActivity {
    RecyclerView r;
    View_Product_Adapter bd;
    String category;
    GridLayoutManager gridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product);
        r = (RecyclerView) findViewById(R.id.view);
        r.setLayoutManager(new LinearLayoutManager(this));
        gridLayoutManager=new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);
        category=getIntent().getStringExtra("category");
        DB_Conn obj = new DB_Conn();
        obj.execute();
    }
    class DB_Conn extends AsyncTask<String, Void, String> {

        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
        ArrayList<HashMap<String, Bitmap>> arrayList1 = new ArrayList<>();

        @Override
        public String doInBackground(String... arg) //compulsory to implement
        {
            String r = "";
            PreparedStatement pst;
            ResultSet rs;
            Bitmap bitmap;

            try {
                Connection con = DB_Connection.get_DBConnection();
                pst = con.prepareStatement("select * from product where category=?");
                pst.setString(1,category);
                rs = pst.executeQuery();
                while (rs.next()) {
                    HashMap<String, String> hashMap = new HashMap<>();
                    HashMap<String, Bitmap> hashMap1=new HashMap<>();
                    hashMap.put("pno", rs.getString("pno"));
                    hashMap.put("name", rs.getString("name"));
                    hashMap.put("price", rs.getString("price"));
                    hashMap.put("category", rs.getString("category"));
                    byte b[] = rs.getBytes("pic");
                    bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                    hashMap1.put("image",bitmap);
                    arrayList.add(hashMap);
                    arrayList1.add(hashMap1);
                }
                pst.close();
                rs.close();


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

        @Override
        public void onProgressUpdate(Void... arg0) //optional
        {

        }

        @Override
        public void onPostExecute(String result) //optional
        {
            //  do something after execution
            if (result.equals("success")) {

                bd = new View_Product_Adapter(ViewProduct.this, arrayList,arrayList1);
                r.setLayoutManager(gridLayoutManager);
                r.setAdapter(bd);


            } else {
                AlertDialog.Builder alert = new AlertDialog.Builder(ViewProduct.this);
                alert.setTitle("Information");
                alert.setMessage("No Product to display");
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface obj, int x) {
                        ViewProduct.this.finish();
                    }
                });
                AlertDialog alertDialog = alert.create();
                alertDialog.show();

            }


        }

    }
}
