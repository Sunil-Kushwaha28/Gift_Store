package com.example.gift;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;
import java.util.Random;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Register extends AppCompatActivity {
    String fname, emailid, mobile, password;
    int i=1;
    ProgressDialog pd;
    EditText txt_fname,txt_emailid,txt_mobile,txt_password,txt_confirmpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        txt_fname = (EditText) findViewById(R.id.editText);
        txt_emailid = (EditText) findViewById(R.id.editText1);
        txt_mobile = (EditText) findViewById(R.id.editText2);
        txt_password = (EditText) findViewById(R.id.editText3);
        Button b1 = (Button) findViewById(R.id.register);
        Button b2 = (Button) findViewById(R.id.return_login);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Register.this,Login.class);
                startActivity(i);
            }
        });


        b1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                fname = txt_fname.getText().toString();
                emailid = txt_emailid.getText().toString();
                mobile = txt_mobile.getText().toString();
                password = txt_password.getText().toString();


                if (fname.equals("") == false) {
                    if (emailid.equals("") == false && android.util.Patterns.EMAIL_ADDRESS.matcher(emailid).matches()) {
                        if (mobile.equals("") == false) {
                            if (password.equals("") == false) {


                                        DB_Conn obj=new DB_Conn();
                                        obj.execute();

                                        pd = new ProgressDialog(Register.this);
                                        pd.setMessage("Sending OTP on email..Please wait.."); // Setting Message
                                        pd.setTitle("Registration"); // Setting Title
                                        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
                                        pd.show(); // Display Progress Dialog
                                        pd.setCancelable(false);

                            } else {
                                txt_password.setError("Please enter password");
                            }
                        } else {
                            txt_mobile.setError("Please enter mobile no");
                        }
                    }
                    else
                        {
                        txt_emailid.setError("Please enter emailid in correct format");
                    }
                }
                else
                    {
                    txt_fname.setError("Please enter name");
                }
            }
        });
    }
    class DB_Conn extends AsyncTask<String, Void, String> {


        String otp = "";
        AlertDialog alertDialog;

        @Override
        public String doInBackground(String... arg) //compulsory to implement
        {
            String result = "";

            try {
                Connection con = DB_Connection.get_DBConnection();
                PreparedStatement pst = con.prepareStatement("select * from login where userid=?");
                pst.setString(1,emailid);
                ResultSet rs = pst.executeQuery();

                if (rs.next() == false) {

                    Random r = new Random();
                    otp = r.nextInt(9) + "" + r.nextInt(9) + "" + r.nextInt(9) + "" + r.nextInt(9);
                    Properties p=new Properties();
                    p.put("mail.smtp.starttls.enable","true");//here smtp donot get start security gets started
                    p.put("mail.smtp.auth","true");
                    p.put("mail.smtp.host","173.194.202.108");
                    p.put("mail.smtp.port","587");

                    Session s= Session.getDefaultInstance(p,new Authenticator()
                    {
                        protected PasswordAuthentication getPasswordAuthentication()
                        {
                            return new PasswordAuthentication(DB_Connection.SENDERS_EMAILID,DB_Connection.SENDERS_PASSWORD);
                        }
                    });


                    MimeMessage msg=new MimeMessage(s);//multipurpose internet mail extension mime
                    msg.setFrom(new InternetAddress(DB_Connection.SENDERS_EMAILID));
                    msg.addRecipient(Message.RecipientType.TO,new InternetAddress(emailid));//here type recipient email id
                    msg.setSubject("OTP for registration");
                    String m="Greeting,\n Your OTP for account activation is "+otp;
                    //msg.setText(m,"UTF-8","html");
                    msg.setContent(m, "text/html; charset=utf-8");
                    Transport.send(msg);


                    result = "success";
                } else {
                    result = "failed";
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        public void onProgressUpdate(Void... arg0) //optional
        {

        }

        @Override
        public void onPostExecute(String result) //optional
        {
            pd.dismiss();
            if (result.equals("success")) {
                Intent i=new Intent(Register.this,Register_otp.class);
                i.putExtra("otp",otp);
                i.putExtra("fname",fname);
                i.putExtra("emailid",emailid);
                i.putExtra("mobile",mobile);
                i.putExtra("password",password);

                startActivity(i);
            }
            else{
                AlertDialog.Builder alert = new AlertDialog.Builder(Register.this);
                alert.setTitle("Error");
                alert.setMessage("This emailid is already registered. Try a different one");
                alert.setPositiveButton("OK", null);
                AlertDialog alertDialog = alert.create();
                alertDialog.show();
                txt_emailid.requestFocus();
            }


        }

        @Override
        public void onPreExecute() //optional
        {
            // do something before start
        }

    }
    }


