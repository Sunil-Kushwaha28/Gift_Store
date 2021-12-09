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

public class Forget_password_otp extends AppCompatActivity implements View.OnClickListener {
    String otp,userid,newp,confirm;
    EditText e1,e2,e3;
    Button b1,b2,b3;
    int i=3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password_otp);
        Intent i=getIntent();
        otp=i.getStringExtra("otp");
        userid=i.getStringExtra("userid");

        e1=(EditText)findViewById(R.id.editText);
        e2=(EditText)findViewById(R.id.editText5);
        e3=(EditText)findViewById(R.id.editText6);

        b1=(Button)findViewById(R.id.button12);
        b2=(Button)findViewById(R.id.button13);
        b3=(Button)findViewById(R.id.back);
        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.button12){

            String o=e1.getText().toString();
            if(o.equals("")==false){

                if(o.equals(otp)==false){
                    i--;
                    if(i==0){
                        AlertDialog.Builder alert = new AlertDialog.Builder(this);
                        alert.setTitle("Error");
                        alert.setMessage("Sorry!!!You have crossed the maximum limit.");
                        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface obj, int x) {
                                Intent i=new Intent(Forget_password_otp.this,Forget_password.class);
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
                    e2.setVisibility(View.VISIBLE);
                    e3.setVisibility(View.VISIBLE);
                    b2.setVisibility(View.VISIBLE);
                    e1.setEnabled(false);
                    b1.setEnabled(false);
                }
            }
            else{

                e1.setError("Please enter otp value");
            }
        }
        else{
            newp = e2.getText().toString();
            confirm = e3.getText().toString();
            if(newp.equals("")==false ){
                if (confirm.equals("") == false) {
                    if(newp.equals(confirm)==true){
                        DB_Conn obj = new DB_Conn();
                        obj.execute();
                    }
                    else {
                        e3.setError("Password and confirm password does not match");
                    }
                } else {
                    e3.setError("Value is required");
                }
            } else {
                e2.setError("Valid is required");
            }
        }
        if (v.getId()==R.id.back)
        {
            Intent intent=new Intent(Forget_password_otp.this,Forget_password.class);
            startActivity(intent);
        }

    }
    class DB_Conn extends AsyncTask<Void,Void,String>
    {


        @Override
        public String doInBackground(Void...arg) //compulsory to implement
        {
            String r="";
            try {

                Connection con=DB_Connection.get_DBConnection();


                PreparedStatement pst1 = con.prepareStatement("update login set password=? where userid=?");
                pst1.setString(1, newp);
                pst1.setString(2, userid);

                pst1.executeUpdate();
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


                AlertDialog.Builder alert = new AlertDialog.Builder(Forget_password_otp.this);
                alert.setTitle("Success");
                alert.setMessage("Password reset successfully");
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface obj, int x) {
                        Intent i=new Intent(Forget_password_otp.this,Home.class);
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
