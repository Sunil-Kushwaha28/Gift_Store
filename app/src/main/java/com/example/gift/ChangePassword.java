package com.example.gift;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ChangePassword extends AppCompatActivity implements View.OnClickListener {
    EditText txt_old,txt_new,txt_confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        Button b1=(Button)findViewById(R.id.update);
        b1.setOnClickListener(this);

        txt_old = (EditText) findViewById(R.id.editText1);
        txt_new = (EditText) findViewById(R.id.editText2);
        txt_confirm = (EditText) findViewById(R.id.editText3);
    }

    @Override
    public void onClick(View view) {
        String old = txt_old.getText().toString();
        String newp = txt_new.getText().toString();
        String confirm = txt_confirm.getText().toString();

        if(old.equals("")==false ) {
            if(newp.equals("")==false ){
                if (confirm.equals("") == false) {
                    if(newp.equals(confirm)){
                        DB_Conn obj = new DB_Conn();
                        obj.execute(old, newp);
                    }
                    else {
                        txt_confirm.setError("Password and Confirm password do not match");
                    }

                } else {
                    txt_confirm.setError("Value is required");
                }
            } else {
                txt_new.setError("Valid is required");
            }
        }
        else {
            txt_old.setError("Value is required");
        }

    }
    class DB_Conn extends AsyncTask<String,Void,String>
    {


        @Override
        public String doInBackground(String...arg) //compulsory to implement
        {
            String r="";
            try {
                SharedPreferences sp = getSharedPreferences("pf", Context.MODE_PRIVATE);

                String userid=sp.getString("userid",null);

                Connection con=DB_Connection.get_DBConnection();

                PreparedStatement pst = con.prepareStatement("select * from login where userid=? and password=?");
                pst.setString(1, userid);
                pst.setString(2, arg[0]);
                ResultSet rs = pst.executeQuery();
                if (rs.next() == true) {

                    PreparedStatement pst1 = con.prepareStatement("update login set password=? where userid=?");
                    pst1.setString(1, arg[1]);
                    pst1.setString(2, userid);

                    pst1.executeUpdate();
                    con.close();
                    r="success";



                }
                else
                {
                    r="failure";
                }
                con.close();
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
            if(result.equals("success"))
            {

                AlertDialog.Builder alert = new AlertDialog.Builder(ChangePassword.this);
                alert.setTitle("Success");
                alert.setMessage("Password changed successfully");
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface obj, int x) {
                        Intent i=new Intent(ChangePassword.this,Home.class);
                        startActivity(i);

                    }
                });
                AlertDialog alertDialog = alert.create();
                alertDialog.show();
            }

            else
            {
                AlertDialog.Builder alert = new AlertDialog.Builder(ChangePassword.this);
                alert.setTitle("Error");
                alert.setMessage("Old password does not match");
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface obj, int x) {


                    }
                });
                AlertDialog alertDialog = alert.create();
                alertDialog.show();
            }
        }


    }
}
