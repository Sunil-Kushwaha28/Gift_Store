package com.example.gift;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

public class View_Feedback extends AppCompatActivity {
    RecyclerView r;
    View_Feedback_Adapter bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__feedback);
        r = (RecyclerView) findViewById(R.id.view2);
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
                Connection con = DB_Connection.get_DBConnection();
                pst = con.prepareStatement("select * from feedback order by date desc");
                rs = pst.executeQuery();
                while (rs.next()) {

                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("name", rs.getString("name"));
                    hashMap.put("date", rs.getString("date"));
                    hashMap.put("feedback", rs.getString("suggestion"));
                    hashMap.put("rate", rs.getString("rating"));
                    arrayList.add(hashMap);
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

                bd = new View_Feedback_Adapter(View_Feedback.this, arrayList);
                r.setAdapter(bd);


            } else {
                AlertDialog.Builder alert = new AlertDialog.Builder(View_Feedback.this);
                alert.setTitle("Information");
                alert.setMessage("No feedback to display");
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface obj, int x) {
                        View_Feedback.this.finish();
                    }
                });
                AlertDialog alertDialog = alert.create();
                alertDialog.show();

            }


        }

    }
}
