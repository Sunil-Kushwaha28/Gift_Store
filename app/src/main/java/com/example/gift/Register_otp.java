package com.example.gift;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class Register_otp extends AppCompatActivity implements View.OnClickListener {
    Button b1,b2;
    String otp,fname,mobile,emailid,password;
    EditText t1;
    int i=3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_otp);
        Intent i=getIntent();
        otp=i.getStringExtra("otp");
        mobile=i.getStringExtra("mobile");
        emailid=i.getStringExtra("emailid");
        password=i.getStringExtra("password");
        fname=i.getStringExtra("fname");

        t1=(EditText)findViewById(R.id.editText);

        b1=(Button)findViewById(R.id.verify);
        b2=(Button)findViewById(R.id.back);
        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String o=t1.getText().toString();

        if(o.equals("")==false){

            if(o.equals(otp)==false){
                i--;
                if(i==0){
                    AlertDialog.Builder alert = new AlertDialog.Builder(this);
                    alert.setTitle("Error");
                    alert.setMessage("Sorry!!!You have crossed the maximum limit.");
                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface obj, int x) {
                            Intent i=new Intent(Register_otp.this,Register.class);
                            startActivity(i);

                        }
                    });
                    AlertDialog alertDialog = alert.create();
                    alertDialog.show();

                }
                else{
                    AlertDialog.Builder alert = new AlertDialog.Builder(this);
                    alert.setTitle("Error");
                    alert.setMessage("Incorrect OTP entered. Try again" +i+" attempts left");
                    alert.setPositiveButton("OK", null);
                    AlertDialog alertDialog = alert.create();
                    alertDialog.show();
                }

            }
            else{
                DB_Conn obj=new DB_Conn();
                obj.execute();
            }

        }
        else{

            t1.setError("Please enter otp value");
        }
        if (view.getId()==R.id.back)
        {
            Intent intent=new Intent(Register_otp.this,Register.class);
            startActivity(intent);
        }


    }
    class DB_Conn extends AsyncTask<Void,Void,String>
    {

        @Override
        public String doInBackground(Void... arg) //compulsory to implement
        {
            String r="";
            try {

                Connection con = DB_Connection.get_DBConnection();

                PreparedStatement pst = con.prepareStatement("insert into user values(?,?,?)");
                pst.setString(1, fname);
                pst.setString(2, emailid);
                pst.setString(3, mobile);
                pst.executeUpdate();

                pst = con.prepareStatement("insert into login values(?,?)");
                pst.setString(1, emailid);
                pst.setString(2, password);
                pst.executeUpdate();
                con.close();
                r="success";
            }
            catch(Exception e)
            {
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
                AlertDialog.Builder alert = new AlertDialog.Builder(Register_otp.this);
                alert.setTitle("Success");
                alert.setMessage("Registration successfull!!!");
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface obj, int x) {

                        Intent i=new Intent(Register_otp.this,Login.class);
                        startActivity(i);

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
