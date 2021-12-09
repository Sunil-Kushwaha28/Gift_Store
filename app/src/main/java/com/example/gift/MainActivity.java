package com.example.gift;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app
             * logo / company
             */

            @Override
            public void run() {
                SharedPreferences sp = getSharedPreferences("pf", Context.MODE_PRIVATE);
                boolean l = sp.getBoolean("loggedin", false);
                String userid = sp.getString("userid", null);
                if (l == true) {
                    if (userid.equalsIgnoreCase("admin@giftstore.com")) {
                        Intent i = new Intent(MainActivity.this, Admin_Home.class);
                        startActivity(i);
                    }

                    else  {
                        Intent i = new Intent(MainActivity.this, Home.class);
                        startActivity(i);
                    }
                }
                else {
                    Intent i = new Intent(MainActivity.this, Login.class);
                    startActivity(i);
                }

                // close this activity
                finish();
            }
        }, 3000);

    }
}
