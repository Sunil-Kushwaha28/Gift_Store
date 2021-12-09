package com.example.gift;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.Session;


public class Forget_password extends AppCompatActivity implements View.OnClickListener {
    ProgressDialog pd;
    EditText txt_userid;
    Button b1,b2;
    String u;
    int i=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        txt_userid=(EditText)findViewById(R.id.editText);
        b1=(Button)findViewById(R.id.back);
        b2=(Button)findViewById(R.id.button);
        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.button){

            u=txt_userid.getText().toString().trim();

            if(u.equals("")==false && android.util.Patterns.EMAIL_ADDRESS.matcher(u).matches()) {

                DB_Conn obj = new DB_Conn();
                obj.execute(u);

                pd = new ProgressDialog(Forget_password.this);
                pd.setMessage("Validating emailid and sending OTP..Please wait.."); // Setting Message
                pd.setTitle("Password Reset"); // Setting Title
                pd.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
                pd.show(); // Display Progress Dialog
                pd.setCancelable(false);
            }
            else {
                txt_userid.setError("Either emailid is not entered or entered in invalid format");
            }
        }
        if (view.getId()==R.id.back)
        {
            Intent intent=new Intent(Forget_password.this,Login.class);
            startActivity(intent);
        }

    }
    class DB_Conn extends AsyncTask<String,Void,String>
    {
        String u,otp="";
        @Override
        public String doInBackground(String...arg) //compulsory to implement
        {
            String r="";
            try {

                Connection con=DB_Connection.get_DBConnection();

                PreparedStatement pst = con.prepareStatement("select * from login where userid=?");
                pst.setString(1, arg[0]);
                ResultSet rs = pst.executeQuery();

                if (rs.next() == true) {
                    u=arg[0];
                    Random r1 = new Random();
                    otp = r1.nextInt(9) + "" + r1.nextInt(9) + "" + r1.nextInt(9) + "" + r1.nextInt(9);
                    Properties p=new Properties();
                    p.put("mail.smtp.starttls.enable","true");//here smtp donot get start security gets started
                    p.put("mail.smtp.auth","true");
                    p.put("mail.smtp.host","173.194.202.108");
                    p.put("mail.smtp.port","587");

                    Session s= Session.getDefaultInstance(p,new Authenticator()
                    {
                        protected javax.mail.PasswordAuthentication getPasswordAuthentication()
                        {
                            return new PasswordAuthentication(DB_Connection.SENDERS_EMAILID,DB_Connection.SENDERS_PASSWORD);
                        }
                    });

                    MimeMessage msg=new MimeMessage(s);//multipurpose internet mail extension mime
                    msg.setFrom(new InternetAddress(DB_Connection.SENDERS_EMAILID));
                    msg.addRecipient(Message.RecipientType.TO,new InternetAddress(u));//here type recipient email id
                    msg.setSubject("OTP for password reset");
                    String m="Greeting,\n Your OTP for password reset is "+otp;
                    msg.setContent(m, "text/html; charset=utf-8");
                    Transport.send(msg);
                    r = "success";

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
        public void onProgressUpdate(Void...arg0)
        {

        }
        @Override
        public void onPostExecute(String result)
        {
            if(result.equals("success"))
            {
                Intent i=new Intent(Forget_password.this,Forget_password_otp.class);
                i.putExtra("userid",u);
                i.putExtra("otp",otp);
                startActivity(i);
            }
            else
            {
                AlertDialog.Builder alert = new AlertDialog.Builder(Forget_password.this);
                alert.setTitle("Error");
                alert.setMessage("Entered userid does not exist");
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface obj, int x) {

                    }
                });
                AlertDialog alertDialog = alert.create();
                alertDialog.show();
            }
        }
        @Override
        public void onPreExecute()
        {

        }
    }
}
