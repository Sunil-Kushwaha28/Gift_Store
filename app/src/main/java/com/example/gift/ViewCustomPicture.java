package com.example.gift;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

public class ViewCustomPicture extends AppCompatActivity {
    RecyclerView r;
    ViewCustomPicture_Adapter bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_custom_picture);
        r=(RecyclerView)findViewById(R.id.recyle);
        r.setLayoutManager(new LinearLayoutManager(this));

        DB_Conn obj = new DB_Conn();
        obj.execute();
    }
    class DB_Conn extends AsyncTask<String,Void,String>
    {

        ArrayList<HashMap<String,String>> arrayList=new ArrayList<>();

        @Override
        public String doInBackground(String...arg) //compulsory to implement
        {
            String r="";
            PreparedStatement pst;
            ResultSet rs;
            try {
                Connection con=DB_Connection.get_DBConnection();
                pst = con.prepareStatement("select * from custom_photo");
                rs = pst.executeQuery();
                while(rs.next()) {
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("no", rs.getString("no"));
                    hashMap.put("name", rs.getString("pro_name"));
                    hashMap.put("userid", rs.getString("userid"));
                    hashMap.put("date", rs.getString("date"));
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

                bd=new ViewCustomPicture_Adapter(ViewCustomPicture.this,arrayList);
                r.setAdapter(bd);


            }
            else{
                AlertDialog.Builder alert = new AlertDialog.Builder(ViewCustomPicture.this);
                alert.setTitle("Information");
                alert.setMessage("No Picture to Display");
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface obj, int x) {
                        ViewCustomPicture.this.finish();
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
