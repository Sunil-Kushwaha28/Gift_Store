package com.example.gift;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Admin_Home extends AppCompatActivity implements View.OnClickListener {
    Button b1,b2,b3,b4,b5,b6,b7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__home);
        b1=(Button)findViewById(R.id.add);
        b2=(Button)findViewById(R.id.view);
        b3=(Button)findViewById(R.id.transaction);
        b4=(Button)findViewById(R.id.feedback);
        b5=(Button)findViewById(R.id.custom);
        b6=(Button)findViewById(R.id.password);
        b7=(Button)findViewById(R.id.logout);

        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);
        b4.setOnClickListener(this);
        b5.setOnClickListener(this);
        b6.setOnClickListener(this);
        b7.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.add){
            Intent i=new Intent(this,Add_Product.class);
            startActivity(i);
        }
        else if(v.getId()==R.id.view){
            Intent i=new Intent(this,ViewProduct_Admin.class);
            startActivity(i);
        }
        else if(v.getId()==R.id.transaction){
            Intent i=new Intent(this,ViewTransaction.class);
            startActivity(i);
        }
        else if(v.getId()==R.id.feedback){
            Intent i=new Intent(this,View_Feedback.class);
            startActivity(i);
        }
        else if(v.getId()==R.id.custom){
            Intent i=new Intent(this,ViewCustomPicture.class);
            startActivity(i);
        }
        else if(v.getId()==R.id.password){
            Intent i=new Intent(this,ChangePassword.class);
            startActivity(i);
        }
        else if(v.getId()==R.id.logout){
            SharedPreferences sp = getSharedPreferences("pf", Context.MODE_PRIVATE);
            SharedPreferences.Editor ed = sp.edit();
            ed.clear();
            ed.commit();

            Intent i=new Intent(this,Login.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }

    }
}
