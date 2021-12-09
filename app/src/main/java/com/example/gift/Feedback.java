package com.example.gift;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;

public class Feedback extends AppCompatActivity {
    RatingBar ratingBar;
    float myrating=0;
    private Toolbar toolbar;
    String feedback,rate,name;
    Button b1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        ratingBar=(RatingBar)findViewById(R.id.ratingbar);
        b1=(Button)findViewById(R.id.feedback);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Rate us");
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                int rating=(int)v;
                String message=null;
                myrating=ratingBar.getRating();

            }
        });
        b1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                EditText txt_feedback=(EditText)findViewById(R.id.suggestion);
                rate=String.valueOf(myrating);

                feedback= txt_feedback.getText().toString();

                if (feedback.equals("")==false) {
                    DB_Conn obj = new DB_Conn();
                    obj.execute();
                }
                else {
                    txt_feedback.setError("Please enter valid feedback");
                }

            }



        });
    }
    class DB_Conn extends AsyncTask<String,Void,String> {

        @Override
        public String doInBackground(String... arg) //compulsory to implement
        {
            ResultSet rs;
            try {

                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String o_date = df.format(new java.util.Date());

                SharedPreferences sp = getSharedPreferences("pf", Context.MODE_PRIVATE);
                String userid = sp.getString("userid", null);
                Connection con = DB_Connection.get_DBConnection();

                PreparedStatement pst=con.prepareStatement("select * from user where emailid=?");
                pst.setString(1,userid);
                rs = pst.executeQuery();
                rs.next();
                name=rs.getString("name");

                PreparedStatement pst1 = con.prepareStatement("insert into feedback values(?,?,?,?,?)");
                pst1.setString(1, name);
                pst1.setString(2, userid);
                pst1.setString(3, o_date);
                pst1.setString(4,feedback);
                pst1.setString(5,rate);


                pst1.executeUpdate();
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "success";
        }

        @Override
        public void onProgressUpdate(Void... arg0) //optional
        {

        }

        @Override
        public void onPostExecute(String result) //optional
        {
            AlertDialog.Builder alert = new AlertDialog.Builder(Feedback.this);
            if (result.equals("success")) {
                alert.setTitle("Success");
                alert.setMessage("Thank you for your valuable feedback.");
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface obj, int x) {

                        finish();
                    }
                });
                AlertDialog alertDialog = alert.create();
                alertDialog.show();
            }

        }
    }
}
