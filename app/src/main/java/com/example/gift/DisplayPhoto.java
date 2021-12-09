package com.example.gift;

import androidx.appcompat.app.AppCompatActivity;

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
import java.text.SimpleDateFormat;

public class DisplayPhoto extends AppCompatActivity implements View.OnClickListener{
    TextView t1,t2,t3;
    ImageView iv;
    Button b1;
    String no,pro_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_photo);

        no=getIntent().getStringExtra("no");
        t1=(TextView)findViewById(R.id.textView14);
        t2=(TextView)findViewById(R.id.textView17);
        t3=(TextView)findViewById(R.id.name);

        b1=(Button)findViewById(R.id.button18);
        iv=(ImageView)findViewById(R.id.imageView3);
        b1.setOnClickListener(this);

        DB_Conn obj=new DB_Conn();
        obj.execute();
    }

    @Override
    public void onClick(View view) {
        finish();
    }
    class DB_Conn extends AsyncTask<String,Void,String>
    {
        Bitmap bitmap;
        String userid,date;

        @Override
        public String doInBackground(String...arg) //compulsory to implement
        {
            String r="";
            PreparedStatement pst;
            ResultSet rs;
            try {
                Connection con=DB_Connection.get_DBConnection();


                pst = con.prepareStatement("select * from custom_photo where no=?");
                pst.setString(1,no);
                rs = pst.executeQuery();
                rs.next();
                byte b[]=rs.getBytes("pic");
                bitmap= BitmapFactory.decodeByteArray(b, 0, b.length);
                userid=rs.getString("userid");
                pro_name=rs.getString("pro_name");
                date=new SimpleDateFormat("dd/MM/yyyy").format(new SimpleDateFormat("yyyy-MM-dd").parse(rs.getString("date")));
                pst.close();
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
            iv.setImageBitmap(bitmap);
            t1.setText(userid);
            t2.setText(date);
            t3.setText(pro_name);

        }


        @Override
        public void onPreExecute() //optional
        {
            // do something before start
        }

    }
}
